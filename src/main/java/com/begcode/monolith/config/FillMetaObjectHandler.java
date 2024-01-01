package com.begcode.monolith.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.begcode.monolith.security.SecurityUtils;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

// jhipster-needle-add-import - JHipster will add getters and setters here, do not remove

@Component
public class FillMetaObjectHandler implements MetaObjectHandler {

    private static final Map<String, Field[]> fieldMap = new HashMap<>();

    @Override
    public void insertFill(MetaObject metaObject) {
        String className = metaObject.getOriginalObject().getClass().getName();
        fieldMap.computeIfAbsent(className, k -> getFields(metaObject.getOriginalObject()));
        Field[] fields = fieldMap.get(className);
        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof TableField) {
                    if (
                        ((TableField) annotation).fill().equals(FieldFill.INSERT_UPDATE) ||
                        ((TableField) annotation).fill().equals(FieldFill.INSERT)
                    ) {
                        if (ZonedDateTime.class.equals(field.getType())) {
                            this.strictInsertFill(metaObject, field.getName(), ZonedDateTime.class, ZonedDateTime.now());
                        } else if (LocalDateTime.class.equals(field.getType())) {
                            this.strictInsertFill(metaObject, field.getName(), LocalDateTime.class, LocalDateTime.now());
                        } else if (Instant.class.equals(field.getType())) {
                            this.strictInsertFill(metaObject, field.getName(), Instant.class, Instant.now());
                        }
                        if ("created_by".equals(((TableField) annotation).value())) {
                            this.strictInsertFill(metaObject, field.getName(), Long.class, SecurityUtils.getCurrentUserId().orElse(null));
                        }
                        if ("last_modified_by".equals(((TableField) annotation).value())) {
                            this.strictInsertFill(metaObject, field.getName(), Long.class, SecurityUtils.getCurrentUserId().orElse(null));
                        }
                        if ("del_flag".equals(((TableField) annotation).value())) {
                            this.strictInsertFill(metaObject, field.getName(), Boolean.class, false);
                        }
                    }
                }
            }
        }
        // jhipster-needle-fill-insert-method - JHipster will add getters and setters here, do not remove
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Field[] fields = metaObject.getOriginalObject().getClass().getDeclaredFields();
        for (Field field : fields) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof TableField) {
                    if (
                        ((TableField) annotation).fill().equals(FieldFill.INSERT_UPDATE) ||
                        ((TableField) annotation).fill().equals(FieldFill.UPDATE)
                    ) {
                        if (ZonedDateTime.class.equals(field.getType())) {
                            this.strictUpdateFill(metaObject, field.getName(), ZonedDateTime.class, ZonedDateTime.now());
                        } else if (LocalDateTime.class.equals(field.getType())) {
                            this.strictUpdateFill(metaObject, field.getName(), LocalDateTime.class, LocalDateTime.now());
                        } else if (Instant.class.equals(field.getType())) {
                            this.strictUpdateFill(metaObject, field.getName(), Instant.class, Instant.now());
                        }
                        if ("last_modified_by".equals(((TableField) annotation).value())) {
                            this.strictUpdateFill(metaObject, field.getName(), Long.class, SecurityUtils.getCurrentUserId().orElse(null));
                        }
                    }
                }
            }
        }
        // jhipster-needle-fill-update-method - JHipster will add getters and setters here, do not remove
    }

    /**
     * 获取所有字段
     * @param metaObject
     * @return
     */
    public static Field[] getFields(Object metaObject) {
        Class clazz = metaObject.getClass();
        List<Field> fieldList = new ArrayList<>(16);
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation instanceof TableField) {
                        if (
                            ((TableField) annotation).fill().equals(FieldFill.INSERT_UPDATE) ||
                            ((TableField) annotation).fill().equals(FieldFill.INSERT) ||
                            ((TableField) annotation).fill().equals(FieldFill.UPDATE)
                        ) {
                            if (ZonedDateTime.class.equals(field.getType())) {
                                if (!fieldList.contains(field)) {
                                    fieldList.add(field);
                                }
                            } else if (LocalDateTime.class.equals(field.getType())) {
                                if (!fieldList.contains(field)) {
                                    fieldList.add(field);
                                }
                            } else if (Instant.class.equals(field.getType())) {
                                if (!fieldList.contains(field)) {
                                    fieldList.add(field);
                                }
                            }
                            if ("created_by".equals(((TableField) annotation).value())) {
                                if (!fieldList.contains(field)) {
                                    fieldList.add(field);
                                }
                            }
                            if ("last_modified_by".equals(((TableField) annotation).value())) {
                                if (!fieldList.contains(field)) {
                                    fieldList.add(field);
                                }
                            }
                            if ("del_flag".equals(((TableField) annotation).value())) {
                                if (!fieldList.contains(field)) {
                                    fieldList.add(field);
                                }
                            }
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        Field[] f = new Field[fieldList.size()];
        return fieldList.toArray(f);
    }
}
