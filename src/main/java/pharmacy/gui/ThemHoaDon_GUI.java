package pharmacy.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import pharmacy.utils.NodeUtil;

public class ThemHoaDon_GUI {

	@FXML
	private Button addMedicineBtn;

	@FXML
	private Button backBtn;

	@FXML
	private Label createDate;

	@FXML
	private Label customerName;

	@FXML
	private TextField customerPhoneField;

	@FXML
	private ComboBox<?> dosageSelect;

	@FXML
	private Label employeeName;

	@FXML
	private TableColumn<?, ?> expirationDateColumn;

	@FXML
	private Button exportInvoiceBtn;

	@FXML
	private Label invoiceId;

	@FXML
	private Label loyaltyPoint;

	@FXML
	private TableColumn<?, ?> medicineNameColumn;

	@FXML
	private TableView<?> medicineTable;

	@FXML
	private TextArea noteField;

	@FXML
	private TableColumn<?, ?> numberColumn;

	@FXML
	private TableColumn<?, ?> priceColumn;

	@FXML
	private TableColumn<?, ?> quantityColumn;

	@FXML
	private TextField quantityField;

	@FXML
	private HBox root;

	@FXML
	private TextField searchMedicineField;

	@FXML
	private Button submitBtn;

	@FXML
	private Label totalProductQuantity;

	@FXML
	private TableColumn<?, ?> unitColumn;

	@FXML
	private ComboBox<?> unitSelect;

	@FXML
	private TextField usePointField;

	@FXML
	public void initialize() {
		handleAddInvoice();
		handleBackBtnClick();
	}

	@FXML
	public void handleAddInvoice() {
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

	@FXML
	public void handleExportInvoice() {
		exportInvoiceBtn.setOnMouseClicked(event -> {

		});
		exportInvoiceBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransiton(exportInvoiceBtn, 1, 0.7, 200, () -> {
			});
		});
		exportInvoiceBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransiton(exportInvoiceBtn, 0.7, 1, 200, () -> {
			});
		});
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
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/KhachHang_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
