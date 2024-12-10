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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pharmacy.bus.HoaDon_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.entity.ChiTietHoaDon;
import pharmacy.entity.HoaDon;
import pharmacy.entity.KhachHang;
import pharmacy.entity.SanPham;
import pharmacy.bus.ChiTietHoaDon_BUS;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;

public class HoaDonTam_GUI {

    @FXML
    private TableColumn<HoaDon, Void> actionColumn;

    @FXML
    private TableColumn<HoaDon, Double> amountPaidColumn;

    @FXML
    private TableColumn<HoaDon, Double> changeColumn;

    @FXML
    private TableColumn<HoaDon, LocalDateTime> createDateColumn;

    @FXML
    private TableColumn<HoaDon, String> customerNameColumn;

    @FXML
    private TableColumn<HoaDon, String> employeeNameColumn;

    @FXML
    private TableColumn<HoaDon, String> idColumn;

    @FXML
    private TableView<HoaDon> invoiceTable;

    @FXML
    private TableColumn<HoaDon, String> paymentMethodColumn;

    @FXML
    private HBox root;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Pane searchPane;

    @FXML
    private TableColumn<HoaDon, Double> totalColumn;

    @FXML
    private TableColumn<HoaDon, Double> usedPointsColumn;

    private final ObservableList<HoaDon> invoiceList = FXCollections.observableArrayList();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private HoaDon currentInvoice;

    @FXML
    public void initialize() throws SQLException {
        renderTable();
        setupTablePlaceholder();
    }

