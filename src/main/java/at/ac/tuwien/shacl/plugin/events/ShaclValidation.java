package at.ac.tuwien.shacl.plugin.events;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import org.topbraid.shacl.constraints.ModelConstraintValidator;

import java.net.URI;
import java.util.Observable;

/**
 *
 */
public class ShaclValidation extends Observable {
    public void runValidation(Dataset dataset, URI uri) throws InterruptedException {
        System.out.println("running validation...");
        //Model resultsModel = model;
        Model model = ModelConstraintValidator.get().validateModel(dataset, uri, null, false, null);
        System.out.println("validation finished...");
        this.setChanged();
        this.notifyObservers(model);
        System.out.println("notifiers notified...");
    }
}
