package pharmacy.gui;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pharmacy.entity.NhaCungCap;
import pharmacy.entity.SanPham;
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
	
	@FXML
	private TableView<NhaCungCap> supplierTable;

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
	public void setUpForm() throws SQLException {
		unitField.getItems().addAll("Viên", "Vỉ", "Hộp", "Chai", "Ống", "Gói");
		taxField.getItems().addAll("0%", "5%", "10%", "15%", "20%");
		idField.setText(generateId());

		productTypeField.getItems().addAll("Thuốc", "Thiết bị y tế");
		categoryField.setDisable(true);

		productTypeField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			categoryField.getItems().clear();
			if (newValue != null) {
				if (newValue.equals("Thuốc")) {
					categoryField.setDisable(false);
					categoryField.getItems().addAll("Giảm đau", "Hạ sốt", "Kháng sinh", "Chống viêm", "Vitamin",
							"An thần", "Siro", "Khác");
				} else if (newValue.equals("Thiết bị y tế")) {
					categoryField.getItems().addAll("Dụng cụ y tế", "Sản phẩm bảo vệ cá nhân", "Dung dịch vệ sinh",
							"Khác");
					categoryField.setDisable(false);
				}
			}
		});

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		createDateField.setText(LocalDateTime.now().format(formatter));

		TextFormatter<String> quantityFormatter = new TextFormatter<>(change -> {
			String newText = change.getControlNewText();
			return newText.matches("\\d*") ? change : null;
		});
		quantityField.setTextFormatter(quantityFormatter);

		TextFormatter<String> priceFormatter = new TextFormatter<>(change -> {
			String newText = change.getControlNewText();
			return newText.matches("\\d*") ? change : null;
		});
		priceField.setTextFormatter(priceFormatter);

		clearDataBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(clearDataBtn, 1, 0.6, 200, () -> {
			});
		});
		clearDataBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(clearDataBtn, 0.6, 1, 200, () -> {
			});
		});
		clearDataBtn.setOnMouseClicked(event -> {
			try {
				clearForm();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		// handle if table empty
		if (addedProductList.isEmpty()) {
			Label noProductLabel = new Label("Không có thuốc nào trong bảng.");
			noProductLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
			supplierTable.setPlaceholder(noProductLabel);
		}

		handleAddCustomer();
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
	
	@FXML
	public void clearForm() throws SQLException {
		nameField.setText("");
		addressField.setText("");
		phoneField.setText("");
		emailField.setText("");
	}

}
