package com.lczyfz.edp.springboot.demo.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.lczyfz.edp.springboot.core.controller.BaseController;
import com.lczyfz.edp.springboot.core.entity.CommonResult;
import com.lczyfz.edp.springboot.core.utils.MsgCodeUtils;
import com.lczyfz.edp.springboot.core.validation.Create;
import com.lczyfz.edp.springboot.demo.sys.constant.RoleConstant;
import com.lczyfz.edp.springboot.demo.sys.entity.Subcriber;
import com.lczyfz.edp.springboot.demo.sys.entity.Test;
import com.lczyfz.edp.springboot.demo.sys.entity.correctOrNot;
import com.lczyfz.edp.springboot.demo.sys.mapper.SubcriberMapper;
import com.lczyfz.edp.springboot.demo.sys.mapper.TestMapper;
import com.lczyfz.edp.springboot.demo.sys.mapper.TestRecordMapper;
import com.lczyfz.edp.springboot.demo.sys.service.TestRecordService;
import com.lczyfz.edp.springboot.demo.sys.utils.EmailDelay;
import com.lczyfz.edp.springboot.demo.sys.utils.EmailUtil;
import com.lczyfz.edp.springboot.demo.sys.utils.OssManagerUtil;
import com.lczyfz.edp.springboot.demo.sys.utils.RoleUtil;
import com.lczyfz.edp.springboot.demo.sys.vo.SubcriberVO;
import com.lczyfz.edp.springboot.demo.sys.vo.TestRecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author YRW
 * @Date 2022/10/9
 * @Description com.lczyfz.edp.springboot.sys.controller
 * @version: 1.0
 */
@RestController
@RequestMapping(value = "${apiPath}/testRecord")
@Api(value = "TestRecordController", tags = "考试记录接口")
public class TestRecordController extends BaseController {

    @Autowired
    RoleUtil roleUtil;

    @Autowired
    TestRecordService testRecordService;

    @Autowired
    TestRecordMapper testRecordMapper;

    @Autowired
    TestMapper testMapper;

    @Autowired
    SubcriberMapper subcriberMapper;

    private DelayQueue<EmailDelay> queue = new DelayQueue<>();

