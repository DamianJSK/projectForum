<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forum login</title>
<link
	href='https://fonts.googleapis.com/css?family=Lato:400,900&subset=latin,latin-ext'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
			<%
				if (session.getAttribute("logged_user") != null) {
					response.sendRedirect("home.jsp");
				}else{
			%>
	<div id="container">
		<div id="logo">Forum</div>

		<div id="content">
			<form action="Login">

				Enter user name: <input type="text" name="uname"
					placeholder="enter name"></br>
				</br> Enter user password: <input type="text" name="upass"
					placeholder="enter password"></br>
				</br> <font color="red"> <%
			if (session.getAttribute("error") != null) {
				out.println(session.getAttribute("error"));
				session.removeAttribute("error");
			}
		%><br>
				</font> <input class="btn" type="submit" value="login"><br>
			</form>
		</div>
		<div id="footer">Forum project by DJ & GF & DK &copy;</div>
	</div>
	<%} %>
</body>
</html>