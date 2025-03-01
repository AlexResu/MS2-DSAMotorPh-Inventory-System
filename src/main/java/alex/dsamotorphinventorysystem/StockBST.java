/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.dsamotorphinventorysystem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Stock class stores details about each stock item.
 * It contains methods for getting and displaying stock information.
 * @author Alex Resurreccion
 */
class Stock {
    private String engineNumber; 
    private String date;
    private String stockLabel;
    private String brand;
    private String status;
    
    // Constructor to initialize stock details
    public Stock(String engineNumber, String date, String stockLabel, String brand, String status) {
        this.engineNumber = engineNumber;
        this.date = date;
        this.stockLabel = stockLabel;
        this.brand = brand;
        this.status = status;
    }
    
    // Getters for stock details
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
    
    // Returns stock details as a comma-separated string
    public String toString() {
        return engineNumber + "," + date + "," + stockLabel + "," + brand + "," + status;
    }
    
    // Displays stock details
    public void display(){
        System.out.println("Item info ");
        System.out.println("Engine number: " + getEngineNumber());
        System.out.println("Date entered: " + getDate());
        System.out.println("Stock label: " + getStockLabel());
        System.out.println("Brand: " + getBrand());
        System.out.println("Status: " + getStatus());
    }
    
    // Checks if the stock is deletable (Old and Sold)
    public boolean isDeletable(){
        return getStockLabel().equals("Old") && getStatus().equals("Sold");
    }
    
}
/**
 * StockBST class is a Binary Search Tree to manage stock items.
 * It provides methods to insert, search, delete, and get all stocks in sorted order.
 */
public class StockBST {
    // Node class represents each node in the tree
    private class Node {
        Stock stock;
        Node left, right;
        
        Node(Stock stock) {
            this.stock = stock;
            left = right = null;
        }
    }
    
    private Node root;
    
    // Insert a stock item into the BST
    public void insert(Stock stock) {
        root = insertRec(root, stock);
    }
    
    // Helper method to insert stock recursively
    private Node insertRec(Node root, Stock stock) {
        if (root == null) return new Node(stock); // If empty, create a new node
        if (stock.getEngineNumber().compareTo(root.stock.getEngineNumber()) <0)
            root.left = insertRec(root.left, stock); // Insert into left subtree
        else
            root.right = insertRec(root.right, stock); // Insert into right subtree
        return root;
    }
    
    // Search for a stock item by engine number
    public Stock search(String engineNumber) {
        return searchRec(root, engineNumber);
    }
    
    // Helper method to search stock recursively
    private Stock searchRec(Node root, String engineNumber) {
        if (root == null) return null; // If not found, return null
        if (root.stock.getEngineNumber().equals(engineNumber)) return root.stock; // Stock found
        return engineNumber.compareTo(root.stock.getEngineNumber()) < 0 ? searchRec(root.left, engineNumber) : 
                searchRec(root.right, engineNumber); // Search left or right based on comparison
    }
    
    // Delete a stock item by engine number
    public void delete(String engineNumber) {
        root = deleteRec(root, engineNumber);
    }
    
    // Helper method to delete stock recursively
    private Node deleteRec(Node root, String engineNumber) {
        if (root == null) return root; // Item not found, return null
        if (engineNumber.compareTo(root.stock.getEngineNumber()) < 0)
            root.left = deleteRec(root.left, engineNumber); // Search left subtree
        else if (engineNumber.compareTo(root.stock.getEngineNumber()) > 0)
            root.right = deleteRec(root.right, engineNumber); // Search right subtree
        else {
            // Node with only one child or no child
            if (root.left == null) return root.right;
            if (root.right == null) return  root.left;
            // Node with two children: Get the inorder successor (smallest in right subtree)
            root.stock = minValue(root.right);
            root.right = deleteRec(root.right, root.stock.getEngineNumber()); // Delete the inorder successor
        }
        return root;
    }
    
    // Get the minimum value node (used for deleting)
    private Stock minValue(Node root) {
        while (root.left != null) root = root.left;
        return root.stock;
    }
    
    
    // Get all stocks in sorted order (inorder traversal)
    public List<Stock> getAllStocks() {
        List<Stock> stockList = new ArrayList<>();
        inOrderTraversal(root, stockList);
        return stockList;
    }
    
    // Helper method for in-order traversal of the BST
    private void inOrderTraversal(Node node, List<Stock> stockList) {
        if (node != null) {
            inOrderTraversal(node.left, stockList); // Traverse left subtree
            stockList.add(node.stock); // Add the stock to the list
            inOrderTraversal(node.right, stockList); // Traverse right subtree
        } 
    }
}
