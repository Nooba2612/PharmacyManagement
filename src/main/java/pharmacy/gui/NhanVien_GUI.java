package pharmacy.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.Optional;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;

import javafx.scene.image.Image;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.entity.NhanVien;
import pharmacy.entity.NhanVien;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;

import java.net.URL;
import java.nio.file.Path;
import java.util.logging.Logger;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.borders.Border;

public class NhanVien_GUI {

	@FXML
	private Button addEmployeeBtn;

	@FXML
	private TableView<NhanVien> employeeTable;

	@FXML
	private ComboBox<String> filter;

	@FXML
	private TableColumn<NhanVien, String> genderColumn;

	@FXML
	private TableColumn<NhanVien, String> levelColumn;

	@FXML
	private TableColumn<NhanVien, String> nameColumn;

	@FXML
	private TableColumn<NhanVien, String> phoneColumn;

	@FXML
	private HBox root;

	@FXML
	private Button searchBtn;

	@FXML
	private TextField searchField;

	@FXML
	private TableColumn<NhanVien, LocalDate> joinDateColumn;

	@FXML
	private TableColumn<NhanVien, String> statusColumn;

	@FXML
	private TableColumn<NhanVien, String> emailColumn;

	@FXML
	private TableColumn<NhanVien, LocalDate> birthdayColumn;

	@FXML
	private TableColumn<NhanVien, String> idColumn;

	@FXML
	private TableColumn<NhanVien, String> positionColumn;

	@FXML
	private Button exportBtn;

	private ObservableList<NhanVien> employeeList = FXCollections.observableArrayList();

	// methods
	@FXML
	public void initialize() {
		handleEmployeeTableAction();
		handleSearchEmployeeAction(employeeList);

	}

	@FXML
	public void handleEmployeeTableAction() {
		filter.getItems().addAll("Tất cả nhân viên", "Nhân viên đã nghỉ", "Nhân viên còn làm",
				"Nhân viên nghỉ tạm thời");
		filter.getSelectionModel().selectFirst();

		setUpEmployeesTable();
		handleExportButtonEmployeeAction();
		handleEditableEmployeeTable();
		setupTablePlaceholder();
		handleAddEmployeeAction();
	}

	@FXML
	public void setUpEmployeesTable() {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
		joinDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngayVaoLam"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		levelColumn.setCellValueFactory(new PropertyValueFactory<>("trinhDo"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
		birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("namSinh"));
		positionColumn.setCellValueFactory(new PropertyValueFactory<>("chucVu"));

		handleRenderEmployeesTable(filter.getValue());

