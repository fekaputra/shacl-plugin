package at.ac.tuwien.shacl.plugin.util;

import org.apache.jena.rdf.model.*;
import org.topbraid.shacl.vocabulary.SH;

public class ShaclValidationResult implements Comparable<ShaclValidationResult> {

    public enum Severity {VIOLATION, WARNING, INFO, UNKNOWN};

    public final Severity resultSeverity;
    public final String sourceShape;
    public final String resultMessage;
    public final String focusNode;
    public final String resultPath;
    public final String value;


    public ShaclValidationResult(Model model, Resource subject) {
        this.resultSeverity = getSeverity(subject.getProperty(SH.resultSeverity));
        this.sourceShape    = getQName(model, subject.getProperty(SH.sourceShape));
        this.resultMessage  = getQName(model, subject.getProperty(SH.resultMessage));
        this.focusNode      = getQName(model, subject.getProperty(SH.focusNode));
        this.resultPath     = getQName(model, subject.getProperty(SH.resultPath));
        this.value          = getQName(model, subject.getProperty(SH.value));
    }

    @Override
    public int compareTo(ShaclValidationResult o) {
        int compareSeverity = this.resultSeverity.compareTo(o.resultSeverity);
        if (compareSeverity != 0)
            return compareSeverity;

        int compareFocusNode = this.focusNode.compareTo(o.focusNode);
        if (compareFocusNode != 0)
            return compareFocusNode;

        int compareResultPath = this.resultPath.compareTo(o.resultPath);
        if (compareResultPath != 0)
            return compareResultPath;

        int compareShape = this.sourceShape.compareTo(o.sourceShape);
        if (compareShape != 0)
            return compareShape;

        int compareResultMessage = this.resultMessage.compareTo(o.resultMessage);
        if (compareResultMessage != 0)
            return compareResultMessage;

        return this.value.compareTo(o.value);
    }

    /**
     * Get a qualified name for an URI, if it exists, otherwise just return the original string.
     *
     * @param model Model containing the prefixes
     * @param stmt Statement to be checked for a qname
     * @return qname if one exists, otherwise the original string of the object
     */
    private static String getQName(Model model, Statement stmt) {
        if (stmt != null && stmt.getObject() != null) {
            String string = stmt.getObject().toString();
            return model.qnameFor(string) == null ? string : model.qnameFor(string);
        } else {
            return "";
        }
    }

    private static Severity getSeverity(Statement stmt) {
        if (stmt != null && stmt.getObject() != null && stmt.getObject().isResource()) {
            Resource r = stmt.getObject().asResource();

            if (r.equals(SH.Info)) {
                return Severity.INFO;
            } else if (r.equals(SH.Warning)) {
                return Severity.WARNING;
            } else if (r.equals(SH.Violation)) {
                return Severity.VIOLATION;
            }
        }

        return Severity.UNKNOWN;
    }
}
