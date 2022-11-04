package com.lczyfz.edp.springboot.demo.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.lczyfz.edp.springboot.core.controller.BaseController;
import com.lczyfz.edp.springboot.core.entity.CommonResult;
import com.lczyfz.edp.springboot.core.entity.Page;
import com.lczyfz.edp.springboot.core.entity.PageResult;
import com.lczyfz.edp.springboot.core.utils.MsgCodeUtils;
import com.lczyfz.edp.springboot.core.validation.Create;
import com.lczyfz.edp.springboot.core.validation.Update;
import com.lczyfz.edp.springboot.demo.sys.constant.RoleConstant;
import com.lczyfz.edp.springboot.demo.sys.entity.Quetion;
import com.lczyfz.edp.springboot.demo.sys.entity.TestPaper;
import com.lczyfz.edp.springboot.demo.sys.service.QuetionService;
import com.lczyfz.edp.springboot.demo.sys.service.TestpaperService;
import com.lczyfz.edp.springboot.demo.sys.utils.RoleUtil;
import com.lczyfz.edp.springboot.demo.sys.vo.PageVO.QuetionPageVO;
import com.lczyfz.edp.springboot.demo.sys.vo.QuetionVO;
import com.lczyfz.edp.springboot.demo.sys.vo.SubcriberVO;
import com.lczyfz.edp.springboot.demo.sys.vo.PageVO.TestPaperPageVO;
import com.lczyfz.edp.springboot.demo.sys.vo.TestPaperVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author YRW
 * @Date 2022/2/25 17:14
 * @Description com.lczyfz.edp.springboot.sys.controller
 * @version: 1.0
 */
@RestController
@RequestMapping(value = "${apiPath}/testPaper")
@Api(value = "TestPaperController", tags = "试卷接口")
public class TestPaperController extends BaseController {

    @Autowired
    private TestpaperService testpaperService;

    @Autowired
    RoleUtil roleUtil;

    @Autowired
    QuetionService quetionService;

    @ApiOperation(value = "创建试卷", notes = "Request-DateTime样例：2022-10-08 20:30")
    @PostMapping(value = "/add")
    public CommonResult add(@Validated(Create.class) @RequestBody @ApiParam(value = "试卷VO") TestPaperVO testPaperVO, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (!roleUtil.roleJudge(subcriberVO, RoleConstant.TEACHER)) {
            result = (CommonResult) roleUtil.getResult();
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        //参数验证
        if (bindingResult.hasErrors()) {
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }

        TestPaper testPaper = new TestPaper();

        BeanUtils.copyProperties(testPaperVO, testPaper);
        testPaper.setCreateBy(username);
        testPaper.setUpdateBy(username);

        if (0 < testpaperService.save(testPaper)) {
            result.success("testPaper", testPaper);
            logger.info("新增成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "修改", notes = "Request-DateTime样例：2022-10-08 20:30")
    @PostMapping(value = "update")
    public CommonResult update(@Validated(Update.class) @RequestBody @ApiParam(value = "试卷VO") TestPaperVO testPaperVO, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (!roleUtil.roleJudge(subcriberVO, RoleConstant.TEACHER)) {
            result = (CommonResult) roleUtil.getResult();
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        //参数验证
        if (bindingResult.hasErrors()) {
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }

        TestPaper testPaper = new TestPaper();
        BeanUtils.copyProperties(testPaperVO, testPaper);
        testPaper.setUpdateBy(username);

        if (0 < testpaperService.save(testPaper)) {
            result.success("stu", testPaper);
            logger.info("修改成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_UPDATADATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "删除", notes = "Request-DateTime样例：2022-10-08 20:44")
    @PostMapping(value = "delete/{id}")
    public CommonResult delete(@PathVariable String id, @RequestParam String username, @RequestParam String password) {
        CommonResult result = new CommonResult().init();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password) || StrUtil.isBlank(id)) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (!roleUtil.roleJudge(subcriberVO, RoleConstant.TEACHER)) {
            result = (CommonResult) roleUtil.getResult();
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        TestPaper testPaper = new TestPaper();
        testPaper.setUpdateBy(username);
        testPaper.setId(id);

        testpaperService.delete(testPaper);
        if (0 < testpaperService.delete(testPaper)) {
            result.success();
            logger.info("删除成功！");
        } else {
            result.fail(MsgCodeUtils.MSG_CODE_UNKNOWN);
            logger.info("删除失败！");
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "查询", notes = "Request-DateTime样例：2022-10-08 21:01")
    @PostMapping(value = "select")
    public PageResult<TestPaper> select(@Validated @RequestBody TestPaperPageVO pageVO, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
        PageResult<TestPaper> result = new PageResult<TestPaper>().init();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());

            return (PageResult<TestPaper>) result.end();
        }

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (!roleUtil.roleJudge(subcriberVO, RoleConstant.TEACHER)) {
            result.setErrMsg(roleUtil.getResult().getErrMsg());
            result.setMsgCode(roleUtil.getResult().getMsgCode());
            logger.info(result.getErrMsg());

            return (PageResult<TestPaper>) result.end();
        }

        //参数验证
        if (bindingResult.hasErrors()) {
            return (PageResult<TestPaper>) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }
        Page<TestPaper> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize(), pageVO.getOrderBy());

        result.success(testpaperService.findPage(page, pageVO));

        return (PageResult<TestPaper>) result.end();
    }

    @ApiOperation(value = "查询试卷题目", notes = "Request-DateTime样例：2022-10-08 21:01")
    @PostMapping(value = "selectQueById")
    public PageResult<Quetion> selectQueById(@Validated @RequestBody TestPaperPageVO pageVO, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
        PageResult<Quetion> result = new PageResult<Quetion>().init();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());

            return (PageResult<Quetion>) result.end();
        }

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (!roleUtil.roleJudge(subcriberVO, RoleConstant.TEACHER)) {
            result.setErrMsg(roleUtil.getResult().getErrMsg());
            result.setMsgCode(roleUtil.getResult().getMsgCode());
            logger.info(result.getErrMsg());

            return (PageResult<Quetion>) result.end();
        }

        //参数验证
        if (bindingResult.hasErrors()) {
            return (PageResult<Quetion>) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }

        Page<Quetion> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize(), pageVO.getOrderBy());

        result.success(quetionService.findPageByTestPaper(page, pageVO.getId()));

        return (PageResult<Quetion>) result.end();
    }


    @ApiOperation(value = "添加题目(必需 id test_paper_belong试卷id)", notes = "Request-DateTime样例：2022-10-08 21:01")
    @PostMapping(value = "addQuetion")
    public CommonResult addQuetion(@Validated(Update.class) @RequestBody @ApiParam(value = "题目VO") QuetionVO quetionVO, @RequestParam String username, @RequestParam String password) {
        CommonResult result = new CommonResult().init();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (!roleUtil.roleJudge(subcriberVO, RoleConstant.TEACHER)) {
            result = (CommonResult) roleUtil.getResult();
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        Quetion quetion = new Quetion();
        quetion.setId(quetionVO.getId());
        quetion.setTestPaperBelong(quetionVO.getTestPaperBelong());

        if (0 < quetionService.save(quetion)) {
            result.success("quetion", quetion);
            logger.info("新增成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }
}
