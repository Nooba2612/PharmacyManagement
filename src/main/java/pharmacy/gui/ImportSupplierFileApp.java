package pharmacy.gui;

import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import io.github.cdimascio.dotenv.Dotenv;

public class ImportSupplierFileApp extends Application {
    private static final Dotenv dotenv = Dotenv.load();

    private static final String DB_URL = dotenv.get("DB_URL");
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASSWORD = dotenv.get("DB_PASSWORD");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Import File CSV từ Nhà Cung Cấp");

        // Nút để chọn file CSV
        Button btnImport = new Button("Nhập file CSV");
        btnImport.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                try {
                    importCSVToDatabase(selectedFile);
                } catch (CsvValidationException e) {
                    e.printStackTrace();
                }
            }
        });

        VBox root = new VBox(10, btnImport);
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void importCSVToDatabase(File file) throws CsvValidationException {
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            String[] header = csvReader.readNext(); // Đọc dòng tiêu đề

            // Kiểm tra các cột trong file CSV
            String[] requiredColumns = {"maSanPham", "tenSanPham", "danhMuc", "loaiSanPham", "ngaySX", "nhaSX", "ngayTao", "soLuongTon", "donGiaBan", "thue", "hanSuDung", "donViTinh", "moTa", "trangThai"};
            for (String column : requiredColumns) {
                if (!java.util.Arrays.asList(header).contains(column)) {
                    System.out.println("Thiếu cột yêu cầu: " + column);
                    return;
                }
            }

            // Kết nối tới CSDL
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String insertSQL = "INSERT INTO SanPham (maSanPham, tenSanPham, danhMuc, loaiSanPham, ngaySX, nhaSX, ngayTao, soLuongTon, donGiaBan, thue, hanSuDung, donViTinh, moTa, trangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy"); // Định dạng ngày trong CSV
                SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                String[] line;
                int successfulInserts = 0;
                while ((line = csvReader.readNext()) != null) {
                    try {
                        preparedStatement.setString(1, line[0]); // maSanPham
                        preparedStatement.setString(2, line[1]); // tenSanPham
                        preparedStatement.setString(3, line[2]); // danhMuc
                        preparedStatement.setString(4, line[3]); // loaiSanPham
                        preparedStatement.setString(5, convertDate(line[4], inputDateFormat, sqlDateFormat)); // ngaySX
                        preparedStatement.setString(6, line[5]); // nhaSX
                        preparedStatement.setString(7, convertDate(line[6], inputDateFormat, sqlDateFormat)); // ngayTao

                        // Kiểm tra và chuyển đổi soLuongTon
                        try {
                            preparedStatement.setInt(8, Integer.parseInt(line[7])); // soLuongTon
                        } catch (NumberFormatException e) {
                            preparedStatement.setInt(8, 0); // Nếu lỗi, gán mặc định là 0
                            System.out.println("Lỗi khi chuyển đổi soLuongTon: " + line[7]);
                        }

                        // Kiểm tra và chuyển đổi donGiaBan
                        try {
                            preparedStatement.setDouble(9, Double.parseDouble(line[8])); // donGiaBan
                        } catch (NumberFormatException e) {
                            preparedStatement.setDouble(9, 0.0); // Nếu lỗi, gán mặc định là 0
                            System.out.println("Lỗi khi chuyển đổi donGiaBan: " + line[8]);
                        }

                        // Kiểm tra và chuyển đổi thue
                        try {
                            preparedStatement.setFloat(10, Float.parseFloat(line[9])); // thue
                        } catch (NumberFormatException e) {
                            preparedStatement.setFloat(10, 0.0f); // Nếu lỗi, gán mặc định là 0
                            System.out.println("Lỗi khi chuyển đổi thue: " + line[9]);
                        }

                        preparedStatement.setString(11, convertDate(line[10], inputDateFormat, sqlDateFormat)); // hanSuDung
                        preparedStatement.setString(12, line[11]); // donViTinh
                        preparedStatement.setString(13, line[12]); // moTa
                        preparedStatement.setString(14, line[13]); // trangThai

                        preparedStatement.addBatch();
                        successfulInserts++;
                    } catch (SQLException e) {
                        System.out.println("Lỗi khi chèn dữ liệu cho sản phẩm: " + line[0] + ": " + e.getMessage());
                    }
                }

                preparedStatement.executeBatch(); // Thực hiện các câu lệnh SQL cùng lúc
                System.out.println("Đã chèn thành công " + successfulInserts + " sản phẩm vào cơ sở dữ liệu.");
            } catch (SQLException e) {
                System.out.println("Lỗi khi nhập dữ liệu vào CSDL: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file CSV: " + e.getMessage());
        }
    }

    private String convertDate(String dateStr, SimpleDateFormat inputFormat, SimpleDateFormat outputFormat) {
        try {
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            System.out.println("Lỗi định dạng ngày: " + dateStr);
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
