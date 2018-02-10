package com.sdabyd2.javafx.orders.controller;

import com.sdabyd2.javafx.orders.model.Product;
import com.sdabyd2.javafx.orders.model.State;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;

public class ProductsController {

    private MainController mainController;
    public static State state = State.NONE;

    @FXML
    Button buttonLoad;
    @FXML
    Button buttonSave;
    @FXML
    Button buttonAddProduct;
    @FXML
    Button buttonEditProduct;
    @FXML
    Button buttonDeleteProduct;
    @FXML
    Button buttonClose;
    @FXML
    Group groupProducts;
    @FXML
    ListView<String> listViewProducts;
    @FXML
    HBox hBoxProducts;

    @FXML
    void initialize() {
        presentProduct();
    }

    @FXML
    public void onActionButtonSave() throws IOException{
        OrderController.saveProductsToFile();
    }

    @FXML
    public void onActionButtonLoad() throws IOException{
        OrderController.loadProductsFromFile();
        presentProduct();
    }

    private void presentProduct() {
        ListView<String> listView = new ListView<String>(FXCollections.observableArrayList(
                Product.formatToList(OrderController.products)));
        listView.setMinWidth(580);
        listView.setMaxHeight(180);
        listView.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {

                    public void changed(
                        ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                        ProductController.editingProduct = -1;
                        for (int i = 0; i < OrderController.products.size(); i++) {
                            if (newValue.contains(OrderController.products.get(i).getName())) {
                                ProductController.editingProduct = i;
                                break;
                            }
                        }
                        buttonEditProduct.setDisable(ProductController.editingProduct == -1);
                        buttonDeleteProduct.setDisable(ProductController.editingProduct == -1);
                    }
                });

        groupProducts.getChildren().addAll(listView);

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
    }

    @FXML
    public void onActionButtonAddItem() {
        state = State.ADD;
        callProductScreen();
    }

    @FXML
    public void onActionButtonEditItem() {
        state = State.EDIT;
        callProductScreen();
    }

    @FXML
    public void onActionButtonDeleteItem() {
        if (ItemController.editingPosition > -1) {
            OrderController.products.remove((int) ProductController.editingProduct);
            //listView.getItems().remove((int) ItemController.editingPosition);
            presentProduct();
        }
    }

    @FXML
    public void onActionButtonClose() {
        mainController.loadMainScreen();
    }

    private void callProductScreen() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ProductScreen.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProductController productController = loader.getController();
        productController.setMainController(mainController);
        mainController.setScreen(anchorPane);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
