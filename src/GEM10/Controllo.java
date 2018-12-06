package GEM10;

public class Controllo {

	/*
	 * Metodo che riceve in input Size ovvero la grandezza della stringa da controllare,
	 * Field la stringa da controllare, Tipo stringa che indica il tipo di controllo
	 * da effettuare. Ritorna un valore booleano a seconda dell'esito del controllo.
	 * Ogni controllo è effettuato mediante espressioni regolari.
	 * 
	 * */
	
	public boolean ControllaInput (int Size, String Field,String Tipo)
	{
	    boolean Esito=false;
	    
	    if(Tipo.matches("N1toSize"))
	    {
	       //Controlla che il campo contenga numeri di lunghezza variabile da 1 a Size
	       if(Field.matches("[0-9]{1,"+Size+"}"))
	       {
	          Esito=true;
	       }
	    }
	    else if(Tipo.matches("NSize"))
	    {
	       //Controlla che il campo contenga numeri di lunghezza pari a Size
	       if(Field.matches("[0-9]{"+Size+"}"))
	       {
	          Esito=true;
	       }
	    }
	    else if(Tipo.matches("N0toSize"))
	    {
	       //Controlla che il campo contenga numeri di lunghezza variabile da 0 a Size
	       if(Field.matches("[0-9]{0,"+Size+"}"))
	       {
	          Esito=true;
	       }
	    }
	    else if(Tipo.matches("NSSize"))
	    {
	       //Controlla che il campo contenga una stringa alfanumerica di n caratteri
	       if(Field.matches("[a-zA-Z0-9]{"+Size+"}"))
	       {
	          Esito=true;
	       }
	    }
	    else if(Tipo.matches("NS1toSize"))
	    {
	       //Controlla che il campo contenga una stringa alfanumerica con spazi di grandeza da 1 a Size
	       //Deve iniziare per un carattere alfabetico
	       if(Field.matches("^[a-zA-Z][a-zA-Z0-9\\s]{0,"+(Size-1)+"}"))
	       {
	          Esito=true;
	       }
	    }
	    else if(Tipo.matches("Password"))
	    {
	       //Controlla che il campo contenga una stringa alfanumerica di minimo 8 caratteri e massimo Size caratteri
	       if(Field.matches("[a-zA-Z0-9]{8,"+Size+"}"))
	       {
	          Esito=true;
	       }
	    }
	    else if(Tipo.matches("SNome"))
	    {
	       //Controlla che il campo contenga una stringa di lunghezza variabile da 1 a Size
	       //Deve iniziare con un carattere e può contenere spazi
	       if(Field.matches("^[a-zA-Z][a-zA-Z\\s]{0,"+(Size-1)+"}"))
	       {  
	          Esito=true;
	       }
	    }
	    else if(Tipo.matches("S1toSize"))
	    {
	       //Controlla che il campo contenga una stringa di lunghezza variabile da 1 a Size contenente spazi
	       if(Field.matches("[a-zA-Z\\s]{1,"+Size+"}"))
	       {
	          Esito=true;
	       }
	    }
	    else if(Tipo.matches("Email"))
	    {
	       //Controlla che il campo contenga una stringa nel formato indirizzo email
	       if(Field.matches("\\b[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}\\b"))
	       {
	    	   Esito=true;
	       }
	    }
	    else if(Tipo.matches("CF"))
	    {
	       //Controlla che rispecchi un Codice Fiscale
	       if(Field.matches("^[a-zA-Z]{6}[0-9]{2}[abcdehlmprstABCDEHLMPRST]{1}[0-9]{2}([a-zA-Z]{1}[0-9]{3})[a-zA-Z]{1}$"))
	       {
	          Esito=true;
	       }
	    }
	    else if(Tipo.matches("data"))
	    {
	       //Controlla che il campo contenga una data gg/mm/aaaa
	       //ed ulteriori controlli di coerenza sulle date
	       if(Field.matches("(0[1-9]|[12][0-9]|3[01]|)[//](0[1-9]|1[012])[//](2\\d\\d\\d)"))
	       {
	           //Esito se è un mese che non ha il 31esimo giorno
	    	   if ( Field.substring(3, 5).matches("(04)|(06)|(09)|(11)") )
	    	   {   //Il giorno inserito non è 31 quindi il formato è valido
	    		   if ( !Field.substring(0, 2).matches("31") )
	    		     Esito=true;
	    	   }
	    	   else if ( Field.substring(3, 5).matches("02") )//Febbraio
	    	   {   //Il giorno deve essere diverso da 30 e da 31
	    		   if ( !Field.substring(0, 2).matches("(31)|(30)"))
	    		   {   // Il giorno può essere 29 ma necessita del controllo sull'anno che deve essere bisestile
		    		   if (Field.substring(0, 2).matches("29")) 
		    		   {   //Un anno è bisestile se il suo numero è divisibile per 4, 
	    				   if (Integer.parseInt(Field.substring(6, 10)) %4 ==0) 
	    				      //con l'eccezione che gli anni secolari (quelli divisibili per 100) sono bisestili solo se divisibili per 400.
	    				      if (Integer.parseInt(Field.substring(6, 10)) %100 != 0 || Integer.parseInt(Field.substring(6, 10)) %400 ==0)
		    			   		   Esito=true;
		    		   }
		    		   else // Il giorno non è ne 31, ne 30, ne 29 quindi è valido
		    		   {
		    			   Esito=true;
		    		   } 
	    		   }
	    	   }
	    	   else // E' un mese che ha il 31esimo giorno, il formato è valido
	    	   {
	    		   Esito=true;
	    	   }
	       }
	    }
	    else if(Tipo.matches("F3.2"))
	    {
	          //Controlla che il campo contenga un float di 3 cifre prima del punto e due dopo
	          if(Field.matches("^[0-9]{1,3}(\\.[0-9]{1,2})?$"))
	          {
	              Esito=true;
	          }
	    }
	   return Esito;
	 } 
	
	
}
