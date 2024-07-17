package controllers;

import java.sql.*;
import java.util.Scanner;

import dbConnection.DBConnection;

public class SellerManagement {
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Register Seller");
            System.out.println("2. View Sellers");
            System.out.println("3. Update Seller");
            System.out.println("4. Delete Seller");
            System.out.println("5. Back to Main Menu");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    registerSeller();
                    break;
                case 2:
                    viewSellers();
                    break;
                case 3:
                    updateSeller();
                    break;
                case 4:
                    deleteSeller();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerSeller() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter seller name: ");
            String name = scanner.nextLine();
            System.out.print("Enter seller email: ");
            String email = scanner.nextLine();
            System.out.print("Enter seller address: ");
            String address = scanner.nextLine();
            System.out.print("Enter seller phone number: ");
            String phoneNumber = scanner.nextLine();

            String query = "INSERT INTO Seller (name, email, address, phone_number) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, address);
                stmt.setString(4, phoneNumber);
                stmt.executeUpdate();
                System.out.println("Seller registered successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error registering seller: " + e.getMessage());
        }
    }

    private static void viewSellers() {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM Seller";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    System.out.println("Seller ID: " + rs.getInt("seller_id"));
                    System.out.println("Name: " + rs.getString("name"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Address: " + rs.getString("address"));
                    System.out.println("Phone Number: " + rs.getString("phone_number"));
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error viewing sellers: " + e.getMessage());
        }
    }

    private static void updateSeller() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter seller ID to update: ");
            int sellerId = scanner.nextInt();
            System.out.print("Enter new seller name: ");
            scanner.nextLine(); // consume newline
            String name = scanner.nextLine();
            System.out.print("Enter new seller email: ");
            String email = scanner.nextLine();
            System.out.print("Enter new seller address: ");
            String address = scanner.nextLine();
            System.out.print("Enter new seller phone number: ");
            String phoneNumber = scanner.nextLine();

            String query = "UPDATE Seller SET name = ?, email = ?, address = ?, phone_number = ? WHERE seller_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, address);
                stmt.setString(4, phoneNumber);
                stmt.setInt(5, sellerId);
                stmt.executeUpdate();
                System.out.println("Seller updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating seller: " + e.getMessage());
        }
    }

    private static void deleteSeller() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter seller ID to delete: ");
            int sellerId = scanner.nextInt();

            String query = "DELETE FROM Seller WHERE seller_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, sellerId);
                stmt.executeUpdate();
                System.out.println("Seller deleted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting seller: " + e.getMessage());
        }
    }
}
