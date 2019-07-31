package at.ac.tuwien.shacl.plugin.util;

import java.util.Comparator;

import at.ac.tuwien.shacl.plugin.syntax.JenaOwlConverter;

public class ShaclValidationResultComparator implements Comparator<ShaclValidationResult> {

    public final static ShaclValidationResultComparator INSTANCE = new ShaclValidationResultComparator();

    @Override
    public int compare(ShaclValidationResult r1, ShaclValidationResult r2) {
        int compareSeverity = r1.resultSeverity.compareTo(r2.resultSeverity);
        if (compareSeverity != 0)
            return compareSeverity;

        int compareFocusNode = JenaOwlConverter.compareRDFNode(r1.focusNode, r2.focusNode);
        if (compareFocusNode != 0)
            return compareFocusNode;

        int compareResultPath = JenaOwlConverter.compareRDFNode(r1.resultPath, r2.resultPath);
        if (compareResultPath != 0)
            return compareResultPath;

        int compareShape = JenaOwlConverter.compareRDFNode(r1.sourceShape, r2.sourceShape);
        if (compareShape != 0)
            return compareShape;

        int compareResultMessage = JenaOwlConverter.compareRDFNode(r1.resultMessage, r2.resultMessage);
        if (compareResultMessage != 0)
            return compareResultMessage;

        return JenaOwlConverter.compareRDFNode(r1.value, r2.value);
    }

}
