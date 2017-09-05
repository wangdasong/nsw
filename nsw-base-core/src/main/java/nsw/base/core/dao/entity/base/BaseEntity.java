package nsw.base.core.dao.entity.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nsw.base.core.utils.JsonDateSerializer;

import org.codehaus.jackson.map.annotate.JsonSerialize;


public abstract class BaseEntity implements Serializable, BaseDbDialogConfig {

	private static final long serialVersionUID = 6172357500773556079L;

	protected String id;

	protected String createUserId;

	protected String updateUserId;

	protected Date createDate;
	protected Date createDateStart;
	protected Date createDateEnd;

	protected Date updateDate;
	protected Date updateDateStart;
	protected Date updateDateEnd;
	
	protected Date deleteDate;
	protected Date deleteDateStart;
	protected Date deleteDateEnd;
	
	String entityName;

	List<String> errors;

	public BaseEntity() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public void addError(String error) {
		if(this.errors == null){
			this.errors = new ArrayList<String>();
		}
		this.errors.add(error);
	}
	public boolean getHasError() {
		return errors != null;
	}
	@JsonSerialize(using=JsonDateSerializer.class) 
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonSerialize(using=JsonDateSerializer.class) 
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return Date
	 */
	@JsonSerialize(using=JsonDateSerializer.class) 
	public Date getDeleteDate() {
		return deleteDate;
	}

	/**
	 * @param deleteDate
	 */
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	@JsonSerialize(using=JsonDateSerializer.class) 
	public Date getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(Date createDateStart) {
		this.createDateStart = createDateStart;
	}

	@JsonSerialize(using=JsonDateSerializer.class) 
	public Date getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(Date createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	@JsonSerialize(using=JsonDateSerializer.class) 
	public Date getUpdateDateStart() {
		return updateDateStart;
	}

	public void setUpdateDateStart(Date updateDateStart) {
		this.updateDateStart = updateDateStart;
	}

	@JsonSerialize(using=JsonDateSerializer.class) 
	public Date getUpdateDateEnd() {
		return updateDateEnd;
	}

	public void setUpdateDateEnd(Date updateDateEnd) {
		this.updateDateEnd = updateDateEnd;
	}

	@JsonSerialize(using=JsonDateSerializer.class) 
	public Date getDeleteDateStart() {
		return deleteDateStart;
	}

	public void setDeleteDateStart(Date deleteDateStart) {
		this.deleteDateStart = deleteDateStart;
	}

	@JsonSerialize(using=JsonDateSerializer.class) 
	public Date getDeleteDateEnd() {
		return deleteDateEnd;
	}

	public void setDeleteDateEnd(Date deleteDateEnd) {
		this.deleteDateEnd = deleteDateEnd;
	}

	@Override
	public String getDbDialog() {
		return dbDialog;
	}
}
