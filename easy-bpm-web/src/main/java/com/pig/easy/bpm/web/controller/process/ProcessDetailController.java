package com.pig.easy.bpm.web.controller.process;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.api.dto.request.ProcessDetailQueryDTO;
import com.pig.easy.bpm.api.dto.request.ProcessDetailSaveOrUpdateDTO;
import com.pig.easy.bpm.api.dto.request.ProcessPublishDTO;
import com.pig.easy.bpm.api.dto.response.ProcessDetailDTO;
import com.pig.easy.bpm.api.dto.response.ProcessDetailExportDTO;
import com.pig.easy.bpm.api.service.ProcessDetailService;
import com.pig.easy.bpm.auth.core.controller.BaseController;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.*;
import com.pig.easy.bpm.web.vo.request.ProcessDetailQueryVO;
import com.pig.easy.bpm.web.vo.request.ProcessDetailSaveOrUpdateVO;
import com.pig.easy.bpm.web.vo.request.ProcessPublishVO;
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
 * 前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-04-09
 */
@RestController
@RequestMapping("/processDetail")
public class ProcessDetailController extends BaseController {

    @Autowired
    ProcessDetailService service;

    @Operation(summary = "查询列表", description = "查询列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody ProcessDetailQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessDetailQueryDTO queryDTO = BeanUtils.switchToDTO(param, ProcessDetailQueryDTO.class);

        Result<PageInfo<ProcessDetailDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询列表", description = "查询列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody ProcessDetailQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessDetailQueryDTO queryDTO = BeanUtils.switchToDTO(param, ProcessDetailQueryDTO.class);

        Result<List<ProcessDetailDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增", description = "新增")
    @PostMapping("/insert")
    public JsonResult insertProcessDetail(@Valid @RequestBody ProcessDetailSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessDetailSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, ProcessDetailSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertProcessDetail(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改", description = "修改")
    @PostMapping("/update")
    public JsonResult updateProcessDetail(@Valid @RequestBody ProcessDetailSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessDetailSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, ProcessDetailSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateProcessDetail(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除", description = "删除")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody ProcessDetailSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessDetailSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, ProcessDetailSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteProcessDetail(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载", description = "下载")
    @PostMapping("download")
    public void download(HttpServletResponse response, @Valid @RequestBody ProcessDetailQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        ProcessDetailQueryDTO queryDTO = BeanUtils.switchToDTO(param, ProcessDetailQueryDTO.class);

        Result<List<ProcessDetailDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), ProcessDetailExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取", description = "根据编号获取")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody ProcessDetailSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getProcessDetailId());

        Result<ProcessDetailDTO> result = service.getProcessDetailById(param.getProcessDetailId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "发布流程", description = "发布流程")
    @PostMapping("/publish")
    public JsonResult publish(@RequestBody ProcessPublishVO processPublishVO) {

        EasyBpmAsset.isAssetEmpty(processPublishVO);
        EasyBpmAsset.isAssetEmpty(processPublishVO.getTenantId());
        EasyBpmAsset.isAssetEmpty(processPublishVO.getProcessKey());
        EasyBpmAsset.isAssetEmpty(processPublishVO.getProcessDetailId());
        EasyBpmAsset.isAssetEmpty(processPublishVO.getProcessXml());

        ProcessPublishDTO processPublishDTO = switchToDTO(processPublishVO, ProcessPublishDTO.class);
        processPublishDTO.setOperatorId(currentUserInfo().getUserId());
        processPublishDTO.setOperatorName(currentUserInfo().getRealName());

        Result<Boolean> result = service.processPublish(processPublishDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());

    }


    @Operation(summary = "修改默认版本", description = "修改默认版本")
    @PostMapping("/updateDefaultVersion/{processId}/{processDetailId}")
    public JsonResult updateDefaultVersion(
            @Parameter(required = true, name = "processId", description = "流程编号", example = "1") @PathVariable("processId") Long processId,
            @Parameter(required = true, name = "processDetailId", description = "流程详细编号", example = "1") @PathVariable("processDetailId") Long processDetailId
    ) {
        Result<Boolean> result = service.updateDefaultVersion(processId, processDetailId, currentUserInfo().getUserId(), currentUserInfo().getRealName());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());

    }

    @Operation(summary = "获取流程详细信息", description = "获取流程详细信息")
    @PostMapping("/getProcessDetailById/{processDetailId}")
    public JsonResult getProcessDetailById(
            @Parameter(required = true, name = "processDetailId", description = "流程详细编号", example = "1") @PathVariable("processDetailId") Long processDetailId) {
        Result<ProcessDetailDTO> result = service.getProcessDetailById(processDetailId);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增或修改流程详细信息", description = "新增或修改流程详细信息")
    @PostMapping("/insertOrUpdate")
    public JsonResult insertOrUpdate(@Valid @RequestBody ProcessDetailSaveOrUpdateVO processDetailVO) {

        EasyBpmAsset.isAssetEmpty(processDetailVO);
        EasyBpmAsset.isAssetEmpty(processDetailVO.getProcessId());
        EasyBpmAsset.isAssetEmpty(processDetailVO.getTenantId());

        ProcessDetailSaveOrUpdateDTO processDetailReqDTO = switchToDTO(processDetailVO, ProcessDetailSaveOrUpdateDTO.class);
        processDetailReqDTO.setOperatorId(currentUserInfo().getUserId());
        processDetailReqDTO.setOperatorName(currentUserInfo().getRealName());
        Result<Integer> result = null;
        if (CommonUtils.evalLong(processDetailReqDTO.getProcessDetailId()) < 0) {
            result = service.insertProcessDetail(processDetailReqDTO);
        } else {
            result = service.updateProcessDetail(processDetailReqDTO);
        }
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
