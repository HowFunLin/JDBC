package Connect;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DruidConnections
{
	@Test
	public void test() throws Exception
	{
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
		
		Properties properties = new Properties();
		properties.load(is);
		
		DataSource source = DruidDataSourceFactory.createDataSource(properties);
		
		Connection connection = source.getConnection();
		
		System.out.println(connection);
	}
}
