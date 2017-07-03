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
 * Servlet implementation class RemovePermission
 */
@WebServlet("/RemovePermission")
public class RemovePermission extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemovePermission() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("In add permission");
		HttpSession session = request.getSession();
		if (session.getAttribute("logged_user") == null) {
			System.out.println("redirect to login.jsp");
			response.sendRedirect("login.jsp");
		} else {

			int message_id = Integer.parseInt(request.getParameter("message_id"));

			int created_by = ((HashMap<Integer, Message>) session.getAttribute("mapMessages")).get(message_id)
					.getCreated_by();
			int logged_user_id = (Integer) session.getAttribute("user_id");

			if (logged_user_id == created_by) {
				int remove_perm_user_id = Integer.parseInt(request.getParameter("remove_permissions"));
				DAOforum daoForum = DAOforum.getDAOforum();
				daoForum.removeEditPermissionsForUser(remove_perm_user_id, message_id);
			}

			response.sendRedirect("edit_permissions.jsp?messageId=" + message_id);
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
