package controllers;

import java.sql.*;
import java.util.Scanner;

import dbConnection.DBConnection;

public class TransactionManagement {
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Process Transaction");
            System.out.println("2. View Transactions");
            System.out.println("3. Update Transaction Status");
            System.out.println("4. Refund Transaction");
            System.out.println("5. Back to Main Menu");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    processTransaction();
                    break;
                case 2:
                    viewTransactions();
                    break;
                case 3:
                    updateTransactionStatus();
                    break;
                case 4:
                    refundTransaction();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void processTransaction() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter product ID: ");
            int productId = scanner.nextInt();
            System.out.print("Enter seller ID: ");
            int sellerId = scanner.nextInt();
            System.out.print("Enter buyer ID: ");
            int buyerId = scanner.nextInt();
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.print("Enter transaction status: ");
            String status = scanner.nextLine();

            String updateProductQuery = "UPDATE Product SET quantity_available = quantity_available - ? WHERE product_id = ? AND quantity_available >= ?";
            try (PreparedStatement updateProductStmt = connection.prepareStatement(updateProductQuery)) {
                updateProductStmt.setInt(1, quantity);
                updateProductStmt.setInt(2, productId);
                updateProductStmt.setInt(3, quantity);
                int rowsAffected = updateProductStmt.executeUpdate();

                if (rowsAffected == 0) {
                    System.out.println("Insufficient quantity available or invalid product ID.");
                    return;
                }
            }

            String query = "INSERT INTO Transaction (product_id, seller_id, buyer_id, quantity, transaction_date, status) VALUES (?, ?, ?, ?, NOW(), ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, productId);
                stmt.setInt(2, sellerId);
                stmt.setInt(3, buyerId);
                stmt.setInt(4, quantity);
                stmt.setString(5, status);
                stmt.executeUpdate();
                System.out.println("Transaction processed successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error processing transaction: " + e.getMessage());
        }
    }

    private static void viewTransactions() {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM Transaction";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    System.out.println("Transaction ID: " + rs.getInt("transaction_id"));
                    System.out.println("Product ID: " + rs.getInt("product_id"));
                    System.out.println("Seller ID: " + rs.getInt("seller_id"));
                    System.out.println("Buyer ID: " + rs.getInt("buyer_id"));
                    System.out.println("Quantity: " + rs.getInt("quantity"));
                    System.out.println("Transaction Date: " + rs.getTimestamp("transaction_date"));
                    System.out.println("Status: " + rs.getString("status"));
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error viewing transactions: " + e.getMessage());
        }
    }

    private static void updateTransactionStatus() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter transaction ID to update: ");
            int transactionId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.print("Enter new transaction status: ");
            String status = scanner.nextLine();

            String query = "UPDATE Transaction SET status = ? WHERE transaction_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, status);
                stmt.setInt(2, transactionId);
                stmt.executeUpdate();
                System.out.println("Transaction status updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating transaction status: " + e.getMessage());
        }
    }

    private static void refundTransaction() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter transaction ID to refund: ");
            int transactionId = scanner.nextInt();

            String query = "SELECT * FROM Transaction WHERE transaction_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, transactionId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int productId = rs.getInt("product_id");
                        int quantity = rs.getInt("quantity");

                        String updateProductQuery = "UPDATE Product SET quantity_available = quantity_available + ? WHERE product_id = ?";
                        try (PreparedStatement updateProductStmt = connection.prepareStatement(updateProductQuery)) {
                            updateProductStmt.setInt(1, quantity);
                            updateProductStmt.setInt(2, productId);
                            updateProductStmt.executeUpdate();
                        }

                        String deleteTransactionQuery = "DELETE FROM Transaction WHERE transaction_id = ?";
                        try (PreparedStatement deleteTransactionStmt = connection.prepareStatement(deleteTransactionQuery)) {
                            deleteTransactionStmt.setInt(1, transactionId);
                            deleteTransactionStmt.executeUpdate();
                            System.out.println("Transaction refunded successfully.");
                        }
                    } else {
                        System.out.println("Invalid transaction ID.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error refunding transaction: " + e.getMessage());
        }
    }
}
