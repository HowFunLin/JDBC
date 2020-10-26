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
	
	//����ʽ��ʹ��API����ǿ����ֲ��
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
		//MySQL��Driverʵ�����еľ�̬������ڼ�����ʱ�Զ�ע������
		/* ���´������ʡ��
		Class<?> drive = Class.forName("com.mysql.jdbc.Driver");
		Driver driver = (Driver) drive.getConstructor().newInstance();
		DriverManager.registerDriver(Driver);
		*/
		//MySQL����ֱ��ʡ�Լ���Class����Ĳ��裬���������ݿ���һ��
		//Class.forName("com.mysql.jdbc.Driver");
		
		String url = "jdbc:mysql://localhost:3306/test";
		String user = "root";
		String password = "123698745lin";
		
		Connection con = DriverManager.getConnection(url, user, password);
		System.out.println(con);
	}
	
	//����ʹ��
	//ʵ�ִ�������õķ��룬�����޸����ú�������´��
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
		
		//��ʡ�Ե�������
		Class.forName(driverClass);
		
		Connection con = DriverManager.getConnection(url, user, password);
		System.out.println(con);
	}
}
