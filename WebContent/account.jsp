<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*,java.util.*, com.models.*"%>
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
		UserDB user = (UserDB) session.getAttribute("logged_user");
	%>
	User name:
	<%=user.getUser_name()%><br> 
	User ID:
	<%=user.getUser_id()%><br> 
	Last login:
	<%=user.getLastLoginDate()%><br> 
	Last invalid login:
	<%=user.getLastInvalidLoginDate()%><br>
	Invalid login attempts: <%=user.getUsedLoginAttempts()%><br>
	<form action="Account">
		Max login attempts: <input type="text" name="max_attempts"
			value="<%=user.getMaxLoginAttempts()%>"><br> 
		Block time in sec: <input type="text" name="block_time"
			value="<%=user.getBlock_time()%>"><br>
			<input
			type="submit" value="Zapisz">
	</form>
	<br>
	<form action="passwordchange.jsp">
		<input type="submit" value="Change password">
	</form><br>
	<form action="home.jsp">
		<input type="submit" value="Back">
	</form>
</body>
</html>