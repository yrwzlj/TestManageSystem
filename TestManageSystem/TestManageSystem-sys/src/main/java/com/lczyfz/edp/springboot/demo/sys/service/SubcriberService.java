package com.lczyfz.edp.springboot.demo.sys.service;

import com.lczyfz.edp.springboot.core.entity.Page;
import com.lczyfz.edp.springboot.core.entity.Role;
import com.lczyfz.edp.springboot.core.service.CrudService;
import com.lczyfz.edp.springboot.core.utils.StringUtils;
import com.lczyfz.edp.springboot.demo.sys.constant.RoleConstant;
import com.lczyfz.edp.springboot.demo.sys.entity.Subcriber;
import com.lczyfz.edp.springboot.demo.sys.entity.example.SubcriberExample;
import com.lczyfz.edp.springboot.demo.sys.mapper.SubcriberMapper;
import com.lczyfz.edp.springboot.demo.sys.utils.Md5Util;
import com.lczyfz.edp.springboot.demo.sys.vo.PageVO.SubcriberPageVO;
import com.lczyfz.edp.springboot.demo.sys.vo.SubcriberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author YRW
 * @description 针对表【user】的数据库操作Service
 * @createDate 2022-09-28 20:30:30
 * 由于userService 命名与依赖冲突 故更换名字
 */
@Service
public class SubcriberService extends CrudService<SubcriberMapper, Subcriber, SubcriberExample> {


    @Autowired
    SubcriberMapper subcriberMapper;

    public Subcriber getUserByName(String name) {
        SubcriberExample subcriberExample = new SubcriberExample();
        SubcriberExample.Criteria criteria = subcriberExample.createCriteria();
        criteria.andNameEqualTo(name);

        List<Subcriber> subcribers = subcriberMapper.selectByExample(subcriberExample);

        if (subcribers.size() == 0) {
            return null;
        }

        return subcribers.get(0);
    }

    public boolean hasName(String name) {
        Subcriber user = getUserByName(name);

        return user != null;
    }

    public String getRole(SubcriberVO subcriberVO) {
        List<Subcriber> subcribers = subcriberMapper.searchByNameAndPassword(subcriberVO.getName(), Md5Util.md5(subcriberVO.getPassword()));

        if (subcribers.size() == 0) {
            return "";
        }

        Subcriber subcriber = subcribers.get(0);

        if (subcriber != null) {
            if (subcriber.getTeacherOrNot() == 0) {
                return RoleConstant.STUDENT;
            } else {
                return RoleConstant.TEACHER;
            }
        }

        return "";
    }

    @Override
    public int save(Subcriber entity) {
        int result = 0;

        if (entity.isNewRecord()) {
            entity.preInsert();
            result = subcriberMapper.insert(entity);
        } else {
            entity.preUpdate();
            result = this.mapper.updateByPrimaryKeySelective(entity);
        }

        return result;
    }

    @Override
    public int delete(Subcriber entity) {
        int result = 0;
        if (entity != null) {
            entity.setDelFlag("1");
            entity.preUpdate();
            result = subcriberMapper.updateUpdateDateAndUpdateByAndDelFlagByNameAndPassword(entity.getUpdateDate(), entity.getUpdateBy(), entity.getDelFlag(), entity.getName(), entity.getPassword());
        }

        return result;
    }


    public Page<Subcriber> findPage(Page<Subcriber> page, SubcriberPageVO pageVO) {
        SubcriberExample subcriberExample = new SubcriberExample();

        subcriberExample.createCriteria().andDelFlagEqualTo(Role.DEL_FLAG_NORMAL);

        //使用stuExample配置多个查询条件
        if (StringUtils.isNotBlank(pageVO.getName())) {
            subcriberExample.getOredCriteria().get(0).andNameEqualTo(pageVO.getName());
        }
        if (StringUtils.isNotBlank(pageVO.getEmail())) {
            subcriberExample.getOredCriteria().get(0).andEmailEqualTo(pageVO.getEmail());
        }

        page.setCount(mapper.countByExample(subcriberExample));
        // 每页条数
        subcriberExample.setPageSize(page.getMaxResults());
        // 第几页
        subcriberExample.setLimitStart(page.getFirstResult());

        page.setList(mapper.selectByExample(subcriberExample));

        return page;
    }
}
