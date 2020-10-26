package Connect;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

public class DBCPConnections
{
	@Test
	public void test() throws Exception
	{
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
		
		Properties properties = new Properties();
		properties.load(is);
		
		DataSource source = BasicDataSourceFactory.createDataSource(properties);
		
		Connection connection = source.getConnection();
		
		System.out.println(connection);
	}
}
