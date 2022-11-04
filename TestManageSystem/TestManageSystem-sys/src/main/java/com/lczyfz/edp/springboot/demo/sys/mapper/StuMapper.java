package com.lczyfz.edp.springboot.demo.sys.mapper;

import com.lczyfz.edp.springboot.core.mapper.CrudMapper;
import com.lczyfz.edp.springboot.demo.sys.entity.Stu;
import com.lczyfz.edp.springboot.demo.sys.entity.StuExample;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StuMapper extends CrudMapper<Stu, StuExample> {

}