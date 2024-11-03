package pharmacy.gui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pharmacy.bus.HistoryLog_BUS;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.entity.EmployeeHistoryLog;
import pharmacy.entity.NhanVien;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.StringUtil;

public class CapNhatNhanVien_GUI {

    @FXML
    private Label birthdayAlert;

    @FXML
    private TableColumn<EmployeeHistoryLog, LocalDate> birthdayColumn;

    @FXML
    private DatePicker birthdayField;

    @FXML
    private Label cccdAlert;

    @FXML
    private TextField cccdField;

    @FXML
    private Button clearDataBtn;

    @FXML
    private Label emailAlert;

    @FXML
    private TableColumn<EmployeeHistoryLog, String> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private TableColumn<EmployeeHistoryLog, String> employeeColumn;

    @FXML
    private TableView<EmployeeHistoryLog> employeeTable;

    @FXML
    private Label genderAlert;

    @FXML
    private TableColumn<EmployeeHistoryLog, String> genderColumn;

    @FXML
    private ComboBox<String> genderField;

    @FXML
    private TableColumn<EmployeeHistoryLog, String> idColumn;

    @FXML
    private TextField idField;

    @FXML
    private Label joinDateAlert;

    @FXML
    private TableColumn<EmployeeHistoryLog, LocalDate> joinDateColumn;

    @FXML
    private DatePicker joinDateField;

    @FXML
    private Label levelAlert;

    @FXML
    private TableColumn<EmployeeHistoryLog, String> levelColumn;

    @FXML
    private ComboBox<String> levelField;

    @FXML
    private Label nameAlert;

    @FXML
    private TableColumn<EmployeeHistoryLog, String> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private ListView<EmployeeHistoryLog> nameSuggestionBox;

    @FXML
    private Label phoneAlert;

    @FXML
    private TableColumn<EmployeeHistoryLog, String> phoneColumn;

    @FXML
    private TextField phoneField;

    @FXML
    private Label positionAlert;

    @FXML
    private TableColumn<EmployeeHistoryLog, String> positionColumn;

    @FXML
    private TableColumn<EmployeeHistoryLog, String> cccdColumn;

    @FXML
    private TableColumn<EmployeeHistoryLog, Double> salaryColumn;

    @FXML
    private ComboBox<String> positionField;

    @FXML
    private Button rejectBtn;

    @FXML
    private HBox root;

    @FXML
    private Label salaryAlert;

    @FXML
    private ComboBox<String> salaryField;

    @FXML
    private Button submitBtn;

    @FXML
    private Label taxAlert;

    @FXML
    private TableColumn<EmployeeHistoryLog, LocalDateTime> updatedAtColumn;

    private NhanVien currentEmployee;

    private ObservableList<EmployeeHistoryLog> historyList = FXCollections.observableArrayList();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() throws SQLException {
        handleReject();
        renderHistory();
    }

