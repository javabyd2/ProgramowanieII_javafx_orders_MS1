package com.sdabyd2.javafx.orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdabyd2.javafx.orders.model.Order;
import com.sdabyd2.javafx.orders.model.Product;
import com.sdabyd2.javafx.orders.model.State;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderController {

    public static List<Product> products = new ArrayList<>();
    public static Order order = new Order();
    public static State state = State.NONE;
    private MainController mainController;
    private ListView<String> listView;

    @FXML
    Button buttonAddItem;
    @FXML
    Button buttonEditItem;
    @FXML
    Button buttonDeleteItem;
    @FXML
    Button buttonCloseApp;
    @FXML
    Button buttonSave;
    @FXML
    Button buttonLoad;
    @FXML
    Button buttonProducts;
    @FXML
    Label labelTotalTitle;
    @FXML
    Label labelTotal;
    @FXML
    Label labelTotalWithRebate;
    @FXML
    Group groupItems;

    public OrderController() {
    }

    @FXML
    void initialize() throws IOException {
        if (products.size() == 0) {
            loadProductsFromFile();
        }
        if (order.getItems().size() == 0) {
            setOrder(loadOrderFromFile("order"));
        }
        presentOrder();
        buttonEditItem.setDisable(true);
        buttonDeleteItem.setDisable(true);
    }

    // prosta metoda odczytu jsona
    public static void loadProductsFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Product[] productsArray = objectMapper.readValue(new File("products.json"), Product[].class);
        products.clear();
        products.addAll(Arrays.asList(productsArray));
    }

    public static void saveProductsToFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File filename = new File("products.json");
        filename.createNewFile();
        mapper.writeValue(filename, products);
    }

    // dłuższa metoda odczytu jsona
    public static Order loadOrderFromFile(String fileName) {
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = new Order();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName+".json"));
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                order = objectMapper.readValue(json, Order.class);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Wystąpił problem z odczytem pliku zamówienia. "+e.getMessage());
        }
        return order;
    }

    public static void saveOrderToFile(Order order, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File filename = new File(fileName+".json");
        filename.createNewFile();
        mapper.writeValue(filename, order);
    }

    @FXML
    public void onActionButtonAddItem(ActionEvent actionEvent) {
        state = State.ADD;
        callItemScreen();
    }

    @FXML
    public void onActionButtonEditItem(ActionEvent actionEvent) {
        state = State.EDIT;
        callItemScreen();
    }

    @FXML
    public void onActionButtonDeleteItem(ActionEvent actionEvent) {
        if (ItemController.editingPosition > -1) {
            order.getItems().remove((int) ItemController.editingPosition);
            //listView.getItems().remove((int) ItemController.editingPosition);
            presentOrder();
        }
    }

    @FXML
    public void onActionButtonCloseApp(ActionEvent actionEvent) throws IOException {
/*
        products.clear();
        products.add(new Product("Chleb", 3.5));
        products.add(new Product("Bagietka", 2.3));
        products.add(new Product("Cukier", 2.1));
        saveProductsToFile();
//*/
        Platform.exit();
    }

    @FXML
    public void onActionButtonSave(ActionEvent actionEvent) {
        try {
            saveOrderToFile(getOrder(), "order");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onActionButtonLoad(ActionEvent actionEvent) {
        setOrder(loadOrderFromFile("order"));
        presentOrder();
    }

    @FXML
    public void onActionButtonProducts(ActionEvent actionEvent) {
        callProductsScreen();
    }

    public void callProductsScreen() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ProductsScreen.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProductsController productsController = loader.getController();
        productsController.setMainController(mainController);
        mainController.setScreen(anchorPane);
    }

    private void callItemScreen() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ItemScreen.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ItemController itemController = loader.getController();
        itemController.setMainController(mainController);
        mainController.setScreen(anchorPane);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    private void presentOrder() {
        listView = new ListView<String>(FXCollections.observableArrayList(
                Order.formatToList(order.getItems())));
        listView.setMinWidth(565);
        listView.setMaxHeight(180);
        listView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {
                    public void changed(
                        ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                        ItemController.editingPosition = -1;
                        for (int i = 0; i < order.getItems().size(); i++) {
                            if (newValue.contains(order.getItems().get(i).getProductName())) {
                                ItemController.editingPosition = i;
                                break;
                            }
                        }
                        buttonEditItem.setDisable(ItemController.editingPosition == -1);
                        buttonDeleteItem.setDisable(ItemController.editingPosition == -1);
                    }
                });

        listView.setCellFactory(cell -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item);
                        setFont(Font.font("Monospaced", 12));
                    }
                }
            };
        });

        groupItems.getChildren().addAll(listView);

        labelTotal.setText(String.format("%1.2f zł", order.countValueOfOrder()));
        labelTotalWithRebate.setText(String.format("%1.2f zł", order.countValueOfOrderWithRebate()));
    }
}
