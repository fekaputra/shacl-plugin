package at.ac.tuwien.shacl.plugin.util;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.inference.NoOpReasoner;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.inference.ReasonerStatus;
import org.protege.editor.owl.model.inference.ReasonerUtilities;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;

import java.awt.*;

public class InferredOntologyLoader {
    public static OWLOntology loadInferredOntology(Component parent, OWLModelManager modelManager) throws OWLOntologyCreationException {
        OWLOntology ont = modelManager.getActiveOntology();

        OWLReasonerManager reasonerManager = modelManager.getOWLReasonerManager();
        ReasonerUtilities.warnUserIfReasonerIsNotConfigured(parent, reasonerManager);

        if (reasonerManager.getReasonerStatus() == ReasonerStatus.INITIALIZED
                || reasonerManager.getReasonerStatus() == ReasonerStatus.OUT_OF_SYNC) {
            OWLReasoner reasoner = modelManager.getReasoner();

            if (reasoner == null || reasoner instanceof NoOpReasoner)
                return ont;

            // Reasoner available, use inferred axioms

            // NOTE: we cannot use modelManager.getOWLOntologyManager()
            // as this would mess up the loaded ontologies in Protégé
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            OWLDataFactory fac = manager.getOWLDataFactory();

            // copy all axioms from the ontology and include all axioms from all imported ontologies
            OWLOntology infOnt = manager.createOntology(ont.getAxioms(Imports.INCLUDED));

            // copy all axioms that the reasoner inferred
            InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner);
            iog.fillOntology(fac, infOnt);

            return infOnt;
        }
        else {
            return ont;
        }
    }
}
