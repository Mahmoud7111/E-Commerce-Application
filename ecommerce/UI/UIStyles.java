package ecommerce.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Centralized UI styling for consistent look across the application
 */
public class UIStyles {
    
    // Color scheme
    public static final String PRIMARY_COLOR = "#2196F3";
    public static final String PRIMARY_DARK = "#1976D2";
    public static final String PRIMARY_LIGHT = "#BBDEFB";
    public static final String ACCENT_COLOR = "#FF5722";
    public static final String SUCCESS_COLOR = "#4CAF50";
    public static final String WARNING_COLOR = "#FF9800";
    public static final String ERROR_COLOR = "#F44336";
    public static final String BACKGROUND_COLOR = "#FAFAFA";
    public static final String CARD_COLOR = "#FFFFFF";
    public static final String TEXT_PRIMARY = "#212121";
    public static final String TEXT_SECONDARY = "#757575";
    
    /**
     * Style a button as primary action button
     */
    public static void stylePrimaryButton(Button button) {
        button.setStyle(
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: " + PRIMARY_DARK + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        ));
        
        button.setOnMouseExited(e -> stylePrimaryButton(button));
    }
    
    /**
     * Style a button as secondary action button
     */
    public static void styleSecondaryButton(Button button) {
        button.setStyle(
            "-fx-background-color: " + CARD_COLOR + ";" +
            "-fx-text-fill: " + PRIMARY_COLOR + ";" +
            "-fx-border-color: " + PRIMARY_COLOR + ";" +
            "-fx-border-width: 2;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: " + PRIMARY_LIGHT + ";" +
            "-fx-text-fill: " + PRIMARY_DARK + ";" +
            "-fx-border-color: " + PRIMARY_DARK + ";" +
            "-fx-border-width: 2;" +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-cursor: hand;"
        ));
        
        button.setOnMouseExited(e -> styleSecondaryButton(button));
    }
    
    /**
     * Style a button as danger action button
     */
    public static void styleDangerButton(Button button) {
        button.setStyle(
            "-fx-background-color: " + ERROR_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #D32F2F;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        ));
        
        button.setOnMouseExited(e -> styleDangerButton(button));
    }
    
    /**
     * Style a button as success action button
     */
    public static void styleSuccessButton(Button button) {
        button.setStyle(
            "-fx-background-color: " + SUCCESS_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #388E3C;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10 20;" +
            "-fx-background-radius: 5;" +
            "-fx-cursor: hand;"
        ));
        
        button.setOnMouseExited(e -> styleSuccessButton(button));
    }
    
    /**
     * Create a styled card container
     */
    public static VBox createCard(String title) {
        VBox card = new VBox(15);
        card.setStyle(
            "-fx-background-color: " + CARD_COLOR + ";" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        card.setPadding(new Insets(20));
        
        if (title != null && !title.isEmpty()) {
            Label titleLabel = new Label(title);
            titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
            titleLabel.setTextFill(Color.web(TEXT_PRIMARY));
            card.getChildren().add(titleLabel);
        }
        
        return card;
    }
    
    /**
     * Style a text field
     */
    public static void styleTextField(TextField field) {
        field.setStyle(
            "-fx-background-color: " + CARD_COLOR + ";" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 14px;"
        );
        
        field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                field.setStyle(
                    "-fx-background-color: " + CARD_COLOR + ";" +
                    "-fx-border-color: " + PRIMARY_COLOR + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;" +
                    "-fx-padding: 10;" +
                    "-fx-font-size: 14px;"
                );
            } else {
                styleTextField(field);
            }
        });
    }
    
    /**
     * Style a password field
     */
    public static void stylePasswordField(PasswordField field) {
        field.setStyle(
            "-fx-background-color: " + CARD_COLOR + ";" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 5;" +
            "-fx-background-radius: 5;" +
            "-fx-padding: 10;" +
            "-fx-font-size: 14px;"
        );
        
        field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                field.setStyle(
                    "-fx-background-color: " + CARD_COLOR + ";" +
                    "-fx-border-color: " + PRIMARY_COLOR + ";" +
                    "-fx-border-width: 2;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;" +
                    "-fx-padding: 10;" +
                    "-fx-font-size: 14px;"
                );
            } else {
                stylePasswordField(field);
            }
        });
    }
    
    /**
     * Create a label with title styling
     */
    public static Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.BOLD, 24));
        label.setTextFill(Color.web(TEXT_PRIMARY));
        return label;
    }
    
    /**
     * Create a label with subtitle styling
     */
    public static Label createSubtitleLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.SEMI_BOLD, 16));
        label.setTextFill(Color.web(TEXT_SECONDARY));
        return label;
    }
    
    /**
     * Create a label with field label styling
     */
    public static Label createFieldLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", FontWeight.MEDIUM, 14));
        label.setTextFill(Color.web(TEXT_PRIMARY));
        return label;
    }
    
    /**
     * Style a TableView
     */
    public static void styleTableView(TableView<?> table) {
        table.setStyle(
            "-fx-background-color: " + CARD_COLOR + ";" +
            "-fx-background-radius: 5;" +
            "-fx-border-color: #E0E0E0;" +
            "-fx-border-radius: 5;"
        );
    }
    
    /**
     * Apply background styling to a pane
     */
    public static void styleBackground(Region pane) {
        pane.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
    }
    
    /**
     * Show a styled success alert
     */
    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + CARD_COLOR + ";");
        
        alert.showAndWait();
    }
    
    /**
     * Show a styled error alert
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + CARD_COLOR + ";");
        
        alert.showAndWait();
    }
    
    /**
     * Show a styled warning alert
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: " + CARD_COLOR + ";");
        
        alert.showAndWait();
    }
}