		filter.getSelectionModel().selectedItemProperty().addListener((observation, oldValue, newValue) -> {
			handleRenderEmployeesTable(newValue);
		});

	}

	@FXML
	public void handleRenderEmployeesTable(String renderType) {

		if (renderType.equals("Tất cả nhân viên")) {
			employeeList = FXCollections.observableArrayList(new NhanVien_BUS().getAllEmployees());
			employeeTable.setItems(FXCollections.observableArrayList(employeeList));
		} else {
			employeeList = FXCollections.observableArrayList(new NhanVien_BUS().getEmployeesByStatus(renderType));
			employeeTable.setItems(FXCollections.observableArrayList(employeeList));
		}
	}

	@FXML
	public void handleExportButtonEmployeeAction() {
		exportBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(exportBtn, 1, 0.7, 200, () -> {
			});
		});

		exportBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(exportBtn, 0.7, 1, 200, () -> {
			});
		});

		exportBtn.setOnAction(event -> {
			try {
				exportEmployeesToPdf("src/main/resources/pdf/DanhSachNhanVien.pdf");
				PDFUtil.showPdfPreview(
						new File(getClass().getClassLoader().getResource("pdf/DanhSachNhanVien.pdf").toURI()));
			} catch (com.itextpdf.io.exceptions.IOException | IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public void setupTablePlaceholder() {
		Label noContentLabel = new Label("Không có nhân viên.");
		noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
		employeeTable.setPlaceholder(noContentLabel);
	}

	@FXML
	public void handleEditableEmployeeTable() {
		makeColumnEditable(nameColumn, "hoTen");
		makeColumnEditable(genderColumn, "gioiTinh");
		makeColumnEditable(phoneColumn, "soDienThoai");
		makeColumnEditable(levelColumn, "trinhDo");
		makeColumnEditable(emailColumn, "email");
		makeColumnEditable(statusColumn, "trangThai");
		makeColumnEditable(positionColumn, "chucVu");
		setDateColumnEditable(birthdayColumn, "namSinh");
		setDateColumnEditable(joinDateColumn, "ngayVaoLam");
	}

	private void setDateColumnEditable(TableColumn<NhanVien, LocalDate> column, String property) {
		column.setCellFactory(col -> new TableCell<NhanVien, LocalDate>() {
			private final DatePicker datePicker = new DatePicker();
			{
				datePicker.setEditable(true);
				datePicker.setOnAction(event -> commitEdit(datePicker.getValue()));
				datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
					if (!isNowFocused && isEditing()) {
						cancelEdit();
					}
				});
			}

			@Override
			protected void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					if (isEditing()) {
						datePicker.setValue(getItem());
						setGraphic(datePicker);
						setText(null);
					} else {
						setText(item == null ? "" : item.toString());
						setGraphic(null);
					}
				}
			}

			@Override
			public void startEdit() {
				super.startEdit();
				datePicker.setValue(getItem());
				setGraphic(datePicker);
				setText(null);
				datePicker.requestFocus();
			}

			@Override
			public void cancelEdit() {
				super.cancelEdit();
				setText(getItem() == null ? "" : getItem().toString());
				setGraphic(null);
			}

		});

		column.setOnEditCommit(event -> {
			NhanVien employee = event.getRowValue();
			Object newValue = event.getNewValue();

			if (newValue != null) {
				showChangeTableConfirmationPopup(employee, newValue, event, property);
			} else {
				showInvalidInputDataDialog();
			}
		});

	}

	private <T> void makeColumnEditable(TableColumn<NhanVien, T> column, String property) {
		column.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<T>() {
			@Override
			public String toString(T object) {
				return object == null ? "" : object.toString();
			}

			@Override
			@SuppressWarnings("unchecked")
			public T fromString(String string) {
				try {
					if (column.getCellData(0) instanceof Double) {
						return (T) Double.valueOf(string);
					} else if (column.getCellData(0) instanceof Integer) {
						return (T) Integer.valueOf(string);
					}
					return (T) string;
				} catch (Exception e) {
					return null;
				}
			}
		}));

		column.setOnEditCommit(event -> {
			NhanVien employee = event.getRowValue();
			Object newValue = event.getNewValue();

			if (newValue != null) {
				showChangeTableConfirmationPopup(employee, newValue, event, property);
			} else {
				showInvalidInputDataDialog();
			}
		});
	}

	@FXML
	private void showInvalidInputDataDialog() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Không thể nhập dữ liệu");
		alert.setHeaderText("Giá trị nhập không phù hợp.");

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		ImageView icon = new ImageView(
				new javafx.scene.image.Image(getClass().getResourceAsStream("/images/alert-icon.png")));
		icon.setFitHeight(48);
		icon.setFitWidth(48);
		alert.setGraphic(icon);

		alert.getButtonTypes().clear();

		ButtonType confirmButton = new ButtonType("Xác nhận");
		alert.getButtonTypes().add(confirmButton);

		Node confirmBtn = alert.getDialogPane().lookupButton(confirmButton);
		confirmBtn.setStyle(
				"-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-cursor: hand;");

		stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/alert-icon.png")));

		alert.showAndWait();
	}

	@FXML
	public <T> void showChangeTableConfirmationPopup(NhanVien employee, Object newValue,
			TableColumn.CellEditEvent<NhanVien, T> event, String property) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Xác nhận thay đổi");
		alert.setHeaderText("Thay đổi thông tin nhân viên " + employee.getHoTen());
		alert.setContentText("Mã nhân viên: " + employee.getMaNhanVien());

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/images/tick-icon.png")));
		stage.initStyle(StageStyle.UNDECORATED);

		ImageView icon = new ImageView(
				new javafx.scene.image.Image(getClass().getResourceAsStream("/images/confirmation-icon.png")));
		icon.setFitHeight(48);
		icon.setFitWidth(48);
		alert.setGraphic(icon);

		ButtonType confirmButton = new ButtonType("Xác nhận", ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Hủy", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(confirmButton, cancelButton);

		Node confirmBtn = alert.getDialogPane().lookupButton(confirmButton);
		Node cancelBtn = alert.getDialogPane().lookupButton(cancelButton);

		confirmBtn.setStyle(
				"-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-cursor: hand;");
		cancelBtn.setStyle(
				"-fx-background-color: red; -fx-font-size: 16px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-cursor: hand;");

		confirmBtn.setOnMouseEntered(e -> {
			NodeUtil.applyFadeTransition(confirmBtn, 1, 0.6, 300, () -> {
			});
		});

		confirmBtn.setOnMouseExited(e -> {
			NodeUtil.applyFadeTransition(confirmBtn, 0.6, 1, 300, () -> {
			});
		});

		cancelBtn.setOnMouseEntered(e -> {
			NodeUtil.applyFadeTransition(cancelBtn, 1, 0.6, 300, () -> {
			});
		});

		cancelBtn.setOnMouseExited(e -> {
			NodeUtil.applyFadeTransition(cancelBtn, 0.6, 1, 300, () -> {
			});
		});

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == confirmButton) {
			try {
				PropertyDescriptor pd = new PropertyDescriptor(property, NhanVien.class);
				pd.getWriteMethod().invoke(employee, newValue);

				if (new NhanVien_BUS().updateEmployee(employee)) {
					System.out.println("Input successfully.");
				} else {
					showInvalidInputDataDialog();
				}

				if (event != null) {
					event.consume();
				}
			} catch (Exception ex) {
				Logger.getLogger(NhanVien_GUI.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			if (event != null) {
				event.consume();
			}
		}
	}

	@FXML
	public void handleAddEmployeeAction() {
		addEmployeeBtn.setOnMouseClicked(event -> {
			try {
				Parent addEmployeeFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemNhanVien_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(addEmployeeFrame);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		addEmployeeBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(addEmployeeBtn, 1, 0.7, 200, () -> {
			});
		});

		addEmployeeBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(addEmployeeBtn, 0.7, 1, 200, () -> {
			});
		});
	}

	@FXML
	public void handleSearchEmployeeAction(ObservableList<NhanVien> employeeList) {
		FilteredList<NhanVien> filteredList = new FilteredList<>(employeeList, b -> true);

		searchBtn.setOnAction(event -> {
			filteredList.setPredicate(employee -> {
				if (searchField.getText() == null || searchField.getText().isEmpty()) {
					return true;
				}

				String lowerCaseFilter = searchField.getText().toLowerCase();
				return (employee.getMaNhanVien().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getMaNhanVien().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getHoTen().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getSoDienThoai().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getChucVu().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getTrangThai().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getTrinhDo().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getGioiTinh().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getEmail().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getNgayVaoLam() != null
								&& employee.getNgayVaoLam().toString().contains(lowerCaseFilter))
						||
						(employee.getNamSinh() != null && employee.getNamSinh().toString().contains(lowerCaseFilter));

			});
		});

		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(employee -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();

				return (employee.getMaNhanVien().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getMaNhanVien().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getHoTen().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getSoDienThoai().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getChucVu().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getTrangThai().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getTrinhDo().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getGioiTinh().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getEmail().toLowerCase().contains(lowerCaseFilter)) ||
						(employee.getNgayVaoLam() != null
								&& employee.getNgayVaoLam().toString().contains(lowerCaseFilter))
						||
						(employee.getNamSinh() != null && employee.getNamSinh().toString().contains(lowerCaseFilter));

			});
		});

		employeeTable.setItems(filteredList.isEmpty() ? employeeList : filteredList);
	}

	private void exportEmployeesToPdf(String outputPdfPath) {
		try (PdfWriter writer = new PdfWriter(outputPdfPath);
				PdfDocument pdfDoc = new PdfDocument(writer);
				Document document = new Document(pdfDoc)) {

			// Load font
			URL fontUrl = getClass().getClassLoader().getResource("fonts/Roboto/Roboto-Regular.ttf");
			Path fontPath = Path.of(fontUrl.toURI());
			PdfFont font = PdfFontFactory.createFont(Files.readAllBytes(fontPath), PdfEncodings.IDENTITY_H,
					EmbeddingStrategy.PREFER_EMBEDDED);

			// Define colors
			String primaryColorHex = "#339933";
			DeviceRgb primaryColor = new DeviceRgb(
					Integer.parseInt(primaryColorHex.substring(1, 3)),
					Integer.parseInt(primaryColorHex.substring(3, 5)),
					Integer.parseInt(primaryColorHex.substring(5, 7)));

			// Add header
			Table headerTable = new Table(2);
			headerTable.setWidth(UnitValue.createPercentValue(40))
					.setHorizontalAlignment(HorizontalAlignment.CENTER);

			com.itextpdf.layout.element.Image logo = new com.itextpdf.layout.element.Image(
					ImageDataFactory.create(getClass().getClassLoader().getResource("images/pharmacy-icon.png")));
			logo.scaleToFit(80, 80);
			Cell logoCell = new Cell().add(logo).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
			headerTable.addCell(logoCell);

			Paragraph name = new Paragraph("MEDKIT")
					.setFont(font)
					.setFontSize(30)
					.setBold()
					.setFontColor(primaryColor)
					.setTextAlignment(TextAlignment.CENTER);
			Cell nameCell = new Cell().add(name).setVerticalAlignment(VerticalAlignment.MIDDLE)
					.setBorder(Border.NO_BORDER);
			headerTable.addCell(nameCell);
			document.add(headerTable.setMarginBottom(10));

			document.add(new Paragraph("DANH SÁCH NHÂN VIÊN").setFont(font)
					.setFontSize(20)
					.setBold()
					.setTextAlignment(TextAlignment.CENTER)
					.setMarginTop(20)
					.setMarginBottom(20));

			// Add table
			Table table = new Table(new float[] { 1, 2, 2, 2, 2, 2, 2, 2 });
			table.setWidth(UnitValue.createPercentValue(100));

			table.addHeaderCell(new Cell().add(new Paragraph("Mã nhân viên").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY));
			table.addHeaderCell(new Cell().add(new Paragraph("Họ tên").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY));
			table.addHeaderCell(new Cell().add(new Paragraph("Giới tính").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY));
			table.addHeaderCell(new Cell().add(new Paragraph("Ngày sinh").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY));
			table.addHeaderCell(new Cell().add(new Paragraph("Trình độ").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY));
			table.addHeaderCell(new Cell().add(new Paragraph("Chức vụ").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY));
			table.addHeaderCell(new Cell().add(new Paragraph("Số điện thoại").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY));
			table.addHeaderCell(new Cell().add(new Paragraph("Email").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY));

			for (NhanVien employee : employeeList) {
				table.addCell(new Paragraph(employee.getMaNhanVien() != null ? employee.getMaNhanVien() : "")
						.setFont(font).setFontSize(8));
				table.addCell(new Paragraph(employee.getHoTen() != null ? employee.getHoTen() : "").setFont(font)
						.setFontSize(8));
				table.addCell(new Paragraph(employee.getGioiTinh() != null ? employee.getGioiTinh() : "").setFont(font)
						.setFontSize(8));
				table.addCell(new Paragraph(employee.getNamSinh() != null ? employee.getNamSinh().toString() : "")
						.setFont(font).setFontSize(8));
				table.addCell(new Paragraph(employee.getTrinhDo() != null ? employee.getTrinhDo() : "").setFont(font)
						.setFontSize(8));
				table.addCell(new Paragraph(employee.getChucVu() != null ? employee.getChucVu() : "").setFont(font)
						.setFontSize(8));
				table.addCell(new Paragraph(employee.getSoDienThoai() != null ? employee.getSoDienThoai() : "")
						.setFont(font).setFontSize(8));
				table.addCell(new Paragraph(employee.getEmail() != null ? employee.getEmail() : "").setFont(font)
						.setFontSize(8));
			}

			document.add(table);
			System.out.println("PDF generated.");
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

}
