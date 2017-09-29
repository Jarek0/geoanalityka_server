package pl.gisexpert.cms.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import pl.gisexpert.cms.model.LoginAttempt;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@ApplicationScoped
public class LoginAttemptRepository extends AbstractRepository<LoginAttempt> {

    @Inject
    @CMSEntityManager
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoginAttemptRepository() {
        super(LoginAttempt.class);
    }

}
