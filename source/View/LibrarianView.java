package source.View;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class LibrarianView {

    private static final Map<String, String> ISBNToBookNameMap = new HashMap<>();
    private static final Map<String, Integer> ISBNToAvailableStockMap = new HashMap<>();

    public static void showLibrarianView(Stage primaryStage) {
        primaryStage.setTitle("Librarian MENU");

        BorderPane layout = new BorderPane();
        Scene scene = new Scene(new VBox(), 800, 700);
        scene.setFill(Color.OLDLACE);


        Button showRequestsButton = new Button("Show Requests");
        layout.setCenter(showRequestsButton);

        showRequestsButton.setOnAction(e -> {
            showRequestsScene(primaryStage);
        });


        Button logoutButton = new Button("Logout");
        layout.setBottom(logoutButton);

        logoutButton.setOnAction(e -> {
            LoginScene.showLoginScene(primaryStage);
        });


        primaryStage.setScene(new Scene(layout, 400, 400));
        primaryStage.show();
    }

    private static void showRequestsScene(Stage primaryStage) {

        Stage requestsStage = new Stage();
        requestsStage.setTitle("Book Requests");

        VBox requestsLayout = new VBox(10);
        Scene requestsScene = new Scene(requestsLayout, 600, 400);

        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\joelb\\IdeaProjects\\Object_Oriented_Programming_Project\\files\\Request.txt"));
            String line;

            ObservableList<HBox> hboxList = FXCollections.observableArrayList();

            while ((line = reader.readLine()) != null) {
                final String requestLine = line;

                Button checkButton = new Button("Check");
                checkButton.setOnAction(e -> handleCheck(requestsStage,requestLine,hboxList));

                TextField textField = new TextField(line);
                textField.setPrefColumnCount(17);
                textField.setEditable(false);

                HBox requestBox = new HBox(10, textField, checkButton);
                hboxList.add(requestBox);
            }
            reader.close();

            requestsLayout.getChildren().addAll(hboxList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestsStage.setScene(requestsScene);
        requestsStage.show();
    }

    private static void handleCheck(Stage requestsStage,String requestDetails,ObservableList<HBox> hboxList) {
        String[] parts = requestDetails.split(",");
        if (parts.length == 3) {
            String orderID = parts[0];
            String ISBN = parts[1];
            int requestedQuantity = Integer.parseInt(parts[2]);

            String bookName = getBookNameFromISBN(ISBN);

            int availableStock = getAvailableStock(ISBN);

            if (availableStock >= requestedQuantity) {
                displayConfirmationBox(bookName, requestedQuantity, orderID);
            } else {
                displayErrorBox(bookName,requestedQuantity,availableStock);
                deleteRequest(requestDetails);
                hboxList.removeIf(hbox -> {
                    String line = ((TextField) hbox.getChildren().get(0)).getText();
                    return line.equals(requestDetails);
                });
                showRequestsScene(new Stage());
                requestsStage.close();
            }
        }
    }

    private static String getBookNameFromISBN(String ISBN) {
        //System.out.println("aaaa");
        if (ISBNToBookNameMap.isEmpty()) {
            loadISBNToBookNameMapping();
        }
        return ISBNToBookNameMap.getOrDefault(ISBN, "Not Found");
    }

    private static void loadISBNToBookNameMapping() {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\joelb\\IdeaProjects\\Object_Oriented_Programming_Project\\files\\Books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String ISBN = parts[0].trim();
                    String bookName = parts[1].trim();
                    ISBNToBookNameMap.put(ISBN, bookName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getAvailableStock(String ISBN) {

        if (ISBNToAvailableStockMap.isEmpty()) {
            loadISBNToAvailableStockMapping();
        }
        return ISBNToAvailableStockMap.getOrDefault(ISBN, 0);
    }

    private static void loadISBNToAvailableStockMapping() {
        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\joelb\\IdeaProjects\\Object_Oriented_Programming_Project\\files\\Books.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip the header line
                if (line.startsWith("ISBN")) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 10) {
                    String ISBN = parts[0].trim();
                    int availableStock = Integer.parseInt(parts[9].trim());
                    ISBNToAvailableStockMap.put(ISBN, availableStock);
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static void displayConfirmationBox(String bookName, int requestedQuantity, String orderID) {
        Platform.runLater(() -> {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation");
            confirmationAlert.setHeaderText("The \"" + bookName + "\" is in stock, create bill?");
            confirmationAlert.setContentText("Order ID: " + orderID + "\nRequested Quantity: " + requestedQuantity);

            ButtonType createBillButton = new ButtonType("Create Bill");
            ButtonType cancelButton = new ButtonType("Cancel");

            confirmationAlert.getButtonTypes().setAll(createBillButton, cancelButton);

            // Handle button actions when the confirmation box is closed
            confirmationAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == createBillButton) {
                    // Handle "Create Bill" button action
                    System.out.println("Creating bill for order ID: " + orderID);
                } else {
                    // Handle "Cancel" button action
                    System.out.println("Canceling order ID: " + orderID);
                }
            });
        });
    }

    private static void displayErrorBox(String bookName,int requestedQuantity, int availableStock) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Error, no stock for the requested book: " + '"' + bookName + '"' + "\n" +
                "Requested stock: " + requestedQuantity+ "\n" + "Available stock: " +availableStock );
        errorAlert.showAndWait();
    }

    private static void deleteRequest(String requestDetails) {

        String filePath = "C:\\Users\\joelb\\IdeaProjects\\Object_Oriented_Programming_Project\\files\\Request.txt";

        try {

            File tempFile = new File("C:\\Users\\joelb\\IdeaProjects\\Object_Oriented_Programming_Project\\files\\temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {

                if (!currentLine.equals(requestDetails)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }

            writer.close();
            reader.close();

            Files.move(tempFile.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


