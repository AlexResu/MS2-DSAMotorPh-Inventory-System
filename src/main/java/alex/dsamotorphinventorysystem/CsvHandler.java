/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alex.dsamotorphinventorysystem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The {@code CsvHandler} class provides methods for reading and processing CSV files.
 * It handles loading inventory data from a CSV, deleting specific records, and appending new items to the CSV.
 * This class works with the MotorPH Inventory System to persist stock data to a CSV file.
 * @author Alex Resurreccion
 */
public class CsvHandler {
    private String fileName = "resources/MotorPH Inventory Data - March 2023 Inventory Data.csv";
    
    // Loads inventory data from the CSV file into a Binary Search Tree (BST).
    public void loadInventory(StockBST bst) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                // Ignore first and second row of csv
                if(row < 2){
                    row++;
                } else {
                    String[] data = line.split(",");
                    bst.insert(new Stock(data[0], data[1], data[2], data[3], data[4]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage());
        }
    }
    
    /**
     * Deletes a stock item from the CSV file based on the provided engine number.
     * It creates a temporary file, writes all lines except the one to be deleted, 
     * and then renames the temporary file to replace the original CSV file.
     * 
     * @param engineNumberToDelete The engine number of the stock item to be deleted.
     * @return true if the item was successfully deleted, false otherwise.
     */
    public boolean delete(String engineNumberToDelete){
        File inputFile = new File(fileName); // original CSV file
        File tempFile = new File("myTempFile.csv"); // temporary file to store updated data
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim().split(",")[0];
                if(trimmedLine.equals(engineNumberToDelete)){
                    continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close(); 
            reader.close(); 
        } catch (IOException e) {
            System.out.println("error: " + e);
        }
        
        boolean isDeleted = false;
        if(inputFile.delete()){
            isDeleted = tempFile.renameTo(inputFile);
        }
        return isDeleted;
    }
    
    /**
     * Appends a new stock item to the CSV file.
     * It writes the provided stock information to the CSV file, with each field separated by commas.
     * 
     * @param itemInfo The stock item data to append, represented as a String array.
     */
    public void add(String[] itemInfo){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(String.join(",", itemInfo) + System.getProperty("line.separator"));
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(CsvHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Splits a string by commas and returns the resulting array of strings.
     * This method is typically used to parse a line from a CSV file.
     * 
     * @param data The string to be split.
     * @return An array of strings resulting from splitting the input string by commas.
     */
    public String[] splitStringByComma(String data){
        return data.split(",");
    }
}
