package GEM10;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 *  Con questa servlet l'utente loggato può effettuare un completo iter di
 *  prenotazione di un evento: dalla ricerca al pagamento.
 */
public class Prenotazione extends HttpServlet {
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
		HttpSession session = request.getSession(true);
		Controllo reg = new Controllo();
		String whologgin = (String) session.getAttribute("loggato");
			
		try{
            
            //Richiedo una connessione al connection pool
			DB_Connection CP = DB_Connection.getConnectionPool();
            Connection connection = CP.getConnection();
			
			// Ottengo lo Statement per interagire con il database. 
			Statement statement = connection.createStatement();
		    
			// ELIMINAZIONE PRENOTAZIONE
			if (request.getParameter("id_prenotazione") != null && whologgin != null)
			{   //Salviamo l'idevento da eliminare
				String id_prenotazione = new String (request.getParameter("id_prenotazione"));
				//Query sulla tabella prenotazione
				String query_del = "DELETE FROM prenotazione WHERE id_prenotazione='"+id_prenotazione+"'";
				statement.executeUpdate(query_del);
				// Viene rilsciata la connessione al DataBase
			    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
			    //Redirect ad admin.jsp per mostrare l'esito dell'eliminazione 
				response.sendRedirect("ElencoPrenotazioni");
			}
			
			// NEL CASO E' RICHIAMATA DA UN FORM: BISOGNA LEGGERE IL VALORE DEL TASTO SUBMIT
			// I POSSIBILI CASI SONO:
			// - RICERCA EVENTO --> dalla index.jsp (utente semplice o loggato)
			// - AVANTI --> dalla prenotazione.jsp
			// - PAGA --> da pagamnento.jsp
			
			String aggiungi = new String (request.getParameter("tasto"));
			
			if (aggiungi.matches("RICERCA EVENTO"))
			{   //Salviamo i campi del form
				String nomeevento = new String (request.getParameter("nomeevento"));
				String citta = new String (request.getParameter("citta"));
				String luogo = new String (request.getParameter("luogo"));
				String data = new String (request.getParameter("data"));
				String prezzo = new String (request.getParameter("prezzo"));
				String tipo = new String (request.getParameter("tipo"));
				//Iniziamo a creare la query per la ricerca di un evento
				//Salviamo anche il risultato di disponibili ovvero i posti ancora acquistabili per l'evento
				query="SELECT *, e.posti-sum(COALESCE(p.biglietti,0)) as disponibili FROM evento e left join prenotazione p on e.id_evento=p.id_evento WHERE ";
				//Effettuiamo i controlli sui campi del modulo, in questo caso possono essere anche vuoti
				//In caso di assenza di errori continuiamo a concatenare clausole alla query
				//con l'opzione LIKE per facilitare la ricerca da parte dell'utente
				if (!tipo.matches(""))
				if( !reg.ControllaInput(32,tipo,"SNome"))
				{
					Errore="-Il campo tipo è errato <br>";
					Error = true;
				}
				else
				{
					query += "tipo LIKE '%"+tipo+"%' AND ";
				}
				
				if (!nomeevento.matches(""))
				if( !reg.ControllaInput(32,nomeevento,"SNome"))
				{	
					Errore+="-Il campo nome_evento è errato <br>";
					Error = true;
				}
				else
				{
					query += "nome LIKE '%"+nomeevento+"%' AND ";
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
					query += "e.data='"+data+"' AND ";
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
					
				if (!citta.matches(" "))
					query += "citta='"+citta+"' AND ";
				
				//Infine concateniamo il controllo per filtrare gli eventi con una data successiva alla data corrente
                query+="STR_TO_DATE(e.data,'%d/%m/%Y') > SYSDATE() GROUP BY e.id_evento";

				if(Error)//Nel caso c'è stato almeno un campo con dati errati
				{
				    request.setAttribute("Esito_Mostra_Evento", Errore);
				}
				else  //Tutti i campi hanno dato esito positivo al controllo
				{  //Viene salvata in una stringa la tabella che conterrà i risultati della ricerca
					 Successo = new String("<br><br>LISTA EVENTI:<br>");
					 if (whologgin == null)//Nel caso di un visitatore non loggato
						 Successo += "Nota Bene - Per prenotare un evento devi aver effettuato il login.<br><br>";
					 
					 Successo += "<table width=\"700\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">";
					 Successo += "<tr><td> NOME";	 
					 Successo += "</td><td>TIPO";
					 Successo += "</td><td>LUOGO";
					 Successo += "</td><td>CITTA";
					 Successo += "</td><td>PREZZO";
					 Successo += "</td><td>DISPONIBILI DI TOTALI";
					 Successo += "</td><td>DATA";
					 Successo += "</td><td>PRENOTA";
					 Successo += "</td></tr>";
					
					 //ESECUZIONE DELLA QUERY
					 ResultSet resultset = statement.executeQuery(query);
					
					 int i=0;// variabile per colorare alternativamente le righe della tabella
					
					 //salviamo le righe del risultato della query nella stringa 
					 while (resultset.next())
					 {   //Controlliamo la variabile disponibili per visualizzare solo eventi con posti acquistabili
						 if (resultset.getInt("disponibili")>0)
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
							 Successo += resultset.getInt("disponibili")+"/"+resultset.getString("posti");
							 Successo += "</td><td>";
							 Successo += resultset.getString("data");
							 Successo += "</td><td>";
							 if (whologgin != null)
								 //Se il visitatore è loggato allora ha la possibilità di prenotare e gli viene visualizzato il link
								 Successo += "<a href=\"prenotazione.jsp?id_evento="+resultset.getInt("id_evento")+"&disponibili="+resultset.getInt("disponibili")+"\">PRENOTA</a>";	
							 else
								 //Altrimenti la scritta PRENOTA non sarà cliccabile
								 Successo += "PRENOTA";
							 Successo += "</td></tr>";
						 } 
					 }	
					 Successo += "</table>";	
					 request.setAttribute("Esito_Mostra_Evento",Successo);
			    }
				// Passaggio della stringa alla jsp che la visulizzerà a video 
				RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
			    dispatcher.forward(request, response);	
			    // Viene rilsciata la connessione al DataBase	
			    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
			    // Redirect alla jsp
				response.sendRedirect("index.jsp");
			}		
			else if (aggiungi.matches("AVANTI"))//Dalla prenotazione.jsp
			{   if(whologgin == null)
					response.sendRedirect("admin.jsp");
				//Salviamo i dati del form
				String posti = new String (request.getParameter("posti"));
				String id_evento = new String (request.getParameter("id_evento"));
				String disponibili = new String (request.getParameter("disponibili"));
				String Errore_Posti = new String ("Errore: ");
				//Controlliamo la validità del campo inserito 
				//Gli altri sono passati come hidden e sicuro corretti
				if(!reg.ControllaInput(3,posti,"N1toSize"))
				{
					Errore_Posti +="Il numero posti deve essere massimo di 3 cifre.<br>";
				}
				else if (Integer.parseInt(posti) > Integer.parseInt(disponibili))
				{
					Errore_Posti +="Il numero posti deve essere minore di quelli disponibili.<br>";
				}
				if (!Errore_Posti.matches("Errore: "))//Ci sono errori nel campo controllato
					//Si ritorna alla pagina prenotazione.jsp per consentire all'utente
					//di inserire un valore corretto nel campo
					response.sendRedirect("prenotazione.jsp?id_evento="+id_evento+"&disponibili="+disponibili+"&errore="+Errore_Posti);
				//Query richiederà al database il prezzo dell'evento che l'utente sta per prenotare
				query="SELECT prezzo FROM evento WHERE id_evento='"+id_evento+"'"; 
				//Eseguiamo la query	
                ResultSet resultset = statement.executeQuery(query);
				//In base al prezzo calcoliamo il totale da pagare
                if (resultset.next())
				{	
					float totale = Float.parseFloat(resultset.getString("prezzo"))*Integer.parseInt(posti);
					request.setAttribute("totale",totale);
				}	
	
                whologgin = (String) session.getAttribute("userid");
                //Query richiederà al database carta_credito per impostare la carta di credito di default (eventualmente modificabile)
				query="SELECT carta_credito FROM utente WHERE userid='"+whologgin+"'"; 
				//Eseguiamo la query
				resultset = statement.executeQuery(query);
                if (resultset.next())
				{   
                	String carta = resultset.getString("carta_credito");
                	request.setAttribute("carta", carta);
				}           
                // Passaggio delle variabili alla jsp pagamento
			    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pagamento.jsp");
				dispatcher.forward(request, response);
				// Viene rilsciata la connessione al DataBase
			    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
			    // Redirect alla jsp
				response.sendRedirect("pagamento.jsp?id_evento="+id_evento+"&posti="+posti);

			}
			else if (aggiungi.matches("PAGA"))//Dalla pagamento.jsp
			{   
				if(whologgin == null)
					response.sendRedirect("admin.jsp");
				//Salviamo i dati del form
				String posti = new String (request.getParameter("posti"));
				String id_evento = new String (request.getParameter("id_evento"));
				String carta = new String (request.getParameter("carta"));
				String totale = new String (request.getParameter("totale"));
				
				String Errore_Carta = new String ("Errore: ");
                
				if(!reg.ControllaInput(14,carta,"N1toSize"))//Controllo da esito negativo
				{   //Settiamo la stringa da visualizzare come esito
					Errore_Carta ="-Il campo carta di credito è errato <br>";
					//Bisogna ritornare alla pagina pagamento per consentire all'utente
					//di modificare i dati, bisogna però conservare alcune informazioni:
					request.setAttribute("carta", carta);
					request.setAttribute("totale",totale);
					RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/pagamento.jsp");
					dispatcher.forward(request, response);
					response.sendRedirect("pagamento.jsp?id_evento="+id_evento+"&posti="+posti+"&errore_carta="+Errore_Carta);
				}	
				else //Il controllo è stato eseguito con successo
				{
					//Salviamo l'userid dell'utente che sta effettuando l'acquisto
					String userid = (String) session.getAttribute("userid");
					//Generiamo un codice alfanumerico casuale con l'ausilio del metodo randomString
					String codice= randomString(10);
					//Concateniamo al codice alfanumerico l'userid in modo tale da ridurre le possibilità
					//di duplicazioni del codice univoco di accesso (in ogni caso il database ha il vincolo di unicità)
					codice += userid;
					
					//A QUESTO PUNTO IL SISTEMA PRIMA DI EFFETTUARE LA PRENOTAZIONE SI DEVE
					//COLLEGARE CON UN SISTEMA BANCARIO CHE DEVE REALIZZARE LA TRANSAZIONE CON
					//CARTA DI CREDITO E RITORNARE UNA VARIABILE DI ESITO OPERAZIONE.
					//QUESTA FUNZIONALITA' E' DA IMPLEMENTARE
					
					//Query richiederà al database l'inserimento nella tabella prenotazione
					query="INSERT INTO prenotazione  (id_user, id_evento, biglietti, data, codice) VALUES ('"+userid+"','"+id_evento+"','"+posti+"', SYSDATE(),'" +codice+"')"; 
					statement.executeUpdate(query);
					//Verrà mostrato all'utente un messaggio di esito e il codice per l'accesso all'evento
					Successo = "Pagamento avvenuto con successo, la prenotazione è stata effettuata.<br><br>";
					Successo += "Il codice per accedere all'evento è: "+codice;
					// Passaggio della stringa alla jsp che la visulizzerà a video 
					request.setAttribute("Esito",Successo);
					RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/esito_pagamento.jsp");
				    dispatcher.forward(request, response);
				    // Viene rilsciata la connessione al DataBase
				    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
				    // Redirect alla jsp
					response.sendRedirect("esito_pagamento.jsp");
				    
			    }
			}
		  }	
        catch(Exception e)
        {	
        	//Redirect alla pagina errore.jsp nella quale viene mostrato a video un messaggio di errore generico
        	//ed il relativo messaggio di errore catturato dall'eccezione.
        	out.println("<script type=\"text/javascript\">window.location = \"errore.jsp?Errore="+e.getMessage()+"\"</script>");
        }
        }
	
	
	public static String randomString (int length) {
	    Random rnd = new Random ();
	    char[] arr = new char[length];

	    for (int i=0; i<length; i++) {
	        int n = rnd.nextInt (36);
	        arr[i] = (char) (n < 10 ? '0'+n : 'a'+n-10);
	    }

	    return new String (arr);
	}

}


