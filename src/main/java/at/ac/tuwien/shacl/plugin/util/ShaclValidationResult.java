package at.ac.tuwien.shacl.plugin.util;

import org.apache.jena.rdf.model.*;
import org.topbraid.shacl.vocabulary.SH;

public class ShaclValidationResult {

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
