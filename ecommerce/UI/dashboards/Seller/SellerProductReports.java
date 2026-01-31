package ecommerce.UI.dashboards.Seller;

import ecommerce.UI.UIStyles;
import ecommerce.domain.Shopping.Product;
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

public class SellerProductReports {

    public static void show(Stage stage, Seller seller) {
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
        VBox dateCard = UIStyles.createCard("Select Date Range");
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

        // Product Pieces Sold Card
        VBox piecesCard = UIStyles.createCard("Pieces Sold by Product");
        
        Label productIdLabel = UIStyles.createFieldLabel("Product ID");
        TextField productIdField = new TextField();
        productIdField.setPromptText("Enter your product ID");
        UIStyles.styleTextField(productIdField);
        productIdField.setMaxWidth(300);

        Button piecesSoldBtn = new Button("Get Pieces Sold");
        UIStyles.stylePrimaryButton(piecesSoldBtn);

        Label piecesResult = new Label();
        piecesResult.setFont(Font.font("System", FontWeight.BOLD, 16));
        piecesResult.setWrapText(true);

        piecesSoldBtn.setOnAction(e -> {
            try {
                if (productIdField.getText().trim().isEmpty()) {
                    UIStyles.showWarning("Missing Input", "Please enter a product ID");
                    return;
                }
                
                int sold = Product.getPiecesSold(
                    productIdField.getText(),
                    fromDate.getValue().atStartOfDay(),
                    toDate.getValue().atStartOfDay()
                );
                piecesResult.setText("📊 Pieces Sold: " + sold);
                piecesResult.setStyle("-fx-text-fill: " + UIStyles.SUCCESS_COLOR + ";");
            } catch (Exception ex) {
                piecesResult.setText("Error: " + ex.getMessage());
                piecesResult.setStyle("-fx-text-fill: " + UIStyles.ERROR_COLOR + ";");
            }
        });

        piecesCard.getChildren().addAll(productIdLabel, productIdField, piecesSoldBtn, piecesResult);

        // Best Seller Card
        VBox bestSellerCard = UIStyles.createCard("Best Seller Product (All Products)");
        
        Button bestSellerBtn = new Button("Find Best Seller");
        UIStyles.styleSuccessButton(bestSellerBtn);

        TextArea bestSellerResult = new TextArea();
        bestSellerResult.setEditable(false);
        bestSellerResult.setPrefRowCount(4);
        bestSellerResult.setWrapText(true);
        bestSellerResult.setStyle(
            "-fx-background-color: #F5F5F5;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        bestSellerBtn.setOnAction(e -> {
            try {
                Product p = Product.getBestSellerProduct(
                    fromDate.getValue().atStartOfDay(),
                    toDate.getValue().atStartOfDay()
                );
                
                if (p == null) {
                    bestSellerResult.setText("No data available for the selected period");
                } else {
                    bestSellerResult.setText(
                        "🏆 Best Seller Product:\n\n" +
                        "Product ID: " + p.getProductId() + "\n" +
                        "Name: " + p.getName() + "\n" +
                        "Price: $" + String.format("%.2f", p.getPrice()) + "\n" +
                        "Current Stock: " + p.getStock()
                    );
                }
            } catch (Exception ex) {
                bestSellerResult.setText("Error: " + ex.getMessage());
            }
        });

        bestSellerCard.getChildren().addAll(bestSellerBtn, bestSellerResult);

        // Most Revenue Card
        VBox revenueCard = UIStyles.createCard("Most Revenue Product (All Products)");
        
        Button revenueBtn = new Button("Find Most Revenue Product");
        UIStyles.stylePrimaryButton(revenueBtn);

        TextArea revenueResult = new TextArea();
        revenueResult.setEditable(false);
        revenueResult.setPrefRowCount(4);
        revenueResult.setWrapText(true);
        revenueResult.setStyle(
            "-fx-background-color: #F5F5F5;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;"
        );

        revenueBtn.setOnAction(e -> {
            try {
                Product p = Product.getMostRevenueProduct(
                    fromDate.getValue().atStartOfDay(),
                    toDate.getValue().atStartOfDay()
                );
                
                if (p == null) {
                    revenueResult.setText("No data available for the selected period");
                } else {
                    revenueResult.setText(
                        "💰 Most Revenue Product:\n\n" +
                        "Product ID: " + p.getProductId() + "\n" +
                        "Name: " + p.getName() + "\n" +
                        "Price: $" + String.format("%.2f", p.getPrice()) + "\n" +
                        "Current Stock: " + p.getStock()
                    );
                }
            } catch (Exception ex) {
                revenueResult.setText("Error: " + ex.getMessage());
            }
        });

        revenueCard.getChildren().addAll(revenueBtn, revenueResult);

        // Back button
        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(20, 0, 0, 0));
        
        Button backBtn = new Button("Back to Dashboard");
        UIStyles.styleSecondaryButton(backBtn);
        backBtn.setOnAction(e -> SellerDashboard.show(stage, seller));
        
        bottomBox.getChildren().add(backBtn);

        mainContent.getChildren().addAll(
            dateCard, 
            piecesCard, 
            bestSellerCard, 
            revenueCard,
            bottomBox
        );

        scrollPane.setContent(mainContent);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 900, 750);
        stage.setScene(scene);
        stage.setTitle("Product Reports");
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

        Label titleLabel = new Label("📊 Product Reports");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        header.getChildren().add(titleLabel);
        return header;
    }
}