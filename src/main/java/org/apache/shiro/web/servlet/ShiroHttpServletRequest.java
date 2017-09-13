package org.apache.shiro.web.servlet;

/*     */
/*     */import java.io.Serializable;
import java.security.Principal;

/*     */
import javax.servlet.ServletContext;
/*     */
import javax.servlet.http.HttpServletRequest;
/*     */
import javax.servlet.http.HttpServletRequestWrapper;
/*     */
import javax.servlet.http.HttpSession;

/*     */
import org.apache.shiro.SecurityUtils;
/*     */
import org.apache.shiro.session.Session;
/*     */
import org.apache.shiro.subject.Subject;
/*     */
import org.apache.shiro.subject.support.DisabledSessionException;
/*     */
import org.apache.shiro.web.util.WebUtils;

/*     */
/*     */public class ShiroHttpServletRequest extends
		HttpServletRequestWrapper implements Serializable
/*     */{
	private static final long serialVersionUID = 1772444031718519124L;
	/*     */public static final String COOKIE_SESSION_ID_SOURCE = "cookie";
	/*     */public static final String URL_SESSION_ID_SOURCE = "url";
	/*  49 */public static final String REFERENCED_SESSION_ID = ShiroHttpServletRequest.class
			.getName() + "_REQUESTED_SESSION_ID";
	/*  50 */public static final String REFERENCED_SESSION_ID_IS_VALID = ShiroHttpServletRequest.class
			.getName() + "_REQUESTED_SESSION_ID_VALID";
	/*  51 */public static final String REFERENCED_SESSION_IS_NEW = ShiroHttpServletRequest.class
			.getName() + "_REFERENCED_SESSION_IS_NEW";
	/*  52 */public static final String REFERENCED_SESSION_ID_SOURCE = ShiroHttpServletRequest.class
			.getName() + "REFERENCED_SESSION_ID_SOURCE";
	/*  53 */public static final String IDENTITY_REMOVED_KEY = ShiroHttpServletRequest.class
			.getName() + "_IDENTITY_REMOVED_KEY";
	/*  54 */public static final String SESSION_ID_URL_REWRITING_ENABLED = ShiroHttpServletRequest.class
			.getName() + "_SESSION_ID_URL_REWRITING_ENABLED";
	/*     */
	/*  56 */protected ServletContext servletContext = null;
	/*     */
	/*  58 */protected HttpSession session = null;
	/*  59 */protected boolean httpSessions = true;

	/*     */
	/*     */public ShiroHttpServletRequest(HttpServletRequest wrapped,
			ServletContext servletContext, boolean httpSessions) {
		/*  62 */super(wrapped);
		/*  63 */this.servletContext = servletContext;
		/*  64 */this.httpSessions = httpSessions;
		/*     */}

	/*     */
	/*     */public boolean isHttpSessions() {
		/*  68 */return this.httpSessions;
		/*     */}

	/*     */
	/*     */public String getRemoteUser()
	/*     */{
		/*  73 */Object scPrincipal = getSubjectPrincipal();
		/*     */String remoteUser;
		/*  74 */if (scPrincipal != null) {
			/*  75 */if (scPrincipal instanceof String)
				/*  76 */return ((String) scPrincipal);
			/*  77 */if (scPrincipal instanceof Principal)
				/*  78 */remoteUser = ((Principal) scPrincipal).getName();
			/*     */else
				/*  80 */remoteUser = scPrincipal.toString();
			/*     */}
		/*     */else {
			/*  83 */remoteUser = super.getRemoteUser();
			/*     */}
		/*  85 */return remoteUser;
		/*     */}

	/*     */
	/*     */protected Subject getSubject() {
		/*  89 */return SecurityUtils.getSubject();
		/*     */}

	/*     */
	/*     */protected Object getSubjectPrincipal() {
		/*  93 */Object userPrincipal = null;
		/*  94 */Subject subject = getSubject();
		/*  95 */if (subject != null) {
			/*  96 */userPrincipal = subject.getPrincipal();
			/*     */}
		/*  98 */return userPrincipal;
		/*     */}

	/*     */
	/*     */public boolean isUserInRole(String s) {
		/* 102 */Subject subject = getSubject();
		/* 103 */boolean inRole = (subject != null) && (subject.hasRole(s));
		/* 104 */if (!(inRole)) {
			/* 105 */inRole = super.isUserInRole(s);
			/*     */}
		/* 107 */return inRole;
		/*     */}

	/*     */
	/*     */public Principal getUserPrincipal()
	/*     */{
		/* 112 */Object scPrincipal = getSubjectPrincipal();
		/*     */Principal userPrincipal;
		/* 113 */if (scPrincipal != null)
		/*     */{
			/* 114 */if (scPrincipal instanceof Principal)
				/* 115 */userPrincipal = (Principal) scPrincipal;
			/*     */else
				/* 117 */userPrincipal = new ObjectPrincipal(scPrincipal);
			/*     */}
		/*     */else {
			/* 120 */userPrincipal = super.getUserPrincipal();
			/*     */}
		/* 122 */return userPrincipal;
		/*     */}

	/*     */
	/*     */public String getRequestedSessionId() {
		/* 126 */String requestedSessionId = null;
		/* 127 */if (isHttpSessions()) {
			/* 128 */requestedSessionId = super.getRequestedSessionId();
			/*     */} else {
			/* 130 */Object sessionId = getAttribute(REFERENCED_SESSION_ID);
			/* 131 */if (sessionId != null) {
				/* 132 */requestedSessionId = sessionId.toString();
				/*     */}
			/*     */}
		/*     */
		/* 136 */return requestedSessionId;
		/*     */}

	/*     */
	/*     */public HttpSession getSession(boolean create)
	/*     */{
		/*     */HttpSession httpSession;
		/* 143 */if (isHttpSessions()) {
			/* 144 */httpSession = super.getSession(false);
			/* 145 */if ((httpSession == null) && (create))
			/*     */{
				/* 147 */if (WebUtils._isSessionCreationEnabled(this))
					/* 148 */httpSession = super.getSession(create);
				/*     */else
					/* 150 */throw newNoSessionCreationException();
				/*     */}
			/*     */}
		/*     */else {
			/* 154 */if (this.session == null)
			/*     */{
				/* 156 */boolean existing = getSubject().getSession(false) != null;
				/*     */
				/* 158 */Session shiroSession = getSubject()
						.getSession(create);
				/* 159 */if (shiroSession != null) {
					/* 160 */this.session = new ShiroHttpSession(shiroSession,
							this, this.servletContext);
					/* 161 */if (!(existing)) {
						/* 162 */setAttribute(REFERENCED_SESSION_IS_NEW,
								Boolean.TRUE);
						/*     */}
					/*     */}
				/*     */}
			/* 166 */httpSession = this.session;
			/*     */}
		/*     */
		/* 169 */return httpSession;
		/*     */}

	/*     */
	/*     */private DisabledSessionException newNoSessionCreationException()
	/*     */{
		/* 183 */String msg = "Session creation has been disabled for the current request.  This exception indicates that there is either a programming error (using a session when it should never be used) or that Shiro's configuration needs to be adjusted to allow Sessions to be created for the current request.  See the "
				+ DisabledSessionException.class
				/* 183 */.getName() + " JavaDoc for more.";
		/*     */
		/* 185 */return new DisabledSessionException(msg);
		/*     */}

	/*     */
	/*     */public HttpSession getSession() {
		/* 189 */return getSession(true);
		/*     */}

	/*     */
	/*     */public boolean isRequestedSessionIdValid() {
		/* 193 */if (isHttpSessions()) {
			/* 194 */return super.isRequestedSessionIdValid();
			/*     */}
		/* 196 */Boolean value = (Boolean) getAttribute(REFERENCED_SESSION_ID_IS_VALID);
		/* 197 */return ((value != null) && (value.equals(Boolean.TRUE)));
		/*     */}

	/*     */
	/*     */public boolean isRequestedSessionIdFromCookie()
	/*     */{
		/* 202 */if (isHttpSessions()) {
			/* 203 */return super.isRequestedSessionIdFromCookie();
			/*     */}
		/* 205 */String value = (String) getAttribute(REFERENCED_SESSION_ID_SOURCE);
		/* 206 */return ((value != null) && (value.equals("cookie")));
		/*     */}

	/*     */
	/*     */public boolean isRequestedSessionIdFromURL()
	/*     */{
		/* 211 */if (isHttpSessions()) {
			/* 212 */return super.isRequestedSessionIdFromURL();
			/*     */}
		/* 214 */String value = (String) getAttribute(REFERENCED_SESSION_ID_SOURCE);
		/* 215 */return ((value != null) && (value.equals("url")));
		/*     */}

	/*     */
	/*     */public boolean isRequestedSessionIdFromUrl()
	/*     */{
		/* 220 */return isRequestedSessionIdFromURL();
		/*     */}

	/*     */
	/*     */private class ObjectPrincipal implements Principal {
		/* 224 */private Object object = null;

		/*     */
		/*     */public ObjectPrincipal(Object paramObject) {
			/* 227 */this.object = paramObject;
			/*     */}

		/*     */
		/*     */public Object getObject() {
			/* 231 */return this.object;
			/*     */}

		/*     */
		/*     */public String getName() {
			/* 235 */return getObject().toString();
			/*     */}

		/*     */
		/*     */public int hashCode() {
			/* 239 */return this.object.hashCode();
			/*     */}

		/*     */
		/*     */public boolean equals(Object o) {
			/* 243 */if (o instanceof ObjectPrincipal) {
				/* 244 */ObjectPrincipal op = (ObjectPrincipal) o;
				/* 245 */return getObject().equals(op.getObject());
				/*     */}
			/* 247 */return false;
			/*     */}

		/*     */
		/*     */public String toString() {
			/* 251 */return this.object.toString();
			/*     */}
		/*     */
	}
	/*     */
}