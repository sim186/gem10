package GEM10;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Con questa servlet l'utente effettua il login, se è un utente semplice ritorna in index.jsp, altrimenti
 * se è un amministratore verrà indirizzato nella pagina admin.jsp
 */
public class Login extends HttpServlet {
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
		
		try{
            //Richiedo una connessione al connection pool
			DB_Connection CP = DB_Connection.getConnectionPool();
            Connection connection = CP.getConnection();
			
			// Ottengo lo Statement per interagire con il database. 
			Statement statement = connection.createStatement();
			
			//Salviamo il contenuto del form di login
			String email = new String (request.getParameter("email"));
			String password = new String (request.getParameter("password"));
	
			String Errore = new String("");
			boolean Error = false;
			
			Controllo Register = new Controllo();
				    
			if( !Register.ControllaInput(32,email,"Email")){
				Errore="-Il campo mail è errato o vuoto <br>";
				Error = true;
			}
			if(!Register.ControllaInput(16,password,"Password")){
				Errore+="-Il campo password è errato o vuoto<br>";
				Error = true;
			}
            
			if(!Error)//I campi rispettano i formati
			{
				//Query per controllare la validità del login
				String query="SELECT * FROM utente WHERE email='"+email+"' AND password='"+password+"'"; 
				
				//ESECUZIONE DELLA QUERY
				ResultSet resultset = statement.executeQuery(query);
				
				if (resultset.next())//La query ha dato un risultato quindi il login è corretto
				{	//Salviamo l'userid come variabile di sessione
					session.setAttribute("userid", resultset.getString("userid"));
					String TipoOk = new String("");//Stringa per salvare il tipo di utente (adimin o utente)
					TipoOk = resultset.getString("tipo");//Salviamo il risultato

					if (TipoOk.matches("utente"))
					{   //Se l'utente loggato è di tipo utente salviamo la relativa variabile di sessione
						session.setAttribute("loggato", "utente");
					}
					else if (TipoOk.matches("admin"))
					{   //Se l'utente loggato è di tipo amministratore salviamo la relativa variabile di sessione
						session.setAttribute("loggato", "admin");
						response.sendRedirect("admin.jsp");//Redirect alla pagina principale dell'amministratore
					}
				}
			   	else
				{	//La query non ha dato risultati quindi email o password sono errate
			   		Errore+= "email non presente nel database o password errata.<br><br>";
				}
		    }
			// Passaggio della stringa alla jsp che la visulizzerà a video 
				request.setAttribute("Esito",Errore);
				RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/index.jsp");
				dispatcher.forward(request, response);
			// Viene rilsciata la connessione al DataBase	
		    CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
		    // Redirect alla jsp
		    response.sendRedirect("index.jsp");
        }
        catch(Exception e)
        {
        	//Redirect alla pagina errore.jsp nella quale viene mostrato a video un messaggio di errore generico
        	//ed il relativo messaggio di errore catturato dall'eccezione.
        	out.println("<script type=\"text/javascript\">window.location = \"errore.jsp?Errore="+e.getMessage()+"\"</script>");
        }
	}
}

