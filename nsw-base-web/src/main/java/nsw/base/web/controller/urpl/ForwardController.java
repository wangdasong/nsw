package nsw.base.web.controller.urpl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 视图控制�?,返回视图给前�?
 **/
@Controller
@RequestMapping("/forward")
public class ForwardController {
	
	/**
     * 首页
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * 登录�?
     */
    @RequestMapping("/login")
    public String login() {
        return "views/login";
    }


    /**
     * 404�?
     */
    @RequestMapping("/404")
    public String error404() {
        return "views/errorpage/404";
    }
    
    /**
     * 404�?-2
     * 
     * 404的备用页面，风格略有不同
     */
    @RequestMapping("/4042")
    public String error4042() {
        return "views/errorpage/404_2";
    }


    /**
     * 401�?
     */
    @RequestMapping("/401")
    public String error401() {
        return "views/errorpage/401";
    }
    /**
     * 401页，
     * 
     * 内部权限不足，�?�不是没有权�?
     * 
     * 可以当做401页面的备用页面使用�??
     */
    @RequestMapping("/4012")
    public String error4012() {
    	return "views/errorpage/401_2";
    }

    /**
     * 500�?
     */
    @RequestMapping("/500")
    public String error500() {
        return "views/errorpage/500";
    }

}