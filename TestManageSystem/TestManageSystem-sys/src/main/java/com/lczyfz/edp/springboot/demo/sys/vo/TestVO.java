package com.lczyfz.edp.springboot.demo.sys.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lczyfz.edp.springboot.core.validation.Create;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * @Author YRW
 * @Date 2022/10/08 16:25
 * @Description com.lczyfz.edp.springboot.demo.sys.vo
 * @version: 1.0
 */
@ApiModel(value = "TestVO", description = "考试实体VO")
@Data
public class TestVO {

    /**
     *
     */
    @TableId
    private String id;


    /**
     *
     */
    @ApiModelProperty(value = "name")
    @NotBlank(groups = {Create.class},message = "不能为空")
    private String name;

    /**
     *
     */
    @ApiModelProperty(value = "teacherId")
    @NotBlank(groups = {Create.class},message = "不能为空")
    private String teacherId;

    /**
     *
     */
    private String paperId;

    /**
     * 考试开始时间
     */
    @ApiModelProperty(value = "beginTime",example = "2022-10-9 8:0:0")
    @NotNull(groups = {Create.class},message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    private Date beginTime;

    /**
     * 考试结束时间
     */
    @ApiModelProperty(value = "endTime",example = "2022-10-9 8:0:0")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(groups = {Create.class},message = "不能为空")
    @Future
    private Date endTime;
}
