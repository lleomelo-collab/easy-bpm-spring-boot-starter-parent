package com.pig.easy.bpm.generator.controller;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.generator.dto.request.VersionQueryDTO;
import com.pig.easy.bpm.generator.dto.request.VersionSaveOrUpdateDTO;
import com.pig.easy.bpm.generator.dto.response.VersionDTO;
import com.pig.easy.bpm.generator.dto.response.VersionExportDTO;
import com.pig.easy.bpm.generator.service.VersionService;
import com.pig.easy.bpm.generator.vo.request.VersionQueryVO;
import com.pig.easy.bpm.generator.vo.request.VersionSaveOrUpdateVO;
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
 * 版本表 前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-03-10
 */
@RestController
@RequestMapping("/version")
public class VersionController {

    @Autowired
    VersionService service;

    @Operation(summary = "查询版本表列表", description = "查询版本表列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody VersionQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        VersionQueryDTO queryDTO = BeanUtils.switchToDTO(param, VersionQueryDTO.class);

        Result<PageInfo<VersionDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询版本表列表", description = "查询版本表列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody VersionQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        VersionQueryDTO queryDTO = BeanUtils.switchToDTO(param, VersionQueryDTO.class);

        Result<List<VersionDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增版本表", description = "新增版本表")
    @PostMapping("/insert")
    public JsonResult insertVersion(@Valid @RequestBody VersionSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        VersionSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, VersionSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertVersion(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改版本表", description = "修改版本表")
    @PostMapping("/update")
    public JsonResult updateVersion(@Valid @RequestBody VersionSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        VersionSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, VersionSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateVersion(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除版本表", description = "删除版本表")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody VersionSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        VersionSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, VersionSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteVersion(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载版本表", description = "下载版本表")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody VersionQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        VersionQueryDTO queryDTO = BeanUtils.switchToDTO(param, VersionQueryDTO.class);

        Result<List<VersionDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + "版本表.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), VersionExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取版本表", description = "根据编号获取版本表")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody VersionSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getId());

        Result<VersionDTO> result = service.getVersionById(param.getId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
