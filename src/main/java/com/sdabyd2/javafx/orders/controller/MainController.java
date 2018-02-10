package com.sdabyd2.javafx.orders.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane anchorPaneMain;

    @FXML
    public void initialize() {
        loadMainScreen();
    }

    public void loadMainScreen() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/OrderScreen.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        OrderController orderController = loader.getController();
        orderController.setMainController(this);
        setScreen(anchorPane);
    }

    public void setScreen(AnchorPane anchorPane) {
        anchorPaneMain.getChildren().clear();
        anchorPaneMain.getChildren().add(anchorPane);
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }
}
