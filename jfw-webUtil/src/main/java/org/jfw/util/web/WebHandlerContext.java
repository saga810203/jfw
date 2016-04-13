package org.jfw.util.web;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfw.util.web.model.WebRequestEntry;

public class WebHandlerContext {
	private static final Class<?>[] CTRL_METHOD_PARAM_TYPE = new Class<?>[] { HttpServletRequest.class,
			HttpServletResponse.class, int.class };

	public static final String REQ_MATCH_URI = "org.jfw.web.reqMacthUri";
	public static final String REQ_MATCH_URI_DYN = "org.jfw.web.reqMacthUri_DYN";
	public static final String CONF_FILE = "jfw_web_dispatcher.properties";
	public static final String REQ_VIEW_TYPE = "org.jfw.web.reqViewType";

	public static final Map<String, ControllerMethod> getStaticUrls = new HashMap<String, ControllerMethod>();
	public static final Map<String, ControllerMethod> postStaticUrls = new HashMap<String, ControllerMethod>();
	public static final Map<String, ControllerMethod> deleteStaticUrls = new HashMap<String, ControllerMethod>();
	public static final Map<String, ControllerMethod> putStaticUrls = new HashMap<String, ControllerMethod>();

	private static final ControllerMethod[][] getDynamicUrls = new ControllerMethod[100][0];
	private static final ControllerMethod[][] postDynamicUrls = new ControllerMethod[100][0];
	private static final ControllerMethod[][] deleteDynamicUrls = new ControllerMethod[100][0];
	private static final ControllerMethod[][] putDynamicUrls = new ControllerMethod[100][0];

	private static String[] matchDynamicUrl(String url) {
		String[] result = WebUtil.splitUri(url);
		boolean dynamic = false;
		for (int i = 0; i < result.length; ++i) {
			String pathPart = result[i];
			if (pathPart.startsWith("{") && pathPart.endsWith("}")) {
				dynamic = true;
				result[i] = null;
			}
		}
		if (dynamic)
			return result;
		return null;
	}

	public static ControllerMethod[] extendArray(ControllerMethod[] cms) {
		if (cms == null || cms.length == 0) {
			return new ControllerMethod[1];
		}
		ControllerMethod[] result = new ControllerMethod[cms.length + 1];
		System.arraycopy(cms, 0, result, 0, cms.length);
		return result;
	}

	public static boolean addWebHandler(WebRequestEntry wre) {
		Method method = null;
		try {
			method = wre.getWebHandler().getClass().getMethod(wre.getMethodName(), CTRL_METHOD_PARAM_TYPE);
		} catch (Exception e) {
			return false;
		}

		String url = wre.getUri().trim().substring(1).intern();

		String[] dynamicUrl = matchDynamicUrl(url);
		Object handler = wre.getWebHandler();
		if (dynamicUrl == null) {
			ControllerMethod cm = new ControllerMethod(handler, method);
			if (wre.getMethodName().equalsIgnoreCase("GET")) {
				getStaticUrls.put(url, cm);
			} else if (wre.getMethodName().equalsIgnoreCase("POST")) {
				postStaticUrls.put(url, cm);
			} else if (wre.getMethodName().equalsIgnoreCase("DELETE")) {
				deleteStaticUrls.put(url, cm);
			} else if (wre.getMethodName().equalsIgnoreCase("PUT")) {
				putStaticUrls.put(url, cm);
			} else {
				return false;
			}
		} else {
			ControllerMethod cm = new ControllerMethod(handler, method, dynamicUrl);
			if (wre.getMethodName().equalsIgnoreCase("GET")) {
				ControllerMethod[] cms = extendArray(getDynamicUrls[dynamicUrl.length]);
				cms[cms.length - 1] = cm;
				getDynamicUrls[dynamicUrl.length] = cms;
			} else if (wre.getMethodName().equalsIgnoreCase("POST")) {
				ControllerMethod[] cms = extendArray(postDynamicUrls[dynamicUrl.length]);
				cms[cms.length - 1] = cm;
				postDynamicUrls[dynamicUrl.length] = cms;
			} else if (wre.getMethodName().equalsIgnoreCase("DELETE")) {
				ControllerMethod[] cms = extendArray(deleteDynamicUrls[dynamicUrl.length]);
				cms[cms.length - 1] = cm;
				deleteDynamicUrls[dynamicUrl.length] = cms;
			} else if (wre.getMethodName().equalsIgnoreCase("PUT")) {
				ControllerMethod[] cms = extendArray(putDynamicUrls[dynamicUrl.length]);
				cms[cms.length - 1] = cm;
				putDynamicUrls[dynamicUrl.length] = cms;
			} else {
				return false;
			}
		}
		return true;
	}

