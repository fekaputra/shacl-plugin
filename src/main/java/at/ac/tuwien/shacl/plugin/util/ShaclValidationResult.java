package at.ac.tuwien.shacl.plugin.util;

import org.apache.jena.rdf.model.*;
import org.topbraid.shacl.vocabulary.SH;

public class ShaclValidationResult {

    public final String resultSeverity;
    public final String sourceShape;
    public final String resultMessage;
    public final String focusNode;
    public final String resultPath;
    public final String value;


    public ShaclValidationResult(Model model, Resource subject) {
        this.resultSeverity = getQName(model, subject.getProperty(SH.resultSeverity));
        this.sourceShape    = getQName(model, subject.getProperty(SH.sourceShape));
        this.resultMessage  = getQName(model, subject.getProperty(SH.resultMessage));
        this.focusNode      = getQName(model, subject.getProperty(SH.focusNode));
        this.resultPath     = getQName(model, subject.getProperty(SH.resultPath));
        this.value          = getQName(model, subject.getProperty(SH.value));
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
}
