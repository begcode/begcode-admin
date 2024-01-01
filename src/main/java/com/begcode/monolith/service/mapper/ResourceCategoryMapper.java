package com.begcode.monolith.service.mapper;

import com.begcode.monolith.domain.ResourceCategory;
import com.begcode.monolith.service.dto.ResourceCategoryDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceCategory} and its DTO {@link ResourceCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ResourceCategoryMapper extends EntityMapper<ResourceCategoryDTO, ResourceCategory> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "resourceCategoryTitle")
    ResourceCategoryDTO toDto(ResourceCategory s);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parentId", source = "parent.id")
    ResourceCategory toEntity(ResourceCategoryDTO resourceCategoryDTO);

    @Named("resourceCategoryTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    ResourceCategoryDTO toDtoResourceCategoryTitle(ResourceCategory resourceCategory);

    @Named("resourceCategoryTitleList")
    default List<ResourceCategoryDTO> toDtoResourceCategoryTitleList(List<ResourceCategory> resourceCategory) {
        return resourceCategory.stream().map(this::toDtoResourceCategoryTitle).collect(Collectors.toList());
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
