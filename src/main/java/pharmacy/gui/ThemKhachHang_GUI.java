package pharmacy.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pharmacy.utils.NodeUtil;

public class ThemKhachHang_GUI {
	@FXML
	private HBox root;

	@FXML
	private TextField nameField;

	@FXML
	private TextField phoneField;

	@FXML
	private ComboBox<String> genderSelect;

	@FXML
	private Text nameFieldAlert;

	@FXML
	private Text phoneFieldAlert;

	@FXML
	private Text genderFieldAlert;

	@FXML
	private Button submitBtn;

	@FXML
	Button backBtn;

	// methods
	@FXML
	public void initialize() {
		initGenderSelect();
		handleAddCustomer();
		handleBackBtnClick();
	}

	@FXML
	public void initGenderSelect() {
		genderSelect.getItems().addAll("Nam", "Nữ", "Khác");
	}

	@FXML
	public void handleAddCustomer() {
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
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/KhachHang_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}