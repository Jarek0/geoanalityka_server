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

import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

import pl.gisexpert.cms.data.RoleRepository;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.PremiumPlanType;
import pl.gisexpert.cms.model.Role;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class PremiumPlanService {

	@Inject
	@CMSEntityManager
	private EntityManager em;

	@Inject
	AccountService accountService;

	@Inject
	RoleRepository roleRepository;

	@Transactional
	public void activatePlan(Account account, PremiumPlanType planType) {
		
		account = em.getReference(account.getClass(), account.getId());
		Set<Role> accountRoles = account.getRoles();
		
		Set<Role> newRoles = Sets.newHashSet(Iterables.filter(accountRoles, new Predicate<Role>() {
			@Override
			public boolean apply(Role input) {
				return !(input.getName().equals("PLAN_STANDARDOWY") || input.getName().equals("PLAN_ZAAWANSOWANY")
						|| input.getName().equals("PLAN_DEDYKOWANY") || input.getName().equals("PLAN_TESTOWY"));
			}
		}));

		Role newPlanTypeRole = roleRepository.findByName(planType.toString());
		newRoles.add(newPlanTypeRole);
		
		account.setRoles(newRoles);
		em.merge(account);
	}

	@Transactional
	public PremiumPlanType getActivePlan(Account account) {
		Set<Role> roles = account.getRoles();
		Set<Role> planRoles = Sets.newHashSet(Iterables.filter(roles, new Predicate<Role>() {
			@Override
			public boolean apply(Role input) {
				return (input.getName().equals("PLAN_STANDARDOWY") || input.getName().equals("PLAN_ZAAWANSOWANY")
						|| input.getName().equals("PLAN_DEDYKOWANY") || input.getName().equals("PLAN_TESTOWY"));
			}
		}));

		if (planRoles.size() > 0) {
			return PremiumPlanType.valueOf(planRoles.iterator().next().getName());
		}

		return null;
	}

}
