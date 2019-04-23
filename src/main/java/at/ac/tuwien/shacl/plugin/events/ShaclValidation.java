package at.ac.tuwien.shacl.plugin.events;

import java.util.Observable;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import org.topbraid.shacl.validation.ValidationUtil;
import org.topbraid.shacl.util.ModelPrinter;

import at.ac.tuwien.shacl.plugin.util.ShaclValidationReport;

/**
 *
 */
public class ShaclValidation extends Observable {

    public void runValidation2(Model shaclModel, Model dataModel) {
        // Run the validator
        Resource results = ValidationUtil.validateModel(dataModel, shaclModel, true);

        // Print violations
        System.out.println("--- ************* ---");
        System.out.println(ModelPrinter.get().print(results.getModel()));


        ShaclValidationReport report = new ShaclValidationReport(results);

        this.setChanged();
        this.notifyObservers(report);
    }

}
