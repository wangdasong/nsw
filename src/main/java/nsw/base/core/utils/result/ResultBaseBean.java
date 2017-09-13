package nsw.base.core.utils.result;

public abstract class ResultBaseBean<T> {
	public static final int RESULT_STATUS_SUCCESS = 0;
	public static final int RESULT_STATUS_FAILED = 1;

	int status;
	String msg;
	T data;

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}
