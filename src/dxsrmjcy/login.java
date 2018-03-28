package dxsrmjcy;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.Logger;


import org.apache.logging.log4j.LogManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
public class login extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public login() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

    Logger logger = LogManager.getLogger(login.class.getName());  
    String strLog = "";


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		logger.trace("start");
		String httpsURL = request.getScheme() + "://" + request.getServerName() +":"+ request.getServerPort() + request.getContextPath() + "/";
		logger.trace(httpsURL);

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		HttpSession session = request.getSession(true);

		PrintWriter out = response.getWriter();
		String uname = request.getParameter("name");
		String passwd = null;
		try {
			passwd = InitParam.getMD5(request.getParameter("pwd"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (passwd != null)
			logger.trace("get password success!");
		ResultSet rs = null;
		Connection conn = null;
		// 用于查询

		try {

			try {
				// 加载MySql的驱动类
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				logger.trace("找不到驱动程序类 ，加载驱动失败！");
				e.printStackTrace();
			}
			logger.trace("mysql load success!");
			// 首先修改这里的url，user，password
			conn = DriverManager.getConnection(InitParam.url);
			logger.trace("mysql conn success!");
			PreparedStatement pstmt = conn.prepareStatement("select * from user where user=? and pwd=?");
			pstmt.setString(1, uname);
			pstmt.setString(2, passwd);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				//session值设置
				//ID算法，如果当天是此月的第X天，key就为X，针对id根据AES算法得出Token;
				logger.trace("login success!");
				int uid = rs.getInt("id");
				String strUID = Integer.toString(uid);
				String strEncrypt = InitParam.GenToken(uid);
				session.setAttribute("dxsrmjcy_UID", strUID);
				session.setAttribute("Token", strEncrypt);
				response.sendRedirect(httpsURL + "index.jsp");
			} else {
				logger.trace("login failed!");
				response.sendRedirect(httpsURL + "login.jsp");
			}
			out.flush();
			out.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
