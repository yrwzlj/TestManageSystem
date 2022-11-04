package com.lczyfz.edp.springboot.demo.sys.controller;

import com.lczyfz.edp.springboot.core.controller.BaseController;
import com.lczyfz.edp.springboot.core.entity.CommonResult;
import com.lczyfz.edp.springboot.core.entity.Page;
import com.lczyfz.edp.springboot.core.entity.PageResult;
import com.lczyfz.edp.springboot.core.utils.MsgCodeUtils;
import com.lczyfz.edp.springboot.core.validation.Create;
import com.lczyfz.edp.springboot.core.validation.Update;
import com.lczyfz.edp.springboot.demo.sys.entity.Stu;
import com.lczyfz.edp.springboot.demo.sys.service.StuService;
import com.lczyfz.edp.springboot.demo.sys.vo.StuPageVO;
import com.lczyfz.edp.springboot.demo.sys.vo.StuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author LJX
 * @Date 2022/2/25 17:14
 * @Description com.lczyfz.edp.springboot.sys.controller
 * @version: 1.0
 */
@RestController
@RequestMapping(value = "${apiPath}/stu/")
@Api(value = "StuController", tags = "学生接口")
public class StuController extends BaseController {
    @Autowired
    private StuService stuService;

    @ApiOperation(value = "创建用户", notes = "创建用户，getRequest-DateTime样例：2018-09-29 11:26:20")
    @PostMapping(value = "v1.0/add")
    public CommonResult add(@Validated(Create.class) @RequestBody @ApiParam(value = "学生VO") StuVO stuVO, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();
        //参数验证
        if (bindingResult.hasErrors()) {
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }
        Stu stu = new Stu();
        BeanUtils.copyProperties(stuVO, stu);
        if (0 < stuService.save(stu)) {
            result.success("stu", stu);
            logger.info("新增成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
            logger.info(result.getErrMsg());
        }
        return (CommonResult) result.end();
    }

    @ApiOperation(value = "修改", notes = "Request-DateTime样例：2018-09-29 11:26:20")
    @PostMapping(value = "v1.0/update")
    public CommonResult update(@Validated(Update.class) @RequestBody @ApiParam(value = "学生VO") StuVO stuVO, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();
        //参数验证
        if (bindingResult.hasErrors()) {
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }
        Stu stu = new Stu();
        BeanUtils.copyProperties(stuVO, stu);
        if (0 < stuService.save(stu)) {
            result.success("stu", stu);
            logger.info("修改成功！");

        } else {
            result.fail(MsgCodeUtils.MSC_DATA_UPDATADATA_ERROR);
            logger.info(result.getErrMsg());
        }
        return (CommonResult) result.end();
    }

    @ApiOperation(value = "删除", notes = "Request-DateTime样例：2018-09-29 11:26:20")
    @PostMapping(value = "v1.0/delete/{id}")
    public CommonResult delete(@PathVariable String id) {
        CommonResult result = new CommonResult().init();
        Stu stu = new Stu();
        stu.setId(id);
        stuService.delete(stu);
        if (0 < stuService.delete(stu)) {
            result.success();
            logger.info("删除成功！");
        } else {
            result.fail(MsgCodeUtils.MSG_CODE_UNKNOWN);
            logger.info("删除失败！");
        }
        return (CommonResult) result.end();
    }

    @ApiOperation(value = "查询", notes = "Request-DateTime样例：2018-09-29 11:26:20")
    @PostMapping(value = "v1.0/select")
    public PageResult<Stu> select(@Validated @RequestBody StuPageVO pageVO, BindingResult bindingResult) {
        PageResult<Stu> result = new PageResult<Stu>().init();
        //参数验证
        if (bindingResult.hasErrors()) {
            return (PageResult<Stu>) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }
        Page<Stu> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize(), pageVO.getOrderBy());
        result.success(stuService.findPage(page, pageVO));
        return (PageResult<Stu>) result.end();
    }
}
