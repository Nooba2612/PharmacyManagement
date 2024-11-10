package pharmacy.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;

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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import pharmacy.bus.ChiTietPhieuNhap_BUS;
import pharmacy.bus.NhaCungCap_BUS;
import pharmacy.bus.PhieuNhap_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.ChiTietPhieuNhap;
import pharmacy.entity.NhaCungCap;
import pharmacy.entity.NhanVien;
import pharmacy.entity.PhieuNhap;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;
import javafx.scene.text.Text;

public class ThemPhieuNhapImportFile_GUI {
	@FXML
	private Button backBtn;

	@FXML
	private HBox root;

	@FXML
	private TextField createDateField;

	@FXML
	private Button submitBtn, exportIRecieptBtn;

	@FXML
	private TextField phieuNhapId;

	@FXML
	private TableView<ChiTietPhieuNhap> productTable;

	@FXML
	private TableColumn<ChiTietPhieuNhap, String> maSanPham;

	@FXML
	private TableColumn<ChiTietPhieuNhap, String> tenSanPham;

	@FXML
	private TableColumn<ChiTietPhieuNhap, LocalDate> ngaySanXuat;

	@FXML
	private TableColumn<ChiTietPhieuNhap, LocalDate> ngayHetHan;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Integer> soLuongNhap;

	// @FXML
	// private TableColumn<SanPham, String> donViTinh;

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
	private Button importButton;

	private ObservableList<ChiTietPhieuNhap> addedProductList = FXCollections.observableArrayList();

	@FXML
	private Text totalProduct, totalPrice;

	private List<NhaCungCap> listSupplier;

	@FXML
	private ComboBox<String> supplierSelect;

	@FXML
	private Label supplierAlert, employeeName;

	private NhanVien nhanVien;
	File selectedFile;

	@FXML
	public void initialize() throws SQLException {
		setUpForm();
		handleBackBtnClick();
		handleEditableProductTable();
		handleAddPhieuNhap();
		handleExportReceipt();
	}

	@FXML
	public void handleTableClick(MouseEvent event) throws java.io.IOException, SQLException {
		// Kiểm tra xem bảng có trống hay không
		if (productTable.getItems().isEmpty()) {
			// Nếu bảng trống, mở cửa sổ chọn file
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
			Stage stage = (Stage) productTable.getScene().getWindow(); // Lấy cửa sổ hiện tại
			selectedFile = fileChooser.showOpenDialog(stage); // Mở hộp thoại chọn file

			if (selectedFile != null) {
				List<ChiTietPhieuNhap> importedProducts = readProductsFromCSV(selectedFile);
				if (importedProducts != null) {
					addedProductList.clear();
					addedProductList.addAll(importedProducts);
					handleRenderAddedProductsTable(); // Cập nhật bảng với dữ liệu mới
					System.out.println("Import file thành công!");
					setupActionColumn();
				} else {
					showErrorDialog("Lỗi khi đọc file CSV",
							"Có lỗi xảy ra khi khi đọc file CSV. Vui lòng kiểm tra lại dữ liệu.");
					return;
				}
			}
		}
	}

