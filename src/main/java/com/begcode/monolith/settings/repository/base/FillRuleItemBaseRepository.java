package com.begcode.monolith.settings.repository.base;

import com.begcode.monolith.settings.domain.FillRuleItem;
import com.diboot.core.binding.Binder;
import com.diboot.core.mapper.BaseCrudMapper;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.NoRepositoryBean;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

/**
 * Spring Data JPA repository for the FillRuleItem entity.
 */
@NoRepositoryBean
public interface FillRuleItemBaseRepository<E extends FillRuleItem> extends BaseCrudMapper<FillRuleItem> {
    default Optional<FillRuleItem> findOneWithEagerRelationships(Long id) {
        return Optional.ofNullable(this.selectById(id)).map(fillRuleItem -> {
            Binder.bindRelations(fillRuleItem, new String[] { "fillRule" });
            return fillRuleItem;
        });
    }

    default List<FillRuleItem> findAll() {
        return this.selectList(null);
    }

    default Optional<FillRuleItem> findById(Long id) {
        return Optional.ofNullable(this.selectById(id));
    }

    @Select("delete from fill_rule_item fill_rule_item where fill_rule_item.fill_rule_id = #{fillRuleId}")
    void deleteAllByFillRuleId(@Param("fillRuleId") Long fillRuleId);

    default FillRuleItem saveAndGet(FillRuleItem fillRuleItem) {
        if (Objects.nonNull(fillRuleItem.getId())) {
            this.updateById(fillRuleItem);
        } else {
            this.insert(fillRuleItem);
        }
        return this.selectById(fillRuleItem.getId());
    }
    // jhipster-needle-repository-add-method - JHipster will add getters and setters here, do not remove

}
