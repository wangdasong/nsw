package nsw.base.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletOutputStream;

public class HttpUtils {
	
	//返回内容字符串 但是会造成图片等无法解析,暂不使用
	public static String getPageContent(String strUrl, String strPostRequest,
			int maxLength) {
		// 读取结果网页
		StringBuffer buffer = new StringBuffer();
		System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
		System.setProperty("sun.net.client.defaultReadTimeout", "5000");
		try {
			URL newUrl = new URL(strUrl);
			HttpURLConnection hConnect = (HttpURLConnection) newUrl
					.openConnection();
			// POST方式的额外数据
			if (strPostRequest.length() > 0) {
				hConnect.setDoOutput(true);
				OutputStreamWriter out = new OutputStreamWriter(
						hConnect.getOutputStream());
				out.write(strPostRequest);
				out.flush();
				out.close();
			}
			// 读取内容
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					hConnect.getInputStream()));
			int ch;
			for (int length = 0; (ch = rd.read()) > -1
					&& (maxLength <= 0 || length < maxLength); length++)
				buffer.append((char) ch);
			rd.close();
			hConnect.disconnect();
			return buffer.toString().trim();
		} catch (Exception e) {
			// return "错误:读取网页失败！";
			return null;
		}
	}
/**
 * 用流操作,可以解析图片等二进制文件
 * @param strUrl
 * @param out
 * @return
 */
	public static ServletOutputStream getOutputContext(String strUrl,String cookie, String paraUrl, ServletOutputStream out) {
		System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
		System.setProperty("sun.net.client.defaultReadTimeout", "5000");
		try {
//			URL newUrl = new URL(strUrl.split("\\?")[0] + "?" + paraUrl);
			URL newUrl = new URL(strUrl);
			HttpURLConnection hConnect = (HttpURLConnection) newUrl
					.openConnection();
			hConnect.setRequestProperty("Cookie", cookie);
			BufferedInputStream bis = new BufferedInputStream(
					hConnect.getInputStream());
			byte[] buffer1 = new byte[1024];
			while (true) {
				if(bis.available()<1024){
					 // 一位一位读出再写入目的文件
                    int remain = -1;
                    while((remain = bis.read())!= -1) {
                    	out.write(remain);
                    }
                    break;
				}else{
					bis.read(buffer1); // 读入到缓冲区
					out.write(buffer1); 
				}
			}
			
			 bis.close();
			return out;
		} catch (Exception e) {
			// return "错误:读取网页失败！";
			e.printStackTrace();
			return null;
		}
	}

}
