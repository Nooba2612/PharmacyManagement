package pharmacy.gui;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.support.RootBeanDefinition;

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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import pharmacy.bus.KhachHang_BUS;
import pharmacy.entity.*;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;

public class KhachHang_GUI {

    @FXML
    HBox root;

    @FXML
    private TableView<KhachHang> customerTable;

    @FXML
    private TableColumn<KhachHang, String> idColumn;

    @FXML
    private TableColumn<KhachHang, String> nameColumn;

    @FXML
    private TableColumn<KhachHang, String> phoneColumn;

    @FXML
    private TableColumn<KhachHang, Integer> pointsColumn;

    @FXML
    private TableColumn<KhachHang, LocalDate> yearColumn;

    @FXML
    private TableColumn<KhachHang, String> noteColumn;

    @FXML
    private TableColumn<KhachHang, String> genderColumn;

    @FXML
    private TableColumn<KhachHang, Void> actionColumn;

    @FXML
    private Button addCustomerBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchKhachHangBtn;

    @FXML
    private Button exportListBtn;

    private ObservableList<KhachHang> customerList = FXCollections.observableArrayList();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // methods
    @FXML
    public void initialize() {
        handleExportKhachHangList();
        handleCustomerTableAction();
    }

    @FXML
    public void handleCustomerTableAction() {
        handleEditableCustomerTable();
        renderKhachHangs();
        setupTablePlaceholder();
        handleAddCustomerAction();
    }

