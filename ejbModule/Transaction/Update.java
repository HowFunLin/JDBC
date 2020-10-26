package Transaction;

import java.sql.Connection;

import org.junit.Test;

import Util.JDBCUtil;

public class Update
{
	@Test
	public void test() throws Exception
	{
		Connection connection = null;
		
		try
		{
			connection = JDBCUtil.getConnection();
			connection.setAutoCommit(false);
			
			String sql1 = "update `user` set balance = balance - 100 where username = ?";
			JDBCUtil.commonUpdateTransaction(connection, sql1, "AA");
			
			//ʹ�����׳��쳣
			System.out.println(1 / 0);
			
			String sql2 = "update `user` set balance = balance + 100 where username = ?";
			JDBCUtil.commonUpdateTransaction(connection, sql2, "BB");
			
			System.out.println("Transfer accounts success!");

			connection.commit();
		} 
		catch (Exception e)
		{
			connection.rollback();
			System.out.println("Transfer accounts failed!");
		}
		finally
		{
			//�������ӻָ�ΪĬ��״̬
			connection.setAutoCommit(true);
			
			JDBCUtil.closeResource(connection, null);
		}
	}
}
