package at.ac.tuwien.shacl.plugin.syntax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

/**
 *
 * @author elvio
 * @author andrea.nuzzolese
 */
public class JenaOwlConverter {

    private boolean availablemain = true;

    public JenaOwlConverter() {
    }

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * This function converts an ontology object from OWLapi to Jena
     *
     * @param owlmodel {An OWLOntology object}
     * @param format {RDF/XML or TURTLE}
     * @return {An OntModel that is a Jena object}
     */
    public synchronized OntModel ModelOwlToJenaConvert(OWLOntology owlmodel, String format) {

        while (availablemain == false) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.err.println("ModelOwlToJenaConvert::: " + e);
            }
        }

        availablemain = false;
        OWLOntologyID id;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            OWLOntologyManager owlmanager = owlmodel.getOWLOntologyManager();

            format = format.trim();

            if (format.equals("TURTLE") || format.equals("RDF/XML")) {

                if (format.equals("TURTLE"))
                    owlmanager.setOntologyFormat(owlmodel, new TurtleDocumentFormat());
                if (format.equals("RDF/XML"))
                    owlmanager.setOntologyFormat(owlmodel, new RDFXMLDocumentFormat());

                OWLDocumentFormat owlformat = owlmanager.getOntologyFormat(owlmodel);

                owlmanager.saveOntology(owlmodel, owlformat, out);

                OntModel jenamodel = ModelFactory.createOntologyModel();
                id = owlmodel.getOntologyID();
                jenamodel.read(new ByteArrayInputStream(out.toByteArray()),
                        id.toString().replace("<", "").replace(">", ""), format);

                availablemain = true;
                notifyAll();
                return jenamodel;
            } else {
                System.err.println("The only format supported is RDF/XML or TURTLE. Please check the format!");

                availablemain = true;
                notifyAll();
                return null;
            }
        } catch (OWLOntologyStorageException eos) {
            System.err.print("ModelOwlToJenaConvert::: ");
            eos.printStackTrace();
            return null;
        }
    }

}
