package Transaction;

public class User
{
	private String username;
	private String password;
	private int balance;
	
	public User()
	{
		
	}

	public User(String username, String password, int balance)
	{
		this.username = username;
		this.password = password;
		this.balance = balance;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public int getBalance()
	{
		return balance;
	}

	public void setBalance(int balance)
	{
		this.balance = balance;
	}

	@Override
	public String toString()
	{
		return "User [username=" + username + ", password=" + password + ", balance=" + balance + "]";
	}
}
