<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Nowa wiadomosc</title>
</head>
<body>
	<%
		if (session.getAttribute("username") == null) {
			response.sendRedirect("login.jsp");
		}
	%>

	<form action="NewMessage">
		<pre>
		 <textarea name="message" placeholder="Wpisz widomosc"
				rows="10" cols="25" maxlength="200"></textarea><br>
		 		<input type="submit" value="Dodaj"><br>
				</pre>
	</form>

</body>
</html>