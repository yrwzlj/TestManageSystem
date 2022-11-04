package com.lczyfz.edp.springboot.demo.sys.service;

import com.lczyfz.edp.springboot.core.entity.Page;
import com.lczyfz.edp.springboot.core.entity.Role;
import com.lczyfz.edp.springboot.core.service.CrudService;
import com.lczyfz.edp.springboot.demo.sys.entity.Test;
import com.lczyfz.edp.springboot.demo.sys.entity.example.TestExample;
import com.lczyfz.edp.springboot.demo.sys.mapper.TestMapper;
import com.lczyfz.edp.springboot.demo.sys.vo.PageVO.TestPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author YRW
 * @description 针对表【test】的数据库操作Service
 * @createDate 2022-09-28 20:30:18
 */
@Service
public class TestService extends CrudService<TestMapper, Test, TestExample> {

    @Autowired
    private TestMapper testMapper;

    @Override
    public int save(Test entity) {
        int result = 0;

        if (entity.isNewRecord()) {
            entity.preInsert();
            result = testMapper.insert(entity);
        } else {
            entity.preUpdate();
            result = this.mapper.updateByPrimaryKeySelective(entity);
        }

        return result;
    }

    public Page<Test> findPage(Page<Test> page, TestPageVO pageVO) {
        TestExample testExample = new TestExample();

        testExample.createCriteria().andDelFlagEqualTo(Role.DEL_FLAG_NORMAL);

        if (com.lczyfz.edp.springboot.core.utils.StringUtils.isNotBlank(pageVO.getId())) {
            testExample.getOredCriteria().get(0).andIdEqualTo(pageVO.getId());
        }

        page.setCount(mapper.countByExample(testExample));
        // 每页条数
        testExample.setPageSize(page.getMaxResults());
        // 第几页
        testExample.setLimitStart(page.getFirstResult());

        page.setList(mapper.selectByExample(testExample));

        return page;
    }

}
