package pharmacy.gui;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import pharmacy.bus.ChiTietHoaDon_BUS;
import pharmacy.bus.HoaDon_BUS;
import pharmacy.bus.KhachHang_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.dao.ChiTietHoaDon_DAO;
import pharmacy.dao.KhachHang_DAO;
import pharmacy.dao.NhanVien_DAO;
import pharmacy.entity.ChiTietHoaDon;
import pharmacy.entity.HoaDon;
import pharmacy.entity.KhachHang;
import pharmacy.entity.NhanVien;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;

public class BanHang_GUI {

    @FXML
    private TableColumn<SanPham, Void> actionColumn;

    @FXML
    private Button addCustomerBtn;

    @FXML
    private Button addProductBtn;

    @FXML
    private Button importTempInvoiceBtn;

    @FXML
    private TableView<ChiTietHoaDon> addedProductTable;

    @FXML
    private Button clearDataBtn;

    @FXML
    private Label createDate;

    @FXML
    private Label customerName;

    @FXML
    private TextField customerPhoneField;

    @FXML
    private Text discountPrice;

    @FXML
    private Label employeeName;

    @FXML
    private ComboBox<String> dosageSelect;

    @FXML
    private ComboBox<String> paymentMethodSelect;

    @FXML
    private Button useMaxPointsBtn;

    @FXML
    private Button checkoutBtn;

    @FXML
    private Button saveTempBtn;

    @FXML
    private Label invoiceId;

    @FXML
    private Label loyaltyPoint;

    @FXML
    private TableView<SanPham> productTable;

    @FXML
    private HBox root;

    @FXML
    private Pane rootPane;

    @FXML
    private ComboBox<String> searchProductField;

    @FXML
    private Text totalPrice;

    @FXML
    private Text totalProduct;

    @FXML
    private Spinner<Integer> usePointField;

    @FXML
    private TableColumn<ChiTietHoaDon, String> addedProductDosageColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, LocalDate> addedProductExpirationDateColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, String> addedProductNameColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, Double> addedIdColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, Double> addedProductPriceColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, Integer> addedProductQuantityColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, String> addedProductUnitColumn;

    @FXML
    private TableColumn<SanPham, Integer> availableQuantityColumn;

    @FXML
    private TableColumn<SanPham, String> descriptionColumn;

    @FXML
    private TableColumn<ChiTietHoaDon, Double> totalColumn;

    @FXML
    private TableColumn<SanPham, String> productTypeColumn;

    @FXML
    private TableColumn<SanPham, String> categoryColumn;

    @FXML
    private TableColumn<SanPham, LocalDate> expirationDateColumn;

    @FXML
    private TableColumn<SanPham, String> idColumn;

    @FXML
    private TableColumn<SanPham, LocalDate> manufactureDateColumn;

    @FXML
    private TableColumn<SanPham, String> manufacturerColumn;

    @FXML
    private TableColumn<SanPham, String> nameColumn;

    @FXML
    private Spinner<Integer> quantityField;

    @FXML
    private TableColumn<ChiTietHoaDon, Integer> numberColumn;

    @FXML
    private TableColumn<SanPham, Double> priceColumn;

    @FXML
    private TableColumn<SanPham, String> statusColumn;

    @FXML
    private TableColumn<SanPham, Float> taxColumn;

    @FXML
    private TableColumn<SanPham, String> unitColumn;

    @FXML
    private Label productAlert;

    @FXML
    private Label quantityAlert;

    @FXML
    private Label dosageAlert;

    @FXML
    private Label phoneAlert;

    @FXML
    private Label paymentMethodAlert;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private ObservableList<ChiTietHoaDon> addedProductList = FXCollections.observableArrayList();

    private final List<ChiTietHoaDon> currentDetailInvoice = new ArrayList<>();

    private SanPham currentProduct = null;

    private double checkoutPrice = 0;

    FilteredList<String> filteredList;

    @FXML
    public void initialize() throws SQLException {
        setUpForm();
        handleSearchProduct();
        handleHoverBtn();
        handleAddProduct();
        handleSearchCustomer();
        handleSaveInvoiceTemp();
        handleCheckoutProduct();
    }

