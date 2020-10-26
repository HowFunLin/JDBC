package PreparedStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import Util.JDBCUtil;

public class BatchInsert
{
	@Test
	public void test() throws Exception
	{
		Connection connection = JDBCUtil.getConnection();
		connection.setAutoCommit(false);
		
		String sql = "insert into `batch` (name) values (?)";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		
		for(int i = 1; i <= 200000; i++) 
		{
			prepareStatement.setString(1, "Object" + i);
			
			//prepareStatement.execute();
			
			prepareStatement.addBatch();
			
			if(i % 500 == 0)
			{
				prepareStatement.executeBatch();
				prepareStatement.clearBatch();
			}
		}
		
		connection.commit();
		
		JDBCUtil.closeResource(connection, prepareStatement);
	}
}
