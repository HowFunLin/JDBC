package PreparedStatement;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import Util.JDBCUtil;

public class UpdateBlob
{
	@Test
	public void test() throws Exception
	{
		Connection connection = JDBCUtil.getConnection();
		
		String sql = "Update customer set photo = ? where id = ?";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.setBlob(1, new FileInputStream("AlumniMeeting.jpg"));
		prepareStatement.setInt(2, 2);
		
		prepareStatement.execute();
		
		JDBCUtil.closeResource(connection, prepareStatement);
	}
}
