package com.pig.easy.bpm.generator.controller;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.generator.dto.request.DbConfigQueryDTO;
import com.pig.easy.bpm.generator.dto.request.DbConfigSaveOrUpdateDTO;
import com.pig.easy.bpm.generator.dto.response.DbConfigDTO;
import com.pig.easy.bpm.generator.dto.response.DbConfigExportDTO;
import com.pig.easy.bpm.generator.service.DbConfigService;
import com.pig.easy.bpm.generator.vo.request.DbConfigQueryVO;
import com.pig.easy.bpm.generator.vo.request.DbConfigSaveOrUpdateVO;
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
 * 数据源表 前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/dbConfig")
public class DbConfigController {

    @Autowired
    DbConfigService service;

    @Operation(summary = "查询数据源表列表", description = "查询数据源表列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody DbConfigQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DbConfigQueryDTO queryDTO = BeanUtils.switchToDTO(param, DbConfigQueryDTO.class);

        Result<PageInfo<DbConfigDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询数据源表列表", description = "查询数据源表列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody DbConfigQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DbConfigQueryDTO queryDTO = BeanUtils.switchToDTO(param, DbConfigQueryDTO.class);

        Result<List<DbConfigDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增数据源表", description = "新增数据源表")
    @PostMapping("/insert")
    public JsonResult insertDbConfig(@Valid @RequestBody DbConfigSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DbConfigSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DbConfigSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertDbConfig(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改数据源表", description = "修改数据源表")
    @PostMapping("/update")
    public JsonResult updateDbConfig(@Valid @RequestBody DbConfigSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DbConfigSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DbConfigSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateDbConfig(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除数据源表", description = "删除数据源表")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody DbConfigSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DbConfigSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DbConfigSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteDbConfig(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载数据源表", description = "下载数据源表")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody DbConfigQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        DbConfigQueryDTO queryDTO = BeanUtils.switchToDTO(param, DbConfigQueryDTO.class);

        Result<List<DbConfigDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + "数据源表.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), DbConfigExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取数据源表", description = "根据编号获取数据源表")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody DbConfigSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getDbId());

        Result<DbConfigDTO> result = service.getDbConfigById(param.getDbId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
