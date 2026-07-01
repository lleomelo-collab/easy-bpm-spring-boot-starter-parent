package com.pig.easy.bpm.web.controller.process;


import com.github.pagehelper.PageInfo;
import com.pig.easy.bpm.api.dto.request.CompleteTaskDTO;
import com.pig.easy.bpm.api.dto.request.UserTaskInfoQueryDTO;
import com.pig.easy.bpm.api.dto.response.ReturnNodeDTO;
import com.pig.easy.bpm.api.service.UserTaskService;
import com.pig.easy.bpm.auth.core.controller.BaseController;
import com.pig.easy.bpm.common.entityError.EntityError;
import com.pig.easy.bpm.common.utils.JsonResult;
import com.pig.easy.bpm.common.utils.Result;
import com.pig.easy.bpm.web.vo.request.BatchCompleteTaskVO;
import com.pig.easy.bpm.web.vo.request.CompleteTaskVO;
import com.pig.easy.bpm.web.vo.request.UserTaskQueryVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pig
 * @since 2020-05-20
 */
@RestController
@RequestMapping("/userTask")
@Tag(name = "用户任务管理")
public class UserTaskController extends BaseController {

    @Resource
    UserTaskService userTaskService;

    @Operation(summary = "获取草稿列表")
    @RequestMapping("/getDraftListByCondition")
    public JsonResult getDraftListByCondition(@RequestBody @Valid UserTaskQueryVO userTaskQueryVO) {

        if (userTaskQueryVO == null) {
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }

        UserTaskInfoQueryDTO userTaskInfoQueryDTO = switchToDTO(userTaskQueryVO, UserTaskInfoQueryDTO.class);

        Result<PageInfo> result = userTaskService.getDraftListByCondition(userTaskInfoQueryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "获取申请列表")
    @RequestMapping("/getApplyListByCondition")
    public JsonResult getApplyListByCondition(@RequestBody @Valid UserTaskQueryVO userTaskQueryVO) {

        if (userTaskQueryVO == null) {
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }

        UserTaskInfoQueryDTO userTaskInfoQueryDTO = switchToDTO(userTaskQueryVO, UserTaskInfoQueryDTO.class);

        Result<PageInfo> result = userTaskService.getApplyListByCondition(userTaskInfoQueryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "获取已办列表")
    @RequestMapping("/getHaveDoListByCondition")
    public JsonResult getHaveDoListByCondition(@RequestBody @Valid UserTaskQueryVO userTaskQueryVO) {

        if (userTaskQueryVO == null) {
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }

        UserTaskInfoQueryDTO userTaskInfoQueryDTO = switchToDTO(userTaskQueryVO, UserTaskInfoQueryDTO.class);

        Result<PageInfo> result = userTaskService.getHaveDoListByCondition(userTaskInfoQueryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "获取待办列表")
    @RequestMapping("/getToDoListByCondition")
    public JsonResult getToDoListByCondition(@RequestBody @Valid UserTaskQueryVO userTaskQueryVO) {

        if (userTaskQueryVO == null) {
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }

        UserTaskInfoQueryDTO userTaskInfoQueryDTO = switchToDTO(userTaskQueryVO, UserTaskInfoQueryDTO.class);

        Result<PageInfo> result = userTaskService.getToDoListByCondition(userTaskInfoQueryDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "完成任务")
    @RequestMapping("/completeTask")
    public JsonResult completeTask(@RequestBody @Valid CompleteTaskVO completeTaskVO) {

        if (completeTaskVO == null) {
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }

        CompleteTaskDTO completeTaskDTO = switchToDTO(completeTaskVO, CompleteTaskDTO.class);
        completeTaskDTO.setApproveId(currentUserInfo().getUserId());
        completeTaskDTO.setApproveName(currentUserInfo().getRealName());
        completeTaskDTO.setBusinessData(completeTaskVO.getBusinessData());
        Result<Boolean> result = userTaskService.completeTask(completeTaskDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "批量审批任务")
    @RequestMapping("/batchCompleteTask")
    public JsonResult batchCompleteTask(@RequestBody @Valid BatchCompleteTaskVO BatchCompleteTaskVO) {

        if (BatchCompleteTaskVO == null) {
            return JsonResult.error(EntityError.ILLEGAL_ARGUMENT_ERROR);
        }

        CompleteTaskDTO completeTaskDTO = switchToDTO(BatchCompleteTaskVO, CompleteTaskDTO.class);
        completeTaskDTO.setApproveId(currentUserInfo().getUserId());
        completeTaskDTO.setApproveName(currentUserInfo().getRealName());
        completeTaskDTO.setBusinessData(BatchCompleteTaskVO.getBusinessData());
        Result<Boolean> result = userTaskService.batchCompleteTask(completeTaskDTO);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "获取可退回节点")
    @RequestMapping("/getReturnNode/{taskId}")
    public JsonResult getReturnNode(@Parameter(required = true, name = "taskId", description = "任务编号", example = "1") @PathVariable("taskId") Long taskId) {

        Result<List<ReturnNodeDTO>> result = userTaskService.getReturnNode(taskId);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "获取自由跳转节点")
    @RequestMapping("/getRandomJumpNode/{taskId}")
    public JsonResult getRandomJumpNode(@Parameter(required = true, name = "taskId", description = "任务编号", example = "1") @PathVariable("taskId") Long taskId) {

        Result<List<ReturnNodeDTO>> result = userTaskService.getRandomJumpNode(taskId);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }

    @Operation(summary = "获取已办/待办/已发/草稿 数量")
    @RequestMapping("/getCountListByUserType/{tenantId}/{userType}")
    public JsonResult getCountListByUserType(@Parameter(required = true, name = "tenantId", description = "租户编号", example = "pig") @PathVariable("tenantId") String tenantId,
                                             @Parameter(required = true, name = "userType", description = "查询类型 all：所有， apply：申请,toDo ： 待办,haveDo：已办,draft：草稿", example = "1") @PathVariable("userType") String userType) {

        Result<Map<String, Object>> result = userTaskService.getCountListByUserType(currentUserInfo().getUserId(), tenantId, userType);
        if (result.getEntityError().getCode() != EntityError.SUCCESS.getCode()) {
            return JsonResult.error(result.getEntityError());
        }
        return JsonResult.success(result.getData());
    }
}

