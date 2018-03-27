package dxsrmjcy;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;


public class helloWorld extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    Logger logger = LogManager.getLogger(helloWorld.class.getName());  

	private static String jsonString = "";

	/**
		 * Constructor of the object.
		 */
	public helloWorld() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
		 * The doGet method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to get.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.trace("start!");
		request.setCharacterEncoding("utf-8"); 
		response.setContentType("application/json;;charset=utf-8");
		
		
		PrintWriter myout = response.getWriter();

		Connection conn = null;

		Statement stmtQuery = null;

		Statement stmtUpdate = null;
		ResultSet rs = null;

		try{
		    Class.forName("com.mysql.jdbc.Driver");

		    conn = DriverManager.getConnection(InitParam.url);
		    stmtQuery = conn.createStatement();
		    stmtUpdate = conn.createStatement();
			String uid = request.getParameter("UID");
			if (uid != null && uid.trim().length() > 0)
			{
				int intUID = Integer.parseInt(uid);
				String strSQL = "select * from fileinfo where uid="+intUID;
			    rs = stmtQuery.executeQuery(strSQL);
			    jsonString = InitParam.resultSetToJson(rs);
			    myout.print(jsonString);
				myout.flush();
				myout.close();
			}
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JSONException e){
			e.printStackTrace();
		}
		finally{
		    try{
		        if(stmtQuery != null) 
		            stmtQuery.close();
		    }catch(Exception e){ }

		    try{
		        if(stmtUpdate != null) 
		            stmtUpdate.close();
		    }catch(Exception e){ }

		}
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		Enumeration<String> en = request.getParameterNames();

        while (en.hasMoreElements()) {

            String paramName = (String) en.nextElement();
            out.println(paramName + " = " + request.getParameter(paramName) + "<br/>");

        }		
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the POST method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {

	}
	

}
