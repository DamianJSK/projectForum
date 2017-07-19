<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, com.models.*, com.dao.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit message permissions</title>
<link
	href='https://fonts.googleapis.com/css?family=Lato:400,900&subset=latin,latin-ext'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
	<%
		if (session.getAttribute("logged_user") == null) {
			response.sendRedirect("login.jsp");
		}else{
		Message message = null;
		ArrayList<Message> messages = (ArrayList<Message>) session.getAttribute("messageList");
		int messageId = Integer.parseInt(request.getParameter("messageId"));
		int createdBy = -1;
		UserDB logged_user = (UserDB) session.getAttribute("logged_user");
		for (Message ms : messages) {
			if (ms.getMessage_id() == messageId && ms.getCreated_by() == logged_user.getUser_id()) {
				createdBy = logged_user.getUser_id();
			}
		}
		HashMap<Integer, UserDB> usersMap = (HashMap<Integer, UserDB>) session.getAttribute("usersMap");
		if (((UserDB) session.getAttribute("logged_user")).getUser_id() != createdBy) {
			response.sendRedirect("forum.jsp");
		} else {
			DAOforum daoForum = DAOforum.getDAOforum();
			ArrayList<UserDB> privilagedUsers = daoForum.getEditPrivilagesForMessage(messageId);
		
	%>

	<div id="container">
		<div id="logo">Forum</div>
		<div id="content">
			<span class="bigtitle">Edit message permissions for
				${username} </span> <br> Privilege to edit this message have:</br>
			<textarea name="editList" rows="10" maxlength="200" 
			readonly="readonly"><%if (!privilagedUsers.isEmpty()) {
							for (UserDB user : privilagedUsers) {
								out.println(user.getUser_name());
							}
						}
				%>
			</textarea>
			<br>

			<form action="AddPermission">
				<input type="hidden" name="message_id" value="<%=messageId%>">
				<input class="btn" type="submit" value="Add"> <select
					name="add_permissions" id="mainselection">
					<%
						for (Map.Entry<Integer, UserDB> entry : usersMap.entrySet()) {
								Integer key = entry.getKey();
								UserDB userDB = entry.getValue();

								if (createdBy != userDB.getUser_id()) {
					%>
					<option value="<%=userDB.getUser_id()%>"><%=userDB.getUser_name()%></option>
					<%
						}
							}
					%>
				</select>
			</form>
			</br>

			<form action="RemovePermission">
				<input type="hidden" name="message_id" value="<%=messageId%>">
				<input class="btn" type="submit" value="Remove"> <select
					name="remove_permissions" id="mainselection">
					<%
						for (Map.Entry<Integer, UserDB> entry : usersMap.entrySet()) {
								Integer key = entry.getKey();
								UserDB userDB = entry.getValue();

								if (createdBy != userDB.getUser_id()) {
					%>
					<option value="<%=userDB.getUser_id()%>"><%=userDB.getUser_name()%></option>
					<%
						}
							}
					%>
				</select>
			</form>
			</br>
			<form action="Forum">
				<input class="btn" type="submit" value="Back">
			</form>

		</div>
		<div id="footer">Forum project by DJ & GF & DK &copy;</div>
	</div>
	<%
		}
		}
	%>
</body>
</html>