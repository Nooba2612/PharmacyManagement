package pharmacy.gui;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import pharmacy.bus.HoaDon_BUS;
import pharmacy.bus.KhachHang_BUS;
import pharmacy.bus.NhaCungCap_BUS;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.ThietBiYTe_BUS;
import pharmacy.bus.Thuoc_BUS;
import pharmacy.entity.HoaDon;
import pharmacy.entity.Thuoc;
import pharmacy.utils.NodeUtil;

import java.io.IOException;
import java.sql.Wrapper;
import java.time.LocalDate;
import java.util.List;

import javax.lang.model.element.Element;

public class ThongKe_GUI {
	@FXML
	private BarChart<String, Double> employeeRevenueChart;

	@FXML
	private DatePicker employeeRevenueFromDate;

	@FXML
	private DatePicker employeeRevenueToDate;

	@FXML
	private GridPane quantityListPane;

	@FXML
	private BarChart<String, Double> revenueChart;

	@FXML
	private DatePicker revenueFromDate;

	@FXML
	private ComboBox<String> revenueStatisticType;

	@FXML
	private DatePicker revenueToDate;

	@FXML
	private TableColumn<?, ?> topEmployeeColumn;

	@FXML
	private TableView<?> topEmployeeTable;

	@FXML
	private TableView<?> topProductTable;

	@FXML
	private Text totalCustomerQuantity;

	@FXML
	private Text totalEmployeeQuantity;

	@FXML
	private Text totalEquipmentQuantity;

	@FXML
	private Text totalInvoiceQuantity;

	@FXML
	private Text totalMedicineLowExpirationQuantity;

	@FXML
	private Text totalMedicineLowQuantity;

	@FXML
	private Text totalMedicineQuantity;

	@FXML
	private Text totalRevenue;

	@FXML
	private Text totalSupplierQuantity;

	// methods
	@FXML
	public void initialize() {
		handleTotalQuantityListAction();
		setUpRevenueChart();
		setUpEmployeeRevenueChart();
		handleRenderTotalQuantity();
	}

	@FXML
	public void setUpRevenueChart() {
		XYChart.Series<String, Double> series = new XYChart.Series<>();

		series.getData().add(new XYChart.Data<>("2024-09-11", 50000.0));
		series.getData().add(new XYChart.Data<>("2024-09-12", 75000.0));
		series.getData().add(new XYChart.Data<>("2024-09-13", 75000.0));
		series.getData().add(new XYChart.Data<>("2024-09-25", 75000.0));
		series.getData().add(new XYChart.Data<>("2024-09-26", 75000.0));
		series.getData().add(new XYChart.Data<>("2024-09-27", 75000.0));
		series.getData().add(new XYChart.Data<>("2024-09-28", 75000.0));
		series.getData().add(new XYChart.Data<>("2024-09-29", 100000.0));

		revenueChart.getData().add(series);
	}

	@FXML
	public void setUpEmployeeRevenueChart() {
		XYChart.Series<String, Double> series = new XYChart.Series<>();

		series.getData().add(new XYChart.Data<>("Nguyên", 50000.0));
		series.getData().add(new XYChart.Data<>("Mẫn", 75000.0));
		series.getData().add(new XYChart.Data<>("Vy", 90000.0));
		series.getData().add(new XYChart.Data<>("Thiên", 100000.0));

		employeeRevenueChart.getData().add(series);
	}

	@FXML
	public void handleTotalQuantityListAction() {
		List<Node> quantityList = quantityListPane.getChildren();

		for (Node item : quantityList) {
			item.setOnMouseEntered(event -> {
				NodeUtil.applyScaleTransition(item, 1, 1.05, 1, 1.05, 200, () -> {
				});
			});

			item.setOnMouseExited(event -> {
				NodeUtil.applyScaleTransition(item, 1.05, 1, 1.05, 1, 200, () -> {
				});
			});
		}
	}

	@FXML
	public void handleRenderTotalQuantity() {
		totalCustomerQuantity.setText(Integer.toString(new KhachHang_BUS().countKhachHang()));

		totalEmployeeQuantity.setText(Integer.toString(new NhanVien_BUS().countEmployees()));

		totalEquipmentQuantity.setText(Integer.toString(new ThietBiYTe_BUS().countThietBiYTe()));

		totalInvoiceQuantity.setText(Integer.toString(new HoaDon_BUS().countHoaDon()));

		totalMedicineLowExpirationQuantity.setText(Integer.toString(new Thuoc_BUS().countThuocSapHetHanSuDung()));

		totalMedicineLowQuantity.setText(Integer.toString(new Thuoc_BUS().countThuocSapHetTonKho()));

		totalMedicineQuantity.setText(Integer.toString(new Thuoc_BUS().countThuoc()));

		totalRevenue.setText(new HoaDon_BUS().calculateTotalRevenue());

		totalSupplierQuantity.setText(Integer.toString(new NhaCungCap_BUS().countNhaCungCap()));
	}

}
