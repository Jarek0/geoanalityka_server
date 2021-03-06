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

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.Role;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@Stateless
public class AccountService {

    @Inject
    @CMSEntityManager
    private EntityManager em;
    
    @Inject
    private Logger log;
    
    @Transactional
    public Set<Role> getRoles(Account account) {
    	
    	TypedQuery<Role> query = em.createNamedQuery("Account.getRoles", Role.class);
    	query.setParameter("username", account.getUsername());
    	
    	Set<Role> accountRoles = new HashSet<>(query.getResultList());
    	return accountRoles;
    }
   
    public void setRoles(Account account, Set<Role> roles) {
    	
    	account = em.getReference(account.getClass(), account.getId());
    	account.setRoles(roles);
    	em.merge(account);
    }
    
    public boolean hasRole(Account account, Role role) {
    	return hasRole(account, role.getName());
    }
    
    public boolean hasRole(Account account, String roleName) {
    	TypedQuery<Integer> query = em.createNamedQuery("Account.hasRole", Integer.class);
    	query.setParameter("username", account.getUsername());
    	query.setParameter("role", roleName);
    	
    	Integer result = query.getSingleResult();
    	
    	return result > 0;
    }
    
}
