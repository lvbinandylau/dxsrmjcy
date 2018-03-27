package dxsrmjcy;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Image;  
import java.awt.Rectangle;  
import java.awt.geom.AffineTransform;  
import java.awt.image.AffineTransformOp;  
import java.awt.image.BufferedImage;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;  
  
import javax.imageio.ImageIO;  
import javax.imageio.ImageReadParam;  
import javax.imageio.ImageReader;  
import javax.imageio.stream.ImageInputStream;  
/**
 * Servlet implementation class IMGCrop
 */
@WebServlet("/IMGCrop")
public class IMGCrop extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
    public static void cutCenterImage(String src,String dest,int w,int h) throws IOException{   
        Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");   
        ImageReader reader = (ImageReader)iterator.next();   
        InputStream in=new FileInputStream(src);  
        ImageInputStream iis = ImageIO.createImageInputStream(in);   
        reader.setInput(iis, true);   
        ImageReadParam param = reader.getDefaultReadParam();   
        int imageIndex = 0;   
        Rectangle rect = new Rectangle((reader.getWidth(imageIndex)-w)/2, (reader.getHeight(imageIndex)-h)/2, w, h);    
        param.setSourceRegion(rect);   
        BufferedImage bi = reader.read(0,param);     
        ImageIO.write(bi, "jpg", new File(dest));             
   
    }  
   /* 
    * 图片裁剪二分之一 
    */  
    public static void cutHalfImage(String src,String dest) throws IOException{   
        Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");   
        ImageReader reader = (ImageReader)iterator.next();   
        InputStream in=new FileInputStream(src);  
        ImageInputStream iis = ImageIO.createImageInputStream(in);   
        reader.setInput(iis, true);   
        ImageReadParam param = reader.getDefaultReadParam();   
        int imageIndex = 0;   
        int width = reader.getWidth(imageIndex)/2;   
        int height = reader.getHeight(imageIndex)/2;   
        Rectangle rect = new Rectangle(width/2, height/2, width, height);   
        param.setSourceRegion(rect);   
        BufferedImage bi = reader.read(0,param);     
        ImageIO.write(bi, "jpg", new File(dest));     
    }  
   /* 
    * 图片裁剪通用接口 
    */  
 
   public static void cutImage(String src,String dest,int x,int y,int w,int h) throws IOException{   
          Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");   
          ImageReader reader = (ImageReader)iterator.next();   
          InputStream in=new FileInputStream(src);  
          ImageInputStream iis = ImageIO.createImageInputStream(in);   
          reader.setInput(iis, true);   
          ImageReadParam param = reader.getDefaultReadParam();   
          Rectangle rect = new Rectangle(x, y, w,h);    
          param.setSourceRegion(rect);   
          BufferedImage bi = reader.read(0,param);     
          ImageIO.write(bi, "jpg", new File(dest));             
 
   }   
   /* 
    * 图片缩放 
    */  
   public static void zoomImage(String src,String dest,int w,int h) throws Exception {  
       double wr=0,hr=0;  
       File srcFile = new File(src);  
       File destFile = new File(dest);  
       BufferedImage bufImg = ImageIO.read(srcFile);  
       Image Itemp = bufImg.getScaledInstance(w, h, bufImg.SCALE_SMOOTH);  
       wr=w*1.0/bufImg.getWidth();  
       hr=h*1.0 / bufImg.getHeight();  
       AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);  
       Itemp = ato.filter(bufImg, null);  
       try {  
           ImageIO.write((BufferedImage) Itemp,dest.substring(dest.lastIndexOf(".")+1), destFile);  
       } catch (Exception ex) {  
           ex.printStackTrace();  
       }           
   }  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IMGCrop() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		String strsrc = null;
		String strx = null;
		String stry = null;
		String strw = null;
		String strh = null;
		strsrc = request.getParameter("src");
		strx = request.getParameter("x");
		stry = request.getParameter("y");
		strw = request.getParameter("w");
		strh = request.getParameter("h");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
