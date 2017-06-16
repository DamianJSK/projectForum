<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, com.models.Message"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		if (session.getAttribute("logged_user") == null) {
			response.sendRedirect("login.jsp");
		}
	%>
	This is forum
	<br>
	<a href="home.jsp">Back Home</a>

	<table border="1">
		<tbody>
			<tr>
				<td>Message ID</td>
				<td>Message text</td>
				<td>Created</td>
				<td>Options</td>
			</tr>
			<%
			ArrayList<Message> messages = (ArrayList<Message>)session.getAttribute("messageList");
			for(Message ms : messages){  %>
			<tr>
				<td><%= Integer.toString(ms.getMessage_id())  %></td>
				<td><%= ms.getText() %></td>
				<td><%= ms.getCreatedFormated()  %></td>
				<td><a href="./Edit?messageId=<%=ms.getMessage_id() %>">Edit</a>
				<a href="./Delete?messageId=<%=ms.getMessage_id() %>">Delete</a>
				</td>
			</tr>
			<% } %>
		</tbody>

	</table>
	<form action="newmessage.jsp">
		<input type="submit" value="Dodaj wiadomosc">
	</form>

</body>
</html>