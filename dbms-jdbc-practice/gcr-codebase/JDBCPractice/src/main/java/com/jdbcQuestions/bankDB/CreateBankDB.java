package com.jdbcQuestions.bankDB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateBankDB {
    
	public static void createTable() {
		String createDB = "CREATE DATABASE IF NOT EXISTS bank_db";
		String useDB ="USE bank_db";
		String createTable =  """
				              CREATE TABLE IF NOT EXISTS accounts(
				                account_id INT PRIMARY KEY,
				                balance DOUBLE
				              ) 
				              """;
		
	  try(Connection con = DatabaseConnection.getConnection();
		  Statement smt = con.createStatement()){
		  
		  smt.execute(createDB);
		  System.out.println("Bank DB created successfully.");
		  
		  smt.execute(useDB);
		  System.out.println("Bank DB using");
		  
		  smt.execute(createTable);
		  System.out.println("accounts table created");
		  
	  }catch(SQLException e) {
		  System.out.println(e.getMessage());
	  }
	}
	
	
	public static void main(String[] args) {
		createTable();
	}
}
