package com.lczyfz.edp.springboot.demo.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.lczyfz.edp.springboot.core.controller.BaseController;
import com.lczyfz.edp.springboot.core.entity.CommonResult;
import com.lczyfz.edp.springboot.core.entity.Page;
import com.lczyfz.edp.springboot.core.entity.PageResult;
import com.lczyfz.edp.springboot.core.utils.MsgCodeUtils;
import com.lczyfz.edp.springboot.core.validation.Create;
import com.lczyfz.edp.springboot.core.vo.PageVO;
import com.lczyfz.edp.springboot.demo.sys.constant.RoleConstant;
import com.lczyfz.edp.springboot.demo.sys.entity.Test;
import com.lczyfz.edp.springboot.demo.sys.entity.TestPaper;
import com.lczyfz.edp.springboot.demo.sys.entity.correctOrNot;
import com.lczyfz.edp.springboot.demo.sys.mapper.SubcriberMapper;
import com.lczyfz.edp.springboot.demo.sys.mapper.TestMapper;
import com.lczyfz.edp.springboot.demo.sys.mapper.TestPaperMapper;
import com.lczyfz.edp.springboot.demo.sys.mapper.TestRecordMapper;
import com.lczyfz.edp.springboot.demo.sys.service.TestRecordService;
import com.lczyfz.edp.springboot.demo.sys.service.TestService;
import com.lczyfz.edp.springboot.demo.sys.service.TestpaperService;
import com.lczyfz.edp.springboot.demo.sys.utils.DateUtil;
import com.lczyfz.edp.springboot.demo.sys.utils.RoleUtil;
import com.lczyfz.edp.springboot.demo.sys.vo.SubcriberVO;
import com.lczyfz.edp.springboot.demo.sys.vo.PageVO.TestPaperPageVO;
import com.lczyfz.edp.springboot.demo.sys.vo.TestRecordVo;
import com.lczyfz.edp.springboot.demo.sys.vo.TestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author YRW
 * @Date 2022/10/9
 * @Description com.lczyfz.edp.springboot.sys.controller
 * @version: 1.0
 */
@RestController
@RequestMapping(value = "${apiPath}/test")
@Api(value = "TestController", tags = "考试接口")
public class TestController extends BaseController {

    @Autowired
    TestService testService;

    @Autowired
    RoleUtil roleUtil;

    @Autowired
    TestMapper testMapper;

    @Autowired
    TestPaperMapper testPaperMapper;

    @Autowired
    TestpaperService testpaperService;

    @Autowired
    SubcriberMapper subcriberMapper;

    @Autowired
    TestRecordService testRecordService;

    @Autowired
    TestRecordMapper testRecordMapper;

