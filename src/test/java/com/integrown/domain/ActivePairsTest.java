package com.integrown.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.integrown.web.rest.TestUtil;

public class ActivePairsTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivePairs.class);
        ActivePairs activePairs1 = new ActivePairs();
        activePairs1.setId("id1");
        ActivePairs activePairs2 = new ActivePairs();
        activePairs2.setId(activePairs1.getId());
        assertThat(activePairs1).isEqualTo(activePairs2);
        activePairs2.setId("id2");
        assertThat(activePairs1).isNotEqualTo(activePairs2);
        activePairs1.setId(null);
        assertThat(activePairs1).isNotEqualTo(activePairs2);
    }
}
