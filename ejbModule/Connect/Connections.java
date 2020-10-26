package Connect;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

public class Connections
{
	@Test
	public void connection1() throws SQLException
	{
		Driver driver = new com.mysql.jdbc.Driver();
		
		String url = "jdbc:mysql://localhost:3306/test";
		
		Properties info = new Properties();
		
		info.setProperty("user", "root");
		info.setProperty("password", "123698745lin");
		
		Connection con = driver.connect(url, info);
		
		System.out.println(con);
	}
	
	//不显式地使用API，增强可移植性
	@Test
	public void connection2() throws SQLException, ReflectiveOperationException 
	{
		Class<?> drive = Class.forName("com.mysql.jdbc.Driver");
		Driver driver = (Driver) drive.getConstructor().newInstance();
		
		String url = "jdbc:mysql://localhost:3306/test";
		
		Properties info = new Properties();
		
		info.setProperty("user", "root");
		info.setProperty("password", "123698745lin");
		
		Connection con = driver.connect(url, info);
		
		System.out.println(con);
	}
	
	@Test
	public void connection3() throws SQLException
	{
		//MySQL的Driver实现类中的静态代码块在加载类时自动注册驱动
		/* 以下代码可以省略
		Class<?> drive = Class.forName("com.mysql.jdbc.Driver");
		Driver driver = (Driver) drive.getConstructor().newInstance();
		DriverManager.registerDriver(Driver);
		*/
		//MySQL可以直接省略加载Class对象的步骤，而其他数据库则不一定
		//Class.forName("com.mysql.jdbc.Driver");
		
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "123698745lin";
		
		Connection con = DriverManager.getConnection(url, user, password);
		System.out.println(con);
	}
	
	//建议使用
	//实现代码和配置的分离，避免修改配置后程序重新打包
	@Test
	public void connection4() throws SQLException, IOException, ClassNotFoundException
	{
		InputStream is = Connections.class.getResourceAsStream("jdbc.properties");
		
		Properties pros = new Properties();
		pros.load(is);
		
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");
		
		//可省略但不建议
		Class.forName(driverClass);
		
		Connection con = DriverManager.getConnection(url, user, password);
		System.out.println(con);
	}
}
