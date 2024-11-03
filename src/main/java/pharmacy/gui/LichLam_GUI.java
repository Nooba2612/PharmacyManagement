package pharmacy.gui;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import pharmacy.entity.LichLamViec;
import pharmacy.entity.NhanVien;
import pharmacy.utils.NodeUtil;

public class LichLam_GUI {

    @FXML
    private Button currentTimeBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private Button prevBtn;

    @FXML
    private DatePicker dateSelect;

    @FXML
    private HBox root;

    @FXML
    private TableColumn<LichLamViec, String> shiftColumn;

    @FXML
    private TableColumn<LichLamViec, Void> mondayColumn;

    @FXML
    private TableColumn<LichLamViec, Void> tuesdayColumn;

    @FXML
    private TableColumn<LichLamViec, Void> wednesdayColumn;

    @FXML
    private TableColumn<LichLamViec, Void> thursdayColumn;

    @FXML
    private TableColumn<LichLamViec, Void> fridayColumn;

    @FXML
    private TableColumn<LichLamViec, Void> saturdayColumn;

    @FXML
    private TableColumn<LichLamViec, Void> sundayColumn;

    @FXML
    private Label mondayLabel;

    @FXML
    private Label tuesdayLabel;

    @FXML
    private Label wednesdayLabel;

    @FXML
    private Label thursdayLabel;

    @FXML
    private Label fridayLabel;

    @FXML
    private Label saturdayLabel;

    @FXML
    private Label sundayLabel;

    @FXML
    private TableView<LichLamViec> workScheduleTable;

    private LocalDate currentDate = LocalDate.now();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private void initialize() {
        setupScheduleTable();
        renderSchedule();
    }

