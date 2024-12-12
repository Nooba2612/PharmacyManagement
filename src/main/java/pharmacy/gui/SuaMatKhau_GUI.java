package pharmacy.gui;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PasswordUtil;

public class SuaMatKhau_GUI {

    @FXML
    private Button cancelBtn;

    @FXML
    private Button confirmBtn;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private TextField newPasswordTextField;

    @FXML
    private TextField oldPasswordTextField;

    @FXML
    private PasswordField oldPasswordField;

    @FXML
    private PasswordField passwordConfirmationField;

    @FXML
    private TextField passwordConfirmationTextField;

    @FXML
    private CheckBox showPasswordBtn;

    private String username;

    @FXML
    public void initialize() {
        handleCancelBtn();
        handleInputPassword();
        handleShowPassword();
    }

    @FXML
    public void handleShowPassword() {
        showPasswordBtn.selectedProperty().addListener(e -> {
            if (showPasswordBtn.isSelected()) {
                oldPasswordField.setVisible(false);
                newPasswordField.setVisible(false);
                passwordConfirmationField.setVisible(false);

                oldPasswordTextField.setVisible(true);
                newPasswordTextField.setVisible(true);
                passwordConfirmationTextField.setVisible(true);

                oldPasswordTextField.setText(oldPasswordField.getText());
                newPasswordTextField.setText(newPasswordField.getText());
                passwordConfirmationTextField.setText(passwordConfirmationField.getText());
            } else {
                oldPasswordField.setVisible(true);
                newPasswordField.setVisible(true);
                passwordConfirmationField.setVisible(true);

                oldPasswordTextField.setVisible(false);
                newPasswordTextField.setVisible(false);
                passwordConfirmationTextField.setVisible(false);

                oldPasswordField.setText(oldPasswordTextField.getText());
                newPasswordField.setText(newPasswordTextField.getText());
                passwordConfirmationField.setText(passwordConfirmationTextField.getText());
            }
        });
    }

