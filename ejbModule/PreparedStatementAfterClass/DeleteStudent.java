package PreparedStatementAfterClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

import Util.JDBCUtil;

public class DeleteStudent
{
	public static void main(String[] args) throws Exception
	{
		System.out.println("������ѧ���Ŀ��ţ�");
		Scanner scan = new Scanner(System.in);
		String examCard = scan.next();
		scan.close();
		
		Connection connection = JDBCUtil.getConnection();
		String sql = "delete from examstudent where ExamCard = ?";
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		prepareStatement.setString(1, examCard);
		if(prepareStatement.executeUpdate() > 0) System.out.println("ɾ���ɹ���");
		else System.out.println("���޴��ˣ����������룡");
		JDBCUtil.closeResource(connection, prepareStatement);
	}
}
