package com.begcode.monolith.service.mapper;

import com.begcode.monolith.domain.Authority;
import com.begcode.monolith.domain.Department;
import com.begcode.monolith.service.dto.AuthorityDTO;
import com.begcode.monolith.service.dto.DepartmentDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {
    @Mapping(target = "authorities", source = "authorities", qualifiedByName = "authorityNameList")
    @Mapping(target = "parent", source = "parent", qualifiedByName = "departmentName")
    DepartmentDTO toDto(Department s);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "parentId", source = "parent.id")
    Department toEntity(DepartmentDTO departmentDTO);

    @Named("departmentName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    DepartmentDTO toDtoDepartmentName(Department department);

    @Named("departmentNameList")
    default List<DepartmentDTO> toDtoDepartmentNameList(List<Department> department) {
        return department.stream().map(this::toDtoDepartmentName).collect(Collectors.toList());
    }

    @Named("authorityName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    AuthorityDTO toDtoAuthorityName(Authority authority);

    @Named("authorityNameList")
    default List<AuthorityDTO> toDtoAuthorityNameList(List<Authority> authority) {
        return authority.stream().map(this::toDtoAuthorityName).collect(Collectors.toList());
    }
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
