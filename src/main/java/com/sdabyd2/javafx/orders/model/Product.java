package com.sdabyd2.javafx.orders.model;

import com.sdabyd2.javafx.orders.controller.MainController;
import com.sdabyd2.javafx.orders.controller.OrderController;

import java.util.List;

public class Product {
    private String name;
    private Double price = 0.00;

    public Product() {
    }

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public static String[] formatToList(List<Product> list) {
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            //result[i] = String.format("%s %1.2f", list.get(i).getName(), list.get(i).getPrice())+" zł";
            result[i] = MainController.padRight(list.get(i).getName(), 60)+MainController.padLeft(String.format("%1.2f", list.get(i).getPrice())+" zł", 10);
        }
        return result;
    }

    public static void editProduct(List<Product> list, int index, Product productNew) {
        Product product = list.get(index);
        product.setName(productNew.getName());
        product.setPrice(productNew.getPrice());
    }
}
