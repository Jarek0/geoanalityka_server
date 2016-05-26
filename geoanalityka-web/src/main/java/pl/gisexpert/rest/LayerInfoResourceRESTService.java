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
package pl.gisexpert.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import pl.gisexpert.cms.data.LayerInfoRepository;
import pl.gisexpert.cms.model.LayerInfo;
import pl.gisexpert.cms.service.LayerInfoService;

@Path("/layerInfos")
@RequestScoped
public class LayerInfoResourceRESTService {
    @Inject
    private Logger log;

    @Inject
    private LayerInfoRepository repository;

    @Inject
    LayerInfoService registration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<LayerInfo> listAllLayerInfos() {
        return repository.findAllOrderedByName();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public LayerInfo lookupLayerInfoById(@PathParam("id") long id) {
        LayerInfo layerInfo = repository.findById(id);
        if (layerInfo == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return layerInfo;
    }

}
