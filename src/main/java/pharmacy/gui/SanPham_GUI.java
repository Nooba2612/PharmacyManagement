package pharmacy.gui;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import pharmacy.bus.SanPham_BUS;
import pharmacy.entity.KhachHang;
import pharmacy.entity.NhanVien;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;
import pharmacy.utils.PDFUtil;

import java.util.logging.Logger;
import java.net.URL;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFontFactory.EmbeddingStrategy;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.layout.borders.Border;
import com.sun.prism.PixelFormat;
import com.sun.prism.PixelFormat.DataType;

import javafx.stage.StageStyle;

public class SanPham_GUI {

    @FXML
    private Button addSanPhamBtn;

    @FXML
    private TableColumn<SanPham, Void> actionColumn;

    @FXML
    private TableColumn<SanPham, Integer> quantityColumn;

    @FXML
    private TableColumn<SanPham, Integer> availableQuantityColumn;

    @FXML
    private TableColumn<SanPham, String> descriptionColumn;

    @FXML
    private TableColumn<SanPham, LocalDate> expirationDateColumn;

    @FXML
    private TableColumn<SanPham, String> manufacturerColumn;

    @FXML
    private TableColumn<SanPham, LocalDate> manufactureDateColumn;

    @FXML
    private TableColumn<SanPham, LocalDate> idColumn;

    @FXML
    private TableView<SanPham> productTable;

    @FXML
    private TableColumn<SanPham, String> nameColumn;

    @FXML
    private TableColumn<SanPham, Double> priceColumn;

    @FXML
    private HBox root;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchSanPhamBtn;

    @FXML
    private TableColumn<SanPham, Float> taxColumn;

    @FXML
    private TableColumn<SanPham, String> statusColumn;

    @FXML
    private ComboBox<String> filter;

    @FXML
    private Button exportListBtn;

    @FXML
    private TableColumn<SanPham, String> unitColumn;

    @FXML
    private TableColumn<SanPham, String> productTypeColumn;

    @FXML
    private TableColumn<SanPham, String> categoryColumn;

    @FXML
    private Button refreshBtn;

    private ObservableList<SanPham> productList = FXCollections.observableArrayList();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // methods
    @FXML
    public void initialize() throws SQLException {
        handleExportSanPhamList();
        setUpSanPhamTableAction();
    }

