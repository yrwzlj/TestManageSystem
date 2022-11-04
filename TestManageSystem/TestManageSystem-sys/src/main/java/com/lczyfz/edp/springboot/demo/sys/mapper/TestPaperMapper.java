package com.lczyfz.edp.springboot.demo.sys.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.lczyfz.edp.springboot.core.mapper.CrudMapper;

import com.lczyfz.edp.springboot.demo.sys.entity.TestPaper;
import com.lczyfz.edp.springboot.demo.sys.entity.example.TestPaperExample;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestPaperMapper extends CrudMapper<TestPaper, TestPaperExample> {

    List<TestPaper> searchTestTimeById(@Param("id") String id);

    List<TestPaper> searchAll();

    List<TestPaper> searchAllById(@Param("id") String id);


    TestPaper searchOneById(@Param("id") String id);
}