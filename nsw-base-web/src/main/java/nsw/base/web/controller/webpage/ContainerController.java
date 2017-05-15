package nsw.base.web.controller.webpage;

import nsw.base.core.dao.entity.Container;
import nsw.base.core.service.ContainerService;
import nsw.base.core.utils.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(Constants.REST_CONTAINER_PREFIX)
public class ContainerController {

	@Autowired
	ContainerService containerService;

	/**
	 * 保存容器数据
	 */
	@RequestMapping(value = Constants.REST_CONTAINER_SOU)
	@ResponseBody
	public Container saveOrUpdate(Container container){
		container = containerService.saveOrUpdateContainer(container);
		return container;
	}

	/**
	 * 删除容器数据
	 */
	@RequestMapping(value = Constants.REST_CONTAINER_DEL)
	@ResponseBody
	public Container del(Container container){
		containerService.delContainer(container);
		return container;
	}
}
