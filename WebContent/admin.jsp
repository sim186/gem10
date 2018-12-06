<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>GEM10 - admin.jsp</title> 
</head>
<body>
 <%@ include file="header.jsp" %>
 
      <% 
     
     String whologgin = (String) session.getAttribute("loggato");
     
     
     if (whologgin == null || !whologgin.matches("admin"))
    	response.sendRedirect("index.jsp");
     %>

 <hr>    	
<a href="Logout">Logout</a>
<hr>    	
<h1>BENVENUTO NEL PANNELLO DI AMMINISTRAZIONE   </h1> 	
NOTA: Lasciando tutti i campi vuoti verranno mostrati tutti gli eventi/utenti disponibili (Nella ricerca).
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top"><h2>AMMINISTRA EVENTO</h2>
<form method="post" action="Pannello">
<strong>RICERCA EVENTI:</strong>
	<table width="600" border="0" cellspacing="0" cellpadding="0">
  
  <tr>
    <td ><div align="left">NOME EVENTO </div></td>
    <td ><input type="text" name="nomeevento"></td>
    <td > Massimo 32 caratteri (Es. Napoli-Juve)</td>
  </tr>
  <tr>
    <td> <div align="left"><strong>CITTA'</strong></div></td>
    <td><select name="citta" ><option value=" " selected="selected"/>  </option><option value="Agrigento">Agrigento</option><option value="Alessandria">Alessandria</option><option value="Ancona">Ancona</option><option value="Aosta">Aosta</option><option value="Arezzo">Arezzo</option><option value="Ascoli Piceno">Ascoli Piceno</option><option value="Asti">Asti</option><option value="Avellino">Avellino</option><option value="Bari">Bari</option><option value="Barletta-Andria-Trani">Barletta-Andria-Trani</option><option value="Belluno">Belluno</option><option value="Benevento">Benevento</option><option value="Bergamo">Bergamo</option><option value="Biella">Biella</option><option value="Bologna">Bologna</option><option value="Bolzano">Bolzano</option><option value="Brescia">Brescia</option><option value="Brindisi">Brindisi</option><option value="Cagliari">Cagliari</option><option value="Caltanissetta">Caltanissetta</option><option value="Campobasso">Campobasso</option><option value="Carbonia Iglesias">Carbonia Iglesias</option><option value="Caserta">Caserta</option><option value="Catania">Catania</option><option value="Catanzaro">Catanzaro</option><option value="Chieti">Chieti</option><option value="Como">Como</option><option value="Cosenza">Cosenza</option><option value="Cremona">Cremona</option><option value="Crotone">Crotone</option><option value="Cuneo">Cuneo</option><option value="Enna">Enna</option><option value="Fermo">Fermo</option><option value="Ferrara">Ferrara</option><option value="Firenze">Firenze</option><option value="Foggia">Foggia</option><option value="Forli e Cesena">Forli e Cesena</option><option value="Frosinone">Frosinone</option><option value="Genova">Genova</option><option value="Gorizia">Gorizia</option><option value="Grosseto">Grosseto</option><option value="Imperia">Imperia</option><option value="Isernia">Isernia</option><option value="La Spezia">La Spezia</option><option value="L'Aquila">L'Aquila</option><option value="Latina">Latina</option><option value="Lecce">Lecce</option><option value="Lecco">Lecco</option><option value="Livorno">Livorno</option><option value="Lodi">Lodi</option><option value="Lucca">Lucca</option><option value="Macerata">Macerata</option><option value="Mantova">Mantova</option><option value="Massa-Carrara">Massa-Carrara</option><option value="Matera">Matera</option><option value="Medio Campidano">Medio Campidano</option><option value="Messina">Messina</option><option value="Milano">Milano</option><option value="Modena">Modena</option><option value="Monza e Brianza">Monza e Brianza</option><option value="Napoli">Napoli</option><option value="Novara">Novara</option><option value="Nuoro">Nuoro</option><option value="Ogliastra">Ogliastra</option><option value="Olbia-Tempio">Olbia-Tempio</option><option value="Oristano">Oristano</option><option value="Padova">Padova</option><option value="Palermo">Palermo</option><option value="Parma">Parma</option><option value="Pavia">Pavia</option><option value="Perugia">Perugia</option><option value="Pesaro e Urbino">Pesaro e Urbino</option><option value="Pescara">Pescara</option><option value="Piacenza">Piacenza</option><option value="Pisa">Pisa</option><option value="Pistoia">Pistoia</option><option value="Pordenone">Pordenone</option><option value="Potenza">Potenza</option><option value="Prato">Prato</option><option value="Ragusa">Ragusa</option><option value="Ravenna">Ravenna</option><option value="Reggio Calabria">Reggio Calabria</option><option value="Reggio Emilia">Reggio Emilia</option><option value="Rieti">Rieti</option><option value="Rimini">Rimini</option><option value="Roma">Roma</option><option value="Rovigo">Rovigo</option><option value="Salerno">Salerno</option><option value="Sassari">Sassari</option><option value="Savona">Savona</option><option value="Siena">Siena</option><option value="Siracusa">Siracusa</option><option value="Sondrio">Sondrio</option><option value="Taranto">Taranto</option><option value="Teramo">Teramo</option><option value="Terni">Terni</option><option value="Torino">Torino</option><option value="Trapani">Trapani</option><option value="Trento">Trento</option><option value="Treviso">Treviso</option><option value="Trieste">Trieste</option><option value="Udine">Udine</option><option value="Varese">Varese</option><option value="Venezia">Venezia</option><option value="Verbano-Cusio-Ossola">Verbano-Cusio-Ossola</option><option value="Vercelli">Vercelli</option><option value="Verona">Verona</option><option value="Vibo Valentia">Vibo Valentia</option><option value="Vicenza">Vicenza</option><option value="Viterbo">Viterbo</option>
		  </select>