    @FXML
    public void handleCheckoutProduct() throws SQLException {
        checkoutBtn.setOnAction(event -> {
            phoneAlert.setVisible(false);

            if (currentDetailInvoice.isEmpty()) {
                showErrorDialog("Không có sản phẩm trong giỏ hàng.");
                return;
            }

            if (paymentMethodSelect.getValue() == null) {
                showErrorDialog("Chưa chọn phương thức thanh toán.");
                return;
            }

            switch (paymentMethodSelect.getValue()) {
                case "Tiền mặt" -> {
                    try {
                        handleCashPayment();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                // case "Chuyển khoản" ->
                //     handleBankTransfer();
                default ->
                    showErrorDialog("Phương thức thanh toán không hợp lệ.");
            }
        });
    }

    @FXML
    public void handleCashPayment() throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThanhToanTienMat_GUI.fxml"));
            Parent popupContent = loader.load();
            ThanhToanTienMat_GUI controller = loader.getController();

            KhachHang khachHang = null;
            if (!customerName.getText().equals("Khách hàng lẻ")) {
                khachHang = new KhachHang_BUS().getKhachHangByPhone(customerPhoneField.getText());
            } else {
                khachHang = new KhachHang_BUS().getKhachHangById("KH0000");
            }
            LocalDateTime createDateTime = LocalDateTime.parse(createDate.getText(), formatter);
            controller.setUpForm(checkoutPrice, currentDetailInvoice, khachHang, Double.parseDouble(usePointField.getValue().toString()), createDateTime);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Thêm khách hàng");
            popupStage.getIcons().add(new Image(getClass().getResource("/images/money-icon.png").toExternalForm()));
            popupStage.setScene(new Scene(popupContent));

            popupStage.setOnHidden(event -> {
                if (controller.getStatus() == true) {
                    try {
                        refreshForm();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });

            popupStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleHoverBtn() {
        rootPane.getChildren().forEach(child -> {
            if (child instanceof Button) {
                child.setOnMouseEntered(event -> {
                    NodeUtil.applyFadeTransition(child, 1, 0.7, 300, () -> {
                    });
                });

                child.setOnMouseExited(event -> {
                    NodeUtil.applyFadeTransition(child, 0.7, 1, 300, () -> {
                    });
                });
            }
        });
    }

    @FXML
    public void handleAddProduct() {
        addProductBtn.setOnAction(event -> {
            String dosage = dosageSelect.getValue();
            int quantity = quantityField.getValue();
            if (validateProduct()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon(invoiceId.getText(), currentProduct.getMaSanPham(), quantity, 0.08f, dosage, 8000);
                currentDetailInvoice.add(cthd);
                renderAddedProduct(cthd);
                searchProductField.setValue(null);
                dosageSelect.setValue(null);
                quantityField.getValueFactory().setValue(0);
                productTable.getItems().clear();
                currentProduct = null;
                try {
                    calulateTotalPrice();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    renderTotalInvoice();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void calulateTotalPrice() throws SQLException {
        if (currentDetailInvoice != null && !currentDetailInvoice.isEmpty()) {
            for (ChiTietHoaDon cthd : currentDetailInvoice) {
                if (cthd != null && cthd.getMaSanPham() != null) {
                    // SanPham sp = new SanPham_BUS().getSanPhamByMaSanPham(cthd.getMaSanPham());
                    // if (sp != null) {
                    checkoutPrice += (cthd.getDonGiaBan() * cthd.getSoLuong());
                    // - (sp.getDonGiaBan() * cthd.getSoLuong() * cthd.getThue());
                    // }
                }
            }
        }
        checkoutPrice -= usePointField.getValue();
    }

    @FXML
    public void refreshForm() throws SQLException {
        searchProductField.setValue(null);
        dosageSelect.setValue(null);
        quantityField.getValueFactory().setValue(0);
        employeeName.setText(new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap().getHoTen());
        invoiceId.setText(generateId());
        createDate.setText(formatter.format(LocalDateTime.now()));
        currentDetailInvoice.clear();
        currentProduct = null;
        productTable.getItems().clear();
        addedProductTable.getItems().clear();
        customerPhoneField.setText("");
        customerName.setText("Khách hàng lẻ");
        usePointField.getValueFactory().setValue(0);
        totalProduct.setText("0");
        totalPrice.setText("0");
        discountPrice.setText("0");
        loyaltyPoint.setText("0");
        checkoutPrice = 0;
    }

    @FXML
    public boolean validateProduct() {
        String dosage = dosageSelect.getValue();
        int quantity = quantityField.getValue();

        try {
            if (searchProductField.getValue() != null && searchProductField.getValue().length() >= 6) {
                currentProduct = new SanPham_BUS().getSanPhamByMaSanPham(searchProductField.getValue().substring(0, 6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (currentProduct == null) {
            productAlert.setVisible(true);
            showErrorDialog("Chưa nhập sản phẩm.");
            return false;
        } else {
            productAlert.setVisible(false);
        }

        if (quantity == 0) {
            quantityAlert.setVisible(true);
            showErrorDialog("Chưa nhập số lượng.");
            return false;
        } else {
            quantityAlert.setVisible(false);
        }

        if (dosage == null) {
            dosageAlert.setVisible(true);
            showErrorDialog("Chưa nhập liều lượng.");
            return false;
        } else {
            dosageAlert.setVisible(false);
        }

        return true;
    }

    @FXML
    public void handleSearchCustomer() {
        customerPhoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            KhachHang customer = null;
            try {
                customer = new KhachHang_BUS().getKhachHangByPhone(newValue);
            } catch (SQLException ex) {
            }

            if (customer != null) {
                int maxPoints = (int) (checkoutPrice * 0.1) > customer.getDiemTichLuy() ? customer.getDiemTichLuy() : (int) (checkoutPrice * 0.1);
                customerName.setText(customer.getHoTen());
                usePointField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxPoints));
                loyaltyPoint.setText(String.valueOf(customer.getDiemTichLuy()));
                useMaxPointsBtn.setOnAction(event -> {
                    usePointField.getValueFactory().setValue(maxPoints);
                    try {
                        renderTotalInvoice();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }

    @FXML
    public void setUpForm() throws SQLException {
        employeeName.setText(new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap().getHoTen());
        invoiceId.setText(generateId());
        createDate.setText(formatter.format(LocalDateTime.now()));

        quantityField.getEditor().setPromptText("Số lượng");
        quantityField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999));
        TextFormatter<String> quantityFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        });
        quantityField.getEditor().setTextFormatter(quantityFormatter);

        dosageSelect.getItems().addAll("1 lần/ngày", "2 lần/ngày", "3 lần/ngày", "4 lần/ngày", "Mỗi 6 giờ một lần", "Mỗi 8 giờ một lần", "Mỗi 12 giờ một lần", "Mỗi 24 giờ một lần");

        usePointField.getEditor().setPromptText("Số lượng");
        usePointField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999));
        TextFormatter<String> pointFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        });
        usePointField.getEditor().setTextFormatter(pointFormatter);

        TextFormatter<String> phoneFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        });
        customerPhoneField.setTextFormatter(phoneFormatter);

        loyaltyPoint.setText("0");

        setupTablePlaceholder("Chưa có thông tin sản phẩm.", productTable);
        Label noContentLabel = new Label("Chưa có sản phẩm.");
        noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
        addedProductTable.setPlaceholder(noContentLabel);

        paymentMethodSelect.getItems().addAll("Tiền mặt", "Chuyển khoản");

        productTable.setRowFactory(tv -> {
            TableRow<SanPham> row = new TableRow<>();
            row.setPrefHeight(65);
            return row;
        });

        clearDataBtn.setOnAction(event -> {
            try {
                refreshForm();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        addCustomerBtn.setOnAction(event -> {
            try {

                if (!customerPhoneField.getText().matches("^0\\d{9}$")) {
                    showErrorDialog("Chưa nhập số điện thoại.");
                    phoneAlert.setText("Chưa nhập số điện thoại.");
                    phoneAlert.setVisible(true);
                    return;
                }

                if (new KhachHang_BUS().getKhachHangByPhone(customerPhoneField.getText()) != null) {
                    showErrorDialog("Khách hàng đã tồn tại.");
                    phoneAlert.setText("Khách hàng đã tồn tại.");
                    phoneAlert.setVisible(true);
                    return;
                }

                if (customerPhoneField.getText().matches("^0\\d{9}$") && new KhachHang_BUS().getKhachHangByPhone(customerPhoneField.getText()) == null) {
                    phoneAlert.setVisible(false);

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThemKhachHangNhanh_GUI.fxml"));
                        Parent popupContent = loader.load();
                        ThemKhachHangNhanh_GUI controller = loader.getController();

                        try {
                            controller.setUpForm(customerPhoneField.getText());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setTitle("Thêm khách hàng");
                        popupStage.getIcons().add(new Image(getClass().getResource("/images/customer-icon-2.png").toExternalForm()));
                        popupStage.setScene(new Scene(popupContent));

                        popupStage.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    private String generateId() throws SQLException {
        int productNumber = new HoaDon_BUS().countHoaDon();
        String id = String.format("HD%04d", productNumber + 1);

        return id;
    }

    private String generateTempInvoiceId() throws SQLException {
        int productNumber = new HoaDon_BUS().countHoaDon() + new HoaDon_BUS().countHoaDon();
        String id = String.format("HD%04d", productNumber + 1);

        return id;
    }

    @FXML
    public void setupTablePlaceholder(String placeholderStr, TableView<SanPham> table) {
        Label noContentLabel = new Label(placeholderStr);
        noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
        table.setPlaceholder(noContentLabel);
    }

    @FXML
    public void handleSaveInvoiceTemp() {
        saveTempBtn.setOnAction(event -> {
            if (!addedProductList.isEmpty()) {
                KhachHang khachHang = new KhachHang();
                try {
                    khachHang = new KhachHang_DAO().getKhachHangByPhone(customerPhoneField.getText());
                } catch (SQLException ex) {
                }
                NhanVien nhanVien = new NhanVien();
                try {
                    nhanVien = new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap();
                } catch (SQLException ex) {
                }

                LocalDateTime ngayTao = LocalDateTime.now();
                double tienKhachDua = 0;
                double diemSuDung = usePointField.getValue();
                String loaiThanhToan = paymentMethodSelect.getValue();

                double tongTien = 0.0;
                if (currentDetailInvoice != null && !currentDetailInvoice.isEmpty()) {
                    for (ChiTietHoaDon chiTietHoaDon : currentDetailInvoice) {
                        if (chiTietHoaDon == null || chiTietHoaDon.getMaSanPham() == null) {
                        } else {
                            SanPham sp = new SanPham();
                            try {
                                sp = new SanPham_BUS().getSanPhamByMaSanPham(chiTietHoaDon.getMaSanPham());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if (sp != null) {
                                tongTien += (sp.getDonGiaBan() * chiTietHoaDon.getSoLuong())
                                        - (sp.getDonGiaBan() * chiTietHoaDon.getSoLuong() * chiTietHoaDon.getThue());
                            }
                        }
                    }
                }

                HoaDon hoaDon = new HoaDon();
                try {
                    hoaDon = new HoaDon(generateId(), khachHang, nhanVien, ngayTao, tienKhachDua, diemSuDung, loaiThanhToan, tongTien);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (new HoaDon_BUS().createHoaDonTam(hoaDon)) {
                        showSuccessfulModal("Lưu hóa đơn tạm thành công.");
                        refreshForm();
                    }
                } catch (SQLException ex) {
                }

                if (currentDetailInvoice != null) {
                    for (ChiTietHoaDon chiTietHoaDon : currentDetailInvoice) {
                        new ChiTietHoaDon_BUS().createChiTietHoaDon(chiTietHoaDon);
                    }
                } else {
                    System.out.println("Danh sách chi tiết hóa đơn trống!");
                }
            } else {
                showErrorDialog("Chưa có sản phẩm trong giỏ hàng");
            }
        });
    }

    @FXML
    private void showSuccessfulModal(String message) {
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

    @FXML
    public void handleSearchProduct() throws SQLException {
        ObservableList<SanPham> productList = FXCollections.observableArrayList(new SanPham_BUS().getAllSanPham());
        List<String> productNames = productList.stream()
                .map(product -> product.getMaSanPham() + " - " + product.getTenSanPham())
                .collect(Collectors.toList());
        filteredList = new FilteredList<>(FXCollections.observableArrayList(productNames), b -> true);
        searchProductField.getItems().addAll(productNames);
        searchProductField.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                filteredList.setPredicate(product -> {
                    if (newValue == null || newValue.isEmpty()) {
                        searchProductField.getItems().addAll(productNames);
                        searchProductField.hide();
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();
                    return product.toLowerCase().contains(lowerCaseFilter);
                });
                if (newValue == null || newValue.isEmpty()) {
                    searchProductField.hide();
                } else {
                    searchProductField.show();
                }
                searchProductField.getItems().clear();
                searchProductField.getItems().addAll(filteredList);
            }
        });

        searchProductField.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue != null && newValue.length() >= 6) {
                    currentProduct = new SanPham_BUS().getSanPhamByMaSanPham(newValue.substring(0, 6));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (currentProduct != null) {
                renderProductInfo(currentProduct);
            }

        });
    }

    @FXML
    public void renderTotalInvoice() throws SQLException {

        int quantity = 0;
        if (currentDetailInvoice != null && !currentDetailInvoice.isEmpty()) {
            for (ChiTietHoaDon cthd : currentDetailInvoice) {
                if (cthd != null && cthd.getMaSanPham() != null) {
                    quantity += cthd.getSoLuong();
                }
            }
        }
        totalProduct.setText(String.valueOf(quantity));
        totalPrice.setText(String.valueOf(checkoutPrice).replace(".0", ""));
        discountPrice.setText(String.valueOf(usePointField.getValue()));
    }

    @FXML
    public void renderAddedProduct(ChiTietHoaDon cthd) {
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        addedIdColumn.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
        addedProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        addedProductUnitColumn.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
        addedProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("donGiaBan"));
        addedProductExpirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("hanSuDung"));
        addedProductDosageColumn.setCellValueFactory(new PropertyValueFactory<>("lieuLuong"));
        addedProductQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("soLuong"));

        numberColumn.setCellValueFactory(cellData
                -> new ReadOnlyObjectWrapper<>(addedProductTable.getItems().indexOf(cellData.getValue()) + 1)
        );

        totalColumn.setCellValueFactory(cellData -> {
            ChiTietHoaDon detail = cellData.getValue();
            double thanhTien = 0.0;
            try {
                thanhTien = detail.getDonGiaBan() * detail.getSoLuong();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new ReadOnlyObjectWrapper<>(thanhTien);
        });

        handleAddDeleteButtonToColumn();
        setIntegerSpinnerColumnEditable(addedProductQuantityColumn);
        setStringComboBoxColumnEditable(addedProductDosageColumn, new String[]{"1 lần/ngày", "2 lần/ngày", "3 lần/ngày", "4 lần/ngày", "Mỗi 6 giờ một lần", "Mỗi 8 giờ một lần", "Mỗi 12 giờ một lần", "Mỗi 24 giờ một lần"});

        addedProductExpirationDateColumn.setCellFactory(col -> new TableCell<ChiTietHoaDon, LocalDate>() {
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

        boolean isProductExists = false;
        for (ChiTietHoaDon detail : addedProductList) {
            if (detail.getMaSanPham().equals(cthd.getMaSanPham())) {
                detail.setSoLuong(detail.getSoLuong() + cthd.getSoLuong());
                isProductExists = true;
                break;
            }
        }

        if (!isProductExists) {
            addedProductList.add(cthd);
        }

        addedProductTable.getItems().clear();
        addedProductTable.getItems().addAll(addedProductList);
    }

    private void setIntegerSpinnerColumnEditable(TableColumn<ChiTietHoaDon, Integer> column) {
        column.setCellFactory(col -> new TableCell<ChiTietHoaDon, Integer>() {
            private final Spinner<Integer> spinner = new Spinner<>();
            private final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    0, 999, 0);
            private final TextFormatter<String> quantityFormatter = new TextFormatter<>(change -> {
                String newText = change.getControlNewText();
                return newText.matches("\\d*") ? change : null;
            });

            {
                spinner.setValueFactory(valueFactory);
                spinner.setEditable(true);
                spinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && isEditing()) {
                        cancelEdit();
                    }
                });
                spinner.getEditor().setTextFormatter(quantityFormatter);

                spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    spinner.getEditor().setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            switch (event.getCode()) {
                                case ENTER -> {
                                    try {
                                        if (isEditing() && newValue != null) {
                                            commitEdit(newValue);
                                            setGraphic(null);
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input: " + spinner.getEditor().getText());
                                    }
                                }
                                default -> {
                                }
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        spinner.getValueFactory().setValue(item);
                        setGraphic(spinner);
                        setText(null);
                    } else {
                        setText(item == null ? "" : item.toString());
                        setGraphic(null);
                    }
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                Integer currentValue = getItem();
                if (currentValue != null) {
                    spinner.getValueFactory().setValue(currentValue);
                }
                setGraphic(spinner);
                setText(null);
                spinner.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() == null ? "" : getItem().toString());
                setGraphic(null);
            }
        });

        column.setOnEditCommit(event -> {
            ChiTietHoaDon cthd = event.getRowValue();
            Object newValue = event.getNewValue();

            if (newValue != null) {
                boolean isConfirmed = false;
                try {
                    isConfirmed = showChangeTableConfirmationPopup(cthd, newValue, event);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (isConfirmed) {
                    cthd.setSoLuong((Integer) newValue);
                    column.getTableView().refresh();
                }
            }
        });
    }

    private void setStringComboBoxColumnEditable(TableColumn<ChiTietHoaDon, String> column,
            String[] options) {
        column.setCellFactory(col -> new TableCell<ChiTietHoaDon, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            {
                comboBox.setEditable(true);
                comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && isEditing()) {
                        cancelEdit();
                    }
                });

                comboBox.setOnAction(event -> {
                    commitEdit((comboBox.getValue().trim()));
                });

                comboBox.getItems().addAll(options);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        comboBox.setValue(getItem());
                        setGraphic(comboBox);
                        setText(null);
                    } else {
                        setText(item == null ? "" : item);
                        setGraphic(null);
                    }
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                comboBox.setValue(getItem().trim());
                setGraphic(comboBox);
                setText(null);
                comboBox.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() == null ? "" : getItem().trim());
                setGraphic(null);
            }
        });

        column.setOnEditCommit(event -> {
            ChiTietHoaDon cthd = event.getRowValue();
            Object newValue = event.getNewValue();

            if (newValue != null) {
                boolean isConfirmed = false;
                try {
                    isConfirmed = showChangeTableConfirmationPopup(cthd, newValue, event);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (isConfirmed) {
                    cthd.setLieuLuong(newValue.toString());
                    column.getTableView().refresh();
                }
            }
        });
    }

    @FXML
    public <T> boolean showChangeTableConfirmationPopup(ChiTietHoaDon cthd, Object newValue,
            TableColumn.CellEditEvent<ChiTietHoaDon, T> event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thay đổi");
        alert.setHeaderText("Thay đổi thông tin sản phẩm " + cthd.getSanPham().getTenSanPham());
        alert.setContentText("Mã sản phẩm: " + cthd.getMaSanPham());

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/tick-icon.png")));
        stage.initStyle(StageStyle.UNDECORATED);

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

        confirmBtn.setOnMouseEntered(e -> NodeUtil.applyFadeTransition(confirmBtn, 1, 0.6, 300, () -> {
        }));
        confirmBtn.setOnMouseExited(e -> NodeUtil.applyFadeTransition(confirmBtn, 0.6, 1, 300, () -> {
        }));
        cancelBtn.setOnMouseEntered(e -> NodeUtil.applyFadeTransition(cancelBtn, 1, 0.6, 300, () -> {
        }));
        cancelBtn.setOnMouseExited(e -> NodeUtil.applyFadeTransition(cancelBtn, 0.6, 1, 300, () -> {
        }));

        Optional<ButtonType> result = alert.showAndWait();
        event.consume();
        return result.isPresent() && result.get() == confirmButton;
    }

    @FXML
    public <T> void showDeleteProductConfirmationPopup(TableView<SanPham> table, SanPham product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Xóa sản phẩm ra khỏi giỏ hàng " + product.getTenSanPham());
        alert.setContentText("Mã sản phẩm: " + product.getMaSanPham());

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/tick-icon.png")));
        stage.initStyle(StageStyle.UNDECORATED);

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
            table.getItems().remove(product);
        }
    }

    @FXML
    public void handleAddDeleteButtonToColumn() {
        actionColumn.setCellFactory(column -> {
            return new TableCell<SanPham, Void>() {
                private final Button deleteButton = new Button();

                {
                    deleteButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");

                    Image image = new Image(getClass().getResourceAsStream("/images/x-icon.png"));
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);
                    imageView.setPreserveRatio(true);
                    deleteButton.setGraphic(imageView);
                    deleteButton.setStyle("-fx-background-color: transparent;");
                    deleteButton.setVisible(false);

                    deleteButton.setOnAction(event -> {
                        SanPham product = getTableView().getItems().get(getIndex());
                        showDeleteProductConfirmationPopup(getTableView(), product);
                    });

                    deleteButton.setOnMouseEntered(event -> {
                        NodeUtil.applyFadeTransition(deleteButton, 1, 0.7, 300, () -> {
                        });
                        NodeUtil.applyScaleTransition(deleteButton, 1, 1.1, 1, 1.1, 300, () -> {
                        });
                    });
                    deleteButton.setOnMouseExited(event -> {
                        NodeUtil.applyFadeTransition(deleteButton, 0.7, 1, 300, () -> {
                        });
                        NodeUtil.applyScaleTransition(deleteButton, 1.1, 1, 1.1, 1, 300, () -> {
                        });
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                        setStyle("-fx-alignment: CENTER;");
                    }
                }

                @Override
                public void updateIndex(int i) {
                    super.updateIndex(i);
                    if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                        TableRow<SanPham> currentRow = getTableRow();

                        currentRow.setOnMouseEntered(event -> {
                            deleteButton.setVisible(true);
                            NodeUtil.applyFadeTransition(deleteButton, 0, 1, 300, () -> {
                            });
                        });
                        currentRow.setOnMouseExited(event -> {
                            NodeUtil.applyFadeTransition(deleteButton, 1, 0, 300, () -> {
                                deleteButton.setVisible(false);
                            });
                        });
                    }
                }
            };
        });
    }

    @FXML
    public void renderProductInfo(SanPham product) {
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        manufactureDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngaySX"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("donGiaBan"));
        taxColumn.setCellValueFactory(new PropertyValueFactory<>("thue"));
        availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("nhaSX"));
        expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("hanSuDung"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("danhMuc"));

        manufactureDateColumn.setCellFactory(col -> new TableCell<SanPham, LocalDate>() {
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

        expirationDateColumn.setCellFactory(col -> new TableCell<SanPham, LocalDate>() {
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

        productTable.getItems().clear();
        productTable.getItems().add(product);
    }

    public void setUpProductTable() {
        availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("moTa"));
        expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngayHetHan"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
        manufactureDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngaySanXuat"));
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("nhaSanXuat"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("donGiaBan"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));
        taxColumn.setCellValueFactory(new PropertyValueFactory<>("thue"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));

        addedProductTable.getItems().addAll(addedProductList);
    }
}
