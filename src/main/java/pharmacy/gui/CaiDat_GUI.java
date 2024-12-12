package pharmacy.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import pharmacy.bus.NhaCungCap_BUS;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.connections.DatabaseConnection;
import pharmacy.dao.NhanVien_DAO;
import pharmacy.entity.NhaCungCap;
import pharmacy.entity.NhanVien;
import pharmacy.entity.TaiKhoan;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PasswordUtil;

public class CaiDat_GUI {

    @FXML
    private Button backBtn;

    @FXML
    private TextField usernameField;

    @FXML
    private DatePicker birthdayField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField phoneField;

    @FXML
    private HBox root;

    @FXML
    private PasswordField passwordField;

    @FXML
    private RadioButton xRadio;

    @FXML
    private RadioButton yRadio;

    @FXML
    private Text passwordEditBtn;

    @FXML
    private Text birthdayEditBtn;

    @FXML
    private Text phoneEditBtn;

    @FXML
    private Text emailEditBtn;

    @FXML
    private Button birthBtn;

    @FXML
    private Button phoneBtn;

    @FXML
    private Button emailBtn;

    @FXML
    private Pane mainContentPane;

    private Stage currentStage;

    private boolean isEditing = false;

    private NhanVien_BUS nvBus = new NhanVien_BUS();

    @FXML
    public void initialize() throws SQLException {

        handleBackBtnClick();
        loadDataToFields();
        handleEditPassword();
        handlePasswordInput();
        handleEditBirthday();
        handleEditEmail();
        handleEditPhone();
        setupRadioButtons();
    }

