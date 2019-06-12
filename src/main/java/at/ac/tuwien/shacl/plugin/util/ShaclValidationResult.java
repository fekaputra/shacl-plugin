package at.ac.tuwien.shacl.plugin.util;

import org.apache.jena.rdf.model.*;
import org.topbraid.shacl.vocabulary.SH;

import at.ac.tuwien.shacl.plugin.syntax.JenaOwlConverter;

public class ShaclValidationResult implements Comparable<ShaclValidationResult> {

    public enum Severity {VIOLATION, WARNING, INFO, UNKNOWN};

    public final Model model;

    public final Severity resultSeverity;

    public final RDFNode sourceShape;
    public final RDFNode resultMessage;
    public final RDFNode focusNode;
    public final RDFNode resultPath;
    public final RDFNode value;


    public ShaclValidationResult(Model model, Resource subject) {
        this.model = model;

        this.resultSeverity = getSeverity(subject.getProperty(SH.resultSeverity));

        this.sourceShape    = tryGetObject(subject.getProperty(SH.sourceShape));
        this.resultMessage  = tryGetObject(subject.getProperty(SH.resultMessage));
        this.focusNode      = tryGetObject(subject.getProperty(SH.focusNode));
        this.resultPath     = tryGetObject(subject.getProperty(SH.resultPath));
        this.value          = tryGetObject(subject.getProperty(SH.value));
    }

    @Override
    public int compareTo(ShaclValidationResult o) {
        int compareSeverity = this.resultSeverity.compareTo(o.resultSeverity);
        if (compareSeverity != 0)
            return compareSeverity;

        int compareFocusNode = JenaOwlConverter.compareRDFNode(this.focusNode, o.focusNode);
        if (compareFocusNode != 0)
            return compareFocusNode;

        int compareResultPath = JenaOwlConverter.compareRDFNode(this.resultPath, o.resultPath);
        if (compareResultPath != 0)
            return compareResultPath;

        int compareShape = JenaOwlConverter.compareRDFNode(this.sourceShape, o.sourceShape);
        if (compareShape != 0)
            return compareShape;

        int compareResultMessage = JenaOwlConverter.compareRDFNode(this.resultMessage, o.resultMessage);
        if (compareResultMessage != 0)
            return compareResultMessage;

        return JenaOwlConverter.compareRDFNode(this.value, o.value);
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

    private static RDFNode tryGetObject(Statement stmt) {
        if (stmt != null) return stmt.getObject();
        return null;
    }
}
