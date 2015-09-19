package org.jfw.core.code.webmvc.handler.buildParam;

import java.lang.reflect.Type;

import org.jfw.core.code.generator.annotations.webmvc.Controller;
import org.jfw.core.code.generator.annotations.webmvc.PathVar;
import org.jfw.core.code.utils.Utils;
import org.jfw.core.code.webmvc.ControllerMethodCodeGenerator;
import org.jfw.core.code.webmvc.handler.BuildParamHandler;

public class PathVarHandler extends BuildParamHandler.BuildParameter {

	private int getIndexInPath(String name, String path) {
		String pathL = "{" + name.trim() + "}";
		if (!path.startsWith("/"))
			path = "/" + path;
		String[] paths = path.split("/");
		for (int i = 1; i < paths.length; ++i) {
			if (pathL.equals(paths[i])) {
				return i;
			}
		}
		return -1;
	}

	private void appendTransfer(StringBuilder sb ,Class<?> cls,PathVar pv,int pathIndex){
		if (cls == int.class) {
			sb.append("Integer.parseInt(uriArray[").append(pathIndex).append("]);");
		} else if (cls == Integer.class) {
			sb.append("Integer.valueof(uriArray[").append(pathIndex).append("]);");
		} else if (cls == byte.class) {
			sb.append("Byte.parseByte(uriArray[").append(pathIndex).append("]);");
		} else if (cls == Byte.class) {
			sb.append("Byte.valueof(uriArray[").append(pathIndex).append("]);");
		} else if (cls == short.class) {
			sb.append("Short.parseShort(uriArray[").append(pathIndex).append("]);");
		} else if (cls == Short.class) {
			sb.append("Short.valueof(uriArray[").append(pathIndex).append("]);");
		} else if (cls == long.class) {
			sb.append("Long.parseLong(uriArray[").append(pathIndex).append("]);");
		} else if (cls == Long.class) {
			sb.append("Long.valueof(uriArray[").append(pathIndex).append("]);");
		} else if (cls == double.class) {
			sb.append("Double.parseDouble(uriArray[").append(pathIndex).append("]);");
		} else if (cls == Double.class) {
			sb.append("Double.valueof(uriArray[").append(pathIndex).append("]);");
		} else if (cls == float.class) {
			sb.append("Float.parseFloat(uriArray[").append(pathIndex).append("]);");
		} else if (cls == Float.class) {
			sb.append("Float.valueof(uriArray[").append(pathIndex).append("]);");
		} else if (cls == boolean.class || cls == Boolean.class) {
			sb.append("\"1\".equals(uriArray[").append(pathIndex)
					.append("])|| \"true\".equalsIgnoreCase(uriArray[").append(pathIndex)
					.append("])||\"yes\".equalsIgnoreCase(uriArray[").append(pathIndex).append("])");
		} else if (cls == String.class) {
			if (pv.encoding()) {
				sb.append("java.net.URLDecoder.decode(uriArray[").append(pathIndex).append("],\"UTF-8\");");
			} else {
				sb.append("uriArray[").append(pathIndex).append("]");
			}
		} else if (cls == java.math.BigInteger.class) {
			sb.append("java.math.BigInteger.valueof(uriArray[").append(pathIndex).append("]);");
		} else if (cls == java.math.BigDecimal.class) {
			sb.append("java.math.BigDecimal.valueof(uriArray[").append(pathIndex).append("]);");
		} else {
			throw new IllegalArgumentException("不支持的类型：" + cls.getName());
		}
	}
	@Override
	public void build(StringBuilder sb, int index, Type type, ControllerMethodCodeGenerator cmcg, Object annotation) {
		PathVar pv = (PathVar) annotation;
		cmcg.readURI(pv.pathAttribute());
		if (pv.value() == null || (0 == pv.value().trim().length())) {
			throw new RuntimeException("@PathVar没有指定value");
		}
		String path = cmcg.getMethod().getAnnotation(Controller.class).value().trim();
		int pathIndex = getIndexInPath(pv.value().trim(), path);
		if (pathIndex <= 0)
			throw new RuntimeException("@PathVar指定的参数【" + pv.value().trim() + "】不存在于@Controller定义的URL【" + path + "】中 ");
		if (type instanceof Class<?>) {
			Class<?> cls = (Class<?>) type;
			Utils.writeNameOfType(type, sb);
			sb.append(" param").append(index).append(" =");
			this.appendTransfer(sb, cls, pv, pathIndex);
		} else {
			StringBuilder sss = new StringBuilder();
			Utils.writeNameOfType(type, sss);
			throw new RuntimeException("@PathVar 不支持的参数类型：" + sss.toString());
		}
	}

}
