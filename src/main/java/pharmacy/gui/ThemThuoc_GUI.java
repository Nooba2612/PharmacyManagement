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
import javafx.scene.text.Text;
import pharmacy.utils.NodeUtil;

public class ThemThuoc_GUI {

	@FXML
	private Button backBtn;

	@FXML
	private TextField descriptionField;

	@FXML
	private Text descriptionFieldAlert;

	@FXML
	private DatePicker expiredDateField;

	@FXML
	private Text expiredDateFieldAlert;

	@FXML
	private DatePicker manufacturerDateField;

	@FXML
	private Text manufacturerDateFieldAlert;

	@FXML
	private TextField manufacturerField;

	@FXML
	private Text manufacturerFieldAlert;

	@FXML
	private TextField nameField;

	@FXML
	private Text nameFieldAlert;

	@FXML
	private TextField priceField;

	@FXML
	private Text priceFieldAlert;

	@FXML
	private TextField quantityField;

	@FXML
	private Text quantityFieldAlert;

	@FXML
	private HBox root;

	@FXML
	private Button submitBtn;

	@FXML
	private TextField taxField;

	@FXML
	private Text taxFieldAlert;

	@FXML
	private ComboBox<String> unitSelect;

	@FXML
	private Text unitSelectAlert;

	@FXML
	public void initialize() {
		unitSelect.getItems().addAll("Viên", "Vỉ", "Hộp", "Chai", "Ống", "Gói");
		handleBackBtnClick();
	}

	@FXML
	public void handleBackBtnClick() {
		backBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransiton(backBtn, 1, 0.5, 200, () -> {
			});
		});

		backBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransiton(backBtn, 0.5, 1, 200, () -> {
			});
		});

		backBtn.setOnMouseClicked(event -> {
			try {
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/Thuoc_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public void handleAddCustomer() {
		submitBtn.setOnMouseClicked(event -> {

		});
		submitBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransiton(submitBtn, 1, 0.7, 200, () -> {
			});
		});
		submitBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransiton(submitBtn, 0.7, 1, 200, () -> {
			});
		});
	}

}
