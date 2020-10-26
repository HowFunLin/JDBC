package PreparedStatement;

import org.junit.Test;

import Util.JDBCUtil;

public class CommonSelectTest
{
	@Test
	public void test() throws Exception
	{
		String sql = "select id, name, email, birth from customer where id = ?";
		Customer customer = JDBCUtil.commonSelect(Customer.class, sql, 1);
		System.out.println(customer);
	}
}
