package com.lczyfz.edp.springboot.demo.sys.entity;

import com.lczyfz.edp.springboot.core.entity.DataEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

;

@ApiModel(value = "Stu", description = "Stu")
public class Stu extends DataEntity<Stu> {


    /**
     * 
     * 表字段 : stu.name
     *
     * @mbggenerated Mon Feb 28 15:57:17 CST 2022
     */
    @ApiModelProperty(value = "")
    @NotBlank(message = "不能为空")
    private String name;

    /**
     * 
     * 表字段 : stu.age
     *
     * @mbggenerated Mon Feb 28 15:57:17 CST 2022
     */
    @ApiModelProperty(value = "")
    @NotBlank(message = "不能为空")
    private Integer age;
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stu.name
     *
     * @return the value of stu.name
     *
     * @mbggenerated Mon Feb 28 15:57:17 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stu.name
     *
     * @param name the value for stu.name
     *
     * @mbggenerated Mon Feb 28 15:57:17 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column stu.age
     *
     * @return the value of stu.age
     *
     * @mbggenerated Mon Feb 28 15:57:17 CST 2022
     */
    public Integer getAge() {
        return age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column stu.age
     *
     * @param age the value for stu.age
     *
     * @mbggenerated Mon Feb 28 15:57:17 CST 2022
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table stu
     *
     * @mbggenerated Mon Feb 28 15:57:17 CST 2022
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", age=").append(age);
        sb.append(", createBy=").append(createBy);
        sb.append(", createDate=").append(createDate);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateDate=").append(updateDate);
        sb.append(", remarks=").append(remarks);
        sb.append(", delFlag=").append(delFlag);
        sb.append("]");
        return sb.toString();
    }
}