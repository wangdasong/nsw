package nsw.base.core.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class SimpleProxy {
  
    /** 
     * 提交到目标服务器。 
     *  
     * @param request 
     * @param response 
     * @param targetUrl 
     * @throws IOException 
     */  
    public static void doServer(HttpServletRequest request, HttpServletResponse response,  
        String targetUrl) throws IOException {  
	    URL url = new URL(targetUrl);  
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    // 发送POST请求必须设置如下两行  
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    conn.setRequestMethod(request.getMethod());  
	    // 可以拷贝客户端的head信息作为请求的head参数  
	    conn.setRequestProperty("Charsert", request.getCharacterEncoding());
	    conn.setRequestProperty("Content-Type", request.getContentType());
		Cookie[] cookies= request.getCookies();
		String cookiesStr = "";
		for(Cookie cookie : cookies){
			cookiesStr = cookiesStr + cookie.getName() + "=" + cookie.getValue() + "; ";
		}
		conn.setRequestProperty("Cookie", cookiesStr);
		conn.connect();
	    // 直接把客户端的BODY传给目标服务器  
		DataOutputStream send = new DataOutputStream(conn.getOutputStream());
	    DataInputStream body = new DataInputStream(request.getInputStream());
	    IOUtils.copy(body, send);
	    send.flush();  
	    send.close();  
	    body.close();  
	  
	    // 获取返回值  
	    int resultCode=conn.getResponseCode();
	    String contentType = conn.getContentType();
	    String headerServer = conn.getHeaderField("Server");
	    String headerContentDisposition = conn.getHeaderField("Content-Disposition");
	    String headerContentLength = conn.getHeaderField("Content-Length");
	    response.setContentType(contentType);
	    response.setHeader("Server", headerServer);
	    response.setHeader("Content-Disposition", headerContentDisposition);
	    response.setHeader("Content-Length", headerContentLength);
	    response.setCharacterEncoding("utf-8");
	    if(HttpURLConnection.HTTP_OK==resultCode){
	    	DataInputStream providerInput = new DataInputStream(conn.getInputStream());
	    	DataOutputStream responseOutput = new DataOutputStream(response.getOutputStream());
		    IOUtils.copy(providerInput, responseOutput);
		    responseOutput.flush();
		    responseOutput.close();
		    providerInput.close();
//	        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
//	        ServletOutputStream out = response.getOutputStream();
//	        String line;
//	        while ((line = in.readLine()) != null) {  
//	            out.write(line.getBytes("utf-8"));
//	        }  
//	        out.flush();
//	        in.close();
//	        out.close();
	    }
	    conn.disconnect();
    }
}
