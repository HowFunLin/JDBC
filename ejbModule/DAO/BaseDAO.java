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
	
	//��ȡ����̳еĸ���ķ��ͣ�����ǰ�������ķ���
	{
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType paramType = (ParameterizedType)genericSuperclass;
		Type[] actualTypeArguments = paramType.getActualTypeArguments();
		c = (Class<T>) actualTypeArguments[0];
	}
	
	/**
	 * ��������֮���ͨ����ɾ�Ĳ���
	 * @param con �����ݿ������
	 * @param sql Ҫִ�е�SQL���
	 * @param objects �������ռλ���Ķ�������
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
	 * ��������֮���ͨ�õĲ�ѯ����
	 * @param <T> ���صĴ洢��ѯ������Ϣ�Ķ�������������
	 * @param con �����ݿ������
	 * @param c	�����������Class����
	 * @param sql Ҫִ�в�ѯ��SQL���
	 * @param objects �������ռλ���Ķ�������
	 * @return �洢��ѯ������Ϣ�Ķ���
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
	 * ���������ͨ�õĲ�ѯ����
	 * @param <T> ���صĴ洢��ѯ������Ϣ�Ķ�������������
	 * @param c �����������Class����
	 * @param sql Ҫִ�в�ѯ��SQL���
	 * @param objects �������ռλ���Ķ�������
	 * @return �洢��ѯ������Ϣ�Ķ���ļ���
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
	 * ִ���ض���ֵ��ѯ����
	 * @param <E> ����ѯ��ֵ������
	 * @param con �����ݿ������
	 * @param sql ��Ҫִ�е�SQL���
	 * @param objects �������ռλ���Ķ�������
	 * @return �����ѯ��ֵ
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