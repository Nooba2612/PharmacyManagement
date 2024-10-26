package pharmacy.gui;

import java.io.IOException;
import java.time.LocalDate;

import com.itextpdf.layout.element.List;

import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
	private TableView<HoaDon> invoiceTable;

	@FXML
	private TableColumn<HoaDon, String> idColumn;

	@FXML
	private TableColumn<HoaDon, String> customerNameColumn;

	@FXML
	private TableColumn<HoaDon, String> employeeNameColumn;

	@FXML
	private TableColumn<HoaDon, LocalDate> createDateColumn;

	@FXML
	private TableColumn<HoaDon, Double> totalColumn;

	@FXML
	private TableColumn<HoaDon, Double> amountPaidColumn;

	@FXML
	private TableColumn<HoaDon, Double> changeColumn;

	@FXML
	private TableColumn<HoaDon, String> paymentMethodColumn;

	@FXML
	private TableColumn<HoaDon, Void> detailColumn;

	@FXML
	private DatePicker fromDate;

	@FXML
	private DatePicker toDate;

	@FXML
	private TextField searchField;

	@FXML
	private Button searchBtn;

	@FXML
	private Pane searchPane;

	@FXML
	private HBox root;

	private ObservableList<HoaDon> data;

	@FXML
	public void initialize() {
		handleinvoiceTableAction();
		handleSearchHoaDonAction();
	}

	@FXML
	public void handleinvoiceTableAction() {
		setUpTable();
		setupTablePlaceholder();
	}

	@FXML
	public void setUpTable() {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
		customerNameColumn
				.setCellValueFactory(
						cellData -> new SimpleStringProperty(cellData.getValue().getKhachHang().getHoTen()));
		employeeNameColumn
				.setCellValueFactory(
						cellData -> new SimpleStringProperty(cellData.getValue().getNhanVien().getHoTen()));
		createDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));
		totalColumn.setCellValueFactory(new PropertyValueFactory<>("tongTien")); // Cần thêm thuộc tính này trong HoaDon
		amountPaidColumn.setCellValueFactory(new PropertyValueFactory<>("tienKhachDua"));
		changeColumn.setCellValueFactory(new PropertyValueFactory<>("tienThay")); // Cần thêm thuộc tính này trong
		paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("loaiThanhToan"));
		handleAddDetailButtonToColumn();

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
	public void handleSearchHoaDonAction() {
	}
}
