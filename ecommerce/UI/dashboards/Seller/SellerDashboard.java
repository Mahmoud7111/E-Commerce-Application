package ecommerce.UI.dashboards.Seller;

import ecommerce.UI.LoginMenu;
import ecommerce.UI.UIStyles;
import ecommerce.domain.users.Seller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class SellerDashboard {

    public static void show(Stage stage, Seller seller) {
        BorderPane root = new BorderPane();
        UIStyles.styleBackground(root);
        root.setPadding(new Insets(20));

        // Header
        HBox header = createHeader(stage, seller);
        root.setTop(header);

        // Main content - Dashboard cards
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20, 0, 0, 0));
        mainContent.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Seller Control Panel");
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        welcomeLabel.setStyle("-fx-text-fill: " + UIStyles.TEXT_PRIMARY + ";");

        Label storeLabel = new Label("Store: " + seller.getStoreName());
        storeLabel.setFont(Font.font("System", FontWeight.NORMAL, 16));
        storeLabel.setStyle("-fx-text-fill: " + UIStyles.TEXT_SECONDARY + ";");

        // Create grid of management cards
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        // Product Management Card
        VBox productCard = createDashboardCard(
            "📦 Manage Products",
            "Add, edit, and remove your products",
            UIStyles.PRIMARY_COLOR
        );
        productCard.setOnMouseClicked(e -> SellerProductManagement.show(stage, seller));

        // Product Reports Card
        VBox productReportCard = createDashboardCard(
            "📊 Product Reports",
            "View sales analytics for your products",
            UIStyles.ACCENT_COLOR
        );
        productReportCard.setOnMouseClicked(e -> SellerProductReports.show(stage, seller));

        // Order Reports Card
        VBox orderCard = createDashboardCard(
            "💰 Order Reports",
            "Track revenue and order statistics",
            UIStyles.SUCCESS_COLOR
        );
        orderCard.setOnMouseClicked(e -> SellerOrderReports.show(stage, seller));

        grid.add(productCard, 0, 0);
        grid.add(productReportCard, 1, 0);
        grid.add(orderCard, 0, 1);

        mainContent.getChildren().addAll(welcomeLabel, storeLabel, grid);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, 900, 650);
        stage.setScene(scene);
        stage.setTitle("Seller Dashboard - " + seller.getUsername());
        stage.show();
    }

    private static HBox createHeader(Stage stage, Seller seller) {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));
        header.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        Label titleLabel = new Label("🏪 Seller Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        Label welcomeLabel = new Label("Welcome, " + seller.getUsername() + "!");
        welcomeLabel.setFont(Font.font("System", FontWeight.NORMAL, 16));
        welcomeLabel.setStyle("-fx-text-fill: #757575;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("Logout");
        UIStyles.styleSecondaryButton(logoutBtn);
        logoutBtn.setOnAction(e -> LoginMenu.show(stage));

        header.getChildren().addAll(titleLabel, welcomeLabel, spacer, logoutBtn);
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
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: " + color + ";" +
                "-fx-border-width: 0 0 0 5;" +
                "-fx-border-radius: 15;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 20, 0, 0, 5);" +
                "-fx-cursor: hand;"
            );
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