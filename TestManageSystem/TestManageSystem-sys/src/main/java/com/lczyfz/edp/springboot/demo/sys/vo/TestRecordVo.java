package com.lczyfz.edp.springboot.demo.sys.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lczyfz.edp.springboot.core.validation.Create;
import com.lczyfz.edp.springboot.core.validation.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
@ApiModel(value = "TestRecordVO",description = "考试记录实体vo")
public class TestRecordVo {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column test_record.id
     *
     * @mbggenerated
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column test_record.test_id
     *
     * @mbggenerated
     */

    @ApiModelProperty(value = "testId")
    @NotBlank(groups = {Create.class, Update.class},message = "不能为空")
    private String testId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column test_record.begin_time
     *
     * @mbggenerated
     */
    @ApiModelProperty(value = "beginTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column test_record.submit_time
     *
     * @mbggenerated
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "submitTime")
    private Date submitTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column test_record.is_correct
     *
     * @mbggenerated
     */
    @TableField("is_correct")
    private Byte correctOrNot;

    private String officeId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ds_test_record.submiter
     *
     * @mbg.generated
     */
    @ApiModelProperty(value = "name")
    @NotBlank(groups = {Create.class, Update.class},message = "不能为空")
    private String submiter;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ds_test_record.pap_id
     *
     * @mbg.generated
     */
    private String papId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ds_test_record.correct_paper
     *
     * @mbg.generated
     */
    private String correctPaper;

}