package ecommerce.UI;

import ecommerce.UI.dashboards.Admin.AdminDashboard;
import ecommerce.UI.dashboards.Seller.SellerDashboard;
import ecommerce.UI.dashboards.CustomerDashboard;
import ecommerce.domain.users.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginMenu {

    public static void show(Stage stage) {
        // Create main container
        BorderPane root = new BorderPane();
        UIStyles.styleBackground(root);
        
        // Create center card
        VBox card = UIStyles.createCard(null);
        card.setMaxWidth(400);
        card.setAlignment(Pos.CENTER);
        
        // Title
        Label titleLabel = UIStyles.createTitleLabel("E-Commerce Login");
        titleLabel.setAlignment(Pos.CENTER);
        
        Label subtitleLabel = UIStyles.createSubtitleLabel("Welcome back! Please login to your account");
        subtitleLabel.setAlignment(Pos.CENTER);
        subtitleLabel.setWrapText(true);
        
        // Input fields
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        
        // Role Selection
        Label roleLabel = UIStyles.createFieldLabel("Login as");
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Admin", "Seller", "Customer");
        roleCombo.setValue("Customer");
        roleCombo.setPrefWidth(350);
        roleCombo.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10;"
        );
        
        // Add visual indicator for role
        roleCombo.setOnAction(e -> {
            String selected = roleCombo.getValue();
            switch (selected) {
                case "Admin" -> roleCombo.setStyle(
                    "-fx-background-color: #FFEBEE;" +
                    "-fx-border-color: " + UIStyles.ERROR_COLOR + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10;"
                );
                case "Seller" -> roleCombo.setStyle(
                    "-fx-background-color: #FFF3E0;" +
                    "-fx-border-color: " + UIStyles.WARNING_COLOR + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10;"
                );
                case "Customer" -> roleCombo.setStyle(
                    "-fx-background-color: #E8F5E9;" +
                    "-fx-border-color: " + UIStyles.SUCCESS_COLOR + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;" +
                    "-fx-font-size: 14px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 10;"
                );
            }
        });
        
        Label usernameLabel = UIStyles.createFieldLabel("Username");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        UIStyles.styleTextField(usernameField);
        usernameField.setPrefWidth(350);
        
        Label passwordLabel = UIStyles.createFieldLabel("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        UIStyles.stylePasswordField(passwordField);
        passwordField.setPrefWidth(350);

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: " + UIStyles.ERROR_COLOR + "; -fx-font-size: 12px;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);

        // Buttons
        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(350);
        UIStyles.stylePrimaryButton(loginBtn);
        
        Button registerBtn = new Button("Create New Account");
        registerBtn.setPrefWidth(350);
        UIStyles.styleSecondaryButton(registerBtn);

        loginBtn.setOnAction(e -> {
            try {
                messageLabel.setText("");
                
                String username = usernameField.getText().trim();
                String password = passwordField.getText();
                String selectedRole = roleCombo.getValue();
                
                if (username.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("Please fill in all fields");
                    return;
                }
                
                // Attempt login
                User user = User.login(username, password);
                
                // Verify the user has the selected role
                String userRole = user.getRole();
                boolean roleMatches = false;
                
                switch (selectedRole) {
                    case "Admin" -> roleMatches = userRole.equals("ADMIN");
                    case "Seller" -> roleMatches = userRole.equals("SELLER");
                    case "Customer" -> roleMatches = userRole.equals("CUSTOMER");
                }
                
                if (!roleMatches) {
                    messageLabel.setText("Invalid credentials for " + selectedRole + " role.\n" +
                                       "This account is registered as: " + getUserRoleDisplay(userRole));
                    return;
                }

                // Route to appropriate dashboard
                if (User.isAdmin(user)) {
                    AdminDashboard.show(stage, (Admin)user);
                }
                else if (User.isSeller(user)) {
                    SellerDashboard.show(stage, (Seller)user);
                }
                else if (User.isCustomer(user)) {
                    CustomerDashboard.show(stage, (Customer)user);
                }

            } catch (Exception ex) {
                messageLabel.setText("Login failed: " + ex.getMessage());
            }
        });

        registerBtn.setOnAction(e -> RegisterMenu.show(stage));
        
        // Allow Enter key to login
        passwordField.setOnAction(e -> loginBtn.fire());

        // Quick login hints
        VBox hintsBox = new VBox(5);
        hintsBox.setAlignment(Pos.CENTER);
        hintsBox.setPadding(new Insets(10, 0, 0, 0));
        hintsBox.setStyle(
            "-fx-background-color: #F5F5F5;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10;"
        );
        
        Label hintsTitle = new Label("💡 Quick Login Hints:");
        hintsTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
        
        Label adminHint = new Label("Admin: username: admin | password: admin");
        adminHint.setStyle("-fx-font-size: 10px; -fx-text-fill: " + UIStyles.ERROR_COLOR + ";");
        
        Label noteLabel = new Label("(Create Seller/Customer accounts via registration)");
        noteLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: #757575; -fx-font-style: italic;");
        
        hintsBox.getChildren().addAll(hintsTitle, adminHint, noteLabel);

        formBox.getChildren().addAll(
            roleLabel, roleCombo,
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            messageLabel,
            loginBtn,
            registerBtn,
            hintsBox
        );
        
        card.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            new Separator(),
            formBox
        );
        
        // Center the card
        VBox centerContainer = new VBox(card);
        centerContainer.setAlignment(Pos.CENTER);
        centerContainer.setPadding(new Insets(50));
        
        root.setCenter(centerContainer);

        Scene scene = new Scene(root, 600, 750);
        stage.setScene(scene);
        stage.setTitle("E-Commerce Application - Login");
        stage.show();
    }
    
    
    /**
     * Helper method to display role in user-friendly format
     */
    private static String getUserRoleDisplay(String role) {
        return switch (role) {
            case "ADMIN" -> "Admin";
            case "SELLER" -> "Seller";
            case "CUSTOMER" -> "Customer";
            default -> role;
        };
    }
}