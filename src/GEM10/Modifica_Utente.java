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


/**Modifica_utente.java
 * La servlet si occupa di gestire la modifica dei dati relativi all'utente,
 * controllando che l'email utilizzata non sia già presente nel database,
 * solo l'admin ha il permesso di accedere a questa servlet
 */
public class Modifica_Utente extends HttpServlet {
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
		HttpSession session = request.getSession(true);
		String whologgin = (String) session.getAttribute("loggato");
		
		try{
				//Richiedo una connessione al connection pool
				DB_Connection CP = DB_Connection.getConnectionPool();
				Connection connection = CP.getConnection();
		
				// Ottengo lo Statement per interagire con il database. 
				Statement statement = connection.createStatement();
		        // Salviamo il parametro userid passato nell'url 
				String userid = (String) request.getParameter("userid");
				if(!whologgin.matches("admin"))
					response.sendRedirect("admin.jsp");
				
				if (userid != null )
				{	// quando questa servlet viene richiamata per generare il form per la modifica dei dati utente
					// il form avrà i campi riempiti con i dati dell'utente userid
		
					String query = "SELECT * FROM utente WHERE userid='"+userid+"'";//Query per i dati dell'utente
					//Eseguiamo la Query
					ResultSet resultset = statement.executeQuery(query);
					// Avanziamo e ci posizioniamo sulla riga del risultato della query
					resultset.next();
		            //Salviamo nella stringa form il form da stampare con i dati dell'utente come valore
					//di default nei moduli
					String form = "<form method=\"post\" action=\"Modifica_Utente\">";
					form +="<strong>UTENTE DA MODIFICARE:</strong>";
					form +="<table width=\"900\" border=\"0\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\">";
					form +="<tr>";
					form +="<td valign=\"top\"><strong>&nbsp;NOME:</strong></td>";
					form +="<td valign=\"top\"><input type=\"text\" name=\"nome\" value=\""+resultset.getString("nome")+"\"></td>";
					form +="<td valign=\"top\"> Stringa non vuota massimo 15 car, Es. Antonio</td>";
					form +="</tr>";
					form +="<tr>";
					form +="<td valign=\"top\"><strong>&nbsp;COGNOME:</strong></td>";
					form +="<td valign=\"top\"><input type=\"text\" name=\"cognome\" value=\""+resultset.getString("cognome")+"\"></td>";
					form +="<td valign=\"top\"> Stringa non vuota massimo 15 car, Es. Rossi</td>";
					form +="</tr>";
					form +="<tr>";
					form +="<td valign=\"top\"><strong>&nbsp;INDIRIZZO:</strong></td>";
					form +="<td valign=\"top\"><input type=\"text\" name=\"indirizzo\" value=\""+resultset.getString("indirizzo")+"\"></td>";
					form +="<td valign=\"top\"> Stringa alfanumerica non vuota massimo 32 car, Es. via degli oleandri n 15</td>";
					form +="</tr>";
					form +="<tr>";
					form +="<td valign=\"top\"><strong>&nbsp;CODICE FISCALE:</strong></td>";
					form +="<td valign=\"top\"><input type=\"text\" name=\"codicefiscale\" value=\""+resultset.getString("codice_fiscale")+"\"> </td>";
					form +="<td valign=\"top\"> Stringa di 16 car, Es. ADBDRF85S12F839J</td>";
					form +="</tr>";
					form +="<tr>";
					form +="<td valign=\"top\"><strong>&nbsp;CARTA DI CREDITO:</strong></td>";
					form +="<td valign=\"top\"><input type=\"text\" name=\"cartadicredito\" value=\""+resultset.getString("carta_credito")+"\"></td>";
					form +="<td valign=\"top\"> Stringa numerica non vuota massimo 14 car, Es. 12345678901234</td>";
					form +="</tr>";
					form +="<tr>";
					form +="<td valign=\"top\"><strong>&nbsp;EMAIL:</strong></td>";
					form +="<td valign=\"top\"><input type=\"text\" name=\"email\" value=\""+resultset.getString("email")+"\"></td>";
					form +="<td valign=\"top\"> Stringa non vuota massimo 32 car, Es. email-prova@email.it</td>";
					form +="</tr>";
					form +="<tr>";
					form +="<td><br><input name=\"tasto\" type=\"submit\" value=\"MODIFICA_UTENTE\"></td>";
					form +="</tr>";
					form += "<input type=\"hidden\" name=\"user_id\" value=\""+ userid+"\"/>";
					form +="</table>";
					form +="</form>";
					
					// Passaggio della stringa alla jsp che la visulizzerà a video 
					request.setAttribute("form",form);
					RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/modifica_utente.jsp");
					dispatcher.forward(request, response);
					// Viene rilsciata la connessione al DataBase
					CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
					// Redirect alla jsp
					response.sendRedirect("modifica_utente.jsp");
				}
				else
				{   // In questo caso bisogna salvare il contenuto di tutti i moduli del form e bisogna
					// eseguire una query per l'aggiornamento dei dati dell'utente
			
					String nome = new String (request.getParameter("nome"));
					String cognome = new String (request.getParameter("cognome"));
					String indirizzo = new String (request.getParameter("indirizzo"));
					String email = new String (request.getParameter("email"));
					String carta = new String (request.getParameter("cartadicredito"));
					String cf = new String (request.getParameter("codicefiscale"));
					userid = request.getParameter("user_id");
			
					String Errore = new String("Sono stati riscontrati i seguenti errori nella compilazione del modulo.<br><a href=\"javascript:history.back();\">Torna al Modulo</a><br><br>");
					boolean Error = false;

					Controllo reg = new Controllo();
					// I campi vanno ricontrollati in quanto potrebbero risultare errati dopo la modifica
					if( !reg.ControllaInput(15,nome,"SNome")){	
						Errore="-Il campo nome è errato <br>";
						Error = true;
					}
					if(!reg.ControllaInput(15,cognome,"SNome")){
						Errore+="-Il campo cognome è errato <br>";
						Error = true;
					}
					if(!reg.ControllaInput(32,indirizzo,"NS1toSize")){
						Errore+="-Il campo indirizzo è errato <br>";
						Error = true;
					}
					if(!reg.ControllaInput(14,carta,"N1toSize")){
						Errore+="-Il campo carta di credito è errato <br>";
						Error = true;
					}
					if(!reg.ControllaInput(0,cf,"CF")){
						Errore+="-Il campo codice fiscale è errato <br>";
						Error = true;
					}
					if(!reg.ControllaInput(32,email,"Email")){
						Errore+="-Il campo Email è errato <br>";
						Error = true;
					}
					//ESECUZIONE DELLA QUERY PER CONTROLLARE SE L'EMAIL E' GIA' PRESENTE NEL DATABASE
					String query_mail="SELECT * FROM utente WHERE email='"+email+"' AND userid <>'"+userid+"'"; 
					ResultSet resultset = statement.executeQuery(query_mail);
			
					if (resultset.next())
					{   //E' stato modificato l'indirizzo email ma è stato inserito un altro
						//già presente nel database. Viene comunicato l'errore all'amministratore.
						Errore+="-Indirizzo e-mail già presente. Scegliere un altro indirizzo e-mail.<br>";
						Error = true;
					}
			
					if(Error) //C'è almeno un campo del form che contiene errori
					{
						request.setAttribute("Esito_Modifica_Utente", Errore);
					}
					else // Tutti i campi hanno dato esito positvo ai controlli
					{
						String Successo = new String("Utente modificato con successo.<br><br>");
						//ESECUZIONE DELLA QUERY DI UPDATE
						String query="UPDATE utente SET nome='"+nome+"', cognome='"+cognome+"', codice_fiscale='"+cf+"', email='"+email+"', carta_credito='"+carta+"', indirizzo='"+indirizzo+"' WHERE userid='"+userid+"'"; 
						statement.executeUpdate(query);
						request.setAttribute("Esito_Modifica_Utente",Successo);
					}
					// Passaggio della stringa alla jsp che la visulizzerà a video 
					RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/admin.jsp");
					dispatcher.forward(request, response);	
					// Viene rilsciata la connessione al DataBase	
					CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
					//Redirect ad admin.jsp per mostrare l'esito della modifica dati utente 
					response.sendRedirect("admin.jsp");
				
				}
		
		}	
        catch(Exception e){
        	
        	//Redirect alla pagina errore.jsp nella quale viene mostrato a video un messaggio di errore generico
        	//ed il relativo messaggio di errore catturato dall'eccezione.
        	out.println("<script type=\"text/javascript\">window.location = \"errore.jsp?Errore="+e.getMessage()+"\"</script>");
        	
        }
		
	}

}

