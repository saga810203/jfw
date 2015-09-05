package org.jfw.code.generator.orm.po;

import org.jfw.core.code.generator.annotations.orm.DBField;
import org.jfw.core.code.generator.annotations.orm.Table;
import org.jfw.core.code.generator.enums.orm.DE;

@Table("USER")
public class User {
    @DBField(value=DE.string,primaryKey=true)
    private String userid;
    @DBField(DE.Boolean)
    private boolean man;
    @DBField(DE.BOOLEAN)
    private Boolean man2;

    @DBField(DE.Byte)
    private byte age;
    @DBField(DE.integer)
    private int height;
   @DBField(DE.string)
    private String name;
    @DBField(DE.STRING)
    private String aliasName;
    @DBField(DE.INTEGER)
    private Integer weight;
    @DBField(DE.createTime)
    private String createTime;
    @DBField(DE.modifyTime)
    private String modifyTime;
    
    public String getUserid() {
        return userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public boolean isMan() {
        return man;
    }
    public void setMan(boolean man) {
        this.man = man;
    }
    public byte getAge() {
        return age;
    }
    public void setAge(byte age) {
        this.age = age;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAliasName() {
        return aliasName;
    }
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    public Integer getWeight() {
        return weight;
    }
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    public Boolean getMan2() {
        return man2;
    }
    public void setMan2(Boolean man2) {
        this.man2 = man2;
    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }
    
    
    
}