    @FXML
    public void handleEditPassword() {
        passwordEditBtn.setOnMouseClicked(event -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SuaMatKhau_GUI.fxml"));
            try {
                Parent parent = loader.load();

                SuaMatKhau_GUI suaMatKhauGui = loader.getController();

                suaMatKhauGui.setUsername(new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap().getMaNhanVien());

                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.setResizable(false);
                stage.getIcons().addAll(new Image(getClass().getResource("/images/update-icon.png").toExternalForm()));
                stage.setTitle("Sửa Mật Khẩu");
                stage.show();

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void handleEditBirthday() throws SQLException {
        birthBtn.setVisible(false);

        birthdayEditBtn.setOnMouseClicked(event -> {
            birthdayField.setValue(null);
            birthdayField.setEditable(true);
            birthBtn.setVisible(true);
            birthdayEditBtn.setVisible(false);
        });

        birthBtn.setOnMouseClicked(event -> {
            try {
                TaiKhoan_BUS accountBus = new TaiKhoan_BUS();

                LocalDate newBirthday = birthdayField.getValue();
                // birthday
                if (newBirthday == null) {
                    showErrorDialog("Ngày sinh chưa được sửa.");
                    return;
                }
                NhanVien currentEmp = accountBus.getCurrentAccount().getTenDangNhap();
                NhanVien nhanVien = new NhanVien(currentEmp.getMaNhanVien(), currentEmp.getHoTen(), currentEmp.getChucVu(), currentEmp.getSoDienThoai(), currentEmp.getEmail(), currentEmp.getNgayVaoLam(), currentEmp.getTrangThai(), currentEmp.getTrinhDo(), currentEmp.getGioiTinh(), newBirthday, currentEmp.getTienLuong(), currentEmp.getCccd());
                new NhanVien_BUS().updateEmployee(nhanVien);

                showSuccessModal("Cập nhật thông tin thành công!");
                birthBtn.setVisible(false);
                birthdayEditBtn.setVisible(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(message);

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
    public void handleEditEmail() throws SQLException {
        emailBtn.setVisible(false);

        emailEditBtn.setOnMouseClicked(event -> {
            emailField.clear();
            emailField.setEditable(true);
            emailBtn.setVisible(true);
            emailEditBtn.setVisible(false);
        });

        emailBtn.setOnMouseClicked(event -> {
            try {
                // Get current logged-in user (can be adapted as per your logic)
                TaiKhoan_BUS accountBus = new TaiKhoan_BUS();
                // email    
                String newEmail = emailField.getText().trim();

                if (newEmail.isEmpty()) {
                    showErrorDialog("Email không được rỗng.");
                } else if (!newEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                    showErrorDialog("Email không hợp lệ. Vui lòng ...");
                } else {
                    NhanVien currentEmp = accountBus.getCurrentAccount().getTenDangNhap();
                    NhanVien nhanVien = new NhanVien(currentEmp.getMaNhanVien(), currentEmp.getHoTen(), currentEmp.getChucVu(), currentEmp.getSoDienThoai(), newEmail, currentEmp.getNgayVaoLam(), currentEmp.getTrangThai(), currentEmp.getTrinhDo(), currentEmp.getGioiTinh(), currentEmp.getNamSinh(), currentEmp.getTienLuong(), currentEmp.getCccd());
                    new NhanVien_BUS().updateEmployee(nhanVien);

                    showSuccessModal("Cập nhật thông tin thành công!");
                    emailBtn.setVisible(false);
                    emailEditBtn.setVisible(true);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void handleEditPhone() throws SQLException {
        phoneBtn.setVisible(false);

        phoneEditBtn.setOnMouseClicked(event -> {
            phoneField.clear();
            phoneField.setEditable(true);
            phoneBtn.setVisible(true);
            phoneEditBtn.setVisible(false);
        });

        phoneBtn.setOnMouseClicked(event -> {
            try {
                // Get current logged-in user (can be adapted as per your logic)
                TaiKhoan_BUS accountBus = new TaiKhoan_BUS();
                // email    
                String newPhone = phoneField.getText().trim();
                if (newPhone.matches("^0//d{9}$")) {
                    showErrorDialog("Số điện thoại không hợp lệ. Vui lòng ...");
                    return;
                }

                NhanVien currentEmp = accountBus.getCurrentAccount().getTenDangNhap();
                NhanVien nhanVien = new NhanVien(currentEmp.getMaNhanVien(), currentEmp.getHoTen(), currentEmp.getChucVu(), newPhone, currentEmp.getEmail(), currentEmp.getNgayVaoLam(), currentEmp.getTrangThai(), currentEmp.getTrinhDo(), currentEmp.getGioiTinh(), currentEmp.getNamSinh(), currentEmp.getTienLuong(), currentEmp.getCccd());
                new NhanVien_BUS().updateEmployee(nhanVien);

                showSuccessModal("Cập nhật thông tin thành công!");
                phoneBtn.setVisible(false);
                phoneEditBtn.setVisible(true);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void loadDataToFields() throws SQLException {
        try {

            TaiKhoan_BUS accountBus = new TaiKhoan_BUS();
            String maNhanVien = accountBus.getCurrentAccount().getTenDangNhap().getMaNhanVien();

            NhanVien nhanVien = nvBus.getEmployeeByMaNhanVien(maNhanVien);

            usernameField.setText(nhanVien.getMaNhanVien());
            birthdayField.setValue(nhanVien.getNamSinh());
            phoneField.setText(nhanVien.getSoDienThoai());
            emailField.setText(nhanVien.getEmail());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showSuccessModal(String message) {
        Stage modalStage = new Stage();
        modalStage.setResizable(false);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.getIcons().addAll(new Image(
                getClass().getClassLoader().getResource("images/tick-icon.png").toExternalForm()));

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

    @FXML
    public void handleBackBtnClick() {
        backBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(backBtn, 1, 0.5, 200, () -> {
            });
        });

        backBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(backBtn, 0.5, 1, 200, () -> {
            });
        });

        backBtn.setOnMouseClicked(event -> {
            try {
                Parent insFrame = FXMLLoader.load(getClass().getResource("/fxml/TrangChu_GUI.fxml"));
                root.getChildren().clear();
                root.getChildren().add(insFrame);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void handlePasswordInput() {
        passwordField.setOnKeyTyped(event -> {
            passwordTextField.setText(passwordField.getText());
        });

        passwordTextField.setOnKeyTyped(event -> {
            passwordField.setText(passwordTextField.getText());
        });

    }

    private void setupRadioButtons() {

        // ngang
        xRadio.setOnAction(event -> {
            switchLayout("/fxml/MainLayout_md_GUI.fxml");
            xRadio.setSelected(true);
            yRadio.setSelected(false);
        });

        // dọc
        yRadio.setOnAction(event -> {
            switchLayout("/fxml/MainLayout_lg_GUI.fxml");
            yRadio.setSelected(true);
            xRadio.setSelected(false);
        });
    }

    private void switchLayout(String fxmlPath) {
        try {
            // Load the new layout
            Parent newRoot = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene newScene = new Scene(newRoot);

            // Get the current stage
            if (currentStage == null) {
                currentStage = (Stage) root.getScene().getWindow();
            }

            // Apply the new scene
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void setStatusOfMenuBar(String status) {
        if (status.equals("horizontal")) {
            xRadio.setSelected(true);
            yRadio.setSelected(false);
        } else {
            xRadio.setSelected(false);
            yRadio.setSelected(true);
        }
    }
}
