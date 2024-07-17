package controllers;

import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Manage Products");
            System.out.println("2. Manage Sellers");
            System.out.println("3. Manage Transactions");
            System.out.println("4. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    ProductManagement.menu();
                    break;
                case 2:
                    SellerManagement.menu();
                    break;
                case 3:
                    TransactionManagement.menu();
                    break;
                case 4:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
