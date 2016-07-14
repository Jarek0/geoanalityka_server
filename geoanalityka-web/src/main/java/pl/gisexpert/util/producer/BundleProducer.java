package pl.gisexpert.util.producer;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import pl.gisexpert.util.producer.qualifier.RESTI18n;

@Provider
public class BundleProducer {

	@Context
	private HttpServletRequest httpRequest;
    
    @Produces @Dependent @RESTI18n
    ResourceBundle produceMessageBundle() {
        return ResourceBundle.getBundle("pl.gisexpert.i18n.Text", this.getCurrentLocale());
    }
    
    private Locale getCurrentLocale() {
    	
        return httpRequest.getLocale();
    }
}
