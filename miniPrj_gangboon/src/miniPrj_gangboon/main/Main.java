package miniPrj_gangboon.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import miniPrj_gangboon.jdbc.JdbcTemplate;
import miniPrj_gangboon.service.ServiceManager;

public class Main {
	
	public static final Scanner SC = new Scanner(System.in);
	
	public static void main(String[] args) {
		ServiceManager sm = new ServiceManager();
			try {
				sm.login();
				sm.showMenu();
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
			}
}
	



