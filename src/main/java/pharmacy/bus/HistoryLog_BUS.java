package pharmacy.bus;

import java.sql.SQLException;
import java.util.List;

import pharmacy.dao.HistoryLog_DAO;
import pharmacy.entity.ProductHistoryLog;

public class HistoryLog_BUS {
    private final HistoryLog_DAO historyLogDAO;

    public HistoryLog_BUS() throws SQLException {
        this.historyLogDAO = new HistoryLog_DAO();
    }

    public void addProductHistory(ProductHistoryLog history) {
        historyLogDAO.addProductHistory(history);
    }

    public List<ProductHistoryLog> getAllProductHistory() {
        return historyLogDAO.getAllProductHistory();
    }
}
