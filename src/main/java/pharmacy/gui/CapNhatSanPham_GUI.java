package pharmacy.gui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pharmacy.bus.HistoryLog_BUS;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.entity.NhanVien;
import pharmacy.entity.ProductHistoryLog;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;

public class CapNhatSanPham_GUI {
    @FXML
    private TableColumn<ProductHistoryLog, Number> availableQuantityColumn;

    @FXML
    private ComboBox<String> categoryField;

    @FXML
    private Button refreshDataBtn;

    @FXML
    private TextField createDateField;

    @FXML
    private TextField desciptionField;

    @FXML
    private TableColumn<ProductHistoryLog, String> descriptionColumn;

    @FXML
    private ComboBox<String> productTypeField;

    @FXML
    private TableColumn<ProductHistoryLog, LocalDate> expirationDateColumn;

    @FXML
    private DatePicker expirationDateField;

    @FXML
    private TableColumn<ProductHistoryLog, String> idColumn;

    @FXML
    private TextField idField;

    @FXML
    private TableColumn<ProductHistoryLog, LocalDate> manufactureDateColumn;

    @FXML
    private DatePicker manufactureDateField;

    @FXML
    private TableColumn<ProductHistoryLog, String> manufacturerColumn;

    @FXML
    private TextField manufacturerField;

    @FXML
    private TableView<ProductHistoryLog> historyTable;

    @FXML
    private TableColumn<ProductHistoryLog, String> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<ProductHistoryLog, Number> priceColumn;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private HBox root;

    @FXML
    private Button submitBtn;

    @FXML
    private TableColumn<ProductHistoryLog, Number> taxColumn;

    @FXML
    private ComboBox<String> taxField;

    @FXML
    private TableColumn<ProductHistoryLog, String> unitColumn;

    @FXML
    private TableColumn<ProductHistoryLog, LocalDate> updatedAtColumn;

    @FXML
    private ComboBox<String> unitField;

    @FXML
    private Label categoryAlert;

    @FXML
    private Label expirationDateAlert;

    @FXML
    private Label idAlert;

    @FXML
    private Label manufactureDateAlert;

    @FXML
    private Label manufacturerAlert;

    @FXML
    private Label nameAlert;

    @FXML
    private Label priceAlert;

    @FXML
    private Label productTypeAlert;

    @FXML
    private Label taxAlert;

    @FXML
    private Label unitAlert;

    @FXML
    private Label quantityAlert;

    @FXML
    private Button rejectBtn;

    @FXML
    private TableColumn<ProductHistoryLog, String> productTypeColumn;

    @FXML
    private TableColumn<ProductHistoryLog, String> categoryColumn;

    @FXML
    private TableColumn<ProductHistoryLog, String> employeeColumn;

    private ObservableList<ProductHistoryLog> historyProductList = FXCollections.observableArrayList();

    private SanPham currentProduct;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    public void initialize() throws SQLException {
        handleReject();
        renderHistory();
    }

