<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GEM10 - prenotazioni_elenco.jsp</title>
</head>
<body>
 <%@ include file="header.jsp" %>
<p >
<img align="left" src="Statistiche.jpg" width="293" height="309" border="0" alt="statistiche">
MENU:
     <%
     
     String whologgin = (String) session.getAttribute("loggato");
     String menu = new String(" ");
     if (whologgin != null)//se il visitatore è loggato
     { 
          	//STAMPIAMO IL MENU E LO DIFFERENZIAMO A SECONDA SE E' UTENTE O ADMIN
    	    if (whologgin.matches("utente"))//SE E' UN UTENTE
    		  menu +="<ul><li><a href=\"index.jsp\">Torna alla HomePage</a></li>";
    	    else //SE E' UN AMMININSTRATORE
    	      menu +="<ul><li><a href=\"admin.jsp\">Torna al Pannello di Amministrazione</a></li>";
    	 
    	     menu += "	<li><a href=\"Logout\">Logout</a></li>  	     </ul>	    	     <br><br>";
    	     out.println(menu);
    	 
    	   // STAMPA DELL'ELENCO DELLE PRENOTAZIONI  
    	   String Prenotazioni = (String) request.getAttribute("Prenotazioni_Cancellabili"); 
    	   
    	   if (Prenotazioni != null)
    		   out.println(Prenotazioni);
    	   
     }   
     else //SE IL VISITATORE NON E' LOGGATO
     {
    	 response.sendRedirect("index.jsp"); 
     } 
    	 
    	 

     %>

</p>
</body>
</html>