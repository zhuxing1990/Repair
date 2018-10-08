package com.vunke.repair.util;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;



public class DesUtil {

	private static byte[] IV = { 0x12, 0x34, 0x56, 0x78, (byte) 0x90,
            (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };
	
	public static DateFormat df = new SimpleDateFormat("yyyyMMdd");

	public static String encrypt(String userTel) throws Exception {
		ResourceBundle rb = ResourceBundle.getBundle("appconf");
		String appCode = rb.getString("integralappCode");
		String key = rb.getString("integralkey");
		String integralURL = rb.getString("integralURL");
		String timestamp = df.format(new Date());
		String[] str = { appCode, userTel, timestamp };
		Arrays.sort(str);
		String bigStr = str[0] + str[1] + str[2];
		String sign = new SHA1().getDigestOfString(bigStr.getBytes())	.toLowerCase();
		String data = appCode + "|" + userTel + "|" + timestamp + "|"+ sign.toUpperCase();
		System.out.println("data===" + data);
		String sertoken = encrypt(data, key);
		System.out.println("sertoken===" + sertoken);
		integralURL = integralURL.replace("###", URLEncoder.encode(sertoken));
		return integralURL;
	}

	/**
	 * 加密
	 * @param data 待加密的明文
	 * @param key 加密密钥
	 * @return 密文
	 * @throws Exception
	 */
	public static String encrypt(String data, String key) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec ks = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey sk = skf.generateSecret(ks);
		Cipher cip = Cipher.getInstance("DES/CBC/PKCS5Padding");// Cipher.getInstance("DES");
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		cip.init(Cipher.ENCRYPT_MODE, sk, ivSpec);// IV的方式
		// cip.init(Cipher.ENCRYPT_MODE, sk, sr);//没有传递IV
		return new String(new Base64().encode(cip.doFinal(data
				.getBytes("UTF-8"))));

	}

	/**
	 * 解密
	 * @param data 待解密的密文
	 * @param key 解密密钥
	 * @return 明文
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data, String key) throws IOException,
			Exception {
		if (data == null) {
			return null;
		}

		byte[] buf = new Base64().decode(data.getBytes("UTF-8"));

		SecureRandom sr = new SecureRandom();

		DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		IvParameterSpec ivSpec = new IvParameterSpec(IV);
		cipher.init(Cipher.DECRYPT_MODE, securekey, ivSpec);

		// cipher.init(Cipher.DECRYPT_MODE, securekey, sr);//没有传递IV

		return new String(cipher.doFinal(buf));
	}

	public static void main(String[] args) throws Exception {

		
		String key = "i@O!R%an145&eX2#0#17@Yx0";		
		//String a = "886e954ff9b0c8f9c3061cd18d77b7a0";//13378014521      bYEbLJySO7vYCcRRKpENBQ==	18973103721 0CkUYQeVlgK6+NTTnoZpUw==
		//FDtMGCDLWQ2Q18w7RUXFqHYwifiYrF/PJ/MBH3i6WsZAZXRl/vECmg==  我的易信加密后的内容
		//String a = "f8596cfd57fc8223c3061cd18d77b7d2";
	//	String a = "18973103721";//"13378014521";//"886e954ff9b0c8f9c3061cd18d77b7a0";13378014521
		String a = "%7B%22openid%22%3A%22ovCfnjrORSnoE4PJwrTJRD1S_7Q0%22%2C%22nickname%22%3A%22%E9%9D%92%E5%B3%B0%E4%BE%A0%28%E6%88%90%E6%96%B9%E6%B8%85%2918974248568%22%2C%22sex%22%3A1%2C%22language%22%3A%22zh_CN%22%2C%22city%22%3A%22%E5%B8%B8%E5%BE%B7%22%2C%22province%22%3A%22%E6%B9%96%E5%8D%97%22%2C%22country%22%3A%22%E4%B8%AD%E5%9B%BD%22%2C%22headimgurl%22%3A%22http%3A%5C%2F%5C%2Fthirdwx.qlogo.cn%5C%2Fmmopen%5C%2Fvi_32%5C%2FoYqLY20YRO50Qia3hichfnViaBibdjopY2pD7oVoYO2nQluyTj3mfFicTjwIDmXGuzibjCAEE5bKXoWHLUbbukxBgWicQ%5C%2F132%22%2C%22privilege%22%3A%5B%5D%2C%22unionid%22%3A%22ourAr0qbKzkaBZz9i9Snf-MtM6ZA%22%7D";//"13378014521";//"886e954ff9b0c8f9c3061cd18d77b7a0";13378014521

		a = URLDecoder.decode(a, "utf-8");
		System.out.println(a);
		a = DesUtil.encrypt(a, key);
		System.out.println(a);
		a= URLEncoder.encode(a, "utf-8");
		System.out.println(a);
		
		
		/*String b ="0CkUYQeVlgK6+NTTnoZpUw==";//"bYEbLJySO7vYCcRRKpENBQ==";//"bYEbLJySO7vYCcRRKpENBQ";
		String text = DesUtil.encrypt(a, key);   
		String detext=DesUtil.decrypt(b, key);
		System.out.println(text);
		System.out.println(detext);
		text=text.replace("+","A_a");
		System.out.println(text);
		String text2=text.replace("A_a","+");
		System.out.println(text2);
		String replaceNo=text2.replace("A_a", "+");
		String jiemino=DesUtil.decrypt(text2, key);
		System.out.println(jiemino);*/
		//from_user_name=b3ZDZm5qanF0b0ctRnJSaGZ5RmM1VVRVamVBMCMxNDExMDI4NjMwMjAz&to_user_name=gh_c532a7cb88e9


	}
}
