package pharmacy.gui;

import java.beans.PropertyDescriptor;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import com.itextpdf.commons.utils.Base64.InputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;
import pharmacy.bus.Thuoc_BUS;
import pharmacy.entity.*;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;

public class Thuoc_GUI {

	@FXML
	private Button addMedicineBtn;

	@FXML
	private TableColumn<Thuoc, Integer> quantityColumn;

	@FXML
	private TableColumn<Thuoc, Integer> availableQuantityColumn;

	@FXML
	private TableColumn<Thuoc, String> descriptionColumn;

	@FXML
	private TableColumn<Thuoc, LocalDate> expirationDateColumn;

	@FXML
	private TableColumn<Thuoc, String> manufacturerColumn;

	@FXML
	private TableColumn<Thuoc, LocalDate> manufactureDateColumn;

	@FXML
	private TableColumn<Thuoc, LocalDate> idColumn;

	@FXML
	private TableView<Thuoc> medicineTable;

	@FXML
	private TableColumn<Thuoc, String> nameColumn;

	@FXML
	private TableColumn<Thuoc, Double> priceColumn;

	@FXML
	private HBox root;

	@FXML
	private TextField searchField;

	@FXML
	private Button searchMedicineBtn;

	@FXML
	private Pane searchPane;

	@FXML
	private TableColumn<Thuoc, Double> taxColumn;

	@FXML
	private TableColumn<Thuoc, String> statusColumn;

	@FXML
	private ComboBox<String> filter;

	@FXML
	private Button exportListBtn;

	@FXML
	private TableColumn<Thuoc, String> unitColumn;

	private ObservableList<Thuoc> medicineList = FXCollections.observableArrayList();

	// methods
	@FXML
	public void initialize() throws SQLException {
		handleExportMedicineList();
		setUpMedicineTableAction();
	}

	@FXML
	public void setUpMedicineTableAction() throws SQLException {
		filter.getItems().setAll("Tất cả thuốc", "Thuốc sắp hết hạn", "Thuốc đã hết hạn",
				"Thuốc có số lượng tồn kho thấp");
		filter.getSelectionModel().selectFirst();

		filter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				try {
					renderMedicines(newValue.toString());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		renderMedicines(filter.getValue());
		handleEditableMedicineTable();
		setupTablePlaceholder();
		handleAddMedicineAction();
	}

	@FXML
	public void setupTablePlaceholder() {
		Label noContentLabel = new Label("Không có thuốc.");
		noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");

		medicineTable.setPlaceholder(noContentLabel);
	}

	@FXML
	public void handleAddMedicineAction() {
		addMedicineBtn.setOnMouseClicked(event -> {
			try {
				Parent addMedicineFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemThuoc_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(addMedicineFrame);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		addMedicineBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(addMedicineBtn, 1, 0.7, 200, () -> {
			});
		});

		addMedicineBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(addMedicineBtn, 0.7, 1, 200, () -> {
			});
		});
	}

