package pharmacy.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import pharmacy.utils.NodeUtil;

public class ThemNhanVien_GUI {

	@FXML
	private Button backBtn;

	@FXML
	private TextField emailField;

	@FXML
	private Text emailFieldAlert;

	@FXML
	private Text genderFieldAlert;

	@FXML
	private ComboBox<String> genderSelect;

	@FXML
	private DatePicker joinDateField;

	@FXML
	private Text joinDateFieldAlert;

	@FXML
	private ComboBox<String> levelSelect;

	@FXML
	private TextField nameField;

	@FXML
	private Text nameFieldAlert;

	@FXML
	private TextField phoneField;

	@FXML
	private Text phoneFieldAlert;

	@FXML
	private ComboBox<String> positionSelect;

	@FXML
	private Text positionSelectAlert;

	@FXML
	private Pane root;

	@FXML
	private TextField salaryField;

	@FXML
	private Text salaryFieldAlert;

	@FXML
	private Button submitBtn;

	@FXML
	private Text unitSelectAlert;

	@FXML
	private DatePicker birthdayField;

	// methods
	@FXML
	public void initialize() {
		genderSelect.getItems().addAll("Nam", "Nữ", "Khác");
		levelSelect.getItems().addAll("Đại học", "Cao đẳng", "Cao học");
		positionSelect.getItems().addAll("Quản lý", "Nhân viên bán hàng");
		handleBackBtnClick();
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
	public void handleAddEmployee() {
		submitBtn.setOnMouseClicked(event -> {

		});
		submitBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 1, 0.7, 200, () -> {
			});
		});
		submitBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 0.7, 1, 200, () -> {
			});
		});
	}

}
