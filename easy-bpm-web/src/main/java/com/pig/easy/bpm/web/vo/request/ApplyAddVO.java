package com.pig.easy.bpm.web.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * todo:
 *
 * @author : pig
 * @date : 2020/5/21 10:35
 */
@Data
@ToString
@Schema(description = "发起流程")
public class ApplyAddVO implements Serializable {

    private static final long serialVersionUID = -1904908751341094576L;

    private Long applyId;

    private String processKey;

    private String formKey;

    private Map<String,Object> data;

    @NotNull
    private Long startUserId;
}
