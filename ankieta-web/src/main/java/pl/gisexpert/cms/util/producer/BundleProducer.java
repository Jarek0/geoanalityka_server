package pl.gisexpert.cms.util.producer;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import pl.gisexpert.cms.util.producer.qualifier.I18n;

public class BundleProducer {

    @Inject
    private FacesContext facesContext;
    
    @I18n
    @Produces
    ResourceBundle produceMessageBundle() {
        return ResourceBundle.getBundle("pl.gisexpert.i18n.Text", this.getCurrentLocale());
    }
    
    private Locale getCurrentLocale() {
        return this.facesContext.getViewRoot().getLocale();
    }
}
