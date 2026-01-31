package ecommerce.UI.dashboards.Admin;

import ecommerce.UI.UIStyles;
import ecommerce.data.DataStore;
import ecommerce.domain.Shopping.Product;
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



public class AdminProductManagement {

    private static final ObservableList<Product> productList = FXCollections.observableArrayList();

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

        VBox contentCard = UIStyles.createCard("Product Management");
        VBox.setVgrow(contentCard, Priority.ALWAYS);

        // Search field
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search products by any field...");
        UIStyles.styleTextField(searchField);

        // Table
        TableView<Product> tableView = new TableView<>(productList);
        UIStyles.styleTableView(tableView);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        TableColumn<Product, String> idCol = new TableColumn<>("Product ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        idCol.setPrefWidth(150);

        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<Product, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(250);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(100);
        priceCol.setCellFactory(col -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%.2f", price));
            }
        });

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setPrefWidth(80);

        TableColumn<Product, String> sellerCol = new TableColumn<>("Seller ID");
        sellerCol.setCellValueFactory(new PropertyValueFactory<>("sellerId"));
        sellerCol.setPrefWidth(150);

        tableView.getColumns().addAll(idCol, nameCol, descCol, priceCol, stockCol, sellerCol);

        // Load products
        refreshProducts();

        // Search functionality
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            productList.clear();
            for (Product p : DataStore.getInstance().getProducts().values()) {
                if (Product.searchProduct(p, newVal)) {
                    productList.add(p);
                }
            }
        });

        // Action buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        Button addBtn = new Button("Add Product");
        UIStyles.stylePrimaryButton(addBtn);
        addBtn.setOnAction(e -> showProductForm(stage, null));

        Button editBtn = new Button("Edit");
        UIStyles.styleSecondaryButton(editBtn);
        editBtn.setOnAction(e -> {
            Product selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showProductForm(stage, selected);
            } else {
                UIStyles.showWarning("No Selection", "Please select a product to edit");
            }
        });

        Button deleteBtn = new Button("Delete");
        UIStyles.styleDangerButton(deleteBtn);
        deleteBtn.setOnAction(e -> {
            Product selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete Product?");
                confirm.setContentText("Are you sure you want to delete: " + selected.getName() + "?");
                
                confirm.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        selected.removeProduct();
                        refreshProducts();
                        UIStyles.showSuccess("Deleted", "Product deleted successfully");
                    }
                });
            } else {
                UIStyles.showWarning("No Selection", "Please select a product to delete");
            }
        });

        Button backBtn = new Button("Back");
        UIStyles.styleSecondaryButton(backBtn);
        backBtn.setOnAction(e -> AdminDashboard.show(stage, null));

        buttonBox.getChildren().addAll(addBtn, editBtn, deleteBtn, backBtn);

        contentCard.getChildren().addAll(searchField, tableView, buttonBox);
        mainContent.getChildren().add(contentCard);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);
        stage.setTitle("Admin - Product Management");
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

        Label titleLabel = new Label("📦 Product Management");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));

        header.getChildren().add(titleLabel);
        return header;
    }

    private static void refreshProducts() {
        productList.clear();
        productList.addAll(DataStore.getInstance().getProducts().values());
    }

    private static void showProductForm(Stage stage, Product product) {
        Stage formStage = new Stage();
        formStage.setTitle(product == null ? "Add New Product" : "Edit Product");

        VBox root = new VBox(15);
        root.setPadding(new Insets(25));
        UIStyles.styleBackground(root);

        VBox formCard = UIStyles.createCard(product == null ? "Add New Product" : "Edit Product");

        GridPane form = new GridPane();
        form.setHgap(15);
        form.setVgap(15);

        // Name
        Label nameLabel = UIStyles.createFieldLabel("Product Name *");
        TextField nameField = new TextField(product != null ? product.getName() : "");
        nameField.setPromptText("Enter product name");
        UIStyles.styleTextField(nameField);

        // Description
        Label descLabel = UIStyles.createFieldLabel("Description");
        TextArea descField = new TextArea(product != null ? product.getDescription() : "");
        descField.setPromptText("Enter product description");
        descField.setPrefRowCount(3);
        descField.setWrapText(true);
        UIStyles.styleTextField(new TextField()); // Style similar

        // Price
        Label priceLabel = UIStyles.createFieldLabel("Price *");
        TextField priceField = new TextField(product != null ? String.valueOf(product.getPrice()) : "");
        priceField.setPromptText("0.00");
        UIStyles.styleTextField(priceField);

        // Stock
        Label stockLabel = UIStyles.createFieldLabel("Stock Quantity *");
        TextField stockField = new TextField(product != null ? String.valueOf(product.getStock()) : "");
        stockField.setPromptText("0");
        UIStyles.styleTextField(stockField);

        // Seller ID
        Label sellerLabel = UIStyles.createFieldLabel("Seller ID *");
        TextField sellerField = new TextField(product != null ? product.getSellerId() : "");
        sellerField.setPromptText("Enter seller ID");
        UIStyles.styleTextField(sellerField);

        form.add(nameLabel, 0, 0);
        form.add(nameField, 0, 1);
        form.add(descLabel, 0, 2);
        form.add(descField, 0, 3);
        form.add(priceLabel, 0, 4);
        form.add(priceField, 0, 5);
        form.add(stockLabel, 0, 6);
        form.add(stockField, 0, 7);
        form.add(sellerLabel, 0, 8);
        form.add(sellerField, 0, 9);

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

                String name = nameField.getText().trim();
                String desc = descField.getText().trim();
                String priceStr = priceField.getText().trim();
                String stockStr = stockField.getText().trim();
                String sellerId = sellerField.getText().trim();

                if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty() || sellerId.isEmpty()) {
                    errorLabel.setText("Please fill in all required fields (*)");
                    return;
                }

                double price = Double.parseDouble(priceStr);
                int stock = Integer.parseInt(stockStr);

                if (price < 0) {
                    errorLabel.setText("Price cannot be negative");
                    return;
                }

                if (stock < 0) {
                    errorLabel.setText("Stock cannot be negative");
                    return;
                }

                if (product == null) {
                    // Add new
                    Product newProduct = new Product(
                        IDGenerator.generateProductId(),
                        name, desc, price, stock, sellerId
                    );
                    newProduct.addProduct();
                    UIStyles.showSuccess("Success", "Product added successfully");
                } else {
                    // Edit existing
                    product.setName(name);
                    product.setDescription(desc);
                    product.setPrice(price);
                    product.setStock(stock);
                    product.setSellerId(sellerId);
                    UIStyles.showSuccess("Success", "Product updated successfully");
                }

                refreshProducts();
                formStage.close();
                show(stage);

            } catch (NumberFormatException ex) {
                errorLabel.setText("Invalid number format for price or stock");
            } catch (Exception ex) {
                errorLabel.setText("Error: " + ex.getMessage());
            }
        });

        cancelBtn.setOnAction(e -> formStage.close());

        buttonBox.getChildren().addAll(saveBtn, cancelBtn);

        formCard.getChildren().addAll(form, errorLabel, buttonBox);
        root.getChildren().add(formCard);

        Scene scene = new Scene(root, 500, 700);
        formStage.setScene(scene);
        formStage.show();
    }
}