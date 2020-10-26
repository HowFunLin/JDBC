package PreparedStatement;

import java.util.List;

import org.junit.Test;

import Util.JDBCUtil;

public class CommonSelectListTest
{
	@Test
	public void test() throws Exception
	{
		String sql = "select id, name, email, birth from customer where id <= ?";
		List<Customer> list = JDBCUtil.commonSelectList(Customer.class, sql, 2);
		list.forEach(System.out::println);
	}
}
