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
	public static final String url = "jdbc:mysql://192.168.128.128:3306/dxsrmjcy?user=dxsrmjcy&password=Jsk@20170628&useUnicode=true&characterEncoding=UTF-8";
	public static final String UPLOAD_PATH = "/home/upload";
	public static final String UPLOAD_DIRECTORY = "upload";
	private static final String DEFAULT_CODE = "utf-8";
	
	public static final String AESKEY = "tTZONBSapV8dl5Gpra83NKvlN1YxUo2aaVyzEPgtiR3";// 娑堟伅鍔犺В瀵嗗瘑閽�
	public static final String APPID = "wxe7c24cb218f1eeee";
	public static final String APPSECRET = "a2d674e31fa462ed869adea589574044";
	public static final String TOKEN = "dxsrmjcy";
	public static final String WX_SESSION_USER = "dxsrmjcy";

	/**
	 * 寰俊娑堟伅绫诲瀷
	 */

	public final static String MSG_TEXT_TYPE = "text";// 鏂囨湰娑堟伅
	public final static String MSG_IMAGE_TYPE = "image";// 鍥剧墖娑堟伅
	public final static String MSG_VOICE_TYPE = "voice";// 璇煶娑堟伅
	public final static String MSG_VIDEO_TYPE = "video";// 瑙嗛
	public final static String MSG_SHORTVIDEO_TYPE = "shortvideo";// 灏忚棰戜负shortvideo
	public final static String MSG_LOCATION_TYPE = "location";// 鍦扮悊浣嶇疆
	public final static String MSG_EVENT_TYPE = "event";// 浜嬩欢
	public final static String GRAPHIC_TYPE = "news";

	/**
	 * 寰俊绔帹閫佽繃鏉ョ殑Event浜嬩欢绫诲瀷
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
	public static final String EVT_CARD_SKU_REMIND = "card_sku_remind"; // 搴撳瓨鎶ヨ
	public static final String EVT_KF_CREATE_SESSION = "kf_create_session"; // 瀹㈡湇鎺ュ叆浼氳瘽
	public static final String EVT_KF_CLOSE_SESSION = "kf_close_session"; // 瀹㈡湇鍏抽棴浼氳瘽
	public static final String EVT_KF_SWITCH_SESSION = "kf_switch_session"; // 瀹㈡湇杞帴浼氳瘽
	public static final String EVT_POI_CHECK_NOTIFY = "poi_check_notify"; // 闂ㄥ簵瀹℃牳浜嬩欢鎺ㄩ��
	public static final String EVT_QUALIFICATION_VERIFY_FAIL = "qualification_verify_fail";//// 璧勮川璁よ瘉澶辫触
	public static final String EVT_NAMING_VERIFY_FAIL = "naming_verify_fail";// 鍚嶇О璁よ瘉澶辫触
	public static final String EVT_QUALIFICATION_VERIFY_SUCCESS = "qualification_verify_success";// 璧勮川璁よ瘉鎴愬姛
	public static final String EVT_NAMING_VERIFY_SUCCESS = "naming_verify_success";// 鍚嶇О璁よ瘉鎴愬姛
	public static final String EVT_ANNUAL_RENEW = "annual_renew";// 骞村閫氱煡
	public static final String EVT_VERIFY_EXPIRED = "verify_expired";// 璁よ瘉杩囨湡澶辨晥閫氱煡
	public static final String BUTTON_VIEW_LIMITED = "view_limited";// 璺宠浆鍥炬枃娑堟伅URL
	/**
	 * 缃戦〉鎺堟潈鏂瑰紡 SCOPE涓猴細snsapi_base鏂瑰紡
	 */
	public static final String SNSAPI_BASE = "snsapi_base";
	/**
	 * 缃戦〉鎺堟潈鏂瑰紡 SCOPE涓猴細snsapi_userinfo鏂瑰紡
	 */
	public static final String SNSAPI_INFO = "snsapi_userinfo";
	/**
	 * 寰俊鎺ュ彛璋冪敤鐨勫敮涓�绁ㄦ嵁 鏈夋晥鏃堕棿 7200绉� access_token鏄叕浼楀彿鐨勫叏灞�鍞竴绁ㄦ嵁锛屽叕浼楀彿璋冪敤鍚勬帴鍙ｆ椂閮介渶浣跨敤access_token
	 * 寮�鍙戣�呴渶瑕佽繘琛屽Ε鍠勪繚瀛樸�俛ccess_token鐨勫瓨鍌ㄨ嚦灏戣淇濈暀512涓瓧绗︾┖闂淬�俛ccess_token鐨勬湁鏁堟湡鐩墠涓�2涓皬鏃讹紝闇�瀹氭椂鍒锋柊锛�
	 * 閲嶅鑾峰彇灏嗗鑷翠笂娆¤幏鍙栫殑access_token澶辨晥銆�
	 */
	public static final String GET_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	/**
	 * 寰俊缃戦〉OAuth2.0閴存潈
	 */
	public static final String WX_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	/**
	 * 鑾峰彇寰俊缃戦〉鎺堟潈鎵�鐢ㄧ殑AccessToken
	 */
	public static final String WX_AUTHORIZE_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	/**
	 * 鏍规嵁缃戦〉鎺堟潈鐨凙ccessToken浠ュ強openId 鏉ヨ幏鍙栫敤鎴蜂俊鎭�
	 */
	public static final String WX_AUTHORIZE_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	/**
	 * 璺熸牴鎹瓵ccessToken鑾峰彇 js鎺ュ彛鍒嗕韩鐨勫敮涓�绁ㄦ嵁 jsapi_ticket
	 */
	public final static String TICKET_GET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	public static String getHttpsURL(HttpServletRequest request) {
		String temp = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

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
			// 鐢熸垚涓�涓狹D5鍔犲瘑璁＄畻鎽樿
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 璁＄畻md5鍑芥暟
			md.update(str.getBytes());
			// digest()鏈�鍚庣‘瀹氳繑鍥瀖d5 hash鍊硷紝杩斿洖鍊间负8涓哄瓧绗︿覆銆傚洜涓簃d5 hash鍊兼槸16浣嶇殑hex鍊硷紝瀹為檯涓婂氨鏄�8浣嶇殑瀛楃
			// BigInteger鍑芥暟鍒欏皢8浣嶇殑瀛楃涓茶浆鎹㈡垚16浣峢ex鍊硷紝鐢ㄥ瓧绗︿覆鏉ヨ〃绀猴紱寰楀埌瀛楃涓插舰寮忕殑hash鍊�
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			throw new Exception("MD5鍔犲瘑鍑虹幇閿欒");
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
			// 涓や綅涓�缁勶紝琛ㄧず涓�涓瓧鑺�,鎶婅繖鏍疯〃绀虹殑16杩涘埗瀛楃涓诧紝杩樺師鎴愪竴涓瓧鑺�
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
