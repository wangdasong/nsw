package nsw.base.core.utils.redis;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class RedisReqRep implements Serializable {
	private static final long serialVersionUID = 3598901357217228065L;
	//请求ID确定请求的唯一主键
	private String requestId;
	//消费者ID，决定哪个服务消费者来接收Response
	private String consumerId;
	//提供者ID，决定哪个服务提供者来接收request
	private String providerId;
	//传递的请求数据
	private HttpServletRequest request;
	//传递的请求数据
	private HttpServletResponse response;
	//返回的json数据
	private ModelAndView mv;
	
	private Boolean dealFlag = false;
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getConsumerId() {
		return consumerId;
	}
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}
	public String getProviderId() {
		return providerId;
	}
	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public Boolean getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(Boolean dealFlag) {
		this.dealFlag = dealFlag;
	}
	public ModelAndView getMv() {
		return mv;
	}
	public void setMv(ModelAndView mv) {
		this.mv = mv;
	}
	
}
