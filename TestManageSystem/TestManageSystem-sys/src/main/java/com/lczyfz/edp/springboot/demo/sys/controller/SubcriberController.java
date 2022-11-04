package com.lczyfz.edp.springboot.demo.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.lczyfz.edp.springboot.core.controller.BaseController;
import com.lczyfz.edp.springboot.core.entity.CommonResult;
import com.lczyfz.edp.springboot.core.entity.Page;
import com.lczyfz.edp.springboot.core.entity.PageResult;
import com.lczyfz.edp.springboot.core.utils.MsgCodeUtils;
import com.lczyfz.edp.springboot.core.validation.Create;
import com.lczyfz.edp.springboot.core.validation.Update;
import com.lczyfz.edp.springboot.demo.sys.entity.Subcriber;
import com.lczyfz.edp.springboot.demo.sys.mapper.SubcriberMapper;
import com.lczyfz.edp.springboot.demo.sys.service.SubcriberService;
import com.lczyfz.edp.springboot.demo.sys.utils.Md5Util;
import com.lczyfz.edp.springboot.demo.sys.vo.PageVO.SubcriberPageVO;
import com.lczyfz.edp.springboot.demo.sys.vo.SubcriberVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @Author YRW
 * @Date 2022/10/6
 * @Description com.lczyfz.edp.springboot.sys.controller
 * @version: 1.0
 */
@RestController
@RequestMapping(value = "${apiPath}/subcriber")
@Api(value = "SubcriberController", tags = "自定义用户接口")
public class SubcriberController extends BaseController {

    @Autowired
    private SubcriberService subcriberService;

    @Autowired
    private SubcriberMapper subcriberMapper;

    @ApiOperation(value = "创建用户(除id外都必需)", notes = "Request-DateTime样例：2022-10-08 20:30")
    @PostMapping(value = "/add")
    public CommonResult add(@Validated(Create.class) @RequestBody @ApiParam(value = "用户VO") SubcriberVO subcriberVO, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();

        //参数验证
        if (bindingResult.hasErrors()) {
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }

        if (subcriberMapper.searchByNameOrEmail(subcriberVO.getName(), subcriberVO.getEmail()).size() != 0) {
            result.fail(MsgCodeUtils.MSG_CODE_SYSTEM_ROLE_NAME_EXIST);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        Subcriber subcriber = new Subcriber();

        BeanUtils.copyProperties(subcriberVO, subcriber);
        subcriber.setCreateBy("root");
        subcriber.setUpdateBy("root");
        subcriber.setPassword(Md5Util.md5(subcriber.getPassword()));

        if (0 < subcriberService.save(subcriber)) {
            result.success("subcriber", subcriber);
            logger.info("新增成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "用户删除", notes = "Request-DateTime样例：2022-10-08 20:44")
    @PostMapping(value = "deleteByUser")
    public CommonResult deleteByUser(String name, String password) {
        CommonResult result = new CommonResult().init();

        if (StrUtil.isBlank(name) || StrUtil.isBlank(password)) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());
            return (CommonResult) result.end();
        }

        Subcriber subcriber = new Subcriber();
        subcriber.setUpdateBy(name);
        subcriber.setName(name);
        subcriber.setPassword(Md5Util.md5(password));

        int delete = subcriberService.delete(subcriber);
        if (0 < delete) {
            result.success();
            logger.info("删除成功！");
        } else {
            result.fail(MsgCodeUtils.MSG_CODE_UNKNOWN);
            logger.info("删除失败！");
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "超级管理员删除", notes = "Request-DateTime样例：2022-10-08 20:44")
    @PostMapping(value = "deleteByRoot/{id}")
    public CommonResult deleteByRoot(String id) {
        CommonResult result = new CommonResult().init();

        if (StrUtil.isBlank(id)) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());
            return (CommonResult) result.end();
        }

        Subcriber subcriber = new Subcriber();
        subcriber.setUpdateBy("root");
        subcriber.setId(id);

        int deleteNum = subcriberMapper.updateUpdateDateAndUpdateByAndDelFlagById(new Date(), "root", "1", id);
        if (0 < deleteNum) {
            result.success();
            logger.info("删除成功！");
        } else {
            result.fail(MsgCodeUtils.MSG_CODE_UNKNOWN);
            logger.info("删除失败！");
        }

        return (CommonResult) result.end();
    }


    @ApiOperation(value = "根据id修改", notes = "Request-DateTime样例：2022-10-08 20:30")
    @PostMapping(value = "update")
    public CommonResult update(@Validated(Update.class) @RequestBody @ApiParam(value = "用户VO") SubcriberVO subcriberVO, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();

        //参数验证
        if (bindingResult.hasErrors()) {
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }

        if (subcriberMapper.searchByNameOrEmail(subcriberVO.getName(), subcriberVO.getEmail()).size() != 0) {
            result.fail(MsgCodeUtils.MSG_CODE_SYSTEM_ROLE_NAME_EXIST);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        Subcriber subcriber = new Subcriber();
        BeanUtils.copyProperties(subcriberVO, subcriber);
        subcriber.setUpdateBy(subcriberVO.getName());

        if (0 < subcriberService.save(subcriber)) {
            result.success("subcriber", subcriber);
            logger.info("修改成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_UPDATADATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "通过name或email查询", notes = "Request-DateTime样例：2022-10-08 21:01")
    @PostMapping(value = "select")
    public PageResult<Subcriber> select(@Validated @RequestBody @ApiParam(value = "用户VO") SubcriberPageVO pageVO, BindingResult bindingResult) {
        PageResult<Subcriber> result = new PageResult<Subcriber>().init();

        //参数验证
        if (bindingResult.hasErrors()) {
            return (PageResult<Subcriber>) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }

        Page<Subcriber> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize(), pageVO.getOrderBy());

        result.success(subcriberService.findPage(page, pageVO));

        return (PageResult<Subcriber>) result.end();
    }
}
