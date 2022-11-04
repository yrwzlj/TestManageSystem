package com.lczyfz.edp.springboot.demo.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.lczyfz.edp.springboot.core.config.Global;
import com.lczyfz.edp.springboot.core.entity.DataEntity;
import com.lczyfz.edp.springboot.core.utils.SnowFlakeIdUtils;
import lombok.Data;

/**
 * 
 * @TableName test
 */
@TableName(value ="ds_test")
@Data
public class Test extends DataEntity<Test> {
    /**
     * 
     */
    @TableId
    private String id;


    /**
     * 
     */
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
    private Date beginTime;

    /**
     * 考试结束时间
     */
    private Date endTime;

    private String officeId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public void preInsert() {
        if (!this.isNewRecord) {
            this.setId(SnowFlakeIdUtils.getIdWorker(Global.WORKER_ID, Global.DATA_CENTER_ID).nextId() + "");
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm-ss");

        this.updateDate = new Date();
        try {
            this.createDate = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").parse(localDateTime.format(dateTimeFormatter));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}