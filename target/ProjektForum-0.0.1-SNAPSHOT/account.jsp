<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, com.models.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Account settings</title>
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
				UserDB user = (UserDB) session.getAttribute("logged_user");
			%>
	<div id="container">
		<div id="logo">Forum</div>
		<div id="content">
			<span class="bigtitle">Info about account user ${username} </span> <br>
			User name:
			<%=user.getUser_name()%><br> User ID:
			<%=user.getUser_id()%><br> Last login:
			<%=user.getLastLoginDate()%><br> Last invalid login:
			<%=user.getLastInvalidLoginDate()%><br> Invalid login attempts:
			<%=user.getUsedLoginAttempts()%><br>
			<form action="Account">
				Max login attempts: <input type="text" name="max_attempts"
					value="<%=user.getMaxLoginAttempts()%>"><br>D
					Delay invalid login ratio: <input type="text" name="block_time"
					value="<%=user.getBlock_time()%>"><br> <input
					class="btn" type="submit" value="Save">
			</form>
			<br>
			<form action="passwordchange.jsp">
				<input class="btn" type="submit" value="Change password">
			</form>
			<br>
			<form action="home.jsp">
				<input class="btn" type="submit" value="Back">
			</form>
		</div>
		<div id="footer">Forum project by DJ & GF & DK &copy;</div>
	</div>
	<%} %>
</body>
</html>