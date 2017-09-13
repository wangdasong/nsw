/**
 * 
 */
package nsw.base.core.utils.filter;
import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;





/**
 * 项目名称：finance<br>
 * *********************************<br>
 * <P>类名称：FinanceCharactorFilter</P>
 * *********************************<br>
 * <P>类描述：</P>
 * 创建人：wenjie.zhu<br>
 * 创建时间：2017年1月5日 上午11:15:56<br>
 * 修改人：wenjie.zhu<br>
 * 修改时间：2017年1月5日 上午11:15:56<br>
 * 修改备注：<br>
 * @version 1.0<br>    
 */
public class FinanceCharactorFilter implements Filter, Serializable{
	private static final long serialVersionUID = 7534120014073996238L;
	//此处也可以不采用web.xml配置encoding的方式来设置UTF-8编码，直接在此处指定也是一样。
	String encoding = null;

	class Request extends HttpServletRequestWrapper implements Serializable
	{

		private static final long serialVersionUID = -1107679292702064267L;


		public Request(HttpServletRequest request) {
	    	super(request);
	    }


		public String toChi(String input) {
		    try {
		      byte[] bytes = input.getBytes("ISO8859-1");
		      return new String(bytes, "UTF-8");
		    }
		    catch (Exception ex) {
		    }
		    return null;
		}

	    /**
	     * Return the HttpServletRequest holded by this object.
	     */
	    private HttpServletRequest getHttpServletRequest()
	    {
	        return (HttpServletRequest)super.getRequest();
	    }


	    public String getParameter(String name)
	    {
	    	return toChi(getHttpServletRequest().getParameter(name));
	    }


	    public String[] getParameterValues(String name)
	    {
		      String values[] = getHttpServletRequest().getParameterValues(name);
		      if (values != null) {
		        for (int i = 0; i < values.length; i++) {
		          values[i] = toChi(values[i]);
		        }
		      }
		      return values;
	    }
	}
	
	@Override
	public void init(FilterConfig conf) throws ServletException {
		encoding = conf.getInitParameter("encoding");	
		
		//System.out.println("=============FinanceCharactorFilter=========init======encoding===" + encoding);
		
	}
	

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filter) throws IOException, ServletException {
		//System.out.println("=============FinanceCharactorFilter=========doFilter=========req.getCharacterEncoding()======" + req.getCharacterEncoding());
		HttpServletRequest httpreq = (HttpServletRequest)req;
		if(httpreq.getMethod().equals("GET") 
				|| httpreq.getContentType() == null
				|| "application/json;charset=utf-8".equals(httpreq.getContentType())
				|| "application/json;charset=UTF-8".equals(httpreq.getContentType())
				|| "".equals(httpreq.getContentType())
				) {
			req = new Request(httpreq);
		} else {
			req.setCharacterEncoding("UTF-8");
		}
		filter.doFilter(req, resp);
	}


	
	
	@Override
	public void destroy() {
		encoding = null;
		//System.out.println("=============FinanceCharactorFilter=========destroy======encoding===" + encoding);
	}
	
	
	
	
}
