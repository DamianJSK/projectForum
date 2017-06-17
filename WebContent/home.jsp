<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
	WELCOME OUTLANDER! You are ${username}
	<br>

	<form action="Forum">
		<a href="Forum">Go to FORUM</a>
	</form>
	<form action="account.jsp">
		<input type="submit" value="Account">
	</form>
	<form action="Logout">
		<input type="submit" value="Logout">
	</form>


</body>
</html>