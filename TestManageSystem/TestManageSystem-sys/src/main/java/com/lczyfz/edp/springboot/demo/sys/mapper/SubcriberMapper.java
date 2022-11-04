package com.lczyfz.edp.springboot.demo.sys.mapper;
import java.util.Date;
import java.util.List;

import com.lczyfz.edp.springboot.demo.sys.entity.example.SubcriberExample;
import org.apache.ibatis.annotations.Param;

import com.lczyfz.edp.springboot.core.mapper.CrudMapper;
import com.lczyfz.edp.springboot.demo.sys.entity.Subcriber;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubcriberMapper extends CrudMapper<Subcriber, SubcriberExample> {

    List<Subcriber> searchByNameAndPassword(@Param("name") String name, @Param("password") String password);

    int updateUpdateDateAndUpdateByAndDelFlagByNameAndPassword(@Param("updateDate") Date updateDate, @Param("updateBy") String updateBy, @Param("delFlag") String delFlag, @Param("name") String name, @Param("password") String password);
    int updatePasswordAndUpdateByAndUpdateDateByName(@Param("password") String password, @Param("updateBy") String updateBy, @Param("updateDate") Date updateDate, @Param("name") String name);

    List<Subcriber> searchByName(@Param("name") String name);

    List<Subcriber> searchAllById(@Param("id") String id);

    List<Subcriber> searchByTeacherOrNot(@Param("teacherOrNot") Byte teacherOrNot);

    List<Subcriber> searchByNameOrEmail(@Param("name") String name, @Param("email") String email);

    int updateUpdateDateAndUpdateByAndDelFlagById(@Param("updateDate") Date updateDate, @Param("updateBy") String updateBy, @Param("delFlag") String delFlag, @Param("id") String id);
}