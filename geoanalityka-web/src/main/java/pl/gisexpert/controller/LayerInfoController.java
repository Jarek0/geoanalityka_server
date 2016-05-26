/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.gisexpert.controller;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import pl.gisexpert.cms.model.LayerInfo;
import pl.gisexpert.cms.model.LayerType;
import pl.gisexpert.cms.service.LayerInfoService;

@RequestScoped
@Named
public class LayerInfoController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private LayerInfoService layerInfoRegistration;
    

    private LayerInfo newLayerInfo;

    @Produces
    @Named
    public LayerInfo getNewLayerInfo() {
        return newLayerInfo;
    }

    public void add() throws Exception {
        try {
            layerInfoRegistration.add(newLayerInfo);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Operation successful!", "New layer info has been added."));
            initNewLayerInfo();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Adding layer info failed");
            facesContext.addMessage(null, m);
        }
    }

    @PostConstruct
    public void initNewLayerInfo() {
        newLayerInfo = new LayerInfo();
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Adding layer info failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }
    
    public LayerType[] getLayerTypes(){
    	return LayerType.values();
    }
}
