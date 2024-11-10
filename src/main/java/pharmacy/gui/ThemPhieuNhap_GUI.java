package pharmacy.gui;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import pharmacy.bus.ChiTietPhieuNhap_BUS;
import pharmacy.bus.NhaCungCap_BUS;
import pharmacy.bus.PhieuNhap_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.ChiTietPhieuNhap;
import pharmacy.entity.KhachHang;
import pharmacy.entity.NhaCungCap;
import pharmacy.entity.NhanVien;
import pharmacy.entity.PhieuNhap;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;
import javafx.scene.text.Text;

public class ThemPhieuNhap_GUI {
	@FXML
	private Button backBtn;

	@FXML
	private HBox root;

	@FXML
	private TextField createDateField;

	@FXML
	private Button submitBtn, resetBtn, resetBtnAddProduct, exportIRecieptBtn;

	@FXML
	private TextField phieuNhapId, nameField;

	@FXML
	private ComboBox<String> supplierSelect;

	@FXML
	private ComboBox<String> unitField;

	@FXML
	private TextField manufacturerField;

	@FXML
	private DatePicker manufactureDateField, expirationDateField;

	private List<NhaCungCap> listSupplier;
	private List<SanPham> listProductSearch;
	private List<SanPham> listProductInit;

	@FXML
	private TableView<ChiTietPhieuNhap> productTable;

	@FXML
	private TableColumn<ChiTietPhieuNhap, String> maSanPham;

	@FXML
	private TableColumn<ChiTietPhieuNhap, String> tenSanPham;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Integer> soLuongNhap;

	@FXML
	private TableColumn<ChiTietPhieuNhap, LocalDate> ngaySanXuat;

	@FXML
	private TableColumn<ChiTietPhieuNhap, LocalDate> ngayHetHan;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Double> donGiaNhap;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Float> thue;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Double> thanhTien;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Void> actionColumn;

	@FXML
	private TableColumn<ChiTietPhieuNhap, String> ghiChuColumn;

	@FXML
	private TableColumn<ChiTietPhieuNhap, String> statusSPColumn;

	@FXML
	private ComboBox<String> productField;

	@FXML
	private Button addProductBtn;

	private ObservableList<ChiTietPhieuNhap> addedProductList = FXCollections.observableArrayList();

	@FXML
	private Text totalProduct, totalPrice;

	@FXML
	private Label supplierAlert, productAlert, employeeName;

	private NhanVien nhanVien;

	@FXML
	private ComboBox<String> productTypeField;

	@FXML
	private ComboBox<String> categoryField;

	@FXML
	private Label nameProductAlert, manufacturerAlert, unitAlert, productTypeAlert, categoryAlert, manufactureDateAlert, expirationDateAlert;

	@FXML
	public void initialize() throws SQLException {
		setUpForm();
		handleBackBtnClick();
		handleSearchProduct();
		handleEditableProductTable();
		resetForm();
		resetFormProduct();
		handleAddProduct();
		handleAddPhieuNhap();
		handleExportReceipt();
	}

	@FXML
	private void setUpForm() throws SQLException {
		// Set up icon for Import button
		ImageView importIcon = new ImageView(new Image(
				getClass().getClassLoader().getResource("images/arrow.png").toExternalForm()));
		importIcon.setFitHeight(15);
		importIcon.setFitWidth(15);

		// Set up ID, current employee name, and creation date
		phieuNhapId.setText(generateId());
		nhanVien = new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap();
		employeeName.setText(nhanVien.getHoTen());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		createDateField.setText(LocalDateTime.now().format(formatter));

		// Load initial list of products and suppliers
		listProductSearch = new SanPham_BUS().getTop20SanPhamTheoSLTon();
		listProductInit = listProductSearch;
		for (SanPham sanPham : listProductSearch) {
			String displayText = sanPham.getMaSanPham() + " - " + sanPham.getTenSanPham();
			productField.getItems().add(displayText);
		}

		listSupplier = new NhaCungCap_BUS().getAllNhaCungCap();
		for (NhaCungCap supplier : listSupplier) {
			String displayText = supplier.getMaNCC() + " - " + supplier.getTenNCC();
			supplierSelect.getItems().add(displayText);
		}

		// Set up placeholder for empty table
		if (productTable.getItems().isEmpty()) {
			Label noProductLabel = new Label("Không có sản phẩm nào trong bảng.");
			noProductLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
			productTable.setPlaceholder(noProductLabel);
		}

		// set up create product
		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		manufactureDateField.setConverter(new StringConverter<LocalDate>() {
			@Override
			public String toString(LocalDate date) {
				return date != null ? formatterDate.format(date) : "";
			}

			@Override
			public LocalDate fromString(String string) {
				return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatterDate) : null;
			}
		});

		manufactureDateField.setValue(LocalDate.now());

