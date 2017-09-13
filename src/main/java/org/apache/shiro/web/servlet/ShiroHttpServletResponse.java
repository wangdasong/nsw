package org.apache.shiro.web.servlet;

/*     */
/*     */import java.io.IOException;
import java.io.Serializable;
/*     */
import java.net.MalformedURLException;
/*     */
import java.net.URL;
/*     */
import java.net.URLEncoder;

/*     */
import javax.servlet.ServletContext;
/*     */
import javax.servlet.http.HttpServletRequest;
/*     */
import javax.servlet.http.HttpServletResponse;
/*     */
import javax.servlet.http.HttpServletResponseWrapper;
/*     */
import javax.servlet.http.HttpSession;

/*     */
/*     */public class ShiroHttpServletResponse extends
		HttpServletResponseWrapper implements Serializable
/*     */{
	private static final long serialVersionUID = -5528044207462735600L;
	/*     */private static final String DEFAULT_SESSION_ID_PARAMETER_NAME = "JSESSIONID";
	/*  51 */private ServletContext context = null;
	/*     */
	/*  53 */private ShiroHttpServletRequest request = null;

	/*     */
	/*     */public ShiroHttpServletResponse(HttpServletResponse wrapped,
			ServletContext context, ShiroHttpServletRequest request) {
		/*  56 */super(wrapped);
		/*  57 */this.context = context;
		/*  58 */this.request = request;
		/*     */}

	/*     */
	/*     */public ServletContext getContext()
	/*     */{
		/*  63 */return this.context;
		/*     */}

	/*     */
	/*     */public void setContext(ServletContext context)
	/*     */{
		/*  68 */this.context = context;
		/*     */}

	/*     */
	/*     */public ShiroHttpServletRequest getRequest() {
		/*  72 */return this.request;
		/*     */}

	/*     */
	/*     */public void setRequest(ShiroHttpServletRequest request)
	/*     */{
		/*  77 */this.request = request;
		/*     */}

	/*     */
	/*     */public String encodeRedirectURL(String url)
	/*     */{
		/*  87 */if (isEncodeable(toAbsolute(url))) {
			/*  88 */return toEncoded(url, this.request.getSession().getId());
			/*     */}
		/*  90 */return url;
		/*     */}

	/*     */
	/*     */public String encodeRedirectUrl(String s)
	/*     */{
		/*  96 */return encodeRedirectURL(s);
		/*     */}

	/*     */
	/*     */public String encodeURL(String url)
	/*     */{
		/* 107 */String absolute = toAbsolute(url);
		/* 108 */if (isEncodeable(absolute))
		/*     */{
			/* 110 */if (url.equalsIgnoreCase("")) {
				/* 111 */url = absolute;
				/*     */}
			/* 113 */return toEncoded(url, this.request.getSession().getId());
			/*     */}
		/* 115 */return url;
		/*     */}

	/*     */
	/*     */public String encodeUrl(String s)
	/*     */{
		/* 120 */return encodeURL(s);
		/*     */}

	/*     */
	/*     */protected boolean isEncodeable(String location)
	/*     */{
		/* 140 */if (Boolean.FALSE
				.equals(this.request
						.getAttribute(ShiroHttpServletRequest.SESSION_ID_URL_REWRITING_ENABLED))) {
			/* 141 */return false;
			/*     */}
		/* 143 */if (location == null) {
			/* 144 */return false;
			/*     */}
		/*     */
		/* 147 */if (location.startsWith("#")) {
			/* 148 */return false;
			/*     */}
		/*     */
		/* 151 */HttpServletRequest hreq = this.request;
		/* 152 */HttpSession session = hreq.getSession(false);
		/* 153 */if (session == null)
			/* 154 */return false;
		/* 155 */if (hreq.isRequestedSessionIdFromCookie()) {
			/* 156 */return false;
			/*     */}
		/* 158 */return doIsEncodeable(hreq, session, location);
		/*     */}

	/*     */
	/*     */private boolean doIsEncodeable(HttpServletRequest hreq,
			HttpSession session, String location)
	/*     */{
		/*     */URL url;
		/*     */try {
			/* 165 */url = new URL(location);
			/*     */} catch (MalformedURLException e) {
			/* 167 */return false;
			/*     */}
		/* 171 */if (!(hreq.getScheme().equalsIgnoreCase(url.getProtocol())))
			/* 172 */return false;
		/* 173 */if (!(hreq.getServerName().equalsIgnoreCase(url.getHost())))
			/* 174 */return false;
		/* 175 */int serverPort = hreq.getServerPort();
		/* 176 */if (serverPort == -1) {
			/* 177 */if ("https".equals(hreq.getScheme()))
				/* 178 */serverPort = 443;
			/*     */else
				/* 180 */serverPort = 80;
			/*     */}
		/* 182 */int urlPort = url.getPort();
		/* 183 */if (urlPort == -1) {
			/* 184 */if ("https".equals(url.getProtocol()))
				/* 185 */urlPort = 443;
			/*     */else
				/* 187 */urlPort = 80;
			/*     */}
		/* 189 */if (serverPort != urlPort) {
			/* 190 */return false;
			/*     */}
		/* 192 */String contextPath = getRequest().getContextPath();
		/* 193 */if (contextPath != null) {
			/* 194 */String file = url.getFile();
			/* 195 */if ((file == null) || (!(file.startsWith(contextPath))))
				/* 196 */return false;
			/* 197 */String tok = new StringBuilder().append(";JSESSIONID=")
					.append(session.getId()).toString();
			/* 198 */if (file.indexOf(tok, contextPath.length()) >= 0) {
				/* 199 */return false;
				/*     */}
			/*     */}
		/*     */
		/* 203 */return true;
		/*     */}

	/*     */
	/*     */private String toAbsolute(String location)
	/*     */{
		/* 220 */if (location == null) {
			/* 221 */return location;
			/*     */}
		/* 223 */boolean leadingSlash = location.startsWith("/");
		/*     */
		/* 225 */if ((leadingSlash) || (!(hasScheme(location))))
		/*     */{
			/* 227 */StringBuilder buf = new StringBuilder();
			/*     */
			/* 229 */String scheme = this.request.getScheme();
			/* 230 */String name = this.request.getServerName();
			/* 231 */int port = this.request.getServerPort();
			/*     */try
			/*     */{
				/* 234 */buf.append(scheme).append("://").append(name);
				/* 235 */if (((scheme.equals("http")) && (port != 80)) || (
				/* 236 */(scheme
				/* 236 */.equals("https")) &&
				/* 236 */(port != 443))) {
					/* 237 */buf.append(':').append(port);
					/*     */}
				/* 239 */if (!(leadingSlash)) {
					/* 240 */String relativePath = this.request
							.getRequestURI();
					/* 241 */int pos = relativePath.lastIndexOf(47);
					/* 242 */relativePath = relativePath.substring(0, pos);
					/*     */
					/* 244 */String encodedURI = URLEncoder.encode(
							relativePath, getCharacterEncoding());
					/* 245 */buf.append(encodedURI).append('/');
					/*     */}
				/* 247 */buf.append(location);
				/*     */} catch (IOException e) {
				/* 249 */IllegalArgumentException iae = new IllegalArgumentException(
						location);
				/* 250 */iae.initCause(e);
				/* 251 */throw iae;
				/*     */}
			/*     */
			/* 254 */return buf.toString();
			/*     */}
		/*     */
		/* 257 */return location;
		/*     */}

	/*     */
	/*     */public static boolean isSchemeChar(char c)
	/*     */{
		/* 269 */return ((Character.isLetterOrDigit(c)) || (c == '+')
				|| (c == '-') || (c == '.'));
		/*     */}

	/*     */
	/*     */private boolean hasScheme(String uri)
	/*     */{
		/* 281 */int len = uri.length();
		/* 282 */for (int i = 0; i < len; ++i) {
			/* 283 */char c = uri.charAt(i);
			/* 284 */if (c == ':')
				/* 285 */return (i > 0);
			/* 286 */if (!(isSchemeChar(c))) {
				/* 287 */return false;
				/*     */}
			/*     */}
		/* 290 */return false;
		/*     */}

	/*     */
	/*     */protected String toEncoded(String url, String sessionId)
	/*     */{
		/* 302 */if ((url == null) || (sessionId == null)) {
			/* 303 */return url;
			/*     */}
		/* 305 */String path = url;
		/* 306 */String query = "";
		/* 307 */String anchor = "";
		/* 308 */int question = url.indexOf(63);
		/* 309 */if (question >= 0) {
			/* 310 */path = url.substring(0, question);
			/* 311 */query = url.substring(question);
			/*     */}
		/* 313 */int pound = path.indexOf(35);
		/* 314 */if (pound >= 0) {
			/* 315 */anchor = path.substring(pound);
			/* 316 */path = path.substring(0, pound);
			/*     */}
		/* 318 */StringBuilder sb = new StringBuilder(path);
		/* 319 */if (sb.length() > 0) {
			/* 320 */sb.append(";");
			/* 321 */sb.append("JSESSIONID");
			/* 322 */sb.append("=");
			/* 323 */sb.append(sessionId);
			/*     */}
		/* 325 */sb.append(anchor);
		/* 326 */sb.append(query);
		/* 327 */return sb.toString();
		/*     */}
	/*     */
}