package pharmacy.bus;

import java.sql.SQLException;
import java.util.List;

import pharmacy.dao.HistoryLog_DAO;
import pharmacy.entity.CustomerHistoryLog;
import pharmacy.entity.EmployeeHistoryLog;
import pharmacy.entity.ProductHistoryLog;
import pharmacy.entity.SupplierHistoryLog;

public class HistoryLog_BUS {

    public static final SupplierHistoryLog DateTimeFormatter = null;
    private final HistoryLog_DAO historyLogDAO;

    public HistoryLog_BUS() throws SQLException {
        this.historyLogDAO = new HistoryLog_DAO();
    }

    public void addProductHistory(ProductHistoryLog history) {
        historyLogDAO.addProductHistory(history);
    }

    public List<ProductHistoryLog> getProductHistoryById(String productId) {
        return historyLogDAO.getProductHistoryById(productId);
    }

    public void addEmployeeHistory(EmployeeHistoryLog history) {
        historyLogDAO.addEmployeeHistory(history);
    }

    public List<EmployeeHistoryLog> getEmployeeHistoryById(String employeeId) {
        return historyLogDAO.getEmployeeHistoryById(employeeId);
    }

    public void addCustomerHistory(CustomerHistoryLog history) {
        historyLogDAO.addCustomerHistory(history);
    }

    public List<CustomerHistoryLog> getCustomerHistoryById(String customerId) {
        return historyLogDAO.getCustomerHistoryById(customerId);
    }

    public void addSupplierHistory(SupplierHistoryLog history) {
        historyLogDAO.addSupplierHistory(history);
    }

    public List<SupplierHistoryLog> getSupplierHistoryById(String supplierId) {
        return historyLogDAO.getSupplierHistoryById(supplierId);
    }

}
