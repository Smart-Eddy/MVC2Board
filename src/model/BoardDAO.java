package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;
	
	//DB Connection Method
	public void getConnection() {
		
		try {
			Context initctx = new InitialContext();
			Context envCtx  = (Context) initctx.lookup("java:comp/env");
			DataSource ds = (DataSource) envCtx.lookup("jdbc/oracle");
			conn = ds.getConnection();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
