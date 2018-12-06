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


/**
 * Al pannello può accedere solo l'amministratore del sistema.
 * Tale pannello permette la gestione degli eventi e degli
 * utenti, le operazioni che può fare sono aggiungere modificare e/o cancellare
 */
public class Pannello extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	PrintWriter out = response.getWriter();
	String Errore = new String("");
	boolean Error = false;
	String query = new String("");
	String Successo = new String("");
	Controllo reg = new Controllo();
	HttpSession session = request.getSession(true);
	String whologgin = (String) session.getAttribute("loggato");
    
	
	try{
            
            //Richiedo una connessione al connection pool
			DB_Connection CP = DB_Connection.getConnectionPool();
            Connection connection = CP.getConnection();
            
			// Ottengo lo Statement per interagire con il database. 
			Statement statement = connection.createStatement();
			
			//ELIMINAZIONE EVENTO
			if (request.getParameter("idevento") != null && whologgin.matches("admin"))
			{   //Salviamo l'idevento da eliminare
				String idevento = new String (request.getParameter("idevento"));
				//Query sulla tabella prenotazione, bisogna verificare che l'evento richiesto
				//da cancellare non abbia già delle prenotazioni
				String query_del = "SELECT * FROM prenotazione WHERE id_evento='"+idevento+"'";
				ResultSet resultset = statement.executeQuery(query_del);
				
				if (!resultset.next())//Non sono presenti prenotazioni relative all'evento
				{	//Query per l'eliminazione
					query_del = "DELETE FROM evento WHERE id_evento='"+idevento+"'";
					//Esecuzione della delete
					statement.executeUpdate(query_del);
					//Impostiamo il messaggio da visualizzare come esito
					request.setAttribute("Esito_Eliminazione_Evento","Evento eliminato con successo");
				}
				else//E' presente almeno una prenotazione
				{   //Non viene eliminato l'evento e viene impostato il messaggio relativo
					request.setAttribute("Esito_Eliminazione_Evento","Evento non eliminato poichè ha già delle prenotazioni.");
				}
				// Passaggio della stringa alla jsp che la visulizzerà a video
				RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/admin.jsp");
				dispatcher.forward(request, response);
				// Viene rilsciata la connessione al DataBase	
			    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
			    //Redirect ad admin.jsp per mostrare l'esito dell'eliminazione 
				response.sendRedirect("admin.jsp");
			}
			
			//ELIMINAZIONE UTENTE
			if (request.getParameter("userid") != null && whologgin.matches("admin"))
			{   //Salviamo l'userid dell'utente da eliminare
				String userid = new String (request.getParameter("userid"));
				//Query per l'eliminazione delle prenotazioni effettuate dall'utente
				String query_del_user = "DELETE FROM prenotazione WHERE id_user='"+userid+"'";
				//Esecuzione della query
				statement.executeUpdate(query_del_user);
				//Query per l'eliminazione dell'utente
				query_del_user = "DELETE FROM utente WHERE userid='"+userid+"'";
				//Esecuzione della query
				statement.executeUpdate(query_del_user);
				// Passaggio della stringa alla jsp che la visulizzerà a video
				request.setAttribute("Esito_Eliminazione_Utente","Utente eliminato con successo");
				RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/admin.jsp");
				dispatcher.forward(request, response);
				// Viene rilsciata la connessione al DataBase	
			    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
			    //Redirect ad admin.jsp per mostrare l'esito dell'eliminazione 
				response.sendRedirect("admin.jsp");
			}
			
			
			String aggiungi = new String (request.getParameter("tasto"));
			//AGGIUNGI O MOSTRA EVENTO
			if (aggiungi.matches("AGGIUNGI") || aggiungi.matches("MOSTRA"))
			{   //Salviamo i dati del form
				String nomeevento = new String (request.getParameter("nomeevento"));
				String citta = new String (request.getParameter("citta"));
				String luogo = new String (request.getParameter("luogo"));
				String data = new String (request.getParameter("data"));
				String prezzo = new String (request.getParameter("prezzo"));
				String posti = new String (request.getParameter("posti"));
				String tipo = new String (request.getParameter("tipo"));
				
				//AGGIUGNI EVENTO (controllo anche che sia un admin)
				if (aggiungi.matches("AGGIUNGI") && whologgin.matches("admin"))
				{
					//Effettuiamo i controlli sui campi del form
					if( !reg.ControllaInput(32,nomeevento,"SNome")){
						Errore="-Il campo nome è errato <br>";
						Error = true;
					}
					if( !reg.ControllaInput(32,tipo,"SNome")){
						
						Errore+="-Il campo tipo è errato <br>";
						Error = true;
					}
					if( !reg.ControllaInput(32,luogo,"SNome")){
						
						Errore+="-Il campo luogo è errato <br>";
						Error = true;
					}
					if( !reg.ControllaInput(32,data,"data")){
						
						Errore+="-Il campo data è errato <br>";
						Error = true;
					}
					if( !reg.ControllaInput(32,prezzo,"F3.2")){
						
						Errore+="-Il campo prezzo è errato <br>";
						Error = true;
					}
					if( !reg.ControllaInput(6,posti,"N1toSize")){
						
						Errore+="-Il campo posti è errato <br>";
						Error = true;
					}
					if (citta.matches(" "))
					{
						Errore+="-Il campo città non deve essere vuoto<br>";
						Error = true;
					}
					
					if(Error) //Nel caso c'è stato almeno un campo con dati errati
					{
					    request.setAttribute("Esito_Aggiunta_Evento", Errore);
					}
					else //Tutti i campi hanno dato esito positivo al controllo
					{
						//Messaggio di esito
						Successo = new String("Evento inserito con successo.<br><br>");
						//Query per l'inserimento dell'evento
						query="INSERT INTO evento (tipo, luogo, citta, data, posti, prezzo, nome) VALUES ('"+tipo+"', '"+luogo+"', '"+citta+"', '"+data+"', '"+posti+"', '"+prezzo+"', '"+nomeevento+"')"; 
						//Esecuzione della query
						statement.executeUpdate(query);
						request.setAttribute("Esito_Aggiunta_Evento",Successo);
				    }
					
					// Passaggio della stringa alla jsp che la visulizzerà a video
					RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/admin.jsp");
				    dispatcher.forward(request, response);	
				    // Viene rilsciata la connessione al DataBase	
				    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
				    //Redirect ad admin.jsp per mostrare l'esito della modifica dati evento 
					response.sendRedirect("admin.jsp");
				}
				else if (aggiungi.matches("MOSTRA") && whologgin.matches("admin"))//MOSTRA EVENTO 
				{   
					//Iniziamo a comporre la query per la ricerca degli eventi da mostrare
					query="SELECT * FROM evento WHERE "; 
					//Effettuiamo i controlli sui campi del modulo, in questo caso possono essere anche vuoti
					//In caso di assenza di errori continuiamo a concatenare clausole alla query
					//con l'opzione LIKE per facilitare la ricerca da parte dell'utente e dell'amministratore
					if (!tipo.matches(""))
					if( !reg.ControllaInput(32,tipo,"SNome"))
					{		
						Errore="-Il campo tipo è errato <br>";
						Error = true;
					}
					else
					{
						query += "tipo LIKE'%"+tipo+"%' AND ";
					}
					
					if (!nomeevento.matches(""))
					if( !reg.ControllaInput(32,nomeevento,"SNome"))
					{
						Errore+="-Il campo nome_evento è errato <br>";
						Error = true;
					}
					else
					{
						query += "nome LIKE'%"+nomeevento+"%' AND ";
					}

					if (!luogo.matches(""))
					if( !reg.ControllaInput(32,luogo,"SNome"))
					{	
						Errore+="-Il campo luogo è errato <br>";
						Error = true;
					}
					else
					{
						query += "luogo LIKE '%"+luogo+"%' AND ";
					}
					
					if (!data.matches(""))
					if( !reg.ControllaInput(32,data,"data"))
					{	
						Errore+="-Il campo data è errato <br>";
						Error = true;
					}
					else
					{
						query += "data='"+data+"' AND ";
					}
					
					if (!prezzo.matches(""))
					if( !reg.ControllaInput(32,prezzo,"F3.2"))
					{
						Errore+="-Il campo prezzo è errato <br>";
						Error = true;
					}
					else
					{
						query += "prezzo LIKE '%"+prezzo+"%' AND ";
					}
					
					if (!posti.matches(""))
					if( !reg.ControllaInput(6,posti,"N1toSize"))
					{
						Errore+="-Il campo posti è errato <br>";
						Error = true;
					}
					else
					{
						query += "posti='"+posti+"' AND ";
					}
					
					if (!citta.matches(" "))
						query += "citta='"+citta+"' AND ";

					//Nessun campo è stato riempito, l'utente vuole visualizzare tutti i campi
					if (query.matches("SELECT * FROM evento WHERE "))
						query = "SELECT * FROM evento";//Eliminiamo quindi la clausola WHERE
					else //Abbiamo aggiunto una o più clausole a seconda dei moduli
						//Eliminiamo dalla stringa gli ultimi 5 caratteri ovvero l'ultimo AND
						query= query.substring(0,query.length()-5);
					
					if(Error)//Nel caso c'è stato almeno un campo con dati errati
					{
					    request.setAttribute("Esito_Mostra_Evento", Errore);

					}
					else  //Tutti i campi hanno dato esito positivo al controllo
					{  //Viene salvata in una stringa la tabella che conterrà i risultati della ricerca
						Successo = new String("LISTA EVENTI:<br><br><br>");
						Successo += "<table width=\"400\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">";
						Successo += "<tr><td> NOME";	 
						Successo += "</td><td>TIPO";
						Successo += "</td><td>LUOGO";
						Successo += "</td><td>CITTA";
						Successo += "</td><td>PREZZO";
						Successo += "</td><td>POSTI";
						Successo += "</td><td>DATA";
						Successo += "</td><td>modifica";
						Successo += "</td><td>elimina";
						Successo += "</td><td>statistiche";
						Successo += "</td></tr>";
						
						//ESECUZIONE DELLA QUERY
						ResultSet resultset = statement.executeQuery(query);
						
						int i=0;// variabile per colorare alternativamente le righe della tabella
						
						//salviamo le righe del risultato della query nella stringa 
						while (resultset.next())
						{
							 if (i==0)
							 {
								 Successo +="<tr bgcolor=\"#DCDCDC\">";
								 i++;
							 }
							 else
							 {
								 Successo +="<tr bgcolor=\"#E9E9E9\">";
								 i--;
							 }
							 Successo += "<td>";	 
							 Successo += resultset.getString("nome");
							 Successo += "</td><td>";
							 Successo += resultset.getString("tipo");
							 Successo += "</td><td>";
							 Successo += resultset.getString("luogo");
							 Successo += "</td><td>";
							 Successo += resultset.getString("citta");
							 Successo += "</td><td>";
							 Successo += resultset.getString("prezzo");
							 Successo += "</td><td>";
							 Successo += resultset.getString("posti");
							 Successo += "</td><td>";
							 Successo += resultset.getString("data");
							 Successo += "</td><td>";
							 Successo += "<a href=\"modifica.jsp?id_evento="+resultset.getInt("id_evento")+"\">MODIFICA</a>";
							 Successo += "</td><td>";
							 Successo += "<a href=\"Pannello?idevento="+resultset.getInt("id_evento")+"\" onclick=\"javascript:return window.confirm('Confermi la cancellazione?');\">ELIMINA</a>";
							 Successo += "</td><td>";
							 Successo += "<a href=\"ElencoEventi?idevento="+resultset.getInt("id_evento")+"\">STATISTICHE</a>";
							 Successo += "</td></tr>";
						}	
						Successo += "</table>";	
						request.setAttribute("Esito_Mostra_Evento",Successo);
				    }
					// Passaggio della stringa alla jsp che la visulizzerà a video 
					RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/admin.jsp");
				    dispatcher.forward(request, response);	
				    // Viene rilsciata la connessione al DataBase	
				    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
				    // Redirect alla jsp
					response.sendRedirect("admin.jsp");
				}
			}//MOSTRA UTENTI (l'utente deve essere admin)
			else if (aggiungi.matches("MOSTRA_UTENTI") && whologgin.matches("admin"))
			{   //Salviamo il contenuto dei campi del form
				String nome = new String (request.getParameter("nome"));
				String cognome = new String (request.getParameter("cognome"));
				String indirizzo = new String (request.getParameter("indirizzo"));
				String email = new String (request.getParameter("email"));
				String carta = new String (request.getParameter("cartadicredito"));
				String cf = new String (request.getParameter("codicefiscale"));
				//Iniziamo a comporre la query per la ricerca degli utenti da mostrare
				query="SELECT * FROM utente WHERE "; 
				//Effettuiamo i controlli sui campi del modulo, in questo caso possono essere anche vuoti
				//In caso di assenza di errori continuiamo a concatenare clausole alla query
				//con l'opzione LIKE per facilitare la ricerca da parte dell'amministratore
				if (!nome.matches(""))
				if( !reg.ControllaInput(15,nome,"SNome"))
				{	
					Errore="-Il campo nome è errato <br>";
					Error = true;
				}
				else
				{
					query += "nome LIKE '%"+nome+"%' AND ";
				}
			
				if (!cognome.matches(""))
				if(!reg.ControllaInput(15,cognome,"SNome"))
				{
					Errore+="-Il campo cognome è errato <br>";
					Error = true;
				}
				else
				{
					query += "cognome LIKE '%"+cognome+"%' AND ";
				}
				
				if (!indirizzo.matches(""))
				if(!reg.ControllaInput(32,indirizzo,"NSSize"))
				{
					Errore+="-Il campo indirizzo è errato <br>";
					Error = true;
				}
				else
				{
					query += "indirizzo LIKE '%"+indirizzo+"%' AND ";
				}
				
				if (!carta.matches(""))
				if(!reg.ControllaInput(14,carta,"N1toSize"))
				{
					Errore+="-Il campo carta di credito è errato <br>";
					Error = true;
				}
				else
				{
					query += "carta_credito='"+carta+"' AND ";
				}
				
				if (!cf.matches(""))
				if(!reg.ControllaInput(0,cf,"CF"))
				{
					Errore+="-Il campo codice fiscale è errato <br>";
					Error = true;
				}
				else
				{
					query += "codice_fiscale='"+cf+"' AND ";
				}
				
				if (!email.matches(""))
				if(!reg.ControllaInput(32,email,"Email"))
				{
					Errore+="-Il campo Email è errato <br>";
					Error = true;
				}
				else
				{
					query += "email='"+email+"' AND ";
				}
				
				//Nessun campo è stato riempito, l'utente vuole visualizzare tutti i campi
				if (query.matches("SELECT * FROM utente WHERE "))
					query = "SELECT * FROM utente";
				else //Abbiamo aggiunto una o più clausole a seconda dei moduli
					//Eliminiamo dalla stringa gli ultimi 5 caratteri ovvero l'ultimo AND
					query= query.substring(0,query.length()-5);
			
				if(Error)//Nel caso c'è stato almeno un campo con dati errati
				{
					request.setAttribute("Esito_Mostra_Utente", Errore);
				}
				else  //Tutti i campi hanno dato esito positivo al controllo
				{  //Viene salvata in una stringa la tabella che conterrà i risultati della ricerca
					Successo = new String("LISTA UTENTI:<br><br><br>");
					Successo += "<table width=\"900\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">";
					Successo += "<tr><td align=\"center\"> NOME";	 
					Successo += "</td><td align=\"center\">COGNOME";
					Successo += "</td><td align=\"center\">CARTA DI CREDITO";
					Successo += "</td><td align=\"center\">CODICE FISCALE";
					Successo += "</td><td align=\"center\">EMAIL";
					Successo += "</td><td align=\"center\">INDIRIZZO";
					Successo += "</td><td align=\"center\">MODIFICA";
					Successo += "</td><td align=\"center\">ELIMINA";
					Successo += "</td><td align=\"center\">STATISTICHE";
					Successo += "</td></tr>";
				
					//ESECUZIONE DELLA QUERY
				    ResultSet resultset = statement.executeQuery(query);
				
				    int i=0;// variabile per colorare alternativamente le righe della tabella
				    //salviamo le righe del risultato della query nella stringa 
				    while (resultset.next())
				    { 
				    	if (i==0)
				    	{
				    		Successo +="<tr bgcolor=\"#DCDCDC\">";
				    		i++;
				    	}
				    	else
				    	{
				    		Successo +="<tr bgcolor=\"#E9E9E9\">";
				    		i--;
				    	}

				    	Successo += "<td>";	 
				    	Successo += resultset.getString("nome");
				    	Successo += "</td><td>";
				    	Successo += resultset.getString("cognome");
				    	Successo += "</td><td>";
				    	Successo += resultset.getString("carta_credito");
				    	Successo += "</td><td>";
				    	Successo += resultset.getString("codice_fiscale");
				    	Successo += "</td><td>";
				    	Successo += resultset.getString("email");
				    	Successo += "</td><td>";
				    	Successo += resultset.getString("indirizzo");
				    	Successo += "</td><td>";
				    	Successo += "<a href=\"modifica_utente.jsp?userid="+resultset.getInt("userid")+"\">MODIFICA</a>";
				    	Successo += "</td><td>";
				    	Successo += "<a href=\"Pannello?userid="+resultset.getInt("userid")+"\" onclick=\"javascript:return window.confirm('Confermi la cancellazione?');\">ELIMINA</a>";
				    	Successo += "</td><td>";
				    	Successo += "<a href=\"ElencoPrenotazioni?userid="+resultset.getInt("userid")+"\">STATISTICHE</a>";
				    	Successo += "</td></tr>";

				    }	
				    Successo += "</table>";	
				    request.setAttribute("Esito_Mostra_Utente",Successo);
		       }
			}
			// Passaggio della stringa alla jsp che la visulizzerà a video 
			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/admin.jsp");
		    dispatcher.forward(request, response);	
		    // Viene rilsciata la connessione al DataBase
		    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
		    // Redirect alla jsp
			response.sendRedirect("admin.jsp");
			
		}	
        catch(Exception e)
        {	
        	//Redirect alla pagina errore.jsp nella quale viene mostrato a video un messaggio di errore generico
        	//ed il relativo messaggio di errore catturato dall'eccezione.
        	out.println("<script type=\"text/javascript\">window.location = \"errore.jsp?Errore="+e.getMessage()+"\"</script>");
        }
	}

}