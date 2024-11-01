package pharmacy.gui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.io.exceptions.IOException;

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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pharmacy.bus.SanPham_BUS;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;

public class ThemSanPham_GUI {

	@FXML
	private TableColumn<SanPham, Number> availableQuantityColumn;

	@FXML
	private Button backBtn;

	@FXML
	private ComboBox<String> categoryField;

	@FXML
	private Button clearDataBtn;

	@FXML
	private TextField createDateField;

	@FXML
	private TextField desciptionField;

	@FXML
	private TableColumn<SanPham, String> descriptionColumn;

	@FXML
	private ComboBox<String> productTypeField;

	@FXML
	private TableColumn<SanPham, LocalDate> expirationDateColumn;

	@FXML
	private DatePicker expirationDateField;

	@FXML
	private TableColumn<SanPham, String> idColumn;

	@FXML
	private TextField idField;

	@FXML
	private TableColumn<SanPham, LocalDate> manufactureDateColumn;

	@FXML
	private DatePicker manufactureDateField;

	@FXML
	private TableColumn<SanPham, String> manufacturerColumn;

	@FXML
	private TextField manufacturerField;

	@FXML
	private TableView<SanPham> medicineTable;

	@FXML
	private TableColumn<SanPham, String> nameColumn;

	@FXML
	private TextField nameField;

	@FXML
	private TableColumn<SanPham, Number> priceColumn;

	@FXML
	private TextField priceField;

	@FXML
	private TextField quantityField;

	@FXML
	private HBox root;

	@FXML
	private Button submitBtn;

	@FXML
	private TableColumn<SanPham, Number> taxColumn;

	@FXML
	private ComboBox<String> taxField;

	@FXML
	private TableColumn<SanPham, String> unitColumn;

	@FXML
	private ComboBox<String> unitField;

	@FXML
	private Label categoryAlert;

	@FXML
	private Label expirationDateAlert;

	@FXML
	private Label idAlert;

	@FXML
	private Label manufactureDateAlert;

	@FXML
	private Label manufacturerAlert;

	@FXML
	private Label nameAlert;

	@FXML
	private Label priceAlert;

	@FXML
	private Label taxAlert;

	@FXML
	private Label unitAlert;

	@FXML
	private Label quantityAlert;

	private ObservableList<SanPham> addedMedicineList = FXCollections.observableArrayList();

	@FXML
	public void initialize() throws SQLException {
		handleBackBtnClick();
		setUpForm();
		clearForm();
	}

