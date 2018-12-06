<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GEM10 - esito registrazione.jsp</title>
</head>
<body>
 <%@ include file="header.jsp" %>
 <% 
 
 String esito = (String) request.getAttribute("Esito"); 
    out.println(esito);
 %>
 <br><br><a href="registrazione.html">Torna al modulo di registrazione</a>
<br><br><a href="index.jsp">Torna alla Home Page</a>
</body>
</html>