package org.apache.shiro.web.servlet;
/*     */ import java.io.Serializable;
import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;

/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import javax.servlet.http.HttpSessionBindingEvent;
/*     */ import javax.servlet.http.HttpSessionBindingListener;
/*     */ import javax.servlet.http.HttpSessionContext;

/*     */ import org.apache.shiro.session.InvalidSessionException;
/*     */ import org.apache.shiro.session.Session;
/*     */ import org.apache.shiro.web.session.HttpServletSession;
/*     */ 
/*     */ public class ShiroHttpSession
/*     */   implements HttpSession, Serializable
/*     */ {
	private static final long serialVersionUID = 8330001977650909247L;
/*     */   public static final String DEFAULT_SESSION_ID_NAME = "JSESSIONID";
/*  47 */   private static final Enumeration EMPTY_ENUMERATION = new Enumeration() {
/*     */     public boolean hasMoreElements() {
/*  49 */       return false;
/*     */     }
/*     */ 
/*     */     public Object nextElement() {
/*  53 */       return null;
/*     */     }
/*  47 */   };
/*     */ 
/*  58 */   private static final HttpSessionContext HTTP_SESSION_CONTEXT = new HttpSessionContext()
/*     */   {
/*     */     public HttpSession getSession(String s) {
/*  61 */       return null;
/*     */     }
/*     */ 
/*     */     public Enumeration getIds() {
/*  65 */       return ShiroHttpSession.EMPTY_ENUMERATION;
/*     */     }
/*  58 */   };
/*     */ 
/*  69 */   protected ServletContext servletContext = null;
/*  70 */   protected HttpServletRequest currentRequest = null;
/*  71 */   protected Session session = null;
/*     */ 
/*     */   public ShiroHttpSession(Session session, HttpServletRequest currentRequest, ServletContext servletContext) {
/*  74 */     if (session instanceof HttpServletSession) {
/*  75 */       String msg = "Session constructor argument cannot be an instance of HttpServletSession.  This is enforced to prevent circular dependencies and infinite loops.";
/*     */ 
/*  77 */       throw new IllegalArgumentException(msg);
/*     */     }
/*  79 */     this.session = session;
/*  80 */     this.currentRequest = currentRequest;
/*  81 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */   public Session getSession() {
/*  85 */     return this.session;
/*     */   }
/*     */ 
/*     */   public long getCreationTime() {
/*     */     try {
/*  90 */       return getSession().getStartTimestamp().getTime();
/*     */     } catch (Exception e) {
/*  92 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getId() {
/*  97 */     return getSession().getId().toString();
/*     */   }
/*     */ 
/*     */   public long getLastAccessedTime() {
/* 101 */     return getSession().getLastAccessTime().getTime();
/*     */   }
/*     */ 
/*     */   public ServletContext getServletContext() {
/* 105 */     return this.servletContext;
/*     */   }
/*     */ 
/*     */   public void setMaxInactiveInterval(int i) {
/*     */     try {
/* 110 */       getSession().setTimeout(i * 1000);
/*     */     } catch (InvalidSessionException e) {
/* 112 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getMaxInactiveInterval() {
/*     */     try {
/* 118 */       return new Long(getSession().getTimeout() / 1000L).intValue();
/*     */     } catch (InvalidSessionException e) {
/* 120 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public HttpSessionContext getSessionContext()
/*     */   {
/* 126 */     return HTTP_SESSION_CONTEXT;
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String s) {
/*     */     try {
/* 131 */       return getSession().getAttribute(s);
/*     */     } catch (InvalidSessionException e) {
/* 133 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getValue(String s) {
/* 138 */     return getAttribute(s);
/*     */   }
/*     */ 
/*     */   protected Set<String> getKeyNames()
/*     */   {
/*     */     Collection keySet;
/*     */     try {
/* 145 */       keySet = getSession().getAttributeKeys();
/*     */     } catch (InvalidSessionException e) {
/* 147 */       throw new IllegalStateException(e);
/*     */     }
/*     */     Set keyNames;
/*     */     Iterator localIterator;
/* 150 */     if ((keySet != null) && (!(keySet.isEmpty()))) {
/* 151 */       keyNames = new HashSet(keySet.size());
/* 152 */       for (localIterator = keySet.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();
/* 153 */         keyNames.add(o.toString());
/*     */       }
/*     */     } else {
/* 156 */       keyNames = Collections.EMPTY_SET;
/*     */     }
/* 158 */     return keyNames;
/*     */   }
/*     */ 
/*     */   public Enumeration getAttributeNames() {
/* 162 */     Set keyNames = getKeyNames();
/* 163 */     final Iterator iterator = keyNames.iterator();
/* 164 */     return new Enumeration() {
/*     */       public boolean hasMoreElements() {
/* 166 */         return iterator.hasNext();
/*     */       }
/*     */ 
/*     */       public Object nextElement() {
/* 170 */         return iterator.next();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public String[] getValueNames() {
/* 176 */     Set keyNames = getKeyNames();
/* 177 */     String[] array = new String[keyNames.size()];
/* 178 */     if (keyNames.size() > 0) {
/* 179 */       array = (String[])keyNames.toArray(array);
/*     */     }
/* 181 */     return array;
/*     */   }
/*     */ 
/*     */   protected void afterBound(String s, Object o) {
/* 185 */     if (o instanceof HttpSessionBindingListener) {
/* 186 */       HttpSessionBindingListener listener = (HttpSessionBindingListener)o;
/* 187 */       HttpSessionBindingEvent event = new HttpSessionBindingEvent(this, s, o);
/* 188 */       listener.valueBound(event);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void afterUnbound(String s, Object o) {
/* 193 */     if (o instanceof HttpSessionBindingListener) {
/* 194 */       HttpSessionBindingListener listener = (HttpSessionBindingListener)o;
/* 195 */       HttpSessionBindingEvent event = new HttpSessionBindingEvent(this, s, o);
/* 196 */       listener.valueUnbound(event); }  } 
/*     */   // ERROR //
/*     */   public void setAttribute(String s, Object o) {} // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 10	org/apache/shiro/web/servlet/ShiroHttpSession:getSession	()Lorg/apache/shiro/session/Session;
/*     */     //   4: aload_1
/*     */     //   5: aload_2
/*     */     //   6: invokeinterface 53 3 0
/*     */     //   11: aload_0
/*     */     //   12: aload_1
/*     */     //   13: aload_2
/*     */     //   14: invokevirtual 54	org/apache/shiro/web/servlet/ShiroHttpSession:afterBound	(Ljava/lang/String;Ljava/lang/Object;)V
/*     */     //   17: goto +30 -> 47
/*     */     //   20: astore_3
/*     */     //   21: aload_0
/*     */     //   22: aload_1
/*     */     //   23: aload_2
/*     */     //   24: invokevirtual 55	org/apache/shiro/web/servlet/ShiroHttpSession:afterUnbound	(Ljava/lang/String;Ljava/lang/Object;)V
/*     */     //   27: new 14	java/lang/IllegalStateException
/*     */     //   30: dup
/*     */     //   31: aload_3
/*     */     //   32: invokespecial 15	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   35: athrow
/*     */     //   36: astore 4
/*     */     //   38: new 14	java/lang/IllegalStateException
/*     */     //   41: dup
/*     */     //   42: aload_3
/*     */     //   43: invokespecial 15	java/lang/IllegalStateException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   46: athrow
/*     */     //   47: return
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	17	20	org/apache/shiro/session/InvalidSessionException
/*     */     //   21	27	36	finally
/*     */     //   36	38	36	finally } 
/*     */   public void putValue(String s, Object o) { setAttribute(s, o);
/*     */   }
/*     */ 
/*     */   public void removeAttribute(String s) {
/*     */     try {
/* 221 */       Object attribute = getSession().removeAttribute(s);
/* 222 */       afterUnbound(s, attribute);
/*     */     } catch (InvalidSessionException e) {
/* 224 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeValue(String s) {
/* 229 */     removeAttribute(s);
/*     */   }
/*     */ 
/*     */   public void invalidate() {
/*     */     try {
/* 234 */       getSession().stop();
/*     */     } catch (InvalidSessionException e) {
/* 236 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isNew() {
/* 241 */     Boolean value = (Boolean)this.currentRequest.getAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW);
/* 242 */     return ((value != null) && (value.equals(Boolean.TRUE)));
/*     */   }
/*     */ }