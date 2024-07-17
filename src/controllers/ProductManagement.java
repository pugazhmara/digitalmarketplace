package controllers;

import java.sql.*;
import java.util.Scanner;

import dbConnection.DBConnection;

public class ProductManagement {
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Back to Main Menu");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    viewProducts();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addProduct() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();
            System.out.print("Enter product description: ");
            String description = scanner.nextLine();
            System.out.print("Enter product price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter quantity available: ");
            int quantity = scanner.nextInt();
            System.out.print("Enter product category: ");
            scanner.nextLine(); // consume newline
            String category = scanner.nextLine();

            String query = "INSERT INTO Product (name, description, price, quantity_available, category) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, description);
                stmt.setDouble(3, price);
                stmt.setInt(4, quantity);
                stmt.setString(5, category);
                stmt.executeUpdate();
                System.out.println("Product added successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private static void viewProducts() {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM Product";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    System.out.println("Product ID: " + rs.getInt("product_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Description: " + rs.getString("description"));
                    System.out.println("Price: " + rs.getDouble("price"));
                    System.out.println("Quantity Available: " + rs.getInt("quantity_available"));
                    System.out.println("Category: " + rs.getString("category"));
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error viewing products: " + e.getMessage());
        }
    }

    private static void updateProduct() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter product ID to update: ");
            int productId = scanner.nextInt();
            System.out.print("Enter new product name: ");
            scanner.nextLine(); // consume newline
            String name = scanner.nextLine();
            System.out.print("Enter new product description: ");
            String description = scanner.nextLine();
            System.out.print("Enter new product price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter new quantity available: ");
            int quantity = scanner.nextInt();
            System.out.print("Enter new product category: ");
            scanner.nextLine(); // consume newline
            String category = scanner.nextLine();

            String query = "UPDATE Product SET name = ?, description = ?, price = ?, quantity_available = ?, category = ? WHERE product_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, description);
                stmt.setDouble(3, price);
                stmt.setInt(4, quantity);
                stmt.setString(5, category);
                stmt.setInt(6, productId);
                stmt.executeUpdate();
                System.out.println("Product updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    private static void deleteProduct() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter product ID to delete: ");
            int productId = scanner.nextInt();

            String query = "DELETE FROM Product WHERE product_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, productId);
                stmt.executeUpdate();
                System.out.println("Product deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }
}

