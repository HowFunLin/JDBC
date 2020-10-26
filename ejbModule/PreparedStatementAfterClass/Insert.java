package PreparedStatementAfterClass;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import Util.JDBCUtil;

public class Insert
{
	public static void main(String[] args) throws Exception
	{
		Connection connection = JDBCUtil.getConnection();
		
		String sql = "insert into customers (cust_id, cust_name, email, birth) values (?, ?, ?, ?);";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Please enter customer's id: ");
		int id = scan.nextInt();
		prepareStatement.setInt(1, id);
		
		System.out.print("Please enter customer's name: ");
		String name = scan.next();
		prepareStatement.setString(2, name);
		
		System.out.print("Please enter customer's E-mail: ");
		String email = scan.next();
		prepareStatement.setString(3, email);
		
		System.out.print("Please enter customer's birthday(yyyy-MM-dd): ");
		String birth = scan.next();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = sdf.parse(birth);
		prepareStatement.setDate(4, new Date(date.getTime()));
		
		scan.close();
		
		int updateCount = prepareStatement.executeUpdate();
		
		if(updateCount > 0) System.out.println("Insert success.");
		else System.out.println("Insert failed.");
		
		JDBCUtil.closeResource(connection, prepareStatement);
	}
}
