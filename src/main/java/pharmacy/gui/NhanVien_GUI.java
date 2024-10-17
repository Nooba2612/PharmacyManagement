package pharmacy.gui;

import java.io.IOException;
import java.time.LocalDate;

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
import pharmacy.entity.*;
import pharmacy.utils.NodeUtil;

public class NhanVien_GUI {

	@FXML
	private TableColumn<NhanVien, String> actionColumn;

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
	private Pane searchPane;

	@FXML
	private TableColumn<NhanVien, LocalDate> joinDateColumn;

	@FXML
	private TableColumn<NhanVien, String> statusColumn;

	@FXML
	private TableColumn<NhanVien, LocalDate> birthdayColumn;

	// methods
	@FXML
	public void initialize() {
		filter.getItems().addAll("Tất cả nhân viên", "Nhân viên đã nghỉ", "Nhân viên còn làm");
		handleEmployeeTableAction();
		handleSeacrhEmployeerAction();
	}

	@FXML
	public void handleEmployeeTableAction() {
		addRow();
		handleEditableEmployeeTable();
		setupTablePlaceholder();
		handleAddEmployeeAction();
	}

	@FXML
	public void addRow() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
		joinDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngayVaoLam"));
		levelColumn.setCellValueFactory(new PropertyValueFactory<>("trinhDo"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
		birthdayColumn.setCellValueFactory(new PropertyValueFactory<>("namSinh"));

		handleAddButtonToActionColumn();

		ObservableList<NhanVien> data = FXCollections.observableArrayList(
				new NhanVien("MK0001", "John Doe", "Nhân viên", "0123456789", "ccc@getEmail.com",
						LocalDate.of(2022, 5, 10), "Đã nghỉ", "Đại học",
						"Nam", LocalDate.of(1990, 1, 1)),
				new NhanVien("MK0002", "Jane Smith", "Người quản lý", "0987654321", "ccc@getEmail.com",
						LocalDate.of(2021, 11, 15), "Còn làm", "Đại học",
						"Nữ", LocalDate.of(1992, 2, 2)));

		employeeTable.setItems(data);
	}

	@FXML
	public void handleAddButtonToActionColumn() {
		actionColumn.setCellFactory(column -> new TableCell<NhanVien, String>() {
			private final Button deleteButton = new Button();

			{
				deleteButton.setStyle("-fx-background-color: #D23617; -fx-text-fill: #FFF;");
				deleteButton.setOnAction(event -> {
					NhanVien employee = getTableView().getItems().get(getIndex());
					getTableView().getItems().remove(employee);
				});

				Image image = new Image(getClass().getResourceAsStream("/images/x-icon.png"));
				ImageView imageView = new ImageView(image);

				imageView.setFitWidth(20);
				imageView.setFitHeight(20);
				imageView.setPreserveRatio(true);
				deleteButton.setGraphic(imageView);
				deleteButton.setStyle("-fx-background-color: transparent;");
				deleteButton.setVisible(false);

				deleteButton.setOnMouseEntered(event -> {
					NodeUtil.applyFadeTransition(deleteButton, 1, 0.7, 300, () -> {
					});
					NodeUtil.applyScaleTransition(deleteButton, 1, 1.1, 1, 1.1, 300, () -> {
					});
				});
				deleteButton.setOnMouseExited(event -> {
					NodeUtil.applyFadeTransition(deleteButton, 0.7, 1, 300, () -> {
					});
					NodeUtil.applyScaleTransition(deleteButton, 1.1, 1, 1.1, 1, 300, () -> {
					});
				});
			}

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(deleteButton);
					setStyle("-fx-alignment: CENTER;");
				}
			}

			@Override
			public void updateIndex(int i) {
				super.updateIndex(i);
				if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
					TableRow<NhanVien> currentRow = getTableRow();
					currentRow.setOnMouseEntered(event -> {
						deleteButton.setVisible(true);
						NodeUtil.applyFadeTransition(deleteButton, 0, 1, 300, () -> {
						});
					});
					currentRow.setOnMouseExited(event -> NodeUtil.applyFadeTransition(deleteButton, 1, 0, 300, () -> {
						deleteButton.setVisible(false);
					}));
				}
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
			public String toString(LocalDate date) {
				return date != null ? date.toString() : "";
			}

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
