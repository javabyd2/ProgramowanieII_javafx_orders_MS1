package com.sdabyd2.orders.orders.model;

import com.sdabyd2.javafx.orders.controller.MainController;
import com.sdabyd2.javafx.orders.model.Product;

import java.io.Serializable;
import java.util.List;

public class Item implements Serializable {
    private String productName;
    private int productCount;
    private double productPrice;

    public Item(String productName, int productCount, double productPrice) {
        this.productName = productName;
        this.productCount = productCount;
        this.productPrice = productPrice;
    }

    public Item() {
    }

    public double countValueOfItem() {
        return productCount * productPrice;
    }

    public double countValueOfItemIncludingRebate() {
        Double rebatedValue = countValueOfItem();
        if (this.productCount > 5 && this.productCount <= 10) {
            rebatedValue *= 1-.05;
        } else
            if (this.productCount > 10 && this.productCount <= 20) {
                rebatedValue *= 1 - .1;
            } else {
                if (this.productCount > 20) {
                    rebatedValue *= 1 - .15;
            }
        }
        return rebatedValue;
    }

    public String giveRebate() {
        Integer rebatValue = 0;
        if (this.productCount > 5 && this.productCount <= 10) {
            rebatValue = 5;
        } else
        if (this.productCount > 10 && this.productCount <= 20) {
            rebatValue = 10;
        } else {
            if (this.productCount > 20) {
                rebatValue = 15;
            }
        }
        return Integer.toString(rebatValue)+"%";
    }

    public static String[] formatToList(List<Product> list) {
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i).getName();
        }
        return result;
    }

    public String toString() {
        return MainController.padRight(productName, 20)
                +MainController.padLeft(String.format("%1.2f", productPrice), 10)+" zł"
                +MainController.padLeft(String.format("%d", productCount), 4)+" szt."
                +MainController.padLeft(String.format("%1.2f", countValueOfItem()), 10)+" zł";
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductCount() {
        return productCount;
    }

    public double getProductPrice() {
        return productPrice;
    }
}
