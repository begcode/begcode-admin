package com.begcode.monolith.util.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.text.MessageFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponentsBuilder;

public final class IPageUtil {

    private static final String HEADER_X_TOTAL_COUNT = "X-Total-Count";
    private static final String HEADER_LINK_FORMAT = "<{0}>; rel=\"{1}\"";

    private IPageUtil() {}

    public static <T> HttpHeaders generatePaginationHttpHeaders(UriComponentsBuilder uriBuilder, IPage<T> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", Long.toString(page.getTotal()));
        int pageNumber = (int) page.getCurrent();
        int pageSize = (int) page.getSize();
        StringBuilder link = new StringBuilder();
        if (pageNumber < page.getPages() - 1) {
            link.append(prepareLink(uriBuilder, pageNumber + 1, pageSize, "next")).append(",");
        }

        if (pageNumber > 0) {
            link.append(prepareLink(uriBuilder, pageNumber - 1, pageSize, "prev")).append(",");
        }

        link
            .append(prepareLink(uriBuilder, (int) (page.getPages() - 1), pageSize, "last"))
            .append(",")
            .append(prepareLink(uriBuilder, 0, pageSize, "first"));
        headers.add("Link", link.toString());
        return headers;
    }

    private static String prepareLink(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize, String relType) {
        return MessageFormat.format("<{0}>; rel=\"{1}\"", preparePageUri(uriBuilder, pageNumber, pageSize), relType);
    }

    private static String preparePageUri(UriComponentsBuilder uriBuilder, int pageNumber, int pageSize) {
        return uriBuilder
            .replaceQueryParam("page", new Object[] { Integer.toString(pageNumber) })
            .replaceQueryParam("size", new Object[] { Integer.toString(pageSize) })
            .toUriString()
            .replace(",", "%2C")
            .replace(";", "%3B");
    }
}
