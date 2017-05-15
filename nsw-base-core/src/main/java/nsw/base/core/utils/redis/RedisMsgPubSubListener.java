package nsw.base.core.utils.redis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nsw.base.core.utils.CommonUtils;
import nsw.base.core.utils.ThreadVariable;

import org.springframework.web.servlet.DispatcherProviderServlet;
import org.springframework.web.servlet.ModelAndView;

import redis.clients.jedis.JedisPubSub;

public class RedisMsgPubSubListener extends JedisPubSub {
	private org.springframework.web.servlet.DispatcherProviderServlet dispatcherProviderServlet;
	public RedisMsgPubSubListener(DispatcherProviderServlet dispatcherProviderServlet){
		this.dispatcherProviderServlet = dispatcherProviderServlet;
	}
	public RedisMsgPubSubListener(){
	}
	@Override  
    public void onMessage(String channel, String requestId) { 
		//取得请求队列里面的request
		byte[] bytes = JedisUtil.get(requestId.getBytes());
		try {
			RedisReqRep redisReqRep = (RedisReqRep) CommonUtils.bytes2Object(bytes);
			String consumerId = redisReqRep.getConsumerId();
			//判断是否是处理过的请求
			HttpServletRequest request = redisReqRep.getRequest();
			HttpServletResponse response = redisReqRep.getResponse();
			if(redisReqRep.getDealFlag()){
				//此处将response返回给浏览器
				
				//销毁请求
				JedisUtil.del(requestId.getBytes());
				RedisMsgPubSubListener listener = ThreadVariable.getRedisMsgPubSubListenerVariable();
				listener.unsubscribe();
				return;
			}
			//呼叫springMVC
			ModelAndView mv = this.dispatcherProviderServlet.getModelAndView(request, response);
			redisReqRep.setMv(mv);
			redisReqRep.setDealFlag(true);
			//返回处理结果
			JedisUtil.set(requestId.getBytes(), CommonUtils.object2Bytes(redisReqRep));
			JedisUtil.publish(consumerId, requestId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//发布redis订阅消息返回给消费服务
		
        System.out.println("channel:" + channel + "receives requestID :" + requestId);  
        //this.unsubscribe();//取消订阅  
    }  
  
    @Override  
    public void onSubscribe(String channel, int subscribedChannels){
        System.out.println("channel:" + channel + "is been subscribed:" + subscribedChannels);
    }  
  
    @Override  
    public void onUnsubscribe(String channel, int subscribedChannels) {  
        System.out.println("channel:" + channel + "is been unsubscribed:" + subscribedChannels);  
    }  
}
