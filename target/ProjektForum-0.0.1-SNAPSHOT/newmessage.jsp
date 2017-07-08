<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New message</title>
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
			<span class="bigtitle">Add new message for ${username} </span> <br>
			<form action="NewMessage">

				<textarea name="message" placeholder="Wpisz widomosc" rows="10"
					maxlength="200"></textarea>
				<br> <br> <input class="btn" type="submit" value="Add"><br>

			</form>
			<form action="Forum">
				<input class="btn" type="submit" value="Back">
			</form>
		</div>
		<div id="footer">Forum project by DJ & GF & DK &copy;</div>
	</div>
	<%} %>
</body>
</html>