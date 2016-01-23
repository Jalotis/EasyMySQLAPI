import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQL {
	
	private String host;
	private int port;
	private String user;
	private String password;
	private String database;
	
	private Connection con;
	
	public MySQL(String host, int port, String user, String password, String database) throws Exception{
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.database = database;
	}
	
	public Connection getConnection(){
		if(hasConnection()){
			return getCurrentConnection();
		} else {
			try {
				return openConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.err.println("Error: could not establish a connection");
		return null;
	}
	
    private Connection openConnection() throws Exception{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection con = DriverManager.getConnection("jdbc:mysql://"+this.host+":"+this.port+"/"+this.database, this.user, this.password);
			this.con = con;
			return con;
	}
	
	private Connection getCurrentConnection(){
		return this.con;
	}
	
	private boolean hasConnection(){
		if(this.con != null){
			return true;
		}
		return false;
	}
	
	public void query(String query){
		Connection con = this.con;
		PreparedStatement st = null;
		try {
			 st = con.prepareStatement(query);
			 st.executeUpdate();
		} catch (SQLException e) {
			System.err.println("The following query failed to send '"+query+"'.");
		} finally {
			this.closeResources(null, st);
		}
	}
	
	public void closeResources(ResultSet rs, PreparedStatement st){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(st != null){
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void closeConnection(){
		try {
			this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.con = null;
	}

}
