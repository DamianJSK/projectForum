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

/**
 * Servlet implementation class Delete
 */
@WebServlet("/Delete")
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Delete() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Served at: " + request.getContextPath());
		HttpSession session = request.getSession();
		if (session.getAttribute("logged_user") == null) {
			response.sendRedirect("login.jsp");
		} else {
			String message_id = request.getParameter("messageId");
			DAOforum daoForum = DAOforum.getDAOforum();
			int created_by = ((HashMap<Integer, Message>) session.getAttribute("mapMessages"))
					.get(Integer.parseInt(message_id)).getCreated_by();
			int user_id = (Integer) session.getAttribute("user_id");

			if (created_by != user_id) {
				response.sendRedirect("Forum");
			} else if (daoForum.deleteMessage(message_id)) {
				response.sendRedirect("Forum");
			} else {
				response.sendRedirect("home.jsp");
			}
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
