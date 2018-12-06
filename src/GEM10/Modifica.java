package GEM10;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**Modifica.java
 * La Servlet si occupa di gestire la modifica dei dati relativi all'evento,
 * solo l'admin ha il permesso di accedere a questa servlet
 */
public class Modifica extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession(true);
		String whologgin = (String) session.getAttribute("loggato");
		if(!whologgin.matches("admin"))
			response.sendRedirect("admin.jsp");
		
		
		try{
				//Richiedo una connessione al connection pool
				DB_Connection CP = DB_Connection.getConnectionPool();
				Connection connection = CP.getConnection();
		
				// Ottengo lo Statement per interagire con il database. 
				Statement statement = connection.createStatement();
				// Salviamo il parametro id_evento passato nell'url 
				String id_evento = (String) request.getParameter("id_evento");
				if (id_evento != null)
				{	// quando questa servlet viene richiamata per generare il form per la modifica dei dati evento
					// il form avrà i campi riempiti con i dati dell'evento idevento
		
					String query = "SELECT * FROM evento WHERE id_evento='"+id_evento+"'";
					//Eseguiamo la Query
					ResultSet resultset = statement.executeQuery(query);
					// Avanziamo e ci posizioniamo sulla riga del risultato della query
					resultset.next();
		            //Salviamo nella stringa form il form da stampare con i dati dell'evento come valore
					//di default nei moduli
					String form = "<form action=\"Modifica\">";
					form += "<strong>EVENTO DA MODIFICARE:</strong>";
					form += "<table width=\"368\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
					form += "<table width=\"368\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
					form += "<tr><th width=\"129\" scope=\"row\"><div align=\"left\">NOME EVENTO </div></th>";
					form += "<td width=\"231\"><input type=\"text\" name=\"nomeevento\" value=\""+resultset.getString("nome")+"\"></td></tr>";
					form += "<tr><td> <div align=\"left\"><strong>CITTA'</strong></div></td>";
					form += "<td><select name=\"citta\"><option value=\""+resultset.getString("citta")+"\" selected=\"selected\">"+resultset.getString("citta")+"</option><option value=\"Agrigento\">Agrigento</option><option value=\"Alessandria\">Alessandria</option><option value=\"Ancona\">Ancona</option><option value=\"Aosta\">Aosta</option><option value=\"Arezzo\">Arezzo</option><option value=\"Ascoli Piceno\">Ascoli Piceno</option><option value=\"Asti\">Asti</option><option value=\"Avellino\">Avellino</option><option value=\"Bari\">Bari</option><option value=\"Barletta-Andria-Trani\">Barletta-Andria-Trani</option><option value=\"Belluno\">Belluno</option><option value=\"Benevento\">Benevento</option><option value=\"Bergamo\">Bergamo</option><option value=\"Biella\">Biella</option><option value=\"Bologna\">Bologna</option><option value=\"Bolzano\">Bolzano</option><option value=\"Brescia\">Brescia</option><option value=\"Brindisi\">Brindisi</option><option value=\"Cagliari\">Cagliari</option><option value=\"Caltanissetta\">Caltanissetta</option><option value=\"Campobasso\">Campobasso</option><option value=\"Carbonia Iglesias\">Carbonia Iglesias</option><option value=\"Caserta\">Caserta</option><option value=\"Catania\">Catania</option><option value=\"Catanzaro\">Catanzaro</option><option value=\"Chieti\">Chieti</option><option value=\"Como\">Como</option><option value=\"Cosenza\">Cosenza</option><option value=\"Cremona\">Cremona</option><option value=\"Crotone\">Crotone</option><option value=\"Cuneo\">Cuneo</option><option value=\"Enna\">Enna</option><option value=\"Fermo\">Fermo</option><option value=\"Ferrara\">Ferrara</option><option value=\"Firenze\">Firenze</option><option value=\"Foggia\">Foggia</option><option value=\"Forli e Cesena\">Forli e Cesena</option><option value=\"Frosinone\">Frosinone</option><option value=\"Genova\">Genova</option><option value=\"Gorizia\">Gorizia</option><option value=\"Grosseto\">Grosseto</option><option value=\"Imperia\">Imperia</option><option value=\"Isernia\">Isernia</option><option value=\"La Spezia\">La Spezia</option><option value=\"L'Aquila\">L'Aquila</option><option value=\"Latina\">Latina</option><option value=\"Lecce\">Lecce</option><option value=\"Lecco\">Lecco</option><option value=\"Livorno\">Livorno</option><option value=\"Lodi\">Lodi</option><option value=\"Lucca\">Lucca</option><option value=\"Macerata\">Macerata</option><option value=\"Mantova\">Mantova</option><option value=\"Massa-Carrara\">Massa-Carrara</option><option value=\"Matera\">Matera</option><option value=\"Medio Campidano\">Medio Campidano</option><option value=\"Messina\">Messina</option><option value=\"Milano\">Milano</option><option value=\"Modena\">Modena</option><option value=\"Monza e Brianza\">Monza e Brianza</option><option value=\"Napoli\">Napoli</option><option value=\"Novara\">Novara</option><option value=\"Nuoro\">Nuoro</option><option value=\"Ogliastra\">Ogliastra</option><option value=\"Olbia-Tempio\">Olbia-Tempio</option><option value=\"Oristano\">Oristano</option><option value=\"Padova\">Padova</option><option value=\"Palermo\">Palermo</option><option value=\"Parma\">Parma</option><option value=\"Pavia\">Pavia</option><option value=\"Perugia\">Perugia</option><option value=\"Pesaro e Urbino\">Pesaro e Urbino</option><option value=\"Pescara\">Pescara</option><option value=\"Piacenza\">Piacenza</option><option value=\"Pisa\">Pisa</option><option value=\"Pistoia\">Pistoia</option><option value=\"Pordenone\">Pordenone</option><option value=\"Potenza\">Potenza</option><option value=\"Prato\">Prato</option><option value=\"Ragusa\">Ragusa</option><option value=\"Ravenna\">Ravenna</option><option value=\"Reggio Calabria\">Reggio Calabria</option><option value=\"Reggio Emilia\">Reggio Emilia</option><option value=\"Rieti\">Rieti</option><option value=\"Rimini\">Rimini</option><option value=\"Roma\">Roma</option><option value=\"Rovigo\">Rovigo</option><option value=\"Salerno\">Salerno</option><option value=\"Sassari\">Sassari</option><option value=\"Savona\">Savona</option><option value=\"Siena\">Siena</option><option value=\"Siracusa\">Siracusa</option><option value=\"Sondrio\">Sondrio</option><option value=\"Taranto\">Taranto</option><option value=\"Teramo\">Teramo</option><option value=\"Terni\">Terni</option><option value=\"Torino\">Torino</option><option value=\"Trapani\">Trapani</option><option value=\"Trento\">Trento</option><option value=\"Treviso\">Treviso</option><option value=\"Trieste\">Trieste</option><option value=\"Udine\">Udine</option><option value=\"Varese\">Varese</option><option value=\"Venezia\">Venezia</option><option value=\"Verbano-Cusio-Ossola\">Verbano-Cusio-Ossola</option><option value=\"Vercelli\">Vercelli</option><option value=\"Verona\">Verona</option><option value=\"Vibo Valentia\">Vibo Valentia</option><option value=\"Vicenza\">Vicenza</option><option value=\"Viterbo\">Viterbo</option></select>";
					form += "</td></tr><tr><th scope=\"row\"><div align=\"left\">LUOGO</div></th>";
					form += " <td><input type=\"text\" name=\"luogo\" value=\""+ resultset.getString("luogo")+"\"/></td></tr>";
					form += "<tr> <th scope=\"row\"><div align=\"left\">DATA</div></th>";
					form += "<td><input type=\"text\" name=\"data\" value=\""+ resultset.getString("data")+"\"/></td> </tr>";
					form += " <tr><th scope=\"row\"><div align=\"left\">PREZZO</div></th>";
					form += "<td><input type=\"text\" name=\"prezzo\" value=\""+ resultset.getString("prezzo")+"\"/></td>		  </tr>";
					form += "<tr><th scope=\"row\"><div align=\"left\">TIPO</div></th>";
					form += "<td><input type=\"text\" name=\"tipo\" value=\""+ resultset.getString("tipo")+"\"/></td>		  </tr>";
					form += "<tr><th scope=\"row\"><div align=\"left\">POSTI</div></th>"; 
					form += "<td><input type=\"text\" name=\"posti\" value=\""+ resultset.getString("posti")+"\"/></td> </tr>"; 
					form += "</table>";
					form += "<input type=\"hidden\" name=\"idevento\" value=\""+ id_evento+"\"/>";
					form += "<input name=\"tasto\" type=\"submit\" value=\"MODIFICA\">";
					form += "</form>";    
					// Passaggio della stringa alla jsp che la visulizzerà a video 
					request.setAttribute("form",form);
					RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/modifica.jsp");
					dispatcher.forward(request, response);
					// Viene rilsciata la connessione al DataBase
					CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
					// Redirect alla jsp
					response.sendRedirect("modifica.jsp");
				}
				else
				{   // In questo caso bisogna salvare il contenuto di tutti i moduli del form e bisogna
					// eseguire una query per l'aggiornamento dei dati dell'evento

					String nomeevento = new String (request.getParameter("nomeevento"));
					String citta = new String (request.getParameter("citta"));
					String luogo = new String (request.getParameter("luogo"));
					String data = new String (request.getParameter("data"));
					String prezzo = new String (request.getParameter("prezzo"));
					String posti = new String (request.getParameter("posti"));
					String tipo = new String (request.getParameter("tipo"));
					String evento = new String (request.getParameter("idevento"));
			
					String Errore = new String("");
					boolean Error = false;

					Controllo reg = new Controllo();
					// I campi vanno ricontrollati in quanto potrebbero risultare errati dopo la modifica
					if( !reg.ControllaInput(32,nomeevento,"SNome")){
				
						Errore="-Il campo nome è errato <br>";
						Error = true;
					}
					if( !reg.ControllaInput(32,tipo,"SNome")){
						
						Errore="-Il campo tipo è errato <br>";
						Error = true;
					}
					if( !reg.ControllaInput(32,luogo,"SNome")){
				
						Errore="-Il campo luogo è errato <br>";
						Error = true;
					}
					if( !reg.ControllaInput(32,data,"data")){
				
						Errore="-Il campo data è errato <br>";
						Error = true;
					}
					if( !reg.ControllaInput(32,prezzo,"F3.2")){
				
						Errore="-Il campo prezzo è errato <br>";
						Error = true;
					}
					if( !reg.ControllaInput(6,posti,"N1toSize")){
				
						Errore="-Il campo posti è errato <br>";
						Error = true;
					}
			
					if(Error) //C'è almeno un campo del form che contiene errori
					{
						request.setAttribute("Esito_Modifica_Evento", Errore);
					}
					else // Tutti i campi hanno dato esito positvo ai controlli
					{
						String Successo = new String("Evento modificato con successo.<br><br>");
						//ESECUZIONE DELLA QUERY
						String query="UPDATE evento SET tipo='"+tipo+"', luogo='"+luogo+"', citta='"+citta+"', data='"+data+"', posti='"+posti+"', prezzo='"+prezzo+"', nome='"+nomeevento+"' WHERE id_evento='"+evento+"'"; 
						statement.executeUpdate(query);
						request.setAttribute("Esito_Modifica_Evento",Successo);
    
					}
					// Passaggio della stringa alla jsp che la visulizzerà a video
					RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/admin.jsp");
					dispatcher.forward(request, response);	
					// Viene rilsciata la connessione al DataBase	
					CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
					//Redirect ad admin.jsp per mostrare l'esito della modifica dati evento 
					response.sendRedirect("admin.jsp");
				}
		
		}	
        catch(Exception e){
        	//Redirect alla pagina errore.jsp nella quale viene mostrato a video un messaggio di errore generico
        	//ed il relativo messaggio di errore catturato dall'eccezione.
        	out.println("<script type=\"text/javascript\">window.location = \"errore.jsp?Errore="+e.getMessage()+"\"</script>");
        	
        }
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
