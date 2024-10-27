package pharmacy.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import pharmacy.bus.*;
import pharmacy.dao.NhanVien_DAO;
import pharmacy.entity.*;
import pharmacy.utils.NodeUtil;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.cglib.core.Local;

public class ThongKe_GUI {
	@FXML
	private BarChart<String, Number> employeeRevenueChart;

	@FXML
	private DatePicker employeeRevenueFromDate;

	@FXML
	private DatePicker employeeRevenueToDate;

	@FXML
	private GridPane quantityListPane;

	@FXML
	private VBox revenuePane;
	@FXML

	private VBox employeeRevenuePane;

	@FXML
	private ComboBox<String> revenueStatisticType;

	@FXML
	private DatePicker revenueDateSelect;

	@FXML
	private DatePicker topSaleDateSelect;

	@FXML
	private TableColumn<NhanVien, Integer> topEmployeeColumn;

	@FXML
	private Text topProductTitle;

	@FXML
	private Text topEmployeeTitle;

	@FXML
	private TableView<NhanVien> topEmployeeTable;

	@FXML
	private TableView<SanPham> topProductTable;

	@FXML
	private Text totalCustomerQuantity;

	@FXML
	private Text totalEmployeeQuantity;

	@FXML
	private Text totalEquipmentQuantity;

	@FXML
	private Text totalInvoiceQuantity;

	@FXML
	private Text totalProductLowExpirationQuantity;

	@FXML
	private Text totalProductLowQuantity;

	@FXML
	private Text totalMedicineQuantity;

	@FXML
	private Text totalRevenue;

	@FXML
	private Text totalSupplierQuantity;

	@FXML
	private TableColumn<NhanVien, String> employeeIdColumn;

	@FXML
	private TableColumn<NhanVien, String> employeeNameColumn;

	@FXML
	private TableColumn<NhanVien, Integer> employeeOrderNumberColumn;

	@FXML
	private TableColumn<NhanVien, Double> employeeRevenueColumn;

	@FXML
	private TableColumn<SanPham, String> productIdColumn;

	@FXML
	private TableColumn<SanPham, String> productNameColumn;

	@FXML
	private TableColumn<SanPham, Double> productPriceColumn;

	@FXML
	private TableColumn<SanPham, Integer> productTopColumn;

	@FXML
	private TableColumn<SanPham, String> productTypeColumn;

	@FXML
	private TableColumn<SanPham, Integer> soldQuantityColumn;

	@FXML
	private ComboBox<String> topSaleType;

	@FXML
	private TableView<KhachHang> topCustomerTable;

	@FXML
	private DatePicker customerStatisticDateSelect;

	@FXML
	private ComboBox<String> customerStatisticType;

	@FXML
	private VBox newMemberChartPane;

	@FXML
	private TableColumn<KhachHang, Integer> topCustomerColumn;

	@FXML
	private TableColumn<KhachHang, String> topCustomerNameColumn;

	@FXML
	private TableColumn<KhachHang, String> topCustomerPhoneColumn;

	@FXML
	private TableColumn<KhachHang, Integer> topCustomerPointsColumn;

	// methods
	@FXML
	public void initialize() throws SQLException {
		handleTotalQuantityListAction();
		setUpRevenueChart();
		handleRenderTotalQuantity();
		setUpTopSaleTable();
		setUpStatisticCustomer();
	}

