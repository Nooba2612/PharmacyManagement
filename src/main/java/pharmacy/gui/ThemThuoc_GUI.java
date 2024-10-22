package pharmacy.gui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import pharmacy.bus.Thuoc_BUS;
import pharmacy.entity.DanhMuc;
import pharmacy.entity.Thuoc;
import pharmacy.utils.NodeUtil;

public class ThemThuoc_GUI {

	@FXML
	private TableColumn<Thuoc, Number> availableQuantityColumn;

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
	private TableColumn<Thuoc, String> descriptionColumn;

	@FXML
	private TableColumn<Thuoc, LocalDate> expirationDateColumn;

	@FXML
	private DatePicker expirationDateField;

	@FXML
	private TableColumn<Thuoc, String> idColumn;

	@FXML
	private TextField idField;

	@FXML
	private TableColumn<Thuoc, LocalDate> manufactureDateColumn;

	@FXML
	private DatePicker manufactureDateField;

	@FXML
	private TableColumn<Thuoc, String> manufacturerColumn;

	@FXML
	private TextField manufacturerField;

	@FXML
	private TableView<Thuoc> medicineTable;

	@FXML
	private TableColumn<Thuoc, String> nameColumn;

	@FXML
	private TextField nameField;

	@FXML
	private TableColumn<Thuoc, Number> priceColumn;

	@FXML
	private TextField priceField;

	@FXML
	private TextField quantityField;

	@FXML
	private HBox root;

	@FXML
	private Button submitBtn;

	@FXML
	private TableColumn<Thuoc, Number> taxColumn;

	@FXML
	private ComboBox<String> taxField;

	@FXML
	private TableColumn<Thuoc, String> unitColumn;

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

	@FXML
	private ListView<String> taxSuggestionBox;

	@FXML
	private ListView<String> categorySuggestionBox;

	@FXML
	private ListView<String> unitSuggestionBox;

	private ObservableList<Thuoc> addedMedicineList = FXCollections.observableArrayList();

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

		quantityField.setText("0");
		priceField.setText("0");
		taxField.getSelectionModel().selectFirst();

		setupAutoCompleteComboBox(unitField);
		setupAutoCompleteComboBox(categoryField);
		setupAutoCompleteComboBox(taxField);

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

		TextFormatter<String> taxFormatter = new TextFormatter<>(change -> {
			String newText = change.getControlNewText();
			return newText.matches("\\d*") ? change : null;
		});
		taxField.getEditor().setTextFormatter(taxFormatter);

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

	private void setupAutoCompleteComboBox(ComboBox<String> comboBox) {
		comboBox.setEditable(true);
		ObservableList<String> originalItems = FXCollections.observableArrayList(comboBox.getItems());

		comboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
			if (newText == null || newText.trim().isEmpty()) {
				if (comboBox == unitField) {
					hideSuggestionBox(unitSuggestionBox);
				} else if (comboBox == taxField) {
					hideSuggestionBox(taxSuggestionBox);
				} else if (comboBox == categoryField) {
					hideSuggestionBox(categorySuggestionBox);
				}
			} else {
				List<String> filteredItems = originalItems.stream()
						.filter(item -> item.toLowerCase().contains(newText.toLowerCase()))
						.collect(Collectors.toList());
				ObservableList<String> filteredItemList = FXCollections.observableArrayList(filteredItems);

				System.out.println("Text: " + comboBox.getEditor().getText());

				if (!filteredItemList.isEmpty()) {
					if (comboBox == unitField) {
						showSuggestionBox(unitSuggestionBox, filteredItemList);
					} else if (comboBox == taxField) {
						showSuggestionBox(taxSuggestionBox, filteredItemList);
					} else if (comboBox == categoryField) {
						showSuggestionBox(categorySuggestionBox, filteredItemList);
					}
				} else if (filteredItemList.isEmpty()) {
					if (comboBox == unitField) {
						hideSuggestionBox(unitSuggestionBox);
					} else if (comboBox == taxField) {
						hideSuggestionBox(taxSuggestionBox);
					} else if (comboBox == categoryField) {
						hideSuggestionBox(categorySuggestionBox);
					}
				}
			}
		});

		comboBox.setOnAction(e -> {
			String selectedItem = comboBox.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				comboBox.getEditor().setText(selectedItem);
			}
		});
	}

	private void showSuggestionBox(ListView<String> suggestionBox, ObservableList<String> items) {
		suggestionBox.setItems(items);
		suggestionBox.setVisible(true);
		NodeUtil.applyFadeTransition(suggestionBox, 0, 1, 300, () -> {
		});
		NodeUtil.applyTranslateYTransition(suggestionBox, 0, 1.1, 300, () -> {
		});
	}

	private void hideSuggestionBox(ListView<String> suggestionBox) {
		NodeUtil.applyFadeTransition(suggestionBox, 1, 0, 300, () -> suggestionBox.setVisible(false));
		NodeUtil.applyTranslateYTransition(suggestionBox, 1.1, 0, 300, () -> {
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
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/Thuoc_GUI.fxml"));
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

			String maThuoc = idField.getText();
			String tenThuoc = nameField.getText();
			String moTa = desciptionField.getText();
			String nhaSX = manufacturerField.getText();
			int soLuongTon = Integer.parseInt(!quantityField.getText().isEmpty() ? quantityField.getText() : "0");
			double donGiaBan = Double.parseDouble(!priceField.getText().isEmpty() ? priceField.getText() : "0");
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

			// lack of handle danhMuc alert
			DanhMuc danhMuc = new DanhMuc("DM0001", trangThai, null, trangThai, soLuongTon);

			Thuoc thuoc = new Thuoc(maThuoc, tenThuoc, danhMuc, ngaySX, nhaSX, ngayTao,
					LocalDate.now(), soLuongTon, donGiaBan, thue,
					hanSuDung, moTa, donViTinh, trangThai);
			try {
				if (validateForm()) {
					new Thuoc_BUS().createThuoc(thuoc);
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
		if (idField.getText().isEmpty()) {
			idAlert.setText("Mã thuốc không được rỗng.");
			idAlert.setVisible(true);
			isValid = false;
		} else {
			idAlert.setVisible(false);
		}

		// Validate Name field
		if (nameField.getText().isEmpty()) {
			nameAlert.setText("Tên thuốc không được rỗng.");
			nameAlert.setVisible(true);
			isValid = false;
		} else {
			nameAlert.setVisible(false);
		}

		// Validate Manufacturer field
		if (manufacturerField.getText().isEmpty()) {
			manufacturerAlert.setText("Nhà sản xuất thuốc không được rỗng.");
			manufacturerAlert.setVisible(true);
			isValid = false;
		} else {
			manufacturerAlert.setVisible(false);
		}

		// Validate Quantity field
		String quantityText = quantityField.getText();
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
		String priceText = priceField.getText();
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
		if (taxField.getValue() == null || taxField.getEditor().getText().isEmpty()) {
			taxAlert.setText("Thuế chưa được chọn.");
			taxAlert.setVisible(true);
			isValid = false;
		} else {
			taxAlert.setVisible(false);
		}

		// Validate Unit field
		if (unitField.getValue() == null || unitField.getEditor().getText().isEmpty()) {
			unitAlert.setText("Đơn vị tính chưa được chọn.");
			unitAlert.setVisible(true);
			isValid = false;
		} else {
			unitAlert.setVisible(false);
		}

		// Validate Unit field
		if (categoryField.getValue() == null || categoryField.getEditor().getText().isEmpty()) {
			categoryAlert.setText("Đơn vị tính chưa được chọn.");
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

		idColumn.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
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