package pharmacy.gui;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pharmacy.entity.ChiTietHoaDon;
import pharmacy.entity.HoaDon;
import pharmacy.utils.NodeUtil;

public class InHoaDon_GUI {

    @FXML
    private Label change;

    @FXML
    private Label checkoutPrice;

    @FXML
    private Label createDate;

    @FXML
    private Label currentLoyalPoints;

    @FXML
    private Label employeeName;

    @FXML
    private TableColumn<ChiTietHoaDon, LocalDate> expirationColumn;

    @FXML
    private Button exportListBtn;

    @FXML
    private Label givenMoney;

    @FXML
    private Pane root;

    @FXML
    private TableColumn<ChiTietHoaDon, String> nameColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, Integer> numberColumn;

    @FXML
    private Label paymentMethod;

    @FXML
    private TableColumn<ChiTietHoaDon, Double> priceColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, Integer> quantityColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, String> dosageColumn;

    @FXML
    private Button rejectBtn;

    @FXML
    private Label totalAmountProduct;

    @FXML
    private Label customerName;

    @FXML
    private TableColumn<ChiTietHoaDon, Double> totalColumn;

    @FXML
    private TableView<ChiTietHoaDon> detailInvoiceTable;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @FXML
    public void initialize(HoaDon hoaDon) {


        rejectBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(rejectBtn, 1, 0.7, 300, () -> {
            });
        });

        rejectBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(rejectBtn, 0.7, 1, 300, () -> {
            });
        });

        exportListBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(exportListBtn, 1, 0.7, 300, () -> {
            });
        });

        exportListBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(exportListBtn, 0.7, 1, 300, () -> {
            });
        });

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        employeeName.setText(hoaDon.getNhanVien().getHoTen());
        createDate.setText(formatter.format(hoaDon.getNgayTao()));
        customerName.setText(hoaDon.getKhachHang().getHoTen());

        System.out.println(hoaDon.getChiTietHoaDonList());
        setUpTable(hoaDon);

        int tongSoLuong = hoaDon.getChiTietHoaDonList()
                .stream()
                .mapToInt(ChiTietHoaDon::getSoLuong)
                .sum();

        totalAmountProduct.setText(String.valueOf(tongSoLuong));
        checkoutPrice.setText(decimalFormat.format(hoaDon.getTongTien()).replace(".0", ""));
        paymentMethod.setText(hoaDon.getLoaiThanhToan());
        givenMoney.setText(String.valueOf(hoaDon.getTienKhachDua()).replace(".0", ""));
        change.setText(String.valueOf(Math.ceil(hoaDon.getTienThua())).replace(".0", ""));
        currentLoyalPoints.setText(String.valueOf(hoaDon.getKhachHang().getDiemTichLuy()));

        rejectBtn.setOnAction(event -> {
            rejectBtn.getScene().getWindow().hide();
        });

        handlePrintInvoice(hoaDon);
    }

    @FXML
    public void setUpTable(HoaDon hoaDon) {
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        numberColumn.setCellValueFactory(clbck
                -> new ReadOnlyObjectWrapper<>(detailInvoiceTable.getItems().indexOf(clbck.getValue()) + 1)
        );
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("donGiaBan"));
        expirationColumn.setCellValueFactory(new PropertyValueFactory<>("hanSuDung"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("tongTien"));
        dosageColumn.setCellValueFactory(new PropertyValueFactory<>("lieuLuong"));

        expirationColumn.setCellFactory(col -> new TableCell<ChiTietHoaDon, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(dateFormatter.format(item));
                }
            }
        });

        detailInvoiceTable.setItems(FXCollections.observableArrayList(hoaDon.getChiTietHoaDonList()));
    }

    @FXML
    public void handlePrintInvoice(HoaDon hoaDon) {
        exportListBtn.setOnAction(event -> {
            try {
                String invoiceContent = generateInvoiceContent(hoaDon);
                Text printText = new Text(invoiceContent);
                PrinterJob printerJob = PrinterJob.createPrinterJob();

                if (printerJob != null) {
                    boolean proceed = printerJob.showPrintDialog(exportListBtn.getScene().getWindow());
                    if (proceed) {
                        boolean printed = printerJob.printPage(printText);
                        if (!printed) {
                            showErrorDialog("Có lỗi xảy ra khi in hóa đơn.");
                        }
                    }
                    printerJob.endJob();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showErrorDialog("Có lỗi xảy ra khi xuất hóa đơn.");
            }
        });
    }

    private String generateInvoiceContent(HoaDon hoaDon) {
        return "HoaDon{" + "maHoaDon='" + hoaDon.getMaHoaDon() + '\'' + ", khachHang=" + hoaDon.getKhachHang() + ", nhanVien=" + hoaDon.getNhanVien()
                + ", ngayTao=" + hoaDon.getNgayTao() + ", tienKhachDua=" + hoaDon.getTienKhachDua() + ", diemSuDung=" + hoaDon.getDiemSuDung()
                + ", loaiThanhToan='" + hoaDon.getLoaiThanhToan() + '\'' + ", chiTietHoaDonList=" + hoaDon.getChiTietHoaDonList() + '}';
    }

    // public void printPdf(String filePath) {
    //     try {
    //         // Mở tệp PDF
    //         File pdfFile = new File(filePath);
    //         PDDocument document = PDDocument.load(pdfFile);
    //         // Lấy dịch vụ in mặc định
    //         PrintService printService = PDFPrintService.getPrintService();
    //         if (printService != null) {
    //             PDFPrintable printable = new PDFPrintable(document);
    //             PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
    //             // In toàn bộ trang
    //             printable.print(printService.createPrintJob(), attributes);
    //             System.out.println("Tệp PDF đã được in thành công.");
    //         } else {
    //             System.out.println("Không tìm thấy máy in.");
    //         }
    //         // Đóng tài liệu PDF sau khi in
    //         document.close();
    //     } catch (IOException | PrinterException e) {
    //         e.printStackTrace();
    //     }
    // }
    @FXML
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/images/alert-icon.png")));
        icon.setFitHeight(48);
        icon.setFitWidth(48);
        alert.setGraphic(icon);

        alert.getButtonTypes().clear();

        ButtonType confirmButton = new ButtonType("Xác nhận");
        alert.getButtonTypes().add(confirmButton);

        Node confirmBtn = alert.getDialogPane().lookupButton(confirmButton);
        confirmBtn.setStyle(
                "-fx-background-color: #339933; -fx-font-size: 16px; -fx-text-fill: white; -fx-border-radius: 10px; -fx-cursor: hand;");

        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/alert-icon.png")));

        alert.showAndWait();
    }

}
