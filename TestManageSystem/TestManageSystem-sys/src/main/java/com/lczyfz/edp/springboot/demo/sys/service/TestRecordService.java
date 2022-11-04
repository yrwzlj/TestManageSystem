package com.lczyfz.edp.springboot.demo.sys.service;

import com.lczyfz.edp.springboot.core.service.CrudService;
import com.lczyfz.edp.springboot.demo.sys.entity.correctOrNot;
import com.lczyfz.edp.springboot.demo.sys.entity.example.TestRecordExample;
import com.lczyfz.edp.springboot.demo.sys.mapper.TestRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author YRW
 * @description 针对表【test_record】的数据库操作Service
 * @createDate 2022-09-28 20:30:25
 */
@Service
public class TestRecordService extends CrudService<TestRecordMapper, correctOrNot, TestRecordExample> {
    @Autowired
    TestRecordMapper testRecordMapper;

    @Override
    public int save(correctOrNot entity) {
        int result = 0;

        if (entity.isNewRecord()) {
            entity.preInsert();
            result = testRecordMapper.insert(entity);
        } else {
            entity.preUpdate();
            result = this.mapper.updateByPrimaryKeySelective(entity);
        }

        return result;
    }


}
