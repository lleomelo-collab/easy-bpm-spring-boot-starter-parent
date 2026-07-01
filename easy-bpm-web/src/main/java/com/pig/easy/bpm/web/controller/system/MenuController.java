package com.pig.easy.bpm.web.controller.system;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.auth.dto.request.MenuQueryDTO;
import com.pig.easy.bpm.auth.dto.request.MenuSaveOrUpdateDTO;
import com.pig.easy.bpm.auth.dto.response.MenuDTO;
import com.pig.easy.bpm.auth.dto.response.MenuExportDTO;
import com.pig.easy.bpm.auth.dto.response.MenuTreeDTO;
import com.pig.easy.bpm.auth.service.MenuService;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.MenuQueryVO;
import com.pig.easy.bpm.web.vo.request.MenuSaveOrUpdateVO;
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
 * 菜单表 前端控制器
 * </p>
 *
 * @author pig
 * @since 2021-04-06
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    MenuService service;

    @Operation(summary = "查询菜单表列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody MenuQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        MenuQueryDTO queryDTO = BeanUtils.switchToDTO(param, MenuQueryDTO.class);

        Result<PageInfo<MenuDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询菜单表列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody MenuQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        MenuQueryDTO queryDTO = BeanUtils.switchToDTO(param, MenuQueryDTO.class);

        Result<List<MenuDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增菜单表")
    @PostMapping("/insert")
    public JsonResult insertMenu(@Valid @RequestBody MenuSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        MenuSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, MenuSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertMenu(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改菜单表")
    @PostMapping("/update")
    public JsonResult updateMenu(@Valid @RequestBody MenuSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        MenuSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, MenuSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateMenu(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除菜单表")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody MenuSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        MenuSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, MenuSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteMenu(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载菜单表")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody MenuQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        MenuQueryDTO queryDTO = BeanUtils.switchToDTO(param, MenuQueryDTO.class);

        Result<List<MenuDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + "菜单表.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), MenuExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "查询树形菜单")
    @PostMapping("/getMenuTree/{tenantId}")
    public JsonResult getList(@Parameter(required = true, name = "tenantId", description = "租户编号", example = "pig") @PathVariable("tenantId") String tenantId) {

       EasyBpmAsset.isAssetEmpty(tenantId);
        Result<List<MenuTreeDTO>> result = service.getMenuTree(tenantId, null);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }


    @Operation(summary = "根据编号获取菜单表")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody MenuSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getMenuId());

        Result<MenuDTO> result = service.getMenuById(param.getMenuId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
