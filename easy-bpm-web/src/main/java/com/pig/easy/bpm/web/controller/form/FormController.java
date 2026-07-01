package com.pig.easy.bpm.web.controller.form;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.api.dto.request.FormQueryDTO;
import com.pig.easy.bpm.api.dto.request.FormSaveOrUpdateDTO;
import com.pig.easy.bpm.api.dto.response.FormDTO;
import com.pig.easy.bpm.api.dto.response.FormExportDTO;
import com.pig.easy.bpm.api.service.FormService;
import com.pig.easy.bpm.web.vo.request.FormQueryVO;
import com.pig.easy.bpm.web.vo.request.FormSaveOrUpdateVO;
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
 * @since 2021-04-07
 */
@RestController
@RequestMapping("/form")
public class FormController {

    @Autowired
    FormService service;

    @Operation(summary = "查询列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody FormQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FormQueryDTO queryDTO = BeanUtils.switchToDTO(param, FormQueryDTO.class);

        Result<PageInfo<FormDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody FormQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FormQueryDTO queryDTO = BeanUtils.switchToDTO(param, FormQueryDTO.class);

        Result<List<FormDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增")
    @PostMapping("/insert")
    public JsonResult insertForm(@Valid @RequestBody FormSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FormSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, FormSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertForm(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public JsonResult updateForm(@Valid @RequestBody FormSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FormSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, FormSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateForm(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody FormSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FormSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, FormSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteForm(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody FormQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        FormQueryDTO queryDTO = BeanUtils.switchToDTO(param, FormQueryDTO.class);

        Result<List<FormDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), FormExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody FormSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getFormId());

        Result<FormDTO> result = service.getFormById(param.getFormId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
