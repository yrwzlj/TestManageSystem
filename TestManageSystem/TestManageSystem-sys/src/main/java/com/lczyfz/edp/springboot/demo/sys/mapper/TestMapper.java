package com.lczyfz.edp.springboot.demo.sys.mapper;
import java.util.List;
import java.util.Date;

import com.lczyfz.edp.springboot.demo.sys.entity.example.TestExample;
import org.apache.ibatis.annotations.Param;

import com.lczyfz.edp.springboot.core.mapper.CrudMapper;
import com.lczyfz.edp.springboot.demo.sys.entity.Test;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 25282
* @description 针对表【test】的数据库操作Mapper
* @createDate 2022-09-28 20:30:18
* @Entity com.lczyfz.edp.springboot.demo.sys.entity.Test
*/
@Mapper
public interface TestMapper extends CrudMapper<Test, TestExample> {

    int updatePaperIdAndUpdateDateAndUpdateByAndEndTimeById(@Param("paperId") String paperId, @Param("updateDate") Date updateDate, @Param("updateBy") String updateBy, @Param("endTime") Date endTime, @Param("id") String id);

    List<Test> searchAllById(@Param("id") String id);

    List<Test> searchBeginTimeById(@Param("id") String id);

    int updateUpdateDateAndUpdateByAndPaperIdById(@Param("updateDate") Date updateDate, @Param("updateBy") String updateBy, @Param("paperId") String paperId, @Param("id") String id);
}




