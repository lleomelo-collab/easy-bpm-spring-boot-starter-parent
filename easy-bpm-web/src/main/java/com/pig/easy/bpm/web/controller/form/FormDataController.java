package com.pig.easy.bpm.web.controller.form;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.api.dto.request.FormDataQueryDTO;
import com.pig.easy.bpm.api.dto.request.FormDataSaveOrUpdateDTO;
import com.pig.easy.bpm.api.dto.response.FormDataDTO;
import com.pig.easy.bpm.api.dto.response.FormDataExportDTO;
import com.pig.easy.bpm.api.service.FormDataService;
import com.pig.easy.bpm.web.vo.request.FormDataQueryVO;
import com.pig.easy.bpm.web.vo.request.FormDataSaveOrUpdateVO;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
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
 * @since 2021-04-09
 */
@RestController
@RequestMapping("/formData")
public class FormDataController {

    @Autowired
    FormDataService service;

    @Operation(summary = "查询列表", description = "查询列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody FormDataQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FormDataQueryDTO queryDTO = BeanUtils.switchToDTO(param, FormDataQueryDTO.class);

        Result<PageInfo<FormDataDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询列表", description = "查询列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody FormDataQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FormDataQueryDTO queryDTO = BeanUtils.switchToDTO(param, FormDataQueryDTO.class);

        Result<List<FormDataDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增", description = "新增")
    @PostMapping("/insert")
    public JsonResult insertFormData(@Valid @RequestBody FormDataSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FormDataSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, FormDataSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertFormData(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改", description = "修改")
    @PostMapping("/update")
    public JsonResult updateFormData(@Valid @RequestBody FormDataSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FormDataSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, FormDataSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateFormData(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除", description = "删除")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody FormDataSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FormDataSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, FormDataSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteFormData(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载", description = "下载")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody FormDataQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        FormDataQueryDTO queryDTO = BeanUtils.switchToDTO(param, FormDataQueryDTO.class);

        Result<List<FormDataDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), FormDataExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取", description = "根据编号获取")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody FormDataSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getDataId());

        Result<FormDataDTO> result = service.getFormDataById(param.getDataId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
