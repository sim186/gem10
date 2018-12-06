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

/**ElencoPrenotazioni.java
 * La servlet si occupa di mostrare all'utente le statistiche, le prenotazioni
 * ancora cancellabili e le prenotazioni non più cancellabili(in quanto scaduto
 * il termine per la cancellabilità) o eventi passati
 */
public class ElencoPrenotazioni extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		HttpSession session = request.getSession(true);
		
        try{
            
            //Richiedo una connessione al connection pool
			DB_Connection CP = DB_Connection.getConnectionPool();
            Connection connection = CP.getConnection();
			
			// Ottengo lo Statement per interagire con il database. 
			Statement statement = connection.createStatement();
			String userid =	new String (" ");
		
			String whologgin = (String) session.getAttribute("loggato");
	        //La variabile whologgin deve essere diversa da null assicurando la presenza di un utente loggato
	        if (whologgin != null)
	        {   //Se l'utente loggato è un utente
	        	if (whologgin.matches("utente"))
	        		 // Salviamo l'userid dalla variabile di sessione poichè l'utente può vedere le informazioni
	        		 // solo su se stesso.
	        		userid =(String) session.getAttribute("userid");
	        	else // L'utente è un amministratore
	        		 //L'userid dell'utente è passato come parametro nell'url e varia a secondo della link cliccato dall'amministratore
	        		userid = request.getParameter("userid");
	        	
	        	// Query per la visualizzazione delle prenotazioni non più cancellabili
	        	String query2 ="SELECT * FROM evento e left join prenotazione p using(id_evento) WHERE DATEDIFF( STR_TO_DATE(e.data,'%d/%m/%Y'), SYSDATE()) < 7 AND id_user='"+userid+"'";
	        	// Query per la visualizzazione delle prenotazioni cancellabili
	        	String query  ="SELECT * FROM evento e left join prenotazione p using(id_evento) WHERE DATEDIFF( STR_TO_DATE(e.data,'%d/%m/%Y'), SYSDATE()) >= 7 AND id_user='"+userid+"'";
	        	// Query per il calcolo di dati statici dell'utente: totale spesa  e totale biglietti aquistati
	        	String query3  ="SELECT COALESCE(sum(e.prezzo*p.biglietti),0) as totspesa, COALESCE (sum(p.biglietti),0) as totbiglietti FROM evento e join prenotazione p using(id_evento) WHERE id_user='"+userid+"'";
	        	//ESECUZIONE DELLA QUERY 						
		        ResultSet resultset = statement.executeQuery(query3);
		        String Successo = new String(" ");
		        if (resultset.next())
		        {   // Salvo le informazioni statistiche
		            Successo = "<br><br>Totale biglietti acquistati: "+resultset.getString("totbiglietti")+"<br><br>Totale Spesa effettuata: "+resultset.getString("totspesa")+" euro.";
		        }
		         
		        //Inizio della tabella degli eventi cancellabili
		        Successo += "<br><br>LISTA EVENTI PRENOTATI CANCELLABILI:<br>";
		        Successo += "Nota Bene - Gli eventi sono cancellabili quando manca più di una settimana all'evento.<br><br>";
		        Successo += "Se si cancella l'evento viene rimborsato l'importo del/dei biglietto/i sulla carta di credito inserita alla registrazione.<br><br>";
		        Successo += "<table width=\"900\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">";
		        Successo += "<tr><td> NOME";	 
		        Successo += "</td><td>TIPO";
		        Successo += "</td><td>LUOGO";
		        Successo += "</td><td>CITTA";
		        Successo += "</td><td>PREZZO";
		        Successo += "</td><td>NUM BIGLIETTI";
		        Successo += "</td><td>CODICE INGRESSO";
		        Successo += "</td><td>DATA EVENTO";
		        Successo += "</td><td>CANCELLA";
		        Successo += "</td></tr>";
		
		        //ESECUZIONE DELLA QUERY 						
		        resultset = statement.executeQuery(query);
		
		        int i=0; // variabile per colorare alternativamente le righe della tabella
		
		        // VISUALIZZAZIONE DEGLI EVENTI CANCELLABILI 
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
		        	 Successo += resultset.getString("e.nome");
		        	 Successo += "</td><td>";
		        	 Successo += resultset.getString("e.tipo");
		        	 Successo += "</td><td>";
		        	 Successo += resultset.getString("e.luogo");
		        	 Successo += "</td><td>";
		        	 Successo += resultset.getString("e.citta");
		        	 Successo += "</td><td>";
		        	 Successo += resultset.getString("e.prezzo");
		        	 Successo += "</td><td>";
		        	 Successo += resultset.getString("p.biglietti");
		        	 Successo += "</td><td>";
		        	 Successo += resultset.getString("p.codice");
		        	 Successo += "</td><td>";
		        	 Successo += resultset.getString("e.data");
		        	 Successo += "</td><td>";
		        	 Successo += "<a href=\"Prenotazione?id_prenotazione=+"+resultset.getInt("p.id_prenotazione")+ "\" onclick=\"javascript:return window.confirm('Confermi la cancellazione?')\" >CANCELLA PRENOTAZIONE</a>";	
		        	 Successo += "</td></tr>";
		        }	
			
		        Successo += "</table>";	

	            //Inizio della tabella degli eventi NON cancellabili
		        Successo += " <br><br>LISTA EVENTI PRENOTATI NON PIU CANCELLABILI:<br>";
			    Successo += "Nota Bene - Gli eventi visualizzati non sono cancellabili (in quanto eventi passati o ai quali manca meno di una settimana)<br><br>";
			    Successo += " <table width=\"900\" border=\"0\" cellspacing=\"2\" cellpadding=\"0\">";
			    Successo += "<tr><td> NOME";	 
			    Successo += "</td><td>TIPO";
			    Successo += "</td><td>LUOGO";
			    Successo += "</td><td>CITTA";
			    Successo += "</td><td>PREZZO";
			    Successo += "</td><td>NUM BIGLIETTI";
			    Successo += "</td><td>CODICE INGRESSO";
			    Successo += "</td><td>DATA EVENTO";
			    Successo += "</td></tr>";
			
			    //ESECUZIONE DELLA QUERY 						
			    ResultSet resultset2 = statement.executeQuery(query2);
			
		        i=0; // variabile per colorare alternativamente le righe della tabella
			
     	        // VISUALIZZAZIONE DEGLI EVENTI NON CANCELLABILI 
			    while (resultset2.next())
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
			        	 Successo += resultset2.getString("e.nome");
			        	 Successo += "</td><td>";
			        	 Successo += resultset2.getString("e.tipo");
			        	 Successo += "</td><td>";
			        	 Successo += resultset2.getString("e.luogo");
			        	 Successo += "</td><td>";
			        	 Successo += resultset2.getString("e.citta");
			        	 Successo += "</td><td>";
			        	 Successo += resultset2.getString("e.prezzo");
			        	 Successo += "</td><td>";
			        	 Successo += resultset2.getString("p.biglietti");
			        	 Successo += "</td><td>";
			        	 Successo += resultset2.getString("p.codice");
			        	 Successo += "</td><td>";
			        	 Successo += resultset2.getString("e.data");	
			        	 Successo += "</td></tr>";
			    }	
				Successo += "</table>";	
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
