package com.jhw.utils.jpa;

import com.clean.core.app.repo.CRUDRepository;
import com.clean.core.exceptions.ValidationException;
import com.clean.core.utils.validation.Validable;
import com.clean.core.utils.validation.ValidationMessage;
import com.clean.core.utils.validation.ValidationResult;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Jesus Hernandez Barrios (jhernandezb96@gmail.com)
 */
public class JPAControllerGeneral<T> implements CRUDRepository<T>, Validable {

    private EntityManagerFactory emf = null;
    private final Class<T> classType;

    public JPAControllerGeneral(EntityManagerFactory emf, Class<T> c) {
        this.emf = emf;
        this.classType = c;
        validate();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public T create(T object) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(object);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return object;
    }

    @Override
    public T edit(T object) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            //check the id isn't null
            Object id = JPAControllerGeneralUtils.getId(object);
            if (id == null) {
                throw new NonExistingEntityException("To edit " + object + " the id can't be null");
            }
            //check if still exist
            T persistedObject = findBy(id);
            if (persistedObject == null) {
                throw new NonExistingEntityException(object + " no longer exists.");
            }

            //edit it
            em.merge(object);
            em.getTransaction().commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return object;
    }

    @Override
    public T findBy(Object id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(classType, id);//return null if don't exits
        } finally {
            em.close();
        }
    }

    @Override
    public T destroy(T object) throws Exception {
        return destroyById(JPAControllerGeneralUtils.getId(object));
    }

    @Override
    public T destroyById(Object id) throws Exception {
        EntityManager em = null;
        T persistedObject;

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            persistedObject = em.find(classType, id);
            if (persistedObject == null) {
                throw new NonExistingEntityException(id + " no longer exists.");
            }

            em.remove(persistedObject);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return persistedObject;
    }

    @Override
    public List<T> findAll() {
        return findAll(true, -1, -1);
    }

    public List<T> findAll(int maxResults, int firstResult) {
        return findAll(false, maxResults, firstResult);
    }

    private List<T> findAll(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(classType));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int count() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<T> rt = cq.from(classType);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public ValidationResult validate() throws ValidationException {
        ValidationResult val = new ValidationResult();
        if (!JPAControllerGeneralUtils.isEntity(classType)) {
            val.add(ValidationMessage.from(this, classType + "isn't an javax.persistence.Entity"));
        }
        return val.throwException();
    }
}
