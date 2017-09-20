package nsw.base.core.shiro;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;


@ControllerAdvice
public class DefaultExceptionHandler  implements HandlerExceptionResolver  {
	
	private static final Logger logger = Logger.getLogger(DefaultExceptionHandler.class);
	
	
    /**
     * 没有权限 异常
     * 后续根据不同的需求定制即可
     */
    //@ExceptionHandler(value={UnauthorizedException.class,AuthorizationException.class})
    //@ResponseStatus(value=HttpStatus.UNAUTHORIZED , reason="无权限访问")
	@ResponseBody  
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,Object object, Exception e) {
		ModelAndView mv = new ModelAndView();
		
		/*  使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常   */  
        FastJsonJsonView view = new FastJsonJsonView();  
        Map<String, Object> attributes = new HashMap<String, Object>();  
        
        
        /**
         * ModelAndView传统视图模式返回的话，解封下面这段代码：
        
        String servletPath = request.getServletPath();
        StringBuffer requestURL = request.getRequestURL();
		
		int i = requestURL.indexOf(servletPath);
		String basePath = requestURL.substring(0,i);
        
        
         */
		//String pathInfo = request.getPathInfo();
		//String requestURI = request.getRequestURI();

		if(e instanceof UnauthorizedException || e instanceof AuthorizationException ){
			attributes.put("code", "401");  
			attributes.put("msg", e.getMessage());  
		}else{
			attributes.put("code", "500");  
			attributes.put("msg", e.getMessage());  
			
			/**
			 * ModelAndView传统视图模式返回的话，解封下面这段代码：
			 * 其余异常的话，依旧采用ModelAndView进行返回处理，当然后期如果有需要也可以改造成上述形式。
			 
	        mv.addObject("exception", ex);
	        mv.setViewName("redirect:" + basePath + "/views/errorpage/500.html");
			return mv;
	        
	        */
		}
        
        view.setAttributesMap(attributes);  
        mv.setView(view);   
        
        logger.error("异常:" + e.getMessage(), e);  
        e.printStackTrace();
		
		return mv;
		
		
	}

}
