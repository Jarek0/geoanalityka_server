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
package pl.gisexpert.cms.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.analysis.demographic.DemographicAnalysis;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class AnalysisService {

	@Inject
	@CMSEntityManager
	private EntityManager em;

	public DemographicAnalysis addDemographicAnalysis(Account account, DemographicAnalysis analysis) {
		
		if (analysis.getCreator() == null) {
			account = em.find(Account.class, account.getId());
			analysis.setCreator(account);
		}
		return em.merge(analysis);
	}

}
