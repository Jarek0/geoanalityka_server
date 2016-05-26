package pl.gisexpert.cms.data;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import pl.gisexpert.cms.model.Address;
import pl.gisexpert.cms.qualifier.CMSEntityManager;

@ApplicationScoped
public class AddressRepository extends AbstractRepository<Address> {
	
    @Inject
    @CMSEntityManager
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    public AddressRepository() {
        super(Address.class);
    }
    
}
