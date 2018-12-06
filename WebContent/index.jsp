<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GEM10 - index.jsp</title>

<script language="javascript">
 <!--
  function verifica() {
    // Variabili associate ai campi del modulo
    var email = document.mode_login.email.value;
    var password = document.mode_login.password.value;
    var regex_email = "[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}";
    var regex_password = "[a-zA-Z0-9]{8,16}";
    var errore = false;
    var msg = "Si sono riscontrati i seguenti errori:\n" ;

     if (!email.match(regex_email)  || (email == "undefined"))
     {
    	 msg = msg + " - Il campo email è errato!\n" ;
    	 errore = true;         
     }

     if (!password.match(regex_password)  || (password == "undefined") || password.length < 8 || password.length > 16 )
     {
    	 msg = msg + " - Il campo password è errato!\n" ;
    	 errore = true;         
     }
     
     if (errore==true)
     {
    	 alert(msg);
    	 return false;
     }
     else 
     {
		return true;
     }

 }

 function verifica_recupero() {
	    // Variabili associate ai campi del modulo
	    var email = document.modulo_recupera.email.value; 
	    var regex_email = "[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}";
	    
	    if (!email.match(regex_email)  || (email == "undefined"))
	    {
	    	alert(" - Il campo email è errato!\n");
	    	return false;         
	    }
	    else 
	    {
			return true;
	    }

	}

 function verifica_evento() {
	    // Variabili associate ai campi del modulo
	    var nome = document.modulo_evento.nomeevento.value;
	    var data = document.modulo_evento.data.value;
	    var luogo = document.modulo_evento.luogo.value;
	    var prezzo = document.modulo_evento.prezzo.value;
	    var tipo = document.modulo_evento.tipo.value;

	    var regex_nome = "^[a-zA-Z][a-zA-Z\s]";
	    var regex_luogo = "^[a-zA-Z][a-zA-Z\s]";
	    var regex_tipo = "^[a-zA-Z][a-zA-Z\s]";
        var regex_data = "^[0-9]{2}\/[0-9]{2}\/[0-9]{4}$";
        var regex_prezzo = "^[0-9]{1,3}(\\.[0-9]{1,2})?$";
	    
	    var errore = false;
	    var msg = "Si sono riscontrati i seguenti errori:\n" ;


	    if (nome != "")
	    if (!nome.match(regex_nome) || (nome == "undefined") || nome.length > 32)
	    {
	    	msg = msg + " - Il campo nome evento è errato!\n" ;
	    	errore = true;         
	    }

	    if (luogo != "") 
	    if (!luogo.match(regex_luogo) || (luogo == "undefined") || luogo.length > 32)
	    {
	    	msg = msg + " - Il campo luogo è errato!\n" ;
	    	errore = true;         
	    }

	    if (data != "")
	    if (!data.match(regex_data) || (data == "undefined") )
	    {
	    	msg = msg + " - Il campo data è errato!\n" ;
	    	errore = true;         
	    }

	    if (prezzo != "")
	    if (!prezzo.match(regex_prezzo) || (prezzo == "undefined") )
	    {
	    	msg = msg + " - Il campo prezzo è errato!\n" ;
	    	errore = true;         
	    }

	    if (tipo != "")
	    if (!tipo.match(regex_tipo) || (tipo == "undefined") || tipo.length > 32)
	    {
	    	msg = msg + " - Il campo tipo è errato!\n" ;
	    	errore = true;         
	    }

	    if (errore==true)
	     {
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
<table width="758" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="375">
     <%
     
     String whologgin = (String) session.getAttribute("loggato");
     
     if (whologgin == null)//SE IL VISITATORE NON RISULTA LOGGATO
     { 
    	 String Esito = (String) request.getAttribute("Esito"); 
    	 if (Esito != null ) 
         //HA EFFETTUATO UN LOGIN ERRATO E QUINDI MOSTRIAMO A VIDEO I MOTIVI DEL FALLIMENTO
    	  out.println(Esito);
    	 
    	 //MOSTRIAMO A VIDEO IL FORM PER IL LOGIN, IL RECUPERO PASSWORD, ED UN LINK PER UNA NUOVA REGISTRAZIONE
    	 out.println("<p><strong>LOGIN:</strong></p><form method=\"post\" name=\"mode_login\" onSubmit=\" return verifica()\"	action=\"Login\">");	 
    	 out.println("<p>email:<input type=\"text\" name=\"email\" /><br><br>password:<input type=\"password\"  name=\"password\" /><br><br>");
    	 out.println("<input name=\"submit\" type=\"submit\" value=\"EFFETTUA LOGIN\" ></p></form>");		 
    	 out.println("<p>Non sei registrato? <a href=\"registrazione.html\">Effettua la registrazione</a></p><p>&nbsp;</p>");
    	 out.println("<td width=\"375\" valign=\"top\"><p><strong>RECUPERO PASSWORD: </strong></p>");
    	 out.println("<form method=\"post\" name=\"modulo_recupera\" onSubmit=\"return verifica_recupero()\" action=\"RecuperaPass\"><p>email: <input type=\"text\" name=\"email\" /><br><br>");
    	 out.println("<input name=\"tasto\" type=\"submit\" value=\"OK\" > </p></form></td>");   
     }
     else if(whologgin.matches("admin"))//SE AMMINISTRATORE
     {   //REDIRECT ALLA PAGINA ADMIN.jsp
    	 response.sendRedirect("admin.jsp");
     }
     else // SE UTENTE
     // VERRA' MOSTRATO IL MENU UTENTE
    	 out.println("Login effettuato correttamente.</td><td width=\"375\" valign=\"top\">MENU UTENTE:<ul><li><a href=\"ElencoPrenotazioni\">Visualizza tutte le prenotazioni</a></li><li><a href=\"Logout\">Logout</a></li></ul></td>");

     %>
     </td>

  </tr>
  <tr>
    <td><strong>RICERCA EVENTI:</strong>
	<table width="368" border="0" cellspacing="0" cellpadding="0">
  
  <tr>
    <th width="129" scope="row"><div align="left">NOME EVENTO </div></th>
    <td width="231"><form method="post" name="modulo_evento" onSubmit="return verifica_evento()" action="Prenotazione" ><input type="text" name="nomeevento"></td>
  </tr>
  <tr>
    <td> <div align="left"><strong>CITTA'</strong></div></td>
    <td><select name="citta" ><option value=" " selected="selected"/><option value="Agrigento">Agrigento</option><option value="Alessandria">Alessandria</option><option value="Ancona">Ancona</option><option value="Aosta">Aosta</option><option value="Arezzo">Arezzo</option><option value="Ascoli Piceno">Ascoli Piceno</option><option value="Asti">Asti</option><option value="Avellino">Avellino</option><option value="Bari">Bari</option><option value="Barletta-Andria-Trani">Barletta-Andria-Trani</option><option value="Belluno">Belluno</option><option value="Benevento">Benevento</option><option value="Bergamo">Bergamo</option><option value="Biella">Biella</option><option value="Bologna">Bologna</option><option value="Bolzano">Bolzano</option><option value="Brescia">Brescia</option><option value="Brindisi">Brindisi</option><option value="Cagliari">Cagliari</option><option value="Caltanissetta">Caltanissetta</option><option value="Campobasso">Campobasso</option><option value="Carbonia Iglesias">Carbonia Iglesias</option><option value="Caserta">Caserta</option><option value="Catania">Catania</option><option value="Catanzaro">Catanzaro</option><option value="Chieti">Chieti</option><option value="Como">Como</option><option value="Cosenza">Cosenza</option><option value="Cremona">Cremona</option><option value="Crotone">Crotone</option><option value="Cuneo">Cuneo</option><option value="Enna">Enna</option><option value="Fermo">Fermo</option><option value="Ferrara">Ferrara</option><option value="Firenze">Firenze</option><option value="Foggia">Foggia</option><option value="Forli e Cesena">Forli e Cesena</option><option value="Frosinone">Frosinone</option><option value="Genova">Genova</option><option value="Gorizia">Gorizia</option><option value="Grosseto">Grosseto</option><option value="Imperia">Imperia</option><option value="Isernia">Isernia</option><option value="La Spezia">La Spezia</option><option value="L'Aquila">L'Aquila</option><option value="Latina">Latina</option><option value="Lecce">Lecce</option><option value="Lecco">Lecco</option><option value="Livorno">Livorno</option><option value="Lodi">Lodi</option><option value="Lucca">Lucca</option><option value="Macerata">Macerata</option><option value="Mantova">Mantova</option><option value="Massa-Carrara">Massa-Carrara</option><option value="Matera">Matera</option><option value="Medio Campidano">Medio Campidano</option><option value="Messina">Messina</option><option value="Milano">Milano</option><option value="Modena">Modena</option><option value="Monza e Brianza">Monza e Brianza</option><option value="Napoli">Napoli</option><option value="Novara">Novara</option><option value="Nuoro">Nuoro</option><option value="Ogliastra">Ogliastra</option><option value="Olbia-Tempio">Olbia-Tempio</option><option value="Oristano">Oristano</option><option value="Padova">Padova</option><option value="Palermo">Palermo</option><option value="Parma">Parma</option><option value="Pavia">Pavia</option><option value="Perugia">Perugia</option><option value="Pesaro e Urbino">Pesaro e Urbino</option><option value="Pescara">Pescara</option><option value="Piacenza">Piacenza</option><option value="Pisa">Pisa</option><option value="Pistoia">Pistoia</option><option value="Pordenone">Pordenone</option><option value="Potenza">Potenza</option><option value="Prato">Prato</option><option value="Ragusa">Ragusa</option><option value="Ravenna">Ravenna</option><option value="Reggio Calabria">Reggio Calabria</option><option value="Reggio Emilia">Reggio Emilia</option><option value="Rieti">Rieti</option><option value="Rimini">Rimini</option><option value="Roma">Roma</option><option value="Rovigo">Rovigo</option><option value="Salerno">Salerno</option><option value="Sassari">Sassari</option><option value="Savona">Savona</option><option value="Siena">Siena</option><option value="Siracusa">Siracusa</option><option value="Sondrio">Sondrio</option><option value="Taranto">Taranto</option><option value="Teramo">Teramo</option><option value="Terni">Terni</option><option value="Torino">Torino</option><option value="Trapani">Trapani</option><option value="Trento">Trento</option><option value="Treviso">Treviso</option><option value="Trieste">Trieste</option><option value="Udine">Udine</option><option value="Varese">Varese</option><option value="Venezia">Venezia</option><option value="Verbano-Cusio-Ossola">Verbano-Cusio-Ossola</option><option value="Vercelli">Vercelli</option><option value="Verona">Verona</option><option value="Vibo Valentia">Vibo Valentia</option><option value="Vicenza">Vicenza</option><option value="Viterbo">Viterbo</option>
		  </select>
</td>
  </tr>
  <tr>
    <th scope="row"><div align="left">LUOGO</div></th>
    <td><input type="text" name="luogo"/></td>
  </tr>
  <tr>
    <th scope="row"><div align="left">DATA</div></th>
    <td><input type="text" name="data"/></td>
  </tr>
  <tr>
    <th scope="row"><div align="left">PREZZO</div></th>
    <td><input type="text" name="prezzo" /></td>
  </tr>
    <tr>
    <th scope="row"><div align="left">TIPO</div></th>
    <td><input type="text" name="tipo" /></td>
  </tr>
  </table>
  <input name="tasto" type="submit" value="RICERCA EVENTO"></form></td>
    <td valign="top"><br>Lasciando tutti i campi vuoti verranno mostrati tutti gli eventi disponibili.</td>
  </tr>
</table>

<% 
   //STAMPA A VIDEO DELLA VARIABILE    Esito_Mostra_Evento
   //OVVERO UNA TABELLA CON GLI EVENTI RISULTANTI DALLA RICERCA DELL'UTENTE

   String esito_mostra_evento = (String) request.getAttribute("Esito_Mostra_Evento"); 
  
   if (esito_mostra_evento != null)
	 out.println(esito_mostra_evento); 

%>
</body>
</html>

