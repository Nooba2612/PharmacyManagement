package pharmacy.gui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pharmacy.bus.KhachHang_BUS;
import pharmacy.entity.KhachHang;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.StringUtil;

public class ThemKhachHangNhanh_GUI {

    @FXML
    private Label birthdayAlert;

    @FXML
    private DatePicker birthdayField;

    @FXML
    private Button clearDataBtn;

    @FXML
    private Label genderAlert;

    @FXML
    private Text genderFieldAlert;

    @FXML
    private ComboBox<String> genderSelect;

    @FXML
    private Label idAlert;

    @FXML
    private TextField idField;

    @FXML
    private Label nameAlert;

    @FXML
    private TextField nameField;

    @FXML
    private Text nameFieldAlert;

    @FXML
    private TextField noteField;

    @FXML
    private Label phoneAlert;

    @FXML
    private TextField phoneField;

    @FXML
    private Text phoneFieldAlert;

    @FXML
    private HBox root;

    @FXML
    private Button rejectBtn;

    @FXML
    private Button submitBtn;

    @FXML
    private Pane rootPane;

    private String customerPhoneNumber;

    @FXML
    public void initialize() throws SQLException {
        rootPane.getChildren().forEach(child -> {
            if (child instanceof Button) {
                child.setOnMouseEntered(event -> {
                    NodeUtil.applyFadeTransition(child, 1, 0.7, 300, () -> {
                    });
                });

                child.setOnMouseExited(event -> {
                    NodeUtil.applyFadeTransition(child, 0.7, 1, 300, () -> {
                    });
                });
            }
        });

        rejectBtn.setOnAction(event -> {
            rejectBtn.getScene().getWindow().hide();
        });

        clearForm();
        handleAddCustomer();
    }

    @FXML
    public void setUpForm(String phone) throws SQLException {
        customerPhoneNumber = phone;
        idField.setText(generateID());
        phoneField.setText(phone);
        genderSelect.getItems().addAll("Nam", "Nữ", "Khác");

        clearDataBtn.setOnMouseClicked(event -> {
            try {
                clearForm();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void clearForm() throws SQLException {
        idField.setText(generateID());
        nameField.setText("");
        phoneField.setText(customerPhoneNumber);
        nameField.setText("");
        genderSelect.setValue(null);
        birthdayField.setValue(null);
        noteField.setText("");
    }

    @FXML
    public void handleAddCustomer() throws SQLException {
        submitBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 1, 0.6, 200, () -> {
            });
        });
        submitBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 0.6, 1, 200, () -> {
            });
        });
        submitBtn.setCursor(Cursor.HAND);

        // add customer
        submitBtn.setOnAction(event -> {

            if (validateForm()) {
                String maKhachHang = idField.getText();
                String hoTen = nameField.getText();
                String soDienThoai = phoneField.getText();
                LocalDate namSinh = birthdayField.getValue();
                String gioiTinh = genderSelect.getValue();
                String ghiChu = noteField.getText();

                if (namSinh == null) {
                    birthdayAlert.setText("Ngày sinh không được để trống.");
                    birthdayAlert.setVisible(true);
                } else {
                    birthdayAlert.setVisible(false);
                }

                KhachHang khachHang = new KhachHang(maKhachHang, hoTen, soDienThoai, 0, namSinh, ghiChu, gioiTinh);

                try {
                    if (new KhachHang_BUS().createKhachHang(khachHang)) {
                        showAddCustomerSuccessModal("Thêm khách hàng thành công");
                        submitBtn.getScene().getWindow().hide();
                    } else {
                        showErrorDialog();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    clearForm();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public String generateID() throws SQLException {
        int customerNumber = new KhachHang_BUS().countCustomer();
        String id = String.format("KH%04d", customerNumber);

        return id;
    }

    @FXML
    public boolean validateForm() {
        boolean isValid = true;

        // Validate Name field
        if (nameField.getText().trim().isEmpty()) {
            nameAlert.setText("Họ và tên không được rỗng.");
            nameAlert.setVisible(true);
            isValid = false;
        } else {
            String name = StringUtil.capitalizeWords(nameField.getText().trim());
            nameField.setText(name);
            nameAlert.setVisible(false);
        }

        // Validate Phone field
        String phoneText = phoneField.getText().trim();
        if (phoneText.isEmpty()) {
            phoneAlert.setText("Số điện thoại không được để trống.");
            phoneAlert.setVisible(true);
            isValid = false;
        } else if (!phoneText.matches("^0\\d{9}$")) {
            phoneAlert.setText("Số điện thoại không hợp lệ.");
            phoneAlert.setVisible(true);
            isValid = false;
        } else {
            phoneAlert.setVisible(false);
        }

        // Validate Customer type field
        String[] VALID_TYPES = {"Nam", "Nữ", "Khác"};
        String customerTypeValue = (genderSelect.getValue() != null) ? genderSelect.getValue().trim()
                : genderSelect.getEditor().getText().trim();

        if (customerTypeValue.isEmpty()) {
            genderAlert.setText("Giới tính chưa được chọn.");
            genderAlert.setVisible(true);
            isValid = false;
        } else if (!Arrays.asList(VALID_TYPES).contains(customerTypeValue)) {
            genderAlert.setText("Giới tính không hợp lệ.");
            genderAlert.setVisible(true);
            isValid = false;
        } else {
            genderAlert.setVisible(false);
        }

        // Validate Birthday field
        if (birthdayField.getValue() == null) {
            birthdayAlert.setText("Ngày sinh không được rỗng.");
            birthdayAlert.setVisible(true);
            isValid = false;
        } else {
            birthdayAlert.setVisible(false);
        }

        return isValid;
    }

    @FXML
    private void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText("Không thể tạo khách hàng.");
        alert.setContentText("Vui lòng kiểm tra lại thông tin đã nhập.");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/images/alert-icon.png")));
        icon.setFitHeight(48);
        icon.setFitWidth(48);
        alert.setGraphic(icon);

        alert.getButtonTypes().clear();

        ButtonType confirmButton = new ButtonType("Xác nhận");
        alert.getButtonTypes().add(confirmButton);

        Node confirmBtn = alert.getDialogPane().lookupButton(confirmButton);
        confirmBtn.setStyle(
                "-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-cursor: hand;");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/alert-icon.png")));

        alert.showAndWait();
    }

    @FXML
    private void showAddCustomerSuccessModal(String message) {
        Stage modalStage = new Stage();
        modalStage.setResizable(false);
        modalStage.initModality(Modality.APPLICATION_MODAL);

        ImageView icon = new ImageView(new Image(
                getClass().getClassLoader().getResource("images/tick-icon.png").toExternalForm()));
        icon.setFitWidth(40);
        icon.setFitHeight(40);

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");

        Button closeButton = new Button("Đóng");
        closeButton.setStyle(
                "-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; "
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
        layout.setStyle(
                "-fx-alignment: center; -fx-background-color: #ffffff; "
                + "-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 20px;");

        Scene scene = new Scene(layout, 400, 150);
        modalStage.setScene(scene);

        modalStage.showAndWait();
    }

}
