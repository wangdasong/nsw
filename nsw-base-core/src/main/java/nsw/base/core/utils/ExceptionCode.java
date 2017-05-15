package nsw.base.core.utils;


public enum ExceptionCode {
	
	/**
	 * 此enum可根据项目具体情况�?�增减�??
	 */
	USER_IS_NULL("USER_IS_NULL","您输入的user对象不可为空，请�?查�??"),
	PERMISSION_IS_NULL("PERMISSION_IS_NULL","您输入的permission对象不可为空，请�?查�??"),
	ROLE_IS_NULL("ROLE_IS_NULL","您输入的role对象不可为空，请�?查�??"),
	RESOURCE_IS_NULL("RESOURCE_IS_NULL","您输入的resource对象不可为空，请�?查�??"),
	ROLEPERMISSION_IS_NULL("ROLEPERMISSION_IS_NULL","您输入的RolePermission对象不可为空，请�?查�??"),
	USERNAME_IS_NULL("USERNAME_IS_NULL","您输入的username不可为空，请�?查�??"),
	PERMISSIONIDSET_IS_NULL("PERMISSIONIDSET_IS_NULL","您输入的permissionIdSet(权限ID的Set集合)不可为空，请�?查�??"),
	RESOURCEIDSET_IS_NULL("RESOURCEIDSET_IS_NULL","您输入的资源ID的Set集合(resourceIdSet)不可为空，请�?查�??"),
	PERMISSIONID_IS_NULL("PERMISSIONID_IS_NULL","您输入的permissionId(权限ID)不可为空，请�?查�??"),
	ROLEID_IS_NULL("ROLEID_IS_NULL","您输入的roleId(角色ID)不可为空，请�?查�??"),
	RESOURCEID_IS_NULL("RESOURCEID_IS_NULL","您输入的resourceId(资源ID)不可为空，请�?查�??"),
	//USERID_IS_NULL("USERID_IS_NULL","您输入的userid不可为空，请�?查�??"),
	LIST_IS_NULL("LIST_IS_NULL","您传入的List不可为空，请�?查�??");
	
	
	private final String code;
	private final String msg;
	
	ExceptionCode(String code,String msg){
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
