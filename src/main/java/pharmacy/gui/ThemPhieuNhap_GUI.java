package pharmacy.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.itextpdf.io.exceptions.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import pharmacy.bus.ChiTietPhieuNhap_BUS;
import pharmacy.bus.NhaCungCap_BUS;
import pharmacy.bus.PhieuNhap_BUS;
import pharmacy.bus.SanPham_BUS;
import pharmacy.bus.TaiKhoan_BUS;
import pharmacy.connections.DatabaseConnection;
import pharmacy.entity.ChiTietPhieuNhap;
import pharmacy.entity.NhaCungCap;
import pharmacy.entity.NhanVien;
import pharmacy.entity.PhieuNhap;
import pharmacy.entity.SanPham;
import pharmacy.utils.NodeUtil;
import javafx.scene.text.Text;

public class ThemPhieuNhap_GUI {
	@FXML
	private Button backBtn;

	@FXML
	private HBox root;

	@FXML
	private TextField createDateField;

	@FXML
	private Button submitBtn, resetBtn;

	@FXML
	private TextField phieuNhapId;

	// @FXML
	// private ComboBox<String> productTypeField;

	@FXML
	private ComboBox<String> supplierSelect;

	// @FXML
	// private ComboBox<String> unitField;

	private List<NhaCungCap> listSupplier;
	private List<SanPham> listProductSearch;
	private List<SanPham> listProductInit;

	@FXML
	private TableView<ChiTietPhieuNhap> productTable;

	@FXML
	private TableColumn<ChiTietPhieuNhap, String> maSanPham;

	@FXML
	private TableColumn<ChiTietPhieuNhap, String> tenSanPham;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Integer> soLuongNhap;

	// @FXML
	// private TableColumn<SanPham, String> donViTinh;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Double> donGiaNhap;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Float> thue;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Double> thanhTien;

	@FXML
	private TableColumn<ChiTietPhieuNhap, Void> actionColumn;

	@FXML
	private ComboBox<String> productField;

	@FXML
	private Button addProductBtn;

	private ObservableList<ChiTietPhieuNhap> addedProductList = FXCollections.observableArrayList();

	@FXML
	private Text totalProduct, totalPrice;

	@FXML
	private Label supplierAlert, productAlert, employeeName;

	private NhanVien nhanVien;

	@FXML
	public void initialize() throws SQLException {
		setUpForm();
		handleBackBtnClick();
		handleSearchProduct();
		handleEditableProductTable();
		resetForm();
	}

	@FXML
	public void setUpForm() throws SQLException {


		actionColumn.setCellFactory(col -> new TableCell<ChiTietPhieuNhap, Void>() {
			ImageView deleteIcon = new ImageView(new Image(
                getClass().getClassLoader().getResource("images/x-icon.png").toExternalForm()));
            // private final ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/x-icon.png"))); // Đường dẫn đến icon
            private final HBox hbox = new HBox(deleteIcon);

            {
                deleteIcon.setFitHeight(15); // Kích thước icon
                deleteIcon.setFitWidth(15);
				hbox.setAlignment(Pos.CENTER);
                hbox.setStyle("-fx-padding: 5;"); 
                deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                    ChiTietPhieuNhap ctpn = getTableView().getItems().get(getIndex());
                    // Xử lý xóa sản phẩm
                    getTableView().getItems().remove(ctpn);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                }
            }
        });



		// id
		phieuNhapId.setText(generateId());

		try {
			nhanVien = new TaiKhoan_BUS().getCurrentAccount().getTenDangNhap();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		employeeName.setText(nhanVien.getHoTen());

		// create date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		createDateField.setText(LocalDateTime.now().format(formatter));

		// danh sach 20 san pham dau tien
		listProductSearch = new SanPham_BUS().getTop20SanPhamTheoSLTon();
		listProductInit = listProductSearch;
		for (SanPham sanPham : listProductSearch) {
			String displayText = sanPham.getMaSanPham() + " - " + sanPham.getTenSanPham();
			productField.getItems().add(displayText);
		}

		// loai san pham
		// productTypeField.getItems().addAll("Thuốc", "Thiết bị y tế");

		// don vi tinh
		// unitField.getItems().addAll("Viên", "Vỉ", "Hộp", "Chai", "Ống", "Gói");

		// danh sach ncc
		listSupplier = new NhaCungCap_BUS().getAllNhaCungCap();
		for (NhaCungCap supplier : listSupplier) {
			String displayText = supplier.getMaNCC() + " - " + supplier.getTenNCC();
			supplierSelect.getItems().add(displayText);
		}

		// handle if table empty
		if (addedProductList.isEmpty()) {
			Label noProductLabel = new Label("Không có sản phẩm nào trong bảng.");
			noProductLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #339933;");
			productTable.setPlaceholder(noProductLabel);
		}

		handleAddPhieuNhap();
	}

