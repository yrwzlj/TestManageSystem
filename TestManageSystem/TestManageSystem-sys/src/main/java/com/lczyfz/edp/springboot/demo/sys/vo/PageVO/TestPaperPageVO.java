package com.lczyfz.edp.springboot.demo.sys.vo.PageVO;

import com.lczyfz.edp.springboot.core.vo.PageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author YRW
 * @Date 2022/10/08 16:25
 * @Description com.lczyfz.edp.springboot.demo.sys.vo
 * @version: 1.0
 */
@ApiModel(value = " TestPaperPageVO", description = "试卷实体VO")
@Data
public class TestPaperPageVO extends PageVO {

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column test_paper.id
     *
     * @mbggenerated
     */
    @ApiModelProperty(value = "id")
    private String id;


    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column test_paper.name
     *
     * @mbggenerated
     */
    @ApiModelProperty(value = "name",example = "高三第一轮数学复习试卷")
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column test_paper.test_time
     *
     * @mbggenerated
     */
    @ApiModelProperty(value = "testTime",example = "9000")
    private int testTime;


    private String officeId;

    public void copyPageVO(PageVO pageVO) {
        this.setPageNo(pageVO.getPageNo());

        this.setPageNo(pageVO.getPageNo());

        this.setOrderBy(pageVO.getOrderBy());
    }

}