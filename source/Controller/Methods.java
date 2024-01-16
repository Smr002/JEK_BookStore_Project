package source.Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javafx.stage.Screen;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.stream.Collectors;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import source.Model.Book;
import javafx.scene.layout.StackPane;
import source.Model.Order;
import source.Model.TransactionData;
import source.Main.Main;
import source.Model.User;
import source.Model.Permission;
import source.Model.PermissionEntry;
import source.View.FirstWindow;

public class Methods {

    public static void saveBooksToFile(List<Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("files/Books.dat"))) {
            oos.writeObject(books);
            System.out.println("Books saved to file: Books.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Book> readBook() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/Books.dat"))) {

            List<Book> books = (List<Book>) ois.readObject();
            System.out.println("Books loaded from file: Books.dat");
            return books;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void getBooks() throws ParseException {
        List<Book> booksList = readBook();

        if (booksList.isEmpty()) {
            System.out.println("No books available.");
        } else {
            Stage booksStage = new Stage();
            booksStage.setTitle("List of Books");

            TableView<Book> table = new TableView<>();

            // isbn column
            TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
            isbnColumn.setMinWidth(100);
            isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

            // title column
            TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
            titleColumn.setMinWidth(200);
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

            // author column
            TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
            authorColumn.setMinWidth(100);
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

            // category column
            TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
            categoryColumn.setMinWidth(100);
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

            // purchasePrice column
            TableColumn<Book, Double> purchasePriceColumn = new TableColumn<>("Purchase Price");
            purchasePriceColumn.setMinWidth(100);
            purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasedPrice"));

            // date purchased column
            TableColumn<Book, Date> datePurchasedColumn = new TableColumn<>("Date Purchased");
            datePurchasedColumn.setMinWidth(100);
            datePurchasedColumn.setCellValueFactory(new PropertyValueFactory<>("purchasedDate"));

            // sellingPrice column
            TableColumn<Book, Double> priceColumn = new TableColumn<>("Selling Price");
            priceColumn.setMinWidth(100);
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

            // stock column
            TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stock");
            stockColumn.setMinWidth(100);
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

            // Set the columns to the table
            table.getColumns().addAll(isbnColumn, titleColumn, authorColumn, categoryColumn, purchasePriceColumn,
                    datePurchasedColumn, priceColumn,
                    stockColumn);

            // Add the data to the table
            table.setItems(FXCollections.observableArrayList(booksList));

            VBox booksLayout = new VBox();
            booksLayout.getChildren().add(table);

            Scene booksScene = new Scene(booksLayout, 600, 400);
            booksStage.setScene(booksScene);
            booksStage.show();
        }
    }

    public List<String> readRequests() {

        List<String> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("files/request.txt"))) {
            String header = reader.readLine();
            String[] columns = header.split(",");
            String line;
            while ((line = reader.readLine()) != null) {
                requests.add(line);
                String[] values = line.split(",");
                String orderRqst = values[0].trim();
                String isbnT = values[1].trim();
                System.out.println(orderRqst + " " + isbnT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static void saveTransaction(User user, Order order, Integer orderID) throws ParseException {

        String filePath = "files/saveTransaction.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            LocalDate localDate = order.getOrderDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            String line = orderID + ";"
                    + order.getIsbnList() + ";"
                    + formattedDate + ";"
                    + order.getTotalPrice() + ";"
                    + order.getQuantityList() + ";"
                    + user.getUsername();

            writer.println(line);
            System.out.println("The data are saved in file " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void requestBook(int quantity) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);

        try {
            List<Book> booksList = readBook();

            System.out.print("Enter your ISBN for the request:");
            String isbnTemp = sc.nextLine();

            for (Book b : booksList) {
                if (b.getISBN().equals(isbnTemp)) {
                    if (quantity > b.getStock() || quantity <= 0) {
                        System.out.println("Invalid quantity. Available stock: " + b.getStock());
                        return; // Exit without creating request
                    }

                    String filePath = "files/request.txt";
                    try (PrintWriter output = new PrintWriter(new FileWriter(filePath, true))) {
                        Random orderRqst = new Random();
                        output.write(orderRqst.nextInt() + ",");
                        output.write(isbnTemp + ",");
                        output.write(quantity + "\n");
                        System.out.println("The request is done successfully");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return; // Exit the loop after successful request
                }
            }

            System.out.println("Book with ISBN " + isbnTemp + " not found");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }

    public void createBill(String ISBN, int quantity) throws IOException, ParseException {
        String filePath = "files/createBill.txt";
        String requestsFilePath = "files/request.txt";
        String booksFilePath = "files/Books.txt";

        List<Book> books = readBook();
        Date date = new Date();
        Random orderId = new Random();

        // Read requests
        List<String> requests = readRequests();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true));
                BufferedWriter requestsWriter = new BufferedWriter(new FileWriter(requestsFilePath));
                PrintWriter booksWriter = new PrintWriter(new FileWriter(booksFilePath))) {

            boolean exists = false;
            for (Book b : books) {
                if (ISBN.equals(b.getISBN())) {

                    int newStock = b.getStock() - quantity;
                    b.setStock(newStock);

                    writer.write(orderId.nextInt() + "," + b.getISBN() + "," + b.getTitle() + "," + b.getAuthor() + ","
                            + b.getCategory() + "," + date + "," + quantity + "\n");
                    exists = true;
                    break;
                    // Update stock in Books.txt

                }
            }

            if (!exists) {
                System.out.println("This book doesn't exist");
            }

            // Remove the first instance of the ISBN in requests
            boolean foundAndRemoved = false;
            List<String> updatedRequests = new ArrayList<>();
            for (String request : requests) {
                if (request.contains(ISBN) && !foundAndRemoved) {
                    foundAndRemoved = true;
                } else {
                    updatedRequests.add(request);
                }
            }

            if (!foundAndRemoved) {
                System.out.println("ISBN not found in requests");
            }

            // Rewrite the requests file
            for (String request : updatedRequests) {
                requestsWriter.write(request + "\n");
            }

            // Rewrite the Books.txt file with updated stock
            try (PrintWriter booksWriters = new PrintWriter(new FileWriter(booksFilePath))) {
                for (int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    booksWriters.write(book.getISBN() + "," + book.getTitle() + "," + book.getCategory() + ","
                            + book.getSupplier() + "," + book.getPurchasedPrice() + ","
                            + new SimpleDateFormat("yyyy-MM-dd").format(book.getPurchasedDate()) + ","
                            + book.getOriginalPrice() + "," + book.getSellingPrice() + ","
                            + book.getAuthor() + "," + book.getStock() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Double> calculateSum(String startDateField, String endDateField, String cb,
            String cb1) {
        HashMap<String, Double> rolePriceSumMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("files/saveTRansaction.txt"))) {
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");

                // Check if the array has enough elements
                if (values.length >= 6) {
                    String dateStr = values[2];
                    Date transactionDate = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);

                    Date startDate = new SimpleDateFormat("dd.MM.yyyy").parse(startDateField);
                    Date endDate = new SimpleDateFormat("dd.MM.yyyy").parse(endDateField);

                    String role = values[5];

                    if (!transactionDate.before(startDate) && !transactionDate.after(endDate) &&
                            (cb.equals("All") || role.equals(cb))) {

                        double price = Double.parseDouble(values[3]);

                        if (cb1.equals("Daily")) {
                            String dailyKey = role + " " + dateStr;
                            updateRoleSum(rolePriceSumMap, dailyKey, price);
                        } else if (cb1.equals("Monthly")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(transactionDate);
                            int transactionMonth = calendar.get(Calendar.MONTH) + 1;

                            String monthlyKey = role + " " + transactionMonth;
                            updateRoleSum(rolePriceSumMap, monthlyKey, price);
                        } else if (cb1.equals("Yearly")) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(transactionDate);
                            int transactionYear = calendar.get(Calendar.YEAR);

                            String yearlyKey = role + " " + transactionYear;
                            updateRoleSum(rolePriceSumMap, yearlyKey, price);
                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return rolePriceSumMap;
    }

    private static void updateRoleSum(HashMap<String, Double> rolePriceSumMap, String key, double price) {
        if (!rolePriceSumMap.containsKey(key)) {
            rolePriceSumMap.put(key, price);
        } else {
            rolePriceSumMap.put(key, rolePriceSumMap.get(key) + price);
        }
    }

    public static void Performance(Stage primaryStage, Scene scene) {
        ArrayList<User> users = Methods.readUsers();
        String[] userr = new String[users.size()];

        for (int i = 0; i < users.size(); i++) {
            User currentUser = users.get(i);

            if ("Librarian".equals(currentUser.getRole())) {
                userr[i] = currentUser.getUsername();
            }
        }

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        Scene scene1 = new Scene(vbox, 400, 400);

        Label startDateLabel = new Label("Start Date:");
        TextField startDate = new TextField();
        Label endDateLabel = new Label("End Date:");
        TextField endDate = new TextField();
        Label label = new Label("Choose librarian and timeframe:");

        ChoiceBox<String> cb = new ChoiceBox<>();
        cb.getItems().add("All");
        cb.getItems().addAll(Arrays.stream(userr)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        ChoiceBox<String> cb1 = new ChoiceBox<>(FXCollections.observableArrayList("Daily", "Monthly", "Yearly"));

        Button check = new Button("Check");

        TextArea transactionTextArea = new TextArea();
        transactionTextArea.setEditable(false);

        check.setOnAction(e -> {
            if (startDate.getText().isEmpty() || endDate.getText().isEmpty() || cb.getSelectionModel().isEmpty()
                    || cb1.getSelectionModel().isEmpty()) {
                showAlert("Warning", "Please select both start date & end date & select both librarian and timeframe.");
            } else {
                if (isValidDateFormat(startDate.getText()) == false || isValidDateFormat(endDate.getText()) == false) {
                    showAlert("Warning", "Enter the valid date with format (dd.mm.yyyy)");
                    return;
                }
                String startDateValue = startDate.getText();
                String endDateValue = endDate.getText();
                String cbValue = cb.getValue();
                String cb1Value = cb1.getValue();
                try {
                    buttonCheck(primaryStage, startDateValue, endDateValue, cbValue, cb1Value, scene1);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(scene));

        vbox.getChildren().addAll(startDateLabel, startDate, endDateLabel, endDate, label, cb, cb1, check, back);

        primaryStage.setScene(scene1);
    }

    public static void buttonCheck(Stage primaryStage, String startDateField, String endDateField,
            String cb, String cb1, Scene scene) {

        HashMap<String, Double> rolePriceSumMap = calculateSum(startDateField, endDateField, cb, cb1);
        showTransactionTable(primaryStage, rolePriceSumMap, scene);
    }

    private static void showTransactionTable(Stage primaryStage, HashMap<String, Double> rolePriceSumMap, Scene scene) {
        System.out.println("Role Price Sum Map: " + rolePriceSumMap);

        TableView<Map.Entry<String, Double>> table = new TableView<>();

        TableColumn<Map.Entry<String, Double>, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKey()));

        TableColumn<Map.Entry<String, Double>, Double> sumColumn = new TableColumn<>("Total Price");
        sumColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getValue()).asObject());

        table.getColumns().addAll(roleColumn, sumColumn);

        ObservableList<Map.Entry<String, Double>> data = FXCollections.observableArrayList(rolePriceSumMap.entrySet());
        table.setItems(data);

        Button back = new Button("Back");

        back.setOnAction(e -> primaryStage.setScene(scene));

        Scene scene3 = new Scene(new VBox(table, back), 800, 700);
        primaryStage.setScene(scene3);
        primaryStage.show();

        System.out.println("Table should be visible now");
    }

    public static ArrayList<User> readUsers() {
        ArrayList<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("files/User.txt"))) {
            // Skip the header line
            br.readLine();

            String line;
            while ((line = br.readLine()) != null) {
                String[] userAttributes = line.split(",");

                // Ensure the userAttributes array has the expected number of elements before
                // creating a User
                if (userAttributes.length == 9) {
                    User user = new User(
                            userAttributes[0], userAttributes[1], userAttributes[2], userAttributes[3],
                            userAttributes[4], userAttributes[5], userAttributes[6], userAttributes[7],
                            userAttributes[8]);

                    users.add(user);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showALertBook() {

        List<Book> books = readBook();
        List<Book> tempBooks = new ArrayList<>();
        boolean verify = false;

        for (Book book : books) {
            if (book.getStock() < 5) {
                tempBooks.add(book);
                verify = true;
            }
        }

        if (verify == true) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Show the books under the stocks of 5");
            alert.setContentText("You have books with the stocks under 5\n" + "Do you want to check them?");

            ButtonType okButton = new ButtonType("OK");
            ButtonType cancelButton = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(okButton, cancelButton);

            alert.showAndWait().ifPresent(result -> {
                if (result == okButton) {
                    if (tempBooks.isEmpty()) {
                        System.out.println("No books available.");
                    } else {
                        Stage booksStage = new Stage();
                        VBox booksLayout = new VBox();

                        Scene booksScene = new Scene(booksLayout, 600, 400);
                        booksStage.setTitle("List of Books");

                        TableView<Book> table = new TableView<>();

                        // isbn column
                        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
                        isbnColumn.setMinWidth(100);
                        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

                        // title column
                        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
                        titleColumn.setMinWidth(200);
                        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

                        // author column
                        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
                        authorColumn.setMinWidth(100);
                        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

                        // category column
                        TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
                        categoryColumn.setMinWidth(100);
                        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

                        // purchasePrice column
                        TableColumn<Book, Double> purchasePriceColumn = new TableColumn<>("Purchase Price");
                        purchasePriceColumn.setMinWidth(100);
                        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("purchasedPrice"));

                        // date purchased column
                        TableColumn<Book, Date> datePurchasedColumn = new TableColumn<>("Date Purchased");
                        datePurchasedColumn.setMinWidth(100);
                        datePurchasedColumn.setCellValueFactory(new PropertyValueFactory<>("purchasedDate"));

                        // sellingPrice column
                        TableColumn<Book, Double> priceColumn = new TableColumn<>("Selling Price");
                        priceColumn.setMinWidth(100);
                        priceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

                        // stock column
                        TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stock");
                        stockColumn.setMinWidth(100);
                        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

                        // Set the columns to the table
                        table.getColumns().addAll(isbnColumn, titleColumn, authorColumn, categoryColumn,
                                purchasePriceColumn, datePurchasedColumn, priceColumn,
                                stockColumn);

                        // Add the data to the table
                        table.setItems(FXCollections.observableArrayList(tempBooks));
                        Button addbooks = new Button("Add Book");

                        addbooks.setOnAction(e -> Methods.addBook(booksStage, booksScene));
                        booksLayout.getChildren().addAll(table, addbooks);
                        booksStage.setScene(booksScene);
                        booksStage.show();
                    }

                }
            });

        }
    }

    public static void registering(Stage primaryStage, Scene scene) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setPadding(new Insets(20));
        ScrollPane registerUsersp = new ScrollPane(vbox);
        registerUsersp.setFitToWidth(true);
        registerUsersp.setFitToHeight(true);

        Scene scene1 = new Scene(registerUsersp, 700, 600);

        Label nameLabel = new Label("Name:");
        TextField name = new TextField();
        Label birthdayLabel = new Label("Birthday(with format:dd.mm.yyyy):");
        TextField birthday = new TextField();
        Label phoneLabel = new Label("Phone:");
        TextField phone = new TextField();
        Label emailLabel = new Label("Email:");
        TextField email = new TextField();
        Label salaryLabel = new Label("Salary:");
        TextField salary = new TextField();
        Label usernameLabel = new Label("Username:");
        TextField usernameTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Label verifyPasswordLabel = new Label("Verify password:");
        PasswordField verifyPasswordField = new PasswordField();
        ChoiceBox<String> access_level = new ChoiceBox<>(
                FXCollections.observableArrayList("Option1", "Option2", "Option3"));
        ChoiceBox<String> role = new ChoiceBox<>(
                FXCollections.observableArrayList("Librarian", "Manager"));

        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(scene));
        Button regis = new Button("Register");
        regis.setOnAction(e -> {
            if (isValidDateFormat(birthday.getText()) == false) {
                showAlert("Warning", "Enter the valid date with format (dd.mm.yyyy)");
                return;
            }
            if (areFieldsEmpty(name, birthday, phone, email, salary, usernameTextField, passwordField,
                    verifyPasswordField)
                    || (access_level.getValue() == null && role.getValue() == null)) {
                showAlert("Warning", "All fields must be filled in.");

            }
            if (!passwordField.getText().equals(verifyPasswordField.getText())) {
                showAlert("Warning", "Password and Verify Password do not match.");
                return;
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Confirm Registration");
                alert.setContentText(
                        "Do you want to registrate: " + name.getText() + " username: "
                                + usernameTextField.getText());

                ButtonType okButton = new ButtonType("OK");
                ButtonType cancelButton = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(okButton, cancelButton);

                String tempEmail = email.getText();
                String regex = "^(?=.*[A-Za-z]{2})(?=.*\\d{2})[A-Za-z\\d]*@epoka\\.edu\\.al$";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(tempEmail);

                String tempPhone = phone.getText();
                String regex1 = "^06[789]\\d{7}$";
                Pattern pattern1 = Pattern.compile(regex1);
                Matcher matcher1 = pattern1.matcher(tempPhone);

                if (matcher1.matches()) {
                    if (matcher.matches()) {
                        alert.showAndWait().ifPresent(result -> {
                            if (result == okButton) {
                                Methods.registeringUpdate(role.getValue(), usernameTextField.getText(),
                                        passwordField.getText(),
                                        name.getText(),
                                        birthday.getText(), tempPhone,
                                        tempEmail, salary.getText(),
                                        access_level.getValue());
                            }
                        });
                    } else {
                        showAlert("Warning", "Write the email in correct form");
                    }
                } else {
                    showAlert("Warning", "Write the phone number in correct form 06 7/8/9");
                }
            }
        });

        vbox.getChildren().addAll(role, usernameLabel, usernameTextField, passwordLabel, passwordField,
                verifyPasswordLabel, verifyPasswordField, nameLabel, name,
                birthdayLabel, birthday, phoneLabel, phone,
                emailLabel, email, salaryLabel, salary,
                access_level, back, regis);

        primaryStage.setScene(scene1);
    }

    public static void modify(Stage primaryStage, Scene scene) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        ScrollPane modifyEmp = new ScrollPane(vbox);
        modifyEmp.setFitToWidth(true);
        modifyEmp.setFitToHeight(true);
        Scene scene1 = new Scene(modifyEmp, 900, 700);

        ArrayList<User> users = Methods.readUsers();

        ChoiceBox<String> employees = new ChoiceBox<>();
        for (User currentUser : users) {
            if ("Librarian".equals(currentUser.getRole())) {
                employees.getItems().add(currentUser.getUsername());
            } else if ("Manager".equals(currentUser.getRole())) {
                employees.getItems().add(currentUser.getUsername());
            }
        }
        Label roleLabel = new Label("Role:");
        TextField role = new TextField();
        Label nameLabel = new Label("Name:");
        TextField name = new TextField();
        Label birthdayLabel = new Label("Birthday (with format: dd.mm.yyyy):");
        TextField birthday = new TextField();
        Label phoneLabel = new Label("Phone:");
        TextField phone = new TextField();
        Label emailLabel = new Label("Email:");
        TextField email = new TextField();
        Label salaryLabel = new Label("Salary:");
        TextField salary = new TextField();
        Label usernameLabel = new Label("Username:");
        TextField usernameTextField = new TextField();
        Label passwordLabel = new Label("Password:");
        TextField password = new TextField();
        Label access_levelLabel = new Label("Access Lvel");
        TextField access_level = new TextField();

        Button showInfoButton = new Button("Show Info");
        Button back = new Button("Back");
        Button modify = new Button("Modify");
        role.setEditable(false);
        showInfoButton.setOnAction(e -> {
            String employeeValue = employees.getValue();

            if (employeeValue == null || employeeValue.isEmpty()) {
                showAlert("Warning", "Please select the employee.");
            } else {
                for (User currentUser : users) {
                    if (employeeValue.equals(currentUser.getUsername())) {
                        role.setText(currentUser.getRole());
                        usernameTextField.setText(currentUser.getUsername());
                        password.setText(currentUser.getPassword());
                        name.setText(currentUser.getName());
                        birthday.setText(currentUser.getBirthday());
                        phone.setText(currentUser.getPhone());
                        email.setText(currentUser.getEmail());
                        salary.setText(currentUser.getSalary());
                        access_level.setText(currentUser.getAccessLevel());
                    }
                }
            }
        });

        back.setOnAction(e -> primaryStage.setScene(scene));
        modify.setOnAction(e -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm Modify");
            alert.setContentText(
                    "Do you want to modify: " + name.getText() + " which is " + role.getText());

            ButtonType okButton = new ButtonType("OK");
            ButtonType cancelButton = new ButtonType("Cancel");
            alert.getButtonTypes().setAll(okButton, cancelButton);

            alert.showAndWait().ifPresent(result -> {
                if (result == okButton) {
                    modifyUpdate(role.getText(), usernameTextField.getText(), password.getText(), name.getText(),
                            birthday.getText(), phone.getText(), email.getText(), salary.getText(),
                            access_level.getText());
                }
            });

        });

        vbox.getChildren().addAll(
                employees, showInfoButton,
                roleLabel, role,
                usernameLabel, usernameTextField,
                passwordLabel, password,
                nameLabel, name,
                birthdayLabel, birthday,
                phoneLabel, phone,
                emailLabel, email,
                salaryLabel, salary,
                access_levelLabel, access_level,
                modify, back);

        primaryStage.setScene(scene1);
    }

    public static void delete(Stage primaryStage, Scene scene) {
        primaryStage.setTitle("User Table View Example");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        ArrayList<User> users = Methods.readUsers();
        ObservableList<User> userList = FXCollections.observableArrayList();

        for (User currentUser : users) {
            if ("Librarian".equals(currentUser.getRole()) || "Manager".equals(currentUser.getRole())) {
                userList.add(currentUser);
            }
        }

        Scene scene1 = new Scene(vbox, 800, 700);

        TableView<User> table = new TableView<>();

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().addAll(roleColumn, usernameColumn, nameColumn);

        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(scene));

        Button delete = new Button("Delete");
        delete.setOnAction(e -> {
            User selectedItem = table.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Confirm Deletion");
                alert.setContentText(
                        "Do you want to delete: " + selectedItem.getName() + " which is " + selectedItem.getRole());

                ButtonType okButton = new ButtonType("OK");
                ButtonType cancelButton = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(okButton, cancelButton);

                alert.showAndWait().ifPresent(result -> {
                    if (result == okButton) {
                        userList.remove(selectedItem);
                        Methods.deleteUpdate(selectedItem.getUsername());

                    }
                });
            } else {
                showAlert("Warning", "Please select which employee do you want to remove");

            }
        });

        table.setItems(userList);

        vbox.getChildren().addAll(table, delete, back);

        primaryStage.setScene(scene1);
    }

    public static void registeringUpdate(String role, String username, String password, String name,
            String birthday, String phone, String email, String salary, String access_level) {
        ArrayList<User> tempuser = readUsers();
        for (User user : tempuser) {
            if (user.getUsername().equals(username)) {
                showAlert("Warning", "The username exists \n" + "Enter the new one");
                return;
            }
        }

        String line = role + "," + username + "," + password + "," + name + ","
                + birthday + "," + phone + "," + email + "," + salary + "," + access_level;
        String filePath = "files/User.txt";

        try {

            PrintWriter printWriter = new PrintWriter(new FileWriter(filePath, true));

            printWriter.println(line);

            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean areFieldsEmpty(TextField... fields) {
        for (TextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static void modifyUpdate(String role, String username, String password, String name,
            String birthday, String phone, String email, String salary, String access_level) {
        ArrayList<User> users = readUsers();
        ArrayList<User> tempUsers = new ArrayList<>();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setRole(role);
                user.setPassword(password);
                user.setName(name);
                user.setBirthday(birthday);
                user.setPhone(phone);
                user.setEmail(email);
                user.setSalary(salary);
                user.setAccessLevel(access_level);
            }
            tempUsers.add(user);
        }
        writeUsers(tempUsers);

    }

    public static void writeUsers(ArrayList<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("files/User.txt"))) {
            String line1 = "Type" + "," + "username" + "," + "password" + "," + "name" + "," + "birthday" + "," +
                    "phone" + "," + "email" + "," + "salary" + "," + "access_level" + "\n";
            writer.write(line1);
            for (User user : users) {
                String line = user.getRole() + "," + user.getUsername() + "," +
                        user.getPassword() + "," + user.getName() + "," +
                        user.getBirthday() + "," + user.getPhone() + "," +
                        user.getEmail() + "," + user.getSalary() + "," +
                        user.getAccessLevel() + "\n";
                writer.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void deleteUpdate(String username) {
        ArrayList<User> users = readUsers();
        ArrayList<User> tempUsers = new ArrayList<>();

        for (User user : users) {
            if (!user.getUsername().equals(username)) {
                tempUsers.add(user);
            }
        }

        writeUsers(tempUsers);
    }

    public static void addBook(Stage primaryStage, Scene scene) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        ScrollPane addingBookSP = new ScrollPane(vbox);
        addingBookSP.setFitToWidth(true);
        addingBookSP.setFitToHeight(true);
        Scene scene2 = new Scene(addingBookSP, 900, 700);
        List<Book> Books = readBook();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        ChoiceBox<String> books = new ChoiceBox<>();
        for (Book book : Books) {
            books.getItems().add(book.getTitle());
        }

        Label isbnLabel = new Label("ISBN:");
        TextField isbn = new TextField();
        isbn.setPromptText("Enter ISBN");
        Label bookNameLabel = new Label("Book Name:");
        TextField bookName = new TextField();
        bookName.setPromptText("Enter book name");
        Label categoryLabel = new Label("Category:");
        TextField category = new TextField();
        category.setPromptText("Enter Category");
        Label supplierLabel = new Label("Supplier:");
        TextField supplier = new TextField();
        supplier.setPromptText("Enter supplier");
        Label priceBoughtLabel = new Label("Price Bought:");
        TextField priceBought = new TextField();
        priceBought.setPromptText("Enter bought price");
        Label dateBoughtLabel = new Label("Date Bought (dd.mm.yyyy):");
        TextField dateBought = new TextField();
        dateBought.setPromptText("Enter bought date");
        Label priceSoldLabel = new Label("Selling Price:");
        TextField priceSold = new TextField();
        priceSold.setPromptText("Enter selling price");
        Label authorLabel = new Label("Author:");
        TextField author = new TextField();
        author.setPromptText("Enter author");
        Label quantityLabel = new Label("Quantity:");
        TextField quantity = new TextField();
        quantity.setPromptText("Enter quantity");
        Label imageLabel = new Label("Image Path");
        TextField imagePathh = new TextField();
        imagePathh.setPromptText("images/imagepath");

        Button show = new Button("Show Books");
        show.setOnAction(e -> {
            String bookValue = books.getValue();
            if (bookValue != null) {
                for (Book book : Books) {
                    if (bookValue.equals(book.getTitle())) {
                        isbn.setText(book.getISBN());
                        bookName.setText(book.getTitle());
                        category.setText(book.getCategory());
                        supplier.setText(book.getSupplier());
                        priceBought.setText(String.valueOf(book.getPurchasedPrice()));
                        dateBought.setText(dateFormat.format(book.getPurchasedDate()));
                        priceSold.setText(String.valueOf(book.getSellingPrice()));
                        author.setText(book.getAuthor());
                        // quantity.setText(String.valueOf(book.getStock()));
                        imagePathh.setText(book.getImagePath());

                        isbn.setEditable(false);
                        bookName.setEditable(false);
                        category.setEditable(false);
                        supplier.setEditable(false);
                        priceBought.setEditable(false);
                        dateBought.setEditable(false);
                        priceSold.setEditable(false);
                        author.setEditable(false);
                        imagePathh.setEditable(false);

                    }
                }
            }
        });
        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(scene));
        Button addBook = new Button("Add Book");
        addBook.setOnAction(e -> {
            if (areFieldsEmpty(isbn, bookName, category, supplier, priceBought, dateBought, priceSold, author,
                    quantity, imagePathh)) {
                showAlert("Warning", "All fields must be filled in.");
            } else {
                // String isbnText = isbn.getText();
                String imagePathText = imagePathh.getText();
                String quantityText = quantity.getText();
                String priceBoughtText = priceBought.getText();
                String sellingPriceText = priceSold.getText();
                if (!priceBoughtText.matches("^\\d+(\\.\\d+)?$")) {
                    showAlert("Invalid Input", "Please enter a valid double price.");
                    return;
                }
                if (!sellingPriceText.matches("^\\d+(\\.\\d+)?$")) {
                    showAlert("Invalid Input", "Please enter a valid double price.");
                    return;
                }

                if (!quantityText.matches("^\\d+$")) {
                    showAlert("Invalid Input", "Please enter a valid integer quantity.");
                    return;
                }

                if (!imagePathText.matches("^images/.+")) {
                    showAlert("Invalid Input", "Image path should start with 'files/'.");
                    return;
                }



                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText("Confirm Book Addition");
                confirmationAlert.setContentText(
                        "Do you want to add the book: " + bookName.getText() + " with ISBN: " + isbn.getText());

                ButtonType okButton = new ButtonType("OK");
                ButtonType cancelButton = new ButtonType("Cancel");
                confirmationAlert.getButtonTypes().setAll(okButton, cancelButton);

                confirmationAlert.showAndWait().ifPresent(result -> {
                    if (result == okButton) {
                        try {
                            Methods.addBookUpdate(isbn.getText(), bookName.getText(), category.getText(),
                                    supplier.getText(), Double.parseDouble(priceBought.getText()),
                                    dateFormat.parse(dateBought.getText()), Double.parseDouble(priceSold.getText()),
                                    author.getText(), Integer.parseInt(quantity.getText()), imagePathh.getText());
                        } catch (NumberFormatException | ParseException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });

        vbox.getChildren().addAll(books, show, isbnLabel, isbn, bookNameLabel, bookName, categoryLabel, category,
                supplierLabel,
                supplier,
                priceBoughtLabel, priceBought, priceSoldLabel, priceSold, dateBoughtLabel, dateBought,
                authorLabel, author, quantityLabel, quantity, imageLabel, imagePathh, back, addBook);

        primaryStage.setScene(scene2);
    }

    public static void addBookUpdate(String isbn, String title, String category, String supplier, double purchasedPrice,
            Date purchasedDate, double sellingPrice, String author, int stock, String image) {
        List<Book> books = Methods.readBook();
        List<Book> tempBook = new ArrayList<>();

        boolean found = false;

        for (Book book : books) {
            if (book.getISBN().equals(isbn)) {
                book.setISBN(isbn);
                book.setTitle(title);
                book.setCategory(category);
                book.setSupplier(supplier);
                book.setPurchasedPrice(purchasedPrice);
                book.setPurchasedDate(purchasedDate);
                book.setSellingPrice(sellingPrice);
                book.setAuthor(author);
                book.setStock(book.getStock() + stock);
                book.setImagePath(image);
                System.out.println("The book modified");

                found = true;
            }
            tempBook.add(book);
        }

        if (!found) {
            // If the book with the given ISBN was not found, add a new book
            Book newBook = new Book();
            newBook.setISBN(isbn);
            newBook.setTitle(title);
            newBook.setCategory(category);
            newBook.setSupplier(supplier);
            newBook.setPurchasedPrice(purchasedPrice);
            newBook.setPurchasedDate(purchasedDate);
            newBook.setSellingPrice(sellingPrice);
            newBook.setAuthor(author);
            newBook.setStock(stock);
            newBook.setImagePath(image);
            System.out.println("The book added");

            tempBook.add(newBook);
        }

        double totalPrice = stock * purchasedPrice;
        Date dateOfNewBook = new Date();

        Methods.saveBooksToFile(tempBook);
        Methods.saveAddBookFile(totalPrice, dateOfNewBook);
    }

    public static Scene createOrderConfirmationScene(Stage primaryStage, FirstWindow firstWindow, double totalPr,
            List<String> isbnListt, List<String> quantityListt, Date order_date) {
        List<Order> orders = readOrder();
        Order tempOrder = new Order();

        GridPane orderConfirmationGrid = new GridPane();
        orderConfirmationGrid.setAlignment(Pos.TOP_CENTER);
        orderConfirmationGrid.setVgap(20);

        Label orderLabel = new Label("Please fill in to order");
        orderLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        orderLabel.setStyle("-fx-text-fill: green;");
        orderConfirmationGrid.add(orderLabel, 0, 0);
        orderConfirmationGrid.add(new Label(" Enter your name:"), 0, 1);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        orderConfirmationGrid.add(nameField, 1, 1);

        orderConfirmationGrid.add(new Label(" Enter your surname:"), 0, 2);
        TextField surnameField = new TextField();
        surnameField.setPromptText("Surname");
        orderConfirmationGrid.add(surnameField, 1, 2);

        orderConfirmationGrid.add(new Label(" Enter your email:"), 0, 3);
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        orderConfirmationGrid.add(emailField, 1, 3);

        orderConfirmationGrid.add(new Label(" Enter your phone number:"), 0, 4);
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone number");
        orderConfirmationGrid.add(phoneNumberField, 1, 4);

        // Order tempOrder = new Order();
        // double totalP = tempOrder.getTotalPrice();

        Label totalPricewV = new Label("Price without VAT: $" + 0.8 * totalPr);
        orderConfirmationGrid.add(totalPricewV, 0, 5);
        Label vatPrice = new Label("VAT: $" + 0.2 * totalPr);
        orderConfirmationGrid.add(vatPrice, 0, 6);

        Label totalPriceLabel = new Label("Total price $" + String.valueOf(totalPr));

        orderConfirmationGrid.add(totalPriceLabel, 0, 7);


        Button confirmOrder = new Button("Confirm Order");
        confirmOrder.setOnAction(e -> {
            // Get user input from the text fields
            if (areFieldsEmpty(nameField, surnameField, emailField, phoneNumberField)) {
                showAlert("Warning", "All fields must be filled in.");
            } else {
                String name = nameField.getText();
                String surname = surnameField.getText();
                String email = emailField.getText();
                String phone = phoneNumberField.getText();


                if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                    showAlert("Invalid Email", "Please enter a valid email address.");
                    return;
                }

                if (!phone.matches("^06[789]\\d{7}$")) {
                    showAlert("Invalid Phone Number", "Please enter a valid phone number starting with 069/8/7");
                    return;
                }

                // Create an Order object with the user input
                Order order1 = new Order(name, surname, phone, email, totalPr, isbnListt, quantityListt, order_date);

                orders.add(order1);
                saveOrdersToFile(orders); // Save the updated list of orders to the file

                Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                confirmationAlert.setTitle("Order Confirmation");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Order confirmed!\nThank you!");
                confirmationAlert.showAndWait();

                Main.showMainScene(primaryStage);
                primaryStage.close();
            }
        });


        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            Main.showMainScene(primaryStage);
            primaryStage.close();
        });

        orderConfirmationGrid.add(backButton, 0, 10);
        orderConfirmationGrid.add(confirmOrder, 1, 10);

        return new Scene(orderConfirmationGrid, 450, 450);
    }

    public static void saveOrdersToFile(List<Order> orders) {
        File file = new File("files/request.dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(orders);
            System.out.println("Orders written to file: request.dat");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveToBill(Stage primaryStage,Order tempOrd, Integer orderID) {

        try (PrintWriter writer = new PrintWriter(new FileWriter("files/bill.txt"))) {

            String line = orderID + "," + tempOrd.getName() + "," + tempOrd.getSurname() + "," + tempOrd.getPhone()
                    + ","
                    + tempOrd.getEmail() + "," + tempOrd.getTotalPrice() + "," + tempOrd.getIsbnList() + ","
                    + tempOrd.getQuantityList();
            writer.write(line);
            printBill(primaryStage,tempOrd, orderID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printBill(Stage primaryStage,Order tempOrd, Integer orderID) {
        String fileName = "files/bills/bill_" + orderID + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("+====================================================+\n" +
                    "|     _ _____ _  __                                  |\n" +
                    "|    | | ____| |/ /                                  |\n" +
                    "| _  | |  _| | ' /                                   |\n" +
                    "|| |_| | |___| . \\                                   |\n" +
                    "| \\___/|_____|_|\\_\\ _  ______ _____ ___  ____  _____ |\n" +
                    "|| __ ) / _ \\ / _ \\| |/ / ___|_   _/ _ \\|  _ \\| ____||\n" +
                    "||  _ \\| | | | | | | ' /\\___ \\ | || | | | |_) |  _|  |\n" +
                    "|| |_) | |_| | |_| | . \\ ___) || || |_| |  _ <| |___ |\n" +
                    "||____/ \\___/ \\___/|_|\\_\\____/ |_| \\___/|_| \\_\\_____||\n" +
                    "+====================================================+");
            writer.println("=====================================================|");
            writer.println("                      ORDER BILL                     |");
            writer.println("=====================================================|");
            writer.println("Order ID: " + orderID);
            writer.println("Name: " + tempOrd.getName());
            writer.println("Surname: " + tempOrd.getSurname());
            writer.println("Phone: " + tempOrd.getPhone());
            writer.println("Email: " + tempOrd.getEmail());
            writer.println("Date: " + tempOrd.getOrderDate());
            writer.println("=====================================================|");
            writer.printf("%-20s%-10s%n", "  ISBN", "  Quantity");
            writer.println("-----------------------------------------------------|");

            List<String> isbnList = tempOrd.getIsbnList();
            List<String> quantityList = tempOrd.getQuantityList();

            // Assuming both lists have the same size
            for (int i = 0; i < isbnList.size(); i++) {
                writer.printf("%-26s%-10s%n", isbnList.get(i), quantityList.get(i));
            }

            writer.println("=====================================================|");
            writer.printf("%-25s$%-10.2f%n", "Total Price:", tempOrd.getTotalPrice());
            writer.println("+=====================================================+");

        } catch (IOException e) {
            e.printStackTrace();
        }
        showBill(primaryStage,orderID);
    }

    public static void getOrders(Stage primaryStage,User user, Scene scene) throws ParseException {
        List<Order> orders = readOrder();
        Button check = new Button("Print bill");
        Button delete = new Button("Delete Request");
        Button back = new Button("Back");

        if (orders.isEmpty()) {
            System.out.println("No Orders available.");
            showAlert("Warning", "No Orders available.");
        } else {
            Stage orderStage = new Stage();
            orderStage.setTitle("List of Orders");

            TableView<Order> table1 = new TableView<>();

            check.setOnAction(e -> {
                Order selectedItem = table1.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Confirm Order");
                    alert.setContentText(
                            "Do you want to confirm order of : " + selectedItem.getName() + "\n of "
                                    + selectedItem.getIsbnList() + " \nwith total price:"
                                    + selectedItem.getTotalPrice());

                    ButtonType okButton = new ButtonType("OK");
                    ButtonType cancelButton = new ButtonType("Cancel");
                    alert.getButtonTypes().setAll(okButton, cancelButton);

                    alert.showAndWait().ifPresent(result -> {
                        if (result == okButton) {
                            Random orderId = new Random();
                            int orderID = orderId.nextInt();
                            saveToBill(primaryStage,selectedItem, orderID);
                            try {
                                saveTransaction(user, selectedItem, orderID);
                            } catch (ParseException e1) {

                                e1.printStackTrace();
                            }
                            deleteBookQuantity(selectedItem);
                            orders.remove(selectedItem);
                            saveOrdersToFile(orders);
                        }
                    });
                } else {
                    showAlert("Warning", "Please select which order do you want to check");

                }
            });


            delete.setOnAction( e -> {
                Order selectedItem = table1.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Remove Order");
                    alert.setContentText(
                            "Do you want to delete the order placed by : " + selectedItem.getName() + "\n of "
                                    + selectedItem.getIsbnList() + " \nwith total price:"
                                    + selectedItem.getTotalPrice());

                    ButtonType okButton = new ButtonType("OK");
                    ButtonType cancelButton = new ButtonType("Cancel");
                    alert.getButtonTypes().setAll(okButton, cancelButton);

                    alert.showAndWait().ifPresent(result -> {
                        if (result == okButton) {
                            orders.remove(selectedItem);
                            saveOrdersToFile(orders);

                        }
                    });
                } else {
                    showAlert("Warning", "Please select which order do you want to delete");

                }
            });


            // name
            TableColumn<Order, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setMinWidth(100);
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            // email column
            TableColumn<Order, String> emailColumn = new TableColumn<>("Email");
            emailColumn.setMinWidth(100);
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

            // total price column
            TableColumn<Order, Double> tpriceColumn = new TableColumn<>("Total Price");
            tpriceColumn.setMinWidth(100);
            tpriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

            // isbn column
            TableColumn<Order, String> isbnColumn = new TableColumn<>("ISBN List");
            isbnColumn.setMinWidth(200);
            isbnColumn.setCellValueFactory(cellData -> {
                List<String> isbnList = cellData.getValue().getIsbnList();
                // Convert the list to a readable string format
                String isbnString = String.join("; ", isbnList);
                return new SimpleStringProperty(isbnString);
            });
            // date column
            TableColumn<Order, Date> dateColumn = new TableColumn<>("Date");
            dateColumn.setMinWidth(100);
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

            // quantity column
            TableColumn<Order, String> quantityColumn = new TableColumn<>("Quantity List");
            quantityColumn.setMinWidth(100);
            quantityColumn.setCellValueFactory(cellData -> {
                List<String> quantityList = cellData.getValue().getQuantityList();
                // Convert the list to a readable string format
                String quantityString = String.join("; ", quantityList);
                return new SimpleStringProperty(quantityString);
            });

            // Set the columns to the table

            table1.getColumns().addAll(nameColumn, emailColumn, tpriceColumn, isbnColumn, quantityColumn, dateColumn);

            // Add the data to the table
            table1.setItems(FXCollections.observableArrayList(orders));

            VBox booksLayout = new VBox();
            back.setOnAction(e->primaryStage.setScene(scene));
            booksLayout.getChildren().addAll(table1, check,delete, back);

            Scene orderScene = new Scene(booksLayout, 800, 600);
            primaryStage.setScene(orderScene);
         //   orderStage.show();
        }
    }

    public static List<String> readRequest() {
        List<String> requests = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/request.dat"))) {

            List<Order> orderList = (List<Order>) ois.readObject();

            for (Order order : orderList) {
                String request = order.getName() + "," + order.getSurname() + "," + order.getPhone() + ","
                        + order.getEmail();
                requests.add(request);
                System.out.println("Order loaded from file: " + request);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return requests;
    }

    public static List<Order> readOrder() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("files/request.dat"))) {

            List<Order> orders = (List<Order>) ois.readObject();
            System.out.println("Orders loaded from file: request.dat");
            return orders;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<String> readAddBookFile() {
        List<String> entries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("files/addBook.txt"))) {

            String line;
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                entries.add(line);
            }
            System.out.println("The data  read from this file:files/addBook.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entries;
    }

    public static void saveAddBookFile(double totalPrice, Date dateOfNewBook) {

        try (PrintWriter output = new PrintWriter(new FileWriter("files/addBook.txt", true))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String formattedDate = dateFormat.format(dateOfNewBook);
            String dataToWrite = String.format(" %.2f, %s%n", totalPrice, formattedDate);
            output.write(dataToWrite);
            System.out.println("The data are save in this file:files/addBook.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void finance(Stage primaryStage, Scene scene) {
        VBox finance = new VBox(10);
        finance.setPadding(new Insets(20));
        Scene sceneFinance = new Scene(finance, 400, 400);

        Label startDateLabel = new Label("Start Date");
        DatePicker startDatePicker = new DatePicker();

        Label endDateLabel = new Label("End Date");
        DatePicker endDatePicker = new DatePicker();

        Button check = new Button("Check");
        Button back = new Button("Back");

        check.setOnAction(e -> {
            LocalDate startLocalDate = startDatePicker.getValue();
            LocalDate endLocalDate = endDatePicker.getValue();

            if (startLocalDate == null || endLocalDate == null) {
                showAlert("Warning", "Select valid dates");
                return;
            }

            String startDate = startLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            String endDate = endLocalDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            try {
                Methods.showFinance(primaryStage, sceneFinance, startDate, endDate);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });

        back.setOnAction(e -> {
            primaryStage.setScene(scene);
        });

        finance.getChildren().addAll(startDateLabel, startDatePicker, endDateLabel, endDatePicker, check, back);

        primaryStage.setScene(sceneFinance);
        primaryStage.show();
    }

    public static boolean isValidDateFormat(String date) {
        String datePattern = "^\\d{2}\\.\\d{2}\\.\\d{4}$";
        Pattern pattern = Pattern.compile(datePattern);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public static List<String> calculateFinance(String startDate, String endDate) throws ParseException {
        List<String> temp = readAddBookFile();
        ArrayList<User> tempUsers = readUsers();
        List<TransactionData> tempTransaction = readTransactionFile();
        double totalPrice = 0;
        double totalSalary = 0;
        double totalSale = 0;

        LocalDate startDatee = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalDate endDatee = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        long monthsBetween = ChronoUnit.MONTHS.between(startDatee, endDatee);

        for (String line : temp) {
            String[] parts = line.split(", ");
            double bookPrice = Double.parseDouble(parts[0]);
            String dateStr = parts[1];

            LocalDate dateOfNewBook = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            if (!dateOfNewBook.isBefore(startDatee) && !dateOfNewBook.isAfter(endDatee)) {
                totalPrice += bookPrice;
            }
        }

        for (User user : tempUsers) {
            totalSalary += Double.parseDouble(user.getSalary());
        }

        if (monthsBetween > 0) {
            totalSalary *= monthsBetween; // This line is causing the issue
        } else {
            totalSalary *= -monthsBetween; // Handle negative monthsBetween
        }

        for (TransactionData transc : tempTransaction) {
            String date = transc.getDate();
            LocalDate dateOfTransaction = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            if (!dateOfTransaction.isBefore(startDatee) && !dateOfTransaction.isAfter(endDatee)) {
                totalSale += transc.getTotalPrice();
            }
        }

        List<String> financen = new ArrayList<>();
        financen.add(String.valueOf(totalPrice));
        financen.add(String.valueOf(totalSalary));
        financen.add(String.valueOf(totalSale));

        // System.out.println(financen);

        return financen;
    }

    public static List<TransactionData> readTransactionFile() {
        List<TransactionData> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("files/saveTRansaction.txt"))) {
            String line;
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                int orderId = Integer.parseInt(parts[0].trim());
                String isbn = parts[1].trim();
                String date = parts[2].trim();
                double totalPrice = Double.parseDouble(parts[3].trim());
                String seller = parts[5].trim();

                TransactionData transaction = new TransactionData(orderId, isbn, date, totalPrice,
                        seller);
                transactions.add(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public static void showFinance(Stage primaryStage, Scene scene, String startDate, String endDate)
            throws ParseException {
        primaryStage.setTitle("Financial Data Table");
        List<String> showfinance = calculateFinance(startDate, endDate);
        VBox finance = new VBox(10);
        finance.setPadding(new Insets(20));
        Scene sceneFinance = new Scene(finance, 400, 400);

        Label totalPriceLabel = new Label("Money Spent on Books");
        TextField totalPrice = new TextField();
        Label totalSalaryLabel = new Label("Total Salary Expenses");
        TextField totalSalary = new TextField();
        Label totalSaleLabel = new Label("Money Made From Books");
        TextField totalSale = new TextField();
        Label profitLabel = new Label("Profit");
        TextField profit = new TextField();

        String firstValue = showfinance.get(0);
        String secondValue = showfinance.get(1);
        String thirdValue = showfinance.get(2);

        double profitValue = Double.parseDouble(thirdValue)
                - (Double.parseDouble(secondValue) + Double.parseDouble(firstValue));

        totalPrice.setText(firstValue);
        totalSalary.setText(secondValue);
        totalSale.setText(thirdValue);
        profit.setText(String.valueOf(profitValue));

        totalPrice.setEditable(false);
        totalSalary.setEditable(false);
        totalSale.setEditable(false);
        profit.setEditable(false);

        Button back = new Button("Back");
        back.setOnAction(e -> {
            primaryStage.setScene(scene);
        });

        finance.getChildren().addAll(totalPriceLabel, totalPrice, totalSalaryLabel, totalSalary, totalSaleLabel,
                totalSale, profitLabel, profit, back);

        primaryStage.setScene(sceneFinance);

    }

    public static List<Book> searchBooks(String searchBy, String searchTerm) {
        List<Book> allBooks = Methods.readBook();
        List<Book> searchResults = new ArrayList<>();

        for (Book book : allBooks) {
            String fieldValue = "";

            switch (searchBy) {
                case "Title":
                    fieldValue = book.getTitle().toLowerCase();
                    break;
                case "Author":
                    fieldValue = book.getAuthor().toLowerCase();
                    break;
                case "Isbn":
                    fieldValue = book.getISBN().toLowerCase();
                    break;
                default:
                    break;
            }

            if (fieldValue.contains(searchTerm.toLowerCase())) {
                searchResults.add(book);
            }
        }

        return searchResults;
    }

    public static void deleteBookQuantity(Order order) {
        List<Book> books = Methods.readBook();
        List<Book> tempBooks = new ArrayList<>();

        for (Book book : books) {
            for (int i = 0; i < order.getIsbnList().size(); i++) {
                if (book.getISBN().equals(order.getIsbnList().get(i))) {
                    int newStock = book.getStock() - Integer.parseInt(order.getQuantityList().get(i));
                    book.setStock(newStock);
                    int soldBooks= book.getBooksSold();
                    soldBooks=soldBooks+Integer.parseInt(order.getQuantityList().get(i));
                    book.setBooksSold(soldBooks);
                    System.out.println("Stock modified");
                }
            }
            tempBooks.add(book);
        }
        Methods.saveBooksToFile(tempBooks);
    }

    public static void askPermissionView(Stage primaryStage, Scene previousScene, User user) {
        primaryStage.setTitle("Permission View");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TableView<Permission> table = new TableView<>();
        TableColumn<Permission, String> permissionsColumn = new TableColumn<>("PERMISSIONS LIST");
        permissionsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));

        table.setItems(FXCollections.observableArrayList(Permission.values()));

        table.getColumns().add(permissionsColumn);

        Button backButton = new Button("Back");
        Button makeRequestOfPermissionButton = new Button("Request permission");

        backButton.setOnAction(e -> primaryStage.setScene(previousScene));
        makeRequestOfPermissionButton.setOnAction(l -> {

            Permission selectedItem = table.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Permission");
                alert.setHeaderText("Permission");
                alert.setContentText(
                        "Do you want to request " + selectedItem.name());

                ButtonType okButton = new ButtonType("OK");
                ButtonType cancelButton = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(okButton, cancelButton);

                alert.showAndWait().ifPresent(result -> {
                    if (result == okButton) {
                        boolean verify = false;
                        savePermissionFile(selectedItem, user, verify);
                        makeRequestOfPermissionButton.setDisable(true);
                    }
                });
            }
        });

        layout.getChildren().addAll(table, backButton, makeRequestOfPermissionButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void savePermissionFile(Permission permission, User user, boolean verify) {

        try (PrintWriter writer = new PrintWriter(new FileWriter("files/Permission.txt", true))) {
            String line = "\n" + permission.name() + "," + user.getUsername() + "," + verify;
            writer.write(line);
            System.out.println("The data are save in this file files/Permission.txt ");

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static List<PermissionEntry> readPermissionFile() {
        List<PermissionEntry> permissionEntries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("files/Permission.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    Permission permission = Permission.valueOf(parts[0]);
                    String username = parts[1];
                    boolean verify = Boolean.parseBoolean(parts[2]);
                    PermissionEntry entry = new PermissionEntry(permission.toString(), username, verify);
                    permissionEntries.add(entry);

                    System.out.println("The data are read from files/Permission.txt ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return permissionEntries;
    }

    public static void approvePermission(Stage primaryStage, Scene scene, User user) {
        List<PermissionEntry> permissionEntries = readPermissionFile();

        TableView<PermissionEntry> tableView = new TableView<>();
        TableColumn<PermissionEntry, String> permissionColumn = new TableColumn<>("Permission");
        TableColumn<PermissionEntry, String> usernameColumn = new TableColumn<>("Username");
        TableColumn<PermissionEntry, Boolean> verifyColumn = new TableColumn<>("Verify");

        permissionColumn.setCellValueFactory(new PropertyValueFactory<>("permission"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        verifyColumn.setCellValueFactory(new PropertyValueFactory<>("verify"));

        tableView.getColumns().addAll(permissionColumn, usernameColumn, verifyColumn);

        ObservableList<PermissionEntry> data = FXCollections.observableArrayList(permissionEntries);

        tableView.setItems(data);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(scene));
        Button approveButton = new Button("Approve");

        approveButton.setOnAction(l -> {

            PermissionEntry selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Confirm Permission");
                alert.setContentText(
                        "Do you want to approve this permission to " + selectedItem.getUsername());

                ButtonType okButton = new ButtonType("OK");
                ButtonType cancelButton = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(okButton, cancelButton);

                alert.showAndWait().ifPresent(result -> {
                    if (result == okButton) {
                        boolean verify = true;
                        savePermissionFile(selectedItem, verify);
                    }
                });
            }
        });

        VBox vbox = new VBox(tableView, backButton, approveButton);
        Scene scene1 = new Scene(vbox, 400, 300);

        primaryStage.setScene(scene1);
        primaryStage.setTitle("Permission Table");
        primaryStage.show();
    }

    public static void savePermissionFile(PermissionEntry permissionEntry, boolean verify) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("files/Permission.txt", true))) {
            String line = "\n" + permissionEntry.getPermission() + "," + permissionEntry.getUsername() + "," + verify;
            writer.write(line);
            System.out.println("The data are saved in this file files/Permission.txt ");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void permission(User user, Stage primaryStage, Scene scene) {
        List<PermissionEntry> permissionEntries = readPermissionFile();

        String tempPermission = "You dont have any permission approved";

        for (PermissionEntry per : permissionEntries) {
            if (user.getUsername().equals(per.getUsername()) && per.isVerify()) {
                tempPermission = "Use this permission: " + per.getPermission();
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Permission");
        alert.setHeaderText("Permission");
        alert.setContentText(" " + tempPermission);

        ButtonType okButton = new ButtonType("OK");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(result -> {
            if (result == okButton) {
                for (PermissionEntry per : permissionEntries) {
                    if (user.getUsername().equals(per.getUsername()) && per.isVerify()) {

                        if ("CHECK_BOOK".equals(per.getPermission())) {
                            try {
                                Methods.getBooks();
                                deletePermissionEntry(user.getUsername(), per.getPermission());
                            } catch (ParseException e) {

                                e.printStackTrace();
                            }
                        } else if ("CREATE_BILL".equals(per.getPermission())) {
                            try {
                                Methods.getOrders(primaryStage,user,scene);
                                deletePermissionEntry(user.getUsername(), per.getPermission());
                                return;
                            } catch (ParseException e) {
                                e.printStackTrace();

                            }

                        } else if ("ADD_BOOK".equals(per.getPermission())) {
                            Methods.addBook(primaryStage, scene);
                            deletePermissionEntry(user.getUsername(), per.getPermission());
                        } else if ("DELETE_EMPLOYEE".equals(per.getPermission())) {
                            Methods.delete(primaryStage, scene);
                            deletePermissionEntry(user.getUsername(), per.getPermission());
                        } else if ("ADD_MEMBER".equals(per.getPermission())) {
                            Methods.registering(primaryStage, scene);
                            deletePermissionEntry(user.getUsername(), per.getPermission());
                        } else if ("MODIFY_MEMBER".equals(per.getPermission())) {
                            Methods.modify(primaryStage, scene);
                            deletePermissionEntry(user.getUsername(), per.getPermission());
                        } else if ("PERFORMANCE_CHECK".equals(per.getPermission())) {
                            Methods.Performance(primaryStage, scene);
                            deletePermissionEntry(user.getUsername(), per.getPermission());
                        }else if("STATISTICS".equals(per.getPermission())){
                            Methods.bookStatistics(primaryStage, scene);
                            deletePermissionEntry(user.getUsername(), per.getPermission());
                        }


                    }
                }
            }
        });

    }

    public static void deletePermissionEntry(String username, String permission) {
        List<PermissionEntry> tempReader = readPermissionFile();

        try (PrintWriter writer = new PrintWriter(new FileWriter("files/Permission.txt"))) {
            for (PermissionEntry per : tempReader) {
                if (!(username.equals(per.getUsername()) && permission.equals(per.getPermission()))) {
                    String line = per.getPermission() + "," + per.getUsername() + "," + per.isVerify();
                    writer.println(line);
                }
            }
            System.out.println("The data are deleted from this file files/Permission.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void disableMenuItem(MenuItem temp, User user) {
        List<PermissionEntry> tempReader = readPermissionFile();
        for (PermissionEntry per : tempReader) {
            if (user.getUsername().equals(per.getUsername())) {
                temp.setDisable(true);
            } else {
                temp.setDisable(false);
            }
        }
    }


    public static void bookStatistics(Stage primaryStage, Scene scene) {
        List<Book> books = Methods.readBook();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Book Sales Chart");
        xAxis.setLabel("Book Title");
        yAxis.setLabel("Number of Books Sold");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Books Sold");

        for (Book book : books) {
            series.getData().add(new XYChart.Data<>(book.getTitle(), book.getBooksSold()));
        }

        ObservableList<XYChart.Series<String, Number>> data = FXCollections.observableArrayList();
        data.add(series);
        barChart.setData(data);

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(scene));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(barChart);


        BorderPane.setAlignment(backButton, Pos.CENTER_LEFT);
        borderPane.setBottom(backButton);

        Scene newScene = new Scene(borderPane, 800, 600);

        primaryStage.setScene(newScene);
        primaryStage.show();
    }

    public static void showBill(Stage primaryStage, int orderID) {
        StackPane root = new StackPane();
        Stage billWindow = new Stage();

        // Create a TextArea to display the content
        TextArea textArea = new TextArea();
        textArea.setEditable(false);

        Path billFilePath = Paths.get("files/bills/bill_" + orderID + ".txt");

        if (Files.exists(billFilePath)) {
            String content = readBillContent(billFilePath);
            textArea.setText(content);
        } else {
            textArea.setText("Bill not found for Order ID: " + orderID);
        }

        root.getChildren().add(textArea);

        Scene scene2 = new Scene(root, 400, 600);

        billWindow.setTitle("Bill Viewer");
        billWindow.setScene(scene2);
        billWindow.show();

    }
    private static String readBillContent(Path filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }

    public static void centerScene(Stage stage, Scene scene) {
        // Get the primary screen bounds
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        // Calculate the center coordinates
        double centerX = (screenWidth - scene.getWidth()) / 2;
        double centerY = (screenHeight - scene.getHeight()) / 2;

        // Set the stage position to the center
        stage.setX(centerX);
        stage.setY(centerY);
    }

}
