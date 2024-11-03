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
        // addedProductDosageColumn.setCellValueFactory(new
        // PropertyValueFactory<>("loaiSanPham"));
        // addedProductExpirationDateColumn.setCellValueFactory(new
        // PropertyValueFactory<>("ngayHetHan"));
        // addedProductNameColumn.setCellValueFactory(new
        // PropertyValueFactory<>("tenSanPham"));
        // addedProductPriceColumn.setCellValueFactory(new
        // PropertyValueFactory<>("donGiaBan"));
        // addedProductQuantityColumn.setCellValueFactory(new
        // PropertyValueFactory<>("soLuongTon"));
        // addedProductUnitColumn.setCellValueFactory(new
        // PropertyValueFactory<>("donViTinh"));
        // totalColumn.setCellValueFactory(new PropertyValueFactory<>("loaiSanPham"));

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

        addedProductTable.getItems().addAll(addedProductList);
    }
}