	private List<ChiTietPhieuNhap> readProductsFromCSV(File file) throws java.io.IOException, SQLException {
		List<ChiTietPhieuNhap> productList = new ArrayList<>();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
			String[] line;
			boolean isHeader = true; // Biến để kiểm tra dòng tiêu đề
	
			while ((line = csvReader.readNext()) != null) {
				// Bỏ qua dòng tiêu đề (dòng đầu tiên)
				if (isHeader) {
					isHeader = false;
					continue;
				}

				try{
					SanPham sanpham = new SanPham();

					sanpham.setNgayTao(LocalDate.now());
					sanpham.setNgayCapNhat(LocalDateTime.now());

					sanpham.setMaSanPham(line[0].trim());
					sanpham.setTenSanPham(line[1].trim());
					if (line[2] != null && !line[2].trim().isEmpty()) {
						sanpham.setDanhMuc(line[2].trim());
					} else {
						sanpham.setDanhMuc("Unknown"); // Giá trị mặc định
					}
					if (line[3] != null && !line[3].trim().isEmpty()) {
						sanpham.setLoaiSanPham(line[3].trim());
					} else {
						sanpham.setLoaiSanPham("Unknown"); // Giá trị mặc định
					}
					if (line[5] != null && !line[5].trim().isEmpty()) {
						sanpham.setNhaSX(line[5].trim());
					} else {
						sanpham.setNhaSX("Unknown"); // Giá trị mặc định
					}

					if (line[10] != null && !line[10].trim().isEmpty()) {
						sanpham.setDonViTinh(line[10].trim());
					} else {
						sanpham.setDonViTinh("Viên"); // Giá trị mặc định
					}

					if (line[11] != null && !line[11].trim().isEmpty()) {
						sanpham.setMoTa(line[11].trim());
					} else {
						sanpham.setMoTa(""); // Giá trị mặc định nếu không có mô tả
					}

					ChiTietPhieuNhap ctpn = new ChiTietPhieuNhap();
					ctpn.setSanPham(sanpham);
						boolean productExists = new SanPham_BUS().checkProductExist(sanpham.getMaSanPham(), sanpham.getTenSanPham(), sanpham.getNhaSX());
	
					if (productExists) {
						ctpn.setGhiChu("Sản phẩm đã tồn tại");
					} else {
						ctpn.setGhiChu("Sản phẩm mới");
					}

					if (line[4] != null && !line[4].isEmpty()) {
						ctpn.setNgaySX(LocalDate.parse(line[4], dateFormatter)); // Set LocalDate
					} else {
						ctpn.setNgaySX(LocalDate.now()); // Nếu không có Ngày sản xuất, đặt giá trị mặc định
					}
	
					if (line[6] != null && !line[6].isEmpty()) {
						ctpn.setSoLuong(Integer.parseInt(line[6]));
					} else {
						ctpn.setSoLuong(1); // Mặc định nếu không có
					}

					if (line[7] != null && !line[7].isEmpty()) {
						ctpn.setDonGia(Double.parseDouble(line[7]));
					} else {
						ctpn.setDonGia(10000); // Mặc định nếu không có
					}

					if (line[8] != null && !line[8].isEmpty()) {
						ctpn.setThue(Float.parseFloat(line[8]));
					} else {
						ctpn.setThue(0.0f); // Mặc định nếu không có
					}
	
					if (line[9] != null && !line[9].isEmpty()) {
						ctpn.setHanSuDung(LocalDate.parse(line[9], dateFormatter)); // Set LocalDate
					} else {
						ctpn.setHanSuDung(LocalDate.now()); // Mặc định nếu không có
					}
	
					// Tính toán thành tiền
					double thanhTien = ctpn.getSoLuong() * ctpn.getDonGia() * (1 + ctpn.getThue() / 100);
					productList.add(ctpn);

				} catch (NumberFormatException | DateTimeParseException e) {
					// Xử lý lỗi khi phân tích dữ liệu không hợp lệ
					System.out.println("Error parsing line: " + Arrays.toString(line));
					e.printStackTrace();
				}
			}

		} catch (IOException | CsvValidationException | NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return productList;
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

		listSupplier = new NhaCungCap_BUS().getAllNhaCungCap();
		for (NhaCungCap supplier : listSupplier) {
			String displayText = supplier.getMaNCC() + " - " + supplier.getTenNCC();
			supplierSelect.getItems().add(displayText);
		}

		// Set up placeholder for empty table
		if (productTable.getItems().isEmpty()) {
			Label noProductLabel = new Label("Click để chọn file.");
			noProductLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
			productTable.setPlaceholder(noProductLabel);
		}

		// Set up action column with delete icon
		setupActionColumn();
	}

