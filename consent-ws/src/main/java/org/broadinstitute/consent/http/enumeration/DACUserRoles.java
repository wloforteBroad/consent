package org.broadinstitute.consent.http.enumeration;

public enum DACUserRoles {

    MEMBER("Member"), CHAIRPERSON("Chairperson"), ALUMNI("Alumni"), ADMIN("Admin"), RESEARCHER("Researcher"), DATA_OWNER("DataOwner");

    private String value;

    DACUserRoles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getValue(String value) {
        for (DACUserRoles e : DACUserRoles.values()) {
            if (e.getValue().equalsIgnoreCase(value)) {
                return e.getValue();
            }
        }
        return null;
    }

}
