package pharmacy.Interface;

import java.util.List;

import pharmacy.entity.CustomerHistoryLog;


public interface HistoryLogCustomer_Interface {
	void addCustomerHistory(CustomerHistoryLog history);

	List<CustomerHistoryLog> getAllCusHistory();
}