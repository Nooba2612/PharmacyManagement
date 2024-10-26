package pharmacy.gui;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.support.RootBeanDefinition;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import pharmacy.entity.*;
import pharmacy.utils.NodeUtil;

public class KhachHang_GUI {
	@FXML
	HBox root;

	@FXML
	private TableView<KhachHang> customerTable;

	@FXML
	private TableColumn<KhachHang, String> idColumn;

	@FXML
	private TableColumn<KhachHang, String> nameColumn;

	@FXML
	private TableColumn<KhachHang, String> phoneColumn;

	@FXML
	private TableColumn<KhachHang, Integer> pointsColumn;

	@FXML
	private TableColumn<KhachHang, LocalDate> yearColumn;

	@FXML
	private TableColumn<KhachHang, String> noteColumn;

	@FXML
	private TableColumn<KhachHang, Void> actionColumn;

	@FXML
	private Button addCustomerBtn;

	@FXML
	private TextField searchField;

	// methods
	@FXML
	public void initialize() {
		handleCustomerTableAction();
		handleSeacrhCustomerAction();
	}

	@FXML
	public void handleCustomerTableAction() {
		addRow();
		handleEditableCustomerTable();
		setupTablePlaceholder();
		handleAddCustomerAction();
	}

	@FXML
	public void addRow() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
		idColumn.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
		pointsColumn.setCellValueFactory(new PropertyValueFactory<>("diemTichLuy"));
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("namSinh"));
		noteColumn.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

		// handleAddButtonToActionColumn();

		ObservableList<KhachHang> data = FXCollections.observableArrayList(
				new KhachHang("KH01", "Nguyen Van A", "0123456789", 100, LocalDate.parse("2023-01-01"), "Ghi chú cc",
						"Nam"),
				new KhachHang("KH02", "Tran Thi B", "0987654321", 50, LocalDate.parse("2023-02-01"), "Ghi chú cc",
						"Nữ"));

		customerTable.setItems(data);

	}

	@FXML
	public void handleAddButtonToActionColumn() {
		actionColumn.setCellFactory(column -> {
			return new TableCell<KhachHang, Void>() {
				private final Button deleteButton = new Button();

				{
					// Style the delete button
					deleteButton.setStyle("-fx-background-color: #D23617; -fx-text-fill: #FFF;");

					// Action for the delete button
					deleteButton.setOnAction(event -> {
						KhachHang customer = getTableView().getItems().get(getIndex());
						getTableView().getItems().remove(customer);
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
						TableRow<KhachHang> currentRow = getTableRow();
						currentRow.setOnMouseEntered(event -> {
							deleteButton.setVisible(true);
							NodeUtil.applyFadeTransition(deleteButton, 0, 1, 300, () -> {
							});
						});
						currentRow
								.setOnMouseExited(event -> NodeUtil.applyFadeTransition(deleteButton, 1, 0, 300, () -> {
									deleteButton.setVisible(false);
								}));
					}
				}
			};
		});
	}

	@FXML
	public void setupTablePlaceholder() {
		Label noContentLabel = new Label("Không có khách hàng.");
		noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
		customerTable.setPlaceholder(noContentLabel);
	}

	@FXML
	public void handleEditableCustomerTable() {
		nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		nameColumn.setOnEditCommit(event -> {
			KhachHang customer = event.getRowValue();
			customer.setHoTen(event.getNewValue());
		});

		phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		phoneColumn.setOnEditCommit(event -> {
			KhachHang customer = event.getRowValue();
			customer.setSoDienThoai(event.getNewValue());
		});

		noteColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		noteColumn.setOnEditCommit(event -> {
			KhachHang customer = event.getRowValue();
			customer.setGhiChu(event.getNewValue());
		});
	}

	@FXML
	public void handleAddCustomerAction() {
		addCustomerBtn.setOnMouseClicked(event -> {
			try {
				Parent addCustomerFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemKhachHang_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(addCustomerFrame);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		addCustomerBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(addCustomerBtn, 1, 0.7, 200, () -> {
			});
		});

		addCustomerBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(addCustomerBtn, 0.7, 1, 200, () -> {
			});
		});
	}

	@FXML
	public void handleSeacrhCustomerAction() {

	}

}