	@SuppressWarnings("unused")
	@FXML
	public void handleSearchProduct() throws SQLException {
		productField.getEditor().setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.SHIFT) {
				String searchKey = productField.getEditor().getText().trim();
				try {
					filterProducts(searchKey);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		productField.setOnAction(event -> {
			String selectedProduct = productField.getValue(); // Lấy giá trị đã chọn từ ComboBox
			if (selectedProduct != null) {
				for (SanPham sanPham : listProductSearch) {
					String displayText = sanPham.getMaSanPham() + " - " + sanPham.getTenSanPham();
					if (displayText.equals(selectedProduct)) {
						boolean isAlreadyAdded = false;

						for (ChiTietPhieuNhap existingProduct : addedProductList) {
							if (existingProduct.getSanPham().getMaSanPham().equals(sanPham.getMaSanPham())) {
								// Nếu sản phẩm đã có hiển thị thông báo lỗi
								isAlreadyAdded = true;

								// Hiển thị thông báo
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Thông báo");
								alert.setHeaderText(null);
								alert.setContentText("Sản phẩm \"" + existingProduct.getSanPham().getTenSanPham()
										+ "\" đã được chọn.");
								alert.showAndWait();

								break;
							}
						}

						// Nếu sản phẩm chưa có, thêm mới với số lượng là 1
						if (!isAlreadyAdded) {
							ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
							chiTiet.setSanPham(sanPham);
							chiTiet.setSanPham(sanPham);
							chiTiet.setDonGia(10000);
							chiTiet.setSoLuong(1);
							chiTiet.setThue(0.1f);

							addedProductList.add(chiTiet);
						}

						handleRenderAddedProductsTable(); // Cập nhật bảng
						productField.getEditor().clear(); // Xóa nội dung của ComboBox
						break;
					}
				}
			}
		});

	}

	private void filterProducts(String keySearch) throws SQLException {
		if (keySearch.length() > 0) {
			productField.getItems().clear();
			listProductSearch = new SanPham_BUS().getSanPhamByMaOrTenSP(keySearch);
			for (SanPham sanPham : listProductSearch) {
				String displayText = sanPham.getMaSanPham() + " - " + sanPham.getTenSanPham();
				productField.getItems().add(displayText);
			}
			productField.show();
		}
	}

	@SuppressWarnings("unused")
	@FXML
	public void handleAddPhieuNhap() throws SQLException {
		submitBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 1, 0.6, 200, () -> {
			});
		});
		submitBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(submitBtn, 0.6, 1, 200, () -> {
			});
		});
		submitBtn.setCursor(Cursor.HAND);

		// add phieu nhap
		submitBtn.setOnAction(event -> {
			boolean check = true;
			if (addedProductList.isEmpty()) {
				productAlert.setText("Sản phẩm không được rỗng.");
				productAlert.setVisible(true);
				check = false;
			} else {
				productAlert.setVisible(false);
			}

			String supplier = supplierSelect.getValue();
			if (supplier == null) {
				supplierAlert.setText("Nhà cung cấp không được rỗng.");
				supplierAlert.setVisible(true);
				check = false;
			} else {
				supplierAlert.setVisible(false);
			}

			if (!check){
				return;
			}

			String maPhieuNhap = phieuNhapId.getText().trim();
			LocalDateTime ngayTao = LocalDateTime.now();
			NhaCungCap selectedNCC = listSupplier.stream()
					.filter(sup -> (sup.getMaNCC() + " - " + sup.getTenNCC()).equals(supplier))
					.findFirst()
					.orElse(null);

			PhieuNhap phieuNhap = new PhieuNhap(maPhieuNhap, nhanVien, selectedNCC, ngayTao, addedProductList);
			Connection connection = null;
			try {
				connection = DatabaseConnection.getConnection();
				connection.setAutoCommit(false); // Bắt đầu giao dịch

				// Lưu phiếu nhập
				boolean isCreated = new PhieuNhap_BUS().createPhieuNhap(phieuNhap, connection);
				if (!isCreated) {
					connection.rollback(); // Rollback nếu không thể tạo phiếu nhập
					showErrorDialog("Lỗi thêm phiếu nhập",
							"Có lỗi xảy ra khi thêm phiếu nhập. Vui lòng kiểm tra lại dữ liệu.");
					return;
				}
				// Thêm chi tiết phiếu nhập
				for (ChiTietPhieuNhap sp : addedProductList) {
					ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
					chiTiet.setPhieuNhap(phieuNhap);
					chiTiet.setSanPham(sp.getSanPham());
					chiTiet.setSoLuong(sp.getSoLuong());
					chiTiet.setDonGia(sp.getDonGia());
					chiTiet.setThue(sp.getThue());

					// Lưu chi tiết phiếu nhập vào cơ sở dữ liệu
					boolean isCreatedCTPN = new ChiTietPhieuNhap_BUS().createChiTietPhieuNhap(chiTiet, connection);
					if (!isCreatedCTPN) {
						connection.rollback(); // Rollback nếu có lỗi
						showErrorDialog("Lỗi thêm chi tiết phiếu nhập", "Có lỗi xảy ra khi thêm chi tiết phiếu nhập.");
						return;
					}

					// Cập nhật số lượng tồn cho sản phẩm
					int newQuantity = sp.getSoLuong();
					boolean isUpdated = new SanPham_BUS().updateProductStock(sp.getMaSanPham(), newQuantity,
							connection);
					if (!isUpdated) {
						connection.rollback(); // Rollback nếu có lỗi
						showErrorDialog("Lỗi cập nhật tồn kho",
								"Không thể cập nhật số lượng tồn cho sản phẩm: " + sp.getMaSanPham());
						return;
					}
				}

				connection.commit(); // Xác nhận giao dịch nếu tất cả đều thành công
				showAddProductSuccessModal("Thêm phiếu nhập thành công");
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/PhieuNhap_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);

			} catch (SQLException e) {
				if (connection != null) {
					try {
						connection.rollback(); // Rollback nếu có ngoại lệ
					} catch (SQLException rollbackEx) {
						rollbackEx.printStackTrace();
					}
				}
				showErrorDialog("Lỗi thêm phiếu nhập",
						"Có lỗi xảy ra khi thêm phiếu nhập. Vui lòng kiểm tra lại dữ liệu.");
				e.printStackTrace();
			} catch (java.io.IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
				if (connection != null) {
					try {
						connection.close(); // Đóng kết nối
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	@SuppressWarnings("unused")
	@FXML
	public void resetForm() {
		resetBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(resetBtn, 1, 0.5, 200, () -> {
			});
		});

		resetBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(resetBtn, 0.5, 1, 200, () -> {
			});
		});

		resetBtn.setOnMouseClicked(event -> {
			try {
				phieuNhapId.setText(generateId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			createDateField.setText(LocalDateTime.now().format(formatter));

			productField.getItems().clear();
			for (SanPham sanPham : listProductInit) {
				String displayText = sanPham.getMaSanPham() + " - " + sanPham.getTenSanPham();
				productField.getItems().add(displayText);
			}

			supplierSelect.getItems().clear();
			listSupplier = new NhaCungCap_BUS().getAllNhaCungCap();
			for (NhaCungCap supplier : listSupplier) {
				String displayText = supplier.getMaNCC() + " - " + supplier.getTenNCC();
				supplierSelect.getItems().add(displayText);
			}
		});
	}

	@SuppressWarnings("unused")
	@FXML
	public void handleBackBtnClick() {
		backBtn.setOnMouseEntered(event -> {
			NodeUtil.applyFadeTransition(backBtn, 1, 0.5, 200, () -> {
			});
		});

		backBtn.setOnMouseExited(event -> {
			NodeUtil.applyFadeTransition(backBtn, 0.5, 1, 200, () -> {
			});
		});

		backBtn.setOnMouseClicked(event -> {
			try {
				Parent customerFrame = FXMLLoader.load(getClass().getResource("/fxml/PhieuNhap_GUI.fxml"));
				root.getChildren().clear();
				root.getChildren().add(customerFrame);
			} catch (IOException | java.io.IOException e) {
				e.printStackTrace();
			}
		});
	}

	@SuppressWarnings("unused")
	@FXML
	public void handleRenderAddedProductsTable() {
		productTable.setItems(addedProductList);

		maSanPham.setCellValueFactory(new PropertyValueFactory<>("maSanPham"));
		tenSanPham.setCellValueFactory(new PropertyValueFactory<>("tenSanPham"));
		soLuongNhap.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
		donGiaNhap.setCellValueFactory(new PropertyValueFactory<>("donGia"));
		thue.setCellValueFactory(new PropertyValueFactory<>("thue"));
		thanhTien.setCellValueFactory(new PropertyValueFactory<>("thanhTien")); // Giữ kiểu Double

		// Định dạng khi hiển thị
		thanhTien.setCellFactory(col -> new TableCell<ChiTietPhieuNhap, Double>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
				} else {
					NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
					setText(formatter.format(item)); // Định dạng thành tiền
				}
			}
		});
		updateTotalProductCount();
		updateTotalAmount();
	}

	public void updateTotalProductCount() {
		int totalCount = 0;

		for (ChiTietPhieuNhap productDetail : addedProductList) {
			totalCount += productDetail.getSoLuong();
		}

		// Cập nhật văn bản của Text
		totalProduct.setText(String.valueOf(totalCount));
	}

	private void updateTotalAmount() {
		double totalAmount = calculateTotalAmount();

		// Định dạng tổng tiền thành tiền Việt Nam
		NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
		totalPrice.setText(formatter.format(totalAmount)); // Hiển thị tổng tiền
	}

	private double calculateTotalAmount() {
		double total = 0.0;

		for (ChiTietPhieuNhap item : addedProductList) {
			double donGia = item.getDonGia();
			int soLuong = item.getSoLuong();
			float thue = item.getThue();

			// Tính thành tiền cho từng sản phẩm
			double thanhTien = donGia * soLuong * (1 + thue); // Nếu thue là tỷ lệ phần trăm

			total += thanhTien; // Cộng dồn vào tổng
		}

		return total; // Trả về tổng tiền
	}

	// edit on table product
	@FXML
	public void handleEditableProductTable() {
		setFloatComboBoxColumnEditable(thue, "thue", new String[] { "0%", "5%", "10%", "15%", "20%" });

		soLuongNhap.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		soLuongNhap.setOnEditCommit(event -> {
			ChiTietPhieuNhap ctpn = event.getRowValue();
			ctpn.setSoLuong(event.getNewValue());
			updateTotalProductCount();
			updateTotalAmount();
		});

		donGiaNhap.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		donGiaNhap.setOnEditCommit(event -> {
			ChiTietPhieuNhap ctpn = event.getRowValue();
			ctpn.setDonGia(event.getNewValue());
			updateTotalProductCount();
			updateTotalAmount();
		});

	}

	private void setFloatComboBoxColumnEditable(TableColumn<ChiTietPhieuNhap, Float> column, String property,
			String[] options) {
		column.setCellFactory(col -> new TableCell<ChiTietPhieuNhap, Float>() {
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
			ChiTietPhieuNhap ctpn = event.getRowValue();
			ctpn.setThue(event.getNewValue());
			updateTotalProductCount();
			updateTotalAmount();
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

	private String generateId() throws SQLException {
		int count = new PhieuNhap_BUS().countPhieuNhap();
		String id = String.format("PN%04d", count + 1);

		return id;
	}

	// succes modal
	@SuppressWarnings("unused")
	@FXML
	private void showAddProductSuccessModal(String message) {
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

	private void showErrorDialog(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null); // No header text
		alert.setContentText(message);
		alert.showAndWait(); // Display the alert and wait for it to be dismissed
	}

	
}
