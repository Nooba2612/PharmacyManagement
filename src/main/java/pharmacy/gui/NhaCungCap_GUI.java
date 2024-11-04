package pharmacy.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
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
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import pharmacy.bus.NhaCungCap_BUS;
import pharmacy.entity.*;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;

public class NhaCungCap_GUI {

    @FXML
    private TableColumn<NhaCungCap, Void> actionColumn;

    @FXML
    private Button addSupplierBtn;

    @FXML
    private TableColumn<NhaCungCap, String> idColumn;

    @FXML
    private TableColumn<NhaCungCap, String> addressColumn;

    @FXML
    private TableColumn<NhaCungCap, String> emailColumn;

    @FXML
    private TableColumn<NhaCungCap, String> nameColumn;

    @FXML
    private TableColumn<NhaCungCap, String> phoneColumn;

    @FXML
    private HBox root;

    // @FXML
    // private Button searchNCCBtn;
    @FXML
    private TextField searchField;

    @FXML
    private Pane searchPane;

    @FXML
    private TableView<NhaCungCap> supplierTable;

    @FXML
    private TableColumn<NhaCungCap, String> taxNumberColumn;

    @FXML
    private Button exportListBtn;

    private ObservableList<NhaCungCap> supplierList = FXCollections.observableArrayList();

    // methods
    @FXML
    public void initialize() {
        handleSupplierTableAction();
        handleExportNhaCungCapList();
    }

    @FXML
    public void handleSupplierTableAction() {
        renderNhaCungCap();
        handleSearchSupplierAction(supplierList);
        handleEditableSupplierTable();
        setupTablePlaceholder();
        handleAddSupplierAction();
    }

    @FXML
    public void renderNhaCungCap() {
        // Set up cell value factories for each column based on supplier properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("maNCC"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("tenNCC"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        supplierList.addAll(new NhaCungCap_BUS().getAllNhaCungCap());
        handleSearchSupplierAction(supplierList);
        // Set data to the TableView
        supplierTable.setItems(supplierList);
    }

