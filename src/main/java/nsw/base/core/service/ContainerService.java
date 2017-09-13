package nsw.base.core.service;

import java.util.List;

import nsw.base.core.dao.entity.Container;

public interface ContainerService {
	public List<Container> getSubContainers(String containerId);
	public Container getContainerById(String containerId);
	public void editContainer(Container container);
	public void addContainer(Container container);
	public void delContainer(Container container);
	public Container saveOrUpdateContainer(Container container);
}