    @FXML
    public void setUpSanPhamTableAction() throws SQLException {
        filter.getItems().setAll("Tất cả sản phẩm", "Sản phẩm sắp hết hạn", "Sản phẩm đã hết hạn",
                "Sản phẩm có số lượng tồn kho thấp");
        filter.getSelectionModel().selectFirst();

        filter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    renderSanPhams(newValue);
                } catch (SQLException e) {
                }
            }
        });

        handleRefreshBtn();
        handleEditableSanPhamTable();
        renderSanPhams(filter.getValue());
        setupTablePlaceholder();
        handleAddSanPhamAction();
        addIconToActionColumn();
    }

    @FXML
    public void handleRefreshBtn() {
        refreshBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(refreshBtn, 1, 0.7, 300, () -> {
            });
        });

        refreshBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(refreshBtn, 0.7, 1, 300, () -> {
            });
        });

        refreshBtn.setOnAction(event -> {
            showLoadingAnimation();

            new Thread(() -> {
                try {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    new SanPham_BUS().refreshSanPham();

                    Platform.runLater(() -> {
                        try {
                            renderSanPhams(filter.getValue());
                            setupTablePlaceholder();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private void addIconToActionColumn() {
        actionColumn.setCellFactory(column -> new TableCell<SanPham, Void>() {
            private final Button actionButton = new Button();

            {
                ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/images/edit-icon.png")));
                icon.setFitHeight(20);
                icon.setFitWidth(20);
                actionButton.setGraphic(icon);
                actionButton.setVisible(false);
                actionButton.setStyle("-fx-background-color: transparent;");

                actionButton.setOnMouseEntered(event -> {
                    NodeUtil.applyFadeTransition(actionButton, 1, 0.7, 300, () -> {
                    });
                    NodeUtil.applyScaleTransition(actionButton, 1, 1.1, 1, 1.1, 300, () -> {
                    });
                });
                actionButton.setOnMouseExited(event -> {
                    NodeUtil.applyFadeTransition(actionButton, 0.7, 1, 300, () -> {
                    });
                    NodeUtil.applyScaleTransition(actionButton, 1.1, 1, 1.1, 1, 300, () -> {
                    });
                });

                actionButton.setOnAction(event -> {
                    SanPham sp = getTableView().getItems().get(getIndex());

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CapNhatSanPham_GUI.fxml"));
                        Parent root = loader.load();
                        CapNhatSanPham_GUI controller = loader.getController();

                        try {
                            controller.setUpForm(sp);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Stage popupStage = new Stage();
                        popupStage.initOwner(actionButton.getScene().getWindow());
                        popupStage.getIcons()
                                .add(new Image(getClass().getResource("/images/update-icon.png").toString()));
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setTitle("Cập nhật sản phẩm - " + sp.getTenSanPham());
                        popupStage.setResizable(false);
                        Scene scene = new Scene(root);
                        popupStage.setScene(scene);
                        popupStage.show();

                        popupStage.setOnHidden(ev -> {
                            showLoadingAnimation();

                            new Thread(() -> {
                                try {
                                    try {
                                        Thread.sleep(800);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    new SanPham_BUS().refreshSanPham();

                                    Platform.runLater(() -> {
                                        try {
                                            renderSanPhams(filter.getValue());
                                            setupTablePlaceholder();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    });

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                actionButton.getStyleClass().add("action-button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                HBox hBox = new HBox(actionButton);
                hBox.setAlignment(Pos.CENTER);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hBox);
                }
            }

            @Override
            public void updateIndex(int i) {
                super.updateIndex(i);
                if (getIndex() >= 0 && getIndex() < getTableView().getItems().size()) {
                    TableRow<SanPham> currentRow = getTableRow();
                    currentRow.setOnMouseEntered(event -> {
                        actionButton.setVisible(true);
                        NodeUtil.applyFadeTransition(actionButton, 0, 1, 300, () -> {
                        });
                    });
                    currentRow.setOnMouseExited(event -> NodeUtil.applyFadeTransition(actionButton, 1, 0, 300, () -> {
                        actionButton.setVisible(false);
                    }));
                }
            }
        });
    }

    @FXML
    public void setupTablePlaceholder() {
        Label noContentLabel = new Label("Không có sản phẩm.");
        noContentLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");

        productTable.setPlaceholder(noContentLabel);
    }

    @FXML
    public void handleAddSanPhamAction() {
        addSanPhamBtn.setOnMouseClicked(event -> {
            try {
                Parent addSanPhamFrame = FXMLLoader.load(getClass().getResource("/fxml/ThemSanPham_GUI.fxml"));
                root.getChildren().clear();
                root.getChildren().add(addSanPhamFrame);

            } catch (IOException e) {
                Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, e);
            }
        });

        addSanPhamBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(addSanPhamBtn, 1, 0.7, 200, () -> {
            });
        });

        addSanPhamBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(addSanPhamBtn, 0.7, 1, 200, () -> {
            });
        });
    }

    @FXML
    public void showLoadingAnimation() {
        productList.clear();
        productTable.getItems().clear();

        ImageView loadingImageView = new ImageView(
                new Image(getClass().getClassLoader().getResource("images/loading-icon.png").toString()));
        loadingImageView.setFitHeight(50);
        loadingImageView.setFitWidth(50);
        productTable.setPlaceholder(loadingImageView);

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.8), loadingImageView);
        rotateTransition.setCycleCount(Animation.INDEFINITE);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(360);
        rotateTransition.play();
    }

    @FXML
    public void renderSanPhams(String type) throws SQLException {
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
                    setText(formatter.format(item));
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
                    setText(formatter.format(item));
                }
            }
        });

        switch (type) {
            case "Tất cả sản phẩm" ->
                productList = FXCollections.observableArrayList(new SanPham_BUS().getAllSanPham());

            case "Sản phẩm sắp hết hạn" ->
                productList = FXCollections.observableArrayList(new SanPham_BUS().getSanPhamSapHetHanSuDung());

            case "Sản phẩm đã hết hạn" ->
                productList = FXCollections.observableArrayList(new SanPham_BUS().getSanPhamDaHetHan());

            case "Sản phẩm có số lượng tồn kho thấp" ->
                productList = FXCollections.observableArrayList(new SanPham_BUS().getSanPhamSapHetTonKho());

            default -> {
            }
        }

        productTable.setItems(productList);

        handleSearchSanPhamAction(productList);

        handleEditableSanPhamTable();
    }

    private void showEditProductTypeModal(SanPham product) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CapNhatLoaiSanPham_GUI.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        CapNhatLoaiSanPham_GUI controller = loader.getController();
        controller.setUpForm(product);

        Stage popupStage = new Stage();
        popupStage.getIcons().add(new Image(getClass().getResource("/images/update-icon.png").toString()));
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Cập nhật loại sản phẩm");
        popupStage.setResizable(false);
        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.show();

        popupStage.setOnHidden(event -> {
            showLoadingAnimation();

            new Thread(() -> {
                try {
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    new SanPham_BUS().refreshSanPham();

                    Platform.runLater(() -> {
                        try {
                            renderSanPhams(filter.getValue());
                            setupTablePlaceholder();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();
        });

    }

    @FXML
    public void handleEditableSanPhamTable() {

        setColumnEditable(nameColumn, "tenSanPham");
        setColumnEditable(descriptionColumn, "moTa");
        setColumnEditable(priceColumn, "donGiaBan");
        setIntegerSpinnerColumnEditable(availableQuantityColumn, "soLuongTon");
        setColumnEditable(manufacturerColumn, "nhaSX");
        setDateColumnEditable(manufactureDateColumn, "ngaySX");
        setFloatComboBoxColumnEditable(taxColumn, "thue", new String[] { "0%", "5%", "10%", "15%", "20%" });
        setDateColumnEditable(expirationDateColumn, "hanSuDung");
        setStringComboBoxColumnEditable(productTypeColumn, "loaiSanPham",
        new String[] { "Thuốc", "Thiết bị y tế" });

        // set up unit column
        unitColumn.setCellFactory(col -> new TableCell<SanPham, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();
            String[] options;
            {
                comboBox.setEditable(true);
                comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && isEditing()) {
                        cancelEdit();
                    }
                });

            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (isEditing()) {
                    comboBox.setValue(item);
                    setGraphic(comboBox);
                    setText(null);
                } else {
                    setText(item);
                    setGraphic(null);
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                comboBox.getItems().clear();

                SanPham currentProduct = (SanPham) getTableRow().getItem();
                String productType = currentProduct.getLoaiSanPham();

                if ("Thuốc".equals(productType)) {
                    options = new String[] { "Viên", "Vỉ", "Hộp", "Chai", "Ống", "Gói" };
                } else {
                    options = new String[] { "Cái", "Chiếc", "Hộp", "Bộ" };
                }
                comboBox.getItems().addAll(options);

                comboBox.setOnAction(event -> {
                    commitEdit(comboBox.getValue());
                });

                comboBox.setValue(getItem());
                setGraphic(comboBox);
                setText(null);
                comboBox.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() == null ? "" : getItem());
                setGraphic(null);
            }
        });

        // set up category column
        categoryColumn.setCellFactory(col -> new TableCell<SanPham, String>() {
            private final ComboBox<String> comboBox = new ComboBox<>();
            String[] options;
            {
                comboBox.setEditable(true);
                comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && isEditing()) {
                        cancelEdit();
                    }
                });

            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (isEditing()) {
                    comboBox.setValue(item);
                    setGraphic(comboBox);
                    setText(null);
                } else {
                    setText(item);
                    setGraphic(null);
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                comboBox.getItems().clear();

                SanPham currentProduct = (SanPham) getTableRow().getItem();
                String productType = currentProduct.getLoaiSanPham();

                if ("Thuốc".equals(productType)) {
                    options = new String[] { "Giảm đau", "Hạ sốt", "Kháng sinh", "Chống viêm", "Vitamin", "An thần",
                            "Siro", "Khác" };
                } else {
                    options = new String[] { "Dụng cụ y tế", "Sản phẩm bảo vệ cá nhân", "Dung dịch vệ sinh", "Khác" };
                }
                comboBox.getItems().addAll(options);

                comboBox.setOnAction(event -> {
                    commitEdit(comboBox.getValue());
                });

                comboBox.setValue(getItem());
                setGraphic(comboBox);
                setText(null);
                comboBox.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() == null ? "" : getItem());
                setGraphic(null);
            }
        });

        // set up product type column
        productTypeColumn.setCellFactory(col -> new TableCell<SanPham, String>() {
            private final Label label = new Label();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(label);

                    label.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            SanPham selectedProduct = getTableView().getItems().get(getIndex());
                            showEditProductTypeModal(selectedProduct);
                        }
                    });
                }
            }
        });

        // handle edit commit
        unitColumn.setOnEditCommit(event -> {
            SanPham product = event.getRowValue();
            Object newValue = event.getNewValue();

            if (newValue != null) {
                showChangeTableConfirmationPopup(product, newValue, event, "donViTinh");
            } else {
            }
        });

        categoryColumn.setOnEditCommit(event -> {
            SanPham product = event.getRowValue();
            Object newValue = event.getNewValue();

            if (newValue != null) {
                showChangeTableConfirmationPopup(product, newValue, event, "danhMuc");
            } else {
            }
        });

        // productTypeColumn.setOnEditCommit(event -> {
        // Object newValue = event.getNewValue();
        // SanPham product = event.getRowValue();

        // if (newValue != null) {
        // try {
        // showChangeTableConfirmationPopup(product, newValue, event, "loaiSanPham");
        // } catch (SQLException e) {
        // e.printStackTrace();
        // }
        // } else {
        // }
        // });
    }

    private void setIntegerSpinnerColumnEditable(TableColumn<SanPham, Integer> column, String property) {
        column.setCellFactory(col -> new TableCell<SanPham, Integer>() {
            private final Spinner<Integer> spinner = new Spinner<>();
            private final SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    0, 9999, 0);

            {
                spinner.setValueFactory(valueFactory);
                spinner.setEditable(true);
                spinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && isEditing()) {
                        cancelEdit();
                    }
                });

                spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
                    spinner.getEditor().setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            switch (event.getCode()) {
                                case ENTER -> {
                                    try {
                                        commitEdit(newValue);
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
                        setText(item.toString());
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
                setText(getItem().toString());
                setGraphic(null);
            }
        });

        column.setOnEditCommit(event -> {
            SanPham product = event.getRowValue();
            Object newValue = event.getNewValue();

            // System.out.println(newValue);
            if (newValue != null) {
                showChangeTableConfirmationPopup(product, newValue, event, property);
            } else {
            }
        });
    }

    public Float parsePercentageString(String percentage) {
        try {
            String numericPart = percentage.replace("%", "").trim();
            return Float.parseFloat(numericPart) / 100;
        } catch (NumberFormatException e) {
            System.err.println("Invalid percentage format: " + percentage);
            return null;
        }
    }

    private void setFloatComboBoxColumnEditable(TableColumn<SanPham, Float> column, String property, String[] options) {
        column.setCellFactory(col -> new TableCell<SanPham, Float>() {
            private final ComboBox<String> comboBox = new ComboBox<>();

            {
                comboBox.setEditable(true);
                comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                    if (!isNowFocused && isEditing()) {
                        cancelEdit();
                    }
                });

                comboBox.setOnAction(event -> {
                    commitEdit(parsePercentageString(comboBox.getValue()));
                });

                comboBox.getItems().addAll(options);
            }

            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isEditing()) {
                        comboBox.setValue(String.valueOf(getItem() * 100).replace(".0", "") + "%");
                        setGraphic(comboBox);
                        setText(null);
                    } else {
                        setText(item == null ? "" : String.valueOf(item * 100).replace(".0", "") + "%");
                        setGraphic(null);
                    }
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                comboBox.setValue(String.valueOf(getItem() * 100).replace(".0", "") + "%");
                setGraphic(comboBox);
                setText(null);
                comboBox.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem() == null ? "" : String.valueOf(getItem() * 100).replace(".0", "") + "%");
                setGraphic(null);
            }
        });

        column.setOnEditCommit(event -> {
            SanPham product = event.getRowValue();
            Object newValue = event.getNewValue();

            if (newValue != null) {
                showChangeTableConfirmationPopup(product, newValue, event, property);
            } else {
                showInvalidInputDataDialog();
            }
        });
    }

    private void setStringComboBoxColumnEditable(TableColumn<SanPham, String> column, String property,
            String[] options) {
        column.setCellFactory(col -> new TableCell<SanPham, String>() {
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
            SanPham product = event.getRowValue();
            Object newValue = event.getNewValue();

            if (newValue != null) {
                showChangeTableConfirmationPopup(product, newValue, event, property);
            } else {
                showInvalidInputDataDialog();
            }
        });
    }

    private void setDateColumnEditable(TableColumn<SanPham, LocalDate> column, String property) {

        column.setCellFactory(col -> new TableCell<SanPham, LocalDate>() {
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
            SanPham product = event.getRowValue();
            Object newValue = event.getNewValue();

            if (newValue != null) {
                showChangeTableConfirmationPopup(product, newValue, event, property);
            } else {
                showInvalidInputDataDialog();
            }
        });

    }

    private <T> void setColumnEditable(TableColumn<SanPham, T> column, String property) {
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
            SanPham product = event.getRowValue();
            Object newValue = event.getNewValue();

            if (newValue != null) {
                showChangeTableConfirmationPopup(product, newValue, event, property);
            } else {
                showInvalidInputDataDialog();
            }
        });
    }

    @FXML
    private void showInvalidInputDataDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Không thể nhập dữ liệu");
        alert.setHeaderText("Giá trị nhập không phù hợp.");

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
    public <T> void showChangeTableConfirmationPopup(SanPham product, Object newValue,
            TableColumn.CellEditEvent<SanPham, T> event, String property) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thay đổi");
        alert.setHeaderText("Thay đổi thông tin sản phẩm " + product.getTenSanPham());
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
            try {
                PropertyDescriptor pd = new PropertyDescriptor(property, SanPham.class);
                pd.getWriteMethod().invoke(product, newValue);
                if (new SanPham_BUS().updateSanPham(product)) {
                    System.out.println("Input successfully.");
                    event.consume();
                } else {
                    showInvalidInputDataDialog();
                }
            } catch (Exception ex) {
                Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            event.consume();
        }
    }

    @FXML
    public void handleSearchSanPhamAction(ObservableList<SanPham> productList) {
        FilteredList<SanPham> filteredList = new FilteredList<>(productList, b -> true);

        searchSanPhamBtn.setOnAction(event -> {
            filteredList.setPredicate(product -> {
                if (searchField.getText() == null || searchField.getText().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = searchField.getText().toLowerCase();
                return product.getMaSanPham().toLowerCase().contains(lowerCaseFilter) ||
                        product.getTenSanPham().toLowerCase().contains(lowerCaseFilter) ||
                        (Double.toString(product.getDonGiaBan()).contains(lowerCaseFilter)) ||
                        (product.getNhaSX() != null && product.getNhaSX().toLowerCase().contains(lowerCaseFilter)) ||
                        Integer.toString(product.getSoLuongTon()).contains(lowerCaseFilter) ||
                        (product.getMoTa() != null && product.getMoTa().toLowerCase().contains(lowerCaseFilter)) ||
                        (product.getDonViTinh() != null
                                && product.getDonViTinh().toLowerCase().contains(lowerCaseFilter))
                        ||
                        product.getNgaySX() != null && product.getNgaySX().toString().contains(lowerCaseFilter) ||
                        (Float.toString(product.getThue()).contains(lowerCaseFilter)) ||
                        (product.getHanSuDung() != null
                                && product.getHanSuDung().toString().contains(lowerCaseFilter));

            });
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return product.getMaSanPham().toLowerCase().contains(lowerCaseFilter) ||
                        product.getTenSanPham().toLowerCase().contains(lowerCaseFilter) ||
                        (Double.toString(product.getDonGiaBan()).contains(lowerCaseFilter)) ||
                        (product.getNhaSX() != null && product.getNhaSX().toLowerCase().contains(lowerCaseFilter)) ||
                        Integer.toString(product.getSoLuongTon()).contains(lowerCaseFilter) ||
                        (product.getMoTa() != null && product.getMoTa().toLowerCase().contains(lowerCaseFilter)) ||
                        (product.getDonViTinh() != null
                                && product.getDonViTinh().toLowerCase().contains(lowerCaseFilter))
                        ||
                        product.getNgaySX() != null && product.getNgaySX().toString().contains(lowerCaseFilter) ||
                        (Float.toString(product.getThue()).contains(lowerCaseFilter)) ||
                        (product.getHanSuDung() != null
                                && product.getHanSuDung().toString().contains(lowerCaseFilter));

            });
        });

        productTable.setItems(filteredList.isEmpty() ? productList : filteredList);
    }

    @FXML
    private void handleExportSanPhamList() {
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
                exportSanPhamsToPdf("src/main/resources/pdf/DanhSachSanPham.pdf");
                PDFUtil.showPdfPreview(
                        new File(getClass().getClassLoader().getResource("pdf/DanhSachSanPham.pdf").toURI()));
            } catch (com.itextpdf.io.exceptions.IOException | URISyntaxException | IOException | SQLException e) {
                Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, e);
            }

        });
    }

    private void exportSanPhamsToPdf(String outputPdfPath) throws SQLException {
        try (PdfWriter writer = new PdfWriter(new FileOutputStream(outputPdfPath));
                PdfDocument pdfDoc = new PdfDocument(writer)) {

            // Aplly font
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

            // Transparent color
            Color transparentColor = new DeviceRgb(0, 0, 0);

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
                document.add(new Paragraph("DANH SÁCH SẢN PHẨM")
                        .setFont(font)
                        .setFontSize(20)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(20)
                        .setMarginBottom(20));

                // Table
                Table table = new Table(new float[] { 1, 2, 2, 2, 2, 2, 2, 2 });
                table.setWidth(UnitValue.createPercentValue(100));

                table.addHeaderCell(new Cell().add(new Paragraph("Mã sản phẩm").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Tên sản phẩm").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Đơn vị tính").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Nhà sản xuất").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Ngày sản xuất").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Ngày hết hạn").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Trạng thái").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));
                table.addHeaderCell(new Cell().add(new Paragraph("Số lượng tồn").setFont(font).setFontSize(8))
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY));

                for (SanPham product : productList) {
                    table.addCell(new Paragraph(product.getMaSanPham()).setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(product.getTenSanPham()).setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(product.getDonViTinh()).setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(product.getNhaSX()).setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(product.getNgaySX().toString()).setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(product.getHanSuDung().toString()).setFont(font).setFontSize(8));
                    table.addCell(new Paragraph(product.getTrangThai()).setFont(font).setFontSize(8));
                    table.addCell(
                            new Paragraph(String.valueOf(product.getSoLuongTon())).setFont(font).setFontSize(8));
                }

                document.add(table);
            }
            System.out.println("\n\nPDF generated.\n\n");
        } catch (IOException e) {
            Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}