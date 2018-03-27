package dxsrmjcy;

import java.io.IOException;

import java.util.Calendar;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class accesscheck
 */
@WebFilter("/accesscheck")
public class accesscheck implements Filter {

    /**
     * Default constructor. 
     */
    public accesscheck() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
        System.out.println("destroy LoginFilter");
	}
    Logger logger = LogManager.getLogger(accesscheck.class.getName());  
    String strLog = "";


	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
        //把ServletRequest和ServletResponse转换成真正的类型
        HttpServletRequest req = (HttpServletRequest)request;
        HttpSession session = req.getSession();

        //由于web.xml中设置Filter过滤全部请求，可以排除不需要过滤的url，从reg.jsp进行跳转的也要放行。
        String requestURI = req.getRequestURI();
        if(requestURI.endsWith("login.jsp") || requestURI.endsWith("reg.jsp")|| requestURI.endsWith("valid.jsp"))
        {
            chain.doFilter(request, response);
            return;
        }
        //判断用户是否登录，进行页面的处理
        if(null == session.getAttribute("dxsrmjcy_UID") || session.getAttribute("dxsrmjcy_UID") == ""){
            //未登录用户，重定向到登录页面
        	logger.trace("未登录用户，转到登陆页面");
            ((HttpServletResponse)response).sendRedirect("login.jsp");
            return;
        } else {
            //已登录用户，验证Token
        	//获取Token
        	logger.trace("已登录用户");
        	String strToken = session.getAttribute("Token").toString();
        	strLog = "strToken="+ strToken;
        	byte[] context = InitParam.HexStringTobyte(strToken);
        	String strUID = session.getAttribute("dxsrmjcy_UID").toString();
			
	         Calendar calendar= Calendar.getInstance();  
	         int nday = calendar.get(Calendar.DAY_OF_MONTH);
	         String key = String.valueOf(nday);
	         String iv = "abcdefghijklmnop";
	         byte[] decrypt = InitParam.AES_CBC_Decrypt(context, key, iv);
	         if (decrypt != null)
			 {
	         String strdecrypt = new String(decrypt);
	         strdecrypt = String.copyValueOf(strdecrypt.toCharArray(), 0, decrypt.length);

	         if (strdecrypt.equals(strUID) == true)//如果ID值相等，则登录
	        	 chain.doFilter(request, response);
	         else
	             ((HttpServletResponse)response).sendRedirect("/dxsrmjcy/login.jsp");
			 }
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
