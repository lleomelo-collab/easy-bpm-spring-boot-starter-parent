package com.pig.easy.bpm.web.controller.system;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.auth.dto.request.DictItemQueryDTO;
import com.pig.easy.bpm.auth.dto.request.DictItemSaveOrUpdateDTO;
import com.pig.easy.bpm.auth.dto.response.DictItemDTO;
import com.pig.easy.bpm.auth.dto.response.DictItemExportDTO;
import com.pig.easy.bpm.auth.service.DictItemService;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.DictItemQueryVO;
import com.pig.easy.bpm.web.vo.request.DictItemSaveOrUpdateVO;
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
 * 字典详细表 前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-04-06
 */
@RestController
@RequestMapping("/dictItem")
public class DictItemController {

    @Autowired
    DictItemService service;

    @Operation(summary = "查询字典详细表列表", description = "查询字典详细表列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody DictItemQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DictItemQueryDTO queryDTO = BeanUtils.switchToDTO(param, DictItemQueryDTO.class);

        Result<PageInfo<DictItemDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询字典详细表列表", description = "查询字典详细表列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody DictItemQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DictItemQueryDTO queryDTO = BeanUtils.switchToDTO(param, DictItemQueryDTO.class);

        Result<List<DictItemDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增字典详细表", description = "新增字典详细表")
    @PostMapping("/insert")
    public JsonResult insertDictItem(@Valid @RequestBody DictItemSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DictItemSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DictItemSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertDictItem(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改字典详细表", description = "修改字典详细表")
    @PostMapping("/update")
    public JsonResult updateDictItem(@Valid @RequestBody DictItemSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DictItemSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DictItemSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateDictItem(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除字典详细表", description = "删除字典详细表")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody DictItemSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        DictItemSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, DictItemSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteDictItem(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载字典详细表", description = "下载字典详细表")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody DictItemQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        DictItemQueryDTO queryDTO = BeanUtils.switchToDTO(param, DictItemQueryDTO.class);

        Result<List<DictItemDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + "字典详细表.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), DictItemExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取字典详细表", description = "根据编号获取字典详细表")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody DictItemSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getItemId());

        Result<DictItemDTO> result = service.getDictItemById(param.getItemId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "根据字典编码获取字典详细列表", description = "根据字典编码获取字典详细列表")
    @PostMapping("/getListByDictCode/{dictCode}")
    public JsonResult getListByDictCode(@Parameter(required = true, name = "dictCode", description = "字典编码", example = "pig") @PathVariable("dictCode") String dictCode) {

        EasyBpmAsset.isAssetEmpty(dictCode);

        Result<List<DictItemDTO>> result = service.getListByDictCode(dictCode);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
