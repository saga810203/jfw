package org.jfw.code.generator.orm.dao.impl;

public class UserDaoImpl implements org.jfw.code.generator.orm.dao.UserDao {
    @Override
    public java.util.List<org.jfw.code.generator.orm.po.User> getUserBySex(java.sql.Connection con, boolean param1)
            throws java.sql.SQLException {
        String sql = "SELECT USERID,MAN,MAN2,AGE,HEIGHT,NAME,ALIAS_NAME,WEIGHT,CREATE_TIME,MODIFY_TIME FROM USER WHERE man=?";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        try {
            int paramIndex = 1;
            ps.setString(paramIndex++, param1 ? "1" : "0");
            java.sql.ResultSet rs = ps.executeQuery();
            try {
                java.util.List<org.jfw.code.generator.orm.po.User> result = new java.util.LinkedList<org.jfw.code.generator.orm.po.User>();
                while (rs.next()) {
                    org.jfw.code.generator.orm.po.User obj = new org.jfw.code.generator.orm.po.User();
                    obj.setUserid(rs.getString(1));
                    obj.setMan("1".equals(rs.getString(2)));
                    java.lang.Boolean tmp_1 = "1".equals(rs.getString(3));
                    if (rs.wasNull()) {
                        obj.setMan2(null);
                    } else {
                        obj.setMan2(tmp_1);
                    }
                    obj.setAge(rs.getByte(4));
                    obj.setHeight(rs.getInt(5));
                    obj.setName(rs.getString(6));
                    java.lang.String tmp_2 = rs.getString(7);
                    if (rs.wasNull()) {
                        obj.setAliasName(null);
                    } else {
                        obj.setAliasName(tmp_2);
                    }
                    java.lang.Integer tmp_3 = rs.getInt(8);
                    if (rs.wasNull()) {
                        obj.setWeight(null);
                    } else {
                        obj.setWeight(tmp_3);
                    }
                    obj.setCreateTime(rs.getString(9));
                    obj.setModifyTime(rs.getString(10));
                    result.add(obj);
                }
                return result;
            } finally {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
        }
    }

    @Override
    public int insertUser(java.sql.Connection con, org.jfw.code.generator.orm.po.User param1)
            throws java.sql.SQLException {
        String sql = "INSERT INTO USER (MODIFY_TIME,CREATE_TIME,USERID,MAN,MAN2,AGE,HEIGHT,NAME,ALIAS_NAME,WEIGHT) VALUES (to_char('YYYYMMDDHH24MISS',SYSDATE),to_char('YYYYMMDDHH24MISS',SYSDATE),?,?,?,?,?,?,?,?)";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        try {
            int paramIndex = 1;
            ps.setString(paramIndex++, param1.getUserid());
            ps.setString(paramIndex++, param1.isMan() ? "1" : "0");
            java.lang.Boolean tmp_1 = param1.getMan2();
            if (null != tmp_1) {
                ps.setString(paramIndex++, tmp_1 ? "1" : "0");
            } else {
                ps.setNull(paramIndex++, 1);
            }
            ps.setByte(paramIndex++, param1.getAge());
            ps.setInt(paramIndex++, param1.getHeight());
            ps.setString(paramIndex++, param1.getName());
            java.lang.String tmp_2 = param1.getAliasName();
            if (null != tmp_2) {
                ps.setString(paramIndex++, tmp_2);
            } else {
                ps.setNull(paramIndex++, 12);
            }
            java.lang.Integer tmp_3 = param1.getWeight();
            if (null != tmp_3) {
                ps.setInt(paramIndex++, tmp_3);
            } else {
                ps.setNull(paramIndex++, 4);
            }
            int result = ps.executeUpdate();
            return result;
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
        }
    }

    @Override
    public int deleteUserByUserId(java.sql.Connection con, java.lang.String param1) throws java.sql.SQLException {
        String sql = "DELETE FROM USER WHERE USERID=?";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        try {
            int paramIndex = 1;
            ps.setString(paramIndex++, param1);
            int result = ps.executeUpdate();
            return result;
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
        }
    }

    @Override
    public java.util.List<org.jfw.code.generator.orm.po.User> getAllUserByName(java.sql.Connection con,
            java.lang.String param1, java.lang.String param2, boolean param3, java.lang.Boolean param4)
            throws java.sql.SQLException {
        String sql = "SELECT USERID,MAN,MAN2,AGE,HEIGHT,NAME,ALIAS_NAME,WEIGHT,CREATE_TIME,MODIFY_TIME FROM USER WHERE MAN=? AND NAME=? AND ALIAS_NAME=? AND MAN2=?";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        try {
            int paramIndex = 1;
            ps.setString(paramIndex++, param3 ? "1" : "0");
            ps.setString(paramIndex++, param1);
            ps.setString(paramIndex++, param2);
            ps.setString(paramIndex++, param3 ? "1" : "0");
            java.sql.ResultSet rs = ps.executeQuery();
            try {
                java.util.List<org.jfw.code.generator.orm.po.User> result = new java.util.LinkedList<org.jfw.code.generator.orm.po.User>();
                while (rs.next()) {
                    org.jfw.code.generator.orm.po.User obj = new org.jfw.code.generator.orm.po.User();
                    obj.setUserid(rs.getString(1));
                    obj.setMan("1".equals(rs.getString(2)));
                    java.lang.Boolean tmp_1 = "1".equals(rs.getString(3));
                    if (rs.wasNull()) {
                        obj.setMan2(null);
                    } else {
                        obj.setMan2(tmp_1);
                    }
                    obj.setAge(rs.getByte(4));
                    obj.setHeight(rs.getInt(5));
                    obj.setName(rs.getString(6));
                    java.lang.String tmp_2 = rs.getString(7);
                    if (rs.wasNull()) {
                        obj.setAliasName(null);
                    } else {
                        obj.setAliasName(tmp_2);
                    }
                    java.lang.Integer tmp_3 = rs.getInt(8);
                    if (rs.wasNull()) {
                        obj.setWeight(null);
                    } else {
                        obj.setWeight(tmp_3);
                    }
                    obj.setCreateTime(rs.getString(9));
                    obj.setModifyTime(rs.getString(10));
                    result.add(obj);
                }
                return result;
            } finally {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
        }
    }

    @Override
    public java.util.List<org.jfw.code.generator.orm.po.User> getAllUserByName3(java.sql.Connection con,
            java.lang.String param1, java.lang.String param2, boolean param3, java.lang.Boolean param4)
            throws java.sql.SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT USERID,MAN,MAN2,AGE,HEIGHT,NAME,ALIAS_NAME,WEIGHT,CREATE_TIME,MODIFY_TIME FROM USER");
        sql.append(" WHERE MAN=?");
        java.lang.String tmp_1 = param1;
        if (null != tmp_1) {
            sql.append(" AND NAME=?");
        }
        java.lang.String tmp_2 = param2;
        if (null != tmp_2) {
            sql.append(" AND ALIAS_NAME=?");
        }
        java.lang.Boolean tmp_3 = param3;
        if (null != tmp_3) {
            sql.append(" AND MAN2=?");
        }
        java.sql.PreparedStatement ps = con.prepareStatement(sql.toString());
        try {
            int paramIndex = 1;
            ps.setString(paramIndex++, param3 ? "1" : "0");
            if (null != tmp_1) {
                ps.setString(paramIndex++, tmp_1);
            }
            if (null != tmp_2) {
                ps.setString(paramIndex++, tmp_2);
            }
            if (null != tmp_3) {
                ps.setString(paramIndex++, tmp_3 ? "1" : "0");
            }
            java.sql.ResultSet rs = ps.executeQuery();
            try {
                java.util.List<org.jfw.code.generator.orm.po.User> result = new java.util.LinkedList<org.jfw.code.generator.orm.po.User>();
                while (rs.next()) {
                    org.jfw.code.generator.orm.po.User obj = new org.jfw.code.generator.orm.po.User();
                    obj.setUserid(rs.getString(1));
                    obj.setMan("1".equals(rs.getString(2)));
                    java.lang.Boolean tmp_4 = "1".equals(rs.getString(3));
                    if (rs.wasNull()) {
                        obj.setMan2(null);
                    } else {
                        obj.setMan2(tmp_4);
                    }
                    obj.setAge(rs.getByte(4));
                    obj.setHeight(rs.getInt(5));
                    obj.setName(rs.getString(6));
                    java.lang.String tmp_5 = rs.getString(7);
                    if (rs.wasNull()) {
                        obj.setAliasName(null);
                    } else {
                        obj.setAliasName(tmp_5);
                    }
                    java.lang.Integer tmp_6 = rs.getInt(8);
                    if (rs.wasNull()) {
                        obj.setWeight(null);
                    } else {
                        obj.setWeight(tmp_6);
                    }
                    obj.setCreateTime(rs.getString(9));
                    obj.setModifyTime(rs.getString(10));
                    result.add(obj);
                }
                return result;
            } finally {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
        }
    }

    @Override
    public int updateUser(java.sql.Connection con, org.jfw.code.generator.orm.po.User param1)
            throws java.sql.SQLException {
        String sql = "UPDATE USER SET MODIFY_TIME=to_char('YYYYMMDDHH24MISS',SYSDATE),HEIGHT=?,AGE=?,MAN=?,MAN2=?,NAME=?,ALIAS_NAME=?,WEIGHT=? WHERE USERID=?";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        try {
            int paramIndex = 1;
            ps.setInt(paramIndex++, param1.getHeight());
            ps.setByte(paramIndex++, param1.getAge());
            ps.setString(paramIndex++, param1.isMan() ? "1" : "0");
            java.lang.Boolean tmp_1 = param1.getMan2();
            if (null != tmp_1) {
                ps.setString(paramIndex++, tmp_1 ? "1" : "0");
            } else {
                ps.setNull(paramIndex++, 1);
            }
            java.lang.String tmp_2 = param1.getName();
            if (null != tmp_2) {
                ps.setString(paramIndex++, tmp_2);
            } else {
                ps.setNull(paramIndex++, 12);
            }
            java.lang.String tmp_3 = param1.getAliasName();
            if (null != tmp_3) {
                ps.setString(paramIndex++, tmp_3);
            } else {
                ps.setNull(paramIndex++, 12);
            }
            java.lang.Integer tmp_4 = param1.getWeight();
            if (null != tmp_4) {
                ps.setInt(paramIndex++, tmp_4);
            } else {
                ps.setNull(paramIndex++, 4);
            }
            ps.setString(paramIndex++, param1.getUserid());
            int result = ps.executeUpdate();
            return result;
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
        }
    }

    @Override
    public java.util.List<org.jfw.code.generator.orm.po.User> getAllUserByName2(java.sql.Connection con,
            java.lang.String param1, java.lang.String param2) throws java.sql.SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT USERID,MAN,MAN2,AGE,HEIGHT,NAME,ALIAS_NAME,WEIGHT,CREATE_TIME,MODIFY_TIME FROM USER");
        StringBuilder sqlw = new StringBuilder();
        java.lang.String tmp_1 = param1;
        if (null != tmp_1) {
            sqlw.append(" AND NAME=?");
        }
        java.lang.String tmp_2 = param2;
        if (null != tmp_2) {
            sqlw.append(" AND ALIAS_NAME=?");
        }
        if (sqlw.length() > 0) {
            sql.append(" WHERE ").append(sqlw.toString().substring(5));
        }
        java.sql.PreparedStatement ps = con.prepareStatement(sql.toString());
        try {
            int paramIndex = 1;
            if (null != tmp_1) {
                ps.setString(paramIndex++, tmp_1);
            }
            if (null != tmp_2) {
                ps.setString(paramIndex++, tmp_2);
            }
            java.sql.ResultSet rs = ps.executeQuery();
            try {
                java.util.List<org.jfw.code.generator.orm.po.User> result = new java.util.LinkedList<org.jfw.code.generator.orm.po.User>();
                while (rs.next()) {
                    org.jfw.code.generator.orm.po.User obj = new org.jfw.code.generator.orm.po.User();
                    obj.setUserid(rs.getString(1));
                    obj.setMan("1".equals(rs.getString(2)));
                    java.lang.Boolean tmp_3 = "1".equals(rs.getString(3));
                    if (rs.wasNull()) {
                        obj.setMan2(null);
                    } else {
                        obj.setMan2(tmp_3);
                    }
                    obj.setAge(rs.getByte(4));
                    obj.setHeight(rs.getInt(5));
                    obj.setName(rs.getString(6));
                    java.lang.String tmp_4 = rs.getString(7);
                    if (rs.wasNull()) {
                        obj.setAliasName(null);
                    } else {
                        obj.setAliasName(tmp_4);
                    }
                    java.lang.Integer tmp_5 = rs.getInt(8);
                    if (rs.wasNull()) {
                        obj.setWeight(null);
                    } else {
                        obj.setWeight(tmp_5);
                    }
                    obj.setCreateTime(rs.getString(9));
                    obj.setModifyTime(rs.getString(10));
                    result.add(obj);
                }
                return result;
            } finally {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
        }
    }

    @Override
    public int updateUserWithSelection(java.sql.Connection con, org.jfw.code.generator.orm.po.User param1)
            throws java.sql.SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE USER SET MODIFY_TIME=to_char('YYYYMMDDHH24MISS',SYSDATE),HEIGHT=?,AGE=?,MAN=?");
        java.lang.Boolean tmp_1 = param1.getMan2();
        if (null != tmp_1) {
            sql.append(",MAN2=?");
        }
        java.lang.String tmp_2 = param1.getName();
        if (null != tmp_2) {
            sql.append(",NAME=?");
        }
        java.lang.String tmp_3 = param1.getAliasName();
        if (null != tmp_3) {
            sql.append(",ALIAS_NAME=?");
        }
        java.lang.Integer tmp_4 = param1.getWeight();
        if (null != tmp_4) {
            sql.append(",WEIGHT=?");
        }
        sql.append(" WHERE USERID=?");
        java.sql.PreparedStatement ps = con.prepareStatement(sql.toString());
        try {
            int paramIndex = 1;
            ps.setInt(paramIndex++, param1.getHeight());
            ps.setByte(paramIndex++, param1.getAge());
            ps.setString(paramIndex++, param1.isMan() ? "1" : "0");
            if (null != tmp_1) {
                ps.setString(paramIndex++, tmp_1 ? "1" : "0");
            }
            if (null != tmp_2) {
                ps.setString(paramIndex++, tmp_2);
            }
            if (null != tmp_3) {
                ps.setString(paramIndex++, tmp_3);
            }
            if (null != tmp_4) {
                ps.setInt(paramIndex++, tmp_4);
            }
            ps.setString(paramIndex++, param1.getUserid());
            int result = ps.executeUpdate();
            return result;
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
        }
    }

    @Override
    public org.jfw.code.generator.orm.po.User getAUser(java.sql.Connection con, java.lang.String param1)
            throws java.sql.SQLException {
        String sql = "SELECT USERID,MAN,MAN2,AGE,HEIGHT,NAME,ALIAS_NAME,WEIGHT,CREATE_TIME,MODIFY_TIME FROM USER WHERE userid = ?";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        try {
            int paramIndex = 1;
            ps.setString(paramIndex++, param1);
            java.sql.ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    org.jfw.code.generator.orm.po.User obj = new org.jfw.code.generator.orm.po.User();
                    obj.setUserid(rs.getString(1));
                    obj.setMan("1".equals(rs.getString(2)));
                    java.lang.Boolean tmp_1 = "1".equals(rs.getString(3));
                    if (rs.wasNull()) {
                        obj.setMan2(null);
                    } else {
                        obj.setMan2(tmp_1);
                    }
                    obj.setAge(rs.getByte(4));
                    obj.setHeight(rs.getInt(5));
                    obj.setName(rs.getString(6));
                    java.lang.String tmp_2 = rs.getString(7);
                    if (rs.wasNull()) {
                        obj.setAliasName(null);
                    } else {
                        obj.setAliasName(tmp_2);
                    }
                    java.lang.Integer tmp_3 = rs.getInt(8);
                    if (rs.wasNull()) {
                        obj.setWeight(null);
                    } else {
                        obj.setWeight(tmp_3);
                    }
                    obj.setCreateTime(rs.getString(9));
                    obj.setModifyTime(rs.getString(10));
                    return obj;
                }
                return null;
            } finally {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
        }
    }

