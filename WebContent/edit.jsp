<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, com.models.Message"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edycja</title>
</head>
<body>
	<%
		if (session.getAttribute("logged_user") == null) {
			response.sendRedirect("login.jsp");
		}
	%>
	<%!String text;%>
	<%
		HashMap<Integer, Message> map = (HashMap<Integer, Message>) session.getAttribute("mapMessages");
		int message_id = Integer.parseInt(request.getParameter("messageId"));
		Message message = map.get(message_id);
		text = message.getText();
	%>

	<form action="Edit" method="POST">
		<input type="hidden" name="messageId" value="<%=message_id%>" />
		<textarea type="input" name="message" rows="10" cols="25"
			maxlength="200"><%=text%></textarea><br>
		<br> <input type="submit" value="Zapisz">
	</form><br>
	<form action="Forum">
		<input type="submit" value="Powrót">
	</form>

</body>
</html>