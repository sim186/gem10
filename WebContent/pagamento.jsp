<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GEM10 - pagamento.jsp</title>
<script language="javascript">
 <!--
  function verifica() {
    // Variabili associate ai campi del modulo
    var carta = document.modulo.carta.value;
    var regex_posti = "^[0-9]+$";

     if (!carta.match(regex_posti)  || (carta == "undefined") || carta.length != 14)
     {
    	 msg = " - Il campo carta di credito è errato!\n" ;
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
<% 

	// Variabili passate con dispatcher
	String carta = (String) request.getAttribute("carta");
	//taglia la stringa dopo due cifre decimali
	String totale = new String("");
	String whologgin = (String) session.getAttribute("loggato");
	String menu = new String(" ");
	String id_evento = (String) request.getParameter("id_evento");
	String posti = (String) request.getParameter("posti");
	String Errore = (String) request.getParameter("errore_carta");
	
	if (whologgin != null)//se il visitatore è loggato
	{ 
		if (request.getAttribute("totale") != null)
		{ 
			totale = (String) Float.toString((Float)request.getAttribute("totale"));
		}
		
		if (Errore != null && !Errore.matches("Errore: "))
		out.println(Errore);
	}
    else //SE IL VISITATORE NON E' LOGGATO
    {
   	 response.sendRedirect("index.jsp"); 
    } 
%>
<br><br>
<form  method="post" name="modulo" onSubmit="return verifica()" action="Prenotazione">
Il totale da pagare è: <%out.println(totale);%> euro
<br><br>
<input type="text" name="carta" value="<%out.println(carta);%>"/>Se necessario cambiare il numero di carta di credito.
<input type="hidden" name="id_evento" value="<%out.println(id_evento);%>"/>
<input type="hidden" name="posti" value="<%out.println(posti);%>"/>
<input type="hidden" name="totale" value="<%out.println(totale);%>"/>
<input name="tasto" type="submit" value="PAGA">
</form>
</body>
</html>