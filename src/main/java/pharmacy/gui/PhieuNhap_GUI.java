package pharmacy.gui;

import java.util.logging.Level;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

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

import java.util.Locale;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.util.Duration;
import javafx.util.converter.LocalDateStringConverter;
import pharmacy.bus.PhieuNhap_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.entity.ChiTietPhieuNhap;
import pharmacy.entity.PhieuNhap;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;

public class PhieuNhap_GUI {

    @FXML
    private Button addPhieuNhapBtn;

	@FXML
    private Button importAddButton;

    @FXML
    private TableColumn<PhieuNhap, String> idColumn;

    @FXML
    private TableColumn<PhieuNhap, String> supplierColumn;

    @FXML
    private TableColumn<PhieuNhap, LocalDateTime> createDateColumn;

    @FXML
    private TableColumn<PhieuNhap, String> employeeNameColumn;

    @FXML
    private TableColumn<PhieuNhap, Integer> tongSoLuongColumn;

    @FXML
    private TableColumn<PhieuNhap, Double> tongTienColumn;

    @FXML
    private TableColumn<PhieuNhap, Void> chiTietPhieuNhapColumn;

    @FXML
    private ComboBox<String> filter;

    @FXML
    private DatePicker fromDate;

    @FXML
    private TableView<PhieuNhap> phieuNhapTable;

    private ObservableList<PhieuNhap> phieuNhapList;


    @FXML
    private HBox root;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Pane searchPane;

