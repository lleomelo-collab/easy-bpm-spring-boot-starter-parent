package com.pig.easy.bpm.web.controller.system;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.auth.core.controller.BaseController;
import com.pig.easy.bpm.auth.dto.request.UserQueryDTO;
import com.pig.easy.bpm.auth.dto.request.UserSaveOrUpdateDTO;
import com.pig.easy.bpm.auth.dto.response.TreeDTO;
import com.pig.easy.bpm.auth.dto.response.UserDTO;
import com.pig.easy.bpm.auth.dto.response.UserExportDTO;
import com.pig.easy.bpm.auth.service.UserService;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.OrganUserQueryVO;
import com.pig.easy.bpm.web.vo.request.UserQueryVO;
import com.pig.easy.bpm.web.vo.request.UserSaveOrUpdateVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-03-20
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    UserService service;

    @Operation(summary = "查询列表", description = "查询列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody UserQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        UserQueryDTO queryDTO = BeanUtils.switchToDTO(param, UserQueryDTO.class);

        Result<PageInfo<UserDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询列表", description = "查询列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody UserQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        UserQueryDTO queryDTO = BeanUtils.switchToDTO(param, UserQueryDTO.class);

        Result<List<UserDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增", description = "新增")
    @PostMapping("/insert")
    public JsonResult insertUser(@Valid @RequestBody UserSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        UserSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, UserSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertUser(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改", description = "修改")
    @PostMapping("/update")
    public JsonResult updateUser(@Valid @RequestBody UserSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        UserSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, UserSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateUser(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除", description = "删除")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody UserSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        UserSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, UserSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteUser(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载", description = "下载")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody UserQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        UserQueryDTO queryDTO = BeanUtils.switchToDTO(param, UserQueryDTO.class);

        Result<List<UserDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), UserExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取", description = "根据编号获取")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody UserSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getId());

        Result<UserDTO> result = service.getUserById(param.getId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "获取机构人员树", description = "获取机构人员树")
    @PostMapping("/getOrganUserTree")
    public JsonResult getOrganUserTree(@RequestBody @Valid OrganUserQueryVO organUserQueryVO) {

        Result<List<TreeDTO>> result = service.getOrganUserTree(organUserQueryVO.getTenantId(), organUserQueryVO.getParentId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
