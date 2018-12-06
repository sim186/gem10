package GEM10;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

import java.sql.*;

/**Registrazione.java
 * La Servlet si occupa di gestire la registrazione inserendo nel database 
 * l'utente con i relativi dati inseriti
 */
public class Registrazione extends HttpServlet {
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
		
		
	try{
            
            //Richiedo una connessione al connection pool
			DB_Connection CP = DB_Connection.getConnectionPool();
            Connection connection = CP.getConnection();
			
			// Ottengo lo Statement per interagire con il database. 
			Statement statement = connection.createStatement();	
	        
			//Salviamo i dati del form
			String nome = new String (request.getParameter("nome"));
			String cognome = new String (request.getParameter("cognome"));
			String indirizzo = new String (request.getParameter("indirizzo"));
			String email = new String (request.getParameter("email"));
			String carta = new String (request.getParameter("cartadicredito"));
			String cf = new String (request.getParameter("codicefiscale"));
			String password = new String (request.getParameter("password"));
			
			String Errore = new String("");
			boolean Error = false;
			
			Controllo reg = new Controllo();
			//Controlliamo i campi del form
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
			if(!reg.ControllaInput(16,password,"Password")){
				Errore+="-Il campo password è errato <br>";
				Error = true;
			}
			
			//Query richiederà al database una riga contente l'email inserita dall'utente
			String query_mail="SELECT * FROM utente WHERE email='"+email+"'"; 
			//Esecuzione della query
			ResultSet resultset = statement.executeQuery(query_mail);
			
			if (resultset.next())
			{   //E' già presente nel database una riga con l'indirizzo email scelto dall'utente
				//Messaggio da visualizzare come esito
				Errore+="-Indirizzo e-mail già presente. Scegliere un altro indirizzo e-mail.<br>";
				Error = true;
			}

			if(Error) //Almeno un campo è errato
			{   
			    request.setAttribute("Esito", Errore);
			    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/esito_registrazione.jsp");
			    dispatcher.forward(request, response);
			}
			else // Tutti i campi sono corretti
			{
				//Query richiederà l'inserimento di un nuovo utente nel database
				String query="INSERT INTO utente (tipo, nome, cognome, codice_fiscale, email, password, indirizzo, carta_credito) VALUES ('utente', '"+nome+"', '"+cognome+"', '"+cf+"', '"+email+"', '"+password+"', '"+indirizzo+"', '"+carta+"')"; 
				//Esecuzione della query
				statement.executeUpdate(query);
				//Messaggio da visualizzare come esito
				String Successo = new String("Registrazione Avvenuta con successo");
				// Passaggio della stringa alla jsp che la visulizzerà a video 
				request.setAttribute("Esito",Successo);
				RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/esito_registrazione.jsp");
			    dispatcher.forward(request, response);
		    }
			// Viene rilsciata la connessione al DataBase 
		    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
		    // Redirect alla jsp
			response.sendRedirect("esito_registrazione.jsp");	
        }
        catch(Exception e){
        	//Redirect alla pagina errore.jsp nella quale viene mostrato a video un messaggio di errore generico
        	//ed il relativo messaggio di errore catturato dall'eccezione.
        	out.println("<script type=\"text/javascript\">window.location = \"errore.jsp?Errore="+e.getMessage()+"\"</script>");
        	
        }
	}
}

