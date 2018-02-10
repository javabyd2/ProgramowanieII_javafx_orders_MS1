package com.sdabyd2.javafx.orders.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdabyd2.javafx.orders.controller.MainController;
import com.sdabyd2.orders.orders.model.Item;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Order implements Serializable {

    private List<Item> items = new ArrayList<>();
    private int maxItemsInOrder;

    public Order() {
        maxItemsInOrder = 10;
    }

    public Order(int maxItemsInOrder) {
        this.maxItemsInOrder = maxItemsInOrder;
    }

    public void addItem(Item item) {
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(0).getProductName().equals(item.getProductName())) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            items.add(item);
        } else {
            items.get(index).setProductCount(items.get(index).getProductCount()+item.getProductCount());
        }
    }

    public double countValueOfOrder() {
        Double value = 0.00;
        for (int i = 0; i < items.size(); i++) {
            value += items.get(i).countValueOfItem();
        }
        return value;
    }

    public double countValueOfRebate() {
        Double rebatValue = 0.00;
        for (int i = 0; i < items.size(); i++) {
            rebatValue += items.get(i).countValueOfItem() - items.get(i).countValueOfItemIncludingRebate();
        }
        return rebatValue;
    }

    public double countValueOfOrderWithRebate() {
        Double rebatValue = 0.00;
        for (int i = 0; i < items.size(); i++) {
            rebatValue += items.get(i).countValueOfItemIncludingRebate();
        }
        return rebatValue;
    }

    @Override
    public String toString() {
        Double value = 0.00;
        Double rebatValue = 0.00;
        String result = "\nZamówienie:";
        for (int i = 0; i < items.size(); i++) {
            result += "\n" + items.get(i).toString();
            value += items.get(i).countValueOfItem();
            rebatValue += items.get(i).countValueOfItem() - items.get(i).countValueOfItemIncludingRebate();
        }
        return result + "\n\nRazem: " + String.format("%1.2f", value)
                +"\nRabat: " + String.format("%1.2f", rebatValue)
                +"\nRazem po rabacie: " + String.format("%1.2f", value - rebatValue);
    }

    public void deleteItem(int index) {
        items.remove(index);
    }

    public void editItem(int index, Item itemNew) {
        Item item = items.get(index);
        item.setProductName(itemNew.getProductName());
        item.setProductCount(itemNew.getProductCount());
    }

    public static void saveOrderToFile(Order order, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File filename = new File(fileName+".json");
        filename.createNewFile();
        mapper.writeValue(filename, order);
    }

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

    public List<Item> getItems() {
        return items;
    }

    public int getMaxItemsInOrder() {
        return maxItemsInOrder;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setMaxItemsInOrder(int maxItemsInOrder) {
        this.maxItemsInOrder = maxItemsInOrder;
    }

    public static String[] formatToList(List<Item> list) {
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            //result[i] = String.format("%s %1.2f", list.get(i).getName(), list.get(i).getPrice())+" zł";
            result[i] = MainController.padRight(list.get(i).getProductName(), 28)
                    +MainController.padLeft(String.format("%d", list.get(i).getProductCount())+" szt.", 10)
                    +MainController.padLeft(String.format("%1.2f", list.get(i).getProductPrice())+" zł", 10)
                    +MainController.padLeft(String.format("%1.2f", list.get(i).countValueOfItem())+" zł", 11)
                    +MainController.padLeft(list.get(i).giveRebate(), 6)
                    +MainController.padLeft(String.format("%1.2f", list.get(i).countValueOfItemIncludingRebate())+" zł", 11);
        }
        return result;
    }
}
