package com.begcode.monolith.service.mapper;

import com.begcode.monolith.domain.ViewPermission;
import com.begcode.monolith.service.dto.ViewPermissionDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ViewPermission} and its DTO {@link ViewPermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface ViewPermissionMapper extends EntityMapper<ViewPermissionDTO, ViewPermission> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "viewPermissionText")
    ViewPermissionDTO toDto(ViewPermission s);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parentId", source = "parent.id")
    ViewPermission toEntity(ViewPermissionDTO viewPermissionDTO);

    @Named("viewPermissionText")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    ViewPermissionDTO toDtoViewPermissionText(ViewPermission viewPermission);

    @Named("viewPermissionTextList")
    default List<ViewPermissionDTO> toDtoViewPermissionTextList(List<ViewPermission> viewPermission) {
        return viewPermission.stream().map(this::toDtoViewPermissionText).collect(Collectors.toList());
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
