package PreparedStatement;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.Test;

import Util.JDBCUtil;

public class CustomerCommonSelect
{	
	public Customer customerCommonSelect(String sql, Object...objects) throws Exception
	{
		Connection con = JDBCUtil.getConnection();
		
		PreparedStatement ps = con.prepareStatement(sql);
		for(int i = 0; i < objects.length; i++)	ps.setObject(i + 1, objects[i]);
		
		ResultSet rs = ps.executeQuery();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int count = rsmd.getColumnCount();
		
		if(rs.next())
		{
			Customer c = new Customer();
			
			for(int i = 0; i < count; i++)
			{
				Object value = rs.getObject(i + 1);
				//不兼容类的属性名与列名不相同的情况，getColumnLabel方法兼容两种情况（第一种情况需在SQL语句的列名后加上类属性名）
				//String name = rsmd.getColumnName(i + 1);
				String name = rsmd.getColumnLabel(i + 1);
				
				Field field = Customer.class.getDeclaredField(name);
				field.setAccessible(true);
				field.set(c, value);
			}
			
			JDBCUtil.closeResource(con, ps, rs);
			
			return c;
		}
		
		return null;
	}
	
	@Test
	public void test() throws Exception
	{
		String sql = "select id, name, birth, email from customer where id = ?";
		Customer c = customerCommonSelect(sql, 1);
		System.out.println(c);
	}
}