	private void setupActionColumn() {
		actionColumn.setCellFactory(col -> new TableCell<ChiTietPhieuNhap, Void>() {
			private final ImageView deleteIcon = new ImageView(new Image(
					getClass().getClassLoader().getResource("images/x-icon.png").toExternalForm()));
			private final HBox hbox = new HBox(deleteIcon);

			{
				deleteIcon.setFitHeight(15);
				deleteIcon.setFitWidth(15);
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
			showErrorDialog("Lỗi thêm phiếu nhập",
					"Có lỗi xảy ra khi thêm phiếu nhập. Sản phẩm không được rỗng.");
			return false;
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
				ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
				chiTiet.setPhieuNhap(phieuNhap);
				chiTiet.setSanPham(sp.getSanPham());
				chiTiet.setNgaySX(sp.getNgaySX());
				chiTiet.setHanSuDung(sp.getHanSuDung());
				chiTiet.setSoLuong(sp.getSoLuong());
				chiTiet.setDonGia(sp.getDonGia());
				chiTiet.setThue(sp.getThue());

				try {
					boolean existingProduct = new SanPham_BUS().checkProductExist(sp.getMaSanPham(),
							sp.getTenSanPham(), sp.getSanPham().getNhaSX());

					if (existingProduct) {
						// Nếu sản phẩm đã có trong cơ sở dữ liệu, cập nhật tồn kho
						int newQuantity = sp.getSoLuong();
						boolean isUpdated = new SanPham_BUS().updateProductStock(sp.getMaSanPham(),
								sp.getTenSanPham(), sp.getSanPham().getNhaSX(), newQuantity, connection);
						if (!isUpdated) {
							connection.rollback();
							showErrorDialog("Lỗi cập nhật tồn kho",
									"Không thể cập nhật số lượng tồn cho sản phẩm: " + sp.getMaSanPham());
							return false;
						}
					} else {
						// Nếu sản phẩm không có trong cơ sở dữ liệu, tạo sản phẩm mới
						SanPham sanPham = sp.getSanPham();
						if (sanPham == null) {
							sanPham = new SanPham();
							sp.setSanPham(sanPham);
						}

						// Đặt số lượng sản phẩm là 0
						sanPham.setSoLuongTon(chiTiet.getSoLuong());
						sanPham.setDonGiaBan(Math.round(chiTiet.getDonGia() * (1 + chiTiet.getThue() / 100)));
						sanPham.setThue(chiTiet.getThue());
						sanPham.setNgaySX(chiTiet.getNgaySX());
						sanPham.setHanSuDung(chiTiet.getHanSuDung());

						boolean isCreatedProduct = new SanPham_BUS().createSanPham(sanPham, connection);
						if (!isCreatedProduct) {
							connection.rollback();
							showErrorDialog("Lỗi tạo sản phẩm mới",
									"Không thể tạo sản phẩm mới: " + sp.getMaSanPham());
							return false;
						}
					}

					connection.commit(); // Commit nếu tất cả các thao tác đều thành công
				} catch (SQLException e) {
					try {
						connection.rollback(); // Rollback trong trường hợp có lỗi
						showErrorDialog("Lỗi giao dịch", "Đã xảy ra lỗi trong quá trình thực hiện giao dịch.");
					} catch (SQLException rollbackException) {
						e.printStackTrace(); // In lỗi rollback nếu có
					}
					e.printStackTrace(); // In lỗi chính nếu có
				}

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
			saveFileToPhieuNhapDirectory(selectedFile);

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
			e.printStackTrace();
			return;
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

		donGiaNhap.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		donGiaNhap.setOnEditCommit(event -> {
			ChiTietPhieuNhap ctpn = event.getRowValue();
			ctpn.setDonGia(event.getNewValue());
			updateTotalProductCount();
			updateTotalAmount();
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

	public void saveFileToPhieuNhapDirectory(File sourceFile) {
		// Đường dẫn tới thư mục phieuNhap trong resources
		String targetDirPath = "src/main/resources/phieuNhap";

		// Tạo thư mục nếu chưa tồn tại
		File targetDirectory = new File(targetDirPath);
		if (!targetDirectory.exists()) {
			targetDirectory.mkdirs();
		}

		// Lấy tên gốc của file và thêm thời gian hiện tại
		String fileName = sourceFile.getName();
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String targetFileName = fileName.replace(".", "_" + timestamp + ".");

		// Tạo đường dẫn tới file đích
		File targetFile = new File(targetDirectory, targetFileName);

		// Sao chép file vào thư mục phieuNhap
		try {
			try {
				Files.copy(sourceFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (java.io.IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("File đã được lưu vào: " + targetFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Không thể sao chép file vào thư mục phieuNhap.");
		}
	}
}
