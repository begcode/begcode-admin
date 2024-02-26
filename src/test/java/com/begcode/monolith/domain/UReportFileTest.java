package com.begcode.monolith.domain;

import static com.begcode.monolith.domain.UReportFileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class UReportFileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UReportFile.class);
        UReportFile uReportFile1 = getUReportFileSample1();
        UReportFile uReportFile2 = new UReportFile();
        assertThat(uReportFile1).isNotEqualTo(uReportFile2);

        uReportFile2.setId(uReportFile1.getId());
        assertThat(uReportFile1).isEqualTo(uReportFile2);

        uReportFile2 = getUReportFileSample2();
        assertThat(uReportFile1).isNotEqualTo(uReportFile2);
    }
}
