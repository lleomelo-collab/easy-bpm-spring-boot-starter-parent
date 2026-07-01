package com.pig.easy.bpm.web.controller.system;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.auth.core.controller.BaseController;
import com.pig.easy.bpm.auth.dto.request.OperatorLogQueryDTO;
import com.pig.easy.bpm.auth.dto.request.OperatorLogSaveOrUpdateDTO;
import com.pig.easy.bpm.auth.dto.response.OperatorLogDTO;
import com.pig.easy.bpm.auth.dto.response.OperatorLogExportDTO;
import com.pig.easy.bpm.auth.service.OperatorLogService;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.OperatorLogQueryVO;
import com.pig.easy.bpm.web.vo.request.OperatorLogSaveOrUpdateVO;
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
 * @since 2021-03-22
 */
@RestController
@RequestMapping("/operatorLog")
public class OperatorLogController extends BaseController {

    @Autowired
    OperatorLogService service;

    @Operation(summary = "查询列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody OperatorLogQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        OperatorLogQueryDTO queryDTO = BeanUtils.switchToDTO(param, OperatorLogQueryDTO.class);

        Result<PageInfo<OperatorLogDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody OperatorLogQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        OperatorLogQueryDTO queryDTO = BeanUtils.switchToDTO(param, OperatorLogQueryDTO.class);

        Result<List<OperatorLogDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增")
    @PostMapping("/insert")
    public JsonResult insertOperatorLog(@Valid @RequestBody OperatorLogSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        OperatorLogSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, OperatorLogSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertOperatorLog(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public JsonResult updateOperatorLog(@Valid @RequestBody OperatorLogSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        OperatorLogSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, OperatorLogSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateOperatorLog(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody OperatorLogSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        OperatorLogSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, OperatorLogSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteOperatorLog(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody OperatorLogQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        OperatorLogQueryDTO queryDTO = BeanUtils.switchToDTO(param, OperatorLogQueryDTO.class);

        Result<List<OperatorLogDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), OperatorLogExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody OperatorLogSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getLogId());

        Result<OperatorLogDTO> result = service.getOperatorLogById(param.getLogId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
