package org.jfw.core.code.generator.orm;

public abstract class UpdateMethodGenerator extends JDBCMethodGenerator {
	protected void checkReturnType()
	{
		if(int.class!=this.method.getGenericReturnType())throw new RuntimeException("invalid returnType with DB DML operation at "+this.parentType.getName()+"."+this.method.getName());
	}
	
    


	@Override
	protected void buildHandleResult(StringBuilder sb) {
		sb.append("int result = ps.executeUpdate();");
		for(int i = 0 ; i < this.psshs.length ; ++i)
		{
			if(this.psshs[i].isReplaceResource()) this.psshs[i].replaceResource(sb);
		}
		sb.append("retrun result");
	}
}
