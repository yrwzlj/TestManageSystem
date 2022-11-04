package com.lczyfz.edp.springboot.demo.sys.service;

import com.lczyfz.edp.springboot.core.entity.Page;
import com.lczyfz.edp.springboot.core.entity.Role;
import com.lczyfz.edp.springboot.core.service.CrudService;
import com.lczyfz.edp.springboot.demo.sys.entity.Quetion;
import com.lczyfz.edp.springboot.demo.sys.entity.example.QuetionExample;
import com.lczyfz.edp.springboot.demo.sys.mapper.QuetionMapper;
import com.lczyfz.edp.springboot.demo.sys.vo.PageVO.QuetionPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author YRW
 * @description 针对表【quetion】的数据库操作Service
 * @createDate 2022-09-28 20:30:08
 */
@Service
public class QuetionService extends CrudService<QuetionMapper, Quetion, QuetionExample> {

    @Autowired
    QuetionMapper quetionMapper;

    @Override
    public int save(Quetion entity) {
        int result = 0;

        if (entity.isNewRecord()) {
            entity.preInsert();
            result = quetionMapper.insert(entity);
        } else {
            entity.preUpdate();
            result = this.mapper.updateByPrimaryKeySelective(entity);
        }

        return result;
    }

    public Page<Quetion> findPageByName(Page<Quetion> page, QuetionPageVO pageVO) {
        QuetionExample queExample = new QuetionExample();

        queExample.createCriteria().andDelFlagEqualTo(Role.DEL_FLAG_NORMAL);

        //使用stuExample配置多个查询条件
        if (com.lczyfz.edp.springboot.core.utils.StringUtils.isNotBlank(pageVO.getName())) {
            queExample.getOredCriteria().get(0).andNameLike("%" + pageVO.getName() + "%");
        }


        page.setCount(mapper.countByExample(queExample));
        // 每页条数
        queExample.setPageSize(page.getMaxResults());
        // 第几页
        queExample.setLimitStart(page.getFirstResult());
        page.setList(mapper.selectByExample(queExample));

        return page;
    }

    public Page<Quetion> findPageByTestPaper(Page<Quetion> page, String testPaperId) {
        QuetionExample queExample = new QuetionExample();

        queExample.createCriteria().andDelFlagEqualTo(Role.DEL_FLAG_NORMAL);

        //使用stuExample配置多个查询条件
        if (com.lczyfz.edp.springboot.core.utils.StringUtils.isNotBlank(testPaperId)) {
            queExample.getOredCriteria().get(0).andTestPaperBelongEqualTo(testPaperId);
        }

        page.setCount(mapper.countByExample(queExample));
        // 每页条数
        queExample.setPageSize(page.getMaxResults());
        // 第几页
        queExample.setLimitStart(page.getFirstResult());
        page.setList(mapper.selectByExample(queExample));

        return page;
    }

}
