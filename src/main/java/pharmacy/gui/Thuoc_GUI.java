package pharmacy.gui;

import java.beans.PropertyDescriptor;
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

public class Thuoc_GUI {

	@FXML
	private Button addMedicineBtn;

	@FXML
	private TableColumn<Thuoc, Integer> quantityColumn;

	@FXML
	private TableColumn<Thuoc, Integer> availableQuantityColumn;

	@FXML
	private TableColumn<Thuoc, String> descriptionColumn;

	@FXML
	private TableColumn<Thuoc, LocalDate> expirationDateColumn;

	@FXML
	private TableColumn<Thuoc, String> manufacturerColumn;

	@FXML
	private TableColumn<Thuoc, LocalDate> manufactureDateColumn;
	
	@FXML
	private TableColumn<Thuoc, LocalDate> idColumn;

	@FXML
	private TableView<Thuoc> medicineTable;

	@FXML
	private TableColumn<Thuoc, String> nameColumn;

	@FXML
	private TableColumn<Thuoc, Double> priceColumn;

	@FXML
	private HBox root;

	@FXML
	private TextField searchField;

	@FXML
	private Button searchMedicineBtn;

	@FXML
	private Pane searchPane;

	@FXML
	private TableColumn<Thuoc, Double> taxColumn;

	@FXML
	private TableColumn<Thuoc, Void> actionColumn;

	@FXML
	private ComboBox<String> filter;

	// methods
	@FXML
	public void initialize() {
		filter.getItems().setAll("Tất cả thuốc", "Thuốc sắp hết hạn", "Thuốc đã hết hạn",
				"Thuốc có số lượng tồn kho thấp");
		handleCustomerTableAction();
		handleSeacrhCustomerAction();
	}

	@FXML
	public void handleCustomerTableAction() {
		addRow();
		handleEditableMedicineTable();
		setupTablePlaceholder();
		handleAddCustomerAction();
		handleAddDeleteButtonToActionColumn();
	}

	@FXML
	public void setupTablePlaceholder() {
		Label noContentLabel = new Label("Không có thuốc.");
		noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");

		medicineTable.setPlaceholder(noContentLabel);
	}

	@FXML
	public void handleAddCustomerAction() {
		addMedicineBtn.setOnMouseClicked(event -> {
			try {
				Parent addCustomerFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemThuoc_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(addCustomerFrame);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		addMedicineBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransiton(addMedicineBtn, 1, 0.7, 200, () -> {
			});
		});

		addMedicineBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransiton(addMedicineBtn, 0.7, 1, 200, () -> {
			});
		});
	}

	@FXML
	public void handleAddDeleteButtonToActionColumn() {
		actionColumn.setCellFactory(column -> {
			return new TableCell<Thuoc, Void>() {
				private final Button deleteButton = new Button();

				{
					// Style the delete button
					deleteButton.setStyle("-fx-background-color: #D23617; -fx-text-fill: #FFF;");

					// Action for the delete button
					deleteButton.setOnAction(event -> {
						Thuoc medicine = getTableView().getItems().get(getIndex());
						getTableView().getItems().remove(medicine);
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
						NodeUtil.applyFadeTransiton(deleteButton, 1, 0.7, 300, () -> {
						});
						NodeUtil.applyScaleTransition(deleteButton, 1, 1.1, 1, 1.1, 300, () -> {
						});
					});
					deleteButton.setOnMouseExited(event -> {
						NodeUtil.applyFadeTransiton(deleteButton, 0.7, 1, 300, () -> {
						});
						NodeUtil.applyScaleTransition(deleteButton, 1.1, 1, 1.1, 1, 300, () -> {
						});
					});
				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(deleteButton);
						// Center the delete button in the cell
						setStyle("-fx-alignment: CENTER;");
					}
				}

				@Override
				public void updateIndex(int i) {
					super.updateIndex(i);
					if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
						TableRow<Thuoc> currentRow = getTableRow();
						currentRow.setOnMouseEntered(event -> {
							deleteButton.setVisible(true);
							NodeUtil.applyFadeTransiton(deleteButton, 0, 1, 300, () -> {
							});
						});
						currentRow
								.setOnMouseExited(event -> NodeUtil.applyFadeTransiton(deleteButton, 1, 0, 300, () -> {
									deleteButton.setVisible(false);
								}));
					}
				}
			};
		});
	}

	@FXML
	public void addRow() {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
		manufactureDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngaySX"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("moTa"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("donGiaBan"));
		availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));
		manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("nhaSX"));
		expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("hanSuDung"));
		taxColumn.setCellValueFactory(new PropertyValueFactory<>("thue"));

		ObservableList<Thuoc> data = FXCollections.observableArrayList(
				new Thuoc("ID123", "Thuốc A", null, LocalDate.now(), "Nhà Sản Xuất A", LocalDate.now(), LocalDate.now(),
						100, 50000, 0.1f, LocalDate.of(2025, 12, 31), "Moo tar cc"),
				new Thuoc("ID124", "Thuốc B", null, LocalDate.now(), "Nhà Sản Xuất B", LocalDate.now(), LocalDate.now(),
						50, 60000, 0.1f, LocalDate.of(2026, 1, 15), "Moo tar cc"));

		medicineTable.setItems(data);
	}

	@FXML
	public void handleEditableMedicineTable() {
		makeColumnEditable(idColumn, "maThuoc");
		makeColumnEditable(nameColumn, "tenThuoc");
		makeColumnEditable(descriptionColumn, "moTa");
		makeColumnEditable(priceColumn, "donGiaBan");
		makeColumnEditable(availableQuantityColumn, "soLuongTon");
		makeColumnEditable(manufacturerColumn, "nhaSX");
		makeColumnEditable(expirationDateColumn, "ngaySanXuat");
		makeColumnEditable(taxColumn, "thue");
		makeColumnEditable(expirationDateColumn, "hanSuDung");
	}

	private <T> void makeColumnEditable(TableColumn<Thuoc, T> column, String property) {
		column.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<T>() {
			@Override
			public String toString(T object) {
				return object == null ? "" : object.toString();
			}

			@Override
			@SuppressWarnings("unchecked")
			public T fromString(String string) {
				return (T) string;
			}
		}));

		column.setOnEditCommit(event -> {
			Thuoc medicine = event.getRowValue();
			try {
				PropertyDescriptor pd = new PropertyDescriptor(property, Thuoc.class);
				pd.getWriteMethod().invoke(medicine, event.getNewValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public void handleSeacrhCustomerAction() {

	}
}
