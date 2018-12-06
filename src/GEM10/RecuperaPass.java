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

/**RecuperaPass.java
 * La Servlet si occupa del recupero della password, controllando se l'email
 * è realmente presente nel database
 */
public class RecuperaPass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       


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
	        
			//Salviamo il contenuto del campo del form
			String email = new String (request.getParameter("email"));
			Controllo reg = new Controllo();
			
			//Verifichiamo che il campo sia corretto
			if(!reg.ControllaInput(32,email,"Email"))
			{    //Campo non corretto
		         request.setAttribute("esito_recupera","<br>- Il campo Email è errato <br>");
			}
			else //Campo corretto
			{
				//Query richiederà al database una riga dove presente l'indirizzo email del form
				String query="SELECT * FROM utente WHERE email='"+email+"'"; 
				
				//Eseguiamo la query
				ResultSet resultset = statement.executeQuery(query);
				
				if (resultset.next())//L'email è presente nel database
				{	
					String Password = resultset.getString("password");
					//A QUESTO PUNTO IL SISTEMA DEVE INVIARE UNA EMAIL ALL'UTENTE
					//CONTENENTE LA PASSWORD DIMENTICATA.
					//QUESTA FUNZIONALITA' E' DA IMPLEMENTARE
					
					request.setAttribute("esito_recupera","<br>- La password è stata recuperata con successo ed è stata inviata per e-mail all'indirizzo: "+email+".<br>");
				}
				else //La query non ha dato nessun risultato
				{ //L'indirizzo e-mail non è presente nel database e quindi non è possibile
				  //effettuare il recupero password
					request.setAttribute("esito_recupera","<br>- E-mail non presente nell'archivio utenti.<br>");
				}
			}
			// Passaggio della stringa alla jsp che la visulizzerà a video
	        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/recupera_pass.jsp");
	        dispatcher.forward(request, response);	
	        // Viene rilsciata la connessione al DataBase
	        CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
	        // Redirect alla jsp
	        response.sendRedirect("recupera_pass.jsp");
        }
        catch(Exception e){
        	
        	//Redirect alla pagina errore.jsp nella quale viene mostrato a video un messaggio di errore generico
        	//ed il relativo messaggio di errore catturato dall'eccezione.
        	out.println("<script type=\"text/javascript\">window.location = \"errore.jsp?Errore="+e.getMessage()+"\"</script>");
        	
        }
	}
}
