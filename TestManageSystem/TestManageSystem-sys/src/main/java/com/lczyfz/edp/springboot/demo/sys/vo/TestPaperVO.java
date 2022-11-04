package com.lczyfz.edp.springboot.demo.sys.vo;

import com.lczyfz.edp.springboot.core.validation.Create;
import com.lczyfz.edp.springboot.core.validation.Update;
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
@ApiModel(value = "TestPaperVO", description = "试卷实体VO")
@Data
public class TestPaperVO {

    @ApiModelProperty(value = "id")
    @NotBlank(groups = {Update.class},message = "不能为空")
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column test_paper.name
     *
     * @mbggenerated
     */
    @ApiModelProperty(value = "name",example = "高三第一轮数学复习试卷")
    @NotBlank(groups = {Create.class},message = "不能为空")
    private String name;

//    /**
//     * This field was generated by MyBatis Generator.
//     * This field corresponds to the database column test_paper.test_time
//     *
//     * @mbggenerated
//     */
//    @ApiModelProperty(value = "testTime",example = "9000")
//    private int testTime;
}