    @FXML
    public void handleReject() {
        rejectBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(rejectBtn, 1, 0.7, 300, () -> {
            });
        });

        rejectBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(rejectBtn, 0.7, 1, 300, () -> {
            });
        });

        rejectBtn.setOnAction(event -> {
            Stage stage = (Stage) rejectBtn.getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    public void setUpForm(SanPham product) throws SQLException {
        currentProduct = product;

        unitField.getItems().addAll("Viên", "Vỉ", "Hộp", "Chai", "Ống", "Gói");
        taxField.getItems().addAll("0%", "5%", "10%", "15%", "20%");

        productTypeField.getItems().addAll("Thuốc", "Thiết bị y tế");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        createDateField.setText(LocalDateTime.now().format(formatter));

        TextFormatter<String> quantityFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        });
        quantityField.setTextFormatter(quantityFormatter);

        TextFormatter<String> priceFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            return newText.matches("\\d*") ? change : null;
        });
        priceField.setTextFormatter(priceFormatter);

        refreshDataBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(refreshDataBtn, 1, 0.6, 200, () -> {
            });
        });
        refreshDataBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(refreshDataBtn, 0.6, 1, 200, () -> {
            });
        });
        refreshDataBtn.setOnMouseClicked(event -> {
            try {
                refreshForm();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // handle if table empty
        if (historyProductList.isEmpty()) {
            Label noProductLabel = new Label("Không có lịch sử chỉnh sửa.");
            noProductLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
            historyTable.setPlaceholder(noProductLabel);
        }

        idField.setText(product.getMaSanPham());
        nameField.setText(product.getTenSanPham());
        desciptionField.setText(product.getMoTa());
        manufacturerField.setText(product.getNhaSX());
        quantityField.setText(String.valueOf(product.getSoLuongTon()));
        priceField.setText(String.valueOf(product.getDonGiaBan()).replace(".0", ""));
        taxField.setValue((product.getThue() * 100 + "%").replace(".0", ""));
        unitField.setValue(product.getDonViTinh());
        productTypeField.setValue(product.getLoaiSanPham());
        categoryField.setValue(product.getDanhMuc());
        productTypeField.setValue(product.getLoaiSanPham());
        manufactureDateField.setValue(product.getNgaySX());
        manufacturerField.setText(product.getNhaSX());
        expirationDateField.setValue(product.getHanSuDung());

        if (productTypeField != null) {
            if (productTypeField.getValue().equals("Thuốc")) {
                categoryField.getItems().addAll("Giảm đau", "Hạ sốt", "Kháng sinh", "Chống viêm", "Vitamin",
                        "An thần", "Siro", "Khác");
                unitField.getItems().addAll("Viên", "Vỉ", "Hộp", "Chai", "Ống",
                        "Gói");
            } else if (productTypeField.getValue().equals("Thiết bị y tế")) {
                categoryField.getItems().addAll("Dụng cụ y tế", "Sản phẩm bảo vệ cá nhân", "Dung dịch vệ sinh",
                        "Khác");
                unitField.getItems().addAll("Cái", "Chiếc", "Hộp", "Bộ");
            }
        }

        productTypeField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            categoryField.getItems().clear();
            if (newValue != null) {
                categoryField.setValue(null);
                unitField.setValue(null);
                categoryField.getItems().clear();
                unitField.getItems().clear();
                if (newValue.equals("Thuốc")) {
                    categoryField.getItems().addAll("Giảm đau", "Hạ sốt", "Kháng sinh", "Chống viêm", "Vitamin",
                            "An thần", "Siro", "Khác");
                    unitField.getItems().addAll("Viên", "Vỉ", "Hộp", "Chai", "Ống",
                            "Gói");
                } else if (newValue.equals("Thiết bị y tế")) {
                    categoryField.getItems().addAll("Dụng cụ y tế", "Sản phẩm bảo vệ cá nhân", "Dung dịch vệ sinh",
                            "Khác");
                    unitField.getItems().addAll("Cái", "Chiếc", "Hộp", "Bộ");
                }
            }
        });

        handleUpdateProduct();
    }

    @FXML
    public void renderHistory() throws SQLException {
        historyProductList = FXCollections.observableArrayList(new HistoryLog_BUS().getAllProductHistory());

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
        productTypeColumn.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("danhMuc"));
        updatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("ngayCapNhat"));
        employeeColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));

        manufactureDateColumn.setCellFactory(col -> new TableCell<ProductHistoryLog, LocalDate>() {
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

        expirationDateColumn.setCellFactory(col -> new TableCell<ProductHistoryLog, LocalDate>() {
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

        expirationDateColumn.setCellFactory(col -> new TableCell<ProductHistoryLog, LocalDate>() {
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
        
        updatedAtColumn.setCellFactory(col -> new TableCell<ProductHistoryLog, LocalDate>() {
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

        historyTable.setItems(historyProductList);
    }

    @FXML
    public void handleUpdateProduct() throws SQLException {
        submitBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 1, 0.6, 200, () -> {
            });
        });
        submitBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(submitBtn, 0.6, 1, 200, () -> {
            });
        });
        submitBtn.setCursor(Cursor.HAND);

        // add product
        submitBtn.setOnAction(event -> {

            String maSanPham = idField.getText().trim();
            String tenSanPham = nameField.getText().trim();
            String moTa = desciptionField.getText().trim();
            String nhaSX = manufacturerField.getText().trim();
            int soLuongTon = Integer
                    .parseInt(!quantityField.getText().trim().isEmpty() ? quantityField.getText().trim() : "0");
            double donGiaBan = Double
                    .parseDouble(!priceField.getText().trim().isEmpty() ? priceField.getText().trim() : "0");
            float thue = 0;
            if (taxField.getValue() != null) {
                String taxValue = taxField.getValue().replace("%", "").trim();
                thue = Float.parseFloat(taxValue.isEmpty() ? "0" : taxValue) / 100;
            }
            LocalDate ngayTao = LocalDate.now();
            LocalDate ngaySX = manufactureDateField.getValue();
            LocalDate hanSuDung = expirationDateField.getValue();
            String donViTinh = unitField.getValue();
            String trangThai = "Có sẵn";
            String loaiSanPham = productTypeField.getValue();
            String danhMuc = categoryField.getValue();

            if (ngaySX == null) {
                expirationDateAlert.setText("Ngày hết hạn không được rỗng.");
                expirationDateAlert.setVisible(true);
            } else {
                expirationDateAlert.setVisible(false);
            }

            if (hanSuDung == null) {
                manufactureDateAlert.setText("Ngày sản xuất không được rỗng.");
                manufactureDateAlert.setVisible(true);
            } else {
                manufactureDateAlert.setVisible(false);
            }

            SanPham sanPham = new SanPham(maSanPham, tenSanPham, danhMuc, ngaySX, nhaSX, ngayTao,
                    LocalDate.now(), soLuongTon, donGiaBan, thue,
                    hanSuDung, moTa, donViTinh, trangThai, loaiSanPham);
            try {
                if (validateForm()) {
                    new SanPham_BUS().updateSanPham(sanPham);
                    NhanVien nhanVien = new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap();
                    ProductHistoryLog history = new ProductHistoryLog(sanPham, nhanVien);
                    new HistoryLog_BUS().addProductHistory(history);
                    showAddProductSuccessModal("Cập nhật sản phẩm thành công");
                    submitBtn.getScene().getWindow().hide();
                    refreshForm();
                    historyProductList.add(history);
                    renderHistory();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    public boolean validateForm() {
        boolean isValid = true;

        // Validate ID field
        if (idField.getText().trim().isEmpty()) {
            idAlert.setText("Mã sản phẩm không được rỗng.");
            idAlert.setVisible(true);
            isValid = false;
        } else {
            idAlert.setVisible(false);
        }

        // Validate Name field
        if (nameField.getText().trim().isEmpty()) {
            nameAlert.setText("Tên sản phẩm không được rỗng.");
            nameAlert.setVisible(true);
            isValid = false;
        } else {
            nameAlert.setVisible(false);
        }

        // Validate Manufacturer field
        if (manufacturerField.getText().trim().isEmpty()) {
            manufacturerAlert.setText("Nhà sản xuất sản phẩm không được rỗng.");
            manufacturerAlert.setVisible(true);
            isValid = false;
        } else {
            manufacturerAlert.setVisible(false);
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

        // Validate Quantity field
        String quantityText = quantityField.getText().trim();
        if (quantityText.isEmpty()) {
            quantityField.setText("");
            quantityAlert.setText("Số lượng không được để trống.");
            quantityAlert.setVisible(true);
            isValid = false;
        } else {
            try {
                int quantity = Integer.parseInt(quantityText);
                if (quantity < 0) {
                    quantityField.setText("");
                    quantityAlert.setText("Số lượng không được là số âm.");
                    quantityAlert.setVisible(true);
                    isValid = false;
                } else {
                    quantityAlert.setVisible(false);
                }
            } catch (NumberFormatException e) {
                quantityField.setText("");
                quantityAlert.setText("Số lượng phải là một số nguyên hợp lệ.");
                quantityAlert.setVisible(true);
                isValid = false;
            }
        }

        // Validate Price field
        String priceText = priceField.getText().trim();
        if (priceText.isEmpty()) {
            priceField.setText("");
            priceAlert.setText("Giá bán không được để trống.");
            priceAlert.setVisible(true);
            isValid = false;
        } else {
            try {
                double price = Double.parseDouble(priceText);
                if (price < 0) {
                    priceField.setText("");
                    priceAlert.setText("Giá không được là số âm.");
                    priceAlert.setVisible(true);
                    isValid = false;
                } else if (price % 1 != 0) {
                    priceField.setText("");
                    priceAlert.setText("Giá không được là số thập phân.");
                    priceAlert.setVisible(true);
                    isValid = false;
                } else {
                    priceAlert.setVisible(false);
                }
            } catch (NumberFormatException e) {
                priceField.setText("");
                priceAlert.setText("Giá phải là một số hợp lệ.");
                priceAlert.setVisible(true);
                isValid = false;
            }
        }

        // Validate Tax field
        String[] VALID_TAXES = { "0%", "5%", "10%", "15%", "20%" };
        String taxValue = (taxField.getValue() != null) ? taxField.getValue().trim()
                : taxField.getEditor().getText().trim();

        if (taxValue.isEmpty()) {
            taxAlert.setText("Thuế chưa được chọn.");
            taxAlert.setVisible(true);
            isValid = false;
        } else if (!Arrays.asList(VALID_TAXES).contains(taxValue)) {
            taxAlert.setText("Thuế không hợp lệ.");
            taxAlert.setVisible(true);
            isValid = false;
        } else {
            taxAlert.setVisible(false);
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

    @FXML
    private void showAddProductSuccessModal(String message) {
        Stage modalStage = new Stage();
        modalStage.setResizable(false);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.getIcons().addAll(new Image(
                getClass().getClassLoader().getResource("images/tick-icon.png").toExternalForm()));

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
    public void refreshForm() throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        createDateField.setText(LocalDateTime.now().format(formatter));
        idField.setText(currentProduct.getMaSanPham());
        nameField.setText(currentProduct.getTenSanPham());
        desciptionField.setText(currentProduct.getMoTa());
        manufacturerField.setText(currentProduct.getNhaSX());
        quantityField.setText(String.valueOf(currentProduct.getSoLuongTon()));
        priceField.setText(String.valueOf(currentProduct.getDonGiaBan()).replace(".0", ""));
        taxField.setValue(currentProduct.getThue() * 100 + "%");
        unitField.setValue(currentProduct.getDonViTinh());
        productTypeField.setValue(currentProduct.getLoaiSanPham());
        categoryField.setValue(currentProduct.getDanhMuc());
        productTypeField.setValue(currentProduct.getLoaiSanPham());
        manufactureDateField.setValue(currentProduct.getNgaySX());
        manufacturerField.setText(currentProduct.getNhaSX());
        expirationDateField.setValue(currentProduct.getHanSuDung());
    }

}
