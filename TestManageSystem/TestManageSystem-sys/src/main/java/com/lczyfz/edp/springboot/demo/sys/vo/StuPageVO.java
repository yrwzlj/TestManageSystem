package com.lczyfz.edp.springboot.demo.sys.vo;

import com.lczyfz.edp.springboot.core.vo.PageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Author LJX
 * @Date 2022/2/25 16:29
 * @Description com.lczyfz.edp.springboot.sys.vo
 * @version: 1.0
 */
@ApiModel(value = "StuVO", description = "学生实体VO")
@Data
public class StuPageVO extends PageVO {
    /**
     *
     * 表字段 : stu.id
     *
     * @mbggenerated Mon Feb 28 15:57:17 CST 2022
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     *
     * 表字段 : stu.name
     *
     * @mbggenerated Mon Feb 28 15:57:17 CST 2022
     */
    @ApiModelProperty(value = "姓名",example = "张三")
    private String name;

    /**
     *
     * 表字段 : stu.age
     *
     * @mbggenerated Mon Feb 28 15:57:17 CST 2022
     */
    @ApiModelProperty(value = "年龄",example = "10")
    private Integer age;

}