    @FXML
    public void setupScheduleTable() {
        shiftColumn.setCellValueFactory(new PropertyValueFactory<>("caLam"));

        Callback<TableColumn<LichLamViec, Void>, TableCell<LichLamViec, Void>> cellFactory = new Callback<TableColumn<LichLamViec, Void>, TableCell<LichLamViec, Void>>() {
            @Override
            public TableCell<LichLamViec, Void> call(final TableColumn<LichLamViec, Void> param) {
                return new TableCell<LichLamViec, Void>() {
                    Image addIcon = new Image(getClass().getResourceAsStream("/images/plus-icon.png"));
                    ImageView addIconImage = new ImageView(addIcon);
                    Button addButton = new Button("Thêm Lịch", addIconImage);

                    {
                        addIconImage.setFitWidth(20);
                        addIconImage.setFitHeight(20);

                        addButton.setVisible(false);
                        addButton.setStyle(
                                "-fx-background-color:  linear-gradient(to right, rgb(63, 188, 63), rgb(0, 230, 0)); -fx-text-fill: white; -fx-font-weight: bold;");

                        addButton.setOnMouseEntered(event -> {
                            NodeUtil.applyFadeTransition(addButton, 1, 0.6, 300, () -> {
                            });
                        });

                        addButton.setOnMouseExited(event -> {
                            NodeUtil.applyFadeTransition(addButton, 0.6, 1, 300, () -> {
                            });
                        });

                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            VBox box = new VBox(addButton);
                            box.setStyle("-fx-alignment: center;");
                            setGraphic(box);
                            setStyle("-fx-alignment: center;");
                            setAlignment(Pos.CENTER);

                            box.setOnMouseEntered(event -> {
                                addButton.setVisible(true);
                                NodeUtil.applyFadeTransition(addButton, 0, 1, 300, () -> {
                                });
                            });

                            box.setOnMouseExited(event -> {
                                addButton.setVisible(false);
                            });

                            // add schedule btn click
                            addButton.setOnAction(event -> {
                                LichLamViec data = getTableView().getItems().get(getIndex());
                                String shift = data.getCaLam();
                                try {
                                    openAddSchedulePopup(box, shift, getIndex(), param);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                };
            }
        };

        mondayColumn.setCellFactory(cellFactory);
        tuesdayColumn.setCellFactory(cellFactory);
        wednesdayColumn.setCellFactory(cellFactory);
        thursdayColumn.setCellFactory(cellFactory);
        fridayColumn.setCellFactory(cellFactory);
        saturdayColumn.setCellFactory(cellFactory);
        sundayColumn.setCellFactory(cellFactory);

        ObservableList<LichLamViec> shifts = FXCollections.observableArrayList(
                new LichLamViec("123", "Ca 1 (7h-15h)",
                        new NhanVien("MK0001", "John Doe", "Nhân viên", "0123456789", "ccc@getEmail.com",
                                LocalDate.of(2022, 5, 10),
                                "Đã nghỉ", "Đại học", "Nam", LocalDate.of(1990, 1, 1), 0, ""),
                        LocalDate.now()),
                new LichLamViec(
                        "456", "Ca 2 (15h-22h)",
                        new NhanVien("MK0002", "Jane Smith", "Người quản lý", "0987654321", "ccc@getEmail.com",
                                LocalDate.of(2021, 11, 15), "Còn làm", "Đại học", "Nữ", LocalDate.of(1992, 2, 2), 0,
                                ""),
                        LocalDate.now()));

        workScheduleTable.setItems(shifts);
    }

    @FXML
    public void renderSchedule() {
        dateSelect.setValue(currentDate);

        dateSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
            mondayLabel.setText("Thứ 2 " + formatter.format(newValue.with(DayOfWeek.MONDAY)));
            tuesdayLabel.setText("Thứ 3 " + formatter.format(newValue.with(DayOfWeek.TUESDAY)));
            wednesdayLabel.setText("Thứ 4 " + formatter.format(newValue.with(DayOfWeek.WEDNESDAY)));
            thursdayLabel.setText("Thứ 5 " + formatter.format(newValue.with(DayOfWeek.THURSDAY)));
            fridayLabel.setText("Thứ 6 " + formatter.format(newValue.with(DayOfWeek.FRIDAY)));
            saturdayLabel.setText("Thứ 7 " + formatter.format(newValue.with(DayOfWeek.SATURDAY)));
            sundayLabel.setText("Chủ nhật " + formatter.format(newValue.with(DayOfWeek.SUNDAY)));
        });

        mondayLabel.setText("Thứ hai " + formatter.format(dateSelect.getValue().with(DayOfWeek.MONDAY)));
        tuesdayLabel.setText("Thứ ba " + formatter.format(dateSelect.getValue().with(DayOfWeek.TUESDAY)));
        wednesdayLabel.setText("Thứ tư " + formatter.format(dateSelect.getValue().with(DayOfWeek.WEDNESDAY)));
        thursdayLabel.setText("Thứ năm " + formatter.format(dateSelect.getValue().with(DayOfWeek.THURSDAY)));
        fridayLabel.setText("Thứ sáu " + formatter.format(dateSelect.getValue().with(DayOfWeek.FRIDAY)));
        saturdayLabel.setText("Thứ bảy " + formatter.format(dateSelect.getValue().with(DayOfWeek.SATURDAY)));
        sundayLabel.setText("Chủ nhật " + formatter.format(dateSelect.getValue().with(DayOfWeek.SUNDAY)));

        prevBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(prevBtn, 1, 0.7, 300, () -> {
            });
        });
        prevBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(prevBtn, 0.7, 1, 300, () -> {
            });
        });
        nextBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(nextBtn, 1, 0.7, 300, () -> {
            });
        });
        nextBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(nextBtn, 0.7, 1, 300, () -> {
            });
        });

        prevBtn.setOnAction(event -> {
            currentDate = currentDate.with(TemporalAdjusters.previous(java.time.DayOfWeek.MONDAY));
            dateSelect.setValue(currentDate);
        });

        nextBtn.setOnAction(event -> {
            currentDate = currentDate.with(TemporalAdjusters.previous(java.time.DayOfWeek.MONDAY));
            dateSelect.setValue(currentDate);
        });

    }

    private void openAddSchedulePopup(VBox box, String shift, int rowIndex, TableColumn<LichLamViec, Void> column)
            throws IOException {
		LocalDate currentDate;
        String dateOfWeek;
        switch (column.getId()) {
            case "mondayColumn" ->{
                dateOfWeek = "Thứ hai";
				currentDate = column.getText().substring(rowIndex)}
            case "tuesdayColumn" ->
                dateOfWeek = "Thứ ba";
            case "wednesdayColumn" ->
                dateOfWeek = "Thứ tư";
            case "thursdayColumn" ->
                dateOfWeek = "Thứ năm";
            case "fridayColumn" ->
                dateOfWeek = "Thứ sáu";
            case "saturdayColumn" ->
                dateOfWeek = "Thứ bảy";
            case "sundayColumn" ->
                dateOfWeek = "Chủ nhật";
            default ->
                throw new IllegalArgumentException("Unexpected column: " + column.getId());
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThemLichLam_GUI.fxml"));
        Parent addSchedulePopup = loader.load();

        Stage popupStage = new Stage();
        Scene popupScene = new Scene(addSchedulePopup);
        ThemLichLam_GUI controller = loader.getController();

        controller.initialize(shift.substring(0, 5), column.getText());

        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/pharmacy-icon.png")));
        popupStage.setTitle(shift.substring(0, 5) + " - " + dateOfWeek);
        popupStage.setResizable(false);
        popupStage.show();

        popupStage.setOnHidden(event -> {
            NhanVien employee = controller.getEmployee();

            if (employee != null) {
                updateSchedule(box, employee);
            }
        });

    }

    private void updateSchedule(VBox box, NhanVien employee) {
        Label employeeText = new Label(employee.getMaNhanVien() + "- " + employee.getHoTen());

        // employeeText.setTextFill(Color.web("#339933"));
        employeeText.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");
        employeeText.setWrapText(true);

        box.getChildren().clear();
        box.getChildren().add(employeeText);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #f0f0f0; -fx-border-width: 1px; -fx-border-color: #339933; -fx-border-radius: 8px;");
    }

}
