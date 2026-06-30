package com.pig.easy.bpm.generator.controller;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.generator.dto.request.TableStrategyConfigQueryDTO;
import com.pig.easy.bpm.generator.dto.request.TableStrategyConfigSaveOrUpdateDTO;
import com.pig.easy.bpm.generator.dto.response.TableStrategyConfigDTO;
import com.pig.easy.bpm.generator.dto.response.TableStrategyConfigExportDTO;
import com.pig.easy.bpm.generator.service.TableStrategyConfigService;
import com.pig.easy.bpm.generator.vo.request.TableStrategyConfigQueryVO;
import com.pig.easy.bpm.generator.vo.request.TableStrategyConfigSaveOrUpdateVO;
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
 * 配置策略表 前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-03-11
 */
@RestController
@RequestMapping("/tableStrategyConfig")
public class TableStrategyConfigController {

    @Autowired
    TableStrategyConfigService service;

    @Operation(summary = "查询配置策略表列表", description = "查询配置策略表列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody TableStrategyConfigQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        TableStrategyConfigQueryDTO queryDTO = BeanUtils.switchToDTO(param, TableStrategyConfigQueryDTO.class);

        Result<PageInfo<TableStrategyConfigDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询配置策略表列表", description = "查询配置策略表列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody TableStrategyConfigQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        TableStrategyConfigQueryDTO queryDTO = BeanUtils.switchToDTO(param, TableStrategyConfigQueryDTO.class);

        Result<List<TableStrategyConfigDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增配置策略表", description = "新增配置策略表")
    @PostMapping("/insert")
    public JsonResult insertTableStrategyConfig(@Valid @RequestBody TableStrategyConfigSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        TableStrategyConfigSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, TableStrategyConfigSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertTableStrategyConfig(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改配置策略表", description = "修改配置策略表")
    @PostMapping("/update")
    public JsonResult updateTableStrategyConfig(@Valid @RequestBody TableStrategyConfigSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        TableStrategyConfigSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, TableStrategyConfigSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateTableStrategyConfig(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除配置策略表", description = "删除配置策略表")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody TableStrategyConfigSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        TableStrategyConfigSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, TableStrategyConfigSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteTableStrategyConfig(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载配置策略表", description = "下载配置策略表")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody TableStrategyConfigQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        TableStrategyConfigQueryDTO queryDTO = BeanUtils.switchToDTO(param, TableStrategyConfigQueryDTO.class);

        Result<List<TableStrategyConfigDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + "配置策略表.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), TableStrategyConfigExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取配置策略表", description = "根据编号获取配置策略表")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody TableStrategyConfigSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getConfigId());

        Result<TableStrategyConfigDTO> result = service.getTableStrategyConfigById(param.getConfigId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
