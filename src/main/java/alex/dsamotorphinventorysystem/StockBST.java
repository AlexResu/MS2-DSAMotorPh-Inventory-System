/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.dsamotorphinventorysystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Stock class encapsulating stock details
 * @author Alex Resurreccion
 */
class Stock {
    private String engineNumber; 
    private String date;
    private String stockLabel;
    private String brand;
    private String status;
    
    public Stock(String engineNumber, String date, String stockLabel, String brand, String status) {
        this.engineNumber = engineNumber;
        this.date = date;
        this.stockLabel = stockLabel;
        this.brand = brand;
        this.status = status;
    }
    
    public String getEngineNumber() {
        return engineNumber;
    }
    
    public String getDate() {
        return date;
    }
    
    public String getStockLabel() {
        return stockLabel;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String toString() {
        return engineNumber + "," + date + "," + stockLabel + "," + brand + "," + status;
    }
    
    public void display(){
        System.out.println("Item info ");
        System.out.println("Engine number: " + getEngineNumber());
        System.out.println("Date entered: " + getDate());
        System.out.println("Stock label: " + getStockLabel());
        System.out.println("Brand: " + getBrand());
        System.out.println("Status: " + getStatus());
    }
    
    public boolean isDeletable(){
        return getStockLabel().equals("Old") && getStatus().equals("Sold");
    }
    
}
// Binary Search Tree for managing stock
public class StockBST {
    private class Node {
        Stock stock;
        Node left, right;
        
        Node(Stock stock) {
            this.stock = stock;
            left = right = null;
        }
    }
    
    private Node root;
    
    public void insert(Stock stock) {
        root = insertRec(root, stock);
    }
    
    private Node insertRec(Node root, Stock stock) {
        if (root == null) return new Node(stock);
        if (stock.getEngineNumber().compareTo(root.stock.getEngineNumber()) <0)
            root.left = insertRec(root.left, stock);
        else
            root.right = insertRec(root.right, stock);
        return root;
    }
    
    public Stock search(String engineNumber) {
        return searchRec(root, engineNumber);
    }
    
    private Stock searchRec(Node root, String engineNumber) {
        if (root == null) return null;
        if (root.stock.getEngineNumber().equals(engineNumber)) return root.stock;
        return engineNumber.compareTo(root.stock.getEngineNumber()) < 0 ? searchRec(root.left, engineNumber) : 
                searchRec(root.right, engineNumber);
    }
    
    public void delete(String engineNumber) {
        root = deleteRec(root, engineNumber);
    }
    
    private Node deleteRec(Node root, String engineNumber) {
        if (root == null) return root;
        if (engineNumber.compareTo(root.stock.getEngineNumber()) < 0)
            root.left = deleteRec(root.left, engineNumber);
        else if (engineNumber.compareTo(root.stock.getEngineNumber()) > 0)
            root.right = deleteRec(root.right, engineNumber);
        else {
            if (root.left == null) return root.right;
            if (root.right == null) return  root.left;
            root.stock = minValue(root.right);
            root.right = deleteRec(root.right, root.stock.getEngineNumber()); 
        }
        return root;
    }
    
    private Stock minValue(Node root) {
        while (root.left != null) root = root.left;
        return root.stock;
    }
    
    
    // Get all stocks in sorted order
    public List<Stock> getAllStocks() {
        List<Stock> stockList = new ArrayList<>();
        inOrderTraversal(root, stockList);
        return stockList;
    }
    
    // Recursive in-order traversal (Left -> Root -> Right)
    private void inOrderTraversal(Node node, List<Stock> stockList) {
        if (node != null) {
            inOrderTraversal(node.left, stockList);
            stockList.add(node.stock);
            inOrderTraversal(node.right, stockList);
        }
    }
}
