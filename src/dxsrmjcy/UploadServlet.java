package dxsrmjcy;

import java.io.File;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class UploadServlet
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger logger = LogManager.getLogger(UploadServlet.class.getName());
	String strLog = "";

	private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
	private static final int MAX_FILE_SIZE      = 1024 * 1024 * 1024; // 100M
	private static final int MAX_ZONE_SIZE      = 1024 * 1024 * 1024; // 1024M
	private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 1024; // 101M

	//fsutil file createnew null.txt 1038090240 创建指定大小文件命令
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (!ServletFileUpload.isMultipartContent(request)) {
			PrintWriter writer = response.getWriter();
			writer.println("Error: Normal enctype=multipart/form-data");
			writer.flush();
			return;
		}
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(MAX_FILE_SIZE);
		upload.setSizeMax(MAX_REQUEST_SIZE);
		upload.setHeaderEncoding("UTF-8");

		//直接上传到指定绝对路径
		Connection conn = null;
		HttpSession session = request.getSession(true);
		String strUID = session.getAttribute("dxsrmjcy_UID").toString();
		int uid = Integer.parseInt(strUID);
		long TotalSize = 0;
		PrintWriter out=response.getWriter();
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		//首先获取当前用户的空间大小
		try {
			String strSelect = "select sum(size) from fileinfo where uid=?";
			conn = DriverManager.getConnection(InitParam.url);
			pstmt = conn.prepareStatement(strSelect);
			pstmt.setInt(1, uid);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				//判断当前用户是否超出大小
				TotalSize = rs.getLong(1);
				if (TotalSize > MAX_ZONE_SIZE)
				{
					logger.trace("空间已满，请删除一些文件后再进行上传");
					return;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 查询需要发送的人的ID
		//实际上传目录为/usr/local/upload/{year}-{month},方便管理。
		//获取当前服务器年份-月份。
		java.util.Date date= new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int nYear = cal.get(Calendar.YEAR);
		int nMonth = cal.get(Calendar.MONTH) + 1;
		String strDate = nYear + "-" + nMonth;
		String filePath = InitParam.UPLOAD_PATH + File.separator + strDate;
		//如果目录不存在，就新建。
		File uploadDir = new File(filePath);
		if (!uploadDir.exists()) {
			if (uploadDir.mkdir() == true)
				logger.trace("Create floder success!");
		}

		try {
			List<FileItem> formItems = upload.parseRequest(request);
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(InitParam.url);
			String strFileExist = "";
			if (formItems != null && formItems.size() > 0) {
				for (FileItem item : formItems) {
					if (!item.isFormField()) {
						//获取当前系统时间，并作为文件名的前缀
						Date dt = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
						String dateNowStr = sdf.format(dt);
						//获取文件大小
						long nSize = item.getSize();
						//判断总文件大小
						TotalSize += nSize;
						if (TotalSize > MAX_ZONE_SIZE)
						{
							logger.trace("文件过大，已经超出空间容量，请删除一些文件后再进行上传");
							break;							
						}
						//判断当前是否有相同文件名的文件
						String OrifileName = new File(item.getName()).getName();//获取原始文件名
						String fileName = dateNowStr + "_" + uid;
						//获取原始文件名后缀
						String suffix = OrifileName.substring(OrifileName.lastIndexOf(".") + 1);
						fileName += "."  + suffix;
						String fullpath = filePath + File.separator +  fileName;
						File storeFile = new File(fullpath);
						logger.trace("file="+fullpath);
						if (storeFile.exists() == true)//如果文件存在，则不储存此文件，判断下一文件
						{
							strFileExist = fileName;
							logger.warn(fullpath+" exist!");
							continue;
						}
						item.write(storeFile);
						logger.trace(storeFile+"上传成功");

						pstmt = conn.prepareStatement("INSERT INTO fileinfo(uid, path, orifilename,filename, uploaddate, size) VALUE(?,?,?,?,?,?)");
						pstmt.setInt(1, uid);
						String strVirtualPath = "upload" + File.separator + strDate + File.separator + fileName;
						logger.trace(strVirtualPath);
						pstmt.setString(2, strVirtualPath);
						pstmt.setString(3, OrifileName);
						pstmt.setString(4, fileName);
						Timestamp d = new Timestamp(System.currentTimeMillis());
						pstmt.setTimestamp(5, d);
						pstmt.setLong(6, nSize);
						int nRet = pstmt.executeUpdate();
						if (nRet == 1)
							logger.trace(fileName+ "写入数据库成功");
						out.print(strVirtualPath);
					}
				}
			}
			if (strFileExist.length() > 0)
				out.write("["+strFileExist+"]" + " 已存在! 重命名文件后再上传");
			out.close();
		} catch (Exception ex) {
			logger.trace(ex.getMessage());
			request.setAttribute("message","写入数据库失败:"  + ex.getMessage());
		}finally{
            try {  
                conn.close();    
            } catch (SQLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } finally {  
                conn = null;  
            }  
            try {  
                rs.close();  
  
            } catch (SQLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } finally {  
                rs = null;  
            }  
  
            try {  
            	pstmt.close();
            } catch (SQLException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } finally {  
            	pstmt = null;  
            }  
		}
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
