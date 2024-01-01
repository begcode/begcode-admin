package com.begcode.monolith.settings.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.settings.domain.Dictionary}的DTO。
 */
@Schema(description = "数据字典")
@Data
@ToString
@EqualsAndHashCode
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DictionaryDTO implements Serializable {

    private Long id;

    /**
     * 字典名称
     */
    @NotNull
    @Schema(description = "字典名称", required = true)
    private String dictName;

    /**
     * 字典Key
     */
    @NotNull
    @Schema(description = "字典Key", required = true)
    private String dictKey;

    /**
     * 是否禁用
     */
    @Schema(description = "是否禁用")
    private Boolean disabled;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sortValue;

    /**
     * 是否内置
     */
    @Schema(description = "是否内置")
    private Boolean builtIn;

    /**
     * 更新枚举
     */
    @Schema(description = "更新枚举")
    private Boolean syncEnum;

    /**
     * 字典项列表
     */
    @Schema(description = "字典项列表")
    private List<CommonFieldDataDTO> items = new ArrayList<>();

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public DictionaryDTO id(Long id) {
        this.id = id;
        return this;
    }

    public DictionaryDTO dictName(String dictName) {
        this.dictName = dictName;
        return this;
    }

    public DictionaryDTO dictKey(String dictKey) {
        this.dictKey = dictKey;
        return this;
    }

    public DictionaryDTO disabled(Boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    public DictionaryDTO sortValue(Integer sortValue) {
        this.sortValue = sortValue;
        return this;
    }

    public DictionaryDTO builtIn(Boolean builtIn) {
        this.builtIn = builtIn;
        return this;
    }

    public DictionaryDTO syncEnum(Boolean syncEnum) {
        this.syncEnum = syncEnum;
        return this;
    }

    public DictionaryDTO items(List<CommonFieldDataDTO> items) {
        this.items = items;
        return this;
    }
    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

}
