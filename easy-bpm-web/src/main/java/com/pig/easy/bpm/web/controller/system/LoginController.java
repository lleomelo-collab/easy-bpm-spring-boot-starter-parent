package com.pig.easy.bpm.web.controller.system;

import com.pig.easy.bpm.auth.core.controller.BaseController;
import com.pig.easy.bpm.auth.dto.response.UserDTO;
import com.pig.easy.bpm.auth.service.UserService;
import com.pig.easy.bpm.common.annotation.Login;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.LoginVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * todo:
 *
 * @author : zhoulin.zhu
 * @date : 2021/2/23 12:10
 */
@RestController
@RequestMapping("/login")
@Tag(name = "登录管理")
public class LoginController extends BaseController {

    @Autowired
    UserService userService;

    @Operation(summary = "用户登录")
    @RequestMapping("/login")
    @Login(false)
    public JsonResult login(@RequestBody @Valid LoginVO loginVO){

        Result<UserDTO> result = userService.login(loginVO.getUsername(), loginVO.getPassword(),loginVO.getCode(),loginVO.getUuid());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "获取用户详情")
    @PostMapping("/getUserInfo/{username}")
    public JsonResult getUserInfo(@Parameter(required = true, name = "username", description = "用户名称", example = "pig") @PathVariable("username") String username) {

        Result<UserDTO> result = userService.getUserInfo(username);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public JsonResult logout() {

        Result<Boolean> result = userService.logout(currentUserInfo().getUserId(),currentUserInfo().getAccessToken());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
