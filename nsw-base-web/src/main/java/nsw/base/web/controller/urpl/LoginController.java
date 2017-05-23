package nsw.base.web.controller.urpl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.service.LoginService;
import nsw.base.core.service.UserService;
import nsw.base.core.shiro.UserRealm;
import nsw.base.core.utils.MD5Util;
import nsw.base.core.utils.ThreadVariable;
import nsw.base.core.utils.UUIDGenerator;
import nsw.base.core.utils.result.ResultString;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

/**
 * 项目名称：finance<br>
 * *********************************<br>
 * <P>类名称：LoginController</P>
 * *********************************<br>
 * <P>类描述：</P>
 * 创建人：wenjie.zhu<br>
 * 创建时间�?2016�?12�?26�? 下午4:45:29<br>
 * 修改人：<br>
 * 修改时间�?<br>
 * 修改备注�?<br>
 * @version 1.0<br>    
 */

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	RoleController roleController;
	
	@Autowired
	private LoginService loginService;
	//@Autowired
	//private MonitorRealm mr;
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRealm uRealm;
	
	
	private Log logger = LogFactory.getLog(super.getClass());
	
	
    /**
	 * 用户登录
	 * 
	 * @param user
	   *           �?登录用户
	 * @return
	 */
	@RequestMapping(value = "/loginAuth", method = RequestMethod.POST)
    @ResponseBody 
	public ResultString login(HttpServletRequest request,HttpServletResponse response, User user) {
		logger.debug("class:LoginController method:login(HttpServletRequest request,HttpServletResponse response)");

		ResultString resultString = new ResultString();
		
		//就是代表当前的用户�??
		Subject currentUser = SecurityUtils.getSubject();

		
		
		//调用私有化方法，实现shiro认证�?
		//是否通过验证 
		//如果通过验证
		if(currentUser.isAuthenticated()){
			
			String shiroUserName = currentUser.getPrincipal().toString();
			
			if(!shiroUserName.equalsIgnoreCase(user.getUsername())){  
                currentUser.logout();  
                resultString = shiroLogin(currentUser, user.getUsername(), user.getPassword());
            }else{
            	resultString.setStatus(ResultString.RESULT_STATUS_SUCCESS);
            	resultString.setData("success");
            }
            
		}else{
			//没有认证的话，则调用本类私有化方法去验证
			
			//采用MD5加密进行密码验证�?
			
			String password = user.getPassword();
			String passwordMd5 = MD5Util.getMd5Value(password);
			resultString = shiroLogin(currentUser, user.getUsername(), passwordMd5);
			
			
        	//如果不使用MD5加密则采用下面这�?
			//resultString = shiroLogin(currentUser, user.getUsername(), user.getPassword());
		}
		
		return resultString;
	}
	
	
	
	
	private ResultString shiroLogin(Subject currentUser,String username,String password){
		logger.debug("class:LoginController method:shiroLogin(Subject currentUser,String username,String password)");
		ResultString result = new ResultString();
		
		//获取基于用户名和密码的令�? 
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		
		/*
			此处为�?�已记住�?
			但是，�?�已记住”和“已认证”是有区别的�? 
			已记住的用户仅仅是非匿名用户，你可以通过subject.getPrincipals()获取用户信息。但是它并非是完全认证�?�过的用户，当你访问�?要认证用户的功能时，你仍然需要重新提交认证信息�?? 
			这一区别可以参�?�亚马�?�网站，网站会默认记住登录的用户，再次访问网站时，对于非敏感的页面功能，页面上会显示记住的用户信息，但是当你访问网站账户信息时仍然需要再次进行登录认证�?? 
		*/
		token.setRememberMe(false); 

		try {
			
			// 回调doGetAuthenticationInfo，进行认�?  进入MonitorRealm中进行验证�??
			currentUser.login(token);
			
			token.clear();
			
			result.setStatus(ResultString.RESULT_STATUS_SUCCESS);
			result.setData("success");
			
			
			
			//下面的范例是代码中如果需要直接验证角色或权限的方法，返回值均为boolean。只有在用户登录以后返回值才为true，登录以前为false，没权限或没角色也为false
			/*
			boolean hasRoleFlag = currentUser.hasRole("admin");
			System.out.println("=======hasRoleFlag==========" + hasRoleFlag);
			boolean permittedFlag0 = currentUser.isPermitted("user:view");
			System.out.println("=========permittedFlag0========" + permittedFlag0);
			boolean permittedFLAG = currentUser.isPermitted("user:management");
			System.out.println("===permittedFLAG==============" + permittedFLAG);
			*/
			
			
			
		} catch (UnknownAccountException uae) {
			logger.error(uae.getMessage());
			uae.printStackTrace();
			//账户不存�?
			result.setStatus(ResultString.RESULT_STATUS_FAILED);
			result.setMsg("failure");
			result.setData(uae.getMessage());
        } catch (IncorrectCredentialsException ice) {  
			logger.error(ice.getMessage());
			ice.printStackTrace();
        	//密码输入错误（MD5或其他加密的时�?�要注意�?
			result.setStatus(ResultString.RESULT_STATUS_FAILED);
			result.setMsg("failure");
			result.setData(ice.getMessage());
        } catch (LockedAccountException lae) {  
			logger.error(lae.getMessage());
			lae.printStackTrace();
        	//用户账户被锁�?
			result.setStatus(ResultString.RESULT_STATUS_FAILED);
			result.setMsg("failure");
			result.setData(lae.getMessage());
        } catch (AuthenticationException ae) {  
			logger.error(ae.getMessage());
			ae.printStackTrace();
        	//认证异常
			result.setStatus(ResultString.RESULT_STATUS_FAILED);
			result.setMsg("failure");
			result.setData(ae.getMessage());
        }  
		
		return result;
	}

    /**
     * 用户登出
     * 
     * @param 
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody 
    public ResultString logout() {
    	logger.debug("class:LoginController method:logout() ");
    	ResultString resultString = new ResultString();
    	String result = "failure";
    	try{
    		// 登出操作
    		Subject subject = SecurityUtils.getSubject();
    		subject.logout();
        	resultString.setStatus(ResultString.RESULT_STATUS_SUCCESS);
        	resultString.setData("success");
    		
    	}catch(Exception e){
    		logger.error(e.getMessage());
    		System.out.println("登出失败...  logout error... ");
    		e.printStackTrace();
        	resultString.setStatus(ResultString.RESULT_STATUS_FAILED);
        	resultString.setData("failed");
    	}
        return resultString;
    }

    
    /**
     * 
     * <p>方法描述: 认证�?查，即登录检�?</p>
     * <p>方法备注: 暂时还没用到，封存，也可随时删除</p>
     * @return
     */
    @RequestMapping(value = "/chklogin", method = RequestMethod.POST)  
    @ResponseBody  
    public String chkLogin() {  
    	logger.debug("class:LoginController method:chkLogin() ");
  
        Subject currentUser = SecurityUtils.getSubject();  
  
        if (currentUser.isAuthenticated()) {  
            return "true";  
        }  
  
        return "false";  
    }  

    /**
     * 
     * <p>方法描述: 修改密码</p>
     * <p>方法备注: </p>
     * @return
     */
    @RequestMapping(value = "/editPassword", method = RequestMethod.POST)  
    @ResponseBody  
    public ResultString editPassword(HttpServletRequest request,HttpServletResponse response) {  
    	logger.debug("class:LoginController method:editPassword(HttpServletRequest request,HttpServletResponse response) ");
    	
    	ResultString resultString = new ResultString();

    	//初始化返回标志位为失�?
    	resultString.setStatus(ResultString.RESULT_STATUS_FAILED);
    	resultString.setData("failed");
    	JSONObject json=JSONObject.fromObject(request.getParameter("data"));
		
    	
		String username = json.getString("username");
		String password = json.getString("password");
		String passwordMd5 = MD5Util.getMd5Value(password);
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordMd5);
		
		int i = loginService.editPassword(user);
		if (i>0) {
			resultString.setStatus(ResultString.RESULT_STATUS_SUCCESS);
        	resultString.setData("success");
		}
		
    	
    	return resultString; 
    }  
    

    /**
     * 
     * <p>方法描述: 修改云服务连接</p>
     * <p>方法备注: </p>
     * @return
     */
    @RequestMapping(value = "/editSaba", method = RequestMethod.POST)  
    @ResponseBody  
    public ResultString editServer(HttpServletRequest request,HttpServletResponse response) {  
    	logger.debug("class:LoginController method:editPassword(HttpServletRequest request,HttpServletResponse response) ");

    	ResultString resultString = new ResultString();

    	//初始化返回标志位为失败
    	resultString.setStatus(ResultString.RESULT_STATUS_FAILED);
    	resultString.setData("failed");
    	JSONObject json=JSONObject.fromObject(request.getParameter("data"));

		String serverType = json.getString("serverType");
		String serverIp = json.getString("serverIp");
		String serverPort = json.getString("serverPort");
		Cookie[] cookies = request.getCookies();
		Cookie serverTypeCookie = null;
		Cookie serverIpCookie = null;
		Cookie serverPortCookie = null;
		for(Cookie cookie : cookies){
            if(cookie.getName().equals("serverTypeCookie")){
            	serverTypeCookie = cookie;
            }
            if(cookie.getName().equals("serverIpCookie")){
            	serverIpCookie = cookie;
            }
            if(cookie.getName().equals("serverPortCookie")){
            	serverPortCookie = cookie;
            }
        }
		if(serverTypeCookie == null){
			serverTypeCookie = new Cookie("serverTypeCookie", serverType);
		}else{
			serverTypeCookie.setValue(serverType);
		}
		serverTypeCookie.setPath("/");
		serverTypeCookie.setMaxAge(60*60*24*365);
		response.addCookie(serverTypeCookie);
		//本地
		if("1".equals(serverType)){
			if(serverIpCookie == null){
				serverIpCookie = new Cookie("serverIpCookie", serverType);
			}else{
				serverIpCookie.setValue(serverIp);
			}
			if(serverPortCookie == null){
				serverPortCookie = new Cookie("serverPortCookie", serverType);
			}else{
				serverPortCookie.setValue(serverPort);
			}
		}
		serverIpCookie.setPath("/");
		serverIpCookie.setMaxAge(60*60*24*365);
		serverPortCookie.setPath("/");
		serverPortCookie.setMaxAge(60*60*24*365);
		response.addCookie(serverIpCookie);
		response.addCookie(serverPortCookie);
		resultString.setStatus(ResultString.RESULT_STATUS_SUCCESS);
    	resultString.setData("success");
		
    	
    	return resultString; 
    }  
   
	
	
    /**
     * <p>方法描述: 找回密码功能</p>
     * <p>方法备注: 由于�?要在管理员界面另外实现功能界面，故此功能尚不完善�?</p>
     * @param request
     * @param response
     * @return
     * <p>创建人：wenjie.zhu</p>
     * <p>创建时间�?2017�?1�?23�? 下午3:25:48</p>
     */
    @RequestMapping(value = "/getBackPWD", method = RequestMethod.POST)  
    @ResponseBody
    public ResultString getBackPWDByEmail(HttpServletRequest request,HttpServletResponse response) {
    	logger.debug("class:LoginController method:getBackPWDByEmail(HttpServletRequest request,HttpServletResponse response) ");
  
        /*此处设计找回密码页面的时候写逻辑，估计要设计�?个新的DB表来处理此请求�??
         * 1.email  根据email来查询user表的相关信息�?
         * 
         * 2.增加处理标志位，处理人，处理时间。以及请求找回密码的请求人username或user表ID�?
         * 
         * 
         */
		ResultString resultString = new ResultString();

    	//初始化返回标志位为失�?
    	resultString.setStatus(ResultString.RESULT_STATUS_FAILED);
    	resultString.setData("failed");
    	JSONObject json=JSONObject.fromObject(request.getParameter("data"));
		String email = json.getString("email");
		/*
		 * 这一部分做业务处理，或�?�开个页面统计申请密码找回的用户，并定时任务自动修改密码并发送邮件到指定邮箱
		 */
    	
    	return resultString;
    }  

    /**
     * <p>方法描述: 注册页面用户注册功能</p>
     * <p>方法备注: </p>
     * @param request
     * @param response
     * @return
     * <p>创建时间�?2017�?1�?23�? 下午3:22:31</p>
     */
    @SuppressWarnings("deprecation")
	@RequestMapping(value = "/register", method = RequestMethod.POST)  
    @ResponseBody  
    public ResultString register(HttpServletRequest request,HttpServletResponse response) {  
    	logger.debug("class:LoginController method:register(HttpServletRequest request,HttpServletResponse response) ");

		ResultString resultString = new ResultString();
    	//初始化返回标志位为失�?
    	resultString.setStatus(ResultString.RESULT_STATUS_FAILED);
    	resultString.setData("failed");
    	JSONObject json=JSONObject.fromObject(request.getParameter("data"));
		//获取�?系列前台传入数据
		String email = json.getString("email");
		String birthday = json.getString("birthday");
		String fullname = json.getString("fullname");
		String mobilephone = json.getString("mobilephone");
		String password = json.getString("password");
		String username = json.getString("username");
		
		String passwordMd5 = MD5Util.getMd5Value(password);
		
		User user = new User();
		
		Date birth = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			birth = sdf.parse(birthday);
		} catch (ParseException e) {
    		logger.error(e.getMessage());
    		e.printStackTrace();
		}
		
		String  uuid = (String) new UUIDGenerator().generate();
		
		user.setId(uuid);
		user.setBirthday(birth);
		user.setCreateDate(new Date());
		user.setEmail(email);
		user.setUsername(username);
		user.setMobilephone(mobilephone);
		user.setPassword(passwordMd5);
		user.setFullname(fullname);
		//创建用户结果�?0为失败，1为成功�??
		int addUserFlag = userService.createUser(user);
		
		if (addUserFlag>0) {
        	resultString.setStatus(ResultString.RESULT_STATUS_SUCCESS);
        	resultString.setData("success");
		}
		
    	return resultString;
    }  

    /**
     * <p>方法描述: �?查用户名是否存在�?</p>
     * <p>方法备注: </p>
     * @param request
     * @param response
     * @return
     * <p>创建时间�?2017�?1�?23�? 下午3:25:02</p>
     */
    @RequestMapping(value = "/checkUnameExist", method = RequestMethod.POST)  
    @ResponseBody
    public ResultString checkUnameExist(HttpServletRequest request,HttpServletResponse response) {  
    	logger.debug("class:LoginController method:checkUnameExist(HttpServletRequest request,HttpServletResponse response) ");
		ResultString resultString = new ResultString();
    	//初始化返回标志位为失�?
    	resultString.setStatus(ResultString.RESULT_STATUS_FAILED);
    	resultString.setData("failed");
    	//获取前台传入数据
    	JSONObject json=JSONObject.fromObject(request.getParameter("data"));
    	//根据用户名查找，�?以获取用户名
		String username = json.getString("username");
		//数据库SYS_USER表中是否有记录，如果没有则对象为空，如果有，则对象不为空。如果出现多个这里会报异常，属于数据错误，联系管理员
		User userNameCheckExist = loginService.loginCheck2(username);
		//如果不存在，即User对象为空的话，则返回success，即表示此用户名尚未被抢注，可以使用�?
		if (userNameCheckExist == null) {
        	resultString.setStatus(ResultString.RESULT_STATUS_SUCCESS);
        	resultString.setData("success");
		}
		
    	return resultString;
    }  
    
    
    @RequestMapping(value = "/getUname", method = RequestMethod.POST)  
    @ResponseBody  
    public User getUsername() {  
    	logger.debug("class:LoginController method:getUsername() ");
    	User user = new User();
    	try {
    		user = (User) ThreadVariable.getUser();    		
		} catch (Exception e) {
    		logger.error(e.getMessage());
			e.printStackTrace();
		}
    	
    	
        return user;  
    }  

    
    
    
    
    
    /**
     * <p>方法描述: 根据username获取某一用户的所有权�?</p>
     * <p>方法备注: </p>
     * @param username
     * @return
     * <p>创建人：wenjie.zhu</p>
     * <p>创建时间�?2017�?2�?28�? 下午4:48:16</p>
     */
    @RequestMapping(value = "/findPermissionsByUname", method = RequestMethod.POST)  
    @ResponseBody  
    public Set<BaseEntity> findPermissionsByUname(String username) {  
    	logger.debug("class:LoginController method:findPermissionsByUname() ");
    	
    	return loginService.findPermissionsByUname(username);  
    	
    }  

    /**
     * <p>方法描述: 根据username获取某一用户的全部角�?</p>
     * <p>方法备注: </p>
     * @param username
     * @return
     * <p>创建人：wenjie.zhu</p>
     * <p>创建时间�?2017�?2�?28�? 下午4:48:44</p>
     */
    @RequestMapping(value = "/findRolesByUname", method = RequestMethod.POST)  
    @ResponseBody  
    public Set<String> findRolesByUname(String username) {  
    	logger.debug("class:LoginController method:findPermissionsByUname() ");
    	
    	return loginService.findRolesByUname(username);  
    	
    } 
    
    
    
    
    
    
    
    
    
    
    
    
    //////////////////下面是一段测试代码，供权限部分开发参�?////////////////////////
    
    //表示当前Subject已经通过login进行了身份验证；即Subject. isAuthenticated()返回true�?
    //@RequiresAuthentication
    
    //表示当前Subject已经身份验证或�?��?�过记住我登录的�?
    //@RequiresUser 
    
    //表示当前Subject�?要角色admin和dev�?
    @RequiresRoles(value={"admin", "dev"}, logical= Logical.AND) 
    
    //表示当前Subject�?要权限user:a或user:b�?  
    @RequiresPermissions (value={"user:create", "role:delete"}, logical= Logical.OR)  
    
    //@RequiresPermissions("user:create")
    //@RequiresRoles("admin")
    @RequestMapping(value = "/testPermission", method = RequestMethod.POST)  
    @ResponseBody  
    @ExceptionHandler 
    public String testPermission() {  
    	logger.debug("class:LoginController method:testPermission() ");
    	
    	Session session = SecurityUtils.getSubject().getSession();
    	
    	User user = (User) session.getAttribute("userInfo");
    	
    	String username = user.getUsername();
    	
    	System.out.println("============username=============" + username);
    	
    	//下面的范例是代码中如果需要直接验证角色或权限的方法，返回值均为boolean。只有在用户登录以后返回值才为true，登录以前为false，没权限或没角色也为false
		
		boolean hasRoleFlag = SecurityUtils.getSubject().hasRole("admin");
		System.out.println("=======hasRoleFlag==========" + hasRoleFlag);
		boolean permittedFlag0 = SecurityUtils.getSubject().isPermitted("user:view");
		System.out.println("=========permittedFlag0========" + permittedFlag0);
		boolean permittedFLAG = SecurityUtils.getSubject().isPermitted("user:management");
		System.out.println("===permittedFLAG==============" + permittedFLAG);
		
		
    	try {
			throw new IOException();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return username;  
    }
    
    
    
    
    
    
    

}
