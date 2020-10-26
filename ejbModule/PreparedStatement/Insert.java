package PreparedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import Util.JDBCUtil;

public class Insert
{
	@Test
	public void insertTest() throws Exception
	{
		Connection con = JDBCUtil.getConnection();

		String sql = "insert into customer (name, email, birth) values (?, ?, ?)";
		PreparedStatement ps = con.prepareStatement(sql);
		
		ps.setString(1, "Amy");
		ps.setString(2, "Amy@qq.com");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("2000-12-21");
		ps.setDate(3, new java.sql.Date(date.getTime()));
		
		ps.execute();
		
		JDBCUtil.closeResource(con, ps);
	}
}
