package nsw.base.core.utils.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.utils.ShiroFilterUtils;
import nsw.base.core.utils.ThreadVariable;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

public class UserSessionFilter extends UserFilter  {
	String loginUrl;
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue){
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rep = (HttpServletResponse) response;
        String reqUrl = req.getRequestURI();
        System.out.println("reqUrl====" + reqUrl);
		Subject subject = getSubject(request, response);
		if (isLoginRequest(request, response)) {
			return true;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(reqUrl.contains("changeSubsys")){
			String subsysCode = req.getParameter("subsysCode");
			subject.getSession().setAttribute("SUBSYS_CODE", subsysCode);
			Cookie subsysCodeCookie = null;
			Cookie[] cookies = req.getCookies();
			for(Cookie cookie : cookies){
	            if(cookie.getName().equals("subsysCode")){
	            	subsysCodeCookie = cookie;
	            }
	        }
			if(subsysCodeCookie == null){
				subsysCodeCookie = new Cookie("subsysCode", subsysCode);
			}else{
				subsysCodeCookie.setValue(subsysCode);
			}
			subsysCodeCookie.setPath("/");
			//设置cookies一年有效
			subsysCodeCookie.setMaxAge(60*60*24*365);
			rep.addCookie(subsysCodeCookie);
			Map<String, Integer> codeMap = new HashMap<String, Integer>();
			codeMap.put("code", 200);
			resultMap.put("status", codeMap);
			ShiroFilterUtils.out(response, resultMap);
			return true;
		}
		User currUser = (User) subject.getSession().getAttribute("LOGIN_USER");
		if((subject.getPrincipal() == null || currUser == null) && ShiroFilterUtils.isAjax(request)){
			Map<String, Integer> codeMap = new HashMap<String, Integer>();
			codeMap.put("code", 300);
			resultMap.put("status", codeMap);
			resultMap.put("message","\u5F53\u524D\u7528\u6237\u6CA1\u6709\u767B\u5F55\uFF01");// 当前用户没有登录！
			ShiroFilterUtils.out(response, resultMap);
		}
		if(currUser == null){
			req.getSession().invalidate();
			return false;
		}
		boolean result = subject.getPrincipal() != null;
		if(result){
			//如果是读取菜单路径，是指
	        if(reqUrl.contains("menuPath")){
	        	String menuId = reqUrl.substring(reqUrl.indexOf("menuPath") + 9, reqUrl.length());
	        	subject.getSession().setAttribute("CURR_MENU_ID", menuId);
	        }
	        //如果是弹出页面的话,设置弹出页面的弹出控件ID
	        if(reqUrl.contains("setPopupSession")){
	        	String popupWidgetId = req.getParameter("widgetId");
				subject.getSession().setAttribute("CURR_POPUP_WIDGET_ID", popupWidgetId);
	        }
	        //清除弹出页面控件ID
	        if(reqUrl.contains("clearPopupSession")){
				subject.getSession().setAttribute("CURR_POPUP_WIDGET_ID", null);
	        	Map<String, Integer> codeMap = new HashMap<String, Integer>();
	        	codeMap.put("code", 100);
				resultMap.put("status", codeMap);
				resultMap.put("message","返回dummy数据！");
				ShiroFilterUtils.out(response, resultMap);
	        }

			Cookie[] cookies = req.getCookies();
			String serverTypeCookie = null;
			String serverIpCookie = null;
			String serverPortCookie = null;
			for(Cookie cookie : cookies){
	            if(cookie.getName().equals("serverTypeCookie")){
	            	serverTypeCookie = cookie.getValue();
	            }
	            if(cookie.getName().equals("serverIpCookie")){
	            	serverIpCookie = cookie.getValue();
	            }
	            if(cookie.getName().equals("serverPortCookie")){
	            	serverPortCookie = cookie.getValue();
	            }
	        }
	        req.getCookies();
			Set<BaseEntity> currResource = (Set<BaseEntity>) subject.getSession().getAttribute("USER_RESOURCES");
			Set<String> currMenuPermissions = (Set<String>) subject.getSession().getAttribute("USER_MENU_PERMISSIONS");
			Set<String> currContainerPermissions = (Set<String>) subject.getSession().getAttribute("USER_CONTAINER_PERMISSIONS");
			Set<String> currWidgetPermissions = (Set<String>) subject.getSession().getAttribute("USER_WIDGET_PERMISSIONS");
			Set<String> currPcPermissions = (Set<String>) subject.getSession().getAttribute("USER_PC_PERMISSIONS");
			Set<String> currPwPermissions = (Set<String>) subject.getSession().getAttribute("USER_PW_PERMISSIONS");
			Set<String> currElementPermissions = (Set<String>) subject.getSession().getAttribute("USER_ELEMENT_PERMISSIONS");
			String currMenuId = (String) subject.getSession().getAttribute("CURR_MENU_ID");
			String currPopupWidgetId = (String) subject.getSession().getAttribute("CURR_POPUP_WIDGET_ID");
			String subsysCode = (String) subject.getSession().getAttribute("SUBSYS_CODE");
			//设置线程变量
			ThreadVariable.setUser(currUser);
			ThreadVariable.setResourceSetVariable(currResource);
			ThreadVariable.setMenuPermissionSetVariable(currMenuPermissions);
			ThreadVariable.setContainerPermissionSetVariable(currContainerPermissions);
			ThreadVariable.setWidgetPermissionSetVariable(currWidgetPermissions);
			ThreadVariable.setPcPermissionSetVariable(currPcPermissions);
			ThreadVariable.setPwPermissionSetVariable(currPwPermissions);
			ThreadVariable.setElementPermissionSetVariable(currElementPermissions);
        	ThreadVariable.setCurrMenuVariable(currMenuId);
        	ThreadVariable.setPopupWidgetVariable(currPopupWidgetId);
        	ThreadVariable.setServerTypeVariable(serverTypeCookie);
        	ThreadVariable.setServerIpVariable(serverIpCookie);
        	ThreadVariable.setServerPortVariable(serverPortCookie);
        	ThreadVariable.setSubsysCodeVariable(subsysCode);
		}
		return result;
	}

	
	
	//未登录重定向到登陆页
	protected void redirectToLogin(ServletRequest req, ServletResponse resp)
			throws IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		WebUtils.issueRedirect(request, response, loginUrl);
	}
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
	
	
}
