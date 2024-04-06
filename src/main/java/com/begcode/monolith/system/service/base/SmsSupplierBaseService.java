package com.begcode.monolith.system.service.base;

import cn.hutool.core.bean.BeanUtil;
import com.aliyun.dysmsapi20170525.models.QuerySmsTemplateListResponse;
import com.aliyun.dysmsapi20170525.models.QuerySmsTemplateListResponseBody;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.begcode.monolith.domain.enumeration.MessageSendType;
import com.begcode.monolith.domain.enumeration.SmsTemplateType;
import com.begcode.monolith.system.domain.SmsSupplier;
import com.begcode.monolith.system.domain.SmsTemplate;
import com.begcode.monolith.system.repository.SmsSupplierRepository;
import com.begcode.monolith.system.service.dto.SmsSupplierDTO;
import com.begcode.monolith.system.service.mapper.SmsSupplierMapper;
import com.diboot.core.binding.Binder;
import com.diboot.core.service.impl.BaseServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import java.util.*;
import java.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.core.datainterface.SmsReadConfig;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.provider.config.BaseConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Service Implementation for managing {@link com.begcode.monolith.system.domain.SmsSupplier}.
 */
@SuppressWarnings("UnusedReturnValue")
public class SmsSupplierBaseService<R extends SmsSupplierRepository, E extends SmsSupplier>
    extends BaseServiceImpl<SmsSupplierRepository, SmsSupplier>
    implements SmsReadConfig {

    private final Logger log = LoggerFactory.getLogger(SmsSupplierBaseService.class);
    private final List<String> relationNames = List.of();

    protected final SmsSupplierRepository smsSupplierRepository;

    protected final CacheManager cacheManager;

    protected final SmsSupplierMapper smsSupplierMapper;

    public SmsSupplierBaseService(
        SmsSupplierRepository smsSupplierRepository,
        CacheManager cacheManager,
        SmsSupplierMapper smsSupplierMapper
    ) {
        this.smsSupplierRepository = smsSupplierRepository;
        this.cacheManager = cacheManager;
        this.smsSupplierMapper = smsSupplierMapper;
    }

    /**
     * Save a smsSupplier.
     *
     * @param smsSupplierDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public SmsSupplierDTO save(SmsSupplierDTO smsSupplierDTO) {
        log.debug("Request to save SmsSupplier : {}", smsSupplierDTO);
        SmsSupplier smsSupplier = smsSupplierMapper.toEntity(smsSupplierDTO);

        this.saveOrUpdate(smsSupplier);
        SmsFactory.createSmsBlend(this, smsSupplier.getId().toString());
        return findOne(smsSupplier.getId()).orElseThrow();
    }

    /**
     * Update a smsSupplier.
     *
     * @param smsSupplierDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public SmsSupplierDTO update(SmsSupplierDTO smsSupplierDTO) {
        log.debug("Request to update SmsSupplier : {}", smsSupplierDTO);
        SmsSupplier smsSupplier = smsSupplierMapper.toEntity(smsSupplierDTO);

        this.saveOrUpdate(smsSupplier);
        SmsFactory.createSmsBlend(this, smsSupplier.getId().toString());
        return findOne(smsSupplier.getId()).orElseThrow();
    }

    /**
     * Partially update a smsSupplier.
     *
     * @param smsSupplierDTO the entity to update partially.
     * @return the persisted entity.
     */
    @Transactional
    public Optional<SmsSupplierDTO> partialUpdate(SmsSupplierDTO smsSupplierDTO) {
        log.debug("Request to partially update SmsSupplier : {}", smsSupplierDTO);

        return smsSupplierRepository
            .findById(smsSupplierDTO.getId())
            .map(existingSmsSupplier -> {
                smsSupplierMapper.partialUpdate(existingSmsSupplier, smsSupplierDTO);

                return existingSmsSupplier;
            })
            .map(tempSmsSupplier -> {
                smsSupplierRepository.save(tempSmsSupplier);
                return smsSupplierMapper.toDto(smsSupplierRepository.selectById(tempSmsSupplier.getId()));
            });
    }

    /**
     * Get all the smsSuppliers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public IPage<SmsSupplierDTO> findAll(Page<SmsSupplier> pageable) {
        log.debug("Request to get all SmsSuppliers");
        return this.page(pageable).convert(smsSupplierMapper::toDto);
    }

    /**
     * Get one smsSupplier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    public Optional<SmsSupplierDTO> findOne(Long id) {
        log.debug("Request to get SmsSupplier : {}", id);
        return Optional.ofNullable(smsSupplierRepository.selectById(id))
            .map(smsSupplier -> {
                Binder.bindRelations(smsSupplier);
                return smsSupplier;
            })
            .map(smsSupplierMapper::toDto);
    }

    /**
     * Delete the smsSupplier by id.
     *
     * @param id the id of the entity.
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        log.debug("Request to delete SmsSupplier : {}", id);

        smsSupplierRepository.deleteById(id);
    }

    public void initService() {
        LambdaQueryWrapper<SmsSupplier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SmsSupplier::getEnabled, true);
        List<SmsSupplier> smsSuppliers = smsSupplierRepository.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(smsSuppliers)) {
            smsSuppliers.forEach(smsSupplier -> {
                SmsFactory.createSmsBlend(this, smsSupplier.getId().toString());
            });
        }
    }

    @Override
    public BaseConfig getSupplierConfig(String s) {
        Optional<SmsSupplier> smsSupplierOptional = smsSupplierRepository.findById(Long.parseLong(s));

        if (smsSupplierOptional.isEmpty()) {
            return null;
        }
        SmsSupplier smsSupplier = smsSupplierOptional.orElseThrow();
        ObjectMapper objectMapper = new ObjectMapper();
        switch (smsSupplier.getProvider()) {
            case ALIBABA:
                try {
                    Map<String, String> configMap = objectMapper.readValue(
                        smsSupplier.getConfigData(),
                        new TypeReference<Map<String, String>>() {}
                    );
                    AlibabaConfig alibabaConfig = new AlibabaConfig();
                    alibabaConfig.setAccessKeyId(configMap.get("accessKeyId"));
                    alibabaConfig.setAccessKeySecret(configMap.get("accessKeySecret"));
                    alibabaConfig.setSignature(configMap.get("signature"));
                    alibabaConfig.setRequestUrl(StringUtils.defaultIfBlank(configMap.get("requestUrl"), "dysmsapi.aliyuncs.com"));
                    alibabaConfig.setTemplateId(StringUtils.defaultIfBlank(configMap.get("templateId"), null));
                    alibabaConfig.setAction(StringUtils.defaultIfBlank(configMap.get("action"), "SendSms"));
                    alibabaConfig.setVersion(StringUtils.defaultIfBlank(configMap.get("version"), "2017-05-25"));
                    alibabaConfig.setRegionId(StringUtils.defaultIfBlank(configMap.get("regionId"), "cn-hangzhou"));
                    alibabaConfig.setConfigId(smsSupplier.getId().toString());
                    return alibabaConfig;
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            case YUNPIAN:
                try {
                    Map<String, String> configMap = objectMapper.readValue(
                        smsSupplier.getConfigData(),
                        new TypeReference<Map<String, String>>() {}
                    );
                    YunpianConfig yunpianConfig = new YunpianConfig();
                    yunpianConfig.setAccessKeyId(configMap.get("accessKeyId"));
                    yunpianConfig.setCallbackUrl(configMap.get("callbackUrl"));
                    yunpianConfig.setConfigId(smsSupplier.getId().toString());
                    yunpianConfig.setTemplateName(configMap.get("templateName"));
                    return yunpianConfig;
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            default:
                return null;
        }
    }

    @Override
    public List<BaseConfig> getSupplierConfigList() {
        List<BaseConfig> result = new ArrayList<>();
        LambdaQueryWrapper<SmsSupplier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SmsSupplier::getEnabled, true);
        List<SmsSupplier> smsSuppliers = smsSupplierRepository.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(smsSuppliers)) {
            ObjectMapper objectMapper = new ObjectMapper();
            smsSuppliers.forEach(smsSupplier -> {
                switch (smsSupplier.getProvider()) {
                    case ALIBABA:
                        try {
                            Map<String, String> configMap = objectMapper.readValue(
                                smsSupplier.getConfigData(),
                                new TypeReference<Map<String, String>>() {}
                            );
                            AlibabaConfig alibabaConfig = new AlibabaConfig();
                            alibabaConfig.setAccessKeyId(configMap.get("accessKeyId"));
                            alibabaConfig.setAccessKeySecret(configMap.get("accessKeySecret"));
                            alibabaConfig.setSignature(configMap.get("signature"));
                            alibabaConfig.setRequestUrl(StringUtils.defaultIfBlank(configMap.get("requestUrl"), "dysmsapi.aliyuncs.com"));
                            alibabaConfig.setTemplateId(StringUtils.defaultIfBlank(configMap.get("templateId"), null));
                            alibabaConfig.setAction(StringUtils.defaultIfBlank(configMap.get("action"), "SendSms"));
                            alibabaConfig.setVersion(StringUtils.defaultIfBlank(configMap.get("version"), "2017-05-25"));
                            alibabaConfig.setRegionId(StringUtils.defaultIfBlank(configMap.get("regionId"), "cn-hangzhou"));
                            alibabaConfig.setConfigId(smsSupplier.getId().toString());
                            result.add(alibabaConfig);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case YUNPIAN:
                        try {
                            Map<String, String> configMap = objectMapper.readValue(
                                smsSupplier.getConfigData(),
                                new TypeReference<Map<String, String>>() {}
                            );
                            YunpianConfig yunpianConfig = new YunpianConfig();
                            yunpianConfig.setAccessKeyId(configMap.get("accessKeyId"));
                            yunpianConfig.setCallbackUrl(configMap.get("callbackUrl"));
                            yunpianConfig.setConfigId(smsSupplier.getId().toString());
                            yunpianConfig.setTemplateName(configMap.get("templateName"));
                            result.add(yunpianConfig);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        break;
                }
            });
        }
        return result;
    }

    // 申请短信签名
    public void AddSmsSign() {}

    // 删除短信签名
    public void DeleteSmsSign() {}

    // 修改短信签名
    public void ModifySmsSign() {}

    // 查询短信签名列表
    public void QuerySmsSignList() {}

    // 查询短信签名申请状态
    public void QuerySmsSign() {}

    // 申请短信模板
    public void AddSmsTemplate() {}

    // 删除短信模板
    public void DeleteSmsTemplate() {}

    // 修改审核未通过的短信模板
    public void ModifySmsTemplate() {}

    // 查询短信模板列表
    public List<SmsTemplate> querySmsTemplateList(Long supplierId) throws Exception {
        SmsSupplier smsSupplier = smsSupplierRepository.selectById(supplierId);
        BaseConfig supplierConfig = getSupplierConfig(supplierId.toString());
        if (smsSupplier == null || supplierConfig == null) {
            return new ArrayList<>();
        }
        switch (smsSupplier.getProvider()) {
            case ALIBABA: {
                AlibabaConfig alibabaConfig = (AlibabaConfig) supplierConfig;
                Config config = new Config()
                    // 必填，您的 AccessKey ID
                    .setAccessKeyId(alibabaConfig.getAccessKeyId())
                    // 必填，您的 AccessKey Secret
                    .setAccessKeySecret(alibabaConfig.getAccessKeySecret());
                // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
                config.endpoint = alibabaConfig.getRequestUrl() != null ? alibabaConfig.getRequestUrl() : "dysmsapi.aliyuncs.com";
                com.aliyun.dysmsapi20170525.Client client = new com.aliyun.dysmsapi20170525.Client(config);
                com.aliyun.dysmsapi20170525.models.QuerySmsTemplateListRequest querySmsTemplateListRequest =
                    new com.aliyun.dysmsapi20170525.models.QuerySmsTemplateListRequest().setPageIndex(1).setPageSize(15);
                com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
                try {
                    // 复制代码运行请自行打印 API 的返回值
                    QuerySmsTemplateListResponse querySmsTemplateListResponse = client.querySmsTemplateListWithOptions(
                        querySmsTemplateListRequest,
                        runtime
                    );
                    List<QuerySmsTemplateListResponseBody.QuerySmsTemplateListResponseBodySmsTemplateList> smsTemplateList =
                        querySmsTemplateListResponse.getBody().getSmsTemplateList();
                    return transformAliyun(smsTemplateList);
                } catch (TeaException error) {
                    // 错误 message
                    System.out.println(error.getMessage());
                    // 诊断地址
                    System.out.println(error.getData().get("Recommend"));
                    com.aliyun.teautil.Common.assertAsString(error.message);
                } catch (Exception _error) {
                    TeaException error = new TeaException(_error.getMessage(), _error);
                    // 错误 message
                    System.out.println(error.getMessage());
                    // 诊断地址
                    System.out.println(error.getData().get("Recommend"));
                    com.aliyun.teautil.Common.assertAsString(error.message);
                }
                return new ArrayList<>();
            }
            case YUNPIAN:
                return new ArrayList<>();
            default:
                return new ArrayList<>();
        }
    }

    // 查询短信模板的审核状态
    public void QuerySmsTemplate() {}

    // 查询模板标签
    public void ListTagResources() {}

    // 添加模板标签
    public void TagResources() {}

    // 删除模板标签
    public void UntagResources() {}

    // 获取OSS上传信息
    public void GetOSSInfoForCardTemplate() {}

    // 获取媒体资源ID
    public void GetMediaResourceId() {}

    // 创建卡片短信模板
    public void CreateCardSmsTemplate() {}

    // 查询卡片短信模板状态
    public void QueryCardSmsTemplate() {}

    // 检查手机号是否支持卡片短信
    public void QueryMobilesCardSupport() {}

    // 获取卡片短信短链
    public void GetCardSmsLink() {}

    // 查询卡片短信发送详情
    public void QueryCardSmsTemplateReport() {}

    private List<SmsTemplate> transformAliyun(
        List<QuerySmsTemplateListResponseBody.QuerySmsTemplateListResponseBodySmsTemplateList> smsTemplateList
    ) {
        List<SmsTemplate> result = new ArrayList<>();
        smsTemplateList
            .stream()
            .filter(smsTemplate -> smsTemplate.getAuditStatus().equals("AUDIT_STATE_PASS"))
            .filter(smsTemplate -> smsTemplate.getTemplateType() >= 0 && smsTemplate.getTemplateType() <= 2)
            .forEach(smsTemplate -> {
                SmsTemplate smsTemplate1 = new SmsTemplate();
                smsTemplate1.setCode(smsTemplate.getTemplateCode());
                smsTemplate1.setName(smsTemplate.getTemplateName());
                smsTemplate1.setContent(smsTemplate.getTemplateContent());
                smsTemplate1.setName(smsTemplate.getTemplateName());
                smsTemplate1.setSendType(MessageSendType.SMS);
                smsTemplate1.setEnabled(true);
                switch (smsTemplate.getTemplateType()) {
                    case 0:
                        smsTemplate1.setType(SmsTemplateType.MESSAGE);
                        break;
                    case 1:
                        smsTemplate1.setType(SmsTemplateType.PROMOTION);
                        break;
                    case 2:
                        smsTemplate1.setType(SmsTemplateType.VERIFICATION);
                        break;
                    case 6:
                    case 7:
                    default:
                        break;
                }
                result.add(smsTemplate1);
            });
        return result;
    }

    /**
     * Update specified field by smsSupplier
     */
    @Transactional
    public void updateBatch(SmsSupplierDTO changeSmsSupplierDTO, List<String> fieldNames, List<Long> ids) {
        List<String> relationshipNames = CollectionUtils.intersection(fieldNames, relationNames).stream().toList();
        fieldNames = CollectionUtils.subtract(fieldNames, relationshipNames).stream().toList();
        if (CollectionUtils.isNotEmpty(fieldNames)) {
            UpdateWrapper<SmsSupplier> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("id", ids);
            fieldNames.forEach(
                fieldName ->
                    updateWrapper.set(
                        CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName),
                        BeanUtil.getFieldValue(changeSmsSupplierDTO, fieldName)
                    )
            );
            this.update(updateWrapper);
        } else if (CollectionUtils.isNotEmpty(relationshipNames)) {
            List<SmsSupplier> smsSupplierList = this.listByIds(ids);
            if (CollectionUtils.isNotEmpty(smsSupplierList)) {
                smsSupplierList.forEach(smsSupplier -> {
                    relationshipNames.forEach(
                        relationName ->
                            BeanUtil.setFieldValue(
                                smsSupplier,
                                relationName,
                                BeanUtil.getFieldValue(smsSupplierMapper.toEntity(changeSmsSupplierDTO), relationName)
                            )
                    );
                    this.createOrUpdateAndRelatedRelations(smsSupplier, relationshipNames);
                });
            }
        }
    }

    public void updateRelationships(List<String> otherEntityIds, String relationshipName, List<Long> relatedIds, String operateType) {
        relatedIds.forEach(id -> {
            SmsSupplier byId = getById(id);
            Binder.bindRelations(byId, relationNames.stream().filter(rel -> !rel.equals(relationshipName)).toArray(String[]::new));
        });
    }
    // jhipster-needle-service-add-method - JHipster will add getters and setters here, do not remove

}
