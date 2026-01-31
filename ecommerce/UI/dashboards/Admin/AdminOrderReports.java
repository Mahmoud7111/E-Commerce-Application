package ecommerce.UI.dashboards.Admin;

import ecommerce.UI.UIStyles;
import ecommerce.data.DataStore;
import ecommerce.domain.Shopping.Order;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AdminOrderReports {

    public static void show(Stage stage) {
        BorderPane root = new BorderPane();
        UIStyles.styleBackground(root);
        root.setPadding(new Insets(20));

        // Header
        HBox header = createHeader(stage);
        root.setTop(header);

        // Main content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(20, 20, 20, 20));

        // Order Lookup Card
        VBox lookupCard = UIStyles.createCard("Order Lookup");
        
        Label orderIdLabel = UIStyles.createFieldLabel("Order ID");
        TextField orderIdField = new TextField();
        orderIdField.setPromptText("Enter order ID to search");
        UIStyles.styleTextField(orderIdField);
        orderIdField.setMaxWidth(400);

        Button viewOrderBtn = new Button("View Order Details");
        UIStyles.stylePrimaryButton(viewOrderBtn);

        TextArea orderDetails = new TextArea();
        orderDetails.setEditable(false);
        orderDetails.setPrefRowCount(6);
        orderDetails.setWrapText(true);
        orderDetails.setStyle(
            "-fx-background-color: #F5F5F5;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-font-family: 'Courier New';" +
            "-fx-font-size: 13px;"
        );

        viewOrderBtn.setOnAction(e -> {
            try {
                String orderId = orderIdField.getText().trim();
                if (orderId.isEmpty()) {
                    UIStyles.showWarning("Missing Input", "Please enter an order ID");
                    return;
                }

                Order order = Order.getOrderById(orderId);
                if (order == null) {
                    orderDetails.setText("❌ Order not found: " + orderId);
                    orderDetails.setStyle(
                        "-fx-background-color: #FFEBEE;" +
                        "-fx-border-color: " + UIStyles.ERROR_COLOR + ";" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-font-family: 'Courier New';" +
                        "-fx-font-size: 13px;"
                    );
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("✅ ORDER DETAILS\n");
                    sb.append("═══════════════════════════════════\n\n");
                    sb.append("Order ID     : ").append(order.getOrderId()).append("\n");
                    sb.append("Customer ID  : ").append(order.getCustomerId()).append("\n");
                    sb.append("Order Date   : ").append(order.getOrderDate()).append("\n");
                    sb.append("Status       : ").append(order.isPaid() ? "✓ PAID" : "⏳ PENDING").append("\n");
                    sb.append("Total Amount : $").append(String.format("%.2f", order.getTotalAmount())).append("\n\n");
                    sb.append("Items:\n");
                    sb.append("───────────────────────────────────\n");
                    order.getItems().forEach(item -> {
                        sb.append("  • ").append(item.getProductId())
                          .append(" (Qty: ").append(item.getQuantity())
                          .append(", Price: $").append(String.format("%.2f", item.getPrice()))
                          .append(", Total: $").append(String.format("%.2f", item.getTotal()))
                          .append(")\n");
                    });

                    orderDetails.setText(sb.toString());
                    orderDetails.setStyle(
                        "-fx-background-color: #E8F5E9;" +
                        "-fx-border-color: " + UIStyles.SUCCESS_COLOR + ";" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-font-family: 'Courier New';" +
                        "-fx-font-size: 13px;"
                    );
                }
            } catch (Exception ex) {
                orderDetails.setText("Error: " + ex.getMessage());
                orderDetails.setStyle(
                    "-fx-background-color: #FFEBEE;" +
                    "-fx-border-color: " + UIStyles.ERROR_COLOR + ";" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;" +
                    "-fx-font-family: 'Courier New';" +
                    "-fx-font-size: 13px;"
                );
            }
        });

        lookupCard.getChildren().addAll(orderIdLabel, orderIdField, viewOrderBtn, orderDetails);

        // Date Range Card
        VBox dateCard = UIStyles.createCard("Revenue Analysis by Date Range");
        GridPane dateGrid = new GridPane();
        dateGrid.setHgap(15);
        dateGrid.setVgap(10);

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
        
        HBox revenueButtonBox = new HBox(15);
        revenueButtonBox.setAlignment(Pos.CENTER_LEFT);

        Button totalRevenueBtn = new Button("Calculate Total Revenue");
        UIStyles.stylePrimaryButton(totalRevenueBtn);
        totalRevenueBtn.setPrefWidth(220);

        Button avgRevenueBtn = new Button("Calculate Average Revenue");
        UIStyles.styleSuccessButton(avgRevenueBtn);
        avgRevenueBtn.setPrefWidth(220);

        revenueButtonBox.getChildren().addAll(totalRevenueBtn, avgRevenueBtn);

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(30);
        statsGrid.setVgap(15);
        statsGrid.setPadding(new Insets(15, 0, 0, 0));

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

        revenueCard.getChildren().addAll(revenueButtonBox, statsGrid);

        // All Orders Card
        VBox allOrdersCard = UIStyles.createCard("All Orders Overview");
        
        Button viewAllOrdersBtn = new Button("View All Orders");
        UIStyles.styleSecondaryButton(viewAllOrdersBtn);
        viewAllOrdersBtn.setPrefWidth(200);

        TextArea allOrdersArea = new TextArea();
        allOrdersArea.setEditable(false);
        allOrdersArea.setPrefRowCount(8);
        allOrdersArea.setWrapText(true);
        allOrdersArea.setStyle(
            "-fx-background-color: #F5F5F5;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-font-family: 'Courier New';" +
            "-fx-font-size: 12px;"
        );

        viewAllOrdersBtn.setOnAction(e -> {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("📋 ALL ORDERS\n");
                sb.append("═══════════════════════════════════════════════════════\n\n");
                
                int count = 0;
                for (Order order : DataStore.getInstance().getOrders().values()) {
                    count++;
                    sb.append(count).append(". Order ID: ").append(order.getOrderId()).append("\n");
                    sb.append("   Customer: ").append(order.getCustomerId()).append("\n");
                    sb.append("   Date: ").append(order.getOrderDate()).append("\n");
                    sb.append("   Status: ").append(order.isPaid() ? "✓ PAID" : "⏳ PENDING").append("\n");
                    sb.append("   Total: $").append(String.format("%.2f", order.getTotalAmount())).append("\n");
                    sb.append("   Items: ").append(order.getItems().size()).append("\n");
                    sb.append("───────────────────────────────────────────────────────\n");
                }
                
                if (count == 0) {
                    sb.append("No orders found in the system.\n");
                } else {
                    sb.append("\nTotal Orders: ").append(count);
                }
                
                allOrdersArea.setText(sb.toString());
            } catch (Exception ex) {
                allOrdersArea.setText("Error loading orders: " + ex.getMessage());
            }
        });

        allOrdersCard.getChildren().addAll(viewAllOrdersBtn, allOrdersArea);

        // Back button
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button backBtn = new Button("Back to Dashboard");
        UIStyles.styleSecondaryButton(backBtn);
        backBtn.setOnAction(e -> AdminDashboard.show(stage, null));
        
        bottomBox.getChildren().add(backBtn);

        mainContent.getChildren().addAll(
            lookupCard,
            dateCard,
            revenueCard,
            allOrdersCard,
            bottomBox
        );

        scrollPane.setContent(mainContent);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 900, 800);
        stage.setScene(scene);
        stage.setTitle("Admin - Order Reports");
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

        Label titleLabel = new Label("🛒 Order Reports");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        header.getChildren().add(titleLabel);
        return header;
    }

    private static VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPrefSize(280, 120);
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