</td>
  </tr>
  <tr>
    <td><div align="left">LUOGO</div></td>
    <td><input type="text" name="luogo"/></td>
    <td > Massimo 32 caratteri (Es. Teatro Bracco)</td>
  </tr>
  <tr>
    <td><div align="left">DATA</div></td>
    <td><input type="text" name="data"/></td>
    <td > gg/mm/aaaa (Es. 12/03/2010)</td>
  </tr>
  <tr>
    <td><div align="left">PREZZO</div></td>
    <td><input type="text" name="prezzo" /></td>
    <td >(Es. 123.50, 10, 10.5)</td>
  </tr>
    <tr>
    <td><div align="left">TIPO</div></td>
    <td><input type="text" name="tipo" /></td>
    <td > Massimo 32 caratteri (Es. Sport)</td>
  </tr>
    <tr>
    <td><div align="left">POSTI</div></td>
    <td><input type="text" name="posti" /></td>
    <td > Fino a 999999 (Es. 2000)</td>
  </tr>
  </table>
  <input name="tasto" type="submit" value="MOSTRA"   >
  <input name="tasto" type="submit" value="AGGIUNGI" >
</form>
(Nel caso si aggiunge un evento tutti i campi sono obbligatori)</td>
    <td valign="top"><h2>AMMINISTRA UTENTE</h2>
<form  method="post" action="Pannello">
<table width="500" border="0" cellpadding="0" cellspacing="0">
  <tr>
       <td ><strong>&nbsp;NOME:</strong></td>
       <td ><input type="text" name="nome"/></td>
       <td > Massimo 15 caratteri (Es. Antonio)</td>
  </tr>
  <tr>
          <td ><strong>&nbsp;COGNOME:</strong></td>
          <td ><input type="text" name="cognome"/></td>
          <td > Massimo 15 caratteri (Es. Rossi)</td>
  </tr>
  <tr>
          <td ><strong>&nbsp;INDIRIZZO:</strong></td>
          <td ><input type="text" name="indirizzo"/></td>
          <td > Massimo 32 caratteri (Es. via roma n 7)</td>
  </tr>
  <tr>
          <td ><strong>&nbsp;CODICE FISCALE:</strong></td>
          <td ><input type="text" name="codicefiscale"/> </td>
          <td > 16 caratteri (Es. ADBDRF85S12F839J)</td>
  </tr>
  <tr>
          <td ><strong>&nbsp;CARTA DI CREDITO:</strong></td>
          <td ><input type="text" name="cartadicredito"/></td>
          <td valign="top"> 14 cifre (Es. 12345678901234)</td>
  </tr>
  <tr>
          <td ><strong>&nbsp;EMAIL:</strong></td>
          <td ><input type="text" name="email"/></td>
          <td > (Es. email-prova@email.it)</td>
  </tr>

  <tr>
          <td><br><input name="tasto" type="submit" value="MOSTRA_UTENTI"></td>
  </tr>
</table>
</form></td>
  </tr>
</table>
<hr>
<p>
   <%  
     
     // STAMPA A VIDEO DI DEGLI ESITI CONSEGUENTI ALLE OPERAZIONI EFFETTUATE DALL'AMMINISTRATORE
     String esito = (String) request.getAttribute("Esito_Aggiunta_Evento"); 
     String esito_mod = (String) request.getAttribute("Esito_Modifica_Evento");
     String esito_mod_utente = (String) request.getAttribute("Esito_Modifica_Utente");
     
     String esito_mostra = (String) request.getAttribute("Esito_Mostra_Evento"); 
     String esito_mostra_utente = (String) request.getAttribute("Esito_Mostra_Utente"); 
     
     String esito_eliminazione_evento = (String) request.getAttribute("Esito_Eliminazione_Evento"); 
     String Esito_Eliminazione_Utente = (String) request.getAttribute("Esito_Eliminazione_Utente"); 
     
     if (esito != null)
       out.println(esito);
     else if (esito_mod != null)
    	 out.println(esito_mod);   
     else if (esito_mostra != null)
    	 out.println(esito_mostra);
     else if (esito_mostra_utente != null)
    	 out.println(esito_mostra_utente);
     else if (esito_mod_utente != null)
    	 out.println(esito_mod_utente);
     else if (esito_eliminazione_evento != null)
    	 out.println(esito_eliminazione_evento);
     else if (Esito_Eliminazione_Utente != null)
    	 out.println(Esito_Eliminazione_Utente);
     
   %>
</p>
</body>
</html>