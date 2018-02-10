package com.sdabyd2.javafx.orders.controller;

import com.sdabyd2.javafx.orders.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ProductController {

    private MainController mainController;
    public static Integer editingProduct = -1;
    private Product product = new Product();

    @FXML
    Label textFieldTitle;
    @FXML
    TextField textFieldName;
    @FXML
    TextField textFieldValue;
    @FXML
    Button buttonApply;
    @FXML
    Button buttonCancel;

    @FXML
    void initialize() {
        switch (ProductsController.state) {
            case ADD:
                textFieldTitle.setText("NOWY PRZEDMIOT ZAMÓWIENIA");
                break;
            case EDIT:
                textFieldTitle.setText("MODYFIKACJA DANYCH PRZEDMIOTU ZAMÓWIENIA");
                copyProduct(OrderController.products.get(editingProduct));
                textFieldName.setText(product.getName());
                textFieldValue.setText(String.format("%1.2f", product.getPrice()));
                break;
        }
    }

    @FXML
    public void onActionButtonApply(ActionEvent actionEvent) {
        product.setName(textFieldName.getText());
        product.setPrice(Double.parseDouble(textFieldValue.getText().replace(',', '.')));
        switch (ProductsController.state) {
            case ADD:
                OrderController.products.add(product);
                try {
                    OrderController.saveProductsToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                callBackProductsScreen();
                break;
            case EDIT:
                Product.editProduct(OrderController.products, editingProduct, product);
                callBackProductsScreen();
                break;
        }
    }

    @FXML
    public void onActionButtonCancel(ActionEvent actionEvent) {
        callBackProductsScreen();
    }

    public void callBackProductsScreen() {
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

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void copyProduct(Product productToCopy) {
        product.setName(productToCopy.getName());
        product.setPrice(productToCopy.getPrice());
    }
}
