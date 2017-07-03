<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, com.models.*, com.dao.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forum</title>
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
			%>
	<div id="container">
		<div id="logo">Forum</div>
		<div id="content">

			<span class="bigtitle">You are logged in as ${username} </span> <br>
			<a href="home.jsp">Back Home</a>

			<table border="1">
				<tbody>
					<tr>
						<td>Message ID</td>
						<td><div style="width: 380px">Message text</div></td>
						<td>Created by</td>
						<td><div style="maxwidth: 30px">Created || Edited</div></td>
						<td>Options</td>
						<td>Edit permissions</td>
					</tr>
					<%
						HashMap<Integer, UserDB> usersMap = (HashMap<Integer, UserDB>) session.getAttribute("usersMap");
						ArrayList<Message> messages = (ArrayList<Message>) session.getAttribute("messageList");
						for (Message ms : messages) {
					%>
					<tr>
						<td><%=Integer.toString(ms.getMessage_id())%></td>
						<td><%=ms.getText()%></td>
						<%System.out.println(usersMap.get(ms.getCreated_by()).getUser_name());
						%>
						<td><%=usersMap.get(ms.getCreated_by()).getUser_name() %>
						<td><%=ms.getCreatedFormated()%><br><%=ms.getEditedFormated()%></td>
						<td>
							<%
							int logged_user_id =  ((UserDB) session.getAttribute("logged_user")).getUser_id();
							DAOforum daoForum = DAOforum.getDAOforum();
							ArrayList<Integer> usersWithPriviliegesIds = daoForum.getEditPrivilagesForMessageByUserID(ms.getMessage_id());
							if (ms.getCreated_by() == logged_user_id || usersWithPriviliegesIds.contains(logged_user_id)) {
								out.print("<a href=" + "./edit.jsp?messageId=" + ms.getMessage_id() + ">Edit</a></br>");
							}
								if (ms.getCreated_by() ==  logged_user_id) {
										out.print("<a href="+ "./Delete?messageId=" + ms.getMessage_id() + ">Delete</a>");
									}
							%>
						</td>
						<td>							
						<%
								if (ms.getCreated_by() == ((UserDB) session.getAttribute("logged_user")).getUser_id()) {
										out.print("<a href=" + "./edit_permissions.jsp?messageId=" + ms.getMessage_id() + 
												">Edit permissions</a>");
									}
							%>
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
			<br>
			<form action="newmessage.jsp">
				<input class="btn" type="submit" value="Dodaj wiadomosc">
			</form>
		</div>
		<div id="footer">Forum project by DJ & GF & DK &copy;</div>
	</div>
	<%} %>
</body>
</html>