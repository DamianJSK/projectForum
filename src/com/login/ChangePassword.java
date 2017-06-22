package com.login;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.dao.DAOforum;
import com.models.UserDB;

/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangePassword() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Served at: " + request.getContextPath() + " change password");
		HttpSession session = request.getSession();
		if (session.getAttribute("logged_user") == null) {
			response.sendRedirect("login.jsp");
		} else {
			String old_pass = request.getParameter("old_pass");
			String new_pass = request.getParameter("new_pass");
			String new_pass_repeat = request.getParameter("new_pass_repeat");
			DAOforum daoForum = DAOforum.getDAOforum();

			if (new_pass.length() > 7 && new_pass.length() < 17 && new_pass.equals(new_pass_repeat)) {
				String user_name = ((UserDB) session.getAttribute("logged_user")).getUser_name();
				if (daoForum.password_check(user_name, old_pass)) {
					if (daoForum.updatePass(user_name, new_pass)) {
						session.setAttribute("error_pass", "Password changed!");
					} else {
						session.setAttribute("error_pass", "Something goes wrong!");
					}
				} else {
					session.setAttribute("error_pass", "Old password is wrong!");
				}
			} else {
				session.setAttribute("error_pass", "Pass is to short or incorrect repeated");
			}

			response.sendRedirect("passwordchange.jsp");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