	public static ControllerMethod findWithGetMethod(HttpServletRequest req, int prefixLen) {
		String uri = WebUtil.normalize(req.getRequestURI());
		uri = uri.substring(prefixLen);
		ControllerMethod result = getStaticUrls.get(uri);
		if (null != result) {
			req.setAttribute(REQ_MATCH_URI, uri);
			return result;
		}
		String[] uripart = WebUtil.splitUri(uri);
		ControllerMethod[] cms = getDynamicUrls[uripart.length];
		for (ControllerMethod cm : cms) {
			if (cm.match(uripart)) {
				req.setAttribute(REQ_MATCH_URI_DYN, uripart);
				return cm;
			}
		}
		return null;
	}

	public static ControllerMethod findWithPostMethod(HttpServletRequest req, int prefixLen) {
		String uri = WebUtil.normalize(req.getRequestURI());
		uri = uri.substring(prefixLen);
		ControllerMethod result = postStaticUrls.get(uri);
		if (null != result) {
			req.setAttribute(REQ_MATCH_URI, uri);
			return result;
		}
		String[] uripart = WebUtil.splitUri(uri);
		ControllerMethod[] cms = postDynamicUrls[uripart.length];
		for (ControllerMethod cm : cms) {
			if (cm.match(uripart)) {
				req.setAttribute(REQ_MATCH_URI_DYN, uripart);
				return cm;
			}
		}
		return null;
	}

	public static ControllerMethod findWithPutMethod(HttpServletRequest req, int prefixLen) {
		String uri = WebUtil.normalize(req.getRequestURI());
		uri = uri.substring(prefixLen);
		ControllerMethod result = putStaticUrls.get(uri);
		if (null != result) {
			req.setAttribute(REQ_MATCH_URI, uri);
			return result;
		}
		String[] uripart = WebUtil.splitUri(uri);
		ControllerMethod[] cms = putDynamicUrls[uripart.length];
		for (ControllerMethod cm : cms) {
			if (cm.match(uripart)) {
				req.setAttribute(REQ_MATCH_URI_DYN, uripart);
				return cm;
			}
		}
		return null;
	}

	public static ControllerMethod findWithDeleteMethod(HttpServletRequest req, int prefixLen) {
		String uri = WebUtil.normalize(req.getRequestURI());
		uri = uri.substring(prefixLen);
		ControllerMethod result = deleteStaticUrls.get(uri);
		if (null != result) {
			req.setAttribute(REQ_MATCH_URI, uri);
			return result;
		}
		String[] uripart = WebUtil.splitUri(uri);
		ControllerMethod[] cms = deleteDynamicUrls[uripart.length];
		for (ControllerMethod cm : cms) {
			if (cm.match(uripart)) {
				req.setAttribute(REQ_MATCH_URI_DYN, uripart);
				return cm;
			}
		}
		return null;
	}
	
	private static final ControllerMethod[] EMPTY_DCM=new ControllerMethod[0];
	private static void resetDynamicUrls(ControllerMethod[][] urls)
	{
		for(int i = 0 ; i < urls.length ; ++i)
		{
			urls[i]= EMPTY_DCM;
		}
	}
	public static void reset(){
		getStaticUrls.clear();
		postStaticUrls.clear();
		putStaticUrls.clear();
		deleteStaticUrls.clear();
		resetDynamicUrls(getDynamicUrls);
		resetDynamicUrls(postDynamicUrls);
		resetDynamicUrls(putDynamicUrls);
		resetDynamicUrls(deleteDynamicUrls);
		
	}

}
