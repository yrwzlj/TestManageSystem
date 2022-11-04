package com.lczyfz.edp.springboot.demo.sys.vo.PageVO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lczyfz.edp.springboot.core.validation.Create;
import com.lczyfz.edp.springboot.core.vo.PageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Author YRW
 * @Date 2022/10/08 16:25
 * @Description com.lczyfz.edp.springboot.demo.sys.vo
 * @version: 1.0
 */
@ApiModel(value = " TestPageVO", description = "试卷实体VO")
@Data
public class TestPageVO extends PageVO {

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
    private String teacherId;


    /**
     *
     */
    private String paperId;

    /**
     * 考试开始时间
     */
    @ApiModelProperty(value = "beginTime")
    @NotBlank(groups = {Create.class},message = "不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

//    /**
//     * 考试结束时间
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date endTime;
}
