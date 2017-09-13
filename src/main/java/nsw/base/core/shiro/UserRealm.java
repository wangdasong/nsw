package nsw.base.core.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import nsw.base.core.dao.entity.Resource;
import nsw.base.core.dao.entity.SubsysConfig;
import nsw.base.core.dao.entity.User;
import nsw.base.core.dao.entity.base.BaseEntity;
import nsw.base.core.service.LoginService;
import nsw.base.core.service.SubsysConfigService;
import nsw.base.core.utils.ThreadVariable;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

@Service("userRealm")
public class UserRealm extends AuthorizingRealm {
	@Autowired
	private LoginService loginService;
	@Autowired
	SubsysConfigService subsysConfigService;
	
	@Override
    public String getName() {
        return "UserRealm";
    }
	
	private Log logger = LogFactory.getLog(super.getClass());
	
	/**
	 * 角色，权限验证
	 */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	logger.debug("class:UserRealm method:doGetAuthorizationInfo(PrincipalCollection principals)");

        //获取shiro的session对象
        Session session = SecurityUtils.getSubject().getSession();
    	//System.out.println("============UserRealm=======L41======start!========");
    	//获取用户名
        String username = (String)principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //通过用户名获取其相关的角色列表（Set集合）
        Set<String> findRolesByUname = loginService.findRolesByUname(username);
        authorizationInfo.setRoles(findRolesByUname);
        //获取用户名相关的权限列表并放入认证对象中。
        Set<BaseEntity> resourceSet = loginService.findPermissionsByUname(username);
        Set<String> resourceStringSet = new HashSet<String>();
        for(BaseEntity resource : resourceSet){
        	if(resource == null){
    			continue;
    		}
        	resourceStringSet.add(resource.getId());
        }
        authorizationInfo.setStringPermissions(resourceStringSet);    
        try {
        	//两手准备，将user对象的信息放入session中。
        	Set<BaseEntity> permissions = resourceSet;
    		Set<String> permissionMenuSet = new HashSet<String>();
    		Set<String> permissionContainerSet = new HashSet<String>();
    		Set<String> permissionWidgetSet = new HashSet<String>();
    		Set<String> permissionPCSet = new HashSet<String>();
    		Set<String> permissionPWSet = new HashSet<String>();
    		Set<String> permissionElementSet = new HashSet<String>();
        	session.setAttribute("USER_RESOURCES", permissions);
        	for(BaseEntity resource : permissions){
        		if(resource == null){
        			continue;
        		}
				if(Resource.RESOURCE_TYPE_MENU.equals(((Resource)resource).getType())){
					permissionMenuSet.add(resource.getId());
				}
				if(Resource.RESOURCE_TYPE_CONTAINER.equals(((Resource)resource).getType())){
					permissionContainerSet.add(resource.getId());
				}
				if(Resource.RESOURCE_TYPE_WIDGET.equals(((Resource)resource).getType())){
					permissionWidgetSet.add(resource.getId());
				}
				if(Resource.RESOURCE_TYPE_PC.equals(((Resource)resource).getType())){
					permissionPCSet.add(resource.getId());
				}
				if(Resource.RESOURCE_TYPE_PW.equals(((Resource)resource).getType())){
					permissionPWSet.add(resource.getId());
				}
				if(Resource.RESOURCE_TYPE_ELEMENT.equals(((Resource)resource).getType())){
					permissionElementSet.add(resource.getId());
				}
			}
        	session.setAttribute("USER_MENU_PERMISSIONS", permissionMenuSet);
        	session.setAttribute("USER_CONTAINER_PERMISSIONS", permissionContainerSet);
        	session.setAttribute("USER_WIDGET_PERMISSIONS", permissionWidgetSet);
        	session.setAttribute("USER_PC_PERMISSIONS", permissionPCSet);
        	session.setAttribute("USER_PW_PERMISSIONS", permissionPWSet);
        	session.setAttribute("USER_ELEMENT_PERMISSIONS", permissionElementSet);
			//设置线程变量
			ThreadVariable.setResourceSetVariable(permissions);
			ThreadVariable.setMenuPermissionSetVariable(permissionMenuSet);
			ThreadVariable.setContainerPermissionSetVariable(permissionContainerSet);
			ThreadVariable.setWidgetPermissionSetVariable(permissionWidgetSet);
			ThreadVariable.setPcPermissionSetVariable(permissionPCSet);
			ThreadVariable.setPwPermissionSetVariable(permissionPWSet);
			ThreadVariable.setElementPermissionSetVariable(permissionElementSet);
		} catch (Exception e) {
			//记录异常
			logger.error(e.getMessage());
			//异常打印，可有可无。
			e.printStackTrace();
			return null;
		}
        
