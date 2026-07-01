package com.pig.easy.bpm.web.controller.system;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.auth.core.controller.BaseController;
import com.pig.easy.bpm.auth.dto.request.RoleGroupQueryDTO;
import com.pig.easy.bpm.auth.dto.request.RoleGroupSaveOrUpdateDTO;
import com.pig.easy.bpm.auth.dto.response.RoleGroupDTO;
import com.pig.easy.bpm.auth.dto.response.RoleGroupExportDTO;
import com.pig.easy.bpm.auth.service.RoleGroupService;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.RoleGroupQueryVO;
import com.pig.easy.bpm.web.vo.request.RoleGroupSaveOrUpdateVO;
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
 * 角色组 前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-03-20
 */
@RestController
@RequestMapping("/roleGroup")
public class RoleGroupController extends BaseController {

    @Autowired
    RoleGroupService service;

    @Operation(summary = "查询角色组列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody RoleGroupQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        RoleGroupQueryDTO queryDTO = BeanUtils.switchToDTO(param, RoleGroupQueryDTO.class);

        Result<PageInfo<RoleGroupDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询角色组列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody RoleGroupQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        RoleGroupQueryDTO queryDTO = BeanUtils.switchToDTO(param, RoleGroupQueryDTO.class);

        Result<List<RoleGroupDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增角色组")
    @PostMapping("/insert")
    public JsonResult insertRoleGroup(@Valid @RequestBody RoleGroupSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        RoleGroupSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, RoleGroupSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertRoleGroup(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改角色组")
    @PostMapping("/update")
    public JsonResult updateRoleGroup(@Valid @RequestBody RoleGroupSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        RoleGroupSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, RoleGroupSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateRoleGroup(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除角色组")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody RoleGroupSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        RoleGroupSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, RoleGroupSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteRoleGroup(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载角色组")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody RoleGroupQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        RoleGroupQueryDTO queryDTO = BeanUtils.switchToDTO(param, RoleGroupQueryDTO.class);

        Result<List<RoleGroupDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + "角色组.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), RoleGroupExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取角色组")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody RoleGroupSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getRoleGroupId());

        Result<RoleGroupDTO> result = service.getRoleGroupById(param.getRoleGroupId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
