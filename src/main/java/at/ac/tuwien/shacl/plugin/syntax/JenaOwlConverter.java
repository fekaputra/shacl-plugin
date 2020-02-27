package at.ac.tuwien.shacl.plugin.syntax;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author elvio
 * @author andrea.nuzzolese
 */
public class JenaOwlConverter {

    private boolean availablemain = true;

    public JenaOwlConverter() {
    }

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Get a qualified name for an URI, if it exists, otherwise just return the original string.
     *
     * @param model Model containing the prefixes
     * @param node  Statement to be checked for a qname
     * @return qname if one exists, otherwise the original string of the object
     */
    public static String getQName(Model model, RDFNode node) {
        if (node != null) {
            String string = node.toString();
            String qName = model.qnameFor(string);
            return qName == null ? string : qName;
        } else {
            return "";
        }
    }

    public static int compareRDFNode(RDFNode a, RDFNode b) {
        if (a == b)
            return 0;
        if (a == null)
            return 1;
        if (b == null)
            return -1;
        if (a.equals(b))
            return 0;
        if (a.isURIResource() && b.isURIResource())
            return (a.asResource().getURI().compareTo(b.asResource().getURI()));
        return a.toString().compareTo(b.toString());

        /*
        // TODO: compare (numeric) literals by their value instead of their string representations...
        // NOTE: (currently) not needed in this project, only in general.
        if (a.isLiteral() && b.isLiteral()) {
            Literal aLit = a.asLiteral();
            Literal bLit = b.asLiteral();
            if (aLit.sameValueAs(bLit)) return 0;
            if (aLit.getDatatype() == bLit.getDatatype()) // TODO..
        }
        */
    }

    /**
     * This function converts an ontology object from OWLapi to Jena
     *
     * @param owlmodel {An OWLOntology object}
     * @param format   {RDF/XML or TURTLE}
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