    @FXML
    public void handleAddButtonToActionColumn() {
        actionColumn.setCellFactory(column -> {
            return new TableCell<NhaCungCap, Void>() {
                private final Button deleteButton = new Button();

                {
                    // Style the delete button
                    deleteButton.setStyle("-fx-background-color: #D23617; -fx-text-fill: #FFF;");

                    // Action for the delete button
                    deleteButton.setOnAction(event -> {
                        NhaCungCap supplier = getTableView().getItems().get(getIndex());
                        getTableView().getItems().remove(supplier);
                    });

                    Image image = new Image(getClass().getResourceAsStream("/images/x-icon.png"));
                    ImageView imageView = new ImageView(image);

                    imageView.setFitWidth(20);
                    imageView.setFitHeight(20);
                    imageView.setPreserveRatio(true);
                    deleteButton.setGraphic(imageView);
                    deleteButton.setStyle("-fx-background-color: transparent;");
                    deleteButton.setVisible(false);

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
                        // Center the delete button in the cell
                        setStyle("-fx-alignment: CENTER;");
                    }
                }

                @Override
                public void updateIndex(int i) {
                    super.updateIndex(i);
                    if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                        TableRow<NhaCungCap> currentRow = getTableRow();
                        currentRow.setOnMouseEntered(event -> {
                            deleteButton.setVisible(true);
                            NodeUtil.applyFadeTransition(deleteButton, 0, 1, 300, () -> {
                            });
                        });
                        currentRow
                                .setOnMouseExited(event -> NodeUtil.applyFadeTransition(deleteButton, 1, 0, 300, () -> {
                            deleteButton.setVisible(false);
                        }));
                    }
                }
            };
        });
    }

    @FXML
    public void setupTablePlaceholder() {
        Label noContentLabel = new Label("Không có nhà cung cấp.");
        noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
        supplierTable.setPlaceholder(noContentLabel);
    }

    @FXML
    public void handleEditableSupplierTable() {
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            NhaCungCap supplier = event.getRowValue();
            supplier.setTenNCC(event.getNewValue());
        });

        phoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneColumn.setOnEditCommit(event -> {
            NhaCungCap supplier = event.getRowValue();
            supplier.setSoDienThoai(event.getNewValue());
        });

        addressColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        addressColumn.setOnEditCommit(event -> {
            NhaCungCap supplier = event.getRowValue();
            supplier.setDiaChi(event.getNewValue());
        });

        idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        idColumn.setOnEditCommit(event -> {
            NhaCungCap supplier = event.getRowValue();
            supplier.setMaNCC(event.getNewValue());
        });

        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> {
            NhaCungCap supplier = event.getRowValue();
            supplier.setEmail(event.getNewValue());
        });

    }

    @FXML
    public void handleAddSupplierAction() {
        addSupplierBtn.setOnMouseClicked(event -> {
            try {
                Parent addSupplierFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemNhaCungCap_GUI.fxml"));
                root.getChildren().clear();
                root.getChildren().add(addSupplierFrame);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        addSupplierBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(addSupplierBtn, 1, 0.7, 200, () -> {
            });
        });

        addSupplierBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(addSupplierBtn, 0.7, 1, 200, () -> {
            });
        });
    }

    public void handleSearchSupplierAction(ObservableList<NhaCungCap> nhaCungCapList) {
        FilteredList<NhaCungCap> filteredList = new FilteredList<>(nhaCungCapList, b -> true);

        // searchNCCBtn.setOnAction(event -> {
        //     filteredList.setPredicate(nhaCungCap -> {
        //         if (searchField.getText() == null || searchField.getText().isEmpty()) {
        //             return true;
        //         }
        //         String lowerCaseFilter = searchField.getText().toLowerCase();
        //         return nhaCungCap.getMaNCC().toLowerCase().contains(lowerCaseFilter) ||
        //                nhaCungCap.getTenNCC().toLowerCase().contains(lowerCaseFilter) ||
        //                nhaCungCap.getSoDienThoai().contains(lowerCaseFilter) ||
        //                nhaCungCap.getDiaChi().toLowerCase().contains(lowerCaseFilter) ||
        //                nhaCungCap.getEmail().toLowerCase().contains(lowerCaseFilter);
        //     });
        // });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(nhaCungCap -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return nhaCungCap.getMaNCC().toLowerCase().contains(lowerCaseFilter)
                        || nhaCungCap.getTenNCC().toLowerCase().contains(lowerCaseFilter)
                        || nhaCungCap.getSoDienThoai().contains(lowerCaseFilter)
                        || nhaCungCap.getDiaChi().toLowerCase().contains(lowerCaseFilter)
                        || nhaCungCap.getEmail().toLowerCase().contains(lowerCaseFilter);
            });
        });

        supplierTable.setItems(filteredList.isEmpty() ? nhaCungCapList : filteredList);
    }

    private void exportNhaCungCapToPdf(String outputPdfPath) throws SQLException {
        try (PdfWriter writer = new PdfWriter(new FileOutputStream(outputPdfPath)); PdfDocument pdfDoc = new PdfDocument(writer)) {

            // Apply font
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
                document.add(new Paragraph("DANH SÁCH NHÀ CUNG CẤP")
                        .setFont(font)
                        .setFontSize(20)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(20)
                        .setMarginBottom(20));

                // Create the supplier table with appropriate column widths
                Table table = new Table(new float[]{1, 2, 2, 2, 2}); // Adjust column ratios according to your needs
                table.setWidth(UnitValue.createPercentValue(100));

                // Add table header for suppliers
                table.addHeaderCell(new Cell().add(new Paragraph("Mã NCC").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Tên NCC").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Số điện thoại").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Địa chỉ").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Email").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));

                for (NhaCungCap supplier : supplierList) {
                    table.addCell(new Cell().add(new Paragraph(supplier.getMaNCC()).setFont(font).setFontSize(8)));
                    table.addCell(new Cell().add(new Paragraph(supplier.getTenNCC()).setFont(font).setFontSize(8)));
                    table.addCell(new Cell().add(new Paragraph(supplier.getSoDienThoai()).setFont(font).setFontSize(8)));
                    table.addCell(new Cell().add(new Paragraph(supplier.getDiaChi()).setFont(font).setFontSize(8)));
                    table.addCell(new Cell().add(new Paragraph(supplier.getEmail()).setFont(font).setFontSize(8)));
                }
                document.add(table);
            }
            System.out.println("\n\nPDF of supplier list generated.\n\n");
        } catch (IOException e) {
            Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void handleExportNhaCungCapList() {
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
                exportNhaCungCapToPdf("src/main/resources/pdf/DanhSachNhaCungCap.pdf");
                PDFUtil.showPdfPreview(
                        new File(getClass().getClassLoader().getResource("pdf/DanhSachNhaCungCap.pdf").toURI()));
            } catch (com.itextpdf.io.exceptions.IOException | URISyntaxException | IOException | SQLException e) {
                Logger.getLogger(KhachHang_GUI.class.getName()).log(Level.SEVERE, null, e);
            }
        });
    }

}
