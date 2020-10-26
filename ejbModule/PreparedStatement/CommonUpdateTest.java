package PreparedStatement;

import org.junit.Test;

import Util.JDBCUtil;

public class CommonUpdateTest
{
	@Test
	public void test() throws Exception
	{
		String sql = "delete from customer where id = ?";
		JDBCUtil.commonUpdate(sql, 1);
	}
}
