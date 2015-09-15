package org.jfw.core.code.webmvc.handler;

import org.jfw.core.code.generator.annotations.webmvc.RemoveSessionAttribute;
import org.jfw.core.code.webmvc.Handler;

public class RemoveSessionAttributeHandler extends Handler{

    @Override
    public void appendBeforCode(StringBuilder sb) {
        RemoveSessionAttribute sa= this.getMethodAnnotation(RemoveSessionAttribute.class);
        if(null==sa || (this.getCmcg().getMethod().getReturnType()==void.class))return;
        this.getCmcg().readSession();
        sb.append("session.removeAttriubte(\"").append(sa.value().trim()).append("\");\r\n");
    }

}

