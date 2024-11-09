package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.CustomerHistoryLog;
import pharmacy.entity.EmployeeHistoryLog;
import pharmacy.entity.ProductHistoryLog;
import pharmacy.entity.SupplierHistoryLog;

public interface HistoryLog_Interface {
    void addProductHistory(ProductHistoryLog history);

    List<ProductHistoryLog> getProductHistoryById(String productId);

    void addEmployeeHistory(EmployeeHistoryLog history);

    List<EmployeeHistoryLog> getEmployeeHistoryById(String employeeId);

    void addCustomerHistory(CustomerHistoryLog history);

    List<CustomerHistoryLog> getCustomerHistoryById(String customerId);

    void addSupplierHistory(SupplierHistoryLog history);

    List<SupplierHistoryLog> getSupplierHistoryById(String supplierId);
}
