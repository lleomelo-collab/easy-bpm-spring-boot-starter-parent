package com.pig.easy.bpm.web.controller.system;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.auth.core.controller.BaseController;
import com.pig.easy.bpm.auth.dto.request.DeptQueryDTO;
import com.pig.easy.bpm.auth.dto.request.DeptSaveOrUpdateDTO;
import com.pig.easy.bpm.auth.dto.response.DeptDTO;
import com.pig.easy.bpm.auth.dto.response.DeptExportDTO;
import com.pig.easy.bpm.auth.dto.response.TreeDTO;
import com.pig.easy.bpm.auth.service.DeptService;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.DeptQueryVO;
import com.pig.easy.bpm.web.vo.request.DeptSaveOrUpdateVO;
import com.pig.easy.bpm.web.vo.request.OrganDeptQueryVO;
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
 * 部门表 前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-03-20
 */
@RestController
@RequestMapping("/dept")
public class DeptController extends BaseController {

    @Autowired
    DeptService service;

    @Operation(summary = "查询部门表列表", description = "查询部门表列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody DeptQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DeptQueryDTO queryDTO = BeanUtils.switchToDTO(param, DeptQueryDTO.class);

        Result<PageInfo<DeptDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询部门表列表", description = "查询部门表列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody DeptQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DeptQueryDTO queryDTO = BeanUtils.switchToDTO(param, DeptQueryDTO.class);

        Result<List<DeptDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增部门表", description = "新增部门表")
    @PostMapping("/insert")
    public JsonResult insertDept(@Valid @RequestBody DeptSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DeptSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DeptSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertDept(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改部门表", description = "修改部门表")
    @PostMapping("/update")
    public JsonResult updateDept(@Valid @RequestBody DeptSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DeptSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DeptSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateDept(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除部门表", description = "删除部门表")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody DeptSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DeptSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DeptSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteDept(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载部门表", description = "下载部门表")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody DeptQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        DeptQueryDTO queryDTO = BeanUtils.switchToDTO(param, DeptQueryDTO.class);

        Result<List<DeptDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + "部门表.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), DeptExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取部门表", description = "根据编号获取部门表")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody DeptSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getDeptId());

        Result<DeptDTO> result = service.getDeptById(param.getDeptId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "获取机构部门树", description = "获取机构部门树")
    @PostMapping("/getOrganDeptTree")
    public JsonResult getOrganUserTree(@RequestBody @Valid OrganDeptQueryVO organDeptQueryVO) {

        Result<List<TreeDTO>> result = service.getOrganTree(organDeptQueryVO.getTenantId(),
                organDeptQueryVO.getCompanyId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

}
