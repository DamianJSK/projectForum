<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forum Home</title>
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
			<span class="bigtitle">Welcome in our forum! You are logged in
				as ${username} </span> <br>
			<br>

			<form action="Forum">
				<a href="Forum">Go to FORUM</a>
			</form>
			<br>
			<form action="account.jsp">
				<input class="btn" type="submit" value="Account">
			</form>
			<br>
			<form action="Logout">
				<input class="btn" type="submit" value="Logout">
			</form>
		</div>
		<div id="footer">Forum project by DJ & GF & DK &copy;</div>
	</div>
	<%} %>
</body>
</html>