    private void showSuccessfulModal(String message) {
        Stage modalStage = new Stage();
        modalStage.setResizable(false);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.getIcons()
                .addAll(new Image(getClass().getClassLoader().getResource("images/tick-icon.png").toString()));

        ImageView icon = new ImageView(
                new Image(getClass().getClassLoader().getResource("images/tick-icon.png").toExternalForm()));
        icon.setFitWidth(40);
        icon.setFitHeight(40);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 15px; -fx-padding: 10px;");

        Button closeButton = new Button("Đóng");
        closeButton.setStyle("-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; "
                + "-fx-border-radius: 10px; -fx-cursor: hand; -fx-padding: 8px 20px;");
        closeButton.setOnAction(e -> modalStage.close());

        closeButton.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(closeButton, 1, 0.6, 300, () -> {
            });
        });
        closeButton.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(closeButton, 0.6, 1, 300, () -> {
            });
        });

        HBox contentLayout = new HBox(10, messageLabel, icon);
        contentLayout.setStyle("-fx-alignment: center; -fx-padding: 20px;");

        VBox layout = new VBox(15, contentLayout, closeButton);
        layout.setStyle("-fx-alignment: center; -fx-background-color: #ffffff; "
                + "-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 20px;");

        Scene scene = new Scene(layout, 400, 150);
        modalStage.setScene(scene);

        modalStage.showAndWait();
    }

    @FXML
    public boolean validateCurrentPassword() throws SQLException {

        String storedPassword = new TaiKhoan_BUS().getCurrentAccount().getMatKhau();


        if (!PasswordUtil.checkPassword(oldPasswordField.getText(), storedPassword)) {
            showErrorModal("Mật khẩu hiện tại không đúng.");
            return false;
        }

        return true;
    }

    @FXML
    private void handleInputPassword() {
        confirmBtn.setOnMouseEntered(e -> {
            NodeUtil.applyFadeTransition(confirmBtn, 1, 0.7, 300, () -> {
            });
        });

        confirmBtn.setOnMouseExited(e -> {
            NodeUtil.applyFadeTransition(confirmBtn, 0.7, 1, 300, () -> {
            });
        });

        confirmBtn.setOnAction(e -> {
            try {
                if (validateCurrentPassword() && validatePassword() && validateConfirmationPassword()) {
                    String passwordHashed;
                    if (passwordConfirmationField.isVisible()) {
                        passwordHashed = PasswordUtil.passwordEncoder.encode(passwordConfirmationField.getText());
                    } else {
                        passwordHashed = PasswordUtil.passwordEncoder.encode(passwordConfirmationTextField.getText());
                    }

                    try {
                        new TaiKhoan_BUS().changePassword(username, passwordHashed);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    showSuccessfulModal("Đã thay đổi mật khẩu thành công.");

                    System.out.println("Password saved: " + passwordHashed);

                    confirmBtn.getScene().getWindow().hide();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

    }

    @FXML
    private boolean validateConfirmationPassword() {
        if (!passwordConfirmationField.getText().equals(newPasswordField.getText())
                || !passwordConfirmationTextField.getText().equals(newPasswordField.getText())) {
            showErrorModal("Mật khẩu xác nhận không khớp.");
            return false;
        }

        return true;
    }

    @FXML
    private boolean validatePassword() {
        String password = newPasswordField.getText();
        String passwordText = newPasswordTextField.getText();

        if (password.isEmpty()) {
            showErrorModal("Chưa nhập mật khẩu.");
            return false;
        }

        if (password.length() < 8 || passwordText.length() < 8) {
            showErrorModal("Mật khẩu phải có ít nhất 8 ký tự.");
            return false;
        }

        if (!password.matches(".*[a-z].*") || !passwordText.matches(".*[a-z].*")) {
            showErrorModal("Mật khẩu phải chứa ít nhất một chữ thường.");
            return false;
        }

        if (!password.matches(".*[A-Z].*") || !passwordText.matches(".*[A-Z].*")) {
            showErrorModal("Mật khẩu phải chứa ít nhất một chữ in hoa.");
            return false;
        }

        if (!password.matches(".*\\d.*") || !passwordText.matches(".*\\d.*")) {
            showErrorModal("Mật khẩu phải chứa ít nhất một chữ số.");
            return false;
        }

        return true;
    }

    @FXML
    public void handleCancelBtn() {
        cancelBtn.setOnMouseEntered(e -> {
            NodeUtil.applyFadeTransition(cancelBtn, 1, 0.7, 300, () -> {
            });
        });

        cancelBtn.setOnMouseExited(e -> {
            NodeUtil.applyFadeTransition(cancelBtn, 0.7, 1, 300, () -> {
            });
        });

        cancelBtn.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/NhapEmail_GUI.fxml"));
            BorderPane borderPane = new BorderPane();
            try {
                borderPane = loader.load();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            cancelBtn.getScene().getWindow().hide();

            Stage stage = new Stage();
            Scene scene = new Scene(borderPane);
            stage.setResizable(false);
            stage.setMaximized(true);
            stage.setScene(scene);
            stage.setTitle("Medkit - Pharmacy Management System");
            stage.getIcons()
                    .add(new Image(getClass().getClassLoader().getResource("images/pharmacy-icon.png").toString()));
            stage.show();
        });
    }

    private void showErrorModal(String message) {
        Stage modalStage = new Stage();
        modalStage.setResizable(false);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.getIcons()
                .addAll(new Image(getClass().getClassLoader().getResource("images/x-icon.png").toString()));

        ImageView icon = new ImageView(
                new Image(getClass().getClassLoader().getResource("images/x-icon.png").toExternalForm()));
        icon.setFitWidth(40);
        icon.setFitHeight(40);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 15px; -fx-padding: 10px;");

        Button closeButton = new Button("Đóng");
        closeButton.setStyle("-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; "
                + "-fx-border-radius: 10px; -fx-cursor: hand; -fx-padding: 8px 20px;");
        closeButton.setOnAction(e -> modalStage.close());

        closeButton.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(closeButton, 1, 0.6, 300, () -> {
            });
        });
        closeButton.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(closeButton, 0.6, 1, 300, () -> {
            });
        });

        HBox contentLayout = new HBox(10, messageLabel, icon);
        contentLayout.setStyle("-fx-alignment: center; -fx-padding: 20px;");

        VBox layout = new VBox(15, contentLayout, closeButton);
        layout.setStyle("-fx-alignment: center; -fx-background-color: #ffffff; "
                + "-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 20px;");

        Scene scene = new Scene(layout, 400, 150);
        modalStage.setScene(scene);

        modalStage.showAndWait();
    }

    @FXML
    public void setUsername(String username) {
        this.username = username;
    }

}
