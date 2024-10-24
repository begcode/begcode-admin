package com.begcode.monolith.settings.domain;

import static com.begcode.monolith.settings.domain.CommonFieldDataTestSamples.*;
import static com.begcode.monolith.settings.domain.DictionaryTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.begcode.monolith.web.rest.TestUtil;
import java.util.*;
import org.junit.jupiter.api.Test;

class DictionaryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dictionary.class);
        Dictionary dictionary1 = getDictionarySample1();
        Dictionary dictionary2 = new Dictionary();
        assertThat(dictionary1).isNotEqualTo(dictionary2);

        dictionary2.setId(dictionary1.getId());
        assertThat(dictionary1).isEqualTo(dictionary2);

        dictionary2 = getDictionarySample2();
        assertThat(dictionary1).isNotEqualTo(dictionary2);
    }

    @Test
    void itemsTest() {
        Dictionary dictionary = getDictionaryRandomSampleGenerator();
        CommonFieldData commonFieldDataBack = getCommonFieldDataRandomSampleGenerator();
        // todo dictionary.addItems(commonFieldDataBack);
        // assertThat(dictionary.getItems()).containsOnly(commonFieldDataBack);

        // dictionary.removeItems(commonFieldDataBack);
        // assertThat(dictionary.getItems()).doesNotContain(commonFieldDataBack);
    }
}
