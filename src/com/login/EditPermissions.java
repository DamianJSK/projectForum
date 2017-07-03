package com.login;

import java.io.IOException;
import java.util.ArrayList;
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
 * Servlet implementation class EditPermissions
 */
@WebServlet("/EditPermissions")
public class EditPermissions extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditPermissions() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());

		HttpSession session = request.getSession();
		if (session.getAttribute("logged_user") == null) {
			response.sendRedirect("login.jsp");
		} else {
			
			int message_id = Integer.parseInt(request.getParameter("message_id"));

			int created_by = ((HashMap<Integer, Message>) session.getAttribute("mapMessages")).get(message_id)
					.getCreated_by();
			int user_id = (Integer) session.getAttribute("user_id");

			DAOforum daoForum = DAOforum.getDAOforum();

			if (created_by == user_id) {
				response.sendRedirect("Forum");
			} else {
				response.sendRedirect("edit_permissions.jsp?messageId=" + message_id);
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