    @Override
    public java.util.List<org.jfw.code.generator.orm.po.User> getAllUser(java.sql.Connection con)
            throws java.sql.SQLException {
        String sql = "SELECT USERID,MAN,MAN2,AGE,HEIGHT,NAME,ALIAS_NAME,WEIGHT,CREATE_TIME,MODIFY_TIME FROM USER";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        try {
            java.sql.ResultSet rs = ps.executeQuery();
            try {
                java.util.List<org.jfw.code.generator.orm.po.User> result = new java.util.LinkedList<org.jfw.code.generator.orm.po.User>();
                while (rs.next()) {
                    org.jfw.code.generator.orm.po.User obj = new org.jfw.code.generator.orm.po.User();
                    obj.setUserid(rs.getString(1));
                    obj.setMan("1".equals(rs.getString(2)));
                    java.lang.Boolean tmp_1 = "1".equals(rs.getString(3));
                    if (rs.wasNull()) {
                        obj.setMan2(null);
                    } else {
                        obj.setMan2(tmp_1);
                    }
                    obj.setAge(rs.getByte(4));
                    obj.setHeight(rs.getInt(5));
                    obj.setName(rs.getString(6));
                    java.lang.String tmp_2 = rs.getString(7);
                    if (rs.wasNull()) {
                        obj.setAliasName(null);
                    } else {
                        obj.setAliasName(tmp_2);
                    }
                    java.lang.Integer tmp_3 = rs.getInt(8);
                    if (rs.wasNull()) {
                        obj.setWeight(null);
                    } else {
                        obj.setWeight(tmp_3);
                    }
                    obj.setCreateTime(rs.getString(9));
                    obj.setModifyTime(rs.getString(10));
                    result.add(obj);
                }
                return result;
            } finally {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                }
            }
        } finally {
            try {
                ps.close();
            } catch (java.sql.SQLException e) {
            }
        }
    }
}