package org.jfw.code.generator.orm.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.jfw.code.generator.orm.po.LoginUser;
import org.jfw.code.generator.orm.po.User;
import org.jfw.code.generator.orm.vo.UserVO;
import org.jfw.core.code.generator.annotations.orm.InsertTable;
import org.jfw.core.code.generator.annotations.orm.SelectTable;
import org.jfw.core.code.generator.annotations.orm.SelectTableRow;
import org.jfw.core.code.generator.annotations.orm.SelectView;
import org.jfw.core.code.generator.annotations.orm.SqlVal;
import org.jfw.core.code.generator.annotations.orm.UpdateSql;
import org.jfw.core.code.generator.annotations.orm.UpdateTable;
import org.jfw.core.code.generator.enums.orm.DE;


public interface UserDao {
	@SelectView
	List<UserVO> getAllUserVO(Connection con)throws SQLException;
	
	
    @SelectTable
    List<LoginUser> getAllUser(Connection con) throws SQLException;
    
    @SelectTable(
 sqlVal={@SqlVal(type=DE.string,sqlEl="NAME=?"),
            @SqlVal(type=DE.string,paramIndex=2,sqlEl="ALIAS_NAME=?"),
            @SqlVal(type=DE.Boolean,paramIndex=3,sqlEl="MAN=?"),
            @SqlVal(type=DE.BOOLEAN,paramIndex=3,sqlEl="MAN2=?"),
            
 })
    
    List<LoginUser> getAllUserByName(Connection con,String name,String aliasName,boolean a,Boolean b) throws SQLException;
    @SelectTable(dynamicFilter=true,
            sqlVal={@SqlVal(type=DE.string,sqlEl="NAME=?"),
                       @SqlVal(type=DE.string,paramIndex=2,sqlEl="ALIAS_NAME=?"),
                       @SqlVal(type=DE.Boolean,paramIndex=3,sqlEl="MAN=?"),
                       @SqlVal(type=DE.BOOLEAN,paramIndex=3,sqlEl="MAN2=?")
                       
            })
               
               List<LoginUser> getAllUserByName3(Connection con,String name,String aliasName,boolean a,Boolean b) throws SQLException;
             
    @SelectTable(dynamicFilter=true, sqlVal={@SqlVal(type=DE.string,sqlEl="NAME=?"),@SqlVal(type=DE.string,paramIndex=2,sqlEl="ALIAS_NAME=?")})
    List<LoginUser> getAllUserByName2(Connection con,String name,String aliasName) throws SQLException;
    
    @SelectTableRow(sqlVal={@SqlVal(type=DE.string,sqlEl="userid = ?")})
    User getAUser(Connection con,String userid)  throws SQLException;
    @SelectTable(sqlVal={@SqlVal(type=DE.Boolean,sqlEl="man=?")})
    List<User> getUserBySex(Connection con,boolean isMan) throws SQLException;
    @InsertTable
    int insertUser(Connection con,LoginUser user) throws SQLException;
    @UpdateTable
    int updateUser(Connection con,LoginUser user) throws SQLException;
    @UpdateTable(dynamic=true)
    int updateUserWithSelection(Connection con,User user) throws SQLException;
    @UpdateSql(value="DELETE FROM USER WHERE USERID=?",where={
            @SqlVal(type=DE.string)
    })
    int deleteUserByUserId(Connection con,String userid) throws SQLException;
    
    
    
    
    

}
