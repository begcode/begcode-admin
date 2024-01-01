package com.begcode.monolith.service.mapper;

import com.begcode.monolith.domain.ApiPermission;
import com.begcode.monolith.service.dto.ApiPermissionDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ApiPermission} and its DTO {@link ApiPermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ApiPermissionMapper extends EntityMapper<ApiPermissionDTO, ApiPermission> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "apiPermissionName")
    ApiPermissionDTO toDto(ApiPermission s);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parentId", source = "parent.id")
    ApiPermission toEntity(ApiPermissionDTO apiPermissionDTO);

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
