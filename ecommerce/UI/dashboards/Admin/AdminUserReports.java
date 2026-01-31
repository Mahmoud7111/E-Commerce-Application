package ecommerce.UI.dashboards.Admin;

import ecommerce.UI.UIStyles;
import ecommerce.domain.Shopping.Order;
import ecommerce.domain.Shopping.Supplier;
import ecommerce.domain.users.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AdminUserReports {

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

        // Date Range Card
        VBox dateCard = UIStyles.createCard("Select Date Range for Revenue Analysis");
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

        // Supplier Reports Section
        VBox supplierSection = UIStyles.createCard("Supplier Analytics");
        supplierSection.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + UIStyles.WARNING_COLOR + ";" +
            "-fx-border-width: 0 0 0 4;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        HBox supplierButtonBox = new HBox(15);
        supplierButtonBox.setAlignment(Pos.CENTER_LEFT);

        Button supOrdersBtn = new Button("Supplier with Max Orders");
        UIStyles.stylePrimaryButton(supOrdersBtn);
        supOrdersBtn.setPrefWidth(220);

        Button supRevenueBtn = new Button("Supplier with Max Revenue");
        UIStyles.styleSuccessButton(supRevenueBtn);
        supRevenueBtn.setPrefWidth(220);

        supplierButtonBox.getChildren().addAll(supOrdersBtn, supRevenueBtn);

        TextArea supplierResult = new TextArea();
        supplierResult.setEditable(false);
        supplierResult.setPrefRowCount(5);
        supplierResult.setWrapText(true);
        supplierResult.setStyle(
            "-fx-background-color: #FFF3E0;" +
            "-fx-border-color: " + UIStyles.WARNING_COLOR + ";" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-font-size: 14px;"
        );

        supOrdersBtn.setOnAction(e -> {
            try {
                Supplier supplier = Supplier.getSupplierWithMaxOrders();
                if (supplier == null) {
                    supplierResult.setText("No supplier data available");
                } else {
                    supplierResult.setText(
                        "🏆 Supplier with Maximum Orders:\n\n" +
                        supplier.toString()
                    );
                }
            } catch (Exception ex) {
                supplierResult.setText("Error: " + ex.getMessage());
            }
        });

        supRevenueBtn.setOnAction(e -> {
            try {
                Supplier supplier = Supplier.getSupplierWithMaxRevenue();
                if (supplier == null) {
                    supplierResult.setText("No supplier data available");
                } else {
                    supplierResult.setText(
                        "💰 Supplier with Maximum Revenue:\n\n" +
                        supplier.toString()
                    );
                }
            } catch (Exception ex) {
                supplierResult.setText("Error: " + ex.getMessage());
            }
        });

        supplierSection.getChildren().addAll(supplierButtonBox, supplierResult);

        // Customer Reports Section
        VBox customerSection = UIStyles.createCard("Customer Analytics");
        customerSection.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + UIStyles.SUCCESS_COLOR + ";" +
            "-fx-border-width: 0 0 0 4;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        HBox customerButtonBox = new HBox(15);
        customerButtonBox.setAlignment(Pos.CENTER_LEFT);

        Button custOrdersBtn = new Button("Customer with Max Orders");
        UIStyles.stylePrimaryButton(custOrdersBtn);
        custOrdersBtn.setPrefWidth(220);

        Button custRevenueBtn = new Button("Customer with Max Revenue");
        UIStyles.styleSuccessButton(custRevenueBtn);
        custRevenueBtn.setPrefWidth(220);

        customerButtonBox.getChildren().addAll(custOrdersBtn, custRevenueBtn);

        TextArea customerResult = new TextArea();
        customerResult.setEditable(false);
        customerResult.setPrefRowCount(5);
        customerResult.setWrapText(true);
        customerResult.setStyle(
            "-fx-background-color: #E8F5E9;" +
            "-fx-border-color: " + UIStyles.SUCCESS_COLOR + ";" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-font-size: 14px;"
        );

        custOrdersBtn.setOnAction(e -> {
            try {
                Customer customer = Order.customerWithMaxOrders();
                if (customer == null) {
                    customerResult.setText("No customer data available");
                } else {
                    customerResult.setText(
                        "🏆 Customer with Maximum Orders:\n\n" +
                        "Username: " + customer.getUsername() + "\n" +
                        "Email: " + customer.getEmail() + "\n" +
                        "Address: " + customer.getAddress() + "\n" +
                        "Phone: " + customer.getPhoneNumber() + "\n" +
                        "Registered: " + customer.getRegisteredDate()
                    );
                }
            } catch (Exception ex) {
                customerResult.setText("Error: " + ex.getMessage());
            }
        });

        custRevenueBtn.setOnAction(e -> {
            try {
                Customer customer = Order.customerWithMaxRevenue();
                if (customer == null) {
                    customerResult.setText("No customer data available");
                } else {
                    customerResult.setText(
                        "💰 Customer with Maximum Revenue:\n\n" +
                        "Username: " + customer.getUsername() + "\n" +
                        "Email: " + customer.getEmail() + "\n" +
                        "Address: " + customer.getAddress() + "\n" +
                        "Phone: " + customer.getPhoneNumber() + "\n" +
                        "Registered: " + customer.getRegisteredDate()
                    );
                }
            } catch (Exception ex) {
                customerResult.setText("Error: " + ex.getMessage());
            }
        });

        customerSection.getChildren().addAll(customerButtonBox, customerResult);

        // Revenue Analysis Section
        VBox revenueSection = UIStyles.createCard("Revenue Analysis");
        revenueSection.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-border-color: " + UIStyles.PRIMARY_COLOR + ";" +
            "-fx-border-width: 0 0 0 4;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        Button revenueBtn = new Button("Calculate Revenue Statistics");
        UIStyles.stylePrimaryButton(revenueBtn);
        revenueBtn.setPrefWidth(250);

        GridPane revenueGrid = new GridPane();
        revenueGrid.setHgap(30);
        revenueGrid.setVgap(15);
        revenueGrid.setPadding(new Insets(15, 0, 0, 0));

        // Total Revenue Card
        VBox totalCard = createStatCard("Total Revenue", "$0.00", UIStyles.PRIMARY_COLOR);
        
        // Average Revenue Card
        VBox avgCard = createStatCard("Average Revenue", "$0.00", UIStyles.SUCCESS_COLOR);

        revenueGrid.add(totalCard, 0, 0);
        revenueGrid.add(avgCard, 1, 0);

        Label totalRevenueLabel = (Label) ((VBox) revenueGrid.getChildren().get(0)).getChildren().get(1);
        Label avgRevenueLabel = (Label) ((VBox) revenueGrid.getChildren().get(1)).getChildren().get(1);

        revenueBtn.setOnAction(e -> {
            try {
                double total = Order.totalRevenue(
                    fromDate.getValue().atStartOfDay(),
                    toDate.getValue().atStartOfDay()
                );
                double avg = Order.averageRevenue(
                    fromDate.getValue().atStartOfDay(),
                    toDate.getValue().atStartOfDay()
                );

                totalRevenueLabel.setText(String.format("$%.2f", total));
                avgRevenueLabel.setText(String.format("$%.2f", avg));
            } catch (Exception ex) {
                UIStyles.showError("Error", "Failed to calculate revenue: " + ex.getMessage());
            }
        });

        revenueSection.getChildren().addAll(revenueBtn, revenueGrid);

        // Back button
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button backBtn = new Button("Back to Dashboard");
        UIStyles.styleSecondaryButton(backBtn);
        backBtn.setOnAction(e -> AdminDashboard.show(stage, null));
        
        bottomBox.getChildren().add(backBtn);

        mainContent.getChildren().addAll(
            dateCard,
            supplierSection,
            customerSection,
            revenueSection,
            bottomBox
        );

        scrollPane.setContent(mainContent);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 900, 750);
        stage.setScene(scene);
        stage.setTitle("Admin - User Reports");
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

        Label titleLabel = new Label("📈 User Reports");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        header.getChildren().add(titleLabel);
        return header;
    }

    private static VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPrefSize(250, 100);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);"
        );

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: white;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        valueLabel.setStyle("-fx-text-fill: white;");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
}