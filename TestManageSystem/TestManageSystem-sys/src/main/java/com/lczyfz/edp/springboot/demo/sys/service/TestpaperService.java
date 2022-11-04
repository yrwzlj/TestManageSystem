package com.lczyfz.edp.springboot.demo.sys.service;

import com.lczyfz.edp.springboot.core.entity.Page;
import com.lczyfz.edp.springboot.core.entity.Role;
import com.lczyfz.edp.springboot.core.service.CrudService;
import com.lczyfz.edp.springboot.demo.sys.entity.TestPaper;
import com.lczyfz.edp.springboot.demo.sys.entity.example.TestPaperExample;
import com.lczyfz.edp.springboot.demo.sys.mapper.TestPaperMapper;
import com.lczyfz.edp.springboot.demo.sys.vo.PageVO.TestPaperPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author YRW
 * @description 针对表【testpaper】的数据库操作Service
 * @createDate 2022-09-28 20:30:28
 */
@Service
public class TestpaperService extends CrudService<TestPaperMapper, TestPaper, TestPaperExample> {

    @Autowired
    private TestPaperMapper testPaperMapper;

    @Override
    public int save(TestPaper entity) {
        int result = 0;

        if (entity.isNewRecord()) {
            entity.preInsert();
            result = testPaperMapper.insert(entity);
        } else {
            entity.preUpdate();
            result = this.mapper.updateByPrimaryKeySelective(entity);
        }

        return result;
    }

    public Page<TestPaper> findPage(Page<TestPaper> page, TestPaperPageVO pageVO) {
        TestPaperExample testPaperExample = new TestPaperExample();

        testPaperExample.createCriteria().andDelFlagEqualTo(Role.DEL_FLAG_NORMAL);

        if (com.lczyfz.edp.springboot.core.utils.StringUtils.isNotBlank(pageVO.getId())) {
            testPaperExample.getOredCriteria().get(0).andIdEqualTo(pageVO.getId());
        } else  if (com.lczyfz.edp.springboot.core.utils.StringUtils.isNotBlank(pageVO.getName())) {
            testPaperExample.getOredCriteria().get(0).andNameLike("%" + pageVO.getName() + "%");
        }

        page.setCount(mapper.countByExample(testPaperExample));
        // 每页条数
        testPaperExample.setPageSize(page.getMaxResults());
        // 第几页
        testPaperExample.setLimitStart(page.getFirstResult());

        page.setList(mapper.selectByExample(testPaperExample));

        return page;
    }

}
