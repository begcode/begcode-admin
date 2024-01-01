package com.begcode.monolith.service.mapper;

import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.service.dto.ApiPermissionDTO;
import com.begcode.monolith.service.dto.AuthorityDTO;
import com.begcode.monolith.service.dto.ViewPermissionDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Authority} and its DTO {@link AuthorityDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorityMapper extends EntityMapper<AuthorityDTO, Authority> {
    @Mapping(target = "viewPermissions", source = "viewPermissions", qualifiedByName = "viewPermissionTextList")
    @Mapping(target = "apiPermissions", source = "apiPermissions", qualifiedByName = "apiPermissionNameList")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "authorityName")
    AuthorityDTO toDto(Authority s);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parentId", source = "parent.id")
    Authority toEntity(AuthorityDTO authorityDTO);

    @Named("authorityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AuthorityDTO toDtoAuthorityName(Authority authority);

    @Named("authorityNameList")
    default List<AuthorityDTO> toDtoAuthorityNameList(List<Authority> authority) {
        return authority.stream().map(this::toDtoAuthorityName).collect(Collectors.toList());
    }

    @Named("viewPermissionText")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    ViewPermissionDTO toDtoViewPermissionText(ViewPermission viewPermission);

    @Named("viewPermissionTextList")
    default List<ViewPermissionDTO> toDtoViewPermissionTextList(List<ViewPermission> viewPermission) {
        return viewPermission.stream().map(this::toDtoViewPermissionText).collect(Collectors.toList());
    }

    @Named("apiPermissionName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ApiPermissionDTO toDtoApiPermissionName(ApiPermission apiPermission);

    @Named("apiPermissionNameList")
    default List<ApiPermissionDTO> toDtoApiPermissionNameList(List<ApiPermission> apiPermission) {
        return apiPermission.stream().map(this::toDtoApiPermissionName).collect(Collectors.toList());
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
