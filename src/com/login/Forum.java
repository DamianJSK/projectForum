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
 * Servlet implementation class Forum
 */
@WebServlet("/Forum")
public class Forum extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Forum() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("logged_user") == null) {
			// response.sendRedirect("login.jsp");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {
			ArrayList<Message> messages;
			HashMap<Integer, UserDB> usersMap;
			HashMap<Integer, Message> mapMessages = new HashMap<>();
			DAOforum daoForum = DAOforum.getDAOforum();
			messages = daoForum.getAllMessages();
			usersMap = daoForum.getAllUsers();
			for (Message ms : messages) {
				mapMessages.put(ms.getMessage_id(), ms);
			}
			session.setAttribute("usersMap", usersMap);
			session.setAttribute("mapMessages", mapMessages);
			session.setAttribute("messageList", messages);
			response.sendRedirect("forum.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
