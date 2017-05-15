package nsw.base.web.controller.webpage;

import nsw.base.core.dao.entity.AttConfig;
import nsw.base.core.service.AttConfigService;
import nsw.base.core.utils.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 属�?�操作相�?
 **/
@Controller
@RequestMapping(Constants.REST_ATT_CONFIG_PREFIX)
public class AttConfigController {
	@Autowired
	AttConfigService attConfigService;

	/**
	 * 修改
	 */
	@RequestMapping(value = Constants.REST_ATT_CONFIG_EDIT)
	@ResponseBody
	public AttConfig edit(AttConfig attConfig){
		attConfigService.editAttConfig(attConfig);
		return attConfig;
	}
	/**
	 * 新增
	 */
	@RequestMapping(value = Constants.REST_ATT_CONFIG_ADD)
	@ResponseBody
	public AttConfig add(AttConfig attConfig){
		attConfigService.addAttConfig(attConfig);
		return attConfig;
	}
	/**
	 * 删除
	 */
	@RequestMapping(value = Constants.REST_ATT_CONFIG_DEL)
	@ResponseBody
	public AttConfig del(AttConfig attConfig){
		attConfigService.delAttConfig(attConfig);
		return attConfig;
	}
}
