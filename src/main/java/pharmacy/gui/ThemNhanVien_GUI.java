package pharmacy.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Observable;

import com.itextpdf.layout.element.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.entity.DanhMuc;
import pharmacy.entity.NhanVien;
import pharmacy.entity.NhanVien;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.StringUtil;

public class ThemNhanVien_GUI {

	@FXML
	private Button backBtn;

	@FXML
	private Label birthdayAlert;

	@FXML
	private TableColumn<NhanVien, LocalDate> birthdayColumn;

	@FXML
	private DatePicker birthdayField;

	@FXML
	private Button clearDataBtn;

	@FXML
	private Label emailAlert;

	@FXML
	private TableColumn<NhanVien, String> emailColumn;

	@FXML
	private TextField emailField;

	@FXML
	private TableView<NhanVien> employeeTable;

	@FXML
	private Label genderAlert;

	@FXML
	private TableColumn<NhanVien, String> genderColumn;

	@FXML
	private ComboBox<String> genderField;

	@FXML
	private TableColumn<NhanVien, String> idColumn;

	@FXML
	private TextField idField;

	@FXML
	private Label joinDateAlert;

	@FXML
	private TableColumn<NhanVien, LocalDate> joinDateColumn;

	@FXML
	private DatePicker joinDateField;

	@FXML
	private Label levelAlert;

	@FXML
	private TableColumn<NhanVien, String> levelColumn;

	@FXML
	private ComboBox<String> levelField;

	@FXML
	private Label nameAlert;

	@FXML
	private TableColumn<NhanVien, String> nameColumn;

	@FXML
	private TextField nameField;

	@FXML
	private ListView<String> nameSuggestionBox;

	@FXML
	private Label phoneAlert;

	@FXML
	private TableColumn<NhanVien, String> phoneColumn;

	@FXML
	private TextField phoneField;

	@FXML
	private Label positionAlert;

	@FXML
	private TableColumn<NhanVien, String> positionColumn;

	@FXML
	private ComboBox<String> positionField;

	@FXML
	private HBox root;

	@FXML
	private Button submitBtn;

	@FXML
	private Label taxAlert;

	private ObservableList<NhanVien> addedEmployeeList = FXCollections.observableArrayList();

	// methods
	@FXML
	public void initialize() throws SQLException {
		handleBackBtnClick();
		setUpForm();
		clearForm();
	}

	@FXML
	public void setUpForm() throws SQLException {
		genderField.getItems().addAll("Nam", "Nữ", "Khác");
		levelField.getItems().addAll("Đại học", "Cao đẳng", "Cao học");
		positionField.getItems().addAll("Người quản lý", "Nhân viên");

		idField.setText(generateId());

		TextFormatter<String> phoneFormatter = new TextFormatter<>(change -> {
			String newText = change.getControlNewText();
			if (newText.matches("\\d*")) {
				return change;
			}
			return null;
		});

		phoneField.setTextFormatter(phoneFormatter);

		clearDataBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(clearDataBtn, 1, 0.6, 200, () -> {
			});
		});
		clearDataBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(clearDataBtn, 0.6, 1, 200, () -> {
			});
		});
		clearDataBtn.setOnMouseClicked(event -> {
			clearForm();
		});

		// handle if table empty
		if (addedEmployeeList.isEmpty()) {
			Label noEmployeeLabel = new Label("Không có nhân viên nào trong bảng.");
			noEmployeeLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
			employeeTable.setPlaceholder(noEmployeeLabel);
		}

		handleAddEmployee();
	}

	private String generateId() {
		int employeeNumber = new NhanVien_BUS().countEmployees();
		String id = String.format("MK%04d", employeeNumber + 1);

		return id;
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
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/NhanVien_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public void handleAddEmployee() throws SQLException {
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

			String id = idField.getText();
			String name = nameField.getText();
			String phoneNumber = phoneField.getText();
			String email = emailField.getText();
			String position = positionField.getValue();
			String gender = genderField.getValue();
			String level = levelField.getValue();
			LocalDate birthday = birthdayField.getValue();
			LocalDate joinDate = joinDateField.getValue();
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

			NhanVien employee = new NhanVien(id, name, position, phoneNumber, email, joinDate, status, level, gender,
					birthday);

			if (validateForm()) {
				new NhanVien_BUS().createEmployee(employee);
				showAddEmployeeSuccessModal("Thêm nhân viên thành công");
				clearForm();
				addedEmployeeList.add(employee);
				handleRenderAddedEmployeesTable();
			}
		});

	}

	@FXML
	private void showAddEmployeeSuccessModal(String message) {
		Stage modalStage = new Stage();
		modalStage.setResizable(false);
		modalStage.initModality(Modality.APPLICATION_MODAL);
		modalStage.initStyle(StageStyle.UNDECORATED);

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
		String[] VALID_POSTIONS = { "Người quản lý", "Nhân viên" };
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
		String genderText = genderField.getValue();
		String genderValue = genderField.getEditor().getText();
		String[] VALID_GENDERS = { "Nam", "Nữ", "Khác" };
		if (genderField == null || genderValue == null || genderField.getValue().trim().isEmpty()
				|| genderField.getEditor().getText().trim().isEmpty()) {
			genderAlert.setText("Giới tính chưa được chọn.");
			genderAlert.setVisible(true);
			isValid = false;
		} else if (!Arrays.asList(VALID_GENDERS).contains(genderText.trim())
				|| !Arrays.asList(VALID_GENDERS).contains(genderValue.trim())) {
			genderAlert.setText("Giới tính không hợp lệ.");
			genderAlert.setVisible(true);
			isValid = false;
		} else {
			genderAlert.setVisible(false);
		}

		// Validate Level field
		String levelText = levelField.getValue();
		String levelValue = levelField.getEditor().getText();
		String[] VALID_LEVELS = { "Cao đẳng", "Đại học", "Cao học" };
		if (levelText == null || levelValue == null || levelField.getValue().trim().isEmpty()
				|| levelField.getEditor().getText().trim().isEmpty()) {
			levelAlert.setText("Trình độ chưa được chọn.");
			levelAlert.setVisible(true);
			isValid = false;
		} else if (!Arrays.asList(VALID_LEVELS).contains(levelText.trim())
				|| !Arrays.asList(VALID_LEVELS).contains(levelValue.trim())) {
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

	@FXML
	public void clearForm() {
		idField.setText(generateId());
		nameField.setText("");
		phoneField.setText("");
		emailField.setText("");
		positionField.setValue(null);
		genderField.setValue(null);
		joinDateField.setValue(null);
		birthdayField.setValue(null);
		levelField.setValue("");
	}

	@FXML
	public void handleRenderAddedEmployeesTable() {
		employeeTable.setItems(addedEmployeeList);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		positionColumn.setCellValueFactory(new PropertyValueFactory<>("chucVu"));
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
		joinDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngayVaoLam"));
		birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("namSinh"));
		levelColumn.setCellValueFactory(new PropertyValueFactory<>("trinhDo"));

	}

}
