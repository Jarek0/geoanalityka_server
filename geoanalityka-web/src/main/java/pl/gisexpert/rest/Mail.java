package pl.gisexpert.rest;

import pl.gisexpert.rest.util.producer.qualifier.RESTI18n;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.UUID;

@lombok.Setter
@lombok.Getter

public class Mail {
    @Inject
    @RESTI18n
    private ResourceBundle i18n;
    private String subject;
    MessageFormat formatter;
    public Mail(){
        subject = "Public Survey bilgoraj - potwierdzenie rejestracji użytkownika";
        formatter= new MessageFormat("");
        i18n= ResourceBundle.getBundle("pl.gisexpert.i18n.Text");
        formatter.setLocale(i18n.getLocale());//
    }

}
