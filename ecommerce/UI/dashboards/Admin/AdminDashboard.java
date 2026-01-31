package ecommerce.UI.dashboards.Admin;

import ecommerce.UI.LoginMenu;
import ecommerce.UI.UIStyles;
import ecommerce.domain.users.Admin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AdminDashboard {

    public static void show(Stage stage, Admin admin) {
        BorderPane root = new BorderPane();
        UIStyles.styleBackground(root);
        root.setPadding(new Insets(20));

        // Header
        HBox header = createHeader(stage);
        root.setTop(header);

        // Main content - Dashboard cards
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20, 0, 0, 0));
        mainContent.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Admin Control Panel");
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        welcomeLabel.setStyle("-fx-text-fill: " + UIStyles.TEXT_PRIMARY + ";");

        // Create grid of management cards
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Product Management Card
        VBox productCard = createDashboardCard(
            "📦 Product Management",
            "Manage all products in the system",
            UIStyles.PRIMARY_COLOR
        );
        productCard.setOnMouseClicked(e -> AdminProductManagement.show(stage));

        // Product Reports Card
        VBox productReportCard = createDashboardCard(
            "📊 Product Reports",
            "View product analytics and statistics",
            UIStyles.ACCENT_COLOR
        );
        productReportCard.setOnMouseClicked(e -> AdminProductReports.show(stage));

        // User Management Card
        VBox userCard = createDashboardCard(
            "👥 User Management",
            "Manage users and accounts",
            UIStyles.SUCCESS_COLOR
        );
        userCard.setOnMouseClicked(e -> AdminUserManagement.show(stage));

        // User Reports Card
        VBox userReportCard = createDashboardCard(
            "📈 User Reports",
            "View user statistics and analytics",
            UIStyles.WARNING_COLOR
        );
        userReportCard.setOnMouseClicked(e -> AdminUserReports.show(stage));

        // Order Reports Card
        VBox orderCard = createDashboardCard(
            "🛒 Order Reports",
            "View order statistics and revenue",
            "#9C27B0"
        );
        orderCard.setOnMouseClicked(e -> AdminOrderReports.show(stage));

        grid.add(productCard, 0, 0);
        grid.add(productReportCard, 1, 0);
        grid.add(userCard, 0, 1);
        grid.add(userReportCard, 1, 1);
        grid.add(orderCard, 0, 2);

        mainContent.getChildren().addAll(welcomeLabel, grid);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, 900, 750);
        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
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

        Label titleLabel = new Label("⚙️ Admin Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("Logout");
        UIStyles.styleSecondaryButton(logoutBtn);
        logoutBtn.setOnAction(e -> LoginMenu.show(stage));

        header.getChildren().addAll(titleLabel, spacer, logoutBtn);
        return header;
    }

    private static VBox createDashboardCard(String title, String description, String color) {
        VBox card = new VBox(10);
        card.setPrefSize(400, 150);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(25));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 0 0 0 5;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 3);" +
            "-fx-cursor: hand;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: " + color + ";");

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("System", FontWeight.NORMAL, 14));
        descLabel.setStyle("-fx-text-fill: #757575;");
        descLabel.setWrapText(true);

        card.getChildren().addAll(titleLabel, descLabel);

        // Hover effect
        card.setOnMouseEntered(e -> card.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 15;" +
            "-fx-border-color: " + color + ";" +
            "-fx-border-width: 0 0 0 5;" +
            "-fx-border-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 5);" +
            "-fx-cursor: hand;"
        ));

        card.setOnMouseEntered(e -> {
            titleLabel.setStyle("-fx-text-fill: white;");
            descLabel.setStyle("-fx-text-fill: white;");
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + color + ";" +
                "-fx-border-width: 0 0 0 5;" +
                "-fx-border-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 3);" +
                "-fx-cursor: hand;"
            );
            titleLabel.setStyle("-fx-text-fill: " + color + ";");
            descLabel.setStyle("-fx-text-fill: #757575;");
        });

        return card;
    }
}