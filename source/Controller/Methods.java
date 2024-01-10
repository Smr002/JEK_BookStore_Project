package source.Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.scene.control.ScrollPane;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import source.Model.Book;
import source.Main.Main;
import source.Model.User;
import source.View.FirstWindow;
import javafx.beans.property.SimpleStringProperty;

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

            // category column
            TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
            categoryColumn.setMinWidth(100);
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

            // sellingPrice column
            TableColumn<Book, Double> priceColumn = new TableColumn<>("Selling Price");
            priceColumn.setMinWidth(100);
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

            // stock column
            TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stock");
            stockColumn.setMinWidth(100);
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

            // Set the columns to the table
            table.getColumns().addAll(isbnColumn, titleColumn, categoryColumn, priceColumn, stockColumn);

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

    public void saveTransaction(User user) throws ParseException {
        List<Book> booksList = readBook();
        List<String> requests = readRequests();

        String filePath = "files/saveTRansaction.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            Map<String, Integer> isbnQuantityMap = processRequests(requests);

            for (Map.Entry<String, Integer> entry : isbnQuantityMap.entrySet()) {
                String isbn = entry.getKey();
                int quantity = entry.getValue();

                Book matchingBook = findBookByISBN(booksList, isbn);
                if (matchingBook != null) {
                    Date date = new Date();
                    Random orderId = new Random();
                    writer.write(orderId.nextInt() + "," + isbn + "," + matchingBook.getTitle() + ","
                            + matchingBook.getAuthor() + ","
                            + date + "," + matchingBook.getSellingPrice() * quantity + "," + quantity + ","
                            + user.getUsername() + "\n");

                    System.out.println("Content has been written to the file: " + filePath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Integer> processRequests(List<String> requests) {
        /*
         * This declares a map where keys are of type String and values are of type
         * Integer.
         * In Java, a Map is a collection that stores key-value pairs. In this case, the
         * keys are ISBNs
         * (which are strings representing book identifiers),
         * and the values are integers representing the quantity of books
         * associated with each ISBN.
         */
        Map<String, Integer> isbnQuantityMap = new HashMap<>();

        for (String request : requests) {
            String[] columns = request.split(",");
            String temp = columns[1].trim();
            isbnQuantityMap.put(temp, isbnQuantityMap.getOrDefault(temp, 0) + 1);
        }

        return isbnQuantityMap;
    }

    private Book findBookByISBN(List<Book> booksList, String isbn) {
        for (Book book : booksList) {
            if (book.getISBN().equals(isbn)) {
                return book;
            }
        }
        return null;
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
                String[] values = line.split(",");
                String dateStr = values[4];
                Date transactionDate = new SimpleDateFormat("dd.MM.yyyy").parse(dateStr);

                Date startDate = new SimpleDateFormat("dd.MM.yyyy").parse(startDateField);
                Date endDate = new SimpleDateFormat("dd.MM.yyyy").parse(endDateField);

                String role = values[7];

                if (!transactionDate.before(startDate) && !transactionDate.after(endDate) &&
                        (cb.equals("All") || role.equals(cb))) {

                    double price = Double.parseDouble(values[5]);

                    if (cb1.equals("Daily")) {
                        String dailyKey = role + " " + dateStr;
                        updateRoleSum(rolePriceSumMap, dailyKey, price);
                    } else if (cb1.equals("Monthly")) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(transactionDate);
                        int transactionMonth = calendar.get(Calendar.MONTH);

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

                        // category column
                        TableColumn<Book, String> categoryColumn = new TableColumn<>("Category");
                        categoryColumn.setMinWidth(100);
                        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

                        // sellingPrice column
                        TableColumn<Book, Double> priceColumn = new TableColumn<>("Selling Price");
                        priceColumn.setMinWidth(100);
                        priceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

                        // stock column
                        TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stock");
                        stockColumn.setMinWidth(100);
                        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

                        // Set the columns to the table
                        table.getColumns().addAll(isbnColumn, titleColumn, categoryColumn, priceColumn,
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
        Scene scene1 = new Scene(vbox, 700, 600);

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
        ChoiceBox<String> access_level = new ChoiceBox<>(
                FXCollections.observableArrayList("Option1", "Option2", "Option3"));
        ChoiceBox<String> role = new ChoiceBox<>(
                FXCollections.observableArrayList("Librarian", "Manager"));

        Button back = new Button("Back");
        back.setOnAction(e -> primaryStage.setScene(scene));
        Button regis = new Button("Register");
        regis.setOnAction(e -> {
            if (areFieldsEmpty(name, birthday, phone, email, salary, usernameTextField, passwordField)) {
                showAlert("Warning", "All fields must be filled in.");
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

        vbox.getChildren().addAll(role, usernameLabel, usernameTextField, passwordLabel, passwordField, nameLabel, name,
                birthdayLabel, birthday, phoneLabel, phone,
                emailLabel, email, salaryLabel, salary,
                access_level, back, regis);

        primaryStage.setScene(scene1);
    }

    public static void modify(Stage primaryStage, Scene scene) {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        Scene scene1 = new Scene(vbox, 800, 700);

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
            // Create a PrintWriter with append mode
            PrintWriter printWriter = new PrintWriter(new FileWriter(filePath, true));

            // Append the line to the file
            printWriter.println(line);

            // Close the PrintWriter
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
        Label bookNameLabel = new Label("Book Name:");
        TextField bookName = new TextField();
        Label categoryLabel = new Label("Category:");
        TextField category = new TextField();
        Label supplierLabel = new Label("Supplier:");
        TextField supplier = new TextField();
        Label priceBoughtLabel = new Label("Price Bought:");
        TextField priceBought = new TextField();
        Label dateBoughtLabel = new Label("Date Bought (dd.mm.yyyy):");
        TextField dateBought = new TextField();
        Label priceSoldLabel = new Label("Price Sold:");
        TextField priceSold = new TextField();
        Label priceLabel = new Label("Price:");
        TextField price = new TextField();
        Label authorLabel = new Label("Author:");
        TextField author = new TextField();
        Label quantityLabel = new Label("Quantity:");
        TextField quantity = new TextField();
        Label imageLabel = new Label("Image Path");
        TextField imagePathh = new TextField();

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
                        priceSold.setText(String.valueOf(book.getOriginalPrice()));
                        price.setText(String.valueOf(book.getSellingPrice()));
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
                        price.setEditable(false);
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
            if (areFieldsEmpty(isbn, bookName, category, supplier, priceBought, dateBought, priceSold, price, author,
                    quantity, imagePathh)) {
                showAlert("Warning", "All fields must be filled in.");
            } else {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation");
                alert.setHeaderText("Confirm Book Addition");
                alert.setContentText(
                        "Do you want to add the book: " + bookName.getText() + " with ISBN: "
                                + isbn.getText());

                ButtonType okButton = new ButtonType("OK");
                ButtonType cancelButton = new ButtonType("Cancel");
                alert.getButtonTypes().setAll(okButton, cancelButton);

                alert.showAndWait().ifPresent(result -> {
                    if (result == okButton) {

                        try {
                            Methods.addBookUpdate(isbn.getText(), bookName.getText(), category.getText(),
                                    supplier.getText(), Double.parseDouble(priceBought.getText()),
                                    dateFormat.parse(dateBought.getText()), Double.parseDouble(priceSold.getText()),
                                    Double.parseDouble(price.getText()),
                                    author.getText(),
                                    Integer.parseInt(quantity.getText()), imagePathh.getText());
                        } catch (NumberFormatException e1) {

                            e1.printStackTrace();
                        } catch (ParseException e1) {

                            e1.printStackTrace();
                        }
                    }
                });
            }
        });

        vbox.getChildren().addAll(books, show, isbnLabel, isbn, bookNameLabel, bookName, categoryLabel, category,
                supplierLabel,
                supplier,
                priceBoughtLabel, priceBought, dateBoughtLabel, dateBought, priceSoldLabel, priceSold, priceLabel,
                price,
                authorLabel, author, quantityLabel, quantity, imageLabel, imagePathh, back, addBook);

        primaryStage.setScene(scene2);
    }

    public static void addBookUpdate(String isbn, String title, String category, String supplier, double purchasedPrice,
            Date purchasedDate, double originalPrice, double sellingPrice, String author, int stock, String image) {
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
                book.setOriginalPrice(originalPrice);
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
            newBook.setOriginalPrice(originalPrice);
            newBook.setSellingPrice(sellingPrice);
            newBook.setAuthor(author);
            newBook.setStock(stock);
            System.out.println("The book added");

            tempBook.add(newBook);
        }

        Methods.saveBooksToFile(tempBook);
    }

    public static Scene createOrderConfirmationScene(Stage primaryStage, FirstWindow firstWindow) {
        // Create a GridPane for the order confirmation scene
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

        Label totalPricewV = new Label("Price without VAT: $");
        orderConfirmationGrid.add(totalPricewV, 0, 5);
        Label vatPrice = new Label("VAT: ");
        orderConfirmationGrid.add(vatPrice, 0, 6);
        Label totalPriceLabel = new Label("Total: $" + String.format("%.2f", firstWindow.getTotalPrice()));
        orderConfirmationGrid.add(totalPriceLabel, 0, 7);

        // Create a confirmation order button
        Button confirmOrder = new Button("Confirm Order");

        // Create a back button to return to the main scene
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            Main.showMainScene(primaryStage);
            primaryStage.close();
        });

        orderConfirmationGrid.add(backButton, 0, 10);
        orderConfirmationGrid.add(confirmOrder, 1, 10);

        return new Scene(orderConfirmationGrid, 450, 450);
    }
}
