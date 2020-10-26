package Util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 实现JDBC对数据库的操作的工具类
 * @author Riyad
 * @version 2.0
 */
public abstract class JDBCUtil
{
	/**
	 * 获取JDBC与数据库的连接
	 * @return 可对当前数据库进行操作的Connection对象
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception
	{
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
		
		Properties pros = new Properties();
		pros.load(is);
		
		String user = pros.getProperty("user");
		String password = pros.getProperty("password");
		String url = pros.getProperty("url");
		String driverClass = pros.getProperty("driverClass");
		
		Class.forName(driverClass);
		
		Connection con = DriverManager.getConnection(url, user, password);
		
		return con;
	}
	
	//无需再每次调用时都创建连接池
	private static ComboPooledDataSource cpds = new ComboPooledDataSource("C3P0");
	
	/**
	  *  实现C3P0数据库连接池，获取连接
	 * @return 连接池中的连接对应的Connection对象
	 * @throws SQLException
	 */
	public static Connection getConnectionC3P0() throws SQLException
	{
		Connection connection = cpds.getConnection();
		return connection;
	}
	
	//无需再每次调用时都创建连接池
	private static DataSource source;
	
	static
	{
		try
		{
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
			
			Properties properties = new Properties();
			properties.load(is);
			
			source = BasicDataSourceFactory.createDataSource(properties);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	  *  实现DBCP连接池，获取连接
	 * @return 连接池中的连接对应的Connection对象
	 * @throws SQLException
	 */
	public static Connection getConnectionDBCP() throws SQLException
	{
		Connection connection = source.getConnection();
		return connection;
	}
	
	//无需再每次调用时都创建连接池
	private static DataSource dataSource;
		
	static
	{
		try
		{
			InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
			
			Properties properties = new Properties();
			properties.load(is);
				
			dataSource = DruidDataSourceFactory.createDataSource(properties);
		} 
		catch (Exception e)
		{
				e.printStackTrace();
		}
	}
		
	/**
	  *  实现Druid连接池，获取连接
	 * @return 连接池中的连接对应的Connection对象
	 * @throws SQLException
	 */
	public static Connection getConnectionDruid() throws SQLException
	{
		Connection connection = dataSource.getConnection();
		return connection;
	}
	
	/**
	 * 用于通用增删改操作之后关闭与数据库的连接
	 * @param con 用于连接的Connection对象
	 * @param ps 发送SQL命令的Statement对象
	 */
	public static void closeResource(Connection con, Statement ps)
	{
		if(con != null)
		{
			try
			{
				con.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if(ps != null)
		{
			try
			{
				ps.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 通用的增删改操作
	 * @param sql 所需执行的SQL语句
	 * @param objects 用于填充占位符的对象数组
	 * @throws Exception
	 */
	public static void commonUpdate(String sql, Object... objects) throws Exception
	{
		Connection con = JDBCUtil.getConnection();
		
		PreparedStatement ps = con.prepareStatement(sql);
		
		for(int i = 0; i < objects.length; i++)
		{
			ps.setObject(i + 1, objects[i]);
		}
		
		ps.execute();
		
		JDBCUtil.closeResource(con, ps);
	}
	
	/**
	 * 用于查询操作之后关闭与数据库的连接
	 * @param con 用于连接的Connection对象
	 * @param ps 发送SQL命令的Statement对象
	 * @param rs 接收查询结果的集合
	 */
	public static void closeResource(Connection con, Statement ps, ResultSet rs)
	{
		if(con != null)
		{
			try
			{
				con.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if(ps != null)
		{
			try
			{
				ps.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if(rs != null)
		{
			try
			{
				rs.close();
			} catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 通用的查询操作
	 * @param <T> 返回的存储查询到的信息的对象所属的类型
	 * @param c	对象所属类的Class对象
	 * @param sql 要执行查询的SQL语句
	 * @param objects 用于填充占位符的对象数组
	 * @return 存储查询到的信息的对象
	 * @throws Exception
	 */
	public static <T> T commonSelect(Class<T> c, String sql, Object...objects) throws Exception
	{
		Connection con = JDBCUtil.getConnection();
		
		PreparedStatement ps = con.prepareStatement(sql);
		for(int i = 0; i < objects.length; i++)	ps.setObject(i + 1, objects[i]);
		
		ResultSet rs = ps.executeQuery();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		
		if(rs.next())
		{
			T t = c.getConstructor().newInstance();
			
			for(int i = 0; i < count; i++)
			{
				Object value = rs.getObject(i + 1);
				String name = rsmd.getColumnLabel(i + 1);
				
				Field field = c.getDeclaredField(name);
				field.setAccessible(true);
				field.set(t, value);
			}
			
			JDBCUtil.closeResource(con, ps, rs);
			
			return t;
		}
		
		return null;
	}
	
	/**
	 * 通用的查询操作
	 * @param <T> 返回的存储查询到的信息的对象所属的类型
	 * @param c 对象所属类的Class对象
	 * @param sql 要执行查询的SQL语句
	 * @param objects 用于填充占位符的对象数组
	 * @return 存储查询到的信息的对象的集合
	 * @throws Exception
	 */
	public static <T> List<T> commonSelectList(Class<T> c, String sql, Object...objects) throws Exception
	{
		Connection con = JDBCUtil.getConnection();
		
		PreparedStatement ps = con.prepareStatement(sql);
		for(int i = 0; i < objects.length; i++)	ps.setObject(i + 1, objects[i]);
		
		ResultSet rs = ps.executeQuery();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		
		ArrayList<T> list = new ArrayList<T>();
		
		while(rs.next())
		{
			T t = c.getConstructor().newInstance();
			
			for(int i = 0; i < count; i++)
			{
				Object value = rs.getObject(i + 1);
				String name = rsmd.getColumnLabel(i + 1);
				
				Field field = c.getDeclaredField(name);
				field.setAccessible(true);
				field.set(t, value);
			}
			
			list.add(t);
		}
		
		JDBCUtil.closeResource(con, ps, rs);
		
		return list;
	}
	
	/**
	 * 考虑事务之后的通用增删改操作
	 * @param con 与数据库的连接
	 * @param sql 要执行的SQL语句
	 * @param objects 用于填充占位符的对象数组
	 * @throws Exception
	 */
	public static void commonUpdateTransaction(Connection con, String sql, Object... objects) throws Exception
	{
		PreparedStatement ps = con.prepareStatement(sql);
		
		for(int i = 0; i < objects.length; i++)
		{
			ps.setObject(i + 1, objects[i]);
		}
		
		ps.execute();
		
		JDBCUtil.closeResource(null, ps);
	}
	
	/**
	 * 考虑事务之后的通用的查询操作
	 * @param <T> 返回的存储查询到的信息的对象所属的类型
	 * @param con 与数据库的连接
	 * @param c	对象所属类的Class对象
	 * @param sql 要执行查询的SQL语句
	 * @param objects 用于填充占位符的对象数组
	 * @return 存储查询到的信息的对象
	 * @throws Exception
	 */
	public static <T> T commonSelectTransaction(Connection con, Class<T> c, String sql, Object...objects) throws Exception
	{
		PreparedStatement ps = con.prepareStatement(sql);
		for(int i = 0; i < objects.length; i++)	ps.setObject(i + 1, objects[i]);
		
		ResultSet rs = ps.executeQuery();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		
		if(rs.next())
		{
			T t = c.getConstructor().newInstance();
			
			for(int i = 0; i < count; i++)
			{
				Object value = rs.getObject(i + 1);
				String name = rsmd.getColumnLabel(i + 1);
				
				Field field = c.getDeclaredField(name);
				field.setAccessible(true);
				field.set(t, value);
			}
			
			JDBCUtil.closeResource(null, ps, rs);
			
			return t;
		}
		
		return null;
	}
	
	/**
	 * 考虑事务后通用的查询操作
	 * @param <T> 返回的存储查询到的信息的对象所属的类型
	 * @param c 对象所属类的Class对象
	 * @param sql 要执行查询的SQL语句
	 * @param objects 用于填充占位符的对象数组
	 * @return 存储查询到的信息的对象的集合
	 * @throws Exception
	 */
	public static <T> List<T> commonSelectListTransaction(Connection con, Class<T> c, String sql, Object...objects) throws Exception
	{
		PreparedStatement ps = con.prepareStatement(sql);
		for(int i = 0; i < objects.length; i++)	ps.setObject(i + 1, objects[i]);
		
		ResultSet rs = ps.executeQuery();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		
		ArrayList<T> list = new ArrayList<T>();
		
		while(rs.next())
		{
			T t = c.getConstructor().newInstance();
			
			for(int i = 0; i < count; i++)
			{
				Object value = rs.getObject(i + 1);
				String name = rsmd.getColumnLabel(i + 1);
				
				Field field = c.getDeclaredField(name);
				field.setAccessible(true);
				field.set(t, value);
			}
			
			list.add(t);
		}
		
		JDBCUtil.closeResource(null, ps, rs);
		
		return list;
	}
	
	/**
	 * 执行特定的值查询操作
	 * @param <E> 所查询的值的类型
	 * @param con 与数据库的连接
	 * @param sql 需要执行的SQL语句
	 * @param objects 用于填充占位符的对象数组
	 * @return 所需查询的值
	 */
	@SuppressWarnings("unchecked")
	public static <E> E getValue(Connection con, String sql, Object...objects)
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try
		{
			preparedStatement = con.prepareStatement(sql);
			for(int i = 0; i < objects.length; i++) preparedStatement.setObject(i + 1, objects[i]);
			
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) return (E) resultSet.getObject(1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			JDBCUtil.closeResource(null, preparedStatement, resultSet);
		}

		return null;
	}
}