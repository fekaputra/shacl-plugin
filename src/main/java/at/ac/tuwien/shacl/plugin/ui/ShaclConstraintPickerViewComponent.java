package at.ac.tuwien.shacl.plugin.ui;


import org.protege.editor.owl.ui.view.AbstractOWLViewComponent;

import java.awt.*;

/**
 * Link to the plugin.xml for the constraint picker.
 */
public class ShaclConstraintPickerViewComponent extends AbstractOWLViewComponent {
    @Override
    protected void initialiseOWLView() throws Exception {
        this.setLayout(new BorderLayout());
        this.add(new ShaclConstraintPickerPanel(), BorderLayout.CENTER);
    }

    @Override
    protected void disposeOWLView() {
    }

//    //TODO use this to update header text of view
//    @Override
//    public void setHeaderText(String text) {
//        //this.getViewBar().getViewBanner().setText(text);
//    }
}
