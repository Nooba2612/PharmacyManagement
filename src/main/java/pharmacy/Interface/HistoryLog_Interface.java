package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.ProductHistoryLog;

public interface HistoryLog_Interface {
    void addProductHistory(ProductHistoryLog history);

    List<ProductHistoryLog> getAllProductHistory();
}
