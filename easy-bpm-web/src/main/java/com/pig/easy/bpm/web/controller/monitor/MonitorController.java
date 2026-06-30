package com.pig.easy.bpm.web.controller.monitor;

import com.pig.easy.bpm.auth.core.controller.BaseController;
import com.pig.easy.bpm.common.monitor.server.Server;
import com.pig.easy.bpm.common.utils.JsonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * todo:
 *
 * @author : zhoulin.zhu
 * @date : 2021/1/27 16:14
 */
@RestController
@Tag(name = "监控管理")
@RequestMapping("/monitor/server")
public class MonitorController  extends BaseController {

    @Operation(summary = "查询服务器信息", description = "查询服务器信息")
    @PostMapping("/getServerInfo")
    public JsonResult getServerInfo()  throws Exception {
        Server server = new Server();
        server.copyTo();
        return JsonResult.success(server);
    }
}