		expirationDateField.setConverter(new StringConverter<LocalDate>() {
			@Override
			public String toString(LocalDate date) {
				return date != null ? formatterDate.format(date) : "";
			}

			@Override
			public LocalDate fromString(String string) {
				return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatterDate) : null;
			}
		});

		expirationDateField.setValue(LocalDate.now().plusDays(1));
		setupActionColumn();
	}

	private void setupActionColumn() throws SQLException {
		actionColumn.setCellFactory(col -> new TableCell<ChiTietPhieuNhap, Void>() {
			private final ImageView deleteIcon = new ImageView(new Image(
					getClass().getClassLoader().getResource("images/x-icon.png").toExternalForm()));
			private final HBox hbox = new HBox(deleteIcon);

			{
				deleteIcon.setFitHeight(15);
				deleteIcon.setFitWidth(15);
				hbox.setAlignment(Pos.CENTER);
				hbox.setStyle("-fx-padding: 5;");
				deleteIcon.setOnMouseClicked(this::handleDeleteProduct);
			}

			private void handleDeleteProduct(MouseEvent event) {
				ChiTietPhieuNhap ctpn = getTableView().getItems().get(getIndex());
				getTableView().getItems().remove(ctpn);
				updateTotalProductCount();
				updateTotalAmount();
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(hbox);
				}
			}
		});

		// id
		phieuNhapId.setText(generateId());

		try {
			nhanVien = new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		employeeName.setText(nhanVien.getHoTen());

		// create date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		createDateField.setText(LocalDateTime.now().format(formatter));

		// danh sach 20 san pham dau tien
		listProductSearch = new SanPham_BUS().getTop20SanPhamTheoSLTon();
		listProductInit = listProductSearch;
		for (SanPham sanPham : listProductSearch) {
			String displayText = sanPham.getMaSanPham() + " - " + sanPham.getTenSanPham();
			productField.getItems().add(displayText);
		}

		// loai san pham
		productTypeField.getItems().addAll("Thuốc", "Thiết bị y tế");
		productTypeField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			categoryField.getItems().clear();
			if (newValue != null) {
				if (newValue.equals("Thuốc")) {
					categoryField.setDisable(false);
					categoryField.getItems().addAll("Giảm đau", "Hạ sốt", "Kháng sinh", "Chống viêm", "Vitamin",
							"An thần", "Siro", "Khác");
				} else if (newValue.equals("Thiết bị y tế")) {
					categoryField.getItems().addAll("Dụng cụ y tế", "Sản phẩm bảo vệ cá nhân", "Dung dịch vệ sinh",
							"Khác");
					categoryField.setDisable(false);
				}
			}
		});
		// don vi tinh
		unitField.getItems().addAll("Viên", "Vỉ", "Hộp", "Chai", "Ống", "Gói");

		// danh sach ncc
		listSupplier = new NhaCungCap_BUS().getAllNhaCungCap();
		for (NhaCungCap supplier : listSupplier) {
			String displayText = supplier.getMaNCC() + " - " + supplier.getTenNCC();
			supplierSelect.getItems().add(displayText);
		}

		// handle if table empty
		if (addedProductList.isEmpty()) {
			Label noProductLabel = new Label("Không có sản phẩm nào trong bảng.");
			noProductLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
			productTable.setPlaceholder(noProductLabel);
		}

	}

	@SuppressWarnings("unused")
	@FXML
	public void handleSearchProduct() throws SQLException {
		productField.getEditor().setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.SHIFT) {
				String searchKey = productField.getEditor().getText().trim();
				try {
					filterProducts(searchKey);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		productField.setOnAction(event -> {
			String selectedProduct = productField.getValue(); // Lấy giá trị đã chọn từ ComboBox
			if (selectedProduct != null) {
				for (SanPham sanPham : listProductSearch) {
					String displayText = sanPham.getMaSanPham() + " - " + sanPham.getTenSanPham();
					if (displayText.equals(selectedProduct)) {
						boolean isAlreadyAdded = false;

						for (ChiTietPhieuNhap existingProduct : addedProductList) {
							if (existingProduct.getSanPham().getMaSanPham().equals(sanPham.getMaSanPham())) {
								// Nếu sản phẩm đã có hiển thị thông báo lỗi
								isAlreadyAdded = true;

								// Hiển thị thông báo
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Thông báo");
								alert.setHeaderText(null);
								alert.setContentText("Sản phẩm \"" + existingProduct.getSanPham().getTenSanPham()
										+ "\" đã được chọn.");
								alert.showAndWait();

								break;
							}
						}

						// Nếu sản phẩm chưa có, thêm mới với số lượng là 1
						if (!isAlreadyAdded) {
							ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
							chiTiet.setSanPham(sanPham);
							chiTiet.setDonGia(10000);
							chiTiet.setSoLuong(1);
							chiTiet.setThue(0.1f);
							chiTiet.setNgaySX(sanPham.getNgaySX());
							chiTiet.setHanSuDung(sanPham.getHanSuDung());
							chiTiet.setGhiChu("Sản phẩm đã tồn tại");

							addedProductList.add(chiTiet);
						}

						handleRenderAddedProductsTable(); // Cập nhật bảng
						productField.getEditor().clear(); // Xóa nội dung của ComboBox
						break;
					}
				}
			}
		});

	}

	@FXML
	public void handleAddProduct() throws SQLException {
		addProductBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 1, 0.6, 200, () -> {
			});
		});
		addProductBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 0.6, 1, 200, () -> {
			});
		});
		addProductBtn.setCursor(Cursor.HAND);

		// add san pham
		addProductBtn.setOnAction(event -> {
			try {
				if (!validateFormCreateProduct()){
					return;
				}
				SanPham sp = new SanPham();
				sp.setMaSanPham(generateIdSanPham());
				sp.setTenSanPham(nameField.getText().trim());
				sp.setLoaiSanPham(productTypeField.getValue());
				sp.setDanhMuc(categoryField.getValue());
				sp.setNhaSX(manufacturerField.getText().trim());
				sp.setDonViTinh(unitField.getValue());
				sp.setNgayTao(LocalDate.now());
				sp.setNgayCapNhat(LocalDateTime.now());
				sp.setNgaySX(manufactureDateField.getValue());
				sp.setHanSuDung(expirationDateField.getValue());
				sp.setSoLuongTon(1);
				sp.setDonGiaBan(10000);
				sp.setThue(0);

				ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
				ctpn.setSanPham(sp);
				ctpn.setDonGia(10000);
				ctpn.setSoLuong(1);
				ctpn.setThue(0.1f);
				ctpn.setNgaySX(manufactureDateField.getValue());
				ctpn.setHanSuDung(expirationDateField.getValue());
				ctpn.setGhiChu("Sản phẩm mới");

				addedProductList.add(ctpn);
				handleRenderAddedProductsTable(); // Cập nhật bảng
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});
	}

	@FXML
	public boolean validateFormCreateProduct() {
		boolean isValid = true;

		// Validate Name field
		if (nameField.getText().trim().isEmpty()) {
			nameProductAlert.setText("Tên sản phẩm không được rỗng.");
			nameProductAlert.setVisible(true);
			isValid = false;
		} else {
			nameProductAlert.setVisible(false);
		}

		// Validate Manufacturer field
		if (manufacturerField.getText().trim().isEmpty()) {
			manufacturerAlert.setText("Nhà sản xuất thuốc không được rỗng.");
			manufacturerAlert.setVisible(true);
			isValid = false;
		} else {
			manufacturerAlert.setVisible(false);
		}

		// Validate Unit field
		String[] VALID_UNITS = { "Viên", "Vỉ", "Hộp", "Chai", "Ống", "Gói", "Cái", "Chiếc", "Bộ" };
		String unitValue = (unitField.getValue() != null) ? unitField.getValue().trim()
				: unitField.getEditor().getText().trim();

		if (unitValue.isEmpty()) {
			unitAlert.setText("Đơn vị tính chưa được chọn.");
			unitAlert.setVisible(true);
			isValid = false;
		} else if (!Arrays.asList(VALID_UNITS).contains(unitValue)) {
			unitAlert.setText("Đơn vị tính không hợp lệ.");
			unitAlert.setVisible(true);
			isValid = false;
		} else {
			unitAlert.setVisible(false);
		}

		// Validate Product type field
		String[] VALID_TYPES = { "Thuốc", "Thiết bị y tế" };
		String productTypeValue = (productTypeField.getValue() != null) ? productTypeField.getValue().trim()
				: productTypeField.getEditor().getText().trim();

		if (productTypeValue.isEmpty()) {
			productTypeAlert.setText("Loại sản phẩm chưa được chọn.");
			productTypeAlert.setVisible(true);
			isValid = false;
		} else if (!Arrays.asList(VALID_TYPES).contains(productTypeValue)) {
			productTypeAlert.setText("Loại sản phẩm không hợp lệ.");
			productTypeAlert.setVisible(true);
			isValid = false;
		} else {
			productTypeAlert.setVisible(false);
		}

		// Validate Category field
		String[] VALID_CATEGORIES = { "giảm đau", "hạ sốt", "kháng sinh", "chống viêm",
				"vitamin", "an thần", "siro", "dụng cụ y tế", "sản phẩm bảo vệ cá nhân", "dung dịch vệ sinh", "khác" };
		if (categoryField.getValue() == null || categoryField.getEditor().getText().trim().isEmpty()) {
			categoryAlert.setText("Danh mục chưa được chọn.");
			categoryAlert.setVisible(true);
			isValid = false;
		} else if (!Arrays.asList(VALID_CATEGORIES).contains(categoryField.getValue().toLowerCase())) {
			unitAlert.setText("Đơn vị tính không hợp lệ.");
			unitAlert.setVisible(true);
			isValid = false;
		} else {
			categoryAlert.setVisible(false);
		}

		// Validate Expiration Date
		if (expirationDateField.getValue() == null) {
			expirationDateAlert.setText("Ngày hết hạn không được rỗng.");
			expirationDateAlert.setVisible(true);
			isValid = false;
		} else {
			expirationDateAlert.setVisible(false);
		}

		// Validate Manufacture Date
		if (manufactureDateField.getValue() == null) {
			manufactureDateAlert.setText("Ngày sản xuất không được rỗng.");
			manufactureDateAlert.setVisible(true);
			isValid = false;
		} else {
			manufactureDateAlert.setVisible(false);
		}

		return isValid;
	}

	private void filterProducts(String keySearch) throws SQLException {
		if (keySearch.length() > 0) {
			productField.getItems().clear();
			listProductSearch = new SanPham_BUS().getSanPhamByMaOrTenSP(keySearch);
			for (SanPham sanPham : listProductSearch) {
				String displayText = sanPham.getMaSanPham() + " - " + sanPham.getTenSanPham();
				productField.getItems().add(displayText);
			}
			productField.show();
		}
	}

	@SuppressWarnings("unused")
	@FXML
	public void handleAddPhieuNhap() throws SQLException {
		submitBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 1, 0.6, 200, () -> {
			});
		});
		submitBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 0.6, 1, 200, () -> {
			});
		});
		submitBtn.setCursor(Cursor.HAND);

		// add phieu nhap
		submitBtn.setOnAction(event -> {
			try {
				boolean check = addPhieuNhap();
				if (!check){
					return;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public boolean addPhieuNhap() throws SQLException {
		boolean check = true;
		if (addedProductList.isEmpty()) {
			productAlert.setText("Sản phẩm không được rỗng.");
			productAlert.setVisible(true);
			check = false;
		} else {
			productAlert.setVisible(false);
		}

		String supplier = supplierSelect.getValue();
		if (supplier == null) {
			supplierAlert.setText("Nhà cung cấp không được rỗng.");
			supplierAlert.setVisible(true);
			check = false;
		} else {
			supplierAlert.setVisible(false);
		}

		if (!check) {
			return false;
		}

		String maPhieuNhap = phieuNhapId.getText().trim();
		LocalDateTime ngayTao = LocalDateTime.now();
		NhaCungCap selectedNCC = listSupplier.stream()
				.filter(sup -> (sup.getMaNCC() + " - " + sup.getTenNCC()).equals(supplier))
				.findFirst()
				.orElse(null);

		PhieuNhap phieuNhap = new PhieuNhap(maPhieuNhap, nhanVien, selectedNCC, ngayTao, addedProductList);
		Connection connection = null;
		try {
			connection = DatabaseConnection.getConnection();
			connection.setAutoCommit(false); // Bắt đầu giao dịch

			// Lưu phiếu nhập
			boolean isCreated = new PhieuNhap_BUS().createPhieuNhap(phieuNhap, connection);
			if (!isCreated) {
				connection.rollback(); // Rollback nếu không thể tạo phiếu nhập
				showErrorDialog("Lỗi thêm phiếu nhập",
						"Có lỗi xảy ra khi thêm phiếu nhập. Vui lòng kiểm tra lại dữ liệu.");
				return false;
			}
			// Thêm chi tiết phiếu nhập
			for (ChiTietPhieuNhap sp : addedProductList) {

				if ("Sản phẩm mới".equals(sp.getGhiChu())) {
					sp.getSanPham().setSoLuongTon(sp.getSoLuong());
					sp.getSanPham().setDonGiaBan(Math.round(sp.getDonGia() * (1 + sp.getThue() / 100)));
					sp.getSanPham().setThue(sp.getThue());
					sp.getSanPham().setNgaySX(sp.getNgaySX());
					sp.getSanPham().setHanSuDung(sp.getHanSuDung());

					boolean isCreatedProduct = new SanPham_BUS().createSanPham(sp.getSanPham());
					if (!isCreatedProduct) {
						connection.rollback(); // Rollback nếu không thể tạo sản phẩm mới
						showErrorDialog("Lỗi tạo sản phẩm mới", "Không thể tạo sản phẩm mới: " + sp.getMaSanPham());
						return false;
					}
				} else {
					// Cập nhật số lượng tồn cho sản phẩm
					int newQuantity = sp.getSoLuong();
					boolean isUpdated = new SanPham_BUS().updateProductStock(sp.getMaSanPham(), sp.getTenSanPham(),
							sp.getSanPham().getNhaSX(), newQuantity,
							connection);
					if (!isUpdated) {
						connection.rollback(); // Rollback nếu có lỗi
						showErrorDialog("Lỗi cập nhật tồn kho",
								"Không thể cập nhật số lượng tồn cho sản phẩm: " + sp.getMaSanPham());
						return false;
					}
				}

				ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
				chiTiet.setPhieuNhap(phieuNhap);
				chiTiet.setSanPham(sp.getSanPham());
				chiTiet.setSoLuong(sp.getSoLuong());
				chiTiet.setDonGia(sp.getDonGia());
				chiTiet.setThue(sp.getThue());
				chiTiet.setNgaySX(sp.getNgaySX());
				chiTiet.setHanSuDung(sp.getHanSuDung());

				// Lưu chi tiết phiếu nhập vào cơ sở dữ liệu
				boolean isCreatedCTPN = new ChiTietPhieuNhap_BUS().createChiTietPhieuNhap(chiTiet, connection);
				if (!isCreatedCTPN) {
					connection.rollback(); // Rollback nếu có lỗi
					showErrorDialog("Lỗi thêm chi tiết phiếu nhập", "Có lỗi xảy ra khi thêm chi tiết phiếu nhập.");
					return false;
				}

			}

			connection.commit(); // Xác nhận giao dịch nếu tất cả đều thành công
			showAddProductSuccessModal("Thêm phiếu nhập thành công");
			Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/PhieuNhap_GUI.fxml"));
			root.getChildren().clear();
			root.getChildren().add(customerFrame);

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback(); // Rollback nếu có ngoại lệ
				} catch (SQLException rollbackEx) {
					rollbackEx.printStackTrace();
				}
			}
			showErrorDialog("Lỗi thêm phiếu nhập",
					"Có lỗi xảy ra khi thêm phiếu nhập. Vui lòng kiểm tra lại dữ liệu.");
			e.printStackTrace();
		} catch (java.io.IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close(); // Đóng kết nối
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
				return true;
	}

	@FXML
	public void handleExportReceipt() {
    exportIRecieptBtn.setOnAction(event -> {
		try {
			boolean check = addPhieuNhap();
			if (!check){
				return;
			}
		} catch (SQLException e) {
			showErrorDialog("Lỗi tạo phiếu nhập mới", "Không thể tạo phiếu nhập mới: " + phieuNhapId.getText());
			e.printStackTrace();
		}

        File pdfFile = new File("src/main/resources/pdf/ChiTietPhieuNhap.pdf");
		if (pdfFile.exists()) {
			pdfFile.delete();
		}

		class PhieuNhapContainer {
			PhieuNhap phieuNhap;
		}
		
		PhieuNhapContainer container = new PhieuNhapContainer();
		try {
			container.phieuNhap = new PhieuNhap_BUS().getPhieuNhapByMaPhieuNhap(phieuNhapId.getText());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			showErrorDialog("Lỗi", "Không thể lấy phiếu nhập.");
			e.printStackTrace();
		}
		if (container.phieuNhap != null) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					try {
						exportInvoiceToPdf("src/main/resources/pdf/ChiTietPhieuNhap.pdf", container.phieuNhap);

						Platform.runLater(() -> {
							try {
								File generatedPdfFile = new File("src/main/resources/pdf/ChiTietPhieuNhap.pdf");

								while (!generatedPdfFile.exists()) {
									try {
										Thread.sleep(100); // Đợi một chút trước khi thử lại
									} catch (InterruptedException e) {
										Thread.currentThread().interrupt();
									}
								}

								if (generatedPdfFile.exists()) {
									PDFUtil.showPdfPreview(generatedPdfFile);
								} else {
									Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, "File PDF không tồn tại sau khi xuất!");
								}
							} catch (Exception e) {
								Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, "Lỗi khi xử lý file PDF: ", e);
							}
						});
					} catch (com.itextpdf.io.exceptions.IOException | SQLException e) {
						Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, "Lỗi khi xuất PDF: ", e);
					}
					return null;
				}
			};
			new Thread(task).start();
		} else {
			showErrorDialog("Lỗi", "Không tìm thấy phiếu nhập với mã: " + phieuNhapId.getText());
		}
    });
}

 private void exportInvoiceToPdf(String outputPdfPath, PhieuNhap phieuNhap) throws SQLException, FileNotFoundException, java.io.IOException {
		try (PdfWriter writer = new PdfWriter(new FileOutputStream(outputPdfPath));
				PdfDocument pdfDoc = new PdfDocument(writer);
				Document document = new Document(pdfDoc)) {

			// Apply font
			URL fontUrl = getClass().getClassLoader().getResource("fonts/Roboto/Roboto-Regular.ttf");
			Path fontPath = Path.of(fontUrl.toURI());
			PdfFont font = PdfFontFactory.createFont(Files.readAllBytes(fontPath), PdfEncodings.IDENTITY_H,
					EmbeddingStrategy.PREFER_EMBEDDED);

			// Define primary color
			DeviceRgb primaryColor = new DeviceRgb(51, 153, 51);

			// Pharmacy Information
			document.add(new Paragraph("NHÀ THUỐC Medkit")
					.setFont(font)
					.setFontSize(16)
					.setBold()
					.setTextAlignment(TextAlignment.CENTER));
			document.add(new Paragraph("12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, Hồ Chí Minh")
					.setFont(font)
					.setFontSize(10)
					.setTextAlignment(TextAlignment.CENTER));
			document.add(new Paragraph("Điện thoại: (088) 6868-8686")
					.setFont(font)
					.setFontSize(10)
					.setTextAlignment(TextAlignment.CENTER));

			// phieuNhap Header
			document.add(new Paragraph("Phiếu Nhập Hàng")
					.setFont(font)
					.setFontSize(14)
					.setBold()
					.setTextAlignment(TextAlignment.CENTER)
					.setMarginTop(20));

			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
			String formattedDateTime = now.format(dateTimeFormatter);

			document.add(new Paragraph("Thời gian: " + formattedDateTime)
			.setFont(font)
			.setFontSize(10)
			.setTextAlignment(TextAlignment.CENTER));
			
			document.add(new Paragraph("Mã số: " + phieuNhap.getMaPhieuNhap())
			.setFont(font)
			.setFontSize(10)
			.setTextAlignment(TextAlignment.CENTER));

			// Customer and Date Information
			document.add(
					new Paragraph("Nhân viên tạo: " + phieuNhap.getTenNhanVien())
					.setFont(font)
					.setFontSize(10)
					.setMarginTop(10));
				
			document.add(new Paragraph("Ngày tạo: " + phieuNhap.getThoiGianNhap().format(dateTimeFormatter))
					.setFont(font)
					.setFontSize(10));

			document.add(
				new Paragraph("Nhập hàng từ nhà cung cấp: " + phieuNhap.getTenNhaCungCap() + ", địa điểm: " + phieuNhap.getNhaCungCap().getDiaChi())
					.setFont(font)
					.setFontSize(10));

			// Table Header
			Table table = new Table(new float[] { 1, 1, 3, 1, 2, 1, 2, 1, 2 });  // Điều chỉnh lại tỷ lệ cột
			table.setWidth(UnitValue.createPercentValue(100));

			// Tiêu đề bảng
			table.addHeaderCell(new Cell().add(new Paragraph("STT").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY)
					.setTextAlignment(TextAlignment.CENTER));  // Căn giữa tiêu đề
			table.addHeaderCell(new Cell().add(new Paragraph("Mã sản phẩm").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY)
					.setTextAlignment(TextAlignment.CENTER));  // Căn giữa tiêu đề
			table.addHeaderCell(new Cell().add(new Paragraph("Tên sản phẩm").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY)
					.setTextAlignment(TextAlignment.CENTER));  // Căn giữa tiêu đề
			table.addHeaderCell(new Cell().add(new Paragraph("Ngày sản xuất").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY)
					.setTextAlignment(TextAlignment.CENTER));  // Căn giữa tiêu đề
			table.addHeaderCell(new Cell().add(new Paragraph("Hạn sử dụng").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY)
					.setTextAlignment(TextAlignment.CENTER));  // Căn giữa tiêu đề
			table.addHeaderCell(new Cell().add(new Paragraph("SL").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY)
					.setTextAlignment(TextAlignment.CENTER));  // Căn giữa tiêu đề
			table.addHeaderCell(new Cell().add(new Paragraph("Đơn giá").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY)
					.setTextAlignment(TextAlignment.CENTER));  // Căn giữa tiêu đề
			table.addHeaderCell(new Cell().add(new Paragraph("Thuế").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY)
					.setTextAlignment(TextAlignment.CENTER));  // Căn giữa tiêu đề
			table.addHeaderCell(new Cell().add(new Paragraph("Thành Tiền").setFont(font).setFontSize(8))
					.setBackgroundColor(ColorConstants.LIGHT_GRAY)
					.setTextAlignment(TextAlignment.CENTER));  // Căn giữa tiêu đề

			double totalInvoiceAmount = 0;
			DateTimeFormatter formatterSanPham = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			for (int i = 0; i < phieuNhap.getChiTietPhieuNhapList().size(); i++) {
				ChiTietPhieuNhap detail = phieuNhap.getChiTietPhieuNhapList().get(i);
				SanPham sanpham = detail.getSanPham();
				totalInvoiceAmount += detail.calculateThanhTien();

				table.addCell(new Paragraph(String.valueOf(i + 1)).setFont(font).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
				table.addCell(new Paragraph(sanpham.getMaSanPham()).setFont(font).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
				table.addCell(new Paragraph(sanpham.getTenSanPham()).setFont(font).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
				table.addCell(new Paragraph(String.valueOf(formatterSanPham.format(sanpham.getNgaySX()))).setFont(font)
						.setFontSize(8).setTextAlignment(TextAlignment.CENTER));
				table.addCell(new Paragraph(String.valueOf(formatterSanPham.format(sanpham.getHanSuDung()))).setFont(font)
						.setFontSize(8).setTextAlignment(TextAlignment.CENTER));
				table.addCell(new Paragraph(String.valueOf(detail.getSoLuong())).setFont(font).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
				table.addCell(new Paragraph(String.valueOf(String.format("%,.0f", detail.getDonGia())))
						.setFont(font).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
				double tax = detail.getThue();
				String formattedTax = String.format("%.0f%%", tax * 100);
				table.addCell(new Paragraph(formattedTax)
						.setFont(font).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
				table.addCell(
						new Paragraph(String.valueOf(String.format("%,.0f", detail.calculateThanhTien()))).setFont(font).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
			}

			document.add(table.setMarginTop(20));

			int productQuantity = 0;
			for (ChiTietPhieuNhap cthd : phieuNhap.getChiTietPhieuNhapList()) {
				productQuantity += cthd.getSoLuong();
			}

			// Total Amount
			document.add(new Paragraph("Tổng " + productQuantity + " sản phẩm")
					.setFont(font)
					.setFontSize(9)
					.setBold()
					.setTextAlignment(TextAlignment.RIGHT)
					.setMarginTop(10));

			document.add(new Paragraph("Tổng tiền: " + String.format("%,.0f", totalInvoiceAmount) + " VND")
					.setFont(font)
					.setFontSize(9)
					.setBold()
					.setTextAlignment(TextAlignment.RIGHT));
		} catch (IOException | URISyntaxException e) {
			Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, e);
		}

		System.out.println("\n\nPDF generated at: " + outputPdfPath + "\n\n");
	}


	@SuppressWarnings("unused")
	@FXML
	public void resetForm() {
		resetBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(resetBtn, 1, 0.5, 200, () -> {
			});
		});

		resetBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(resetBtn, 0.5, 1, 200, () -> {
			});
		});

		resetBtn.setOnMouseClicked(event -> {
			try {
				phieuNhapId.setText(generateId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			createDateField.setText(LocalDateTime.now().format(formatter));

			productField.getItems().clear();
			for (SanPham sanPham : listProductInit) {
				String displayText = sanPham.getMaSanPham() + " - " + sanPham.getTenSanPham();
				productField.getItems().add(displayText);
			}

			supplierSelect.getItems().clear();
			listSupplier = new NhaCungCap_BUS().getAllNhaCungCap();
			for (NhaCungCap supplier : listSupplier) {
				String displayText = supplier.getMaNCC() + " - " + supplier.getTenNCC();
				supplierSelect.getItems().add(displayText);
			}
		});
	}

	@FXML
	public void resetFormProduct() {
		resetBtnAddProduct.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(resetBtn, 1, 0.5, 200, () -> {
			});
		});

		resetBtnAddProduct.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(resetBtn, 0.5, 1, 200, () -> {
			});
		});

		resetBtnAddProduct.setOnMouseClicked(event -> {
			// set up create product
			DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

			manufactureDateField.setConverter(new StringConverter<LocalDate>() {
				@Override
				public String toString(LocalDate date) {
					return date != null ? formatterDate.format(date) : "";
				}

				@Override
				public LocalDate fromString(String string) {
					return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatterDate) : null;
				}
			});

			manufactureDateField.setValue(LocalDate.now());

			expirationDateField.setConverter(new StringConverter<LocalDate>() {
				@Override
				public String toString(LocalDate date) {
					return date != null ? formatterDate.format(date) : "";
				}

				@Override
				public LocalDate fromString(String string) {
					return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatterDate) : null;
				}
			});

			expirationDateField.setValue(LocalDate.now().plusDays(1));
			nameField.setText("");
			manufacturerField.setText("");
			productTypeField.setValue(null);
			categoryField.setValue(null);
			unitField.setValue(null);
		});
	}

	@SuppressWarnings("unused")
	@FXML
	public void handleBackBtnClick() {
		backBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(backBtn, 1, 0.5, 200, () -> {
			});
		});

		backBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(backBtn, 0.5, 1, 200, () -> {
			});
		});

		backBtn.setOnMouseClicked(event -> {
			try {
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/PhieuNhap_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);
			} catch (IOException | java.io.IOException e) {
				e.printStackTrace();
			}
		});
	}

	@SuppressWarnings("unused")
	@FXML
	public void handleRenderAddedProductsTable() {
		productTable.setItems(addedProductList);

		maSanPham.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
		tenSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
		soLuongNhap.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
		donGiaNhap.setCellValueFactory(new PropertyValueFactory<>("donGia"));
		thue.setCellValueFactory(new PropertyValueFactory<>("thue"));
		thanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien")); // Giữ kiểu Double

		// Định dạng khi hiển thị
		thanhTien.setCellFactory(col -> new TableCell<ChiTietPhieuNhap, Double>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
					setText(formatter.format(item)); // Định dạng thành tiền
				}
			}
		});

		ngaySanXuat.setCellValueFactory(new PropertyValueFactory<>("ngaySX"));
		ngayHetHan.setCellValueFactory(new PropertyValueFactory<>("hanSuDung"));
		ghiChuColumn.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));

		updateTotalProductCount();
		updateTotalAmount();
	}

	public void updateTotalProductCount() {
		int totalCount = 0;

		for (ChiTietPhieuNhap productDetail : addedProductList) {
			totalCount += productDetail.getSoLuong();
		}

		// Cập nhật văn bản của Text
		totalProduct.setText(String.valueOf(totalCount));
	}

	private void updateTotalAmount() {
		double totalAmount = calculateTotalAmount();

		// Định dạng tổng tiền thành tiền Việt Nam
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
		totalPrice.setText(formatter.format(totalAmount)); // Hiển thị tổng tiền
	}

	private double calculateTotalAmount() {
		double total = 0.0;

		for (ChiTietPhieuNhap item : addedProductList) {
			double donGia = item.getDonGia();
			int soLuong = item.getSoLuong();
			float thue = item.getThue();

			// Tính thành tiền cho từng sản phẩm
			double thanhTien = donGia * soLuong * (1 + thue); // Nếu thue là tỷ lệ phần trăm

			total += thanhTien; // Cộng dồn vào tổng
		}

		return total; // Trả về tổng tiền
	}

	// edit on table product
	@FXML
	public void handleEditableProductTable() {
		setFloatComboBoxColumnEditable(thue, "thue", new String[] { "0%", "5%", "10%", "15%", "20%" });

		soLuongNhap.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		soLuongNhap.setOnEditCommit(event -> {
			ChiTietPhieuNhap ctpn = event.getRowValue();
			ctpn.setSoLuong(event.getNewValue());
			updateTotalProductCount();
			updateTotalAmount();
		});

		setDateColumnEditable(ngaySanXuat, "ngaySX");
		setDateColumnEditable(ngayHetHan, "hanSuDung");

		donGiaNhap.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		donGiaNhap.setOnEditCommit(event -> {
			ChiTietPhieuNhap ctpn = event.getRowValue();
			ctpn.setDonGia(event.getNewValue());
			updateTotalProductCount();
			updateTotalAmount();
		});

		tenSanPham.setCellFactory(TextFieldTableCell.forTableColumn());
		tenSanPham.setOnEditCommit(event -> {
			ChiTietPhieuNhap ctpn = event.getRowValue();
			if ("Sản phẩm mới".equals(ctpn.getGhiChu())) { // Kiểm tra ghi chú
				ctpn.getSanPham().setTenSanPham(event.getNewValue());
			} else {
				showErrorDialog("Lỗi chỉnh sửa sản phẩm", "Chỉ \"sản phẩm mới\" được phép chỉnh sửa tên.");
				productTable.refresh();
			}
		});

	}

	private void setDateColumnEditable(TableColumn<ChiTietPhieuNhap, LocalDate> column, String property) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		column.setCellFactory(col -> new TableCell<ChiTietPhieuNhap, LocalDate>() {
			private final DatePicker datePicker = new DatePicker();

			{
				datePicker.setEditable(true);

				// Lắng nghe sự kiện khi người dùng chọn ngày và commit giá trị
				datePicker.setOnAction(event -> commitEdit(datePicker.getValue()));

				// Lắng nghe sự kiện mất focus (người dùng nhấn ra ngoài)
				datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
					if (!isNowFocused && isEditing()) {
						commitEdit(datePicker.getValue()); // Commit giá trị khi mất focus
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
						setText(item == null ? "" : formatter.format(item));
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
				setText(getItem() == null ? "" : formatter.format(getItem()));
				setGraphic(null);
			}
		});

		// Xử lý khi người dùng chọn ngày hoặc mất focus
		column.setOnEditCommit(event -> {
			ChiTietPhieuNhap ctpn = event.getRowValue();
			Object newValue = event.getNewValue();

			// Cập nhật ngày sản xuất hoặc hạn sử dụng tùy theo cột được chỉnh sửa
			if ("ngaySX".equals(property)) {
				ctpn.setNgaySX((LocalDate) newValue); // Cập nhật ngày sản xuất
			} else if ("hanSuDung".equals(property)) {
				ctpn.setHanSuDung((LocalDate) newValue); // Cập nhật hạn sử dụng
			} else {
				// Có thể thông báo lỗi nếu không có trường ngày nào phù hợp
				System.out.println("Không có trường ngày phù hợp với property: " + property);
			}
		});
	}

	private void setFloatComboBoxColumnEditable(TableColumn<ChiTietPhieuNhap, Float> column, String property,
			String[] options) {
		column.setCellFactory(col -> new TableCell<ChiTietPhieuNhap, Float>() {
			private final ComboBox<String> comboBox = new ComboBox<>();

			{
				comboBox.setEditable(true);
				comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
					if (!isNowFocused && isEditing()) {
						cancelEdit();
					}
				});

				comboBox.setOnAction(event -> {
					commitEdit(parsePercentageString(comboBox.getValue()));
				});

				comboBox.getItems().addAll(options);
			}

			@Override
			protected void updateItem(Float item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setText(null);
					setGraphic(null);
				} else {
					if (isEditing()) {
						comboBox.setValue(String.valueOf(getItem() * 100).replace(".0", "") + "%");
						setGraphic(comboBox);
						setText(null);
					} else {
						setText(item == null ? "" : String.valueOf(item * 100).replace(".0", "") + "%");
						setGraphic(null);
					}
				}
			}

			@Override
			public void startEdit() {
				super.startEdit();
				comboBox.setValue(String.valueOf(getItem() * 100).replace(".0", "") + "%");
				setGraphic(comboBox);
				setText(null);
				comboBox.requestFocus();
			}

			@Override
			public void cancelEdit() {
				super.cancelEdit();
				setText(getItem() == null ? "" : String.valueOf(getItem() * 100).replace(".0", "") + "%");
				setGraphic(null);
			}
		});

		column.setOnEditCommit(event -> {
			ChiTietPhieuNhap ctpn = event.getRowValue();
			ctpn.setThue(event.getNewValue());
			updateTotalProductCount();
			updateTotalAmount();
		});
	}

	public Float parsePercentageString(String percentage) {
		try {
			String numericPart = percentage.replace("%", "").trim();
			return Float.parseFloat(numericPart) / 100;
		} catch (NumberFormatException e) {
			System.err.println("Invalid percentage format: " + percentage);
			return null;
		}
	}

	private String generateId() throws SQLException {
		int count = new PhieuNhap_BUS().countPhieuNhap();
		String id = String.format("PN%04d", count + 1);

		return id;
	}

	private String generateIdSanPham() throws SQLException {
		int count = new SanPham_BUS().countSanPham();

		long newProductCount = addedProductList.stream()
				.filter(ctpn -> "Sản phẩm mới".equals(ctpn.getGhiChu()))
				.count();

		// Cộng tổng số để tạo ID mới
		int totalCount = count + (int) newProductCount;
		String id = String.format("SP%04d", totalCount + 1);

		return id;
	}

	// succes modal
	@SuppressWarnings("unused")
	@FXML
	private void showAddProductSuccessModal(String message) {
		Stage modalStage = new Stage();
		modalStage.setResizable(false);
		modalStage.initModality(Modality.APPLICATION_MODAL);

		ImageView icon = new ImageView(new Image(
				getClass().getClassLoader().getResource("images/tick-icon.png").toExternalForm()));
		icon.setFitWidth(40);
		icon.setFitHeight(40);

		Label messageLabel = new Label(message);
		messageLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10px;");

		Button closeButton = new Button("Đóng");
		closeButton.setStyle(
				"-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; "
						+ "-fx-border-radius: 10px; -fx-cursor: hand; -fx-padding: 8px 20px;");
		closeButton.setOnAction(e -> modalStage.close());

		closeButton.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(closeButton, 1, 0.6, 300, () -> {
			});
		});
		closeButton.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(closeButton, 0.6, 1, 300, () -> {
			});
		});

		HBox contentLayout = new HBox(10, messageLabel, icon);
		contentLayout.setStyle("-fx-alignment: center; -fx-padding: 20px;");

		VBox layout = new VBox(15, contentLayout, closeButton);
		layout.setStyle(
				"-fx-alignment: center; -fx-background-color: #ffffff; "
						+ "-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 20px;");

		Scene scene = new Scene(layout, 400, 150);
		modalStage.setScene(scene);

		modalStage.showAndWait();
	}

	private void showErrorDialog(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null); // No header text
		alert.setContentText(message);
		alert.showAndWait(); // Display the alert and wait for it to be dismissed
	}

}
