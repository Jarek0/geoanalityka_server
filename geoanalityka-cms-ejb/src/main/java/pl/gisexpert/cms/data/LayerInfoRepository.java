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
package pl.gisexpert.cms.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import pl.gisexpert.cms.model.LayerInfo;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

import java.util.List;

@ApplicationScoped
public class LayerInfoRepository {

    @Inject
    @CMSEntityManager
    private EntityManager em;

    public LayerInfo findById(Long id) {
        return em.find(LayerInfo.class, id);
    }

    public LayerInfo findByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<LayerInfo> criteria = cb.createQuery(LayerInfo.class);
        Root<LayerInfo> layerInfo = criteria.from(LayerInfo.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(layerInfo).where(cb.equal(layerInfo.get(LayerInfo_.name), email));
        criteria.select(layerInfo).where(cb.equal(layerInfo.get("email"), email));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<LayerInfo> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<LayerInfo> criteria = cb.createQuery(LayerInfo.class);
        Root<LayerInfo> layerInfo = criteria.from(LayerInfo.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(layerInfo).orderBy(cb.asc(layerInfo.get(LayerInfo_.name)));
        criteria.select(layerInfo).orderBy(cb.asc(layerInfo.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
