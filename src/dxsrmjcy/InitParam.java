package dxsrmjcy;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InitParam {
	public static final String url = "jdbc:mysql://localhost:3306/dxsrmjcy?user=dxsrmjcy&password=Jsk@20170628&useUnicode=true&characterEncoding=UTF-8";
	public static final String UPLOAD_PATH = "/home/upload";
	public static final String UPLOAD_DIRECTORY = "upload";
	private static final String DEFAULT_CODE = "utf-8";
	
	public static final String AESKEY = "tTZONBSapV8dl5Gpra83NKvlN1YxUo2aaVyzEPgtiR3";// 消息加解密密钥
	public static final String APPID = "wxe7c24cb218f1eeee";
	public static final String APPSECRET = "a2d674e31fa462ed869adea589574044";
	public static final String TOKEN = "dxsrmjcy";
	public static final String WX_SESSION_USER = "dxsrmjcy";

	/**
	 * 微信消息类型
	 */

	public final static String MSG_TEXT_TYPE = "text";// 文本消息
	public final static String MSG_IMAGE_TYPE = "image";// 图片消息
	public final static String MSG_VOICE_TYPE = "voice";// 语音消息
	public final static String MSG_VIDEO_TYPE = "video";// 视频
	public final static String MSG_SHORTVIDEO_TYPE = "shortvideo";// 小视频为shortvideo
	public final static String MSG_LOCATION_TYPE = "location";// 地理位置
	public final static String MSG_EVENT_TYPE = "event";// 事件
	public final static String GRAPHIC_TYPE = "news";

	/**
	 * 微信端推送过来的Event事件类型
	 */

	public static final String EVT_SUBSCRIBE = "subscribe";
	public static final String EVT_UNSUBSCRIBE = "unsubscribe";
	public static final String EVT_SCAN = "SCAN";
	public static final String EVT_LOCATION = "LOCATION";
	public static final String EVT_CLICK = "CLICK";
	public static final String EVT_VIEW = "VIEW";
	public static final String EVT_MASS_SEND_JOB_FINISH = "MASSSENDJOBFINISH";
	public static final String EVT_SCANCODE_PUSH = "scancode_push";
	public static final String EVT_SCANCODE_WAITMSG = "scancode_waitmsg";
	public static final String EVT_PIC_SYSPHOTO = "pic_sysphoto";
	public static final String EVT_PIC_PHOTO_OR_ALBUM = "pic_photo_or_album";
	public static final String EVT_PIC_WEIXIN = "pic_weixin";
	public static final String EVT_LOCATION_SELECT = "location_select";
	public static final String EVT_TEMPLATESENDJOBFINISH = "TEMPLATESENDJOBFINISH";
	public static final String EVT_ENTER_AGENT = "enter_agent";
	public static final String EVT_CARD_PASS_CHECK = "card_pass_check";
	public static final String EVT_CARD_NOT_PASS_CHECK = "card_not_pass_check";
	public static final String EVT_USER_GET_CARD = "user_get_card";
	public static final String EVT_USER_DEL_CARD = "user_del_card";
	public static final String EVT_USER_CONSUME_CARD = "user_consume_card";
	public static final String EVT_USER_SHAKE = "ShakearoundUserShake";
	public static final String EVT_USER_PAY_FROM_PAY_CELL = "user_pay_from_pay_cell";
	public static final String EVT_USER_VIEW_CARD = "user_view_card";
	public static final String EVT_USER_ENTER_SESSION_FROM_CARD = "user_enter_session_from_card";
	public static final String EVT_CARD_SKU_REMIND = "card_sku_remind"; // 库存报警
	public static final String EVT_KF_CREATE_SESSION = "kf_create_session"; // 客服接入会话
	public static final String EVT_KF_CLOSE_SESSION = "kf_close_session"; // 客服关闭会话
	public static final String EVT_KF_SWITCH_SESSION = "kf_switch_session"; // 客服转接会话
	public static final String EVT_POI_CHECK_NOTIFY = "poi_check_notify"; // 门店审核事件推送
	public static final String EVT_QUALIFICATION_VERIFY_FAIL = "qualification_verify_fail";//// 资质认证失败
	public static final String EVT_NAMING_VERIFY_FAIL = "naming_verify_fail";// 名称认证失败
	public static final String EVT_QUALIFICATION_VERIFY_SUCCESS = "qualification_verify_success";// 资质认证成功
	public static final String EVT_NAMING_VERIFY_SUCCESS = "naming_verify_success";// 名称认证成功
	public static final String EVT_ANNUAL_RENEW = "annual_renew";// 年审通知
	public static final String EVT_VERIFY_EXPIRED = "verify_expired";// 认证过期失效通知
	public static final String BUTTON_VIEW_LIMITED = "view_limited";// 跳转图文消息URL
	/**
	 * 网页授权方式 SCOPE为：snsapi_base方式
	 */
	public static final String SNSAPI_BASE = "snsapi_base";
	/**
	 * 网页授权方式 SCOPE为：snsapi_userinfo方式
	 */
	public static final String SNSAPI_INFO = "snsapi_userinfo";
	/**
	 * 微信接口调用的唯一票据 有效时间 7200秒 access_token是公众号的全局唯一票据，公众号调用各接口时都需使用access_token
	 * 开发者需要进行妥善保存。access_token的存储至少要保留512个字符空间。access_token的有效期目前为2个小时，需定时刷新，
	 * 重复获取将导致上次获取的access_token失效。
	 */
	public static final String GET_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	/**
	 * 微信网页OAuth2.0鉴权
	 */
	public static final String WX_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	/**
	 * 获取微信网页授权所用的AccessToken
	 */
	public static final String WX_AUTHORIZE_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	/**
	 * 根据网页授权的AccessToken以及openId 来获取用户信息
	 */
	public static final String WX_AUTHORIZE_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	/**
	 * 跟根据AccessToken获取 js接口分享的唯一票据 jsapi_ticket
	 */
	public final static String TICKET_GET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	public static String getHttpsURL(HttpServletRequest request) {
		String temp = request.getHeader("x-forwarded-proto") + "://" + request.getServerName()
				+ request.getContextPath() + "/";

		return temp;
	}

	public static String resultSetToJson(ResultSet rs) throws SQLException, JSONException {
		JSONArray array = new JSONArray();

		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();

		while (rs.next()) {
			JSONObject jsonObj = new JSONObject();

			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnLabel(i);
				String value = rs.getString(columnName);
				jsonObj.put(columnName, value);
			}
			array.put(jsonObj);
		}

		return array.toString();
	}

	public static String getMD5(String str) throws Exception {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			throw new Exception("MD5加密出现错误");
		}
	}

	public static SecretKey getKey(String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(key.getBytes(DEFAULT_CODE));
		kgen.init(128, secureRandom);
		return kgen.generateKey();
	}

	public static String byteToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length);
		String sTemp;
		for (int i = 0; i < bytes.length; i++) {
			sTemp = Integer.toHexString(0xFF & bytes[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static byte[] HexStringTobyte(String s) {
		int len = s.length();
		byte[] b = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
			b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return b;
	}

	public static byte[] AES_CBC_Decrypt(byte[] content, String keyBytes, String iv) {
		try {
			Logger logger = LogManager.getLogger(login.class.getName());
			String strLog = "content=" + byteToHexString(content) + "|" + "keyBytes=" + keyBytes + "|" + "iv=" + iv;

			SecretKey key = getKey(keyBytes);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("exception:" + e.toString());
		}
		return null;
	}

	public static byte[] AES_CBC_Encrypt(byte[] content, String keyBytes, String iv) {

		try {
			SecretKey key = InitParam.getKey(keyBytes);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv.getBytes()));
			byte[] result = cipher.doFinal(content);
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("exception:" + e.toString());
		}
		return null;
	}

	public static String GenToken(int uid) {
		String context = Integer.toString(uid);
		Calendar calendar = Calendar.getInstance();
		int nday = calendar.get(Calendar.DAY_OF_MONTH);
		String key = String.valueOf(nday);
		String iv = "abcdefghijklmnop";
		byte[] encrypt = AES_CBC_Encrypt(context.getBytes(), key, iv);
		String strEncrypt = InitParam.byteToHexString(encrypt);
		return strEncrypt;
	}

}
