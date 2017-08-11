package pl.gisexpert.rest;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.UUID;

@lombok.Setter
@lombok.Getter

public class Mail {
    private String subject,confirmAccountURL;
    MessageFormat formatter;
    private ResourceBundle i18n;
    public Mail(){
        subject = "Public Survey bilgoraj - potwierdzenie rejestracji użytkownika";
        formatter= new MessageFormat("");
        i18n= ResourceBundle.getBundle("pl.gisexpert.i18n.Text");
        formatter.setLocale(i18n.getLocale());//
    }

}
