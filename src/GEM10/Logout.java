package GEM10;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;

/** 
 * La servlet Logout.java consente all'utente loggato di effettuare 
 * il logout cancellando tutte le variabili di sessione create al momento del login.
 */
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	        response.setContentType("text/html;charset=UTF-8");
	        HttpSession session = request.getSession(false);
	        PrintWriter out = response.getWriter();
	        
	        try {   //Se sono state istanziate variabili di sessione esse vengono cancellate
	                if (session!=null)
	                {
	                    session.setAttribute("loggato",null);
	                    session.removeAttribute("loggato");
	                    // Redirect alla jsp
	                    response.sendRedirect("index.jsp");
	                }
	        }
	        catch(Exception e){   
	        	//Redirect alla pagina errore.jsp nella quale viene mostrato a video un messaggio di errore generico
	        	//ed il relativo messaggio di errore catturato dall'eccezione.
	        	out.println("<script type=\"text/javascript\">window.location = \"errore.jsp?Errore="+e.getMessage()+"\"</script>");
	        }
	    } 

	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	        processRequest(request, response);
	    } 

	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	        processRequest(request, response);
	    }

	}