	@FXML
	public void setUpRevenueChart() {
		revenueDateSelect.setValue(LocalDate.now());
		revenueStatisticType.getItems().setAll("Ngày", "Tháng", "Năm");
		revenueStatisticType.getSelectionModel().selectFirst();

		revenueStatisticType.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					try {
						renderRevenueChart(newValue, revenueDateSelect.getValue());
					} catch (SQLException e) {
						e.printStackTrace();
					}

					// setup employee revenue chart
					switch (newValue) {
						case "Ngày":
							renderEmployeeRevenueChart(revenueDateSelect.getValue().toString());
							break;
						case "Tháng":
							String currentMonth = String.valueOf(LocalDate.now().getMonthValue());
							String currentYear = String.valueOf(LocalDate.now().getYear());
							String monthString = String.format("%s-%s", currentYear, currentMonth);
							renderEmployeeRevenueChart(monthString);
							break;
						case "Năm":
							String currentYearString = String.valueOf(LocalDate.now().getYear());
							renderEmployeeRevenueChart(currentYearString);
							break;
						default:
							break;
					}
				});

		revenueDateSelect.valueProperty().addListener((observable, oldDate, newDate) -> {
			if (newDate != null) {
				try {
					renderRevenueChart(
							revenueStatisticType.getValue(), newDate);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				renderEmployeeRevenueChart(newDate.toString());
			}
		});

		try {
			renderRevenueChart(revenueStatisticType.getValue(), revenueDateSelect.getValue());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		renderEmployeeRevenueChart(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	}

	private void renderRevenueChart(String selectedType, LocalDate dateSelected) throws SQLException {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		BarChart<String, Number> revenueChart = new BarChart<>(xAxis, yAxis);
		XYChart.Series<String, Number> series = new XYChart.Series<>();

		yAxis.setLabel("Tổng thu (Đồng)");
		revenueChart.setLegendVisible(false);
		revenueChart.setStyle("-fx-bar-fill: #339933;");
		revenueChart.getStylesheets().add(getClass().getResource("/css/TableStyles.css").toExternalForm());
		revenueChart.setAnimated(true);
		xAxis.setAnimated(true);
		yAxis.setAnimated(true);

		int currentYear = dateSelected.getYear();
		int currentMonth = dateSelected.getMonthValue();

		HoaDon_BUS hoaDonBus = new HoaDon_BUS();
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		int weekOfMonth = revenueDateSelect.getValue().get(weekFields.weekOfMonth());

		switch (selectedType) {
			case "Ngày":
				revenueChart.setTitle(String.format("Doanh thu tuần " + weekOfMonth + " tháng " + currentMonth));
				xAxis.setLabel("Ngày");
				LocalDate startOfWeek = dateSelected
						.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

				for (int i = 0; i < 7; i++) {
					LocalDate dayOfWeek = startOfWeek.plusDays(i);
					double revenue = hoaDonBus.calculateRevenueByDate(dayOfWeek.toString());
					XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(
							dayOfWeek.format(DateTimeFormatter.ofPattern("dd")), revenue);
					dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
						newNode.setOnMouseClicked(event -> {
							renderEmployeeRevenueChart(dayOfWeek.toString());
						});

						newNode.setOnMouseEntered(event -> {
							newNode.setStyle("-fx-bar-fill: orange;");
						});

						newNode.setOnMouseExited(event -> {
							newNode.setStyle("-fx-bar-fill: #339933;");
						});

						if (newNode != null) {
							NodeUtil.applyTranslateYTransition(newNode, 200, 0, 400, () -> {
							});
							NodeUtil.applyFadeTransition(newNode, 0, 1, 400, () -> {
							});
						}
						newNode.setCursor(Cursor.HAND);
					});
					series.getData().add(dataPoint);
				}
				break;

			case "Tháng":
				revenueChart.setTitle(String.format("Doanh thu tháng " + currentMonth));
				xAxis.setLabel("Ngày");

				int daysInMonth = LocalDate.of(currentYear, currentMonth, 1).lengthOfMonth();

				for (int day = 1; day <= daysInMonth; day++) {
					LocalDate date = LocalDate.of(currentYear, currentMonth, day);
					String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					double revenue = hoaDonBus.calculateRevenueByDate(dateString);
					XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(
							date.format(DateTimeFormatter.ofPattern("dd")), revenue);

					dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
						newNode.setOnMouseClicked(event -> {
							renderEmployeeRevenueChart(dateString);
						});

						newNode.setOnMouseEntered(event -> {
							newNode.setStyle("-fx-bar-fill: orange;");
						});

						newNode.setOnMouseExited(event -> {
							newNode.setStyle("-fx-bar-fill: #339933;");
						});

						if (newNode != null) {
							NodeUtil.applyTranslateYTransition(newNode, 200, 0, 400, () -> {
							});
							NodeUtil.applyFadeTransition(newNode, 0, 1, 400, () -> {
							});
						}
						newNode.setCursor(Cursor.HAND);
					});
					series.getData().add(dataPoint);
				}
				break;

			case "Năm":
				revenueChart.setTitle("Doanh thu năm " + currentYear);
				xAxis.setLabel("Tháng");

				for (int month = 1; month <= 12; month++) {
					double revenue = hoaDonBus.calculateRevenueByMonth(month, currentYear);
					XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(String.valueOf(month), revenue);
					LocalDate date = LocalDate.of(currentYear, month, 1);
					String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));

					dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
						newNode.setOnMouseClicked(event -> {
							renderEmployeeRevenueChart(dateString);
						});

						newNode.setOnMouseEntered(event -> {
							newNode.setStyle("-fx-bar-fill: orange;");
						});

						newNode.setOnMouseExited(event -> {
							newNode.setStyle("-fx-bar-fill: #339933;");
						});
						if (newNode != null) {
							NodeUtil.applyTranslateYTransition(newNode, 200, 0, 400, () -> {
							});
							NodeUtil.applyFadeTransition(newNode, 0, 1, 400, () -> {
							});
						}
						newNode.setCursor(Cursor.HAND);
					});

					series.getData().add(dataPoint);
				}
				break;

			default:
				break;
		}

		revenueChart.getData().add(series);
		revenuePane.getChildren().clear();
		revenuePane.getChildren().add(revenueChart);
	}

	@FXML
	public void renderEmployeeRevenueChart(String date) {
		employeeRevenueChart.getData().clear();
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		List<NhanVien> nhanVienList = new NhanVien_BUS().getAllEmployees();

		String[] dateParts = date.split("[-/]");

		switch (dateParts.length) {
			case 3:
				employeeRevenueChart.getXAxis()
						.setLabel("Ngày " + dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0]);
				break;

			case 2:
				employeeRevenueChart.getXAxis().setLabel("Tháng " + dateParts[1] + "-" + dateParts[0]);
				break;

			case 1:
				employeeRevenueChart.getXAxis().setLabel("Năm " + dateParts[0]);
				break;

			default:
				break;
		}

		for (NhanVien nhanVien : nhanVienList) {
			String maNhanVien = nhanVien.getMaNhanVien();
			double revenue = 0.0;
			try {
				revenue = new HoaDon_BUS().calculateRevenueByEmployee(maNhanVien, date);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			String employeeName = nhanVien.getHoTen();

			XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(employeeName, revenue);

			dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
				newNode.setOnMouseEntered(event -> {
					newNode.setStyle("-fx-bar-fill: orange;");
				});

				newNode.setOnMouseExited(event -> {
					newNode.setStyle("-fx-bar-fill: #339933;");
				});

				if (newNode != null) {
					NodeUtil.applyTranslateYTransition(newNode, 200, 0, 400, () -> {
					});
					NodeUtil.applyFadeTransition(newNode, 0, 1, 400, () -> {
					});
				}
				newNode.setCursor(Cursor.HAND);
			});
			series.getData().add(dataPoint);
		}

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
	public void handleRenderTotalQuantity() throws SQLException {
		totalCustomerQuantity.setText(Integer.toString(new KhachHang_BUS().countKhachHang()));

		totalEmployeeQuantity.setText(Integer.toString(new NhanVien_BUS().countEmployees()));

		totalInvoiceQuantity.setText(Integer.toString(new HoaDon_BUS().countHoaDon()));

		totalProductLowExpirationQuantity.setText(Integer.toString(new SanPham_BUS().countSanPhamSapHetHanSuDung()));

		totalProductLowQuantity.setText(Integer.toString(new SanPham_BUS().countSanPhamSapHetTonKho()));

		totalMedicineQuantity.setText(Integer.toString(new SanPham_BUS().countThuoc()));

		totalEquipmentQuantity.setText(Integer.toString(new SanPham_BUS().countThietBiYTe()));

		totalRevenue.setText(new HoaDon_BUS().calculateTotalRevenue());

		totalSupplierQuantity.setText(Integer.toString(new NhaCungCap_BUS().countNhaCungCap()));
	}

	@FXML
	public void setUpTopSaleTable() throws SQLException {
		topSaleDateSelect.setValue(LocalDate.now());
		topSaleType.getItems().addAll("Ngày", "Tháng", "Năm");
		topSaleType.getSelectionModel().selectFirst();

		topSaleDateSelect.valueProperty().addListener((observable, oldDate, newDate) -> {
			try {
				renderTopSaleProductTable(topSaleType.getValue(), newDate);
				renderTopEmployeeTable(topSaleType.getValue(), newDate);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		topSaleType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			try {
				renderTopEmployeeTable(newValue, topSaleDateSelect.getValue());
				renderTopSaleProductTable(newValue, topSaleDateSelect.getValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		renderTopEmployeeTable(topSaleType.getValue(), topSaleDateSelect.getValue());
		renderTopSaleProductTable(topSaleType.getValue(), topSaleDateSelect.getValue());
	}

	@FXML
	public void renderTopEmployeeTable(String selectedType, LocalDate date) {
		List<NhanVien> topEmployeeList = new ArrayList<>();

		switch (selectedType) {
			case "Ngày":
				topEmployeeList = new NhanVien_BUS().getTopRevenueEmployees(date.toString());
				break;

			case "Tháng":
				YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonth());
				topEmployeeList = new NhanVien_BUS().getTopRevenueEmployees(yearMonth.toString());
				break;

			case "Năm":
				String year = String.valueOf(date.getYear());
				topEmployeeList = new NhanVien_BUS().getTopRevenueEmployees(year);
				break;

			default:
				break;
		}

		ObservableList<NhanVien> observableList = FXCollections.observableArrayList(topEmployeeList);

		topEmployeeColumn.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(observableList.indexOf(cellData.getValue()) + 1).asObject());

		employeeOrderNumberColumn.setCellValueFactory(
				cellData -> new ReadOnlyObjectWrapper<>(
						new NhanVien_DAO().getOrderQuantityOfEmployee(cellData.getValue().getMaNhanVien())));

		employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("maNhanVien"));

		employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));

		employeeRevenueColumn.setCellValueFactory(cellData -> {
			double revenue = 0;
			try {
				revenue = new HoaDon_BUS().calculateRevenueByEmployee(
						cellData.getValue().getMaNhanVien(),
						"");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new SimpleDoubleProperty(revenue).asObject();
		});

		topEmployeeTable.setItems(observableList);
	}

	@FXML
	public void renderTopSaleProductTable(String selectedType, LocalDate date) throws SQLException {
		List<SanPham> topProductList = new ArrayList<>();

		switch (selectedType) {
			case "Ngày":
				topProductList = new SanPham_BUS().getTopSaleSanPhamByDate(date.toString());

				break;

			case "Tháng":
				YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonth());
				topProductList = new SanPham_BUS().getTopSaleSanPhamByDate(yearMonth.toString());

				break;

			case "Năm":
				String year = String.valueOf(date.getYear());
				topProductList = new SanPham_BUS().getTopSaleSanPhamByDate(year);

				break;

			default:
				break;
		}

		ObservableList<SanPham> observableList = FXCollections.observableArrayList(topProductList);

		productTopColumn.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(observableList.indexOf(cellData.getValue()) + 1).asObject());

		productIdColumn.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
		productNameColumn.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
		productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));
		productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("donGiaBan"));

		topProductTable.setItems(observableList);
	}

	public void setUpStatisticCustomer() {
		customerStatisticDateSelect.setValue(LocalDate.now());
		customerStatisticType.getItems().addAll("Ngày", "Tháng", "Năm");

		customerStatisticType.getSelectionModel().selectFirst();

		customerStatisticDateSelect.valueProperty().addListener((observable, oldDate, newDate) -> {
			renderNewMemberChart(customerStatisticType.getValue(), newDate);
		});

		customerStatisticType.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					renderNewMemberChart(newValue, customerStatisticDateSelect.getValue());
				});

		renderNewMemberChart(customerStatisticType.getValue(), customerStatisticDateSelect.getValue());
		renderTopCustomerTable();
	}

	public void renderNewMemberChart(String selectedType, LocalDate date) {
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		BarChart<String, Number> newMemberChart = new BarChart<>(xAxis, yAxis);
		XYChart.Series<String, Number> series = new XYChart.Series<>();

		newMemberChart.getStylesheets().add(getClass().getResource("/css/TableStyles.css").toExternalForm());
		newMemberChart.setLegendVisible(false);

		xAxis.setLabel(selectedType.equals("Năm") ? "Tháng" : "Ngày");
		yAxis.setLabel("Số lượng người");

		switch (selectedType) {
			case "Ngày":
				LocalDate startOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
				for (int i = 0; i < 7; i++) {
					LocalDate dayOfWeek = startOfWeek.plusDays(i);
					List<KhachHang> khachHangList = new KhachHang_BUS().getNewCustomerByDate(dayOfWeek.toString());
					XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(
							dayOfWeek.format(DateTimeFormatter.ofPattern("dd")), khachHangList.size());

					setupDataPoint(dataPoint, dayOfWeek);
					series.getData().add(dataPoint);
				}
				break;

			case "Tháng":
				int daysInMonth = LocalDate.of(date.getYear(), date.getMonth(), 1).lengthOfMonth();
				YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonth());
				for (int day = 1; day <= daysInMonth; day++) {
					LocalDate _date = LocalDate.of(date.getYear(), date.getMonth(), day);
					List<KhachHang> khachHangList = new KhachHang_BUS().getNewCustomerByDate(_date.toString());

					XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(
							_date.format(DateTimeFormatter.ofPattern("dd")), khachHangList.size());
					setupDataPoint(dataPoint, _date);
					series.getData().add(dataPoint);
				}
				break;

			case "Năm":
				for (int month = 1; month <= 12; month++) {
					LocalDate _date = LocalDate.of(date.getYear(), month, 1);
					List<KhachHang> khachHangList = new KhachHang_BUS()
							.getNewCustomerByDate(YearMonth.of(date.getYear(), month).toString());
					XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(String.valueOf(month),
							khachHangList.size());

					setupDataPoint(dataPoint, _date);
					series.getData().add(dataPoint);
				}
				break;

			default:
				break;
		}

		newMemberChart.getData().add(series);
		newMemberChartPane.getChildren().clear();
		newMemberChartPane.getChildren().add(newMemberChart);
	}

	private void setupDataPoint(XYChart.Data<String, Number> dataPoint, LocalDate date) {
		dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
			newNode.setOnMouseClicked(event -> {
				renderEmployeeRevenueChart(date.toString());
			});

			newNode.setOnMouseEntered(event -> {
				newNode.setStyle("-fx-bar-fill: orange;");
			});

			newNode.setOnMouseExited(event -> {
				newNode.setStyle("-fx-bar-fill: #339933;");
			});

			if (newNode != null) {
				NodeUtil.applyTranslateYTransition(newNode, 200, 0, 400, () -> {
				});
				NodeUtil.applyFadeTransition(newNode, 0, 1, 400, () -> {
				});
			}
			newNode.setCursor(Cursor.HAND);
		});
	}

	public void renderTopCustomerTable() {
		List<KhachHang> khachHangList = new KhachHang_BUS().getTopCustomer();
		ObservableList<KhachHang> observableList = FXCollections.observableArrayList(khachHangList);

		topCustomerColumn.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(observableList.indexOf(cellData.getValue()) + 1).asObject());
		topCustomerNameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
		topCustomerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
		topCustomerPointsColumn.setCellValueFactory(new PropertyValueFactory<>("diemTichLuy"));

		topCustomerTable.setItems(observableList);
	}

}
