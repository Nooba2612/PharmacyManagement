package pharmacy.gui;

import java.sql.SQLException;
import java.time.LocalDate;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pharmacy.bus.LichLamViec_BUS;
import pharmacy.bus.NhanVien_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.entity.LichLamViec;
import pharmacy.entity.NhanVien;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;

public class ThemLichLam_GUI {

    @FXML
    private Button addScheduleBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField employeeField;

    @FXML
    private ScrollPane suggestBox;

    @FXML
    private ListView<NhanVien> employeeSuggestList;

    private FilteredList<NhanVien> filteredList;

    private NhanVien selectedEmployee;

    @FXML
    public void initialize(String caLam, LocalDate ngayLam) {
        handleSuggestEmployees();
        handleAddScheduleBtnAction();
    }

    @FXML
    public void handleAddScheduleBtnAction() {
        addScheduleBtn.setDisable(true);
        addScheduleBtn.setOnMouseEntered(event -> {
            NodeUtil.applyFadeTransition(addScheduleBtn, 1, 0.7, 300, () -> {
            });
        });

        addScheduleBtn.setOnMouseExited(event -> {
            NodeUtil.applyFadeTransition(addScheduleBtn, 0.7, 1, 300, () -> {
            });
        });

        addScheduleBtn.setOnAction(event -> {
            NhanVien nhanVien = new NhanVien_BUS().getEmployeeByMaNhanVien(selectedEmployee.getMaNhanVien());
            // LichLamViec lichLamViec = new LichLamViec(generateId(), nhanVien);
            // new LichLamViec_BUS().createLichLamViec(lichLamViec);

            Stage stage = (Stage) addScheduleBtn.getScene().getWindow();
            stage.close();
        });

        if (!filteredList.isEmpty()) {
            addScheduleBtn.setDisable(false);
        } else {
            addScheduleBtn.setDisable(true);
        }

    }

    private String generateId() throws SQLException {
        int scheduleNumber = new LichLamViec_BUS().getAllLichLamViec().size();
        String id = String.format("LLV%04d", scheduleNumber + 1);

        return id;
    }

    @FXML
    public void handleSuggestEmployees() {
        suggestBox.setVisible(false);

        // handle input employee info and show suggestion box
        ObservableList<NhanVien> employeeList = FXCollections.observableArrayList(new NhanVien_BUS().getAllEmployees());
        filteredList = new FilteredList<>(employeeList, b -> true);
        employeeField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(employee -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return employee.getMaNhanVien().toLowerCase().contains(lowerCaseFilter)
                        || employee.getHoTen().toLowerCase().contains(lowerCaseFilter);
            });

            if (filteredList.isEmpty()) {
                NodeUtil.applyFadeTransition(suggestBox, 1, 0, 200, () -> {
                });
                NodeUtil.applyTranslateYTransition(suggestBox, 0, -15, 200, () -> {
                    suggestBox.setVisible(false);
                });
            } else {
                suggestBox.setVisible(true);
                NodeUtil.applyFadeTransition(suggestBox, 0, 1, 200, () -> {
                });
                NodeUtil.applyTranslateYTransition(suggestBox, -15, 0, 200, () -> {
                });
            }

        });

        if (employeeField.getText().equals("")) {
            NodeUtil.applyFadeTransition(suggestBox, 1, 0, 200, () -> {
            });
            NodeUtil.applyTranslateYTransition(suggestBox, 0, -15, 200, () -> {
                suggestBox.setVisible(false);
            });
        }

        searchBtn.setOnAction((event) -> {
            filteredList.setPredicate(employee -> {
                if (employeeField.getText() == null || employeeField.getText().isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = employeeField.getText().toLowerCase();
                return employee.getMaNhanVien().toLowerCase().contains(lowerCaseFilter)
                        || employee.getHoTen().toLowerCase().contains(lowerCaseFilter);
            });

            if (filteredList.isEmpty()) {
                NodeUtil.applyFadeTransition(suggestBox, 1, 0, 200, () -> {
                });
                NodeUtil.applyTranslateYTransition(suggestBox, 0, -15, 200, () -> {
                    suggestBox.setVisible(false);
                });
            } else {
                suggestBox.setVisible(true);
            }

        });

        employeeSuggestList.setItems(filteredList);
        employeeSuggestList.setCellFactory(listView -> new ListCell<NhanVien>() {
            @Override
            protected void updateItem(NhanVien item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMaNhanVien() + " - " + item.getHoTen());
                }
            }
        });

        employeeSuggestList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                Platform.runLater(() -> {
                    employeeField.setText(newValue.getMaNhanVien() + " - " + newValue.getHoTen());
                    selectedEmployee = new NhanVien_BUS().getEmployeeByMaNhanVien(newValue.getMaNhanVien());
                });

                NodeUtil.applyFadeTransition(suggestBox, 1, 0, 200, () -> {
                });
                NodeUtil.applyTranslateYTransition(suggestBox, 0, -15, 200, () -> {
                    suggestBox.setVisible(false);

                    Platform.runLater(() -> {
                        if (!employeeSuggestList.getItems().isEmpty() && employeeSuggestList.getSelectionModel().getSelectedItem() != null) {
                            employeeSuggestList.getSelectionModel().clearSelection();
                        }
                    });
                });
            }
        });

    }

    @FXML
    public NhanVien getEmployee() {
        return selectedEmployee;
    }
}
