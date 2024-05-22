package com.example.ges;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class LoginController implements Initializable {

    @FXML
    private TextField login;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Text showRegisterStage;

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionpatients?user=root&password=";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ecouteurs();
    }

    private void ecouteurs() {
        showRegisterStage.setOnMouseClicked(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                try {
                    showRegisterStage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void login() throws SQLException, ClassNotFoundException, IOException {
        if (isValidUser()) {
            navigateToHomeView();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid username or password.");
        }
    }

    private boolean isValidUser() throws SQLException, ClassNotFoundException {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "SELECT * FROM personne WHERE login = ? AND password = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, login.getText());
                ps.setString(2, password.getText());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToHomeView() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("gestion-patients-view.fxml")));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Home View");
        stage.show();
    }

    @FXML
    private void showRegisterStage() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("RegisterView.fxml")));
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("User Registration");
        stage.getIcons().add(new Image("/asset/icon.png"));
        stage.show();
    }
}
