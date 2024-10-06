package pharmacy.gui;

import java.io.IOException;

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
import pharmacy.entity.*;
import pharmacy.utils.NodeUtil;

public class NhaCungCap_GUI {

	@FXML
	private TableColumn<NhaCungCap, Void> actionColumn;

	@FXML
	private Button addSupplierBtn;

	@FXML
	private TableColumn<NhaCungCap, String> idColumn;
	
	@FXML
	private TableColumn<NhaCungCap, String> addressColumn;

	@FXML
	private TableColumn<NhaCungCap, String> emailColumn;

	@FXML
	private TableColumn<NhaCungCap, String> nameColumn;

	@FXML
	private TableColumn<NhaCungCap, String> phoneColumn;

	@FXML
	private HBox root;

	@FXML
	private Button searchBtn;

	@FXML
	private TextField searchField;

	@FXML
	private Pane searchPane;

	@FXML
	private TableView<NhaCungCap> supplierTable;

	@FXML
	private TableColumn<NhaCungCap, String> taxNumberColumn;

	// methods
	@FXML
	public void initialize() {
		handleSupplierTableAction();
		handleSearchSupplierAction();
	}

	@FXML
	public void handleSupplierTableAction() {
		addRow();
		handleEditableSupplierTable();
		setupTablePlaceholder();
		handleAddSupplierAction();
	}

	@FXML
	public void addRow() {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("maNCC"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenNCC"));
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

		handleAddButtonToActionColumn();

		ObservableList<NhaCungCap> data = FXCollections.observableArrayList(
				new NhaCungCap("NCC0001", "Supplier A", "0123456789", "123 Street", "supplierA@gmail.com"),
				new NhaCungCap("NCC0002", "Supplier B", "0987654321", "456 Street", "supplierB@gmail.com"));

		supplierTable.setItems(data);
	}

	@FXML
	public void handleAddButtonToActionColumn() {
		actionColumn.setCellFactory(column -> {
			return new TableCell<NhaCungCap, Void>() {
				private final Button deleteButton = new Button();

				{
					// Style the delete button
					deleteButton.setStyle("-fx-background-color: #D23617; -fx-text-fill: #FFF;");

					// Action for the delete button
					deleteButton.setOnAction(event -> {
						NhaCungCap supplier = getTableView().getItems().get(getIndex());
						getTableView().getItems().remove(supplier);
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
						TableRow<NhaCungCap> currentRow = getTableRow();
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
	public void setupTablePlaceholder() {
		Label noContentLabel = new Label("Không có nhà cung cấp.");
		noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
		supplierTable.setPlaceholder(noContentLabel);
	}

	@FXML
	public void handleEditableSupplierTable() {
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit(event -> {
			NhaCungCap supplier = event.getRowValue();
			supplier.setTenNCC(event.getNewValue());
		});

		phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		phoneColumn.setOnEditCommit(event -> {
			NhaCungCap supplier = event.getRowValue();
			supplier.setSoDienThoai(event.getNewValue());
		});
		
		addressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		addressColumn.setOnEditCommit(event -> {
			NhaCungCap supplier = event.getRowValue();
			supplier.setDiaChi(event.getNewValue());
		});
		
		idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		idColumn.setOnEditCommit(event -> {
			NhaCungCap supplier = event.getRowValue();
			supplier.setMaNCC(event.getNewValue());
		});
		
		emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		emailColumn.setOnEditCommit(event -> {
			NhaCungCap supplier = event.getRowValue();
			supplier.setEmail(event.getNewValue());
		});
		

	}

	@FXML
	public void handleAddSupplierAction() {
		addSupplierBtn.setOnMouseClicked(event -> {
			try {
				Parent addSupplierFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemNhaCungCap_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(addSupplierFrame);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		addSupplierBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransiton(addSupplierBtn, 1, 0.7, 200, () -> {
			});
		});

		addSupplierBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransiton(addSupplierBtn, 0.7, 1, 200, () -> {
			});
		});
	}

	@FXML
	public void handleSearchSupplierAction() {

	}

}