	@FXML
	public void setUpForm() throws SQLException {
		unitField.getItems().addAll("Viên", "Vỉ", "Hộp", "Chai", "Ống", "Gói");
		categoryField.getItems().addAll("Thuốc giảm đau", "Thuốc kháng sinh", "Thuốc kháng viêm", "Thuốc chống dị ứng",
				"Thuốc hạ sốt");
		taxField.getItems().addAll("0%", "5%", "10%", "15%", "20%");

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
			clearForm();
		});

		// handle if table empty
		if (addedMedicineList.isEmpty()) {
			Label noMedicineLabel = new Label("Không có thuốc nào trong bảng.");
			noMedicineLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
			medicineTable.setPlaceholder(noMedicineLabel);
		}

		handleAddMedicine();
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
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/SanPham_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);
			} catch (IOException | java.io.IOException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public void handleAddMedicine() throws SQLException {
		submitBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 1, 0.6, 200, () -> {
			});
		});
		submitBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 0.6, 1, 200, () -> {
			});
		});
		submitBtn.setCursor(Cursor.HAND);

		// add medicine
		submitBtn.setOnAction(event -> {

			String maSanPham = idField.getText().trim();
			String tenSanPham = nameField.getText().trim();
			String moTa = desciptionField.getText().trim();
			String nhaSX = manufacturerField.getText().trim();
			int soLuongTon = Integer
					.parseInt(!quantityField.getText().trim().isEmpty() ? quantityField.getText().trim() : "0");
			double donGiaBan = Double
					.parseDouble(!priceField.getText().trim().isEmpty() ? priceField.getText().trim() : "0");
			float thue = 0;
			if (taxField.getValue() != null) {
				String taxValue = taxField.getValue().replace("%", "").trim();
				thue = Float.parseFloat(taxValue.isEmpty() ? "0" : taxValue) / 100;
			}
			LocalDate ngayTao = LocalDate.now();
			LocalDate ngaySX = manufactureDateField.getValue();
			LocalDate hanSuDung = expirationDateField.getValue();
			String donViTinh = unitField.getValue();
			String trangThai = "Có sẵn";
			String loaiSanPham = productTypeField.getValue();
			String danhMuc = categoryField.getValue();

			if (ngaySX == null) {
				expirationDateAlert.setText("Ngày hết hạn không được rỗng.");
				expirationDateAlert.setVisible(true);
			} else {
				expirationDateAlert.setVisible(false);
			}

			if (hanSuDung == null) {
				manufactureDateAlert.setText("Ngày sản xuất không được rỗng.");
				manufactureDateAlert.setVisible(true);
			} else {
				manufactureDateAlert.setVisible(false);
			}

			SanPham thuoc = new SanPham(maSanPham, tenSanPham, danhMuc, ngaySX, nhaSX, ngayTao,
					LocalDate.now(), soLuongTon, donGiaBan, thue,
					hanSuDung, moTa, donViTinh, trangThai, loaiSanPham);
			try {
				if (validateForm()) {
					new SanPham_BUS().createSanPham(thuoc);
					showAddMedicineSuccessModal("Thêm thuốc thành công");
					clearForm();
					addedMedicineList.add(thuoc);
					handleRenderAddedMedicinesTable();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

	}

	@FXML
	private void showAddMedicineSuccessModal(String message) {
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

		// Validate ID field
		if (idField.getText().trim().isEmpty()) {
			idAlert.setText("Mã thuốc không được rỗng.");
			idAlert.setVisible(true);
			isValid = false;
		} else {
			idAlert.setVisible(false);
		}

		// Validate Name field
		if (nameField.getText().trim().isEmpty()) {
			nameAlert.setText("Tên thuốc không được rỗng.");
			nameAlert.setVisible(true);
			isValid = false;
		} else {
			nameAlert.setVisible(false);
		}

		// Validate Manufacturer field
		if (manufacturerField.getText().trim().isEmpty()) {
			manufacturerAlert.setText("Nhà sản xuất thuốc không được rỗng.");
			manufacturerAlert.setVisible(true);
			isValid = false;
		} else {
			manufacturerAlert.setVisible(false);
		}

		// Validate Quantity field
		String quantityText = quantityField.getText().trim();
		if (quantityText.isEmpty()) {
			quantityField.setText("");
			quantityAlert.setText("Số lượng không được để trống.");
			quantityAlert.setVisible(true);
			isValid = false;
		} else {
			try {
				int quantity = Integer.parseInt(quantityText);
				if (quantity < 0) {
					quantityField.setText("");
					quantityAlert.setText("Số lượng không được là số âm.");
					quantityAlert.setVisible(true);
					isValid = false;
				} else {
					quantityAlert.setVisible(false);
				}
			} catch (NumberFormatException e) {
				quantityField.setText("");
				quantityAlert.setText("Số lượng phải là một số nguyên hợp lệ.");
				quantityAlert.setVisible(true);
				isValid = false;
			}
		}

		// Validate Price field
		String priceText = priceField.getText().trim();
		if (priceText.isEmpty()) {
			priceField.setText("");
			priceAlert.setText("Giá bán không được để trống.");
			priceAlert.setVisible(true);
			isValid = false;
		} else {
			try {
				double price = Double.parseDouble(priceText);
				if (price < 0) {
					priceField.setText("");
					priceAlert.setText("Giá không được là số âm.");
					priceAlert.setVisible(true);
					isValid = false;
				} else if (price % 1 != 0) {
					priceField.setText("");
					priceAlert.setText("Giá không được là số thập phân.");
					priceAlert.setVisible(true);
					isValid = false;
				} else {
					priceAlert.setVisible(false);
				}
			} catch (NumberFormatException e) {
				priceField.setText("");
				priceAlert.setText("Giá phải là một số hợp lệ.");
				priceAlert.setVisible(true);
				isValid = false;
			}
		}

		// Validate Tax field
		String[] VALID_TAXES = { "0%", "5%", "10%", "15%", "20%" };
		String taxValue = (taxField.getValue() != null) ? taxField.getValue().trim()
				: taxField.getEditor().getText().trim();

		if (taxValue.isEmpty()) {
			taxAlert.setText("Thuế chưa được chọn.");
			taxAlert.setVisible(true);
			isValid = false;
		} else if (!Arrays.asList(VALID_TAXES).contains(taxValue)) {
			taxAlert.setText("Thuế không hợp lệ.");
			taxAlert.setVisible(true);
			isValid = false;
		} else {
			taxAlert.setVisible(false);
		}

		// Validate Unit field
		String[] VALID_UNITS = { "Viên", "Vỉ", "Hộp", "Chai", "Ống", "Gói" };
		String unitValue = (unitField.getValue() != null) ? unitField.getValue().trim()
				: unitField.getEditor().getText().trim();

		if (unitValue.isEmpty()) {
			unitAlert.setText("Đơn vị tính chưa được chọn.");
			unitAlert.setVisible(true);
			isValid = false;
		} else if (!Arrays.asList(VALID_UNITS).contains(unitValue)) {
			unitAlert.setText("Đơn vị tính không hợp lệ.");
			unitAlert.setVisible(true);
			isValid = false;
		} else {
			unitAlert.setVisible(false);
		}

		// Validate Unit field
		if (categoryField.getValue() == null || categoryField.getEditor().getText().trim().isEmpty()) {
			categoryAlert.setText("Danh mục chưa được chọn.");
			categoryAlert.setVisible(true);
			isValid = false;
		} else {
			categoryAlert.setVisible(false);
		}

		// Validate Expiration Date
		if (expirationDateField.getValue() == null) {
			expirationDateAlert.setText("Ngày hết hạn không được rỗng.");
			expirationDateAlert.setVisible(true);
			isValid = false;
		} else {
			expirationDateAlert.setVisible(false);
		}

		// Validate Manufacture Date
		if (manufactureDateField.getValue() == null) {
			manufactureDateAlert.setText("Ngày sản xuất không được rỗng.");
			manufactureDateAlert.setVisible(true);
			isValid = false;
		} else {
			manufactureDateAlert.setVisible(false);
		}

		return isValid;
	}

	@FXML
	public void clearForm() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		createDateField.setText(LocalDateTime.now().format(formatter));
		categoryField.setValue("");
		idField.setText("");
		nameField.setText("");
		manufacturerField.setText("");
		priceField.setText("");
		quantityField.setText("");
		expirationDateField.setValue(null);
		manufactureDateField.setValue(null);
		unitField.setValue("");
		taxField.setValue("");
	}

	@FXML
	public void handleRenderAddedMedicinesTable() {
		medicineTable.setItems(addedMedicineList);

		idColumn.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
		manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("nhaSX"));
		availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("donGiaBan"));
		taxColumn.setCellValueFactory(new PropertyValueFactory<>("thue"));
		manufactureDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngaySX"));
		expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("hanSuDung"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("moTa"));
		unitColumn.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));

	}
}