    @FXML
    public void renderKhachHangs() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("maKhachHang"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("soDienThoai"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("diemTichLuy"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("namSinh"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("ghiChu"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gioiTinh"));
        // handleAddButtonToActionColumn();
        yearColumn.setCellFactory(col -> new TableCell<KhachHang, LocalDate>() {
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

        customerList.addAll(new KhachHang_BUS().getAllKhachHang());
        customerTable.setItems(customerList);
        handleSeacrhCustomerAction(customerList);
        handleEditableCustomerTable();

    }

    @FXML
    public void handleAddButtonToActionColumn() {
        actionColumn.setCellFactory(column -> {
            return new TableCell<KhachHang, Void>() {
                private final Button deleteButton = new Button();

                {
                    // Style the delete button
                    deleteButton.setStyle("-fx-background-color: #D23617; -fx-text-fill: #FFF;");

                    // Action for the delete button
                    deleteButton.setOnAction(event -> {
                        KhachHang customer = getTableView().getItems().get(getIndex());
                        getTableView().getItems().remove(customer);
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
                        TableRow<KhachHang> currentRow = getTableRow();
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
        Label noContentLabel = new Label("Không có khách hàng.");
        noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
        customerTable.setPlaceholder(noContentLabel);
    }

    @FXML
    public void handleEditableCustomerTable() {
        setColumnEditable(nameColumn, "hoTen");
        setColumnEditable(phoneColumn, "soDienThoai");
        setColumnEditable(genderColumn, "gioiTinh");
        setDateColumnEditable(yearColumn, "namSinh");
        setColumnEditable(noteColumn, "ghiChu");
    }

    @FXML
    public void handleAddCustomerAction() {
        addCustomerBtn.setOnMouseClicked(event -> {
            try {
                Parent addCustomerFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemKhachHang_GUI.fxml"));
                root.getChildren().clear();
                root.getChildren().add(addCustomerFrame);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        addCustomerBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(addCustomerBtn, 1, 0.7, 200, () -> {
            });
        });

        addCustomerBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(addCustomerBtn, 0.7, 1, 200, () -> {
            });
        });
    }

    public void handleSeacrhCustomerAction(ObservableList<KhachHang> khachHangList) {
        FilteredList<KhachHang> filteredList = new FilteredList<>(khachHangList, b -> true);

        searchKhachHangBtn.setOnAction(event -> {
            filteredList.setPredicate(khachHang -> {
                if (searchField.getText() == null || searchField.getText().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = searchField.getText().toLowerCase();
                return khachHang.getMaKhachHang().toLowerCase().contains(lowerCaseFilter)
                        || khachHang.getHoTen().toLowerCase().contains(lowerCaseFilter)
                        || khachHang.getSoDienThoai().contains(lowerCaseFilter);
            });
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(khachHang -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return khachHang.getMaKhachHang().toLowerCase().contains(lowerCaseFilter)
                        || khachHang.getHoTen().toLowerCase().contains(lowerCaseFilter)
                        || khachHang.getSoDienThoai().contains(lowerCaseFilter);
            });
        });

        customerTable.setItems(filteredList.isEmpty() ? khachHangList : filteredList);
    }

    private void setDateColumnEditableForCustomer(TableColumn<KhachHang, LocalDate> column, String property) {
        column.setCellFactory(col -> new TableCell<KhachHang, LocalDate>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setEditable(true);
                datePicker.setOnAction(event -> commitEdit(datePicker.getValue()));
                datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && isEditing()) {
                        cancelEdit();
                    }
                });
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        datePicker.setValue(getItem());
                        setGraphic(datePicker);
                        setText(null);
                    } else {
                        setText(item == null ? "" : formatter.format(item));
                        setGraphic(null);
                    }
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                datePicker.setValue(getItem());
                setGraphic(datePicker);
                setText(null);
                datePicker.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() == null ? "" : formatter.format(getItem()));
                setGraphic(null);
            }
        });

        column.setOnEditCommit(event -> {
            KhachHang customer = event.getRowValue();
            LocalDate newValue = event.getNewValue();

            if (newValue != null) {
                showChangeTableConfirmationPopup(customer, newValue, event, property);
            } else {
                showInvalidInputDataDialog();
            }
        });
    }

    private void setDateColumnEditable(TableColumn<KhachHang, LocalDate> column, String property) {

        column.setCellFactory(col -> new TableCell<KhachHang, LocalDate>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setEditable(true);
                datePicker.setOnAction(event -> commitEdit(datePicker.getValue()));
                datePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && isEditing()) {
                        cancelEdit();
                    }
                });
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {

                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        datePicker.setValue(getItem());
                        setGraphic(datePicker);
                        setText(null);
                    } else {
                        setText(item == null ? "" : formatter.format(item));
                        setGraphic(null);
                    }
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                datePicker.setValue(getItem());
                setGraphic(datePicker);
                setText(null);
                datePicker.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() == null ? "" : formatter.format(getItem()));
                setGraphic(null);
            }

        });

        column.setOnEditCommit(event -> {
            KhachHang customer = event.getRowValue();
            Object newValue = event.getNewValue();

            if (newValue != null) {
                showChangeTableConfirmationPopup(customer, newValue, event, property);
            } else {
                showInvalidInputDataDialog();
            }
        });

    }

    private <T> void setColumnEditable(TableColumn<KhachHang, T> column, String property) {
        column.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<T>() {
            @Override
            public String toString(T object) {
                return object == null ? "" : object.toString().trim();
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
                    return (T) string.trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }));

        column.setOnEditCommit(event -> {
            KhachHang customer = event.getRowValue();
            Object newValue = event.getNewValue();

            if (newValue != null) {
                showChangeTableConfirmationPopup(customer, newValue, event, property);
            } else {
                showInvalidInputDataDialog();
            }
        });
    }

    @FXML
    public <T> void showChangeTableConfirmationPopup(KhachHang customer, Object newValue,
            TableColumn.CellEditEvent<KhachHang, T> event, String property) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thay đổi");
        alert.setHeaderText("Thay đổi thông tin khách hàng " + customer.getHoTen());
        alert.setContentText("Mã khách hàng: " + customer.getMaKhachHang());

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
            try {
                PropertyDescriptor pd = new PropertyDescriptor(property, KhachHang.class);
                pd.getWriteMethod().invoke(customer, newValue);
                if (new KhachHang_BUS().updateKhachHang(customer)) {
                    System.out.println("Input successfully.");
                    event.consume();
                } else {
                    showInvalidInputDataDialog();
                }
            } catch (Exception ex) {
                Logger.getLogger(KhachHang_GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            event.consume();
        }
    }

    @FXML
    private void showInvalidInputDataDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Không thể nhập dữ liệu");
        alert.setHeaderText("Giá trị nhập không phù hợp.");
        alert.setContentText("Vui lòng kiểm tra lại dữ liệu bạn đã nhập.");

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

    private void exportKhachHangToPdf(String outputPdfPath) throws SQLException {
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

                Paragraph name = new Paragraph("MEDKIT ")
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
                document.add(new Paragraph("DANH SÁCH KHÁCH HÀNG")
                        .setFont(font)
                        .setFontSize(20)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(20)
                        .setMarginBottom(20));

                // Table
                Table table = new Table(new float[]{1, 2, 2, 2, 2, 2, 2});
                table.setWidth(UnitValue.createPercentValue(100));

                table.addHeaderCell(new Cell().add(new Paragraph("Mã KH").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Họ tên").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Số điện thoại").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Ngày sinh").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Điểm tích lũy").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Ghi chú").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Giới tính").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));

                for (KhachHang kh : customerList) {
                    table.addCell(new Paragraph(kh.getMaKhachHang()).setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(kh.getHoTen()).setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(kh.getSoDienThoai()).setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(kh.getNamSinh() != null ? kh.getNamSinh().toString() : "").setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(String.valueOf(kh.getDiemTichLuy())).setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(kh.getGhiChu() != null ? kh.getGhiChu() : "").setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(kh.getGioiTinh() != null ? kh.getGioiTinh() : "").setFont(font).setFontSize(8));
                }

                document.add(table);
            }
            System.out.println("\n\nPDF of customer list generated.\n\n");
        } catch (IOException e) {
            Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @FXML
    private void handleExportKhachHangList() {
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
                exportKhachHangToPdf("src/main/resources/pdf/DanhSachKhachHang.pdf");
                PDFUtil.showPdfPreview(
                        new File(getClass().getClassLoader().getResource("pdf/DanhSachKhachHang.pdf").toURI()));
            } catch (com.itextpdf.io.exceptions.IOException | URISyntaxException | IOException | SQLException e) {
                Logger.getLogger(KhachHang_GUI.class.getName()).log(Level.SEVERE, null, e);
            }
        });
    }

}
