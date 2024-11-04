package pharmacy.bus;

import java.sql.SQLException;
import java.util.List;

import pharmacy.dao.HistoryLogCustomer_DAO;
import pharmacy.entity.CustomerHistoryLog;

public class HistoryLogCustomer_BUS {

    private final HistoryLogCustomer_DAO historyCustomerDAO;

    public HistoryLogCustomer_BUS() throws SQLException {
        this.historyCustomerDAO = new HistoryLogCustomer_DAO();
    }

    public void addCustomerHistory(CustomerHistoryLog history) {
        historyCustomerDAO.addCustomerHistory(history);
    }

    public List<CustomerHistoryLog> getAllCusHistory() {
        return historyCustomerDAO.getAllCusHistory();
    }
}
