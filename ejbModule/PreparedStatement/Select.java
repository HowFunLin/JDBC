package PreparedStatement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import Util.JDBCUtil;

public class Select
{
	@Test
	public void test() throws Exception
	{
		Connection con = JDBCUtil.getConnection();
		
		String sql = "select id, name, email, birth from customer where id = ?";
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, 1);
		
		ResultSet rs = ps.executeQuery();
		if(rs.next())
		{
			int id = rs.getInt(1);
			String name = rs.getString(2);
			String email = rs.getString(3);
			Date birth = rs.getDate(4);
			
			//使用对象存储数据库中的数据
			Customer c = new Customer(id, name, email, birth);
			System.out.println(c);
		}
		
		JDBCUtil.closeResource(con, ps, rs);
	}
}