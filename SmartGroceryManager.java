import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class SmartGroceryManager {
    static ArrayList<String> pendingItems = new ArrayList<>();
    static String[] purchasedItems = new String[100]; // Arbitrary limit
    static int purchasedCount = 0;

    public static void main(String[] args) {
        // Load saved data
        pendingItems = loadData("pending.txt");
        ArrayList<String> purchasedTemp = loadData("purchased.txt");
        for (String item : purchasedTemp) {
            if (purchasedCount < purchasedItems.length) {
                purchasedItems[purchasedCount++] = item;
            }
        }

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n==== Smart Grocery List Menu ====");
            System.out.println("1. View Pending Items");
            System.out.println("2. Add New Item");
            System.out.println("3. Mark Item as Purchased");
            System.out.println("4. View Purchased Items");
            System.out.println("5. Exit and Save");

            int choice = getValidatedInteger(sc);

            switch (choice) {
                case 1 -> viewPendingItems();
                case 2 -> addNewItem(sc);
                case 3 -> markAsPurchased(sc);
                case 4 -> viewPurchasedItems();
                case 5 -> {
                    saveData(pendingItems, "pending.txt");
                    saveData(arrayToList(), "purchased.txt");
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // === View Pending Items ===
    public static void viewPendingItems() {
        if (pendingItems.isEmpty()) {
            System.out.println("No pending items!");
        } else {
            System.out.println("Pending Items:");
            for (int i = 0; i < pendingItems.size(); i++) {
                System.out.println(i + ": " + pendingItems.get(i));
            }
        }
    }

    // === Add New Item ===
    public static void addNewItem(Scanner sc) {
        System.out.print("Enter item name: ");
        String item = sc.nextLine();
        if (!item.isBlank()) {
            pendingItems.add(item);
            System.out.println("Item added.");
        } else {
            System.out.println("Item name cannot be empty.");
        }
    }

    // === Mark Item as Purchased ===
    public static void markAsPurchased(Scanner sc) {
        viewPendingItems();
        if (pendingItems.isEmpty()) return;

        System.out.print("Enter index of item to mark as purchased: ");
        int index = getValidatedInteger(sc);

        try {
            String item = pendingItems.remove(index);
            if (purchasedCount < purchasedItems.length) {
                purchasedItems[purchasedCount++] = item;
                System.out.println("Item marked as purchased.");
            } else {
                System.out.println("Purchased list is full.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid index. Try again.");
        }
    }

    // === View Purchased Items ===
    public static void viewPurchasedItems() {
        if (purchasedCount == 0) {
            System.out.println("No purchased items yet.");
        } else {
            System.out.println("Purchased Items:");
            for (int i = 0; i < purchasedCount; i++) {
                System.out.println(i + ": " + purchasedItems[i]);
            }
        }
    }

    // === Recursive Input Validation ===
    public static int getValidatedInteger(Scanner sc) {
        System.out.print("Enter a number: ");
        String input = sc.nextLine();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Try again.");
            return getValidatedInteger(sc); // Recursive call
        }
    }

    // === Convert Array to ArrayList ===
    public static ArrayList<String> arrayToList() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < purchasedCount; i++) {
            list.add(purchasedItems[i]);
        }
        return list;
    }

    // === File Save Method ===
    public static void saveData(ArrayList<String> data, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String item : data) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // === File Load Method ===
    public static ArrayList<String> loadData(String filename) {
        ArrayList<String> data = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return data;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        return data;
    }
}
