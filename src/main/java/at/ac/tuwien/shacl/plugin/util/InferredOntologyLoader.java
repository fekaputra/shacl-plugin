package at.ac.tuwien.shacl.plugin.util;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.inference.NoOpReasoner;
import org.protege.editor.owl.model.inference.OWLReasonerManager;
import org.protege.editor.owl.model.inference.ReasonerStatus;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;

import javax.swing.*;
import java.awt.*;

public class InferredOntologyLoader {
    public static OWLOntology loadInferredOntology(Component parent, OWLModelManager modelManager) throws OWLOntologyCreationException {
        OWLOntology ont = modelManager.getActiveOntology();

        OWLReasonerManager reasonerManager = modelManager.getOWLReasonerManager();
        warnUserIfReasonerIsNotConfigured(parent, reasonerManager);

        if (reasonerManager.getReasonerStatus() == ReasonerStatus.INITIALIZED
                || reasonerManager.getReasonerStatus() == ReasonerStatus.OUT_OF_SYNC) {
            OWLReasoner reasoner = modelManager.getReasoner();

            if (reasoner == null || reasoner instanceof NoOpReasoner)
                return ont;

            // Reasoner available, use inferred axioms

            return loadInferredOntologyFromReasoner(ont, reasoner);
        }
        else {
            return ont;
        }
    }

    public static OWLOntology loadInferredOntologyFromReasoner(OWLOntology ont, OWLReasoner reasoner) throws OWLOntologyCreationException {
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


    private static void warnUserIfReasonerIsNotConfigured(Component owner, OWLReasonerManager manager) {
        // NOTE: based on org.protege.editor.owl.model.inference.ReasonerUtilities.warnUserIfReasonerIsNotConfigured

        switch (manager.getReasonerStatus()) {
            case NO_REASONER_FACTORY_CHOSEN:
                JOptionPane.showMessageDialog(owner,
                        "Since no reasoner is activated, the validation will now be conducted without inferred facts.  To include inferred facts, please select a reasoner from the Reasoner menu and then select Start reasoner.",
                        "Reasoner not initialized",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            case REASONER_NOT_INITIALIZED:
                JOptionPane.showMessageDialog(owner,
                        "Since the reasoner is not activated, the validation will now be conducted without inferred facts.  To include inferred facts, please go to the Reasoner menu and select Start reasoner.",
                        "Reasoner not initialized",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            case OUT_OF_SYNC:
                JOptionPane.showMessageDialog(owner,
                        "The reasoner is not synchronized.  This may produce outdated and misleading validation results.  Please go to the Reasoner menu, select Synchronize reasoner and run the validation again.",
                        "Reasoner out of sync",
                        JOptionPane.WARNING_MESSAGE);
                break;
            case INITIALIZATION_IN_PROGRESS:
                JOptionPane.showMessageDialog(owner,
                        "Reasoner still initializing.  The validation will now be conducted without inferred facts.  Please run the validation again when the reasoner is initialized.",
                        "Reasoner initializing",
                        JOptionPane.WARNING_MESSAGE);
                break;
            case INITIALIZED:
                break; // nothing to do...
        }
    }
}
