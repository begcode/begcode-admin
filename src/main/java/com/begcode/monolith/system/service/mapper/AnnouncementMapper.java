package com.begcode.monolith.system.service.mapper;

import com.begcode.monolith.service.mapper.EntityMapper;
import com.begcode.monolith.system.domain.Announcement;
import com.begcode.monolith.system.service.dto.AnnouncementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Announcement} and its DTO {@link AnnouncementDTO}.
 */
@Mapper(componentModel = "spring")
public interface AnnouncementMapper extends EntityMapper<AnnouncementDTO, Announcement> {
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Announcement toEntity(AnnouncementDTO announcementDTO);
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