    public CommonResult add(@Validated(Create.class) @RequestBody @ApiParam(value = "考试记录VO") TestRecordVo testRecordVo, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (roleUtil.roleJudge(subcriberVO, RoleConstant.TEACHER)) {
            result = (CommonResult) roleUtil.getResult();
            logger.info(result.getErrMsg());
            return (CommonResult) result.end();
        }

        //参数验证
        if (bindingResult.hasErrors()) {
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }

        correctOrNot testRecord = new correctOrNot();

        BeanUtils.copyProperties(testRecordVo, testRecord);
        testRecord.setCreateBy(testRecordVo.getSubmiter());
        testRecord.setUpdateBy(testRecordVo.getSubmiter());

        if (0 < testRecordService.save(testRecord)) {
            result.success("testRecord", testRecord);
            logger.info("新增成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }



    @ApiOperation(value = "开始考试(必需 TestRecordId beginTIme testId)", notes = "Request-DateTime样例：2022-10-09 22:30")
    @PostMapping("/testBegin")
    public CommonResult testBegin(@RequestBody() @Validated() @ApiParam(value = "考试记录VO") TestRecordVo testRecordVo, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();

        if (testRecordVo.getBeginTime() == null || StrUtil.isBlank(testRecordVo.getId()) || StrUtil.isBlank(testRecordVo.getTestId())) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (!roleUtil.roleJudge(subcriberVO, RoleConstant.STUDENT)) {
            result = (CommonResult) roleUtil.getResult();
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        //参数验证
        if (bindingResult.hasErrors()) {
            return (CommonResult) result.failIllegalArgument(bindingResult.getFieldErrors()).end();
        }

        //提交试卷验证
        result = legalBeginTime(testRecordVo);
        if (result.getErrMsg() != null) {
            return (CommonResult) result.end();
        }

        int updateNum = testRecordMapper.updateBeginTimeAndUpdateDateAndUpdateByByTestIdAndSubmiter(testRecordVo.getBeginTime(),
                new Date(), username,testRecordVo.getTestId(),subcriberMapper.searchByName(username).get(0).getId());
        if (0 < updateNum) {
            result.success("testRecord", testRecordVo);
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build());
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    correctRemind(testRecordVo.getTestId(), testRecordVo.getSubmiter());
                }
            });
            logger.info("修改成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_UPDATADATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "提交试卷(必需：testId SubmitTime file)", notes = "Request-DateTime样例：2022-10-09 22:30")
    @PostMapping("/testEnd")
    public CommonResult testEnd(@ApiParam(value = "考试记录VO") TestRecordVo testRecordVo, MultipartFile file,@RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (!roleUtil.roleJudge(subcriberVO, RoleConstant.STUDENT)) {
            result = (CommonResult) roleUtil.getResult();
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        testRecordVo.setSubmiter(subcriberMapper.searchByName(username).get(0).getId());

        if (testRecordVo.getSubmitTime() == null || StrUtil.isBlank(testRecordVo.getTestId())) {
            result.fail(MsgCodeUtils.MSG_CODE_PARAMETER_IS_NULL);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        //提交试卷验证
        result = legalSubmitTime(testRecordVo);
        if (result.getErrMsg() != null) {
            return (CommonResult) result.end();
        }

        int updateNum = testRecordMapper.updateUpdateDateAndUpdateByAndSubmitTimeAndPaperByTestIdAndSubmiter(new Date(), username, testRecordVo.getSubmitTime()
                , OssManagerUtil.getUrl(file), testRecordVo.getTestId(),testRecordVo.getSubmiter());
        if (0 < updateNum) {
            result.success("testRecord", testRecordVo);
            ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                    new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build());
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    correctRemind(testRecordVo.getTestId(), testRecordVo.getSubmiter());
                }
            });
            logger.info("修改成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_UPDATADATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    /**
     * 提醒老师改卷
     */
    public void correctRemind(String testId, String stuId) {
        queue.put(new EmailDelay(testId + " " + stuId,
                TimeUnit.NANOSECONDS.convert(60*50, TimeUnit.SECONDS)));

        try {
            queue.take();
            List<correctOrNot> testRecords = testRecordMapper.searchAllByTestIdAndSubmiter(testId, stuId);
            List<Test> tests = testMapper.searchAllById(testId);
            List<Subcriber> subcribers = subcriberMapper.searchAllById(stuId);

            if (testRecords.size() != 0 && tests.size() != 0 && subcribers.size() != 0) {
                if (testRecords.get(0).getCorrectOrNot() == 0) {
                    String content = "老师 你好!" + subcribers.get(0).getName() + "在" + tests.get(0).getName() + "中作答的试卷尚未批改";
                    EmailUtil.emailSend(subcribers.get(0).getEmail(), content);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否在考试结束前半小时内提交
     *
     * @param testRecordVo
     * @return
     */
    public CommonResult legalSubmitTime(TestRecordVo testRecordVo) {
        CommonResult result = new CommonResult().init();

        List<Test> tests = testMapper.searchAllById(testRecordVo.getTestId());
        if (tests.size() == 0) {
            result.fail(MsgCodeUtils.MSG_CODE_DATA_NOT_EXIST);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        } else {
            if (tests.get(0).getEndTime().getTime() - testRecordVo.getSubmitTime().getTime() > 30 * 60 * 1000 || tests.get(0).getEndTime().getTime() - testRecordVo.getSubmitTime().getTime() < 0) {
                result.fail(MsgCodeUtils.MSC_DATA_UPDATADATA_ERROR);
                logger.info(result.getErrMsg());

                return (CommonResult) result.end();
            }
        }

        result = ifSubmit(testRecordVo);

        return result;
    }

    /**
     * 是否在考试开始后半小时内开始考试
     *
     * @param testRecordVo
     * @return
     */
    public CommonResult legalBeginTime(TestRecordVo testRecordVo) {
        CommonResult result = new CommonResult().init();

        List<Test> tests = testMapper.searchAllById(testRecordVo.getTestId());
        if (tests.size() == 0) {
            result.fail(MsgCodeUtils.MSG_CODE_DATA_NOT_EXIST);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        } else {
            if (testRecordVo.getBeginTime().getTime() - tests.get(0).getBeginTime().getTime() > 30 * 60 * 1000 || testRecordVo.getBeginTime().getTime() - tests.get(0).getBeginTime().getTime()  < 0) {
                result.fail(MsgCodeUtils.MSC_DATA_UPDATADATA_ERROR);
                logger.info(result.getErrMsg());

                return (CommonResult) result.end();
            }
        }

        return result;
    }

    /**
     * 是否已经提交过
     *
     * @param testRecordVo
     * @return
     */
    public CommonResult ifSubmit(TestRecordVo testRecordVo) {
        CommonResult result = new CommonResult().init();

        List<correctOrNot> testRecords = testRecordMapper.searchAllByTestIdAndSubmiter(testRecordVo.getTestId(), testRecordVo.getSubmiter());
        if (testRecords.size() == 0) {
            result.fail(MsgCodeUtils.MSG_CODE_DATA_NOT_EXIST);
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        } else {
            if (testRecords.get(0).getSubmitTime() != null) {
                result.fail(MsgCodeUtils.MSG_CODE_DATA_EXIST);
                logger.info(result.getErrMsg());

                return (CommonResult) result.end();
            }
        }

        return result;
    }

    @ApiOperation(value = "批改试卷(必需：testId Submiter file)", notes = "Request-DateTime样例：2022-10-09 22:30")
    @PostMapping("/correct")
    public CommonResult correctPaper(String testId,String submiter,MultipartFile file,@RequestParam String username, @RequestParam String password) {
        CommonResult result = new CommonResult().init();

        SubcriberVO subcriberVO = new SubcriberVO(username, password);
        //权限认证
        if (!roleUtil.roleJudge(subcriberVO, RoleConstant.TEACHER)) {
            result = (CommonResult) roleUtil.getResult();
            logger.info(result.getErrMsg());

            return (CommonResult) result.end();
        }

        String url = OssManagerUtil.getUrl(file);

        int updateNum = testRecordMapper.updateUpdateDateAndUpdateByAndCorrectOrNotAndCorrectPaperByTestIdAndSubmiter(new Date(),username,new Byte("1"),url,testId,submiter);

        if (0 < updateNum) {
            result.success("testRecord", updateNum);
            logger.info("批改成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_UPDATADATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

}
