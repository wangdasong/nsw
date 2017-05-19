package nsw.base.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import nsw.base.core.dao.entity.SubsysConfig;
import nsw.base.core.dao.entity.base.DataSrcEntity;
import nsw.base.core.dao.persistence.SubsysConfigMapper;
import nsw.base.core.dao.persistence.base.BaseDaoMapper;
import nsw.base.core.service.SubsysConfigService;
import nsw.base.core.service.base.DataSrcService;
import nsw.base.core.service.impl.base.BaseServiceImpl;
import nsw.base.core.utils.ThreadVariable;
import nsw.base.core.utils.paging.Pagination;
import nsw.base.core.utils.paging.TablePagingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubsysConfigServiceImpl extends BaseServiceImpl<SubsysConfig> implements
	DataSrcService<SubsysConfig>, SubsysConfigService , TablePagingService{
	@Autowired
	SubsysConfigMapper subsysConfigMapper;

	@Override
	public BaseDaoMapper<SubsysConfig> getCurrDaoMapper() {
		return subsysConfigMapper;
	}

	@Override
	public void add(SubsysConfig subsysConfig) {
		subsysConfigMapper.save(subsysConfig);
	}

	@Override
	public void edit(SubsysConfig subsysConfig) {
		subsysConfigMapper.update(subsysConfig);
	}

	@Override
	public void del(String scid) {
		subsysConfigMapper.deleteById(scid);
	}

	@Override
	public int getCount(Pagination page) {
		return subsysConfigMapper.getCount(page);
	}

	@Override
	public List<SubsysConfig> findPageData(Pagination page) {
		return subsysConfigMapper.findPageData(page);
	}

	@Override
	public Pagination getSubsysConfigPage(int pageNo, int size, String sort,
			SubsysConfig subsysConfig) {
		Pagination pr = new Pagination(pageNo, size, sort, this);
		try {
			pr.doPage(subsysConfig);
			if(pr.getList() != null ){
				for(SubsysConfig currSubsysConfig : (List<SubsysConfig>)pr.getList()){
					System.out.println("getSubsysConfigPage----"+ThreadVariable.getSubsysCodeVariable());
					if(currSubsysConfig.getCode().equals(ThreadVariable.getSubsysCodeVariable())){
						currSubsysConfig.setActive(true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pr;
	}

	@Override
	public List<DataSrcEntity> getDataSrcListByCondition(SubsysConfig t) {
		List<SubsysConfig> subsysConfigList = subsysConfigMapper.getAllData();
		List<DataSrcEntity> dataSrcEntityList = new ArrayList<DataSrcEntity>();
		for(SubsysConfig subsysConfig : subsysConfigList){
			DataSrcEntity currrDataSrcEntity = new DataSrcEntity();
			currrDataSrcEntity.setId(subsysConfig.getId());
			currrDataSrcEntity.setLabel(subsysConfig.getName());
			currrDataSrcEntity.setValue(subsysConfig.getCode());
			dataSrcEntityList.add(currrDataSrcEntity);
		}
		return dataSrcEntityList;
	}


}
