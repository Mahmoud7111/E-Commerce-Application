package ecommerce.UI.dashboards.Seller;

import ecommerce.UI.UIStyles;
import ecommerce.domain.Shopping.Order;
import ecommerce.domain.users.Seller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;

public class SellerOrderReports {

    public static void show(Stage stage, Seller seller) {
        BorderPane root = new BorderPane();
        UIStyles.styleBackground(root);
        root.setPadding(new Insets(20));

        // Header
        HBox header = createHeader(stage);
        root.setTop(header);

        // Main content
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20, 20, 20, 20));
        mainContent.setAlignment(Pos.TOP_CENTER);

        // Date Range Card
        VBox dateCard = UIStyles.createCard("Select Date Range");
        dateCard.setMaxWidth(600);
        
        GridPane dateGrid = new GridPane();
        dateGrid.setHgap(15);
        dateGrid.setVgap(10);
        dateGrid.setAlignment(Pos.CENTER);

        Label fromLabel = UIStyles.createFieldLabel("From Date");
        DatePicker fromDate = new DatePicker(LocalDate.now().minusMonths(1));
        fromDate.setPrefWidth(200);

        Label toLabel = UIStyles.createFieldLabel("To Date");
        DatePicker toDate = new DatePicker(LocalDate.now());
        toDate.setPrefWidth(200);

        dateGrid.add(fromLabel, 0, 0);
        dateGrid.add(fromDate, 0, 1);
        dateGrid.add(toLabel, 1, 0);
        dateGrid.add(toDate, 1, 1);

        dateCard.getChildren().add(dateGrid);

        // Revenue Statistics Card
        VBox revenueCard = UIStyles.createCard("Revenue Statistics");
        revenueCard.setMaxWidth(600);
        
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button totalRevenueBtn = new Button("Calculate Total Revenue");
        UIStyles.stylePrimaryButton(totalRevenueBtn);
        totalRevenueBtn.setPrefWidth(250);

        Button avgRevenueBtn = new Button("Calculate Average Revenue");
        UIStyles.styleSuccessButton(avgRevenueBtn);
        avgRevenueBtn.setPrefWidth(250);

        buttonBox.getChildren().addAll(totalRevenueBtn, avgRevenueBtn);

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(30);
        statsGrid.setVgap(15);
        statsGrid.setPadding(new Insets(20, 0, 0, 0));
        statsGrid.setAlignment(Pos.CENTER);

        // Total Revenue Display
        VBox totalCard = createStatCard("Total Revenue", "$0.00", UIStyles.PRIMARY_COLOR);
        
        // Average Revenue Display
        VBox avgCard = createStatCard("Average Revenue", "$0.00", UIStyles.SUCCESS_COLOR);

        statsGrid.add(totalCard, 0, 0);
        statsGrid.add(avgCard, 1, 0);

        Label totalLabel = (Label) ((VBox) statsGrid.getChildren().get(0)).getChildren().get(1);
        Label avgLabel = (Label) ((VBox) statsGrid.getChildren().get(1)).getChildren().get(1);

        totalRevenueBtn.setOnAction(e -> {
            try {
                double total = Order.totalRevenue(
                    fromDate.getValue().atStartOfDay(),
                    toDate.getValue().atStartOfDay()
                );
                totalLabel.setText(String.format("$%.2f", total));
            } catch (Exception ex) {
                UIStyles.showError("Error", "Failed to calculate total revenue: " + ex.getMessage());
            }
        });

        avgRevenueBtn.setOnAction(e -> {
            try {
                double avg = Order.averageRevenue(
                    fromDate.getValue().atStartOfDay(),
                    toDate.getValue().atStartOfDay()
                );
                avgLabel.setText(String.format("$%.2f", avg));
            } catch (Exception ex) {
                UIStyles.showError("Error", "Failed to calculate average revenue: " + ex.getMessage());
            }
        });

        revenueCard.getChildren().addAll(buttonBox, statsGrid);

        // Info Card
        VBox infoCard = UIStyles.createCard("ℹ️ Information");
        infoCard.setMaxWidth(600);
        infoCard.setStyle(
            "-fx-background-color: #E3F2FD;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + UIStyles.PRIMARY_COLOR + ";" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;"
        );

        Label infoLabel = new Label(
            """
            Note: Revenue statistics show data for ALL sellers in the system.
            This helps you understand market trends and compare your performance.""");
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-text-fill: " + UIStyles.TEXT_PRIMARY + "; -fx-font-size: 13px;");
        
        infoCard.getChildren().add(infoLabel);

        // Back button
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button backBtn = new Button("Back to Dashboard");
        UIStyles.styleSecondaryButton(backBtn);
        backBtn.setPrefWidth(200);
        backBtn.setOnAction(e -> SellerDashboard.show(stage, seller));
        
        bottomBox.getChildren().add(backBtn);

        mainContent.getChildren().addAll(
            dateCard,
            revenueCard,
            infoCard,
            bottomBox
        );

        root.setCenter(mainContent);

        Scene scene = new Scene(root, 800, 700);
        stage.setScene(scene);
        stage.setTitle("Order Reports");
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

        Label titleLabel = new Label("💰 Order Reports");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        header.getChildren().add(titleLabel);
        return header;
    }

    private static VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPrefSize(250, 120);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);"
        );

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: white;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        valueLabel.setStyle("-fx-text-fill: white;");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
}