    @FXML
    public void renderTable() throws SQLException {
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        invoiceList.clear();
        invoiceList.addAll(new HoaDon_BUS().getHoaDonTam());

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
        handleAddActionButtonsToColumn();

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

        createDateColumn.setCellFactory(col -> new TableCell<HoaDon, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
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
    public <T> void showImportTempInvoiceConfirmationPopup(HoaDon hoaDon) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận nhập hóa đơn tạm");
        alert.setHeaderText("Nhập hóa đơn tạm");
        alert.setContentText("Mã hóa đơn tạm: " + hoaDon.getMaHoaDon());

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/tick-icon.png")));
        stage.setResizable(false);

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/images/confirmation-icon.png")));
        icon.setFitHeight(48);
        icon.setFitWidth(48);
        alert.setGraphic(icon);

        ButtonType confirmButton = new ButtonType("Xác nhận", ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Hủy", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Node confirmBtn = alert.getDialogPane().lookupButton(confirmButton);
        Node cancelBtn = alert.getDialogPane().lookupButton(cancelButton);

        confirmBtn.setStyle(
                "-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-cursor: hand;");
        cancelBtn.setStyle(
                "-fx-background-color: red; -fx-font-size: 16px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-cursor: hand;");

        confirmBtn.setOnMouseEntered(e -> {
            NodeUtil.applyFadeTransition(confirmBtn, 1, 0.6, 300, () -> {
            });
        });

        confirmBtn.setOnMouseExited(e -> {
            NodeUtil.applyFadeTransition(confirmBtn, 0.6, 1, 300, () -> {
            });
        });

        cancelBtn.setOnMouseEntered(e -> {
            NodeUtil.applyFadeTransition(cancelBtn, 1, 0.6, 300, () -> {
            });
        });

        cancelBtn.setOnMouseExited(e -> {
            NodeUtil.applyFadeTransition(cancelBtn, 0.6, 1, 300, () -> {
            });
        });

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirmButton) {
            currentInvoice = hoaDon;
            root.getScene().getWindow().hide();
        }
    }

    @FXML
    public HoaDon getCurrentInvoice() {
        return currentInvoice;
    }

    @FXML
    public void handleAddActionButtonsToColumn() {
        actionColumn.setCellFactory(column -> {
            return new TableCell<HoaDon, Void>() {
                private final Button detailButton = new Button();
                private final Button importButton = new Button();
                private final HBox buttonContainer = new HBox(2);

                {
                    Image detailImage = new Image(getClass().getResourceAsStream("/images/detail-icon.png"));
                    ImageView detailImageView = new ImageView(detailImage);
                    detailImageView.setFitWidth(23);
                    detailImageView.setFitHeight(23);
                    detailButton.setGraphic(detailImageView);
                    detailButton.setStyle("-fx-background-color: transparent;");
                    detailButton.setVisible(false);

                    detailButton.setOnAction(event -> {
                        HoaDon invoice = getTableView().getItems().get(getIndex());
                        System.out.println("Detail button clicked for HoaDon: " + invoice.getMaHoaDon());
                        handleShowDetails(invoice);
                    });

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

                    Image deleteImage = new Image(getClass().getResourceAsStream("/images/blue-import-icon.png"));
                    ImageView deleteImageView = new ImageView(deleteImage);
                    deleteImageView.setFitWidth(23);
                    deleteImageView.setFitHeight(23);
                    importButton.setGraphic(deleteImageView);
                    importButton.setStyle("-fx-background-color: transparent;");
                    importButton.setVisible(false);

                    importButton.setOnAction(event -> {
                        HoaDon invoice = getTableView().getItems().get(getIndex());
                        try {
                            showImportTempInvoiceConfirmationPopup(invoice);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                    importButton.setOnMouseEntered(event -> {
                        NodeUtil.applyFadeTransition(importButton, 1, 0.7, 300, () -> {
                        });
                        NodeUtil.applyScaleTransition(importButton, 1, 1.1, 1, 1.1, 300, () -> {
                        });
                    });
                    importButton.setOnMouseExited(event -> {
                        NodeUtil.applyFadeTransition(importButton, 0.7, 1, 300, () -> {
                        });
                        NodeUtil.applyScaleTransition(importButton, 1.1, 1, 1.1, 1, 300, () -> {
                        });
                    });

                    buttonContainer.getChildren().addAll(detailButton, importButton);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(buttonContainer);
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
                            importButton.setVisible(true);
                            NodeUtil.applyFadeTransition(buttonContainer, 0, 1, 300, () -> {
                            });
                        });
                        currentRow.setOnMouseExited(event -> NodeUtil.applyFadeTransition(buttonContainer, 1, 0, 300, () -> {
                            detailButton.setVisible(false);
                            importButton.setVisible(false);
                        }));
                    }
                }
            };
        });
    }

    @FXML
    public void handleShowDetails(HoaDon invoice) {
        try {
            exportInvoiceToPdf("src/main/resources/pdf/ChiTietHoaDon.pdf", invoice);
            PDFUtil.showPdfPreview(
                    new File(getClass().getClassLoader().getResource("pdf/ChiTietHoaDon.pdf").toURI()));
        } catch (com.itextpdf.io.exceptions.IOException | URISyntaxException | IOException | SQLException e) {
            Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void exportInvoiceToPdf(String outputPdfPath, HoaDon invoice) throws SQLException {
        try (PdfWriter writer = new PdfWriter(new FileOutputStream(outputPdfPath)); PdfDocument pdfDoc = new PdfDocument(writer); Document document = new Document(pdfDoc)) {

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

            // Invoice Header
            document.add(new Paragraph("Hóa Đơn Bán Hàng")
                    .setFont(font)
                    .setFontSize(14)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20));

            // Customer and Date Information
            document.add(
                    new Paragraph("Thu ngân: " + new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap().getHoTen())
                            .setFont(font)
                            .setFontSize(10)
                            .setMarginTop(10));

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            String formattedDateTime = now.format(dateTimeFormatter);

            document.add(new Paragraph("Ngày: " + formattedDateTime)
                    .setFont(font)
                    .setFontSize(10));

            // Table Header
            Table table = new Table(new float[]{1, 3, 1, 1, 2, 2});
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(new Cell().add(new Paragraph("STT").setFont(font).setFontSize(8))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Tên thuốc").setFont(font).setFontSize(8))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("SL").setFont(font).setFontSize(8))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Đơn giá").setFont(font).setFontSize(8))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Hạn sử dụng").setFont(font).setFontSize(8))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));
            table.addHeaderCell(new Cell().add(new Paragraph("Thành Tiền").setFont(font).setFontSize(8))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY));

            double totalInvoiceAmount = 0;

            for (int i = 0; i < invoice.getChiTietHoaDonList().size(); i++) {
                ChiTietHoaDon detail = invoice.getChiTietHoaDonList().get(i);
                SanPham product = new SanPham_BUS().getSanPhamByMaSanPham(detail.getMaSanPham());
                double itemTotal = product.getDonGiaBan() * detail.getSoLuong();
                totalInvoiceAmount += itemTotal;

                table.addCell(new Paragraph(String.valueOf(i + 1)).setFont(font).setFontSize(8));
                table.addCell(new Paragraph(product.getTenSanPham()).setFont(font).setFontSize(8));
                table.addCell(new Paragraph(String.valueOf(detail.getSoLuong())).setFont(font).setFontSize(8));
                table.addCell(new Paragraph(String.valueOf(String.format("%,.0f", product.getDonGiaBan())))
                        .setFont(font).setFontSize(8));
                table.addCell(new Paragraph(String.valueOf(formatter.format(product.getHanSuDung()))).setFont(font)
                        .setFontSize(8));
                table.addCell(
                        new Paragraph(String.valueOf(String.format("%,.0f", itemTotal))).setFont(font).setFontSize(8));
            }

            document.add(table.setMarginTop(20));

            int productQuantity = 0;
            for (ChiTietHoaDon cthd : invoice.getChiTietHoaDonList()) {
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

            document.add(new Paragraph("Phương thức thanh toán: " + invoice.getLoaiThanhToan())
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("Tiền khách trả: " + String.format("%,.0f", invoice.getTienKhachDua()) + " VND")
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("Tiền thừa: " + String.format("%,.0f", invoice.getTienThua()) + " VND")
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.RIGHT));

            KhachHang customer = invoice.getKhachHang();
            document.add(new Paragraph("Tổng điểm thành viên hiện tại: " + customer.getDiemTichLuy())
                    .setFont(font)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginBottom(20));
        } catch (IOException | URISyntaxException e) {
            Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, e);
        }

        System.out.println("\n\nPDF generated at: " + outputPdfPath + "\n\n");
    }

    @FXML
    public void setupTablePlaceholder() {
        Label noContentLabel = new Label("Không có hóa đơn tạm.");
        noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
        invoiceTable.setPlaceholder(noContentLabel);
    }
}
