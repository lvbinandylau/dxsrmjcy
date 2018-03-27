package dxsrmjcy;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialNews;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.WxMpMassPreviewMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
/**
 * Servlet implementation class WXServer
 */
@WebServlet("/WXServer")
public class WXServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Logger logger = LogManager.getLogger(WXServer.class.getName());
    protected WxMpService wxService;
    // 鍗曞浘鏂囨秷鎭痬edia_id
    private String singleNewsMediaId = "";
    // 澶氬浘鏂囨秷鎭痬edia_id
    private String multiNewsMediaId = "";
		private String strMediaID = "";
    final private WxMpInMemoryConfigStorage config;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WXServer() {
        super();
        // TODO Auto-generated constructor stub
        logger.trace("start");
				config = new WxMpInMemoryConfigStorage();
        config.setAppId(InitParam.APPID);// 璁剧疆寰俊鍏紬鍙风殑appid
        config.setSecret(InitParam.APPSECRET);// 璁剧疆寰俊鍏紬鍙风殑app corpSecret
        config.setToken(InitParam.TOKEN);// 璁剧疆寰俊鍏紬鍙风殑token
        config.setAesKey(InitParam.AESKEY);// 璁剧疆娑堟伅鍔犺В瀵嗗瘑閽�
        wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(config);
        try {
			String strAccessToken = wxService.getAccessToken();
			logger.trace("AccessToken=["+strAccessToken+"]");
			/*
			//鑾峰彇openid鍒楄〃
			WxMpUserList wxMpUserList = this.wxService.getUserService().userList(null);
			List<String> openidList = wxMpUserList.getOpenids();
			int nlist = openidList.size();
			for(int i = 0; i < nlist; ++i)
			{
				String stropenid = openidList.get(i);
				//鏍规嵁openid鏉ヨ幏鍙栫敤鎴锋樀绉帮紱
				WxMpUser mywxmpuser = this.wxService.getUserService().userInfo(stropenid);
				String strNickname = mywxmpuser.getNickname();
				String strUnionId = mywxmpuser. getUnionId();
				logger.trace(strNickname + "|" + strUnionId);
			}*/
		} catch (WxErrorException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		/*
		try {
        	WxMpMaterialNewsBatchGetResult NBGR =wxService.getMaterialService().materialNewsBatchGet(0, 1);
        	logger.trace(NBGR.getTotalCount()+"---"+NBGR.getItemCount());
        	List<WxMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem> LNBGNI = NBGR.getItems();
        	int nCount = LNBGNI.size();
        	for(int i = 0; i < nCount; ++i)
        	{
        		WxMaterialNewsBatchGetNewsItem NBGNI = LNBGNI.get(i);
        		logger.trace("mediaID=["+NBGNI.getMediaId() + "]");
        		logger.trace("UpdateTime=["+NBGNI.getUpdateTime()+"]");
        		WxMpMaterialNews wxMN = NBGNI.getContent();
        		List<WxMpMaterialNewsArticle> LwxMMNA = wxMN.getArticles();
        		int nCount2 = LwxMMNA.size();
        		for(int j = 0; j < nCount2; ++j)
        		{
        			WxMpMaterialNewsArticle wxMMNA =  LwxMMNA.get(j);
        			logger.trace("Title=[" + wxMMNA.getTitle() + "]");
        			logger.trace("Author=[" + wxMMNA.getAuthor() + "]");
        			//logger.trace("Content=[" + wxMMNA.getContent() + "]");
        			logger.trace("ContentSourceUrl=[" + wxMMNA.getContentSourceUrl() + "]");
        			logger.trace("Digest=[" + wxMMNA.getDigest() + "]");
        			logger.trace("ThumbMediaId=[" + wxMMNA.getThumbMediaId() + "]");
        			logger.trace("ThumbUrl=[" + wxMMNA.getThumbUrl() + "]");
        			logger.trace("Url=[" + wxMMNA.getUrl() + "]");
        		}
        	}
		} catch (WxErrorException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		*/
	}
	/*
	 * Java鏂囦欢鎿嶄綔 鑾峰彇鏂囦欢鎵╁睍鍚�
	 *
	 *  Created on: 2011-8-2
	 *      Author: blueeagle
	 */
	    public static String getExtName(String filename) {
	        if ((filename != null) && (filename.length() > 0)) {
	            int dot = filename.lastIndexOf('.');
	            if ((dot >-1) && (dot < (filename.length() - 1))) {
	                return filename.substring(dot + 1);
	            }
	        }
	        return filename;
	    }
	/*
	 * Java鏂囦欢鎿嶄綔 鑾峰彇涓嶅甫鎵╁睍鍚嶇殑鏂囦欢鍚�
	 *
	 *  Created on: 2011-8-2
	 *      Author: blueeagle
	 */
	    public static String getFileNameNoEx(String filename) {
	        if ((filename != null) && (filename.length() > 0)) {
	            int dot = filename.lastIndexOf('.');
	            if ((dot >-1) && (dot < (filename.length()))) {
	                return filename.substring(0, dot);
	            }
	        }
	        return filename;
	    }

	/*
	 * 鍥剧墖瑁佸壀閫氱敤鎺ュ彛
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

    private String url2local(String url) {
		String urlpre = "www.dxsrmjcy.com.cn";
		int nCount = url.indexOf(urlpre);
		nCount += urlpre.length();
		String localpath = "/home";
		return localpath + url.substring(nCount);
	}
    public void AddSignleNews(String strthumb_media_id, String content, String title, String author) throws WxErrorException, IOException {
    	String strcontent = content;
        // 鍗曞浘鏂囨秷鎭�
        WxMpMaterialNews wxMpMaterialNewsSingle = new WxMpMaterialNews();
        WxMpMaterialNews.WxMpMaterialNewsArticle article = new WxMpMaterialNews.WxMpMaterialNewsArticle();
        /* 此接口为2.7.0的，已经没了，需要是用2.9.0的接口
        article.setAuthor(author);
        article.setThumbMediaId(strthumb_media_id);
        article.setTitle(title);
        article.setContent(strcontent);
        article.setContentSourceUrl("");
        article.setShowCoverPic(false);
        */
        //article.setDigest("single news");//鎽樿
        wxMpMaterialNewsSingle.addArticle(article);
/*
        // 澶氬浘鏂囨秷鎭�
        WxMpMaterialNews wxMpMaterialNewsMultiple = new WxMpMaterialNews();
        WxMpMaterialNews.WxMpMaterialNewsArticle article1 = new WxMpMaterialNews.WxMpMaterialNewsArticle();
        article1.setAuthor("author1");
        article1.setThumbMediaId(this.thumbMediaId);
        article1.setTitle("multi title1");
        article1.setContent("content 1");
        article1.setContentSourceUrl("content url");
        article1.setShowCoverPic(true);
        article1.setDigest("");

        WxMpMaterialNews.WxMpMaterialNewsArticle article2 = new WxMpMaterialNews.WxMpMaterialNewsArticle();
        article2.setAuthor("author2");
        article2.setThumbMediaId(this.thumbMediaId);
        article2.setTitle("multi title2");
        article2.setContent("content 2");
        article2.setContentSourceUrl("content url");
        article2.setShowCoverPic(true);
        article2.setDigest("");
        wxMpMaterialNewsMultiple.addArticle(article1);
        wxMpMaterialNewsMultiple.addArticle(article2);
*/

        WxMpMaterialUploadResult resSingle = this.wxService.getMaterialService().materialNewsUpload(wxMpMaterialNewsSingle);
        /* 此接口为2.7.0的，已经废弃，需要使用2.9.0的接口
        this.singleNewsMediaId = resSingle.getMediaId();
				logger.trace(resSingle.getMediaId());
				*/
/*
        WxMpMaterialUploadResult resMulti = this.wxService.getMaterialService().materialNewsUpload(wxMpMaterialNewsMultiple);
        this.multiNewsMediaId = resMulti.getMediaId();
*/
        }


    public static List<String> getImageSrc(String htmlCode) {
        List<String> imageSrcList = new ArrayList<String>();
        Pattern p = Pattern.compile("[a-zA-z]+://[^\\s]*(jpg|png|gif|bmp)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        String quote = null;
        while (m.find()) {
            quote = m.group();
            imageSrcList.add(quote);
        }
        return imageSrcList;
    }
    //鍒犻櫎list涓殑閲嶅椤�
    public   static   void  removeDuplicate(List<String> list)  {
        HashSet<String> h  =   new  HashSet<String>(list);
        list.clear();
        list.addAll(h);
    }
    //灏嗙綉鍧�杞崲鎴愭湰鍦拌矾寰�
    public String TransLocation(String url)
    {
		String urlpre = "www.dxsrmjcy.com.cn";
		int nCount = url.indexOf(urlpre);
		nCount += urlpre.length();
		//String localpath = "./uploads/image";
		return (getServletContext().getRealPath("/") + url.substring(nCount+1));
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		logger.trace("start");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String strCommand = null;
		String strTitle = null;
		String strAuthor = null;
		String strCoverID = null;
		strCommand = request.getParameter("c");
		strTitle = request.getParameter("t");
		strAuthor = request.getParameter("a");
		String strimgurl = request.getParameter("u");
		int img_x = Integer.parseInt(request.getParameter("x"));
		int img_y = Integer.parseInt(request.getParameter("y"));
		int img_w = Integer.parseInt(request.getParameter("w"));
		int img_h = Integer.parseInt(request.getParameter("h"));
		if (strCommand != null && strCommand != "" && strCommand.trim().length() > 0)
		{
			WxMediaImgUploadResult  wxMIUR;
			List<String> UrlPathList = getImageSrc(strCommand);
			List<String> urlPathFromWXList = new ArrayList<String>();
			removeDuplicate(UrlPathList);//缃戝潃鍘婚櫎閲嶅鐨�
			int nCount = UrlPathList.size();
			//涓嬮潰寮�濮嬪皢鍥剧墖閮戒笂浼犲埌鑵捐鏈嶅姟鍣ㄤ笂
			//瑁佸壀灏侀潰鍥剧墖
			String localpath_coverimg = TransLocation(strimgurl);
			String localpath_coverimg_crop = getFileNameNoEx(localpath_coverimg) + "_crop." + getExtName(localpath_coverimg);
			cutImage(localpath_coverimg, localpath_coverimg_crop, img_x, img_y, img_w, img_h);
			//涓婁紶灏侀潰鍥剧墖鍒扮礌鏉愬簱
			File coverimg = new File(localpath_coverimg_crop);
			WxMpMaterialUploadResult res;
			if (coverimg.exists())
			{
				WxMpMaterial WxMpMaterial_CoverImg = new WxMpMaterial();
				/* 此接口为2.7.0，已经废弃，需要使用2.9.0接口
				WxMpMaterial_CoverImg.setFile(coverimg);
				WxMpMaterial_CoverImg.setName(localpath_coverimg_crop);
				try {
					res = this.wxService.getMaterialService().materialFileUpload(WxConsts.XML_MSG_IMAGE, WxMpMaterial_CoverImg);
					strCoverID = res.getMediaId();
				} catch (WxErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
			}
			//涓婁紶鍥炬枃娑堟伅鍐呯殑鍥剧墖鑾峰彇URL
			//鏈帴鍙ｆ墍涓婁紶鐨勫浘鐗囦笉鍗犵敤鍏紬鍙风殑绱犳潗搴撲腑鍥剧墖鏁伴噺鐨�5000涓殑闄愬埗銆傚浘鐗囦粎鏀寔jpg/png鏍煎紡锛屽ぇ灏忓繀椤诲湪1MB浠ヤ笅銆�
			for(int i = 0; i < nCount; ++i)
			{
				String localurl = UrlPathList.get(i);//瀛樺偍鍦ㄦ垜鐨勬湇鍔″櫒涓婄殑鍦板潃
				logger.trace("url from my server->["+localurl + "]");
				String localpath = TransLocation(localurl);//杞崲涓烘湰鍦板湴鍧�
				File uploadimg = new File(localpath);
				if (uploadimg.exists())
				{
					logger.trace("image exist");					
					/* 此接口为2.7.0，已经废弃，需要使用2.9.0的接口
					try {
						wxMIUR = this.wxService.getMaterialService().mediaImgUpload(uploadimg);
						String urlPathFromWX = wxMIUR.getUrl();
						urlPathFromWXList.add(urlPathFromWX);
						logger.trace("image url returned from WX->["+urlPathFromWX+"]");
						//涓嬮潰寮�濮嬭繘琛宨mg鍦板潃鐨勬浛鎹�
						String strcmdTranslate = strCommand.replace(localurl, urlPathFromWX);
						strCommand = strcmdTranslate;
					} catch (WxErrorException e) {
						// TODO Auto-generated catch block
						logger.trace(e.getMessage());
					}
					*/
				}
				else {
					logger.trace("image not exist");
				}

			}
			//灏嗚浆鎹㈠悗鐨刢ontent鎵撳寘涓婁紶鍒板井淇℃湇鍔″櫒

			try {
				AddSignleNews(strCoverID ,strCommand, strTitle, strAuthor);
			} catch (WxErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//鑾峰彇鍥炬枃娑堟伅鐨刄RL
			WxMpMaterialNews newstmp;
			try {
				newstmp = this.wxService.getMaterialService().materialNewsInfo(this.singleNewsMediaId);
				//logger.trace(newstmp.getArticles().get(0).getUrl());此接口为2.7.0，已经废弃，需要使用2.9.0的接口
			} catch (WxErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//灏嗗浘鏂囨秷鎭彂閫佺粰鐢ㄦ埛棰勮
			WxMpMassPreviewMessage wxmpmpm = new WxMpMassPreviewMessage();
			/* 此接口为2.7.0，已经废弃，需要使用2.9.0的接口
			wxmpmpm.setMediaId(this.singleNewsMediaId);
			wxmpmpm.setMsgType(WxConsts.MASS_MSG_NEWS);
			wxmpmpm.setToWxUserName("lvbinandylau");
			try {
				WxMpMassSendResult wxmpmsr = this.wxService.massMessagePreview(wxmpmpm);
				logger.trace(wxmpmsr.getErrorMsg());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
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
