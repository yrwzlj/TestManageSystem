package com.lczyfz.edp.springboot.demo.sys.controller;

import cn.hutool.core.util.ObjectUtil;
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
import com.lczyfz.edp.springboot.demo.sys.mapper.QuetionMapper;
import com.lczyfz.edp.springboot.demo.sys.service.QuetionService;
import com.lczyfz.edp.springboot.demo.sys.service.SubcriberService;
import com.lczyfz.edp.springboot.demo.sys.utils.OssManagerUtil;
import com.lczyfz.edp.springboot.demo.sys.utils.RoleUtil;
import com.lczyfz.edp.springboot.demo.sys.vo.*;
import com.lczyfz.edp.springboot.demo.sys.vo.PageVO.QuetionPageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author YRW
 * @Date 2022/10/6
 * @Description com.lczyfz.edp.springboot.sys.controller
 * @version: 1.0
 */
@RestController
@RequestMapping(value = "${apiPath}/quetion")
@Api(value = "QuetionController", tags = "题目接口")
public class QuetionController extends BaseController {

    @Autowired
    SubcriberService subcriberService;

    @Autowired
    QuetionMapper quetionMapper;

    @Autowired
    QuetionService quetionService;

    @Autowired
    RoleUtil roleUtil;


    @ApiOperation(value = "无图片创建题目(必需：name type content)", notes = "Request-DateTime样例：2022-10-08 20:30")
    @PostMapping("/add")
    public CommonResult addByContent(@Validated(Create.class) @RequestBody @ApiParam(value = "题目VO") QuetionVO quetionVO, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
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

        Quetion quetion = new Quetion();

        BeanUtils.copyProperties(quetionVO, quetion);
        quetion.setCreateBy(username);
        quetion.setUpdateBy(username);

        if (0 < quetionService.save(quetion)) {
            result.success("quetion", quetion);
            logger.info("新增成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "上传图片创建题目(必需 file/content name type)", notes = "Request-DateTime样例：2022-10-08 20:30")
    @PostMapping("/addImg")
    public CommonResult addByImg(@ApiParam(value = "题目VO") QuetionVO quetionVO,MultipartFile file,@RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();

        if (StrUtil.isBlank(quetionVO.getType()) || StrUtil.isBlank(quetionVO.getName()) || StrUtil.isBlank(username)
                || StrUtil.isBlank(password) || (StrUtil.isBlank(quetionVO.getContent()) && ObjectUtil.isEmpty(file))) {
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

        Quetion quetion = new Quetion();

        BeanUtils.copyProperties(quetionVO, quetion);
        quetion.setCreateBy(username);
        quetion.setUpdateBy(username);
        quetion.setImg(OssManagerUtil.getUrl(file));

        if (0 < quetionService.save(quetion)) {
            result.success("quetion", quetion);
            logger.info("新增成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_ADDDATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }


    @ApiOperation(value = "删除", notes = "Request-DateTime样例：2022-10-08 20:44")
    @PostMapping(value = "delete/{id}")
    public CommonResult deleteById(@PathVariable String id, @RequestParam String username, @RequestParam String password) {
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

        Quetion quetion = new Quetion();
        quetion.setUpdateBy(username);
        quetion.setId(id);

        quetionService.delete(quetion);
        if (0 < quetionService.delete(quetion)) {
            result.success();
            logger.info("删除成功！");
        } else {
            result.fail(MsgCodeUtils.MSG_CODE_UNKNOWN);
            logger.info("删除失败！");
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "修改", notes = "Request-DateTime样例：2022-10-08 20:30")
    @PostMapping("/update")
    public CommonResult update(@Validated(Update.class) @RequestBody @ApiParam(value = "题目VO") QuetionVO quetionVO, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
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

        Quetion quetion = new Quetion();
        BeanUtils.copyProperties(quetionVO, quetion);
        quetion.setUpdateBy(username);

        if (0 < quetionService.save(quetion)) {
            result.success("quetion", quetion);
            logger.info("修改成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_UPDATADATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "修改题目图片", notes = "Request-DateTime样例：2022-10-08 20:30")
    @PostMapping("/updateImg")
    public CommonResult updateImg(@ApiParam(value = "题目VO") QuetionVO quetionVO,MultipartFile file,@RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
        CommonResult result = new CommonResult().init();

        if (StrUtil.isBlank(quetionVO.getId()) || StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
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

        Quetion quetion = new Quetion();
        BeanUtils.copyProperties(quetionVO, quetion);
        quetion.setUpdateBy(username);
        quetion.setImg(OssManagerUtil.getUrl(file));

        if (0 < quetionService.save(quetion)) {
            result.success("quetion", quetion);
            logger.info("修改成功！");
        } else {
            result.fail(MsgCodeUtils.MSC_DATA_UPDATADATA_ERROR);
            logger.info(result.getErrMsg());
        }

        return (CommonResult) result.end();
    }

    @ApiOperation(value = "通过题目名字查询", notes = "Request-DateTime样例：2022-10-08 20:30")
    @PostMapping(value = "search")
    public PageResult<Quetion> searchByName(@Validated @RequestBody @ApiParam(value = "题目VO") QuetionPageVO pageVO, @RequestParam String username, @RequestParam String password, BindingResult bindingResult) {
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

        result.success(quetionService.findPageByName(page, pageVO));

        return (PageResult<Quetion>) result.end();
    }

}
