package com.dao;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import com.models.Message;
import com.models.UserDB;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class DAOforum {

	public static final String BLOCKED = "User is blocked";
	public static final String WRONG_DATA = "wrong_data";
	public static final String CORRECTLY_LOGGED = "correctly_logged";

	private static final DAOforum DAOFORUM = init();
	Connection con = null;

	private DAOforum() {

	}

	private static DAOforum init() {
		return new DAOforum();
	}

	public static DAOforum getDAOforum() {
		return DAOFORUM;
	}

	//local access
//	public void connect() {
//
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "");
//
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	//remote access
	public void connect() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://172.30.62.42:3306/sampledb", "root2", "rootmysql12");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "");

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return con;
	}

	public String authentication(String uName, String uPass) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		try {
			pS = (PreparedStatement) con
					.prepareStatement("select * from usersdb where user_name=? and password_hash=?");
			pS.setString(1, uName);
			pS.setString(2, uPass);
			rS = pS.executeQuery();
			// jezeli login i haslo sie zgadzaja
			if (rS.next() && refreshedLoggedUserByName(uName).getBlocked() == 0 && !userIsFake(uName)) {

				user = refreshedLoggedUserByName(uName);
				System.out.println("Correctly logged user " + user.getUser_name());
				con.close();
				return CORRECTLY_LOGGED;

				// jezeli nie zgadza sie haslo
			} else {
				// zmiany w master dla git test
				UserDB checkedUser = refreshedLoggedUserByName(uName);
				// jezeli nie przekroczono maksymalnej liczby blednych hasel
				if (checkedUser != null && checkedUser.getUsedLoginAttempts() < checkedUser.getMaxLoginAttempts()) {
					// gdy osiagnieta zosatnie maksymalna liczba nieudanych
					// logowan, blokada
					String update_used_login_attempts;
					if (checkedUser.getMaxLoginAttempts() - checkedUser.getUsedLoginAttempts() == 1) {
						update_used_login_attempts = "UPDATE usersdb SET used_login_attempts=used_login_attempts+1,"
								+ "last_invalid_login=?, blocked=1 WHERE user_name=?";
					} else {
						update_used_login_attempts = "UPDATE usersdb SET used_login_attempts=used_login_attempts+1,"
								+ "last_invalid_login=? WHERE user_name=?";
					}
					pS = (PreparedStatement) con.prepareStatement(update_used_login_attempts);
					pS.setString(1, UserDB.ft.format(new Date()));
					pS.setString(2, uName);
					pS.executeUpdate();
					System.out.println("Invalid attempt for user " + checkedUser.getUser_name()
							+ ", login_attempts increased to: " + (checkedUser.getUsedLoginAttempts() + 1)
							+ ", max attempts: " + checkedUser.getMaxLoginAttempts());
					con.close();
					return WRONG_DATA;

					// jezeli osiagnieto maksymalna liczbe nieudanych logowan
				} else if (checkedUser != null) {
					String last_invalid_login = "Select last_invalid_login, block_time from usersdb WHERE user_name=?";
					pS = (PreparedStatement) con.prepareStatement(last_invalid_login);
					pS.setString(1, uName);
					rS = pS.executeQuery();
					while (rS.next()) {
						checkedUser.setLastInvalidLoginDateFromString(rS.getString("last_invalid_login"));
					}
					System.out.println("Overloaded invalid attempts");
					int timeFromLastInvalidLogin = (int) ((new Date()).getTime()
							- (checkedUser.getLastInvalidLoginDate()).getTime()) / 1000;
					int invalidLoginAttempts = checkedUser.getUsedLoginAttempts()-checkedUser.getMaxLoginAttempts();
					int timeToUnblock = (checkedUser.getBlock_time())*(invalidLoginAttempts+1)*(invalidLoginAttempts+1)-timeFromLastInvalidLogin;
					System.out.println("Time from last invalid login  " + timeFromLastInvalidLogin +
							" / time to unblock: " + timeToUnblock);
					// jezeli czas od ostatniego logowania jest wiekszy niz
					// minimalny
					if (timeToUnblock<0) {
						System.out.println("Try after time unblock");
						if(userValidation(uName, uPass)){
							String update_used_login_attempts = "UPDATE usersdb SET used_login_attempts=0, blocked=0 WHERE user_name=?";
							pS = (PreparedStatement) con.prepareStatement(update_used_login_attempts);
							pS.setString(1, uName);
							pS.executeUpdate();
							
							return CORRECTLY_LOGGED;
						}else{
							String update_used_login_attempts;
							update_used_login_attempts = "UPDATE usersdb SET used_login_attempts=used_login_attempts+1,"
										+ "last_invalid_login=? WHERE user_name=?";
							
							pS = (PreparedStatement) con.prepareStatement(update_used_login_attempts);
							pS.setString(1, UserDB.ft.format(new Date()));
							pS.setString(2, uName);
							pS.executeUpdate();
							System.out.println("Invalid attempt for user " + checkedUser.getUser_name()
									+ ", login_attempts increased to: " + (checkedUser.getUsedLoginAttempts() + 1)
									+ ", max attempts: " + checkedUser.getMaxLoginAttempts());
							con.close();
							return WRONG_DATA;
						}
					}
					// zablokuj logowanie jezeli nie minal czas blokady
					String update_blocked_state = "UPDATE usersdb SET blocked=1 WHERE user_name=?";
					pS = (PreparedStatement) con.prepareStatement(update_blocked_state);
					pS.setString(1, uName);
					pS.executeUpdate();
					con.close();
					return BLOCKED+" for "+ timeToUnblock +" seconds";

				} else {

					System.out.println("Choosen user not exist");
					con.close();
					return WRONG_DATA;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean userValidation(String uName, String uPass){
		connect();
		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		boolean validation = false;
		
		try {
			pS = (PreparedStatement) con.prepareStatement("select * from usersdb where user_name=? and password_hash=?");
			pS.setString(1, uName);
			pS.setString(2, uPass);
			rS = pS.executeQuery();
			if(rS.next()){
				validation = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return validation;
	}
	
	public String authentication2(String uName, String uPass) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		try {
			pS = (PreparedStatement) con
					.prepareStatement("select * from usersdb where user_name=? and password_hash=?");
			pS.setString(1, uName);
			pS.setString(2, uPass);
			rS = pS.executeQuery();
			// jezeli login i haslo sie zgadzaja
			if (rS.next() && refreshedLoggedUserByName(uName).getBlocked() == 0 && !userIsFake(uName)) {

				user = refreshedLoggedUserByName(uName);
				System.out.println("Correctly logged user " + user.getUser_name());
				con.close();
				return CORRECTLY_LOGGED;

				// jezeli nie zgadza sie haslo
			} else {
				// zmiany w master dla git test
				UserDB checkedUser = refreshedLoggedUserByName(uName);
				// jezeli nie przekroczono maksymalnej liczby blednych hasel
				if (checkedUser != null && checkedUser.getUsedLoginAttempts() < checkedUser.getMaxLoginAttempts()) {
					// gdy osiagnieta zosatnie maksymalna liczba nieudanych
					// logowan, blokada
					String update_used_login_attempts;
					if (checkedUser.getMaxLoginAttempts() - checkedUser.getUsedLoginAttempts() == 1) {
						update_used_login_attempts = "UPDATE usersdb SET used_login_attempts=used_login_attempts+1,"
								+ "last_invalid_login=?, blocked=1 WHERE user_name=?";
					} else {
						update_used_login_attempts = "UPDATE usersdb SET used_login_attempts=used_login_attempts+1,"
								+ "last_invalid_login=? WHERE user_name=?";
					}
					pS = (PreparedStatement) con.prepareStatement(update_used_login_attempts);
					pS.setString(1, UserDB.ft.format(new Date()));
					pS.setString(2, uName);
					pS.executeUpdate();
					System.out.println("Invalid attempt for user " + checkedUser.getUser_name()
							+ ", login_attempts increased to: " + (checkedUser.getUsedLoginAttempts() + 1)
							+ ", max attempts: " + checkedUser.getMaxLoginAttempts());
					con.close();
					return WRONG_DATA;

					// jezeli osiagnieto maksymalna liczbe nieudanych logowan
				} else if (checkedUser != null) {
					String last_invalid_login = "Select last_invalid_login, block_time from usersdb WHERE user_name=?";
					pS = (PreparedStatement) con.prepareStatement(last_invalid_login);
					pS.setString(1, uName);
					rS = pS.executeQuery();
					while (rS.next()) {
						checkedUser.setLastInvalidLoginDateFromString(rS.getString("last_invalid_login"));
					}
					System.out.println("Overload invalid attempts");
					int timeFromLastInvalidLogin = (int) ((new Date()).getTime()
							- (checkedUser.getLastInvalidLoginDate()).getTime()) / 1000;
					System.out.println("Time from last invalid login  " + timeFromLastInvalidLogin
							+ " / time to unblock: " + (checkedUser.getBlock_time() - timeFromLastInvalidLogin));
					// jezeli czas od ostatniego logowania jest wiekszy niz
					// minimalny
					if (timeFromLastInvalidLogin > checkedUser.getBlock_time()) {
						System.out.println("User unblocked, time after block ends: " + timeFromLastInvalidLogin);
						String update_used_login_attempts = "UPDATE usersdb SET used_login_attempts=0, blocked=0 WHERE user_name=?";
						pS = (PreparedStatement) con.prepareStatement(update_used_login_attempts);
						pS.setString(1, uName);
						pS.executeUpdate();
						con.close();
						return authentication(uName, uPass);
					}
					// zablokuj logowanie jezeli nie minal czas blokady
					String update_blocked_state = "UPDATE usersdb SET blocked=1 WHERE user_name=?";
					pS = (PreparedStatement) con.prepareStatement(update_blocked_state);
					pS.setString(1, uName);
					pS.executeUpdate();
					con.close();
					return BLOCKED;

				} else {

					System.out.println("Choosen user not exist");
					con.close();
					return WRONG_DATA;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean password_check(String uName, String uPass) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		try {
			pS = (PreparedStatement) con
					.prepareStatement("select * from usersdb where user_name=? and password_hash=?");
			pS.setString(1, uName);
			pS.setString(2, uPass);
			rS = pS.executeQuery();
			// jezeli login i haslo sie zgadzaja
			if (rS.next()) {

				System.out.println("Old password correct for user " + uName);
				con.close();
				return true;

				// jezeli nie zgadza sie haslo
			} else {
				con.close();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean logout(UserDB user, Date last_login) {
		connect();
		PreparedStatement pS;
		String update = "UPDATE usersdb SET used_login_attempts=0, last_login=? WHERE user_name=?";
		try {
			pS = (PreparedStatement) con.prepareStatement(update);
			pS.setString(1, UserDB.ft.format(last_login));
			pS.setString(2, user.getUser_name());
			pS.executeUpdate();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("Correctly loged out " + user.getUser_name() + ", used login attempts reset");
		return true;
	}

	public ArrayList<Message> getAllMessages() {
		connect();
		ArrayList<Message> messages = new ArrayList<>();
		Statement st;
		try {
			st = (Statement) con.createStatement();

			ResultSet rS;
			rS = st.executeQuery("select * from message;");
			while (rS.next()) {
				Message ms = new Message(rS.getString("message_id"), rS.getString("text"), rS.getString("created_by"),
						rS.getString("created"), rS.getString("edited"));
				messages.add(ms);
				System.out.println(ms.toString());
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return messages;
	}

	public boolean addMessage(String text, UserDB logged_user) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		int updated = 0;
		Message ms;
		try {
			String insert = "Insert into message (text, created_by, created, edited) values(?, ?, ?, ?)";
			pS = (PreparedStatement) con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			ms = new Message(text);
			pS.setString(1, ms.getText());
			pS.setString(2, Integer.toString(logged_user.getUser_id()));
			pS.setString(3, ms.getCreatedFormated());
			pS.setString(4, ms.getCreatedFormated());
			updated = pS.executeUpdate();
			rS = pS.getGeneratedKeys();
			rS.next();
			int message_id = rS.getInt(1);
			System.out.println("Added message with id: " + message_id);

			insert = "Insert into allowed_messages (user_id, message_id) values(?, ?)";
			pS = (PreparedStatement) con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
			pS.setString(1, Integer.toString(logged_user.getUser_id()));
			pS.setString(2, Integer.toString(message_id));
			pS.executeUpdate();

			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		if (updated > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteMessage(String message_id) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		int delFromMessage, delFromAllowed;
		Message ms;
		try {
			String delete = "delete from message where message_id=?";
			pS = (PreparedStatement) con.prepareStatement(delete);
			pS.setString(1, message_id);
			delFromMessage = pS.executeUpdate();

			delete = "delete from allowed_messages where message_id=?";
			pS = (PreparedStatement) con.prepareStatement(delete);
			pS.setString(1, message_id);
			delFromAllowed = pS.executeUpdate();
			pS.executeUpdate();

			System.out.println("Deleted message with id: " + message_id + " /tDeleted from message: " + delFromMessage
					+ "/tDeleted from alleowed: " + delFromAllowed);
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean updateMessage(String text, String message_id) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		int updateFromMessage;
		Message ms;
		try {
			String update = "UPDATE message SET text=? WHERE message_id=?";
			pS = (PreparedStatement) con.prepareStatement(update);
			pS.setString(1, text);
			pS.setString(2, message_id);
			updateFromMessage = pS.executeUpdate();

			System.out.println("Updated message with id: " + message_id + " \tUpdated text message: " + text);
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean setMaxAttempts(int user_id, String max_attempts, String block_time) {
		connect();
		PreparedStatement pS;

		try {
			String update = "UPDATE usersdb SET max_login_attempts=?, block_time=? WHERE user_id=?";
			pS = (PreparedStatement) con.prepareStatement(update);
			pS.setString(1, max_attempts);
			pS.setString(2, block_time);
			pS.setString(3, Integer.toString(user_id));
			pS.executeUpdate();

			System.out.println("Updated attempts to: " + max_attempts + " \tand blocking time: " + block_time
					+ " for user with id: " + user_id);
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public UserDB refreshedLoggedUser(String user_id) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		try {
			pS = (PreparedStatement) con.prepareStatement("select * from usersdb where user_id=?");
			pS.setString(1, user_id);
			rS = pS.executeQuery();

			if (rS.next()) {
				String user_name = rS.getString("user_name");
				String permissions_id = rS.getString("permissions_id");
				String last_login = rS.getString("last_login");
				String last_invalid_login = rS.getString("last_invalid_login");
				String max_login_attempts = rS.getString("max_login_attempts");
				String used_login_attempts = rS.getString("used_login_attempts");
				String block_time = rS.getString("block_time");
				String blocked = rS.getString("blocked");
				String isFake = rS.getString("isFake");

				user = new UserDB(user_id, user_name, permissions_id, last_login, last_invalid_login,
						max_login_attempts, used_login_attempts, block_time, blocked, isFake);
				return user;
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public UserDB refreshedLoggedUserByName(String user_name) {
		connect();
		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		try {
			pS = (PreparedStatement) con.prepareStatement("select * from usersdb where user_name=?");
			pS.setString(1, user_name);
			rS = pS.executeQuery();

			if (rS.next()) {
				String user_id = rS.getString("user_id");
				user_name = rS.getString("user_name");
				String permissions_id = rS.getString("permissions_id");
				String last_login = rS.getString("last_login");
				String last_invalid_login = rS.getString("last_invalid_login");
				String max_login_attempts = rS.getString("max_login_attempts");
				String used_login_attempts = rS.getString("used_login_attempts");
				String block_time = rS.getString("block_time");
				String blocked = rS.getString("blocked");
				String isFake = rS.getString("isFake");

				user = new UserDB(user_id, user_name, permissions_id, last_login, last_invalid_login,
						max_login_attempts, used_login_attempts, block_time, blocked, isFake);
				return user;
			} else {
				return null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public boolean updatePass(String user_name, String new_pass) {
		connect();
		PreparedStatement pS;
		int updatedPass;
		try {
			String update = "UPDATE usersdb SET password_hash=? WHERE user_name=?";
			pS = (PreparedStatement) con.prepareStatement(update);
			pS.setString(1, new_pass);
			pS.setString(2, user_name);
			updatedPass = pS.executeUpdate();

			System.out.println("Updated password for user with name: " + user_name + "\t updated rows: " + updatedPass);
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public HashMap<Integer, UserDB> getAllUsers() {
		HashMap<Integer, UserDB> usersMap = new HashMap<>();
		UserDB user;
		ResultSet rS = executeFetchQuery("select * from usersdb where isFake=0;");
		try {

			while (rS.next()) {
				String user_id = rS.getString("user_id");
				String user_name = rS.getString("user_name");
				String permissions_id = rS.getString("permissions_id");
				String last_login = rS.getString("last_login");
				String last_invalid_login = rS.getString("last_invalid_login");
				String max_login_attempts = rS.getString("max_login_attempts");
				String used_login_attempts = rS.getString("used_login_attempts");
				String block_time = rS.getString("block_time");
				String blocked = rS.getString("blocked");
				String isFake = rS.getString("isFake");

				user = new UserDB(user_id, user_name, permissions_id, last_login, last_invalid_login,
						max_login_attempts, used_login_attempts, block_time, blocked, isFake);
				usersMap.put(user.getUser_id(), user);
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return usersMap;
	}

	public ArrayList<UserDB> getEditPrivilagesForMessage(int messageId) {
		ArrayList<UserDB> listUserDB = new ArrayList<>();
		ArrayList<Integer> idsUsersWithPrivilages = new ArrayList<>();
		PreparedStatement pS;
		ResultSet rS;
		String selectPrivilages = "Select * from message_edit_perm where message_id=?";
		try {
			connect();
			pS = (PreparedStatement) con.prepareStatement(selectPrivilages);

			pS.setString(1, Integer.toString(messageId));
			rS = pS.executeQuery();
			while (rS.next()) {
				idsUsersWithPrivilages.add(Integer.parseInt(rS.getString("user_id")));
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Iterator it = idsUsersWithPrivilages.iterator();
		while(it.hasNext()){
			listUserDB.add(refreshedLoggedUser(it.next().toString()));
		}
		return listUserDB;
	}
	
	public ArrayList<Integer> getEditPrivilagesForMessageByUserID(int message_id){
		ArrayList<Integer> idsList = new ArrayList<>();
		ArrayList<UserDB> usersList = getEditPrivilagesForMessage(message_id);
		Iterator<UserDB> it = usersList.iterator();
		while(it.hasNext()){
			idsList.add(((UserDB)it.next()).getUser_id());
		}
		return idsList;
	}

	public ResultSet executeFetchQuery(String sql) {
		ResultSet rs = null;
		try {
			connect();
			rs = con.createStatement().executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

//	public boolean permissionToEditMessage(int user_id, int message_id){
//		
//	}
	
	public boolean addEditPermissionsForUser(int user_id, int message_id) {
		ArrayList<UserDB> userList = getEditPrivilagesForMessage(message_id);
		Iterator<UserDB> it = userList.iterator();
		while(it.hasNext()){
			if(it.next().getUser_id() == user_id){
				return false;
			}
		}

		PreparedStatement pS;
		String addPrivilages ="INSERT INTO message_edit_perm (user_id, message_id) VALUES (?, ?);";	
		int updatedRows = -1;
		try {
			connect();
			pS = (PreparedStatement) con.prepareStatement(addPrivilages);
			pS.setString(1, Integer.toString(user_id));
			pS.setString(2, Integer.toString(message_id));
			updatedRows = pS.executeUpdate();
			System.out.println("Add permission for user: "+user_id+" and message: "+message_id+", Inserted rows: " + updatedRows);
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(updatedRows > 0){
			return true;
		}else{		
			return false;
		}
	}

	public boolean removeEditPermissionsForUser(int remove_perm_user_id, int message_id) {
		
		PreparedStatement pS;
		ResultSet rS;
		int deletedRows = 0;
		Message ms;
		try {
			connect();
			String delete = "delete from message_edit_perm where user_id=? and message_id=?";
			pS = (PreparedStatement) con.prepareStatement(delete);
			pS.setString(1, Integer.toString(remove_perm_user_id));
			pS.setString(2, Integer.toString(message_id));
			deletedRows = pS.executeUpdate();


			System.out.println("Deleted permissions, Deleted rows: " + deletedRows + " /tDeleted for message: " + message_id
					+ " Deleted for userID: " + remove_perm_user_id);
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}

	public boolean userExist (String uname){

		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		try {
			connect();
			pS = (PreparedStatement) con.prepareStatement("select * from usersdb where user_name=?");
			pS.setString(1, uname);
			rS = pS.executeQuery();

			if (rS.next()){
				con.close();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public String createFakeUser(String uname) {
		// TODO Auto-generated method stub
//		String user_id = rS.getString("user_id"); //powinien byc zwrot z bazy
		String user_name = uname;
		String permissions_id = "3";//permission dla ownera
		String password_hash = "xxxxxxxx";
		String last_login = UserDB.ft.format(new Date());
//		String last_invalid_login = rS.getString("last_invalid_login"); //default ustawione w bazie
		String max_login_attempts = Integer.toString(new Random().nextInt(10)+1);//od 1 do 10
		String used_login_attempts = "0";
		String block_time = Integer.toString(new Random().nextInt(10)+5);
//		String blocked = rS.getString("blocked");//defalut 0 -false
		String isFake = "1";
		
		
		String insertFakeUser = "INSERT INTO `usersdb` (`user_name`, `permissions_id`, "
				+ "`password_hash`, `last_login`, `max_login_attempts`, `used_login_attempts`, `block_time`, `isFake`) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		

		PreparedStatement pS;
		ResultSet rS;

		try {
			connect();
			pS = (PreparedStatement) con.prepareStatement(insertFakeUser, Statement.RETURN_GENERATED_KEYS);
			pS.setString(1, user_name);
			pS.setString(2, permissions_id);
			pS.setString(3, password_hash);
			pS.setString(4, last_login);
			pS.setString(5, max_login_attempts);
			pS.setString(6, used_login_attempts);
			pS.setString(7, block_time);
			pS.setString(8, isFake);
			int insertedRows = pS.executeUpdate();
			rS = pS.getGeneratedKeys();
			rS.next();
			int user_id = rS.getInt(1);
			System.out.println("Added fake user with id: " + user_id);
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		String option = "";
		option = authentication(uname, null);
		return option;
	}
	
	public boolean userIsFake (String uname){

		PreparedStatement pS;
		ResultSet rS;
		UserDB user;
		String isFake = "";
		try {
			connect();
			pS = (PreparedStatement) con.prepareStatement("select isFake from usersdb where user_name=?");
			pS.setString(1, uname);
			rS = pS.executeQuery();

			while(rS.next()){
				isFake = rS.getString("isFake");
			}
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(isFake.equals("1")){
			return true;
		}else{
			return false;
		}
	}
}
