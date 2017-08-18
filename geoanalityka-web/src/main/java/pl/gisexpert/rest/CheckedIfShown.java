package pl.gisexpert.rest;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class CheckedIfShown {
    private boolean shown = false;
    public void setShow (){
        shown = !shown;
    }
    public boolean getShow(){
        return shown;
    }
}
