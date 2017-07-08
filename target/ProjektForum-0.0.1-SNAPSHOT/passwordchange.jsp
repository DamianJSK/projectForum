<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Change password</title>
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
			<span class="bigtitle">Info about account user ${username} </span> <br>
			<form action="ChangePassword" method="get">
				Old password: <input type="text" name="old_pass"
					placeholder="Enter old password" maxlength="16" minlength="8"><br>
				New password: <input type="text" name="new_pass"
					placeholder="New password" maxlength="16" minlength="8"><br>
				Repeat new password: <input type="text" name="new_pass_repeat"
					placeholder="Repeat new pass" maxlength="16" minlength="8"><br>
				<font color="red"> <%
 	if (session.getAttribute("error_pass") != null) {
 		out.println(session.getAttribute("error_pass"));
 		session.removeAttribute("error_pass");
 	}
 %>
				</font><br> <input class="btn" type="submit" value="Save">
			</form>
			<br>
			<form action="account.jsp">
				<input class="btn" type="submit" value="Account">
			</form>
		</div>
		<div id="footer">Forum project by DJ & GF & DK &copy;</div>
	</div>
	<%} %>
</body>
</html>