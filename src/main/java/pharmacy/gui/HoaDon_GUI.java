package pharmacy.gui;

import java.io.IOException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import pharmacy.entity.*;
import pharmacy.utils.NodeUtil;
import javafx.scene.Node;
import javafx.scene.Parent;

public class HoaDon_GUI {

	@FXML
	private Button addInvoiceBtn;

	@FXML
	private TableColumn<HoaDon, String> createDateColumn;

	@FXML
	private TableColumn<HoaDon, String> customerNameColumn;

	@FXML
	private TableColumn<HoaDon, Void> detailColumn;

	@FXML
	private TableColumn<HoaDon, String> employeeNameColumn;

	@FXML
	private TableColumn<HoaDon, String> idColumn;

	@FXML
	private TableView<HoaDon> invoiceTable;

	@FXML
	private TableColumn<HoaDon, Double> amountPaidColumn;

	@FXML
	private TableColumn<HoaDon, Double> changeColumn;

	@FXML
	private TableColumn<HoaDon, String> paymentMethodColumn;

	@FXML
	private TableColumn<HoaDon, Double> totalColumn;

	@FXML
	private HBox root;

	@FXML
	private Button searchBtn;

	@FXML
	private TextField searchField;

	@FXML
	private Pane searchPane;

	@FXML
	public void initialize() {
		handleinvoiceTableAction();
		handleSearchHoaDonAction();
	}

	@FXML
	public void handleinvoiceTableAction() {
		addRow();
		setupTablePlaceholder();
		handleAddHoaDonAction();
	}

	@FXML
	public void addRow() {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
		createDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));
		amountPaidColumn.setCellValueFactory(new PropertyValueFactory<>("tienKhachDua"));
		// changeColumn.setCellValueFactory(new PropertyValueFactory<>("changeAmount"));
		// totalColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
		paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("loaiThanhToan"));
		customerNameColumn.setCellValueFactory(cellData -> {
			KhachHang khachHang = cellData.getValue().getKhachHang();

			return new SimpleStringProperty(khachHang != null ? khachHang.getHoTen() : "");
		});

		employeeNameColumn.setCellValueFactory(cellData -> {
			NhanVien nhanVien = cellData.getValue().getNhanVien();

			return new SimpleStringProperty(nhanVien != null ? nhanVien.getHoTen() : "");
		});

		handleAddDetailButtonToColumn();

		ObservableList<HoaDon> data = FXCollections.observableArrayList(new HoaDon(),

				new HoaDon(),

				new HoaDon());

		invoiceTable.setItems(data);
	}

	@FXML
	public void handleAddDetailButtonToColumn() {
		detailColumn.setCellFactory(column -> {
			return new TableCell<HoaDon, Void>() {
				private final Button detailButton = new Button();

				{
					// Handle detail button actions
					detailButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");

					detailButton.setOnAction(event -> {
						HoaDon HoaDon = getTableView().getItems().get(getIndex());
						System.out.println("Detail button clicked for HoaDon: " + HoaDon.getMaHoaDon());
					});

					Image image = new Image(getClass().getResourceAsStream("/images/detail-icon.png"));
					ImageView imageView = new ImageView(image);

					imageView.setFitWidth(20);
					imageView.setFitHeight(20);
					imageView.setPreserveRatio(true);
					detailButton.setGraphic(imageView);
					detailButton.setStyle("-fx-background-color: transparent;");
					detailButton.setVisible(false);

					detailButton.setOnMouseEntered(event -> {
						NodeUtil.applyFadeTransition(detailButton, 1, 0.7, 300, () -> {
						});
						NodeUtil.applyScaleTransition(detailButton, 1, 1.1, 1, 1.1, 300, () -> {
						});
					});
					detailButton.setOnMouseExited(event -> {
						NodeUtil.applyFadeTransition(detailButton, 0.7, 1, 300, () -> {
						});
						NodeUtil.applyScaleTransition(detailButton, 1.1, 1, 1.1, 1, 300, () -> {
						});
					});

				}

				@Override
				protected void updateItem(Void item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						setGraphic(detailButton);
						setStyle("-fx-alignment: CENTER;");
					}
				}

				@Override
				public void updateIndex(int i) {
					super.updateIndex(i);
					if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
						TableRow<HoaDon> currentRow = getTableRow();

						currentRow.setOnMouseEntered(event -> {
							detailButton.setVisible(true);
							NodeUtil.applyFadeTransition(detailButton, 0, 1, 300, () -> {
							});
						});
						currentRow
								.setOnMouseExited(event -> NodeUtil.applyFadeTransition(detailButton, 1, 0, 300, () -> {
									detailButton.setVisible(false);
								}));
					}
				}
			};
		});
	}

	@FXML
	public void setupTablePlaceholder() {
		Label noContentLabel = new Label("Không có hóa đơn.");
		noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
		invoiceTable.setPlaceholder(noContentLabel);
	}

	@FXML
	public void handleAddHoaDonAction() {
		addInvoiceBtn.setOnMouseClicked(event -> {
			try {
				Parent addHoaDonFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemHoaDon_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(addHoaDonFrame);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		addInvoiceBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(addInvoiceBtn, 1, 0.7, 200, () -> {
			});
		});

		addInvoiceBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(addInvoiceBtn, 0.7, 1, 200, () -> {
			});
		});
	}

	@FXML
	public void handleSearchHoaDonAction() {
		// Implement search logic here
	}
}
