package dxsrmjcy;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class SqlCommand
 */
public class SqlCommand extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger logger = LogManager.getLogger(SqlCommand.class.getName());
	String strLog = "";

	public String resultSetToJson2(ResultSet rs) throws SQLException, JSONException {
		JSONArray array = new JSONArray();

		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		{
			logger.trace("resultSetToJson2 start!!!!");
			JSONObject jsonObj = new JSONObject();

			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				logger.trace(columnName + " " + value);
				jsonObj.put(columnName, value);
			}
			array.put(jsonObj);
		}

		return array.toString();
	}
	
	public ResultSet SqlSelect()
	{
		ResultSet rs = null;
		return rs;
	}

	public String getMD5(String str) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			throw new Exception("MD5���ܳ��ִ���");
		}
	}

	public boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("删除文件失败：" + fileName + "文件不存在");
			return false;
		} else {
			if (file.isFile()) {

				return deleteFile(fileName);
			} else {
				return deleteDirectory(fileName);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true,否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			System.out.println("删除单个文件" + fileName + "成功！");
			return true;
		} else {
			System.out.println("删除单个文件" + fileName + "失败！");
			return false;
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param dir
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true,否则返回false
	 */
	public boolean deleteDirectory(String dir) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			System.out.println("删除目录失败" + dir + "目录不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			System.out.println("删除目录失败");
			return false;
		}

		// 删除当前目录
		if (dirFile.delete()) {
			System.out.println("删除目录" + dir + "成功！");
			return true;
		} else {
			System.out.println("删除目录" + dir + "失败！");
			return false;
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SqlCommand() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		String strCommand = null;
		strCommand = request.getParameter("command");
		if (strCommand != null && strCommand != "" && strCommand.trim().length() > 0)
			logger.trace("Command="+strCommand);
			if (strCommand.equals("0")) {
				String strid = request.getParameter("id");
				String[] strArray = strid.split(",");
				int nCount = strArray.length;
				logger.trace("Total "+nCount + " record(s) need delete!");
				for(int i = 0; i < nCount; ++i)
				{
					int intid = Integer.parseInt(strArray[i]);

					logger.trace("Now start delete ["+intid +"] record");
					try {
						Class.forName("com.mysql.jdbc.Driver");
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					try {
						conn = DriverManager.getConnection(InitParam.url);
						//首先查询出来filename，以备后面查询共享文件						
						pstmt = conn.prepareStatement("select path from fileinfo where id=?");
						pstmt.setInt(1, intid);
						rs = pstmt.executeQuery();
						String strPath = "";
						if(rs.next())
						{
							strPath = rs.getString(1);
						}
						pstmt = conn.prepareStatement("DELETE FROM fileinfo WHERE id=?");
						pstmt.setInt(1, intid);
						int nRet = pstmt.executeUpdate();
						if (nRet == 1)
							logger.trace("id=" + intid + "数据库记录删除成功");
						// 继续搜索数据库，看是否还有同名的文件，如果没有同名的文件，说明此文件没有被共享过，可以删除。
						pstmt = conn.prepareStatement("SELECT * FROM fileinfo WHERE filename=?");
				    	int nIndex = strPath.lastIndexOf("/");
				    	String strPath2 = strPath.substring(nIndex+1);

						pstmt.setString(1, strPath2);
						rs = pstmt.executeQuery();
						if (rs.next())// 说明当前还存在此文件的共享，不能删除
						{
							logger.trace(strPath2 + "已经被共享，不能删除");
						} else {
							// 删除对应的文件
							int nLength = InitParam.UPLOAD_PATH.length();
							String strfilepath = InitParam.UPLOAD_PATH.substring(0, nLength - 7) + File.separator + strPath;
							boolean bOK = delete(strfilepath);
							if (bOK == true)
								logger.trace(strfilepath + "在硬盘删除成功！");
							else
								logger.trace(strfilepath + "删除失败！");
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}finally
					{
			            try {  
			                conn.close();    
			            } catch (SQLException e) {  
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
			} else if (strCommand.equals("1"))// 注册
			{
				logger.trace("Start SignUp!");
				PrintWriter out = response.getWriter();
				String uname = request.getParameter("uname");
				logger.trace("user name=[" + uname + "]");
				String passwd = null;
				try {
					passwd = getMD5(request.getParameter("pwd"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				try {

					rs = null;// 这种自动释放资源的定义一定要放在try块中定义，否则会报资源泄漏的warning。
					Class.forName("com.mysql.jdbc.Driver");
					conn = DriverManager.getConnection(InitParam.url);
					pstmt = conn.prepareStatement("select * from user where user=?");
					pstmt.setString(1, uname);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						out.println(0);// 存在此用户
						logger.trace("name=[" + uname + "] exist!");
					} else {
						logger.trace("insert name=[" + uname + "] into database!");
						pstmt = conn.prepareStatement("INSERT INTO user(user,pwd,email,regdate)VALUES(?,?,?,?)");
						pstmt.setString(1, uname);
						pstmt.setString(2, passwd);
						pstmt.setString(3, "");
						Timestamp d = new Timestamp(System.currentTimeMillis());

						pstmt.setTimestamp(4, d);
						int nRet = pstmt.executeUpdate();
						if (nRet == 1) {
							logger.trace("insert name=[" + uname + "] into database success!");
							out.println(1);
						}
						out.flush();
						out.close();
					}

				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}finally
				{
		            try {  
		                conn.close();    
		            } catch (SQLException e) {  
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
			} else if (strCommand.equals("2"))// 更新commit信息
			{
				String strid = request.getParameter("id");
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				try {
					conn = DriverManager.getConnection(InitParam.url);
					pstmt = conn.prepareStatement("UPDATE fileinfo SET commit=? WHERE id=?;");
					String strCommit = request.getParameter("commit");
					pstmt.setString(1, strCommit);
					int intid = Integer.parseInt(strid);
					pstmt.setInt(2, intid);
					int nRet = pstmt.executeUpdate();
					if (nRet == 1)
						System.out.println("id=" + intid + "更新成功");

				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (strCommand.equals("3"))// 获取所有用户姓名，除了自己
			{
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				try {
					String strUID = request.getParameter("uid");
					int nUID = Integer.parseInt(strUID);
					conn = DriverManager.getConnection(InitParam.url);
					PreparedStatement pstmt3 = conn.prepareStatement("select user from user where id <> ?");
					pstmt3.setInt(1, nUID);
					rs = pstmt3.executeQuery();
					{
						String jsonString = InitParam.resultSetToJson(rs);
						logger.trace("json=[" + jsonString + "]");
						PrintWriter myout = response.getWriter();
						myout.print(jsonString);
						myout.flush();
						myout.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}finally
					{
			            try {  
			                conn.close();    
			            } catch (SQLException e) {  
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

			} else if (strCommand.equals("4"))// 文件共享
			{
				logger.trace("开始执行文件共享");
				String strFrom = request.getParameter("from");// 共享人
				String strTo = request.getParameter("to");// 被共享人
				String strFileIDs = request.getParameter("fileIDs");// 所有共享的文件ID
				String strLog = String.format("from [%s] to [%s]. file ID  [%s].", strFrom, strTo, strFileIDs);
				logger.trace(strLog);
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				try {
					conn = DriverManager.getConnection(InitParam.url);

					// 查询需要发送的人的ID
					String strUser = request.getParameter("to");
					logger.trace("需要复制到[" + strUser + "]");
					String strSelect = "select id from user where user=?";
					PreparedStatement pstmt5 = conn.prepareStatement(strSelect);
					pstmt5.setString(1, strUser);
					ResultSet rsUser = pstmt5.executeQuery();
					int uid = -1;
					if (rsUser.next()) {
						uid = rsUser.getInt(1);
					}

					// 解析fileID
					String[] strFileIDArray = strFileIDs.split(",");
					int nFileID = -1;
					int nCount = strFileIDArray.length;
					// 构造查询语句，查询需要发送的文件
					strSelect = "SELECT uid, path, orifilename, filename, uploaddate, size FROM fileinfo WHERE";
					for (int i = 0; i < nCount; ++i) {
						strSelect += " id=? OR";
					}
					strSelect = strSelect.substring(0, strSelect.length() - 3);
					logger.trace(strSelect);
					PreparedStatement pstmt4 = conn.prepareStatement(strSelect);
					for (int i = 0; i < nCount; ++i) {
						nFileID = Integer.parseInt(strFileIDArray[i]);
						pstmt4.setInt(i + 1, nFileID);
					}
					logger.trace(pstmt4.toString());
					rs = pstmt4.executeQuery();

					String strInsert = "INSERT INTO fileinfo(uid, path, orifilename, filename, uploaddate, size) VALUES";
					for (int i = 0; i < nCount; ++i) {
						strInsert += "(?,?,?,?,?,?),";
					}
					strInsert = strInsert.substring(0, strInsert.length() - 1);
					logger.trace(strInsert);
					PreparedStatement pstmt6 = conn.prepareStatement(strInsert);
					int nflag = 0;
					while (rs.next()) {
						String path = rs.getString(2);
						String orifilename = rs.getString(3);
						String filename = rs.getString(4);
						Timestamp tt = rs.getTimestamp(5);
						Long lSize = rs.getLong(6);
						pstmt6.setInt(++nflag, uid);
						pstmt6.setString(++nflag, path);
						pstmt6.setString(++nflag, orifilename);
						pstmt6.setString(++nflag, filename);
						pstmt6.setTimestamp(++nflag, tt);
						pstmt6.setLong(++nflag, lSize);
					}
					int nRet = pstmt6.executeUpdate();
					if (nRet > 0) {
						logger.trace(pstmt6.toString() + " 执行成功");
						PrintWriter myout = response.getWriter();
						myout.print(1);
						myout.flush();
						myout.close();
					}
				} catch (SQLException e) {
					logger.trace(e);
				}finally
					{
			            try {  
			                conn.close();    
			            } catch (SQLException e) {  
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
			} else if (strCommand.equals("5"))// 获取用户基本信息
			{
				logger.trace("开始获取用户基本信息！");
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					logger.trace(e.getMessage());
				}
				try {
					String strUID = request.getParameter("uid");
					int nUID = Integer.parseInt(strUID);
					conn = DriverManager.getConnection(InitParam.url);
					pstmt = conn.prepareStatement(
							"SELECT u.id, u.user, u.regdate, COUNT(f.id) as fileCount, SUM(f.size) as totalSize FROM (SELECT id, user, regdate FROM user WHERE id=?) AS u JOIN fileinfo AS f ON u.id = f.uid;");
					pstmt.setInt(1, nUID);
					logger.trace(pstmt);
					rs = pstmt.executeQuery();
					if (rs.next()) {
						String jsonString = resultSetToJson2(rs);
						PrintWriter myout = response.getWriter();
						myout.print(jsonString);
						myout.flush();
						myout.close();
					}
				} catch (SQLException e) {
					logger.trace(e.getMessage());
				} catch (JSONException e) {
					logger.trace(e.getMessage());
				}finally
				{
		            try {  
		                conn.close();    
		            } catch (SQLException e) {  
						logger.trace(e.getMessage());
		            } finally {  
		                conn = null;  
		            }  
		            try {  
		                rs.close();  
		  
		            } catch (SQLException e) {  
		                // TODO Auto-generated catch block  
						logger.trace(e.getMessage());
		            } finally {  
		                rs = null;  
		            }  
		  
		            try {  
		            	pstmt.close();
		            } catch (SQLException e) {  
		                // TODO Auto-generated catch block  
						logger.trace(e.getMessage());
		            } finally {  
		            	pstmt = null;  
		            }  					
			}
			}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
