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
 * ʵ��JDBC�����ݿ�Ĳ����Ĺ�����
 * @author Riyad
 * @version 2.0
 */
public abstract class JDBCUtil
{
	/**
	 * ��ȡJDBC�����ݿ������
	 * @return �ɶԵ�ǰ���ݿ���в�����Connection����
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
	
	//������ÿ�ε���ʱ���������ӳ�
	private static ComboPooledDataSource cpds = new ComboPooledDataSource("C3P0");
	
	/**
	  *  ʵ��C3P0���ݿ����ӳأ���ȡ����
	 * @return ���ӳ��е����Ӷ�Ӧ��Connection����
	 * @throws SQLException
	 */
	public static Connection getConnectionC3P0() throws SQLException
	{
		Connection connection = cpds.getConnection();
		return connection;
	}
	
	//������ÿ�ε���ʱ���������ӳ�
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
	  *  ʵ��DBCP���ӳأ���ȡ����
	 * @return ���ӳ��е����Ӷ�Ӧ��Connection����
	 * @throws SQLException
	 */
	public static Connection getConnectionDBCP() throws SQLException
	{
		Connection connection = source.getConnection();
		return connection;
	}
	
	//������ÿ�ε���ʱ���������ӳ�
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
	  *  ʵ��Druid���ӳأ���ȡ����
	 * @return ���ӳ��е����Ӷ�Ӧ��Connection����
	 * @throws SQLException
	 */
	public static Connection getConnectionDruid() throws SQLException
	{
		Connection connection = dataSource.getConnection();
		return connection;
	}
	
	/**
	 * ����ͨ����ɾ�Ĳ���֮��ر������ݿ������
	 * @param con �������ӵ�Connection����
	 * @param ps ����SQL�����Statement����
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
	 * ͨ�õ���ɾ�Ĳ���
	 * @param sql ����ִ�е�SQL���
	 * @param objects �������ռλ���Ķ�������
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
	 * ���ڲ�ѯ����֮��ر������ݿ������
	 * @param con �������ӵ�Connection����
	 * @param ps ����SQL�����Statement����
	 * @param rs ���ղ�ѯ����ļ���
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
	 * ͨ�õĲ�ѯ����
	 * @param <T> ���صĴ洢��ѯ������Ϣ�Ķ�������������
	 * @param c	�����������Class����
	 * @param sql Ҫִ�в�ѯ��SQL���
	 * @param objects �������ռλ���Ķ�������
	 * @return �洢��ѯ������Ϣ�Ķ���
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
	 * ͨ�õĲ�ѯ����
	 * @param <T> ���صĴ洢��ѯ������Ϣ�Ķ�������������
	 * @param c �����������Class����
	 * @param sql Ҫִ�в�ѯ��SQL���
	 * @param objects �������ռλ���Ķ�������
	 * @return �洢��ѯ������Ϣ�Ķ���ļ���
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
	 * ��������֮���ͨ����ɾ�Ĳ���
	 * @param con �����ݿ������
	 * @param sql Ҫִ�е�SQL���
	 * @param objects �������ռλ���Ķ�������
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
	 * ��������֮���ͨ�õĲ�ѯ����
	 * @param <T> ���صĴ洢��ѯ������Ϣ�Ķ�������������
	 * @param con �����ݿ������
	 * @param c	�����������Class����
	 * @param sql Ҫִ�в�ѯ��SQL���
	 * @param objects �������ռλ���Ķ�������
	 * @return �洢��ѯ������Ϣ�Ķ���
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
	 * ���������ͨ�õĲ�ѯ����
	 * @param <T> ���صĴ洢��ѯ������Ϣ�Ķ�������������
	 * @param c �����������Class����
	 * @param sql Ҫִ�в�ѯ��SQL���
	 * @param objects �������ռλ���Ķ�������
	 * @return �洢��ѯ������Ϣ�Ķ���ļ���
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
	 * ִ���ض���ֵ��ѯ����
	 * @param <E> ����ѯ��ֵ������
	 * @param con �����ݿ������
	 * @param sql ��Ҫִ�е�SQL���
	 * @param objects �������ռλ���Ķ�������
	 * @return �����ѯ��ֵ
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