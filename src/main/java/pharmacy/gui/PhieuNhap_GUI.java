package pharmacy.gui;

import java.lang.System.Logger.Level;
import java.time.LocalDate;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import pharmacy.entity.PhieuNhap;

public class PhieuNhap_GUI {

    @FXML
    private Button addGoodsReceiptBtn;

    @FXML
    private TableColumn<PhieuNhap, LocalDate> createDateColumn;

    @FXML
    private TableColumn<PhieuNhap, Void> detailColumn;

    @FXML
    private TableColumn<PhieuNhap, String> employeeNameColumn;

    @FXML
    private ComboBox<String> filter;

    @FXML
    private DatePicker fromDate;

    @FXML
    private TableView<PhieuNhap> goodReceiptTable;

    @FXML
    private TableColumn<PhieuNhap, String> idColumn;

    @FXML
    private HBox root;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Pane searchPane;

    @FXML
    private TableColumn<PhieuNhap, String> supplierColumn;

    @FXML
    private DatePicker toDate;

    @FXML
    public void initialize() {
        addGoodsReceiptBtn.setOnAction(event -> {
            try {
                Parent addPhieuNhapFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemPhieuNhap_GUI.fxml"));
                root.getChildren().clear();
                root.getChildren().add(addPhieuNhapFrame);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
