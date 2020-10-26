package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import PreparedStatement.Customer;

public interface CustomerDAO
{
	void insert(Connection con, Customer cust);
	
	void deleteById(Connection con, int id);
	
	void update(Connection con, Customer cust);
	
	Customer getCustomerById(Connection con, int id);
	
	List<Customer> getAll(Connection con);
	
	Long getCount(Connection con);
	
	Date getMaxBirth(Connection con);
}
