package pharmacy.gui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.entity.*;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;

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
	private Button exportBtn;

	// methods
	@FXML
	public void initialize() {
		filter.getItems().addAll("Tất cả nhân viên", "Nhân viên đã nghỉ", "Nhân viên còn làm",
				"Nhân viên nghỉ tạm thời");
		filter.getSelectionModel().selectFirst();

		handleEmployeeTableAction();
		handleSeacrhEmployeerAction();
	}

	@FXML
	public void handleEmployeeTableAction() {
		setUpEmployeesTable();
		handleEditableEmployeeTable();
		setupTablePlaceholder();
		handleAddEmployeeAction();
	}

	@FXML
	public void setUpEmployeesTable() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
		joinDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngayVaoLam"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		levelColumn.setCellValueFactory(new PropertyValueFactory<>("trinhDo"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
		birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("namSinh"));

		filter.getSelectionModel().selectedItemProperty().addListener((observation, oldValue, newValue) -> {
			handleRenderEmployeesTable(newValue);
		});

		handleRenderEmployeesTable(filter.getValue());
	}

	@FXML
	public void handleRenderEmployeesTable(String renderType) {
		List<NhanVien> employeeList = new ArrayList<>();

	
		if (renderType.equals("Tất cả nhân viên")) {
			employeeList = new NhanVien_BUS().getAllEmployees();
			employeeTable.setItems(FXCollections.observableArrayList(employeeList));
		} else {
			employeeList = new NhanVien_BUS().getEmployeesByStatus(renderType);
			employeeTable.setItems(FXCollections.observableArrayList(employeeList));
		}
	}

	@FXML
	public void handleExportEmplyeeList() {
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
		// Name column
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit(event -> {
			NhanVien employee = event.getRowValue();
			employee.setHoTen(event.getNewValue());
		});

		// Gender column
		genderColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		genderColumn.setOnEditCommit(event -> {
			NhanVien employee = event.getRowValue();
			employee.setGioiTinh(event.getNewValue());
		});

		// Phone column
		phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		phoneColumn.setOnEditCommit(event -> {
			NhanVien employee = event.getRowValue();
			employee.setSoDienThoai(event.getNewValue());
		});

		// Level column
		levelColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		levelColumn.setOnEditCommit(event -> {
			NhanVien employee = event.getRowValue();
			employee.setChucVu(event.getNewValue());
		});

		// Birthday column
		birthdayColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
			@Override
			public String toString(LocalDate date) {
				return date != null ? date.toString() : "";
			}

			@Override
			public LocalDate fromString(String string) {
				return string != null && !string.isEmpty() ? LocalDate.parse(string) : null;
			}
		}));

		birthdayColumn.setOnEditCommit(event -> {
			NhanVien employee = event.getRowValue();
			employee.setNamSinh(event.getNewValue());
		});
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
	public void handleSeacrhEmployeerAction() {

	}

}