    @FXML
    private DatePicker toDate;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @FXML
    public void initialize() throws SQLException {
		addPhieuNhapBtn.setOnAction(event -> {
            try {
                Parent addPhieuNhapFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemPhieuNhap_GUI.fxml"));
                root.getChildren().clear();
                root.getChildren().add(addPhieuNhapFrame);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Nút thêm phiếu nhập từ file
        importAddButton.setOnAction(event -> {
            try {
                Parent addPhieuNhapFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemPhieuNhapImportFile_GUI.fxml"));
                root.getChildren().clear();
                root.getChildren().add(addPhieuNhapFrame);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

		initFormatDate();
        setUpPhieuNhapTableAction();
        handleSearchPhieuNhapAction();
    }

     @FXML
    public void setUpPhieuNhapTableAction() throws SQLException {
        renderPhieuNhaps();
        setupTablePlaceholder();
        handleAddDetailButtonToColumn();
        handleFilterPhieuNhapByDate();
    }

    @FXML
    public void renderPhieuNhaps() throws SQLException {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("maPhieuNhap"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("tenNhaCungCap"));
        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("thoiGianNhap"));
        createDateColumn.setCellFactory(column -> new TableCell<PhieuNhap, LocalDateTime>() {
        @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("tenNhanVien"));
        tongSoLuongColumn.setCellValueFactory(new PropertyValueFactory<>("tongSoLuong"));

        tongTienColumn.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        tongTienColumn.setCellFactory(col -> new TableCell<PhieuNhap, Double>() {
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
        // chiTietPhieuNhapColumn.setCellValueFactory(new PropertyValueFactory<>("tongTien"));

        phieuNhapList = FXCollections.observableArrayList(new PhieuNhap_BUS().getAllPhieuNhap());
//         for (PhieuNhap phieuNhap : phieuNhapList) {
//     System.out.println("Mã phiếu nhập: " + phieuNhap.getMaPhieuNhap());
//     System.out.println("Nhân viên: " + phieuNhap.getNhanVien().getHoTen());
//     System.out.println("Nhà cung cấp: " + phieuNhap.getNhaCungCap().getTenNCC());
//     System.out.println("Thời gian nhập: " + phieuNhap.getThoiGianNhap());
    
//     // In thêm chi tiết phiếu nhập (nếu cần)
//     List<ChiTietPhieuNhap> chiTietList = phieuNhap.getChiTietPhieuNhapList();
//     for (ChiTietPhieuNhap chiTiet : chiTietList) {
//         System.out.println(" - Sản phẩm: " + chiTiet.getSanPham().getTenSanPham());
//         System.out.println(" - Số lượng: " + chiTiet.getSoLuong());
//         System.out.println(" - Đơn giá: " + chiTiet.getDonGia());
//     }
    
//     System.out.println("----------------------------------------------------");
// }
        phieuNhapTable.setItems(phieuNhapList);

        // handleSearchSanPhamAction(productList);
    }

	@FXML
public void handleAddDetailButtonToColumn() {
    chiTietPhieuNhapColumn.setCellFactory(column -> {
        return new TableCell<PhieuNhap, Void>() {
            private final Button detailButton = new Button();

            {
                // Handle detail button actions
                detailButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");

                detailButton.setOnAction(event -> {
                    PhieuNhap phieuNhap = getTableView().getItems().get(getIndex());
                    System.out.println("Detail button clicked for PhieuNhap: " + phieuNhap.getMaPhieuNhap());
                    handleShowDetails(phieuNhap);
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
                    TableRow<PhieuNhap> currentRow = getTableRow();

                    currentRow.setOnMouseEntered(event -> {
                        detailButton.setVisible(true);
                        NodeUtil.applyFadeTransition(detailButton, 0, 1, 300, () -> {
                        });
                    });
                    currentRow.setOnMouseExited(event -> NodeUtil.applyFadeTransition(detailButton, 1, 0, 300, () -> {
                        detailButton.setVisible(false);
                    }));
                }
            }
        };
    });
}

	@FXML
	public void handleShowDetails(PhieuNhap phieuNhap) {
		File pdfFile = new File("src/main/resources/pdf/ChiTietPhieuNhap.pdf");
		if (pdfFile.exists()) {
			pdfFile.delete();
		}

		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					exportInvoiceToPdf("src/main/resources/pdf/ChiTietPhieuNhap.pdf", phieuNhap);

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
	}



    private void exportInvoiceToPdf(String outputPdfPath, PhieuNhap phieuNhap) throws SQLException {
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

    @FXML
	public void handleSearchPhieuNhapAction() {
		FilteredList<PhieuNhap> filteredList = new FilteredList<>(phieuNhapList, b -> true);

		searchBtn.setOnAction(event -> {
			filteredList.setPredicate(phieuNhap -> {
				if (searchField.getText() == null || searchField.getText().isEmpty()) {
					return true;
				}

				String lowerCaseFilter = searchField.getText().toLowerCase();
				return phieuNhap.getMaPhieuNhap().toLowerCase().contains(lowerCaseFilter) ||
						phieuNhap.getTenNhaCungCap().toLowerCase().contains(lowerCaseFilter) ||
						phieuNhap.getTenNhanVien().toLowerCase().contains(lowerCaseFilter);
                        // phieuNhap.getTongSoLuong().contains(lowerCaseFilter) ||
                        // phieuNhap.getTenNhanVien().toLowerCase().contains(lowerCaseFilter);

			});
		});

		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(phieuNhap -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();
				return phieuNhap.getMaPhieuNhap().toLowerCase().contains(lowerCaseFilter) ||
						phieuNhap.getTenNhaCungCap().toLowerCase().contains(lowerCaseFilter) ||
						phieuNhap.getTenNhanVien().toLowerCase().contains(lowerCaseFilter);

			});
		});

		phieuNhapTable.setItems(filteredList.isEmpty() ? phieuNhapList : filteredList);
	}

    @FXML
	void handleFilterPhieuNhapByDate() {
		fromDate.setValue(LocalDate.now().withDayOfMonth(1));
		toDate.setValue(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));

		fromDate.valueProperty().addListener((observable, oldValue, newValue) -> {
			phieuNhapTable.setItems(FXCollections.observableArrayList(
            		new PhieuNhap_BUS().getPhieuNhapByDate(fromDate.getValue(), toDate.getValue())));

            showLoadingAnimation();

            new Thread(() -> {
				try {
					// Lấy dữ liệu đã lọc theo ngày
					var filteredData = new PhieuNhap_BUS().getPhieuNhapByDate(fromDate.getValue(), toDate.getValue());
		
					// Cập nhật bảng trên UI thread sau khi truy vấn xong
					Platform.runLater(() -> {
						phieuNhapTable.setItems(FXCollections.observableArrayList(filteredData));
					});
		
					// Cập nhật lại sản phẩm trong một luồng nền khác nếu cần
					new SanPham_BUS().refreshSanPham();
		
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}).start();
		});

		toDate.valueProperty().addListener((observable, oldValue, newValue) -> {
			phieuNhapTable.setItems(FXCollections.observableArrayList(
            		new PhieuNhap_BUS().getPhieuNhapByDate(fromDate.getValue(), toDate.getValue())));

            showLoadingAnimation();

            new Thread(() -> {
            	try {
            		try {
            			Thread.sleep(800);
            		} catch (InterruptedException e) {
            			e.printStackTrace();
            		}
            		new SanPham_BUS().refreshSanPham();

            		Platform.runLater(() -> {
            			phieuNhapTable
                        		.setItems(FXCollections.observableArrayList(new PhieuNhap_BUS().getAllPhieuNhap()));
                        setupTablePlaceholder();
            		});

            	} catch (SQLException e) {
            		e.printStackTrace();
            	}
            }).start();
		});
	}

	@FXML
	void initFormatDate() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Set up a TextFormatter for fromDate and toDate
        fromDate.setEditable(true);
        toDate.setEditable(true);

        fromDate.setConverter(new LocalDateStringConverter(dateFormatter, dateFormatter));
        toDate.setConverter(new LocalDateStringConverter(dateFormatter, dateFormatter));
        
        // Set initial values
        fromDate.setValue(LocalDate.now().withDayOfMonth(1));
        toDate.setValue(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
	}

    @FXML
	public void showLoadingAnimation() {
		phieuNhapList.clear();
		phieuNhapTable.getItems().clear();

		ImageView loadingImageView = new ImageView(
				new Image(getClass().getClassLoader().getResource("images/loading-icon.png").toString()));
		loadingImageView.setFitHeight(50);
		loadingImageView.setFitWidth(50);
		phieuNhapTable.setPlaceholder(loadingImageView);

		RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.8), loadingImageView);
		rotateTransition.setCycleCount(Animation.INDEFINITE);
		rotateTransition.setFromAngle(0);
		rotateTransition.setToAngle(360);
		rotateTransition.play();
	}

    @FXML
	public void setupTablePlaceholder() {
		Label noContentLabel = new Label("Không có hóa đơn.");
		noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
		phieuNhapTable.setPlaceholder(noContentLabel);
	}
}
