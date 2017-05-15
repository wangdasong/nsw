package nsw.base.core.shiro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import nsw.base.core.utils.redis.RedisDb;
import nsw.base.core.utils.redis.RedisProviderRouter;

import org.apache.commons.lang.SerializationUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

public class SessionRedisDao extends AbstractSessionDAO {

    // 创建session，保存到数据库
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);    
        this.assignSessionId(session, sessionId);  
        this.saveSession(session);  
        return sessionId;
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
    	 if(sessionId == null){
             return null;  
         }
    	 Session session = null; 
         if(session == null){
             byte[] bytes = RedisDb.getObject(sessionId.toString().getBytes());
             if(bytes != null && bytes.length > 0){
                 session = byteToSession(bytes); 
             }
         }
         return session;
    }

    // 把session对象转化为byte保存到redis中
    public byte[] sessionToByte(Session session){
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(session);
            bytes = bo.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
    
    // 把byte还原为session
    public Session byteToSession(byte[] bytes){
        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
        ObjectInputStream in;
        SimpleSession session = null;
        try {
            in = new ObjectInputStream(bi);
            session = (SimpleSession) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return session;
    }

	@Override
	public void update(Session paramSession) throws UnknownSessionException {
		// TODO Auto-generated method stub
		 this.saveSession(paramSession);
	}
	  
    /** 
     * save session 
     * @param session 
     * @throws UnknownSessionException 
     */  
    private void saveSession(Session session) throws UnknownSessionException{  
        if(session == null || session.getId() == null){
            return;  
        }    
        RedisDb.setObject(session.getId().toString().getBytes(), sessionToByte(session), null);
    } 
    
	@Override
	public void delete(Session paramSession) {
		RedisDb.delString(paramSession.getId() + "");		
	}

	@Override
	public Collection<Session> getActiveSessions() { 
        Set<Session> sessions = new HashSet<Session>();
        Set<String> keys = RedisDb.getAllKeys("*");
        if(keys != null && keys.size() > 0){  
            for(String key : keys){  
            	if(RedisProviderRouter.REDIS_PROVIDERS_KEY.equals(key)){
            		continue;
            	}
                Session s = (Session)SerializationUtils.deserialize(RedisDb.getObject(key.getBytes()));  
                sessions.add(s);  
            }  
        }  
          
        return sessions;  
	}
    
}