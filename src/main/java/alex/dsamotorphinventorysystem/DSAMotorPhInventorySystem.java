/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package alex.dsamotorphinventorysystem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * DSAMotorPhInventory manages inventory using a Binary Search Tree (BST) for fast search, add, and delete operations.
 * It supports adding, deleting, sorting (using Merge Sort), and searching stock items efficiently.
 * @author Alex Resurreccion
 */
public class DSAMotorPhInventorySystem { 
    private static StockBST inventory = new StockBST(); // Binary Search Tree for inventory management
    private static Stock stock;
    private static Scanner scanner = new Scanner(System.in);
    private static CsvHandler csvHandler = new CsvHandler();
    

    public static void main(String[] args) {
        csvHandler.loadInventory(inventory);
        System.out.println("***Welcome to MotoroPH Inventory App!***\n");
        menu();
    }
    
    public static void menu() {
        stock = null; // refresh stock state upon returning to menu
        try {
            System.out.println("-- Main Menu --\n" 
                    + "1 - Add Stock\n" 
                    + "2 - Delete Stock\n" 
                    + "3 - Sort Stocks\n" 
                    + "4 - Search Stock\n" 
                    + "5 - Exit");
            System.out.println("Enter your selection: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: addStock(); break;
                case 2: deleteStock(); break;
                case 3: sortStocks(); break;
                case 4: searchStock(); break;
                case 5: System.out.println("Exiting..."); return;
                default: System.out.println("Invalid selection. Try again");
            }
        } catch(InputMismatchException e) {
            System.out.println("Error: Please enter a valid number.");
            scanner.nextLine(); // Clear buffer
        }
    }
    
    public static void addStock() {
        System.out.print("Enter Engine Number: ");
        String engineNumber = scanner.nextLine();
        
        if (inventory.search(engineNumber) != null) {
            System.out.println("Item already exist in inventory. "
                    + "Do you want to enter a new engine number (y) or display information (any)?");
            String existChoice = scanner.nextLine();
            if(existChoice.equals("y")){
                addStock();
            } else {
                stock = inventory.search(engineNumber);
                stock.display();
            }
        } else {
            addNewStock(engineNumber);
        }
        newTransactionPrompt();
    }
    
    public static void addNewStock(String engineNumber){
        String isInfoCorrect;
        Date date = new Date();
        //Default values for new item info
        String dateEntered = new SimpleDateFormat("MM/dd/yyyy").format(date);
        String stockLabel = "New";
        String status = "On-hand";
        
        // Prompt user to input stock information
        System.out.println("Enter information for the new item ");
        System.out.print("Enter Brand: ");
        String brand = scanner.nextLine();
        String[] newStock = {engineNumber, dateEntered, stockLabel, brand, status};
        stock = new Stock(engineNumber, dateEntered, stockLabel, brand, status);
        stock.display();
        System.out.println("Are all the information correct? (y/n)");
        isInfoCorrect = scanner.nextLine();

        
        if(isInfoCorrect.equals("y")){
            csvHandler.add(newStock); // for csv add
            inventory.insert(stock);
            System.out.println("Stock added successfully.");
        } else {
            addNewStock(engineNumber);
        }
    }
    
    public static void setStock(String engineNumber, String date, String stockLabel, String brand, String status){
        stock = new Stock(engineNumber, date, stockLabel, brand, status);
    }
    
    public static void searchStock() {
        System.out.print("Enter Engine Number to Search: ");
        String engineNumber = scanner.nextLine();
        Stock stock = inventory.search(engineNumber);
        
        if (stock != null) {
            stock.display();
        } else {
            System.out.println("Stock not found.");
        }
        newTransactionPrompt();
    }
    
    public static void deleteStock() {
        System.out.print("Enter Engine Number to Delete: ");
        String engineNumber = scanner.nextLine();
        stock = inventory.search(engineNumber);
        if (stock != null) {
            stock.display();
            if(stock.isDeletable()){
                System.out.println("Are you sure you want to delete this item? "
                    + "Input the engine number to confirm.");
                String confirmDelete = scanner.nextLine();
                if(confirmDelete.equals(engineNumber)){
                    inventory.delete(engineNumber);
                    boolean isDeleted = csvHandler.delete(engineNumber);
                    if(isDeleted){
                        System.out.println("Item has been deleted");
                    } else {
                        System.out.println("Item deletion failed. Something went wrong");
                    }
                } else {
                    System.out.println("Item deletion failed. "
                            + "Engine number did not match.");
                }
            } else {
                System.out.println("Item is not deletable. (must be Old and Sold)");
            }
        } else {
            System.out.println("Item doesn't exist in inventory. "
                    + "Do you want to enter a new engine number (y)?");
            String existChoice = scanner.nextLine();
            if(existChoice.equals("y")){
                deleteStock();
            } 
        }
        newTransactionPrompt();
    }
    
    public static void sortStocks() {
        System.out.println("Type \"y\" to sort brand in ascending order or type \"n\" to in descending order");
        String sortOrder = scanner.nextLine();
        
        List<Stock> sortedList = inventory.getAllStocks();
        sortedList = mergeSort(sortedList);
        
        if(sortOrder.equals("n")){
            sortedList = sortedList.reversed();
        } else if(!sortOrder.equals("y")){
            System.out.println("Invalid input, please choose valid input");
            sortStocks();
        }
        
        for (Stock stock : sortedList) {
            System.out.println(stock);
        }
        newTransactionPrompt();
    }
    
    public static List<Stock> mergeSort(List<Stock> list) { 
        if (list.size() <= 1) return list;
        
        int mid = list.size() / 2;
        List<Stock> left = mergeSort(new ArrayList<>(list.subList(0, mid)));
        List<Stock> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())));
        
        return merge(left, right);
    }
    
    public static List<Stock> merge(List<Stock> left, List<Stock> right) {
        List<Stock> merged = new ArrayList<>();
        int i = 0, j = 0;
        
        while (i < left.size() && j < right.size()) {
            if (left.get(i).getBrand().compareTo(right.get(j).getBrand()) <= 0) {
                merged.add(left.get(i++)); 
            } else {
                merged.add(right.get(j++));
            }
        }
        
        merged.addAll(left.subList(i, left.size()));
        merged.addAll(right.subList(j, right.size()));
        
        return merged;
    }
    
    /**
     * Prompts the user to either exit the program or perform another transaction.
     */
    public static void newTransactionPrompt(){
        System.out.println("Type x to exit or anything to make another transaction");
        String userInput = scanner.nextLine();
        if(!userInput.equals("x")){
            menu();
        } else {
            System.exit(0);
        }
    }
}
