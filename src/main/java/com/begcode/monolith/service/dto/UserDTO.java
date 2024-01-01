package com.begcode.monolith.service.dto;

import com.begcode.monolith.domain.AbstractAuditingEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**

 * {@link com.begcode.monolith.domain.User}的DTO。
 */
@Schema(description = "用户")
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserDTO extends AbstractAuditingEntity<Long, UserDTO> {

    /**
     * 用户ID
     */
    @NotNull
    @Schema(description = "用户ID", required = true)
    private Long id;

    /**
     * 账户名
     */
    @NotNull
    @Schema(description = "账户名", required = true)
    private String login;

    /**
     * 名字
     */
    @Schema(description = "名字")
    private String firstName;

    /**
     * 姓氏
     */
    @Schema(description = "姓氏")
    private String lastName;

    /**
     * 电子邮箱
     */
    @NotNull
    @Schema(description = "电子邮箱", required = true)
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码")
    private String mobile;

    /**
     * 出生日期
     */
    @Schema(description = "出生日期")
    private ZonedDateTime birthday;

    /**
     * 语言Key
     */
    @Schema(description = "语言Key")
    private String langKey;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址")
    private String imageUrl;

    /**
     * 部门
     */
    @Schema(description = "部门")
    private DepartmentDTO department;

    private Long departmentId;

    /**
     * 岗位
     */
    @Schema(description = "岗位")
    private PositionDTO position;

    private Long positionId;

    /**
     * 角色列表
     */
    @Schema(description = "角色列表")
    private List<AuthorityDTO> authorities = new ArrayList<>();

    // jhipster-needle-dto-add-field - JHipster will add fields here, do not remove

    public UserDTO id(Long id) {
        this.id = id;
        return this;
    }

    public UserDTO login(String login) {
        this.login = login;
        return this;
    }

    public UserDTO firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserDTO lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserDTO email(String email) {
        this.email = email;
        return this;
    }

    public UserDTO mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public UserDTO birthday(ZonedDateTime birthday) {
        this.birthday = birthday;
        return this;
    }

    public UserDTO langKey(String langKey) {
        this.langKey = langKey;
        return this;
    }

    public UserDTO imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
    // jhipster-needle-dto-add-getters-setters - JHipster will add getters and setters here, do not remove

}
