package nsw.base.web.controller.urpl;

import java.util.List;

import nsw.base.core.controller.base.BaseController;
import nsw.base.core.dao.entity.SubsysConfig;
import nsw.base.core.dao.entity.Widget;
import nsw.base.core.service.SubsysConfigService;
import nsw.base.core.service.WidgetService;
import nsw.base.core.utils.Constants;
import nsw.base.core.utils.paging.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 服务提供者配置操作相关
 **/
@Controller
@RequestMapping(Constants.REST_SUBSYS_PREFIX)
public class SubsysConfigController extends BaseController {
	@Autowired
	SubsysConfigService subsysConfigService;
	@Autowired
	WidgetService widgetService;
	

	/**	
	 * 取得全部
	 */
	@RequestMapping(value = Constants.REST_SUBSYS_LIST)
	@ResponseBody
	public Pagination findUserPage(Pagination page, SubsysConfig subsysConfig){
		Pagination rePage = null;
		rePage = subsysConfigService.getSubsysConfigPage(page.getPageNumber(), page.getPageSize(),page.getPageSort(), subsysConfig);
		return rePage;
	}
	/**
	 * 修改
	 */
	@RequestMapping(value = Constants.REST_SUBSYS_EDIT)
	@ResponseBody
	public SubsysConfig edit(SubsysConfig subsysConfig){
		subsysConfigService.edit(subsysConfig);
		return subsysConfig;
	}
	/**
	 * 新增
	 */
	@RequestMapping(value = Constants.REST_SUBSYS_ADD)
	@ResponseBody
	public SubsysConfig add(SubsysConfig subsysConfig){
		if("".equals(subsysConfig.getId())){
			subsysConfig.setId(null);
		}
		subsysConfigService.add(subsysConfig);
		Widget widget = new Widget();
		widget.setCode(subsysConfig.getCode());
		widget.setName(subsysConfig.getName() + "菜单");
		widget.setType("menu");
		widget.setContainerId("systemmenu");
		widgetService.addWidget(widget);
		return subsysConfig;
	}
	/**
	 * 删除
	 */
	@RequestMapping(value = Constants.REST_SUBSYS_DEL)
	@ResponseBody
	public SubsysConfig del(SubsysConfig subsysConfig){
		//查看是否还有菜单控件
		Widget widget = new Widget();
		widget.setCode(subsysConfig.getCode());
		List<Widget> widgetList = widgetService.getWidgetByCondition(widget);
		for(Widget currWidget : widgetList ){
			//如果当前菜单有内容，结束删除子系统
			if(currWidget.getElements() != null && currWidget.getElements().size() > 0){
				return null;
			}else{
				//删除菜单控件
				widgetService.delWidget(currWidget);
			}
		}
		subsysConfigService.del(subsysConfig.getId());
		return subsysConfig;
	}
	
}