	@FXML
	public void renderMedicines(String type) throws SQLException {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("maThuoc"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenThuoc"));
		manufactureDateColumn.setCellValueFactory(new PropertyValueFactory<>("ngaySX"));
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("moTa"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("donGiaBan"));
		availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("soLuongTon"));
		manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("nhaSX"));
		expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("hanSuDung"));
		taxColumn.setCellValueFactory(new PropertyValueFactory<>("thue"));
		unitColumn.setCellValueFactory(new PropertyValueFactory<>("donViTinh"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("trangThai"));

		switch (type) {
			case "Tất cả thuốc":
				medicineList = FXCollections.observableArrayList(new Thuoc_BUS().getAllThuoc());
				break;

			case "Thuốc sắp hết hạn":
				medicineList = FXCollections.observableArrayList(new Thuoc_BUS().getThuocSapHetHanSuDung());
				break;

			case "Thuốc đã hết hạn":
				medicineList = FXCollections.observableArrayList(new Thuoc_BUS().getThuocDaHetHan());
				break;

			case "Thuốc có số lượng tồn kho thấp":
				medicineList = FXCollections.observableArrayList(new Thuoc_BUS().getThuocSapHetTonKho());
				break;

			default:
				break;
		}

		medicineTable.setItems(medicineList);

		handleSearchMedicineAction(medicineList);
	}

	@FXML
	public void handleEditableMedicineTable() {
		makeColumnEditable(nameColumn, "tenThuoc");
		makeColumnEditable(descriptionColumn, "moTa");
		makeColumnEditable(priceColumn, "donGiaBan");
		makeColumnEditable(availableQuantityColumn, "soLuongTon");
		makeColumnEditable(manufacturerColumn, "nhaSX");
		makeColumnEditable(expirationDateColumn, "ngaySanXuat");
		makeColumnEditable(taxColumn, "thue");
		makeColumnEditable(expirationDateColumn, "hanSuDung");
		makeColumnEditable(unitColumn, "donViTinh");
	}

	private <T> void makeColumnEditable(TableColumn<Thuoc, T> column, String property) {
		column.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<T>() {
			@Override
			public String toString(T object) {
				return object == null ? "" : object.toString();
			}

			@Override
			@SuppressWarnings("unchecked")
			public T fromString(String string) {
				try {
					if (column.getCellData(0) instanceof Double) {
						return (T) Double.valueOf(string);
					} else if (column.getCellData(0) instanceof Integer) {
						return (T) Integer.valueOf(string);
					}
					return (T) string;
				} catch (Exception e) {
					System.out.println("Data input error: " + e.getMessage());
					return null;
				}
			}
		}));

		column.setOnEditCommit(event -> {
			Thuoc medicine = event.getRowValue();
			try {
				PropertyDescriptor pd = new PropertyDescriptor(property, Thuoc.class);
				pd.getWriteMethod().invoke(medicine, event.getNewValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@FXML
	public void handleSearchMedicineAction(ObservableList<Thuoc> medicineList) {
		FilteredList<Thuoc> filteredList = new FilteredList<>(medicineList, b -> true);

		searchMedicineBtn.setOnAction(event -> {
			filteredList.setPredicate(medicine -> {
				if (searchField.getText() == null || searchField.getText().isEmpty()) {
					return true;
				}

				String lowerCaseFilter = searchField.getText().toLowerCase();
				return medicine.getMaThuoc().toLowerCase().contains(lowerCaseFilter) ||
						medicine.getTenThuoc().toLowerCase().contains(lowerCaseFilter) ||
						(Double.toString(medicine.getDonGiaBan()).contains(lowerCaseFilter)) ||
						(medicine.getNhaSX() != null && medicine.getNhaSX().toLowerCase().contains(lowerCaseFilter)) ||
						Integer.toString(medicine.getSoLuongTon()).contains(lowerCaseFilter) ||
						(medicine.getMoTa() != null && medicine.getMoTa().toLowerCase().contains(lowerCaseFilter)) ||
						(medicine.getDonViTinh() != null
								&& medicine.getDonViTinh().toLowerCase().contains(lowerCaseFilter))
						||
						medicine.getNgaySX() != null && medicine.getNgaySX().toString().contains(lowerCaseFilter) ||
						(Float.toString(medicine.getThue()).contains(lowerCaseFilter)) ||
						(medicine.getHanSuDung() != null
								&& medicine.getHanSuDung().toString().contains(lowerCaseFilter));

			});
		});

		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList.setPredicate(medicine -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();
				return medicine.getMaThuoc().toLowerCase().contains(lowerCaseFilter) ||
						medicine.getTenThuoc().toLowerCase().contains(lowerCaseFilter) ||
						(Double.toString(medicine.getDonGiaBan()).contains(lowerCaseFilter)) ||
						(medicine.getNhaSX() != null && medicine.getNhaSX().toLowerCase().contains(lowerCaseFilter)) ||
						Integer.toString(medicine.getSoLuongTon()).contains(lowerCaseFilter) ||
						(medicine.getMoTa() != null && medicine.getMoTa().toLowerCase().contains(lowerCaseFilter)) ||
						(medicine.getDonViTinh() != null
								&& medicine.getDonViTinh().toLowerCase().contains(lowerCaseFilter))
						||
						medicine.getNgaySX() != null && medicine.getNgaySX().toString().contains(lowerCaseFilter) ||
						(Float.toString(medicine.getThue()).contains(lowerCaseFilter)) ||
						(medicine.getHanSuDung() != null
								&& medicine.getHanSuDung().toString().contains(lowerCaseFilter));

			});
		});

		medicineTable.setItems(filteredList.size() == 0 ? medicineList : filteredList);
	}

	@FXML
	private void handleExportMedicineList() {
		exportListBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(exportListBtn, 1, 0.6, 300, () -> {
			});
		});

		exportListBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(exportListBtn, 0.6, 1, 300, () -> {
			});
		});

		exportListBtn.setOnAction(event -> {
			try {
				try {
					PDFUtil.showPdfPreview(
							new File(getClass().getClassLoader().getResource("pdf/DanhSachThuoc.pdf").toURI()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (com.itextpdf.io.exceptions.IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		});
	}

	private void exportMedicinesToPdf(String outputPdfPath) {
		try {
			PdfWriter writer = new PdfWriter(new FileOutputStream(outputPdfPath));
			PdfDocument pdfDoc = new PdfDocument(writer);
			Document document = new Document(pdfDoc);

			document.close();
			System.out.println("\n\nPDF generated.\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleUpdateMedicine() {

	}

}
