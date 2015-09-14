package org.jfw.code.generator.orm.vo;

import org.jfw.code.generator.orm.po.LoginUser;
import org.jfw.core.code.generator.annotations.orm.DBField;
import org.jfw.core.code.generator.annotations.orm.InheritedTable;
import org.jfw.core.code.generator.annotations.orm.View;
import org.jfw.core.code.generator.enums.orm.DE;
@View(value="LOGINUSER a",inherited=@InheritedTable(value=LoginUser.class,tableAlias="a"))
public class UserVO extends LoginUser {

	public boolean isChild() {
		return child;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

	@DBField(calcField=true,value=DE.Boolean,name="case where AGE >=18 WHEN '0' ELSE '1' END",alias="child")
	private boolean child;
}
