package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.EmployeeHistoryLog;
import pharmacy.entity.ProductHistoryLog;

public interface HistoryLog_Interface {
    void addProductHistory(ProductHistoryLog history);

    List<ProductHistoryLog> getProductHistoryById(String productId);

    void addEmployeeHistory(EmployeeHistoryLog history);

    List<EmployeeHistoryLog> getEmployeeHistoryById(String employeeId);
}
