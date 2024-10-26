package pharmacy.gui;

import java.time.LocalDate;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import pharmacy.entity.SanPham;

public class BanHang_GUI {

    @FXML
    private TableColumn<SanPham, Void> actionColumn;

    @FXML
    private Button addCustomerBtn;

    @FXML
    private Button addProductBtn;

    @FXML
    private TableView<SanPham> addedProductTable;

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
    private Button exportInvoiceBtn;

    @FXML
    private Label invoiceId;

    @FXML
    private Label loyaltyPoint;

    @FXML
    private TableView<SanPham> productTable;

    @FXML
    private TextField quantityField;

    @FXML
    private Text residualMoney;

    @FXML
    private HBox root;

    @FXML
    private TextField searchProductField;

    @FXML
    private Text totalPrice;

    @FXML
    private Text totalProduct;

    @FXML
    private Text totalRecievedMoney;

    @FXML
    private TextField usePointField;

    @FXML
    private TableColumn<SanPham, String> addedProductDosageColumn;

    @FXML
    private TableColumn<SanPham, LocalDate> addedProductExpirationDateColumn;

    @FXML
    private TableColumn<SanPham, String> addedProductNameColumn;

    @FXML
    private TableColumn<SanPham, Double> addedProductPriceColumn;

    @FXML
    private TableColumn<SanPham, Integer> addedProductQuantityColumn;

    @FXML
    private TableColumn<SanPham, String> addedProductUnitColumn;

    @FXML
    private TableColumn<SanPham, Integer> availableQuantityColumn;

    @FXML
    private TableColumn<SanPham, String> descriptionColumn;

    @FXML
    private TableColumn<SanPham, Double> totalColumn;

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
    private TableColumn<SanPham, Integer> numberColumn;

    @FXML
    private TableColumn<SanPham, Double> priceColumn;

    @FXML
    private TableColumn<SanPham, String> statusColumn;

    @FXML
    private TableColumn<SanPham, Float> taxColumn;

    @FXML
    private TableColumn<SanPham, String> unitColumn;

    private ObservableList<SanPham> addedProductList = FXCollections.observableArrayList();
    private ObservableList<SanPham> suggestProductList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // setUpAddedProductTable();
        // setUpProductTable();
    }

    public void setUpAddedProductTable() {
        // addedProductDosageColumn.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));
        // addedProductExpirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngayHetHan"));
        // addedProductNameColumn.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
        // addedProductPriceColumn.setCellValueFactory(new PropertyValueFactory<>("donGiaBan"));
        // addedProductQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));
        // addedProductUnitColumn.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
        // totalColumn.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));

        SanPham thuocAspirin = new SanPham(
                "SP001",
                "Aspirin",
                "Thuốc giảm đau",
                LocalDate.of(2023, 6, 15),
                "Công ty Dược A",
                LocalDate.of(2023, 7, 1),
                LocalDate.of(2024, 1, 1),
                100,
                50000.0,
                0.05f,
                LocalDate.of(2025, 6, 15),
                "Thuốc giảm đau, hạ sốt",
                "Viên",
                "Còn hàng",
                "Thuốc");

        // Tạo đối tượng thiết bị y tế
        SanPham mayDoHuyetAp = new SanPham(
                "SP002",
                "Máy đo huyết áp",
                "Thiết bị y tế",
                LocalDate.of(2022, 9, 1),
                "Công ty Thiết bị Y Tế B",
                LocalDate.of(2022, 9, 5),
                LocalDate.of(2023, 9, 25),
                50,
                1200000.0,
                0.1f,
                LocalDate.of(2028, 9, 1),
                "Máy đo huyết áp điện tử",
                "Cái",
                "Còn hàng",
                "Thiết bị");

        addedProductList.addAll(mayDoHuyetAp, thuocAspirin);
        productTable.getItems().addAll(suggestProductList);
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

        // Tạo đối tượng khác (ví dụ: vitamin)
        SanPham vitaminC = new SanPham(
                "SP003",
                "Vitamin C",
                "Thực phẩm chức năng",
                LocalDate.of(2023, 3, 10),
                "Công ty Dược C",
                LocalDate.of(2023, 3, 12),
                LocalDate.of(2023, 10, 20),
                200,
                30000.0,
                0.05f,
                LocalDate.of(2024, 12, 10),
                "Hỗ trợ tăng cường sức đề kháng",
                "Hộp",
                "Còn hàng",
                "Thực phẩm");

        suggestProductList.addAll(vitaminC);
        addedProductTable.getItems().addAll(addedProductList);
    }
}
