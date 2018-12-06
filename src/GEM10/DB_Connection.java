package GEM10;

import java.sql.DriverManager;
import java.util.LinkedList;
import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class DB_Connection
 */

public class DB_Connection {
	   
	   // La variabile che gestisce l'unica istanza della classe
	   private static DB_Connection cp=null;

	   private LinkedList<Connection> connLibere;
	   private String driver = "com.mysql.jdbc.Driver";
	   private String url = "jdbc:mysql://ip/GEM10";
	   private String user= "user";
	   private String psw="pass";

	   //Costruttore Privato per il singleton
	   private DB_Connection() throws  Exception
	   {
	         //Carichiamo il driver
	         Class.forName(driver);

	         //Creiamo la lista delle connessioni libere
	         connLibere = new LinkedList<Connection>();
	   }


	   public static synchronized DB_Connection getConnectionPool () throws Exception
	   { // Garantisce l'unicità dell'instanza per la classe
	     if (cp==null) 
	         return new DB_Connection();
	     else
	    	 return cp;
	   }


	  public synchronized Connection getConnection() throws Exception
	  {     // Il metodo getConnection restituisce una connessione libera prelevandola 
		    // dalla lista

	      Connection con;

	      if (connLibere.isEmpty()) //Lista vuota, creiamo una nuova connessione
	      {   
	    	  con =  (Connection) DriverManager.getConnection(url,user,psw);
	      }
	      else 
	      {
	            con=connLibere.removeFirst(); //Preleva la connessione
	            if (con.isClosed()) // Verifica se la connessione non è più valida
	            {
	                con = getConnection();// Richiama getConnection
	            }
	      }
	      return con; // Ritorna la connessione
	  }
	    
	   // rilascia una connessione inserendola nella lista delle connessioni libere
	   public synchronized void rilasciaConnessione(Connection con) {

	       connLibere.addLast(con);
	  }

}
