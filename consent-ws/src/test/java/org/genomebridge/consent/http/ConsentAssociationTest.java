package org.genomebridge.consent.http;

import java.util.*;
import static io.dropwizard.testing.FixtureHelpers.*;
import static org.fest.assertions.api.Assertions.assertThat;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.genomebridge.consent.http.models.ConsentAssociation;

/**
 * Created by egolin on 9/16/14.
 */
public class ConsentAssociationTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private static ConsentAssociation buildConsentAssociation(String atype, String... elements) {
        final ArrayList<String> elem_list = new ArrayList<String>();
        for (String elem : elements)
            elem_list.add(elem);
        return new ConsentAssociation(atype, elem_list);
    }



    @Test
    public void serializesToJSON() throws Exception {
        final ConsentAssociation consent_association = buildConsentAssociation("sample", "SM-1234", "SM-5678");
        assertThat(MAPPER.writeValueAsString(consent_association)).isEqualTo(fixture("fixtures/consentassociation.json"));
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        final ConsentAssociation consent_association = buildConsentAssociation("sample", "SM-1234", "SM-5678");
        assertThat(MAPPER.readValue(fixture("fixtures/consentassociation.json"), ConsentAssociation.class)).isEqualsToByComparingFields(consent_association);
    }

    @Test
    public void testEqualsTrue() throws Exception {
        final ConsentAssociation consent_assoc1 = buildConsentAssociation("sample", "SM-1234", "SM-5678");
        final ConsentAssociation consent_assoc2 = buildConsentAssociation("sample", "SM-1234", "SM-5678");
        assertThat(consent_assoc1).isEqualTo(consent_assoc2);
    }

    @Test
    public void testEqualsNotMatchingElements() throws Exception {
        final ConsentAssociation consent_assoc1 = buildConsentAssociation("sample", "SM-1234", "SM-5678");
        final ConsentAssociation consent_assoc2 = buildConsentAssociation("sample", "SM-4321", "SM-8765");
        assertThat(consent_assoc1).isNotEqualTo(consent_assoc2);
    }

    @Test
    public void testEqualsNotMatchingAssociationType() throws Exception {
        final ConsentAssociation consent_assoc1 = buildConsentAssociation("sample", "SM-1234", "SM-5678");
        final ConsentAssociation consent_assoc2 = buildConsentAssociation("sampleSet", "SM-1234", "SM-5678");
        assertThat(consent_assoc1).isNotEqualTo(consent_assoc2);
    }

    @Test
    public void testToString() throws Exception {
        final ConsentAssociation consent_association = buildConsentAssociation("sample", "SM-1234", "SM-5678");
        assertThat(consent_association.toString()).isNotNull();
    }

    @Test
    public void testIsAssociationType() throws Exception {
        final ConsentAssociation consent_association = buildConsentAssociation("sample", "SM-1234", "SM-5678");
        assertThat(consent_association.isAssociationType("sample")).isTrue();
        assertThat(consent_association.isAssociationType("sampleSet")).isFalse();
    }
}