    @ApiOperation(value = "创建考试(必需：name beginTime EndTime teacherId)", notes = "Request-DateTime样例：2022-10-09 15：20")
    @PostMapping("/add")
    public CommonResult add(@Validated(Create.class) @RequestBody @ApiParam(value = "考试VO") TestVO testVO, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        if (testVO.getEndTime().getTime() <= testVO.getBeginTime().getTime()) {
            result.fail(MsgCodeUtils.MSG_CODE_ILLEGAL_ARGUMENT);
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

        Test test = new Test();

        BeanUtils.copyProperties(testVO, test);
        test.setCreateBy(username);
        test.setUpdateBy(username);

        if (0 < testService.save(test)) {
            result.success("test", test);
            logger.info("新增成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "分发试卷", notes = "Request-DateTime样例：2022-10-09 15：20")
    @PostMapping("/dispatch")
    public CommonResult dispatchPaper(@RequestParam @ApiParam(value = "学生id集合") ArrayList<String> stus, String testId, String papId, @RequestParam String username, @RequestParam String password) {
        CommonResult result = new CommonResult().init();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password) || StrUtil.isBlank(testId) || StrUtil.isBlank(papId) || stus.size()==0) {
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

        int dispatchNum = 0;

        for (String s : stus) {
            dispatchNum += testRecordMapper.updatePapIdAndUpdateByAndUpdateDateByTestIdAndSubmiter(papId, username, new Date(), testId, s);
        }

        if (stus.size() == dispatchNum) {
            result.success("dispatch", dispatchNum);
            logger.info("分发试卷成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "撤回试卷(必需 考试id testId 试卷id papId)", notes = "Request-DateTime样例：2022-10-09 19：40")
    @PostMapping("/quash")
    public CommonResult quashPaper(@Validated() @RequestBody @ApiParam(value = "考试记录VO") TestRecordVo testRecordVO, @RequestParam String username, @RequestParam String password) {
        CommonResult result = new CommonResult().init();

        String testId = testRecordVO.getTestId();
        String papId = testRecordVO.getPapId();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password) || StrUtil.isBlank(papId) || StrUtil.isBlank(testId)) {
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

        List<Test> tests = testMapper.searchAllById(testId);

        if (tests.size() == 0) {
            result.fail(MsgCodeUtils.MSG_CODE_DATA_NOT_EXIST);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        } else {
            if (!DateUtil.afterNow(tests.get(0).getBeginTime())) {
                result.fail(MsgCodeUtils.MSG_CODE_DATA_NOT_EXIST);
                logger.info(result.getErrMsg());

                return (CommonResult) result.end();
            } else {
                int updateNum = testRecordMapper.updatePapIdAndUpdateDateAndUpdateByByTestIdAndPapId(null,new Date(),username,testId,papId);

                if (0 < updateNum) {
                    result.success("dispatch", papId);
                    logger.info("撤回试卷成功！");
                } else {
                    result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
                    logger.info(result.getErrMsg());
                }

                return (CommonResult) result.end();
            }
        }
    }

    @ApiOperation(value = "查询所有试卷", notes = "Request-DateTime样例：2022-10-09 19：40")
    @PostMapping("/search")
    public PageResult<TestPaper> searchPaper(@Validated @RequestBody @ApiParam("pageVO") PageVO pageVO, @RequestParam String username, @RequestParam String password) {
        PageResult<TestPaper> result = new PageResult<TestPaper>().init();

        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());
            return (PageResult<TestPaper>) result.end();
        }

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (!roleUtil.roleJudge(subcriberVO, RoleConstant.TEACHER)) {
            result = searchStuPaper(pageVO, username, password);
        } else {
            result = searchTeacherPaper(pageVO, username, password);
        }

        return result;
    }

    /**
     * 查询学生所有试卷
     *
     * @param username
     * @param password
     * @return
     */
    public PageResult<TestPaper> searchStuPaper(PageVO pageVO, String username, String password) {
        PageResult<TestPaper> result = new PageResult<TestPaper>().init();

        List<correctOrNot> testRecords = testRecordMapper.searchAllBySubmiter(subcriberMapper.searchByName(username).get(0).getId());

        List<TestPaper> testPapers = new ArrayList<>();

        for (correctOrNot testRecord : testRecords) {
            TestPaper testPaper = testPaperMapper.searchOneById(testRecord.getPapId());
            if (testPaper != null) {
                testPapers.add(testPaper);
            }
        }

        Page<TestPaper> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize(), pageVO.getOrderBy());

        page.setList(testPapers);
        page.setCount(testPapers.size());

        result.success(page);

        return (PageResult<TestPaper>) result.end();

    }

    /**
     * 查询教师所有试卷
     *
     * @param username
     * @param password
     * @return
     */
    public PageResult<TestPaper> searchTeacherPaper(PageVO pageVO, String username, String password) {
        PageResult<TestPaper> result = new PageResult<TestPaper>().init();

        Page<TestPaper> page = new Page<>(pageVO.getPageNo(), pageVO.getPageSize(), pageVO.getOrderBy());

        TestPaperPageVO testPaperPageVO = new TestPaperPageVO();
        testPaperPageVO.copyPageVO(pageVO);

        result.success(testpaperService.findPage(page, testPaperPageVO));

        return (PageResult<TestPaper>) result.end();
    }

    @ApiOperation(value = "添加参与考试的学生(必需：testId submiter)", notes = "Request-DateTime样例：2022-10-09 21：00")
    @PostMapping("/addStu")
    public CommonResult addStu(@Validated @RequestBody @ApiParam TestRecordVo testRecordVo, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
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

        correctOrNot testRecord = new correctOrNot();
        testRecord.setUpdateBy(username);
        testRecord.setCreateBy(username);
        testRecord.setTestId(testRecordVo.getTestId());
        testRecord.setSubmiter(testRecordVo.getSubmiter());

        if (0 < testRecordService.save(testRecord)) {
            result.success("testRecord", testRecord);
            logger.info("新增成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }


}
