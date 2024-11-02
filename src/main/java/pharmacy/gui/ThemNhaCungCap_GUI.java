package pharmacy.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pharmacy.utils.NodeUtil;

public class ThemNhaCungCap_GUI {

	@FXML
	private TextField addressField;

	@FXML
	private Text addressFieldAlert;

	@FXML
	private Button backBtn;

	@FXML
	private TextField emailField;

	@FXML
	private Text emailFieldAlert;

	@FXML
	private TextField nameField;

	@FXML
	private Text nameFieldAlert;

	@FXML
	private TextField phoneField;

	@FXML
	private Text phoneFieldAlert;

	@FXML
	private HBox root;

	@FXML
	private Button submitBtn;

	// methods
	@FXML
	public void initialize() {
		handleAddCustomer();
		handleBackBtnClick();
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
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/NhaCungCap_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
	
	}
