<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GEM10 - modifica_utente.jsp</title>
</head>
<body>
 <%@ include file="header.jsp" %>
<% 
     
     String whologgin = (String) session.getAttribute("loggato");
     
     if (whologgin == null || !whologgin.matches("admin"))
    	response.sendRedirect("index.jsp");
     

    %>   
    <div align="right"><a href="Logout">Logout</a></div>
    <br>
    <hr> 
    
    <%    
    // VIENE MEMORIZZATA LA VARIABILE PASSATA CON L'URL 
    String userid = (String) request.getParameter("userid");
    String form = (String) request.getAttribute("form"); 
    if (form == null)
   		 // VIENE RICHIAMATA LA SERVLET PER LE ELABORAZIONI DATI PASSANDO 
   		 // L'ID DELL'UTENTE DA MODIFICARE
     	response.sendRedirect("Modifica_Utente?userid="+userid);
    else
    	out.println(form);
   
 %>
</body>
</html>