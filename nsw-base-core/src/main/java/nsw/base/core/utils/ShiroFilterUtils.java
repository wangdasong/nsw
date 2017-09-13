package nsw.base.core.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public class ShiroFilterUtils {
    /**
     * 是否是Ajax请求
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request){
    	String contentType = ((HttpServletRequest) request).getHeader("Content-Type");
    	String accept = ((HttpServletRequest) request).getHeader("Accept");
    	if("application/json;charset=utf-8".equalsIgnoreCase(contentType) 
    			|| (contentType == null && accept.contains("application/json"))){
    		return Boolean.TRUE;
    	}
    	return Boolean.FALSE;
    }
    /**
     *  使用	response 输出JSON
     * @param hresponse
     * @param resultMap
     * @throws IOException
     */
    public static void out(ServletResponse response, Object resultObj){
    	PrintWriter out = null;
    	try {
    		response.setCharacterEncoding("UTF-8");//设置编码
    		response.setContentType("application/json");//设置返回类型
    		out = response.getWriter();
    		out.println(JSONObject.fromObject(resultObj).toString());//输出
    	} catch (Exception e) {
    	}finally{
    		if(null != out){
    			out.flush();
    			out.close();
    		}
    	}
    }
}
