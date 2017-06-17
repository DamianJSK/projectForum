package com.login;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.DAOforum;
import com.models.Message;
import com.models.UserDB;

/**
 * Servlet implementation class Account
 */
@WebServlet("/Account")
public class Account extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Account() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Served at: "+request.getContextPath());
		String max_attempts = request.getParameter("max_attempts");
		
		HttpSession session = request.getSession();
		int user_id = (Integer)session.getAttribute("user_id");
		
		DAOforum daoForum = DAOforum.getDAOforum();
		
		
		if(daoForum.setMaxAttempts(user_id, max_attempts)){
			UserDB logged_user = daoForum.refreshedLoggedUser(Integer.toString(user_id));
			session.setAttribute("logged_user", logged_user);
			session.setAttribute("username", logged_user.getUser_name());
			session.setAttribute("user_id", logged_user.getUser_id());
			response.sendRedirect("account.jsp");
		}else{
			response.sendRedirect("home.jsp");
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
