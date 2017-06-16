<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<p1>Welcome in this Forum</p1>
	<form action="Login">
		<pre>
		Enter user name: <input type="text" name="uname"
				placeholder="enter name"><br>
		Enter user password: <input type="text" name="upass"
				placeholder="enter password"><br>
		<font color="red">
		<%
			if (session.getAttribute("error") != null) {
				out.println(session.getAttribute("error"));
				session.removeAttribute("error");
			}
		%>
		</font>
		<input type="submit" value="login"><br>
	</pre>
	</form>
</body>
</html>