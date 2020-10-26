package Transaction;

import java.sql.Connection;

import org.junit.Test;

import Util.JDBCUtil;

public class Isolation
{
	@Test
	public void selectTest() throws Exception
	{
		Connection connection = JDBCUtil.getConnection();
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		
		String sql = "select username, password, balance from user where username = ?";
		User user = JDBCUtil.commonSelectTransaction(connection, User.class, sql, "AA");
		
		System.out.println(user);
		
		connection.close();
	}
	
	@Test
	public void updateTest() throws Exception
	{
		Connection connection = JDBCUtil.getConnection();
		connection.setAutoCommit(false);
		
		String sql = "update user set balance = ? where username = ?";
		JDBCUtil.commonUpdateTransaction(connection, sql, 2000, "AA");
		
		//Thread.sleep(8000);
		System.out.println("Update success!");
	}
}
