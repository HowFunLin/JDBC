package Connect;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Connections
{
	@Test
	public void test() throws SQLException
	{
		ComboPooledDataSource cpds = new ComboPooledDataSource("C3P0");
		Connection connection = cpds.getConnection();
		System.out.println(connection);
	}
}