    @FXML
    public void handleReject() {
        rejectBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(rejectBtn, 1, 0.7, 300, () -> {
            });
        });

        rejectBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(rejectBtn, 0.7, 1, 300, () -> {
            });
        });

        rejectBtn.setOnAction(event -> {
            Stage stage = (Stage) rejectBtn.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    public void setUpForm(NhanVien employee) throws SQLException {
        currentEmployee = employee;
        historyList.addAll(new HistoryLog_BUS().getEmployeeHistoryById(employee.getMaNhanVien()
        ));

        genderField.getItems().addAll("Nam", "Nữ", "Khác");
        levelField.getItems().addAll("Đại học", "Cao đẳng", "Cao học");
        positionField.getItems().addAll("Người quản lý", "Nhân viên");
        salaryField.getItems().addAll("5.000.000đ", "10.000.000đ", "15.000.000đ", "20.000.000đ", "25.000.000đ",
                "30.000.000đ");

        TextFormatter<String> phoneFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });

        TextFormatter<String> cccdFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });

        TextFormatter<String> salaryFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        });

        phoneField.setTextFormatter(phoneFormatter);
        cccdField.setTextFormatter(cccdFormatter);
        salaryField.getEditor().setTextFormatter(salaryFormatter);

        clearDataBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(clearDataBtn, 1, 0.6, 200, () -> {
            });
        });
        clearDataBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(clearDataBtn, 0.6, 1, 200, () -> {
            });
        });
        clearDataBtn.setOnMouseClicked(event -> {
            refreshForm();
        });

        // handle if table empty
        if (historyList.isEmpty()) {
            Label noEmployeeLabel = new Label("Không có nhân viên nào trong bảng.");
            noEmployeeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
            employeeTable.setPlaceholder(noEmployeeLabel);
        }

        refreshForm();
        try {
            handleUpdateEmployee();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdateEmployee() throws SQLException {
        submitBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 1, 0.6, 200, () -> {
            });
        });
        submitBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 0.6, 1, 200, () -> {
            });
        });
        submitBtn.setCursor(Cursor.HAND);

        // add employee
        submitBtn.setOnAction(event -> {

            if (validateForm()) {
                String id = idField.getText();
                String name = nameField.getText();
                String phoneNumber = phoneField.getText();
                String email = emailField.getText();
                String position = positionField.getValue();
                String gender = genderField.getValue();
                String level = levelField.getValue();
                LocalDate birthday = birthdayField.getValue();
                LocalDate joinDate = joinDateField.getValue();
                String cccd = cccdField.getText();
                Double salary = Double.valueOf(salaryField.getValue().replace(".", "").replace("đ", ""));
                String status = "Còn làm việc";

                if (birthday == null) {
                    birthdayAlert.setText("Ngày hết hạn không được rỗng.");
                    birthdayAlert.setVisible(true);
                } else {
                    birthdayAlert.setVisible(false);
                }

                if (joinDate == null) {
                    joinDateAlert.setText("Ngày sản xuất không được rỗng.");
                    joinDateAlert.setVisible(true);
                } else {
                    joinDateAlert.setVisible(false);
                }

                NhanVien employee = new NhanVien(id, name, position, phoneNumber, email, joinDate, status, level,
                        gender, birthday, salary, cccd);
                NhanVien nguoiSua = new NhanVien();
                try {
                    nguoiSua = new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap();
                } catch (SQLException ex) {
                }

                EmployeeHistoryLog history = new EmployeeHistoryLog(employee, LocalDateTime.now(), nguoiSua);

                showUpdateEmployeeSuccessModal("Cập nhật nhân viên thành công");
                refreshForm();
                try {
                    new HistoryLog_BUS().addEmployeeHistory(history);
                    new NhanVien_BUS().updateEmployee(employee);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                submitBtn.getScene().getWindow().hide();
            }
        });

    }

    @FXML
    public void renderHistory() throws SQLException {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        joinDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngayVaoLam"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("namSinh"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("trinhDo"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("tienLuong"));
        cccdColumn.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("tenNguoiSua"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("ngayCapNhat"));

        joinDateColumn.setCellFactory(col -> new TableCell<EmployeeHistoryLog, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        birthdayColumn.setCellFactory(col -> new TableCell<EmployeeHistoryLog, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        updatedAtColumn.setCellFactory(col -> new TableCell<EmployeeHistoryLog, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(formatter.format(item));
                }
            }
        });

        employeeTable.setItems(historyList);

    }

    @FXML
    private void showUpdateEmployeeSuccessModal(String message) {
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
    public void refreshForm() {
        idField.setText(currentEmployee.getMaNhanVien());
        nameField.setText(currentEmployee.getHoTen());
        phoneField.setText(currentEmployee.getSoDienThoai());
        emailField.setText(currentEmployee.getEmail());
        positionField.setValue(currentEmployee.getChucVu());
        genderField.setValue(currentEmployee.getGioiTinh());
        joinDateField.setValue(currentEmployee.getNgayVaoLam());
        birthdayField.setValue(currentEmployee.getNamSinh());
        levelField.setValue(currentEmployee.getTrinhDo());
        salaryField.setValue(String.valueOf(currentEmployee.getTienLuong()).replace(".0", ""));
        cccdField.setText(currentEmployee.getCccd());
    }

    @FXML
    public boolean validateForm() {
        boolean isValid = true;

        // Validate Name field
        if (nameField.getText().trim().isEmpty()) {
            nameAlert.setText("Tên nhân viên không được rỗng.");
            nameAlert.setVisible(true);
            isValid = false;
        } else if (!nameField.getText().trim().matches("[a-zA-ZÀ-ỹ\\s]+")) {
            nameAlert.setText("Tên không được chứa số hoặc ký tự đặc biệt.");
            nameAlert.setVisible(true);
            isValid = false;
        } else {
            String name = StringUtil.capitalizeWords(nameField.getText().trim());
            nameField.setText(name);
            nameAlert.setVisible(false);
        }

        // Validate Phone field
        if (phoneField.getText().trim().isEmpty()) {
            phoneAlert.setText("Số điện thoại nhân viên không được rỗng.");
            phoneAlert.setVisible(true);
            isValid = false;
        } else if (!phoneField.getText().matches("^0\\d{9}$")) {
            phoneAlert.setText("Số điện thoại không đúng định dạng.");
            phoneAlert.setVisible(true);
            isValid = false;
        } else {
            phoneAlert.setVisible(false);
        }

        // Validate Email field
        if (emailField.getText().isEmpty()) {
            emailAlert.setText("Email nhân viên không được rỗng.");
            emailAlert.setVisible(true);
            isValid = false;
        } else if (!emailField.getText().matches("(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")) {
            emailAlert.setText("Không đúng định dạng email.");
            emailAlert.setVisible(true);
            isValid = false;
        } else {
            emailAlert.setVisible(false);
        }

        // Validate Position field
        String positionText = positionField.getValue();
        String positionValue = positionField.getEditor().getText();
        String[] VALID_POSTIONS = {"Người quản lý", "Nhân viên"};
        if (positionText == null || positionValue == null || positionText.trim().isEmpty()
                || positionValue.trim().isEmpty()) {
            positionAlert.setText("Chức vụ không được để trống.");
            positionAlert.setVisible(true);
            isValid = false;
        } else if (!Arrays.asList(VALID_POSTIONS).contains(positionText.trim())
                || !Arrays.asList(VALID_POSTIONS).contains(positionValue.trim())) {
            positionAlert.setText("Chức vụ không hợp lệ.");
            positionAlert.setVisible(true);
            isValid = false;
        } else {
            positionAlert.setVisible(false);
        }

        // Validate Gender field
        String[] VALID_GENDERS = {"Nam", "Nữ", "Khác"};
        if (genderField == null || genderField.getEditor().getText() == null || genderField.getValue() == null
                || genderField.getEditor().getText().isEmpty()) {
            genderAlert.setText("Giới tính chưa được chọn.");
            genderAlert.setVisible(true);
            isValid = false;
        } else if (!Arrays.asList(VALID_GENDERS).contains(genderField.getValue())
                || !Arrays.asList(VALID_GENDERS).contains(genderField.getEditor().getText())) {
            genderAlert.setText("Giới tính không hợp lệ.");
            genderAlert.setVisible(true);
            isValid = false;
        } else {
            genderAlert.setVisible(false);
        }

        // Validate CCCD field
        String cccdValue = cccdField.getText();
        if (cccdValue == null || cccdValue.isEmpty()) {
            cccdAlert.setText("Căn cước công dân không được bỏ trống.");
            cccdAlert.setVisible(true);
            isValid = false;
        } else if (cccdValue.length() != 12) {
            cccdAlert.setText("Căn cước công dân không hợp lệ.");
            cccdAlert.setVisible(true);
            isValid = false;
        } else {
            cccdAlert.setVisible(false);
        }

        // Validate salary field
        String salaryValue = salaryField.getValue();
        if (salaryValue == null || salaryValue.isEmpty()) {
            salaryAlert.setText("Mức lương không được bỏ trống.");
            salaryAlert.setVisible(true);
            isValid = false;
        } else {
            salaryAlert.setVisible(false);
        }

        // Validate Level field
        String levelText = levelField.getValue();
        String levelValue = levelField.getEditor().getText();
        String[] VALID_LEVELS = {"Cao đẳng", "Đại học", "Cao học"};
        if (levelText == null || levelValue == null || levelField.getValue().isEmpty()
                || levelField.getEditor().getText().isEmpty()) {
            levelAlert.setText("Trình độ chưa được chọn.");
            levelAlert.setVisible(true);
            isValid = false;
        } else if (!Arrays.asList(VALID_LEVELS).contains(levelText)
                || !Arrays.asList(VALID_LEVELS).contains(levelValue)) {
            levelAlert.setText("Trình độ không hợp lệ.");
            levelAlert.setVisible(true);
            isValid = false;
        } else {
            genderAlert.setVisible(false);
        }

        // Validate Birthday
        if (birthdayField.getValue() == null) {
            birthdayAlert.setText("Ngày sinh không được rỗng.");
            birthdayAlert.setVisible(true);
            isValid = false;
        } else if (birthdayField.getValue().isAfter(LocalDate.now())) {
            birthdayAlert.setText("Ngày sinh không hợp lệ.");
            birthdayAlert.setVisible(true);
            isValid = false;
        } else if (birthdayField.getValue().isAfter(LocalDate.now().minusYears(22))) {
            birthdayAlert.setText("Người dùng phải đủ 22 tuổi.");
            birthdayAlert.setVisible(true);
            isValid = false;
        } else {
            birthdayAlert.setVisible(false);
        }

        // Validate Join Date
        if (joinDateField.getValue() == null) {
            joinDateAlert.setText("Ngày vào làm không được rỗng.");
            joinDateAlert.setVisible(true);
            isValid = false;
        } else {
            joinDateAlert.setVisible(false);
        }

        return isValid;
    }

}
