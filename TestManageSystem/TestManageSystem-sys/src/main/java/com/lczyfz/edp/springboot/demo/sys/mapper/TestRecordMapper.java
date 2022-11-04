package com.lczyfz.edp.springboot.demo.sys.mapper;
import java.util.List;

import com.lczyfz.edp.springboot.demo.sys.entity.example.TestRecordExample;
import org.apache.ibatis.annotations.Param;
import java.util.Date;

import com.lczyfz.edp.springboot.core.mapper.CrudMapper;
import com.lczyfz.edp.springboot.demo.sys.entity.correctOrNot;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestRecordMapper extends CrudMapper<correctOrNot, TestRecordExample> {

    int updateUpdateDateAndUpdateByAndSubmitTimeAndPaperByTestIdAndSubmiter(@Param("updateDate") Date updateDate, @Param("updateBy") String updateBy, @Param("submitTime") Date submitTime, @Param("paper") String paper, @Param("testId") String testId, @Param("submiter") String submiter);

    List<correctOrNot> searchAllByTestIdAndSubmiter(@Param("testId") String testId, @Param("submiter") String submiter);

    int updateBeginTimeAndUpdateDateAndUpdateByByTestIdAndSubmiter(@Param("beginTime") Date beginTime, @Param("updateDate") Date updateDate, @Param("updateBy") String updateBy, @Param("testId") String testId, @Param("submiter") String submiter);

    int updatePapIdAndUpdateDateAndUpdateByByTestIdAndPapId(@Param("papId") String papId, @Param("updateDate") Date updateDate, @Param("updateBy") String updateBy, @Param("testId") String testId, @Param("oldPapId") String oldPapId);

    List<correctOrNot> searchAllBySubmiter(@Param("submiter") String submiter);

    int updateUpdateDateAndUpdateByAndCorrectOrNotAndCorrectPaperByTestIdAndSubmiter(@Param("updateDate") Date updateDate, @Param("updateBy") String updateBy, @Param("correctOrNot") Byte correctOrNot, @Param("correctPaper") String correctPaper, @Param("testId") String testId, @Param("submiter") String submiter);

    int updateUpdateByAndUpdateDateAndCorrectOrNotByTestIdAndSubmiter(@Param("updateBy") String updateBy, @Param("updateDate") Date updateDate, @Param("correctOrNot") Byte correctOrNot, @Param("testId") String testId, @Param("submiter") String submiter);

    int updatePapIdAndUpdateByAndUpdateDateByTestIdAndSubmiter(@Param("papId") String papId, @Param("updateBy") String updateBy, @Param("updateDate") Date updateDate, @Param("testId") String testId, @Param("submiter") String submiter);
}