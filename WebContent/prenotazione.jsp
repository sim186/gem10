<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GEM10 - prenotazione.jsp</title>
<script language="javascript">
 <!--
  function verifica() {
    // Variabili associate ai campi del modulo
    var posti = document.modulo.posti.value;
    var regex_posti = "^[0-9]+$";

     if (!posti.match(regex_posti)  || (posti == "undefined") || posti.length > 3)
     {
    	 msg = " - Il campo posti è errato!\n" ;
    	 alert(msg);
    	 return false;
     }
     else 
     {
         return true;
     }

 }
 //-->
 </script>     
  
</head>
<body>
 <%@ include file="header.jsp" %>
 <br><br>
<% 

	String id_evento = (String) request.getParameter("id_evento");
	String disponibili = (String) request.getParameter("disponibili");
	String errore = (String) request.getParameter("errore");
	String whologgin = (String) session.getAttribute("loggato");
	String menu = new String(" ");
	if (whologgin != null)//se il visitatore è loggato
	{ 
		if (errore != null)//se è stato riportato in questa pagina a seguito di un errore
 			out.println(errore);
        
		//STAMPA DEL FORM
		String form = "<form  method=\"post\" name=\"modulo\" onSubmit=\"return verifica()\" action=\"Prenotazione\">";
		form += "<input type=\"text\" name=\"posti\"/>Posti da acquistare (Massimo "+disponibili+" posti)";
		form += "<input type=\"hidden\" name=\"id_evento\" value=\""+id_evento+"\"/>";
		form += "<input type=\"hidden\" name=\"disponibili\" value=\""+disponibili+"\"/>";
		form += "<input name=\"tasto\" type=\"submit\" value=\"AVANTI\" \">";
		form += "</form>";

		out.println(form);
	}
    else //SE IL VISITATORE NON E' LOGGATO
    {
   	 response.sendRedirect("index.jsp"); 
    } 
%>
</body>
</html>