        //System.out.println("============UserRealm=======L46======END!========");
        return authorizationInfo;
    }
    
    
    
    /**
     * 登录验证
     */
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	logger.debug("class:UserRealm method:doGetAuthorizationInfo(PrincipalCollection principals)");
    	Session session = SecurityUtils.getSubject().getSession();
        SavedRequest savedRequest = (SavedRequest)SecurityUtils.getSubject().getSession().getAttribute("shiroSavedRequest");
        
		ThreadVariable.setSubsysCodeVariable((String) session.getAttribute("SUBSYS_CODE"));
    	//获取用户名
        String username = (String)token.getPrincipal();
        //得到密码
        String password = new String((char[])token.getCredentials()); 
        //检查用户名是否存在，即用户是否存在（用户名唯一）
        User user = loginService.loginCheck2(username);
        if(user == null) {
        	logger.error("UnknownAccountException-账号不存在");
            throw new UnknownAccountException();//账号不存在
        }
        
        //检查可用状态位是否为0,0表示可用
        if("1".equals(user.getState())) {
        	logger.error("LockedAccountException-账号被锁定");
            throw new LockedAccountException(); //帐号锁定
        }
        
        //如果user对象不为空，则表示根据此用户名可以在数据库中查到相关记录，即用户名正确的情况下，
        //判断user对象中的密码（即数据库中存的密码）是否跟输入的密码一致
        if(!password.equals(user.getPassword())) {
        	logger.error("IncorrectCredentialsException-密码错误");
            throw new IncorrectCredentialsException(); //如果密码错误
        }
        /**
         * 盐值构造器加密暂时不用，但spring-shiro文件中有配置，只是注释封存了。另shiro包（即本包目录）下的
         * /finance/src/main/java/com/cpscs/finance/shiro/RetryLimitHashedCredentialsMatcher.java 类是关于盐值加密的salt若有需要随时可配。
         * 
         * 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得不好可以在此判断或自定义实现
         */
        //盐值构造器加密
        /*SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
               ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );*/
    	SimpleAuthenticationInfo account= new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
    	session.setAttribute("LOGIN_USER", user);
    	
        if(session.getAttribute("SUBSYS_CODE") == null || "".equals(session.getAttribute("SUBSYS_CODE"))){
        	Cookie subsysCodeCookie = null;
        	HttpServletRequest req = WebUtils.getHttpRequest(SecurityUtils.getSubject());
			Cookie[] cookies = req.getCookies();
			for(Cookie cookie : cookies){
	            if(cookie.getName().equals("subsysCode")){
	            	subsysCodeCookie = cookie;
	            }
	        }
			if(subsysCodeCookie != null && subsysCodeCookie.getValue() != null && !"".equals(subsysCodeCookie.getValue())){
				session.setAttribute("SUBSYS_CODE", subsysCodeCookie.getValue());
			}else{
	        	List<SubsysConfig> subsysConfigList = subsysConfigService.getAllList();
	        	//设置默认的子系统
	        	if(subsysConfigList != null && subsysConfigList.get(0) != null){
	        		SubsysConfig subsysConfig = (SubsysConfig) subsysConfigList.get(0);
	            	session.setAttribute("SUBSYS_CODE", subsysConfig.getCode());
	        	}
			}
    	}
		ThreadVariable.setUser(user);
        return account;
    }
    
    
    
    
    
    
    
    
    /**
     * shiro最新版本均可自动清空缓存，方法覆盖（方法重写）Override一下即可。
     */
    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }


    
    
    
    
}