<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GEM10 - errore.jsp</title>
</head>
<body>
 <div align="center">
  <%@ include file="header.jsp" %>
<h1>ERRORE!</h1>
<br>
Si è verificato un errore, contatta l'amministratore del sistema.
<br><br>
<%

String Errore = (String) request.getParameter("Errore");
if (Errore != null && !Errore.matches("null"))
{
	out.println(Errore);
}
%>
<br><br>
<a href="index.jsp">Torna alla Index (al Pannello se sei Admin)</a>
</div>
</body>
</html>