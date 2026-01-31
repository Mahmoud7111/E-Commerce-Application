package ecommerce.UI;

import ecommerce.domain.users.*;
import ecommerce.util.IDGenerator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class RegisterMenu {

    public static void show(Stage stage) {
        // Create main container
        BorderPane root = new BorderPane();
        UIStyles.styleBackground(root);
        
        // Create center card
        VBox card = UIStyles.createCard(null);
        card.setMaxWidth(450);
        card.setAlignment(Pos.CENTER);
        
        // Title
        Label titleLabel = UIStyles.createTitleLabel("Create Account");
        titleLabel.setAlignment(Pos.CENTER);
        
        Label subtitleLabel = UIStyles.createSubtitleLabel("Register to start shopping");
        subtitleLabel.setAlignment(Pos.CENTER);
        
        // Form
        VBox formBox = new VBox(12);
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(20, 0, 0, 0));
        
        // Username
        Label usernameLabel = UIStyles.createFieldLabel("Username");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Choose a username");
        UIStyles.styleTextField(usernameField);
        usernameField.setPrefWidth(400);
        
        // Email
        Label emailLabel = UIStyles.createFieldLabel("Email");
        TextField emailField = new TextField();
        emailField.setPromptText("your.email@example.com");
        UIStyles.styleTextField(emailField);
        emailField.setPrefWidth(400);
        
        // Password
        Label passwordLabel = UIStyles.createFieldLabel("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Minimum 4 characters");
        UIStyles.stylePasswordField(passwordField);
        passwordField.setPrefWidth(400);
        
        // Confirm Password
        Label confirmPasswordLabel = UIStyles.createFieldLabel("Confirm Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Re-enter your password");
        UIStyles.stylePasswordField(confirmPasswordField);
        confirmPasswordField.setPrefWidth(400);
        
        // Role selection
        Label roleLabel = UIStyles.createFieldLabel("Register as");
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Customer", "Seller");
        roleCombo.setValue("Customer");
        roleCombo.setPrefWidth(400);
        roleCombo.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-font-size: 14px;"
        );
        
        // Additional fields for seller
        Label storeNameLabel = UIStyles.createFieldLabel("Store Name");
        TextField storeNameField = new TextField();
        storeNameField.setPromptText("Your store name");
        UIStyles.styleTextField(storeNameField);
        storeNameField.setPrefWidth(400);
        storeNameLabel.setVisible(false);
        storeNameField.setVisible(false);
        storeNameLabel.setManaged(false);
        storeNameField.setManaged(false);
        
        // Additional fields for customer
        Label addressLabel = UIStyles.createFieldLabel("Address");
        TextField addressField = new TextField();
        addressField.setPromptText("Your address");
        UIStyles.styleTextField(addressField);
        addressField.setPrefWidth(400);
        
        Label phoneLabel = UIStyles.createFieldLabel("Phone Number");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Your phone number");
        UIStyles.styleTextField(phoneField);
        phoneField.setPrefWidth(400);
        
        // Show/hide fields based on role
        roleCombo.setOnAction(e -> {
            boolean isSeller = roleCombo.getValue().equals("Seller");
            storeNameLabel.setVisible(isSeller);
            storeNameField.setVisible(isSeller);
            storeNameLabel.setManaged(isSeller);
            storeNameField.setManaged(isSeller);
            
            addressLabel.setVisible(!isSeller);
            addressField.setVisible(!isSeller);
            addressLabel.setManaged(!isSeller);
            addressField.setManaged(!isSeller);
            
            phoneLabel.setVisible(!isSeller);
            phoneField.setVisible(!isSeller);
            phoneLabel.setManaged(!isSeller);
            phoneField.setManaged(!isSeller);
        });
        
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: " + UIStyles.ERROR_COLOR + "; -fx-font-size: 12px;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(400);
        
        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button registerBtn = new Button("Create Account");
        registerBtn.setPrefWidth(190);
        UIStyles.stylePrimaryButton(registerBtn);
        
        Button backBtn = new Button("Back to Login");
        backBtn.setPrefWidth(190);
        UIStyles.styleSecondaryButton(backBtn);
        
        registerBtn.setOnAction(e -> {
            try {
                messageLabel.setText("");
                
                // Validation
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String password = passwordField.getText();
                String confirmPassword = confirmPasswordField.getText();
                
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("Please fill in all required fields");
                    return;
                }
                
                if (!password.equals(confirmPassword)) {
                    messageLabel.setText("Passwords do not match");
                    return;
                }
                
                if (password.length() < 4) {
                    messageLabel.setText("Password must be at least 4 characters");
                    return;
                }
                
                if (!email.contains("@")) {
                    messageLabel.setText("Please enter a valid email address");
                    return;
                }
                
                String id = IDGenerator.generateUserId();
                User newUser;
                
                if (roleCombo.getValue().equals("Seller")) {
                    String storeName = storeNameField.getText().trim();
                    if (storeName.isEmpty()) {
                        messageLabel.setText("Please enter a store name");
                        return;
                    }
                    newUser = new Seller(id, username, password, email, storeName, LocalDateTime.now());
                } else {
                    // Customer
                    String address = addressField.getText().trim();
                    String phone = phoneField.getText().trim();
                    if (address.isEmpty() || phone.isEmpty()) {
                        messageLabel.setText("Please fill in address and phone number");
                        return;
                    }
                    newUser = new Customer(id, username, password, email, address, phone, LocalDateTime.now());
                }
                
                newUser.addUser();
                
                UIStyles.showSuccess("Success", "Account created successfully!\nYour ID: " + id + "\nPlease login.");
                LoginMenu.show(stage);
                
            } catch (Exception ex) {
                messageLabel.setText("Registration failed: " + ex.getMessage());
            }
        });
        
        backBtn.setOnAction(e -> LoginMenu.show(stage));
        
        buttonBox.getChildren().addAll(registerBtn, backBtn);
        
        formBox.getChildren().addAll(
            usernameLabel, usernameField,
            emailLabel, emailField,
            passwordLabel, passwordField,
            confirmPasswordLabel, confirmPasswordField,
            roleLabel, roleCombo,
            storeNameLabel, storeNameField,
            addressLabel, addressField,
            phoneLabel, phoneField,
            messageLabel,
            buttonBox
        );
        
        card.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            new Separator(),
            formBox
        );
        
        ScrollPane scrollPane = new ScrollPane(card);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + UIStyles.BACKGROUND_COLOR + "; -fx-background-color: transparent;");
        
        VBox centerContainer = new VBox(scrollPane);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setPadding(new Insets(30));
        
        root.setCenter(centerContainer);

        Scene scene = new Scene(root, 650, 750);
        stage.setScene(scene);
        stage.setTitle("E-Commerce Application - Register");
        stage.show();
    }
}