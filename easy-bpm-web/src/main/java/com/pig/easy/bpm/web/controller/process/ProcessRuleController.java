package com.pig.easy.bpm.web.controller.process;


import com.pig.easy.bpm.web.vo.request.ProcessRuleQueryVO;
import com.pig.easy.bpm.web.vo.request.ProcessRuleSaveOrUpdateVO;
import org.springframework.web.bind.annotation.RequestMapping;
import com.pig.easy.bpm.api.dto.request.*;
import com.pig.easy.bpm.api.dto.response.*;
import org.springframework.web.bind.annotation.*;
import com.pig.easy.bpm.api.service.ProcessRuleService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import jakarta.validation.Valid;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import java.io.IOException;
import com.alibaba.excel.EasyExcel;

import io.swagger.v3.oas.annotations.Operation;

import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-04-09
 */
@RestController
@RequestMapping("/processRule")
public class ProcessRuleController {

    @Autowired
    ProcessRuleService service;

    @Operation(summary = "查询列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody ProcessRuleQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessRuleQueryDTO queryDTO = BeanUtils.switchToDTO(param, ProcessRuleQueryDTO.class);

        Result<PageInfo<ProcessRuleDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody ProcessRuleQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessRuleQueryDTO queryDTO = BeanUtils.switchToDTO(param, ProcessRuleQueryDTO.class);

        Result<List<ProcessRuleDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增")
    @PostMapping("/insert")
    public JsonResult insertProcessRule(@Valid @RequestBody ProcessRuleSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessRuleSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, ProcessRuleSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertProcessRule(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public JsonResult updateProcessRule(@Valid @RequestBody ProcessRuleSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessRuleSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, ProcessRuleSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateProcessRule(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody ProcessRuleSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessRuleSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, ProcessRuleSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteProcessRule(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody ProcessRuleQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessRuleQueryDTO queryDTO = BeanUtils.switchToDTO(param, ProcessRuleQueryDTO.class);

        Result<List<ProcessRuleDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), ProcessRuleExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody ProcessRuleSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getRuleId());

        Result<ProcessRuleDTO> result = service.getProcessRuleById(param.getRuleId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
