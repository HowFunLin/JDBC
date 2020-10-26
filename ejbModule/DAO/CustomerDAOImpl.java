package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import PreparedStatement.Customer;

public class CustomerDAOImpl extends BaseDAO<Customer> implements CustomerDAO
{
	@Override
	public void insert(Connection con, Customer cust)
	{
		String sql = "insert into customer(name, email, birth) values (?, ?, ?)";
		commonUpdateTransaction(con, sql, cust.getName(), cust.getEmail(), cust.getBirth());
	}

	@Override
	public void deleteById(Connection con, int id)
	{
		String sql = "delete from customer where id = ?";
		commonUpdateTransaction(con, sql, id);
	}

	@Override
	public void update(Connection con, Customer cust)
	{
		String sql = "update customer set name = ?, email = ?, birth = ? where id = ?";
		commonUpdateTransaction(con, sql, cust.getName(), cust.getEmail(), cust.getBirth(), cust.getId());
	}

	@Override
	public Customer getCustomerById(Connection con, int id)
	{
		String sql = "select id, name, email, birth from customer where id = ?";
		return commonSelectTransaction(con, sql, id);
	}

	@Override
	public List<Customer> getAll(Connection con)
	{
		String sql = "select id, name, email, birth from customer";
		return commonSelectListTransaction(con, sql);
	}

	@Override
	public Long getCount(Connection con)
	{
		String sql = "select count(*) from customer";
		return getValue(con, sql);
	}

	@Override
	public Date getMaxBirth(Connection con)
	{
		String sql = "select max(birth) from customer";
		return getValue(con, sql);
	}
}