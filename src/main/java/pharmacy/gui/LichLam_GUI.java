package pharmacy.gui;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import pharmacy.bus.LichLamViec_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.entity.LichLamViec;
import pharmacy.entity.NhanVien;
import pharmacy.entity.SanPham;
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

    private ObservableList<LichLamViec> shifts = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupScheduleTable();
        renderSchedule();
    }

    @FXML
    public void setupScheduleTable() {
        shiftColumn.setCellValueFactory(new PropertyValueFactory<>("caLam"));
        // shifts.addAll(new LichLamViec_BUS().getAllLichLamViec());
        dateSelect.setValue(currentDate);

        dateSelect.valueProperty().addListener((observable, oldValue, newValue) -> {
            mondayLabel.setText("Thứ 2 " + formatter.format(newValue.with(DayOfWeek.MONDAY)));
            tuesdayLabel.setText("Thứ 3 " + formatter.format(newValue.with(DayOfWeek.TUESDAY)));
            wednesdayLabel.setText("Thứ 4 " + formatter.format(newValue.with(DayOfWeek.WEDNESDAY)));
            thursdayLabel.setText("Thứ 5 " + formatter.format(newValue.with(DayOfWeek.THURSDAY)));
            fridayLabel.setText("Thứ 6 " + formatter.format(newValue.with(DayOfWeek.FRIDAY)));
            saturdayLabel.setText("Thứ 7 " + formatter.format(newValue.with(DayOfWeek.SATURDAY)));
            sundayLabel.setText("Chủ nhật " + formatter.format(newValue.with(DayOfWeek.SUNDAY)));
            renderSchedule();
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
        currentTimeBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(currentTimeBtn, 1, 0.7, 300, () -> {
            });
        });
        currentTimeBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(currentTimeBtn, 0.7, 1, 300, () -> {
            });
        });

        prevBtn.setOnAction(event -> {
            currentDate = currentDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
            dateSelect.setValue(currentDate);
        });

        currentTimeBtn.setOnAction(event -> {
            currentDate = LocalDate.now();
            dateSelect.setValue(currentDate);
        });

        nextBtn.setOnAction(event -> {
            currentDate = currentDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            dateSelect.setValue(currentDate);
        });

        ScrollPane sp = (ScrollPane) workScheduleTable.lookup("ScrollPane");
        if (sp != null) {
            sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        }

    }

    @FXML
    public void renderSchedule() {

        Callback<TableColumn<LichLamViec, Void>, TableCell<LichLamViec, Void>> cellFactory = (final TableColumn<LichLamViec, Void> param) -> {
            return new TableCell<LichLamViec, Void>() {
                Image addIcon = new Image(getClass().getResourceAsStream("/images/plus-icon.png"));
                ImageView addIconImage = new ImageView(addIcon);
                Button addButton = new Button("Thêm Lịch", addIconImage);
                Label headerLabel = (Label) param.getGraphic();
                LocalDate date = LocalDate.parse(headerLabel.getText().substring(headerLabel.getText().length() - 10, headerLabel.getText().length()), formatter);

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
                        VBox box = new VBox();
                        LichLamViec data = getTableView().getItems().get(getIndex());
                        String shift = data.getCaLam();
                        LichLamViec currentSchedule = new LichLamViec_BUS().getLichLamViecByDateAndShift(date, shift);
                        StackPane stackPane = new StackPane();

                        if (currentSchedule == null) {
                            box.getChildren().add(addButton);
                            box.setOnMouseEntered(event -> {
                                addButton.setVisible(true);
                                NodeUtil.applyFadeTransition(addButton, 0, 1, 300, () -> {
                                });
                            });

                            box.setOnMouseExited(event -> {
                                addButton.setVisible(false);
                            });

                            // Add schedule button click
                            addButton.setOnAction(event -> {
                                try {
                                    openAddSchedulePopup(stackPane, box, shift, param);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            setGraphic(box);
                        } else {
                            Label employeeText = new Label(currentSchedule.getNhanVien().getMaNhanVien() + "- " + currentSchedule.getNhanVien().getHoTen());
                            employeeText.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");
                            employeeText.setWrapText(true);

                            Image deleteIcon = new Image(getClass().getResourceAsStream("/images/black-trash-bin-icon.png"));
                            ImageView deleteIconImage = new ImageView(deleteIcon);
                            deleteIconImage.setFitWidth(15);
                            deleteIconImage.setFitHeight(15);
                            Button deleteButton = new Button("", deleteIconImage);
                            deleteButton.setStyle("-fx-background-color: transparent;");

                            deleteButton.setVisible(false);

                            deleteButton.setOnAction(event -> {
                                showChangeTableConfirmationPopup(currentSchedule, getTableView().getColumns());
                                renderSchedule();
                            });

                            stackPane.getChildren().addAll(box, deleteButton);
                            StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
                            StackPane.setMargin(deleteButton, new Insets(5));

                            box.getChildren().clear();
                            box.getChildren().add(employeeText);
                            box.setPadding(new Insets(10));
                            box.setStyle("-fx-background-color: #f0f0f0; -fx-border-width: 1px; -fx-border-color: #339933; -fx-border-radius: 8px;");

                            stackPane.setOnMouseEntered(event -> {
                                deleteButton.setVisible(true);
                                NodeUtil.applyFadeTransition(deleteButton, 0, 1, 300, () -> {
                                });
                            });
                            stackPane.setOnMouseExited(event -> {
                                NodeUtil.applyFadeTransition(deleteButton, 1, 0, 300, () -> {
                                    deleteButton.setVisible(false);
                                });
                            });

                            deleteButton.setOnMouseEntered(event -> {
                                deleteButton.setVisible(true);
                                NodeUtil.applyFadeTransition(deleteButton, 1, 0.6, 300, () -> {
                                });
                            });
                            deleteButton.setOnMouseExited(event -> {
                                NodeUtil.applyFadeTransition(deleteButton, 0.6, 1, 300, () -> {
                                });
                            });

                            setGraphic(stackPane);
                        }

                        box.setAlignment(Pos.CENTER);
                        box.setSpacing(5);
                        setAlignment(Pos.CENTER);
                    }
                }

            };
        };

        mondayColumn.setCellFactory(cellFactory);
        tuesdayColumn.setCellFactory(cellFactory);
        wednesdayColumn.setCellFactory(cellFactory);
        thursdayColumn.setCellFactory(cellFactory);
        fridayColumn.setCellFactory(cellFactory);
        saturdayColumn.setCellFactory(cellFactory);
        sundayColumn.setCellFactory(cellFactory);

        shifts.add(new LichLamViec("", "Ca 1",
                new NhanVien(),
                LocalDate.now()));
        shifts.add(new LichLamViec("", "Ca 2",
                new NhanVien(),
                LocalDate.now()));

        workScheduleTable.setItems(shifts);
    }

    private void openAddSchedulePopup(StackPane stackPane, VBox box, String shift, TableColumn<LichLamViec, Void> column)
            throws IOException {
        String dateOfWeek;
        LocalDate current;
        switch (column.getId()) {
            case "mondayColumn" -> {
                dateOfWeek = "Thứ hai";
                current = LocalDate.parse(mondayLabel.getText().substring(mondayLabel.getText().length() - 10, mondayLabel.getText().length()), formatter);
            }
            case "tuesdayColumn" -> {
                dateOfWeek = "Thứ ba";
                current = LocalDate.parse(tuesdayLabel.getText().substring(tuesdayLabel.getText().length() - 10, tuesdayLabel.getText().length()), formatter);
            }
            case "wednesdayColumn" -> {
                dateOfWeek = "Thứ tư";
                current = LocalDate.parse(wednesdayLabel.getText().substring(wednesdayLabel.getText().length() - 10, wednesdayLabel.getText().length()), formatter);
            }
            case "thursdayColumn" -> {
                dateOfWeek = "Thứ năm";
                current = LocalDate.parse(thursdayLabel.getText().substring(thursdayLabel.getText().length() - 10, thursdayLabel.getText().length()), formatter);
            }
            case "fridayColumn" -> {
                dateOfWeek = "Thứ sáu";
                current = LocalDate.parse(fridayLabel.getText().substring(fridayLabel.getText().length() - 10, fridayLabel.getText().length()), formatter);
            }
            case "saturdayColumn" -> {
                dateOfWeek = "Thứ bảy";
                current = LocalDate.parse(saturdayLabel.getText().substring(saturdayLabel.getText().length() - 10, saturdayLabel.getText().length()), formatter);
            }
            case "sundayColumn" -> {
                dateOfWeek = "Chủ nhật";
                current = LocalDate.parse(sundayLabel.getText().substring(sundayLabel.getText().length() - 10, sundayLabel.getText().length()), formatter);
            }
            default ->
                throw new IllegalArgumentException("Unexpected column: " + column.getId());
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ThemLichLam_GUI.fxml"));
        Parent addSchedulePopup = loader.load();

        Stage popupStage = new Stage();
        Scene popupScene = new Scene(addSchedulePopup);
        ThemLichLam_GUI controller = loader.getController();

        controller.initialize(shift, current);

        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/pharmacy-icon.png")));
        popupStage.setTitle(shift + " - " + dateOfWeek);
        popupStage.setResizable(false);
        popupStage.show();

        popupStage.setOnHidden(event -> {
            LichLamViec schedule = controller.getSchedule();

            if (schedule != null) {
                addSchedule(stackPane, box, schedule);
                showAddScheduleSuccessModal("Thêm lịch làm thành công.");
                renderSchedule();
            }
        });

    }

    @FXML
    public void showChangeTableConfirmationPopup(LichLamViec schedule, ObservableList<TableColumn<LichLamViec, ?>> columns) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Xác nhận xóa lịch làm ngày " + schedule.getNgayLam() + " " + schedule.getCaLam());
        alert.setContentText("Mã lịch làm: " + schedule.getMaLichLamViec());

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
                if (new LichLamViec_BUS().deleteLichLamViec(schedule.getMaLichLamViec())) {
                    System.out.println("Deleted.");
                }
            } catch (Exception ex) {
                Logger.getLogger(SanPham_GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void showAddScheduleSuccessModal(String message) {
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

    private void addSchedule(StackPane stackPane, VBox box, LichLamViec lichLamViec) {
        Label employeeText = new Label(lichLamViec.getNhanVien().getMaNhanVien() + "- " + lichLamViec.getNhanVien().getHoTen());
        employeeText.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");
        employeeText.setWrapText(true);

        Image deleteIcon = new Image(getClass().getResourceAsStream("/images/black-trash-bin-icon.png"));
        ImageView deleteIconImage = new ImageView(deleteIcon);
        deleteIconImage.setFitWidth(15);
        deleteIconImage.setFitHeight(15);
        Button deleteButton = new Button("", deleteIconImage);
        deleteButton.setStyle("-fx-background-color: transparent;");

        deleteButton.setVisible(false);

        deleteButton.setOnAction(event -> {
            new LichLamViec_BUS().deleteLichLamViec(lichLamViec.getMaLichLamViec());
            renderSchedule();
        });

        stackPane.getChildren().addAll(box, deleteButton);
        StackPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
        StackPane.setMargin(deleteButton, new Insets(5));

        box.getChildren().clear();
        box.getChildren().add(employeeText);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #f0f0f0; -fx-border-width: 1px; -fx-border-color: #339933; -fx-border-radius: 8px;");

        stackPane.setOnMouseEntered(event -> {
            deleteButton.setVisible(true);
            NodeUtil.applyFadeTransition(deleteButton, 0, 1, 300, () -> {
            });
        });
        stackPane.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(deleteButton, 1, 0, 300, () -> {
                deleteButton.setVisible(false);
            });
        });

        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setVisible(true);
            NodeUtil.applyFadeTransition(deleteButton, 1, 0.6, 300, () -> {
            });
        });
        deleteButton.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(deleteButton, 0.6, 1, 300, () -> {
            });
        });
    }

}
