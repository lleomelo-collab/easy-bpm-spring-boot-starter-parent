package com.pig.easy.bpm.web.controller.system;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.api.service.ProcessService;
import com.pig.easy.bpm.auth.dto.request.DeptQueryDTO;
import com.pig.easy.bpm.auth.dto.request.DictQueryDTO;
import com.pig.easy.bpm.auth.dto.request.DictSaveOrUpdateDTO;
import com.pig.easy.bpm.auth.dto.response.DictDTO;
import com.pig.easy.bpm.auth.dto.response.DictExportDTO;
import com.pig.easy.bpm.auth.dto.response.ItemDTO;
import com.pig.easy.bpm.auth.service.DeptService;
import com.pig.easy.bpm.auth.service.DictService;
import com.pig.easy.bpm.auth.service.RoleGroupService;
import com.pig.easy.bpm.auth.service.RoleService;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.DictQueryVO;
import com.pig.easy.bpm.web.vo.request.DictSaveOrUpdateVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-04-06
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    DictService service;
    @Autowired
    DeptService deptService;
    @Autowired
    RoleGroupService roleGroupService;
    @Autowired
    RoleService roleService;
    @Autowired
    ProcessService processService;

    @Operation(summary = "查询字典表列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody DictQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DictQueryDTO queryDTO = BeanUtils.switchToDTO(param, DictQueryDTO.class);

        Result<PageInfo<DictDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询字典表列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody DictQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DictQueryDTO queryDTO = BeanUtils.switchToDTO(param, DictQueryDTO.class);

        Result<List<DictDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增字典表")
    @PostMapping("/insert")
    public JsonResult insertDict(@Valid @RequestBody DictSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DictSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DictSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertDict(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改字典表")
    @PostMapping("/update")
    public JsonResult updateDict(@Valid @RequestBody DictSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DictSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DictSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateDict(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除字典表")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody DictSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DictSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DictSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteDict(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载字典表")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody DictQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        DictQueryDTO queryDTO = BeanUtils.switchToDTO(param, DictQueryDTO.class);

        Result<List<DictDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + "字典表.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), DictExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取字典表")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody DictSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getDictId());

        Result<DictDTO> result = service.getDictById(param.getDictId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询字典值")
    @PostMapping("/getDictListByDictCode/{dictCode}")
    public JsonResult getDictListByDictCode(
            @Parameter(required = true, name = "dictCode", description = "字典编码", example = "pig:dict:processStatus") @PathVariable("dictCode") String dictCode) {

        if(StringUtils.isEmpty(dictCode)){
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }

        Result<List<ItemDTO>> result = service.getDictListByDictCode(dictCode);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询角色字典")
    @PostMapping("/getRoleDictByTenantId/{tenantId}")
    public JsonResult getRoleDictByTenantId(@Parameter(required = true, name = "tenantId", description = "租户编号", example = "pig") @PathVariable("tenantId") String tenantId) {

        if(StringUtils.isEmpty(tenantId)){
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }
        Result<List<ItemDTO>> result = roleService.getRoleDictByTenantId(tenantId);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询角色组字典")
    @PostMapping("/getRoleGroupDictByTenantId/{tenantId}")
    public JsonResult getRoleGroupDictByTenantId(@Parameter(required = true, name = "tenantId", description = "租户编号", example = "pig") @PathVariable("tenantId") String tenantId) {

        if(StringUtils.isEmpty(tenantId)){
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }
        Result<List<ItemDTO>> result = roleGroupService.getRoleGroupDict(tenantId);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询部门字典")
    @PostMapping("/getDeptListByTenantId/{tenantId}")
    public JsonResult getDeptListByTenantId(@Parameter(required = true, name = "tenantId", description = "租户编号", example = "pig") @PathVariable("tenantId") String tenantId) {

        if(StringUtils.isEmpty(tenantId)){
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }
        DeptQueryDTO deptQueryDTO = new  DeptQueryDTO();
        deptQueryDTO.setTenantId(tenantId);
        Result<List<ItemDTO>> result = deptService.getDeptItemList(deptQueryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询部门字典")
    @PostMapping("/getDeptIdItemList/{tenantId}")
    public JsonResult getDeptIdItemList(@Parameter(required = true, name = "tenantId", description = "租户编号", example = "pig") @PathVariable("tenantId") String tenantId) {

        if(StringUtils.isEmpty(tenantId)){
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }
        DeptQueryDTO deptQueryDTO = new  DeptQueryDTO();
        deptQueryDTO.setTenantId(tenantId);
        Result<List<ItemDTO>> result = deptService.getDeptIdItemList(deptQueryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }



    @Operation(summary = "获取流程列表字典")
    @PostMapping("/getProcessDict/{tenantId}")
    public JsonResult getProcessDict(@Parameter(required = true, name = "tenantId", description = "租户编号", example = "pig") @PathVariable("tenantId") String tenantId) {

        if(StringUtils.isEmpty(tenantId)){
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }
        Result<List<ItemDTO>> result = processService.getProcessDict(tenantId);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
