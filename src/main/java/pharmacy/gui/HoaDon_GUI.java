package pharmacy.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.commons.bouncycastle.asn1.IASN1EncodableVector;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import javafx.collections.FXCollections;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.util.Duration;
import pharmacy.entity.*;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;
import javafx.scene.Node;
import javafx.scene.Parent;
import pharmacy.bus.HoaDon_BUS;
import pharmacy.bus.SanPham_BUS;

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
	private TableColumn<HoaDon, Double> usedPointsColumn;

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
	private Button refreshBtn;

	@FXML
	private Pane searchPane;

	@FXML
	private HBox root;

	private ObservableList<HoaDon> invoiceList;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@FXML
	public void initialize() throws SQLException {
		handleInvoiceTableAction();
	}

	@FXML
	public void handleInvoiceTableAction() throws SQLException {
		setUpTable();
		setupTablePlaceholder();
		handleFilterInvoiceByDate();
		handleRefreshBtn();
		handleSearchHoaDonAction();
	}

	@FXML
	public void setUpTable() throws SQLException {
		DecimalFormat decimalFormat = new DecimalFormat("#.#");
		invoiceList = FXCollections.observableArrayList(new HoaDon_BUS().getAllHoaDon());

		idColumn.setCellValueFactory(new PropertyValueFactory<>("maHoaDon"));
		customerNameColumn
				.setCellValueFactory(
						cellData -> new SimpleStringProperty(cellData.getValue().getKhachHang().getHoTen()));
		employeeNameColumn
				.setCellValueFactory(
						cellData -> new SimpleStringProperty(cellData.getValue().getNhanVien().getHoTen()));
		createDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngayTao"));
		totalColumn.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
		amountPaidColumn.setCellValueFactory(new PropertyValueFactory<>("tienKhachDua"));
		changeColumn.setCellValueFactory(new PropertyValueFactory<>("tienThua"));
		paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("loaiThanhToan"));
		usedPointsColumn.setCellValueFactory(new PropertyValueFactory<>("diemSuDung"));
		handleAddDetailButtonToColumn();

		totalColumn.setCellFactory(column -> new TableCell<HoaDon, Double>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(decimalFormat.format(item));
				}
			}
		});

		amountPaidColumn.setCellFactory(column -> new TableCell<HoaDon, Double>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(decimalFormat.format(item));
				}
			}
		});

		usedPointsColumn.setCellFactory(column -> new TableCell<HoaDon, Double>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(decimalFormat.format(item));
				}
			}
		});

		changeColumn.setCellFactory(column -> new TableCell<HoaDon, Double>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					setText(decimalFormat.format(item));
				}
			}
		});

		createDateColumn.setCellFactory(col -> new TableCell<HoaDon, LocalDate>() {
			@Override
			protected void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText("");
				} else {
					setText(formatter.format(item));
				}
			}
		});

		invoiceTable.setItems(invoiceList);
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
						HoaDon invoice = getTableView().getItems().get(getIndex());
						System.out.println("Detail button clicked for HoaDon: " + invoice.getMaHoaDon());
						handleShowDetails(invoice);
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
		FilteredList<HoaDon> filteredList = new FilteredList<>(invoiceList, b -> true);

		searchBtn.setOnAction(event -> {
			filteredList.setPredicate(invoice -> {
				if (searchField.getText() == null || searchField.getText().isEmpty()) {
					return true;
				}

				String lowerCaseFilter = searchField.getText().toLowerCase();
				return invoice.getMaHoaDon().toLowerCase().contains(lowerCaseFilter) ||
						invoice.getKhachHang().getMaKhachHang().toLowerCase().contains(lowerCaseFilter) ||
						invoice.getNhanVien().getMaNhanVien().toLowerCase().contains(lowerCaseFilter);

			});
		});

		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(invoice -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();
				return invoice.getMaHoaDon().toLowerCase().contains(lowerCaseFilter) ||
						invoice.getKhachHang().getMaKhachHang().toLowerCase().contains(lowerCaseFilter) ||
						invoice.getNhanVien().getMaNhanVien().toLowerCase().contains(lowerCaseFilter);

			});
		});

		invoiceTable.setItems(filteredList.isEmpty() ? invoiceList : filteredList);
	}

	@FXML
	public void handleRefreshBtn() {
		refreshBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(refreshBtn, 1, 0.7, 300, () -> {
			});
		});

		refreshBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(refreshBtn, 0.7, 1, 300, () -> {
			});
		});

		refreshBtn.setOnAction(event -> {
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
						try {
							invoiceTable.setItems(FXCollections.observableArrayList(new HoaDon_BUS().getAllHoaDon()));
							setupTablePlaceholder();

						} catch (SQLException e) {
							e.printStackTrace();
						}
					});

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}).start();
		});
	}

	@FXML
	public void showLoadingAnimation() {
		invoiceList.clear();
		invoiceTable.getItems().clear();

		ImageView loadingImageView = new ImageView(
				new Image(getClass().getClassLoader().getResource("images/loading-icon.png").toString()));
		loadingImageView.setFitHeight(50);
		loadingImageView.setFitWidth(50);
		invoiceTable.setPlaceholder(loadingImageView);

		RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.8), loadingImageView);
		rotateTransition.setCycleCount(Animation.INDEFINITE);
		rotateTransition.setFromAngle(0);
		rotateTransition.setToAngle(360);
		rotateTransition.play();
	}

	@FXML
	void handleFilterInvoiceByDate() {
		fromDate.setValue(LocalDate.now().withDayOfMonth(1));
		toDate.setValue(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));

		fromDate.valueProperty().addListener((observable, oldValue, newValue) -> {
			try {
				invoiceTable.setItems(FXCollections.observableArrayList(
						new HoaDon_BUS().getInvoiceByDate(fromDate.getValue(), toDate.getValue())));

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
							try {
								invoiceTable
										.setItems(FXCollections.observableArrayList(new HoaDon_BUS().getAllHoaDon()));
								setupTablePlaceholder();

							} catch (SQLException e) {
								e.printStackTrace();
							}
						});

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}).start();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		toDate.valueProperty().addListener((observable, oldValue, newValue) -> {
			try {
				invoiceTable.setItems(FXCollections.observableArrayList(
						new HoaDon_BUS().getInvoiceByDate(fromDate.getValue(), toDate.getValue())));

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
							try {
								invoiceTable
										.setItems(FXCollections.observableArrayList(new HoaDon_BUS().getAllHoaDon()));
								setupTablePlaceholder();

							} catch (SQLException e) {
								e.printStackTrace();
							}
						});

					} catch (SQLException e) {
						e.printStackTrace();
					}
				}).start();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public void handleShowDetails(HoaDon invoice) {
		try {
			exportInvoiceToPdf("src/main/resources/pdf/ChiTietHoaDon.pdf");
			PDFUtil.showPdfPreview(
					new File(getClass().getClassLoader().getResource("pdf/ChiTietHoaDon.pdf").toURI()));
		} catch (com.itextpdf.io.exceptions.IOException | URISyntaxException | IOException | SQLException e) {
			Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	private void exportInvoiceToPdf(String outputPdfPath) throws SQLException {
        try (PdfWriter writer = new PdfWriter(new FileOutputStream(outputPdfPath));
                PdfDocument pdfDoc = new PdfDocument(writer)) {

            // Aplly font
            URL fontUrl = getClass().getClassLoader().getResource("fonts/Roboto/Roboto-Regular.ttf");
            Path fontPath;
            try {
                fontPath = Path.of(fontUrl.toURI());
            } catch (URISyntaxException e) {
                throw new IOException("Invalid URI syntax for font URL", e);
            }
            PdfFont font = PdfFontFactory.createFont(Files.readAllBytes(fontPath), PdfEncodings.IDENTITY_H,
                    EmbeddingStrategy.PREFER_EMBEDDED);

            // Define primary color
            String primaryColorHex = "#339933";
            DeviceRgb primaryColor = new DeviceRgb(
                    Integer.parseInt(primaryColorHex.substring(1, 3)),
                    Integer.parseInt(primaryColorHex.substring(3, 5)),
                    Integer.parseInt(primaryColorHex.substring(5, 7)));

            // Transparent color
            Color transparentColor = new DeviceRgb(0, 0, 0);

            try (Document document = new Document(pdfDoc)) {
                // Header
                Table headerTable = new Table(2);
                headerTable.setWidth(UnitValue.createPercentValue(40))
                        .setHorizontalAlignment(HorizontalAlignment.CENTER);

                com.itextpdf.layout.element.Image logo = new com.itextpdf.layout.element.Image(
                        ImageDataFactory.create(getClass().getClassLoader().getResource("images/pharmacy-icon.png")));
                logo.scaleToFit(80, 80);
                Cell logoCell = new Cell().add(logo).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
                headerTable.addCell(logoCell);

                Paragraph name = new Paragraph("MEDKIT")
                        .setFont(font)
                        .setFontSize(30)
                        .setBold()
                        .setFontColor(primaryColor)
                        .setTextAlignment(TextAlignment.CENTER);
                Cell nameCell = new Cell().add(name).setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setBorder(Border.NO_BORDER);
                headerTable.addCell(nameCell);
                document.add(headerTable.setMarginBottom(10));

                // Title
                document.add(new Paragraph("DANH SÁCH THUỐC")
                        .setFont(font)
                        .setFontSize(20)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(20)
                        .setMarginBottom(20));

                // Table
                Table table = new Table(new float[] { 1, 2, 2, 2, 2, 2, 2, 2 });
                table.setWidth(UnitValue.createPercentValue(100));

                table.addHeaderCell(new Cell().add(new Paragraph("Mã thuốc").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Tên thuốc").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Đơn vị tính").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Nhà sản xuất").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Ngày sản xuất").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Ngày hết hạn").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Trạng thái").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Số lượng tồn").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));

//                for (HoaDon invoice : invoiceList) {
//                    table.addCell(new Paragraph(invoice.getMaSanPham()).setFont(font).setFontSize(8));
//                    table.addCell(new Paragraph(invoice.getTenSanPham()).setFont(font).setFontSize(8));
//                    table.addCell(new Paragraph(invoice.getDonViTinh()).setFont(font).setFontSize(8));
//                    table.addCell(new Paragraph(invoice.getNhaSX()).setFont(font).setFontSize(8));
//                    table.addCell(new Paragraph(invoice.getNgaySX().toString()).setFont(font).setFontSize(8));
//                    table.addCell(new Paragraph(invoice.getHanSuDung().toString()).setFont(font).setFontSize(8));
//                    table.addCell(new Paragraph(invoice.getTrangThai()).setFont(font).setFontSize(8));
//                    table.addCell(
//                            new Paragraph(String.valueOf(invoice.getSoLuongTon())).setFont(font).setFontSize(8));
//                }

                document.add(table);
            }
            System.out.println("\n\nPDF generated.\n\n");
        } catch (IOException e) {
            Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
