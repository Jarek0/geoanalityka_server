package pl.gisexpert.rest.Validator;

import pl.gisexpert.cms.data.AccountRepository;
import pl.gisexpert.rest.model.RegisterForm;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.*;
import java.util.UUID;

@RequestScoped
public class Validator {

    @Inject
    private AccountRepository accountRepository;


    public ArrayList validate(RegisterForm form){
        return checkFirstname(form);
    }
    private ArrayList checkFirstname(RegisterForm form){
        String firstname = form.getFirstname();
        ArrayList obj = checkLastname(form);
        if(matching("(^[A-Z ÀÁÂÃÄÅ ĄŻŹ ÒÓÔÕÖØ Ł Ć ĘŚ Ń ÈÉÊË Ç ÌÍÎÏ ÙÚÛÜ Ñ ]{1})([a-zàáâãäåąźżòóÓłćęśńôõöøèéêëçìíîïùúûüÿñ]{1,}$)",firstname) && firstname.length()<=30){
                return obj;
        }else {
            return addError (obj,"Imię powinno zaczynać się z wielkiej litery oraz mieć mniej niż 30 znaków");
        }
    }
    private ArrayList checkLastname(RegisterForm form) {
        String lastname = form.getLastname();
        ArrayList obj = checkMail(form);
        if (matching("(^[A-Z ÀÁÂÃÄÅ ĄŻŹ ÒÓÔÕÖØ Ł Ć ĘŚ Ń ÈÉÊË Ç ÌÍÎÏ ÙÚÛÜ Ñ ]{1})([a-zàáâãäåąźżòóÓłćęśńôõöøèéêëçìíîïùúûüÿñ]{1,}$)",lastname) && lastname.length()<=30) {
                return obj;
        } else {
                return addError(obj,"Nazwisko powinno zaczynać się z wielkiej litery oraz mieć mniej niż 30 znaków");
        }
    }
    private ArrayList checkMail(RegisterForm form){
        String username = form.getUsername();
        ArrayList obj = checkPassword(form);
        if(accountRepository.checkIfUserWithThisMailExist(username))
            return addError(obj,"Użytkownik o podanym mailu istnieje");
        if (matching("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",username)) {
                return obj;
        } else {
                return addError(obj,"Mail powinien być w formacie: przyklad@mail.pl");
        }
    }
    private ArrayList checkPassword(RegisterForm form) {
        String password = form.getPassword();
        String confirmPassword = form.getConfirmPassword();
        ArrayList obj = checkStreet(form);
        if (password == null) {
            if (confirmPassword == null)
                addError(obj, "Potwierdzenie jest puste");
            return addError(obj, "Hasło jest puste");
        }
        if (password.length() >= 6) {
            if (password.equals(confirmPassword)) {
                return obj;
            } else {
                return addError(obj, "Hasła nie zgadzają się");
            }
        } else {
            addError(obj, "Hasło jest za krótkie");
            return obj;
        }
    }
    private ArrayList checkStreet(RegisterForm form){
        String street = form.getAddress().getStreet();
        ArrayList obj = checkBuilding(form);
        if(matching("(^[A-Z ÀÁÂÃÄÅ ĄŻŹ ÒÓÔÕÖØ Ł Ć ĘŚ Ń ÈÉÊË Ç ÌÍÎÏ ÙÚÛÜ Ñ]{1})([a-zàáâãäåąźżòóÓłćęśńôõöøèéêëçìíîïùúûüÿñ]{1,}$)",street) && street.length()<=30)
            return obj;
        else
            return addError(obj,"Nazwa ulicy powinna zaczynać się z wielkiej litery oraz mieć mniej niż 30 znaków");
    }
    private ArrayList checkBuilding (RegisterForm form){
        String buildingNumber = form.getAddress().getBuildingNumber();
        ArrayList obj = checkLocale(form);
        try{
            Integer.valueOf(buildingNumber);
        }catch (NumberFormatException e){
            addError(obj, "Numer budynku powinien być cyfrą");
        }
        finally {
            return obj;
        }
    }
    private ArrayList checkLocale (RegisterForm form){
        String flatNumber = form.getAddress().getFlatNumber();
        ArrayList obj = checkZip(form);
        if(flatNumber != null){
        try{
            Integer.valueOf(flatNumber);
        }catch (NumberFormatException e){
            addError(obj, "Numer mieszkania powinien być cyfrą");
        }
        };
            return obj;

    }
    private ArrayList checkZip(RegisterForm form){
        ArrayList obj = checkCity(form);
        if(matching("^[0-9]{2}-?[0-9]{3}$",form.getAddress().getZipCode()))
            return obj;
        else
            return addError(obj,"Kod pocztowy powinien być w formacie: 22-222");
    }
    private ArrayList checkCity (RegisterForm form){
        String city = form.getAddress().getCity();
        ArrayList obj = checkPhone(form);
        if(matching("(^[A-Z ÀÁÂÃÄÅ ĄŻŹ ÒÓÔÕÖØ Ł Ć ĘŚ Ń ÈÉÊË Ç ÌÍÎÏ ÙÚÛÜ Ñ]{1})([a-zàáâãäåąźżòóÓłćęśńôõöøèéêë)çìíîïùúûüÿñ]{1,}$)",city) && city.length()<=30)
            return obj;
        else
            return addError(obj, "Nazwa maista powinna zaczynać się z wielkiej litery oraz mieć mniej niż 30 znaków");
    }
    private ArrayList checkPhone(RegisterForm form){
        ArrayList obj = new ArrayList();
        if(matching("^(\\(?\\+?[0-9]{1,3}\\)?)? ?[0-9 \\-]{7,20}$",form.getAddress().getPhone()))
            return obj;
        else
            return addError(obj, "Numer telefonu powinien być mieć ciąg 9 cyfr bez spacji");
    }
    private Boolean matching(String pattern, String regex){
        Pattern p = Pattern.compile(pattern);
        Matcher match = p.matcher(regex);
        return match.find();
    }
    private ArrayList addError (ArrayList lista, String nazwa){
        lista.add(nazwa);
        return lista;
    }

}