package com.lczyfz.edp.springboot.demo.sys.utils;

import com.lczyfz.edp.springboot.core.entity.BaseResult;
import com.lczyfz.edp.springboot.core.entity.CommonResult;
import com.lczyfz.edp.springboot.core.utils.MsgCodeUtils;
import com.lczyfz.edp.springboot.demo.sys.constant.RoleConstant;
import com.lczyfz.edp.springboot.demo.sys.service.SubcriberService;
import com.lczyfz.edp.springboot.demo.sys.vo.SubcriberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author YRW
 * @Date 2022/10/6
 * @Description com.lczyfz.edp.springboot.sys.controller
 * @version: 1.0
 */
@Component
public class RoleUtil {

    @Autowired
    SubcriberService subcriberService;

    private CommonResult result ;

    public boolean roleJudge(SubcriberVO subcriberVO, String role) {
        result = new CommonResult().init();

        String dbRole = subcriberService.getRole(subcriberVO);

        if ("".equals(dbRole)) {
            result.fail(MsgCodeUtils.MSG_CODE_USERNAME_OR_PASSWORD_INCORRECT);

            return false;
        } else if (!dbRole.equals(role)) {
            result.fail(MsgCodeUtils.MSG_CODE_NOT_PERMISSION);

            return false;
        }

        return true;
    }

    public CommonResult getResult() {
        return result;
    }

    public void setResult(CommonResult result) {
        this.result = result;
    }
}
