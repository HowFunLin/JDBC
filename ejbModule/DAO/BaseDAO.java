 package DAO;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Util.JDBCUtil;

@SuppressWarnings("unchecked")
public abstract class BaseDAO<T>
{
	private Class<T> c = null;
	
	//获取子类继承的父类的泛型，即当前类声明的泛型
	{
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType)genericSuperclass;
		Type[] actualTypeArguments = paramType.getActualTypeArguments();
		c = (Class<T>) actualTypeArguments[0];
	}
	
	/**
	 * 考虑事务之后的通用增删改操作
	 * @param con 与数据库的连接
	 * @param sql 要执行的SQL语句
	 * @param objects 用于填充占位符的对象数组
	 */
	public static void commonUpdateTransaction(Connection con, String sql, Object... objects)
	{
		PreparedStatement ps = null;
		try
		{
			ps = con.prepareStatement(sql);
			
			for(int i = 0; i < objects.length; i++)
			{
				ps.setObject(i + 1, objects[i]);
			}
			
			ps.execute();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			JDBCUtil.closeResource(null, ps);
		}
	}
	
	/**
	 * 考虑事务之后的通用的查询操作
	 * @param <T> 返回的存储查询到的信息的对象所属的类型
	 * @param con 与数据库的连接
	 * @param c	对象所属类的Class对象
	 * @param sql 要执行查询的SQL语句
	 * @param objects 用于填充占位符的对象数组
	 * @return 存储查询到的信息的对象
	 */
	public T commonSelectTransaction(Connection con, String sql, Object...objects)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = con.prepareStatement(sql);
			for(int i = 0; i < objects.length; i++)	ps.setObject(i + 1, objects[i]);
			
			rs = ps.executeQuery();
			
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
				return t;
			}
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException | SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			JDBCUtil.closeResource(null, ps, rs);
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
	public List<T> commonSelectListTransaction(Connection con, String sql, Object...objects)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<T> list = null;
		try
		{
			ps = con.prepareStatement(sql);
			for(int i = 0; i < objects.length; i++)	ps.setObject(i + 1, objects[i]);
			
			rs = ps.executeQuery();
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int count = rsmd.getColumnCount();
			
			list = new ArrayList<T>();
			
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
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException | SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			JDBCUtil.closeResource(null, ps, rs);
		}
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