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

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import pl.gisexpert.cms.model.AccessToken;
import pl.gisexpert.cms.model.Account;
import pl.gisexpert.cms.model.LoginAttempt;
import pl.gisexpert.cms.qualifier.CMSEntityManager;


// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class LoginAttemptService{

    @Inject
    @CMSEntityManager
    private EntityManager em;
    
    public List<LoginAttempt> findRecentLoginAttempts(Integer minutesLimit, Account account, Integer limit) {
    	
    	String queryString = "SELECT la FROM LoginAttempt la WHERE la.date > :date AND la.account = :account";
    	TypedQuery<LoginAttempt> query = em.createQuery(queryString, LoginAttempt.class);
    	
    	Date date = new Date(System.currentTimeMillis() - 60 * 1000 * minutesLimit);
    	
    	query.setParameter("date", date);
    	query.setParameter("account", account);
    	query.setMaxResults(limit);
 
    	List<LoginAttempt> loginAttempts = query.getResultList();
   
    	return loginAttempts;
    	
    };
    
    public List<LoginAttempt> findRecentLoginAttempts(Account account) {
    	return findRecentLoginAttempts(15, account, 5);
    }
}
