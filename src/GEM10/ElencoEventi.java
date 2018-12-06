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

/**ElencoEventi.java
 * La Servlet si occupa di gestire le statistiche relative ad un evento, solo
 * l'admin può accedervi
 */
public class ElencoEventi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(true);
		
     try{
            // leggiamo la variabile di sessione "loggato" poichè bisogna verificare
    	    // che sia un utente loggato e che sia un amministratore
			String whologgin = (String) session.getAttribute("loggato");
	     
	        if (whologgin != null && whologgin.matches("admin"))
	        { 
	            //Richiedo una connessione al connection pool
				DB_Connection CP = DB_Connection.getConnectionPool();
	            Connection connection = CP.getConnection();
				
				// Ottengo lo Statement per interagire con il database. 
				Statement statement = connection.createStatement();
				String idevento =	new String (" ");
				
				//Salviamo l'id evento per poi calolarne le statistiche
				idevento = request.getParameter("idevento");
	        	//Query per avere informazioni sull'incasso totale e il numero di biglietti venduti di un evento
				String query3 ="SELECT COALESCE(sum(e.prezzo*p.biglietti),0) as totincasso, COALESCE(sum(p.biglietti),0) as totbiglietti FROM evento e join prenotazione p using(id_evento) WHERE id_evento='"+idevento+"'";
	        	//ESECUZIONE DELLA QUERY 						
		        ResultSet resultset = statement.executeQuery(query3);
		        String Successo = new String(" ");
		        if (resultset.next())
		        {    //Salviamo in una stringa le informazioni statistiche
		        	 Successo = "<br><br>Totale incasso evento: "+resultset.getString("totincasso")+" euro.<br><br>Totale Biglietti Venduti: "+resultset.getString("totbiglietti");
		        }
		        // Passaggio della stringa alla jsp che la visulizzerà a video
		        	request.setAttribute("Prenotazioni_Cancellabili",Successo);
		        	RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/prenotazioni_elenco.jsp");
		        	dispatcher.forward(request, response);	
		        // Viene rilsciata la connessione al DataBase	
				CP.rilasciaConnessione((com.mysql.jdbc.Connection) connection);
				// Redirect alla jsp
		        response.sendRedirect("prenotazioni_elenco.jsp");
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
