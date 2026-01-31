package ecommerce.UI.dashboards;

import ecommerce.UI.LoginMenu;
import ecommerce.UI.UIStyles;
import ecommerce.data.DataStore;
import ecommerce.domain.Shopping.*;
import ecommerce.domain.users.Customer;
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

public class CustomerDashboard {

    private static Customer currentCustomer;
    private static Cart currentCart;

    private static ObservableList<Product> productList;
    private static ObservableList<Item> cartList;

    private static TableView<Product> productTable;
    private static TableView<Item> cartTable;
    private static Label cartTotalLabel;

    public static void show(Stage stage, Customer customer) {
        currentCustomer = customer;

        // Load or create cart
        currentCart = DataStore.getInstance().getCarts()
                .getOrDefault(currentCustomer.getId(), new Cart(currentCustomer.getId()));
        Cart.setDataStore(DataStore.getInstance());

        productList = FXCollections.observableArrayList(DataStore.getInstance().getProducts().values());
        cartList = FXCollections.observableArrayList(currentCart.getItems());

        BorderPane root = new BorderPane();
        UIStyles.styleBackground(root);
        root.setPadding(new Insets(15));

        // Top: Header
        HBox header = createHeader(stage);
        root.setTop(header);

        // Center: Main content with products and cart
        HBox centerContent = new HBox(15);
        centerContent.setPadding(new Insets(15, 0, 0, 0));
        
        // Left: Products
        VBox productsSection = createProductsSection();
        HBox.setHgrow(productsSection, Priority.ALWAYS);
        
        // Right: Cart
        VBox cartSection = createCartSection();
        cartSection.setPrefWidth(400);
        
        centerContent.getChildren().addAll(productsSection, cartSection);
        root.setCenter(centerContent);

        // Bottom: Orders history
        VBox ordersSection = createOrdersSection();
        root.setBottom(ordersSection);

        Scene scene = new Scene(root, 1400, 800);
        stage.setScene(scene);
        stage.setTitle("Customer Dashboard - " + currentCustomer.getUsername());
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

        Label titleLabel = new Label("🛍️ Shopping Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        Label welcomeLabel = new Label("Welcome, " + currentCustomer.getUsername() + "!");
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

    private static VBox createProductsSection() {
        VBox section = UIStyles.createCard("Available Products");
        section.setPrefHeight(500);

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search products by name, description, or seller...");
        UIStyles.styleTextField(searchField);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterProducts(newVal));

        // Product table
        productTable = new TableView<>(productList);
        UIStyles.styleTableView(productTable);
        VBox.setVgrow(productTable, Priority.ALWAYS);
        
        TableColumn<Product, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        idCol.setPrefWidth(80);
        
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
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });
        
        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockCol.setPrefWidth(80);
        stockCol.setCellFactory(col -> new TableCell<Product, Integer>() {
            @Override
            protected void updateItem(Integer stock, boolean empty) {
                super.updateItem(stock, empty);
                if (empty || stock == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.valueOf(stock));
                    if (stock == 0) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else if (stock < 10) {
                        setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: green;");
                    }
                }
            }
        });
        
        TableColumn<Product, String> sellerCol = new TableColumn<>("Seller");
        sellerCol.setCellValueFactory(new PropertyValueFactory<>("sellerId"));
        sellerCol.setPrefWidth(100);

        productTable.getColumns().addAll(idCol, nameCol, descCol, priceCol, stockCol, sellerCol);

        // Add to cart button
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setPadding(new Insets(10, 0, 0, 0));
        
        Label quantityLabel = UIStyles.createFieldLabel("Quantity:");
        Spinner<Integer> quantitySpinner = new Spinner<>(1, 100, 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.setPrefWidth(80);
        
        Button addToCartBtn = new Button("Add to Cart");
        UIStyles.stylePrimaryButton(addToCartBtn);
        addToCartBtn.setOnAction(e -> addToCart(quantitySpinner.getValue()));
        
        actionBox.getChildren().addAll(quantityLabel, quantitySpinner, addToCartBtn);

        section.getChildren().addAll(searchField, productTable, actionBox);
        return section;
    }

    private static VBox createCartSection() {
        VBox section = UIStyles.createCard("Shopping Cart");
        section.setPrefHeight(500);

        // Cart table
        cartTable = new TableView<>(cartList);
        UIStyles.styleTableView(cartTable);
        VBox.setVgrow(cartTable, Priority.ALWAYS);

        TableColumn<Item, String> productCol = new TableColumn<>("Product");
        productCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getProductId()));
        productCol.setPrefWidth(120);

        TableColumn<Item, Integer> quantityCol = new TableColumn<>("Qty");
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setPrefWidth(50);

        TableColumn<Item, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(80);
        priceCol.setCellFactory(col -> new TableCell<Item, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });

        TableColumn<Item, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getTotal()).asObject());
        totalCol.setPrefWidth(90);
        totalCol.setCellFactory(col -> new TableCell<Item, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", total));
                }
            }
        });

        cartTable.getColumns().addAll(productCol, quantityCol, priceCol, totalCol);

        // Cart total
        cartTotalLabel = new Label("Total: $0.00");
        cartTotalLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        cartTotalLabel.setStyle("-fx-text-fill: " + UIStyles.PRIMARY_COLOR + ";");
        updateCartTotal();

        // Action buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        Button removeBtn = new Button("Remove");
        UIStyles.styleDangerButton(removeBtn);
        removeBtn.setOnAction(e -> removeFromCart());
        
        Button clearBtn = new Button("Clear Cart");
        UIStyles.styleSecondaryButton(clearBtn);
        clearBtn.setOnAction(e -> clearCart());
        
        Button checkoutBtn = new Button("Checkout");
        UIStyles.styleSuccessButton(checkoutBtn);
        checkoutBtn.setOnAction(e -> checkout());
        
        buttonBox.getChildren().addAll(removeBtn, clearBtn, checkoutBtn);

        section.getChildren().addAll(cartTable, new Separator(), cartTotalLabel, buttonBox);
        return section;
    }

    private static VBox createOrdersSection() {
        VBox section = UIStyles.createCard("Order History");
        section.setPrefHeight(200);
        section.setPadding(new Insets(15));
        section.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );

        TableView<Order> ordersTable = new TableView<>();
        UIStyles.styleTableView(ordersTable);
        VBox.setVgrow(ordersTable, Priority.ALWAYS);
        
        ObservableList<Order> orders = FXCollections.observableArrayList(
                Order.getOrdersForCustomer(currentCustomer.getId())
        );
        ordersTable.setItems(orders);
        
        TableColumn<Order, String> orderIdCol = new TableColumn<>("Order ID");
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        orderIdCol.setPrefWidth(150);
        
        TableColumn<Order, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        dateCol.setPrefWidth(180);
        
        TableColumn<Order, Boolean> paidCol = new TableColumn<>("Status");
        paidCol.setCellValueFactory(new PropertyValueFactory<>("paid"));
        paidCol.setPrefWidth(100);
        paidCol.setCellFactory(col -> new TableCell<Order, Boolean>() {
            @Override
            protected void updateItem(Boolean paid, boolean empty) {
                super.updateItem(paid, empty);
                if (empty || paid == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(paid ? "✓ Paid" : "Pending");
                    setStyle(paid ? "-fx-text-fill: " + UIStyles.SUCCESS_COLOR + ";" : "-fx-text-fill: " + UIStyles.WARNING_COLOR + ";");
                }
            }
        });
        
        TableColumn<Order, Double> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        totalCol.setPrefWidth(120);
        totalCol.setCellFactory(col -> new TableCell<Order, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });
        
        ordersTable.getColumns().addAll(orderIdCol, dateCol, paidCol, totalCol);

        section.getChildren().add(ordersTable);
        return section;
    }

    private static void filterProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            productList.setAll(DataStore.getInstance().getProducts().values());
        } else {
            productList.setAll(DataStore.getInstance().getProducts().values().stream()
                    .filter(p -> Product.searchProduct(p, keyword))
                    .toList());
        }
    }

    private static void addToCart(int quantity) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIStyles.showWarning("No Selection", "Please select a product to add to cart");
            return;
        }
        
        if (selected.getStock() < quantity) {
            UIStyles.showError("Insufficient Stock", "Only " + selected.getStock() + " items available");
            return;
        }
        
        if (selected.getStock() == 0) {
            UIStyles.showError("Out of Stock", "This product is currently out of stock");
            return;
        }
        
        currentCart.addItem(selected.getProductId(), quantity, selected.getPrice());
        cartList.setAll(currentCart.getItems());
        updateCartTotal();
        UIStyles.showSuccess("Added", quantity + " x " + selected.getName() + " added to cart");
    }

    private static void removeFromCart() {
        Item selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIStyles.showWarning("No Selection", "Please select an item to remove");
            return;
        }
        currentCart.removeItem(selected.getProductId());
        cartList.setAll(currentCart.getItems());
        updateCartTotal();
    }
    
    private static void clearCart() {
        if (currentCart.getItems().isEmpty()) {
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Clear Cart");
        confirm.setHeaderText("Are you sure?");
        confirm.setContentText("This will remove all items from your cart.");
        
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                currentCart.clear();
                cartList.setAll(currentCart.getItems());
                updateCartTotal();
            }
        });
    }

    private static void checkout() {
        if (currentCart.getItems().isEmpty()) {
            UIStyles.showWarning("Empty Cart", "Your cart is empty. Add some products first!");
            return;
        }
        
        try {
            // Validate stock availability for all items
            for (Item item : currentCart.getItems()) {
                Product product = DataStore.getInstance().getProducts().get(item.getProductId());
                if (product == null) {
                    UIStyles.showError("Product Not Found", "Product " + item.getProductId() + " not found");
                    return;
                }
                if (product.getStock() < item.getQuantity()) {
                    UIStyles.showError("Insufficient Stock", 
                        "Not enough stock for " + product.getName() + 
                        ". Available: " + product.getStock() + ", Required: " + item.getQuantity());
                    return;
                }
            }
            
            // Create order
            String orderId = IDGenerator.generateOrderId();
            Order order = new Order(
                orderId, 
                currentCustomer.getId(), 
                FXCollections.observableArrayList(currentCart.getItems())
            );
            order.pay();
            Order.addOrder(order);
            
            // CRITICAL FIX: Reduce stock for each item
            for (Item item : currentCart.getItems()) {
                Product product = DataStore.getInstance().getProducts().get(item.getProductId());
                if (product != null) {
                    product.reduceStock(item.getQuantity());
                    System.out.println("Stock reduced: " + product.getName() + " - New stock: " + product.getStock());
                }
            }
            
            // Clear cart
            currentCart.clear();
            cartList.setAll(currentCart.getItems());
            updateCartTotal();
            
            // Refresh product list to show updated stock
            productList.setAll(DataStore.getInstance().getProducts().values());
            
            UIStyles.showSuccess("Order Placed", 
                "Your order has been placed successfully!\n" +
                "Order ID: " + order.getOrderId() + "\n" +
                "Total: $" + String.format("%.2f", order.getTotalAmount()) + "\n\n" +
                "Stock has been updated.");
            
        } catch (Exception ex) {
            UIStyles.showError("Checkout Failed", "Failed to place order: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private static void updateCartTotal() {
        double total = currentCart.getItems().stream()
            .mapToDouble(Item::getTotal)
            .sum();
        cartTotalLabel.setText(String.format("Total: $%.2f", total));
    }
}