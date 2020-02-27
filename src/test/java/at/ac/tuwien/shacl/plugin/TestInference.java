package at.ac.tuwien.shacl.plugin;

import at.ac.tuwien.shacl.plugin.events.ShaclValidation;
import at.ac.tuwien.shacl.plugin.util.InferredOntologyLoader;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import org.junit.Assert;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TestInference {
    private final URL example3 = ShaclValidation.class.getClassLoader().getResource("example3-data.owl");

    private OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

    @Test public void testInferenceFilling()
            throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
        int axiomsInitial;
        int axiomsCopied;

        OWLOntology ontCopied;

        try (InputStream in = example3.openStream()) {
            OWLOntology ont = manager.loadOntologyFromOntologyDocument(in);

            axiomsInitial = ont.getAxiomCount();

            ont.saveOntology(System.out);

            OWLReasoner reasoner = PelletReasonerFactory.getInstance().createReasoner(ont);

            reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);

            ontCopied = InferredOntologyLoader.loadInferredOntologyFromReasoner(ont, reasoner);
        }

        axiomsCopied = ontCopied.getAxiomCount();

        // NOTE / TODO: just a very basic test to see if it actually works. Should test in-depth.

        Assert.assertTrue("axiomsCopied >= axiomsInitial", axiomsCopied >= axiomsInitial);

        ontCopied.saveOntology(System.out);
    }

}
