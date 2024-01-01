package com.begcode.monolith.util;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Stream;

public class PlaceholderUtil {

    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    private static final PlaceholderUtil DEFAULT_RESOLVER = new PlaceholderUtil();
    private String placeholderPrefix = "${";
    private String placeholderSuffix = "}";

    private PlaceholderUtil() {}

    private PlaceholderUtil(String placeholderPrefix, String placeholderSuffix) {
        this.placeholderPrefix = placeholderPrefix;
        this.placeholderSuffix = placeholderSuffix;
    }

    public static PlaceholderUtil getDefaultResolver() {
        return DEFAULT_RESOLVER;
    }

    public static PlaceholderUtil getResolver(String placeholderPrefix, String placeholderSuffix) {
        return new PlaceholderUtil(placeholderPrefix, placeholderSuffix);
    }

    public String resolve(String content, String... values) {
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            return content;
        } else {
            int valueIndex = 0;

            StringBuilder result;
            String replaceContent;
            for (
                result = new StringBuilder(content);
                start != -1;
                start = result.indexOf(this.placeholderPrefix, start + replaceContent.length())
            ) {
                int end = result.indexOf(this.placeholderSuffix);
                replaceContent = values[valueIndex++];
                result.replace(start, end + this.placeholderSuffix.length(), replaceContent);
            }

            return result.toString();
        }
    }

    public String resolve(String content, Object[] values) {
        return this.resolve(content, Stream.of(values).map(String::valueOf).toArray(String[]::new));
    }

    public String resolveByRule(String content, Function<String, String> rule) {
        int start = content.indexOf(this.placeholderPrefix);
        if (start == -1) {
            return content;
        } else {
            StringBuilder result;
            String replaceContent;
            for (
                result = new StringBuilder(content);
                start != -1;
                start = result.indexOf(this.placeholderPrefix, start + replaceContent.length())
            ) {
                int end = result.indexOf(this.placeholderSuffix, start + 1);
                String placeholder = result.substring(start + this.placeholderPrefix.length(), end);
                replaceContent = placeholder.trim().isEmpty() ? "" : (String) rule.apply(placeholder);
                result.replace(start, end + this.placeholderSuffix.length(), replaceContent);
            }

            return result.toString();
        }
    }

    public String resolveByMap(String content, final Map<String, Object> valueMap) {
        return this.resolveByRule(
                content,
                placeholderValue -> {
                    return String.valueOf(valueMap.get(placeholderValue));
                }
            );
    }

    public String resolveByProperties(String content, final Properties properties) {
        Objects.requireNonNull(properties);
        return this.resolveByRule(content, properties::getProperty);
    }
}
