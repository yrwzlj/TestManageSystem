package com.lczyfz.edp.springboot.demo.sys.service;

import com.lczyfz.edp.springboot.core.entity.Page;
import com.lczyfz.edp.springboot.core.entity.Role;
import com.lczyfz.edp.springboot.core.service.CrudService;
import com.lczyfz.edp.springboot.core.utils.StringUtils;
import com.lczyfz.edp.springboot.demo.sys.entity.Stu;
import com.lczyfz.edp.springboot.demo.sys.entity.StuExample;
import com.lczyfz.edp.springboot.demo.sys.mapper.StuMapper;
import com.lczyfz.edp.springboot.demo.sys.vo.StuPageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author LJX
 * @Date 2022/2/28 15:20
 * @Description com.lczyfz.edp.springboot.sys.service
 * @version: 1.0
 */
@Service
@Transactional(readOnly = true)
public class StuService extends CrudService<StuMapper, Stu, StuExample> {

    public Page<Stu> findPage(Page<Stu> page, StuPageVO pageVO) {
        StuExample stuExample = new StuExample();
        stuExample.createCriteria().andDelFlagEqualTo(Role.DEL_FLAG_NORMAL);
        //使用stuExample配置多个查询条件
        if (StringUtils.isNotBlank(pageVO.getName())) {
            stuExample.getOredCriteria().get(0).andNameLike(pageVO.getName() + "%");
        }
        page.setCount(mapper.countByExample(stuExample));
        // 每页条数
        stuExample.setPageSize(page.getMaxResults());
        // 第几页
        stuExample.setLimitStart(page.getFirstResult());
        page.setList(mapper.selectByExample(stuExample));
        return page;
    }
}
