package com.begcode.monolith.system.repository;

import com.begcode.monolith.system.domain.SmsTemplate;
import com.begcode.monolith.system.repository.base.SmsTemplateBaseRepository;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SmsTemplateRepository extends SmsTemplateBaseRepository<SmsTemplate> {
    // begcode-please-regenerate-this-file 如果您不希望重新生成代码时被覆盖，将please修改为don't ！！！
}
