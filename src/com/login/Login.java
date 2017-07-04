package com.login;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.DAOforum;
import com.models.UserDB;


@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		UserDB logged_user = null;
		String uname = request.getParameter("uname");
		String upass = request.getParameter("upass");
		String option = "";
		DAOforum daoForum = DAOforum.getDAOforum();
		if(daoForum.userExist(uname)){
			//gdyz uzytkownik istnieje, sprawdza opcje, authentication musi uwzgleniac fake
			option = daoForum.authentication(uname, upass);
		}else{
			//stworzenie uzytkownika jezeli nie istnieje
			option = daoForum.createFakeUser(uname);
		}
		
		if(option == DAOforum.CORRECTLY_LOGGED){
		logged_user = daoForum.refreshedLoggedUserByName(uname);
		}else if(option == DAOforum.BLOCKED){
			session.setAttribute("error", "User is blocked");
			response.sendRedirect("login.jsp");
		}else{
			session.setAttribute("error", "Invalid login or password");
			response.sendRedirect("login.jsp");
		}
		
		if(logged_user != null){
			session = request.getSession();
			session.setAttribute("logged_user", logged_user);
			session.setAttribute("username", logged_user.getUser_name());
			session.setAttribute("user_id", logged_user.getUser_id());
			session.setAttribute("last_login", new Date());
			response.sendRedirect("home.jsp");
		}else{
//			session.setAttribute("error", "Invalid login or password");
//			response.sendRedirect("login.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
