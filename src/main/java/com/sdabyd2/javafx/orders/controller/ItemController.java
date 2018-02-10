package com.sdabyd2.javafx.orders.controller;

import com.sdabyd2.javafx.orders.model.Product;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.sdabyd2.orders.orders.model.Item;

public class ItemController {

    private Item item = new Item();
    private MainController mainController;
    private Product product = null;
    public static Integer editingPosition = 0;

    @FXML
    Label textFieldTitle;
    @FXML
    ComboBox comboBoxProduct;
    @FXML
    Spinner<Integer> spinnerQuantity;
    @FXML
    TextField textFieldPrice;
    @FXML
    TextField textFieldValue;
    @FXML
    TextField textFieldRebat;
    @FXML
    TextField textFieldValueWithRebate;
    @FXML
    Button buttonApply;
    @FXML
    Button buttonCancel;

    @FXML
    void initialize() {
        comboBoxProduct.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {

                    public void changed(
                            ObservableValue<? extends String> observable,
                            String oldValue, String newValue) {
                        for (int i = 0; i < OrderController.products.size(); i++) {
                            if (OrderController.products.get(i).getName().equals(newValue)) {
                                product = OrderController.products.get(i);
                                textFieldPrice.setText(String.format("%1.2f zł", product.getPrice()));
                                item.setProductName(newValue);
                                item.setProductPrice(product.getPrice());
                                countValues();
                            }
                        }
                    }
                });

        comboBoxProduct.setItems(FXCollections.observableArrayList(
                Item.formatToList(OrderController.products)));

        switch (OrderController.state) {
            case ADD:
                textFieldTitle.setText("NOWY PRZEDMIOT ZAMÓWIENIA");
                initializeSpinner(1, 100, 0);
                break;
            case EDIT:
                textFieldTitle.setText("MODYFIKACJA DANYCH PRZEDMIOTU ZAMÓWIENIA");
                copyItem(OrderController.order.getItems().get(editingPosition));
                comboBoxProduct.setPromptText(item.getProductName());
                textFieldPrice.setText(String.format("%1.2f zł", item.getProductPrice()));
                initializeSpinner(1, 100, item.getProductCount());
                countValues();
                break;
        }
    }

    private void initializeSpinner(int min, int max, int value) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, value);
        spinnerQuantity.setValueFactory(valueFactory);

        spinnerQuantity.getValueFactory().valueProperty().addListener((ov, oldValue, newValue) -> {
            item.setProductCount(newValue);
            countValues();
        });
    }

    private void countValues() {
        textFieldValue.setText(String.format("%1.2f zł", item.getProductPrice() * item.getProductCount()));
        textFieldRebat.setText(item.giveRebate());
        textFieldValueWithRebate.setText(String.format("%1.2f zł", item.countValueOfItemIncludingRebate()));
    }

    @FXML
    public void onActionButtonApply(ActionEvent actionEvent) {
        if (comboBoxProduct.getValue() != null)
            item.setProductName(comboBoxProduct.getValue().toString());
        item.setProductCount(spinnerQuantity.getValue());
        switch (OrderController.state) {
            case ADD:
                OrderController.order.addItem(item);
                break;
            case EDIT:
                OrderController.order.editItem(editingPosition, item);
                break;
        }
        mainController.loadMainScreen();
    }

    @FXML
    public void onActionButtonCancel(ActionEvent actionEvent) {
        mainController.loadMainScreen();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void copyItem(Item item) {
        this.item.setProductName(item.getProductName());
        this.item.setProductCount(item.getProductCount());
        this.item.setProductPrice(item.getProductPrice());
    }
}
