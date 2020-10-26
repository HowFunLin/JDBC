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
		
		System.out.print("��ѡ������Ҫ��������ͣ�a��׼��֤�ţ�b�����֤�ţ���");
		String cardType = scan.next();
		String sql = null;
		
		switch(cardType)
		{
			case "a":
				sql = "select * from examstudent where ExamCard = ?";
				System.out.print("������׼��֤�ţ�");
				break;
			case "b":
				sql = "select * from examstudent where IDCard = ?";
				System.out.print("���������֤�ţ�");
				break;
			default:
				System.out.println("�����������������½������");
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
			System.out.println("========��ѯ���========");
			System.out.println("��ˮ�ţ�    \t" + resultSet.getInt(1));
			System.out.println("�ļ�/������\t" + resultSet.getInt(2));
			System.out.println("���֤�ţ� \t" + resultSet.getString(3));
			System.out.println("׼��֤�ţ� \t" + resultSet.getString(4));
			System.out.println("ѧ�������� \t" + resultSet.getString(5));
			System.out.println("����        \t" + resultSet.getString(6));
			System.out.println("�ɼ���        \t" + resultSet.getString(7));
		}
		else
		{
			System.out.println("���޴��ˣ������½������");
			System.exit(0);
		}
		
		JDBCUtil.closeResource(connection, prepareStatement, resultSet);
	}
}