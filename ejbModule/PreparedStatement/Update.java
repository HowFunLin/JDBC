package PreparedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import Util.JDBCUtil;

public class Update
{
	@Test
	public void updateTest() throws Exception
	{
		Connection con = JDBCUtil.getConnection();

		String sql = "update customer set name = ? where id = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		
		ps.setString(1, "Black");
		ps.setInt(2, 1);
		
		ps.execute();
		
		JDBCUtil.closeResource(con, ps);
	}
}
