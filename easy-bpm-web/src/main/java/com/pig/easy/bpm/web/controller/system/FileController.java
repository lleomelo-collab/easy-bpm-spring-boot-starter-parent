package com.pig.easy.bpm.web.controller.system;


import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.auth.core.controller.BaseController;
import com.pig.easy.bpm.auth.dto.request.FileQueryDTO;
import com.pig.easy.bpm.auth.dto.request.FileSaveOrUpdateDTO;
import com.pig.easy.bpm.auth.dto.response.FileDTO;
import com.pig.easy.bpm.auth.dto.response.FileExportDTO;
import com.pig.easy.bpm.auth.service.FileService;
import com.pig.easy.bpm.common.converter.LocalDateTimeConverter;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.BeanUtils;
import com.pig.easy.bpm.common.utils.EasyBpmAsset;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.FileQueryVO;
import com.pig.easy.bpm.web.vo.request.FileSaveOrUpdateVO;
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
 * @since 2021-03-20
 */
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    @Autowired
    FileService service;

    @Operation(summary = "查询列表")
    @PostMapping("/getListPage")
    public JsonResult getListPage(@Valid @RequestBody FileQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FileQueryDTO queryDTO = BeanUtils.switchToDTO(param, FileQueryDTO.class);

        Result<PageInfo<FileDTO>> result = service.getListPageByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "查询列表")
    @PostMapping("/getList")
    public JsonResult getList(@Valid @RequestBody FileQueryVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FileQueryDTO queryDTO = BeanUtils.switchToDTO(param, FileQueryDTO.class);

        Result<List<FileDTO>> result = service.getListByCondition(queryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "新增")
    @PostMapping("/insert")
    public JsonResult insertFile(@Valid @RequestBody FileSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FileSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, FileSaveOrUpdateDTO.class);

        Result<Integer> result = service.insertFile(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public JsonResult updateFile(@Valid @RequestBody FileSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FileSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, FileSaveOrUpdateDTO.class);

        Result<Integer> result = service.updateFile(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
          return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "删除")
    @PostMapping("/deleteById")
    public JsonResult deleteById(@Valid @RequestBody FileSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        FileSaveOrUpdateDTO saveDTO = BeanUtils.switchToDTO(param, FileSaveOrUpdateDTO.class);
        saveDTO.setValidState(0);

        Result<Integer> result = service.deleteFile(saveDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "下载")
    @PostMapping("download")
    public void download(HttpServletResponse response,@Valid @RequestBody FileQueryVO param) throws IOException {

        EasyBpmAsset.isAssetEmpty(param);
        FileQueryDTO queryDTO = BeanUtils.switchToDTO(param, FileQueryDTO.class);

        Result<List<FileDTO>> result = service.getListByCondition(queryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = String.valueOf(System.currentTimeMillis()) + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        EasyExcel.write(response.getOutputStream(), FileExportDTO.class).registerConverter(new LocalDateTimeConverter()).sheet().doWrite(result.getData());
    }

    @Operation(summary = "根据编号获取")
    @PostMapping("/getById")
    public JsonResult getById(@Valid @RequestBody FileSaveOrUpdateVO param) {

        EasyBpmAsset.isAssetEmpty(param);
        EasyBpmAsset.isAssetEmpty(param.getFileId());

        Result<FileDTO> result = service.getFileById(param.getFileId());
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
