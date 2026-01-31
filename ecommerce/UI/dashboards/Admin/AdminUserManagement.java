package ecommerce.UI.dashboards.Admin;

import ecommerce.UI.UIStyles;
import ecommerce.data.DataStore;
import ecommerce.domain.users.*;
import ecommerce.util.IDGenerator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDateTime;


public class AdminUserManagement {

    private static final ObservableList<User> userList = FXCollections.observableArrayList();

    public static void show(Stage stage) {
        BorderPane root = new BorderPane();
        UIStyles.styleBackground(root);
        root.setPadding(new Insets(20));

        // Header
        HBox header = createHeader(stage);
        root.setTop(header);

        // Main content
        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20, 0, 0, 0));

        VBox contentCard = UIStyles.createCard("User Management");
        VBox.setVgrow(contentCard, Priority.ALWAYS);

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search users by username, email, role...");
        UIStyles.styleTextField(searchField);

        // Table
        TableView<User> tableView = new TableView<>(userList);
        UIStyles.styleTableView(tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        TableColumn<User, String> idCol = new TableColumn<>("User ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(200);

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(150);

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(100);
        roleCol.setCellFactory(col -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String role, boolean empty) {
                super.updateItem(role, empty);
                if (empty || role == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(role);
                    switch (role) {
                        case "ADMIN" -> setStyle("-fx-text-fill: " + UIStyles.ERROR_COLOR + "; -fx-font-weight: bold;");
                        case "SELLER" -> setStyle("-fx-text-fill: " + UIStyles.WARNING_COLOR + "; -fx-font-weight: bold;");
                        case "CUSTOMER" -> setStyle("-fx-text-fill: " + UIStyles.SUCCESS_COLOR + "; -fx-font-weight: bold;");
                        default -> setStyle("");
                    }
                }
            }
        });

        TableColumn<User, String> dateCol = new TableColumn<>("Registered");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("registeredDate"));
        dateCol.setPrefWidth(180);

        tableView.getColumns().addAll(idCol, usernameCol, emailCol, roleCol, dateCol);

        // Load users
        refreshUsers();

        // Search functionality
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            userList.setAll(DataStore.getInstance().searchUsers(newVal));
        });

        // Action buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        Button addBtn = new Button("Add User");
        UIStyles.stylePrimaryButton(addBtn);
        addBtn.setOnAction(e -> showUserForm(stage, null));

        Button editBtn = new Button("Edit");
        UIStyles.styleSecondaryButton(editBtn);
        editBtn.setOnAction(e -> {
            User selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showUserForm(stage, selected);
            } else {
                UIStyles.showWarning("No Selection", "Please select a user to edit");
            }
        });

        Button deleteBtn = new Button("Delete");
        UIStyles.styleDangerButton(deleteBtn);
        deleteBtn.setOnAction(e -> {
            User selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete User?");
                confirm.setContentText("Are you sure you want to delete: " + selected.getUsername() + "?");
                
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        selected.removeUser();
                        refreshUsers();
                        UIStyles.showSuccess("Deleted", "User deleted successfully");
                    }
                });
            } else {
                UIStyles.showWarning("No Selection", "Please select a user to delete");
            }
        });

        Button backBtn = new Button("Back");
        UIStyles.styleSecondaryButton(backBtn);
        backBtn.setOnAction(e -> AdminDashboard.show(stage, null));

        buttonBox.getChildren().addAll(addBtn, editBtn, deleteBtn, backBtn);

        contentCard.getChildren().addAll(searchField, tableView, buttonBox);
        mainContent.getChildren().add(contentCard);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, 1050, 700);
        stage.setScene(scene);
        stage.setTitle("Admin - User Management");
        stage.show();
    }

    private static HBox createHeader(Stage stage) {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));
        header.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        Label titleLabel = new Label("👥 User Management");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        header.getChildren().add(titleLabel);
        return header;
    }

    private static void refreshUsers() {
        userList.clear();
        userList.addAll(DataStore.getInstance().getUsers().values());
    }

    private static void showUserForm(Stage stage, User user) {
        Stage formStage = new Stage();
        formStage.setTitle(user == null ? "Add New User" : "Edit User");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        UIStyles.styleBackground(root);

        VBox formCard = UIStyles.createCard(user == null ? "Add New User" : "Edit User");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        // Username
        Label usernameLabel = UIStyles.createFieldLabel("Username *");
        TextField usernameField = new TextField(user != null ? user.getUsername() : "");
        usernameField.setPromptText("Enter username");
        UIStyles.styleTextField(usernameField);

        // Email
        Label emailLabel = UIStyles.createFieldLabel("Email *");
        TextField emailField = new TextField(user != null ? user.getEmail() : "");
        emailField.setPromptText("user@example.com");
        UIStyles.styleTextField(emailField);

        // Password (only for new users)
        Label passwordLabel = UIStyles.createFieldLabel("Password *");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Minimum 4 characters");
        UIStyles.stylePasswordField(passwordField);
        
        if (user != null) {
            passwordLabel.setVisible(false);
            passwordField.setVisible(false);
            passwordLabel.setManaged(false);
            passwordField.setManaged(false);
        }

        // Role (only for new users)
        Label roleLabel = UIStyles.createFieldLabel("Role *");
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("ADMIN", "SELLER", "CUSTOMER");
        roleCombo.setValue("CUSTOMER");
        roleCombo.setPrefWidth(300);
        
        if (user != null) {
            roleLabel.setVisible(false);
            roleCombo.setVisible(false);
            roleLabel.setManaged(false);
            roleCombo.setManaged(false);
        }

        form.add(usernameLabel, 0, 0);
        form.add(usernameField, 0, 1);
        form.add(emailLabel, 0, 2);
        form.add(emailField, 0, 3);
        form.add(passwordLabel, 0, 4);
        form.add(passwordField, 0, 5);
        form.add(roleLabel, 0, 6);
        form.add(roleCombo, 0, 7);

        ColumnConstraints col = new ColumnConstraints();
        col.setMinWidth(350);
        form.getColumnConstraints().add(col);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: " + UIStyles.ERROR_COLOR + ";");
        errorLabel.setWrapText(true);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        Button saveBtn = new Button("Save");
        UIStyles.stylePrimaryButton(saveBtn);
        saveBtn.setPrefWidth(150);

        Button cancelBtn = new Button("Cancel");
        UIStyles.styleSecondaryButton(cancelBtn);
        cancelBtn.setPrefWidth(150);

        saveBtn.setOnAction(e -> {
            try {
                errorLabel.setText("");

                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();

                if (username.isEmpty() || email.isEmpty()) {
                    errorLabel.setText("Please fill in all required fields");
                    return;
                }

                if (!email.contains("@")) {
                    errorLabel.setText("Invalid email address");
                    return;
                }

                if (user == null) {
                    // Add new user
                    String password = passwordField.getText();
                    if (password.length() < 4) {
                        errorLabel.setText("Password must be at least 4 characters");
                        return;
                    }

                    String id = IDGenerator.generateUserId();
                    User newUser = switch (roleCombo.getValue()) {
                        case "ADMIN" -> new Admin(id, username, password, email, LocalDateTime.now());
                        case "SELLER" -> new Seller(id, username, password, email, "Default Store", LocalDateTime.now());
                        default -> new Customer(id, username, password, email, "Default Address", "000-0000", LocalDateTime.now());
                    };
                    
                    newUser.addUser();
                    UIStyles.showSuccess("Success", "User created successfully");
                } else {
                    // Edit existing
                    user.setUsername(username);
                    user.setEmail(email);
                    UIStyles.showSuccess("Success", "User updated successfully");
                }

                refreshUsers();
                formStage.close();
                show(stage);

            } catch (Exception ex) {
                errorLabel.setText("Error: " + ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> formStage.close());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);

        formCard.getChildren().addAll(form, errorLabel, buttonBox);
        root.getChildren().add(formCard);

        Scene scene = new Scene(root, 500, user == null ? 550 : 400);
        formStage.setScene(scene);
        formStage.show();
    }
}