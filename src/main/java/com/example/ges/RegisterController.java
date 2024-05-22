package com.example.ges;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.text.Text; // Correct import for Text

public class RegisterController implements Initializable {

    @FXML
    private TextField tfCin;

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfPrenom;

    @FXML
    private TextField tfLogin;

    @FXML
    private TextField tfPassword;

    @FXML
    private Text showLoginStage;

    @FXML
    private TextField tfConfirmPassword;

    @FXML
    private Button registerButton;

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionpatients?user=root&password=";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ecouteurs();
    }

    private void ecouteurs() {
        showLoginStage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    showLoginStage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void register() throws SQLException, ClassNotFoundException, IOException {
        if (isValidated()) {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                if (!isAlreadyRegistered(conn)) {
                    String query = "INSERT INTO personne (cin, nom, prenom, login, password) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(query)) {
                        ps.setString(1, tfCin.getText());
                        ps.setString(2, tfNom.getText());
                        ps.setString(3, tfPrenom.getText());
                        ps.setString(4, tfLogin.getText());
                        ps.setString(5, tfPassword.getText());

                        ps.executeUpdate();
                        clearForm();
                        showLoginStage();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Registration successful.");
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Username already exists.");
                }
            }
        }
    }

    private boolean isAlreadyRegistered(Connection conn) throws SQLException {
        String query = "SELECT * FROM personne WHERE login = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, tfLogin.getText());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean isValidated() {
        String password = tfPassword.getText();
        String confirmPassword = tfConfirmPassword.getText();

        if (tfCin.getText().isEmpty() || tfNom.getText().isEmpty() || tfPrenom.getText().isEmpty() ||
                tfLogin.getText().isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Passwords do not match.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        tfCin.clear();
        tfNom.clear();
        tfPrenom.clear();
        tfLogin.clear();
        tfPassword.clear();
        tfConfirmPassword.clear();
    }

    @FXML
    private void showLoginStage() throws IOException {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginView.fxml")));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Login");
        stage.getIcons().add(new Image("/asset/icon.png"));
        stage.show();
    }
    private void navigateToHomeView() throws IOException {
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("gestion-patients-view.fxml")));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Home View");
        stage.show();
    }
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
