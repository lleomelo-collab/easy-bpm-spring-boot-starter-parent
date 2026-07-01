package com.pig.easy.bpm.web.controller.process;

import com.pig.easy.bpm.api.dto.response.ProcessDiagramDTO;
import com.pig.easy.bpm.api.service.ProcessDiagramService;
import com.pig.easy.bpm.auth.core.controller.BaseController;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * todo:
 *
 * @author : pig
 * @date : 2020/7/5 18:29
 */
@Tag(name = "流程图详细信息")
@RestController
@RequestMapping("/processDiagram")
public class ProcessDiagramController extends BaseController {

    @Resource
    ProcessDiagramService processDiagramService;

    @Operation(summary = "获取图流程详细信息")
    @PostMapping("/getProcessDiagramByApplyId/{applyId}")
    public JsonResult getProcessDiagramByApplyId(
            @Parameter(required = true, name = "applyId", description = "申请编号", example = "1") @PathVariable("applyId") Long applyId
    ) {
        Result<ProcessDiagramDTO> result = processDiagramService.getProcessDiagramByApplyId(applyId);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}
