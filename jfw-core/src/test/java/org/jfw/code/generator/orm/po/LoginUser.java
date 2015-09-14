package org.jfw.code.generator.orm.po;

import org.jfw.core.code.generator.annotations.orm.DBField;
import org.jfw.core.code.generator.annotations.orm.Table;
import org.jfw.core.code.generator.enums.orm.DE;
@Table("LOGINUSER")
public class LoginUser extends User{
    @DBField(value=DE.Long)
	private long loginTime;

	public long getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	
}
