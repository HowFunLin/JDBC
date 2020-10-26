package PreparedStatement;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import Util.JDBCUtil;

public class GetBlob
{
	@Test
	public void test() throws Exception
	{
		Connection connection = JDBCUtil.getConnection();
		
		String sql = "select photo from customer where id = ?";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.setInt(1, 2);
		
		ResultSet resultSet = prepareStatement.executeQuery();
		resultSet.next();
		
		Blob blob = resultSet.getBlob(1);
		InputStream inputStream = blob.getBinaryStream();
		FileOutputStream fos = new FileOutputStream("newTrio.jpg");
		
		byte[] b = new byte[1024];
		int len;
		while((len = inputStream.read(b)) != -1) fos.write(b, 0, len);
		
		fos.close();
		
		JDBCUtil.closeResource(connection, prepareStatement, resultSet);
	}
}
