package com.pig.easy.bpm.web.controller.process;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.api.dto.request.ProcessQueryDTO;
import com.pig.easy.bpm.api.dto.request.ProcessSaveOrUpdateDTO;
import com.pig.easy.bpm.api.dto.response.DynamicFormDataDTO;
import com.pig.easy.bpm.api.dto.response.ProcessDTO;
import com.pig.easy.bpm.api.dto.response.ProcessExportDTO;
import com.pig.easy.bpm.api.dto.response.ProcessInfoDTO;
import com.pig.easy.bpm.api.service.ProcessService;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.ProcessQueryVO;
import com.pig.easy.bpm.web.vo.request.ProcessSaveOrUpdateVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


/**
 * <p>
 * 流程表 前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-04-09
 */
@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    ProcessService service;

    @Operation(summary = "查询流程表列表", description = "查询流程表列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody ProcessQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessQueryDTO queryDTO = BeanUtils.switchToDTO(param, ProcessQueryDTO.class);

        Result<PageInfo<ProcessInfoDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询流程表列表", description = "查询流程表列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody ProcessQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessQueryDTO queryDTO = BeanUtils.switchToDTO(param, ProcessQueryDTO.class);

        Result<List<ProcessInfoDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增流程表", description = "新增流程表")
    @PostMapping("/insert")
    public JsonResult insertProcess(@Valid @RequestBody ProcessSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, ProcessSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertProcess(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改流程表", description = "修改流程表")
    @PostMapping("/update")
    public JsonResult updateProcess(@Valid @RequestBody ProcessSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, ProcessSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateProcess(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除流程表", description = "删除流程表")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody ProcessSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, ProcessSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteProcess(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载流程表", description = "下载流程表")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody ProcessQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessQueryDTO queryDTO = BeanUtils.switchToDTO(param, ProcessQueryDTO.class);

        Result<List<ProcessInfoDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + "流程表.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), ProcessExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取流程表", description = "根据编号获取流程表")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody ProcessSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getProcessId());

        Result<ProcessDTO> result = service.getProcessById(param.getProcessId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "根据流程编号获取流程详细信息", description = "获取流程")
    @PostMapping("/getProcessById/{processId}")
    public JsonResult getProcessById(@Parameter(required = true, name = "processId", description = "流程编号", example = "1") @PathVariable("processId") Long processId) {

        Result<ProcessDTO> result = service.getProcessById(processId);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "根据流程KEY获取流程详细信息", description = "获取流程")
    @PostMapping("/getProcessByKey/{processKey}")
    public JsonResult getProcessByKey(@Parameter(required = true, name = "processKey", description = "流程编号", example = "pig:processKey") @PathVariable("processKey") String processKey) {

        Result<ProcessDTO> result = service.getProcessByProcessKey(processKey);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "初始化发起流程表单数据", description = "初始化发起流程表单数据")
    @PostMapping("/getInitStartFormData/{processKey}")
    public JsonResult getInitStartFormData(@Parameter(required = true, name = "processKey", description = "流程编号", example = "pig:processKey") @PathVariable("processKey") String processKey) {

        Result<DynamicFormDataDTO> result = service.getInitStartFormData(processKey);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
