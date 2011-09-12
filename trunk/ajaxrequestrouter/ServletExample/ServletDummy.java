/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author marcellolarocca@gmail.com
 */
@WebServlet(name="ServletDummy", urlPatterns={"/ServletDummy"})
public class ServletDummy extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
		Enumeration paramNames = request.getParameterNames();
		HashMap<String, String> parameters = new HashMap<String, String>() ;

                //Gets all the parameters from the URL and puts them in a HashMap
		while(paramNames.hasMoreElements()) {
			String paramName = (String)paramNames.nextElement();

		      String[] paramValues = request.getParameterValues(paramName);
		      if (paramValues.length == 1) {
			        String paramValue = paramValues[0];

			        if (paramValue.length() == 0 || paramValue.equals("''") ){
//DEBUG			        	System.out.println(paramName + " = "+" No Value ");
			        	continue;
			        }
			        else{
			        	parameters.put(paramName, paramValue);
//DEBUG			        	System.out.println(paramName + " = "+paramValue);
			        }
		      } else {

			        for(int i=0; i<paramValues.length; i++) {
			        	parameters.put(paramName, paramValues.toString());
//DEBUG			        	System.out.println(i + " -> " + paramValues[i]);
			        }
		      }
	    }

            //KEY STEP: Gets the value for the callback parameter
            String callback = parameters.get("callback");


            String message;
            
            //HERE DO whatever you need to get your data in a JSON format
            message = getJSON();

            //KEY STEP: the response to the request must be a javascript function call
            response.setContentType("text/javascript");
            response.setHeader("Expires", "-1");

            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");

            //KEY STEP: the response must have the format _callback_function(_json_data)
            message = callback + "(" + message + ");";
            
            response.setContentLength(message.length());
            response.setStatus(200);

            out.println(message );
//DEBUG     System.out.println("Msg = " + message );
        } finally { 
            out.close();
            out.flush();
        }



    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>


    public String getJSON(){
        return "[{'id': 1,'name': 'Foo','price': 123,'tags': ['Bar','Eek']}]";
    }

}
