package org.jfw.web.servlet;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import javax.servlet.ServletException;

import org.jfw.util.bean.AfterBeanFactory;
import org.jfw.util.bean.BeanFactory;
import org.jfw.util.io.IoUtil;
import org.jfw.util.web.WebHandlerContext;
import org.jfw.util.web.model.WebRequestEntry;

public class FilenameConfigServlet extends BaseServlet {

	private static final long serialVersionUID = 4188897681050379687L;
	public static final String JFW_MVC_GROUPNAME = "jfwmvc";
	public static final String BEAN_FAC_FILE_NAME = "configFileName";

	public static final String AFTER_BEANFACTORY = "afterBeanFactory";

	protected String configFileName = "beanConfig.properties";

	protected BeanFactory bf = null;

	protected Properties config;

	protected void buildBeanFactoryConfig() {
		String tmp = this.getServletConfig().getInitParameter(BEAN_FAC_FILE_NAME);
		if (tmp != null && tmp.trim().length() > 0)
			this.configFileName = tmp.trim();
		try {

			InputStream in = this.getClass().getClassLoader().getResourceAsStream(configFileName);
			try {
				if (in == null)
					return;

				config = new Properties();
				config.load(in);
			} finally {
				IoUtil.close(in);
			}
		} catch (Throwable th) {
			this.config = null;
			this.log(FilenameConfigServlet.class.getName() + ":build beanfactory config with file[" + configFileName
					+ "] error", th);

		}

	}

	protected void buildBeanFactory() {
		try {
			if (config != null)
				this.bf = BeanFactory.build(null, config);
		} catch (Throwable th) {
			this.bf = null;
			this.log(FilenameConfigServlet.class.getName() + ":build beanFactory error", th);
			failStart();
		}
	}

	protected void buildWebHandlers() {
		if (null == this.bf)
			return;
		List<String> list = this.bf.getBeanIdsWithGroup(JFW_MVC_GROUPNAME);
		if (list == null || list.isEmpty())
			return;
		for (String name : list) {
			Object obj = this.bf.getBean(name);
			if (obj instanceof WebRequestEntry) {
				if (!WebHandlerContext.addWebHandler((WebRequestEntry) obj)) {
					this.log(FilenameConfigServlet.class.getName() + ":bean[id=" + name + "] can't load as webHandler");
				}
			} else {
				this.log(FilenameConfigServlet.class.getName() + ":bean[id=" + name
						+ "] invalid org.jfw.web.model.WebRequestEntry");
			}
		}
	}

	public void doAfterBeanFactory() {
		if(null == this.bf) return ;
		String tmp = this.getServletConfig().getInitParameter(AFTER_BEANFACTORY);
		if (tmp == null || tmp.trim().length() == 0)
			return;
		tmp = tmp.trim();

		String[] cns = tmp.split("[,;]");
		LinkedList<AfterBeanFactory> list = new LinkedList<AfterBeanFactory>();
		try {
			for (int i = 0; i < cns.length; ++i) {
				if (cns[i] != null && cns[i].trim().length() > 0) {
					Class<?> cls = Class.forName(cns[i].trim());
					list.add((AfterBeanFactory) cls.newInstance());
				}
			}
		} catch (Throwable th) {
			this.log("create " + AfterBeanFactory.class.getName() + " instance error", th);
			failStart();
			return;
		}
		try {
			for (ListIterator<AfterBeanFactory> it = list.listIterator(); it.hasNext();) {
				it.next().handle(this.bf);
			}
		} catch (Throwable th) {
			this.log("invoke " + AfterBeanFactory.class.getName() + ".handle instance error", th);
			failStart();
			return;
		}

	}

	@Override
	public void init() throws ServletException {
		super.init();
		this.buildBeanFactoryConfig();
		this.buildBeanFactory();
		this.doAfterBeanFactory();
		this.buildWebHandlers();
	}

}
