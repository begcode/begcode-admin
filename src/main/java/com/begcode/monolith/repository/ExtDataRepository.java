package com.begcode.monolith.repository;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data  repository for the CommonTableFieldJhiExt entity.
 */
@SuppressWarnings("unused")
@Mapper
public interface ExtDataRepository {
    Integer selectByTableAndId(@Param("tableName") String tableName, @Param("id") Long id);

    default Boolean existsByTableNameAndId(String tableName, Long id) {
        return this.selectByTableAndId(tableName, id) != null;
    }

    int insertByMap(@Param("tableName") String tableName, @Param("fieldsMap") Map<String, Object> fieldsMap);

    Map<String, Object> selectMapByIdAndColumns(
        @Param("tableName") String tableName,
        @Param(value = "id") Long id,
        @Param("fieldsMap") Map<String, Object> fieldsMap
    );

    int updateToManyRelationById(
        @Param(value = "tableName") String tableName,
        @Param(value = "id") Long id,
        @Param("fieldsMap") Map<String, Object> fieldsMap
    );

    int insertToManyRelation(@Param(value = "tableName") String tableName, @Param("fieldsMap") Map<String, Object> fieldsMap);

    Map<String, Object> selectMapByTableAndIdAndColumns(
        @Param("table") String table,
        @Param("id") Long id,
        @Param("fieldsMap") Map<String, Object> fieldsMap,
        @Param("joinTable") String joinTable,
        @Param("relationIdName") String relationIdName
    );

    List<Map<String, Object>> selectMapsByTableAndIdAndColumns(
        @Param("id") Long id,
        @Param("fieldsMap") Map<String, Object> fieldsMap,
        @Param("joinTable") String joinTable,
        @Param("otherTable") String otherTable,
        @Param("relationIdName") String relationIdName,
        @Param("otherIdName") String otherIdName
    );
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove
}
