package PreparedStatementAfterClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import Util.JDBCUtil;

public class QueryStudent
{
	public static void main(String[] args) throws Exception
	{
		Scanner scan = new Scanner(System.in);
		
		System.out.print("请选择您想要输入的类型（a：准考证号，b：身份证号）：");
		String cardType = scan.next();
		String sql = null;
		
		switch(cardType)
		{
			case "a":
				sql = "select * from examstudent where ExamCard = ?";
				System.out.print("请输入准考证号：");
				break;
			case "b":
				sql = "select * from examstudent where IDCard = ?";
				System.out.print("请输入身份证号：");
				break;
			default:
				System.out.println("您的输入有误！请重新进入程序！");
				System.exit(0);
		}
		
		Connection connection = JDBCUtil.getConnection();
		
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		
		String cardNumber = scan.next();
		scan.close();
		prepareStatement.setString(1, cardNumber);
		
		ResultSet resultSet = prepareStatement.executeQuery();
		
		if(resultSet.next())
		{
			System.out.println("========查询结果========");
			System.out.println("流水号：    \t" + resultSet.getInt(1));
			System.out.println("四级/六级：\t" + resultSet.getInt(2));
			System.out.println("身份证号： \t" + resultSet.getString(3));
			System.out.println("准考证号： \t" + resultSet.getString(4));
			System.out.println("学生姓名： \t" + resultSet.getString(5));
			System.out.println("区域：        \t" + resultSet.getString(6));
			System.out.println("成绩：        \t" + resultSet.getString(7));
		}
		else
		{
			System.out.println("查无此人！请重新进入程序！");
			System.exit(0);
		}
		
		JDBCUtil.closeResource(connection, prepareStatement, resultSet);
	}
}