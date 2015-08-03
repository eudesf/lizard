package org.coode.dlquery.lizard;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/09/2013
 */
public enum ResultsSection {

    DIRECT_SUPER_CLASSES("Direct superclasses"),

    DIRECT_SUB_CLASSES("Direct subclasses"),

    SUPER_CLASSES("Superclasses"),

    SUB_CLASSES("Subclasses"),

    EQUIVALENT_CLASSES("Equivalent classes"),

    INSTANCES("Instances");


    private String displayName;

    private ResultsSection(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
