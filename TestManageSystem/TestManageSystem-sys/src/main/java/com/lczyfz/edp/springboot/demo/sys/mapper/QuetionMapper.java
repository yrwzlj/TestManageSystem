package com.lczyfz.edp.springboot.demo.sys.mapper;
import java.util.List;

import com.lczyfz.edp.springboot.demo.sys.entity.Quetion;
import com.lczyfz.edp.springboot.demo.sys.entity.example.QuetionExample;
import org.apache.ibatis.annotations.Param;

import com.lczyfz.edp.springboot.core.mapper.CrudMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuetionMapper extends CrudMapper<Quetion, QuetionExample> {

    int updateCententByIdAndCreateBy(@Param("centent") String centent, @Param("id") String id, @Param("createBy") String createBy);

    List<Quetion> searchAllByCreateBy(@Param("createBy") String createBy);
}