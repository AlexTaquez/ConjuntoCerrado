/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Conjunto;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Torre;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Usuario
 */
public class ConjuntoJpaController implements Serializable {

    public ConjuntoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Conjunto conjunto) throws RollbackFailureException, Exception {
        if (conjunto.getTorreCollection() == null) {
            conjunto.setTorreCollection(new ArrayList<Torre>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Torre> attachedTorreCollection = new ArrayList<Torre>();
            for (Torre torreCollectionTorreToAttach : conjunto.getTorreCollection()) {
                torreCollectionTorreToAttach = em.getReference(torreCollectionTorreToAttach.getClass(), torreCollectionTorreToAttach.getIdTorre());
                attachedTorreCollection.add(torreCollectionTorreToAttach);
            }
            conjunto.setTorreCollection(attachedTorreCollection);
            em.persist(conjunto);
            for (Torre torreCollectionTorre : conjunto.getTorreCollection()) {
                Conjunto oldIdConjuntoOfTorreCollectionTorre = torreCollectionTorre.getIdConjunto();
                torreCollectionTorre.setIdConjunto(conjunto);
                torreCollectionTorre = em.merge(torreCollectionTorre);
                if (oldIdConjuntoOfTorreCollectionTorre != null) {
                    oldIdConjuntoOfTorreCollectionTorre.getTorreCollection().remove(torreCollectionTorre);
                    oldIdConjuntoOfTorreCollectionTorre = em.merge(oldIdConjuntoOfTorreCollectionTorre);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Conjunto conjunto) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Conjunto persistentConjunto = em.find(Conjunto.class, conjunto.getId());
            Collection<Torre> torreCollectionOld = persistentConjunto.getTorreCollection();
            Collection<Torre> torreCollectionNew = conjunto.getTorreCollection();
            List<String> illegalOrphanMessages = null;
            for (Torre torreCollectionOldTorre : torreCollectionOld) {
                if (!torreCollectionNew.contains(torreCollectionOldTorre)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Torre " + torreCollectionOldTorre + " since its idConjunto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Torre> attachedTorreCollectionNew = new ArrayList<Torre>();
            for (Torre torreCollectionNewTorreToAttach : torreCollectionNew) {
                torreCollectionNewTorreToAttach = em.getReference(torreCollectionNewTorreToAttach.getClass(), torreCollectionNewTorreToAttach.getIdTorre());
                attachedTorreCollectionNew.add(torreCollectionNewTorreToAttach);
            }
            torreCollectionNew = attachedTorreCollectionNew;
            conjunto.setTorreCollection(torreCollectionNew);
            conjunto = em.merge(conjunto);
            for (Torre torreCollectionNewTorre : torreCollectionNew) {
                if (!torreCollectionOld.contains(torreCollectionNewTorre)) {
                    Conjunto oldIdConjuntoOfTorreCollectionNewTorre = torreCollectionNewTorre.getIdConjunto();
                    torreCollectionNewTorre.setIdConjunto(conjunto);
                    torreCollectionNewTorre = em.merge(torreCollectionNewTorre);
                    if (oldIdConjuntoOfTorreCollectionNewTorre != null && !oldIdConjuntoOfTorreCollectionNewTorre.equals(conjunto)) {
                        oldIdConjuntoOfTorreCollectionNewTorre.getTorreCollection().remove(torreCollectionNewTorre);
                        oldIdConjuntoOfTorreCollectionNewTorre = em.merge(oldIdConjuntoOfTorreCollectionNewTorre);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = conjunto.getId();
                if (findConjunto(id) == null) {
                    throw new NonexistentEntityException("The conjunto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Conjunto conjunto;
            try {
                conjunto = em.getReference(Conjunto.class, id);
                conjunto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The conjunto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Torre> torreCollectionOrphanCheck = conjunto.getTorreCollection();
            for (Torre torreCollectionOrphanCheckTorre : torreCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Conjunto (" + conjunto + ") cannot be destroyed since the Torre " + torreCollectionOrphanCheckTorre + " in its torreCollection field has a non-nullable idConjunto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(conjunto);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Conjunto> findConjuntoEntities() {
        return findConjuntoEntities(true, -1, -1);
    }

    public List<Conjunto> findConjuntoEntities(int maxResults, int firstResult) {
        return findConjuntoEntities(false, maxResults, firstResult);
    }

    private List<Conjunto> findConjuntoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Conjunto.class));
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

    public Conjunto findConjunto(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Conjunto.class, id);
        } finally {
            em.close();
        }
    }

    public int getConjuntoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Conjunto> rt = cq.from(Conjunto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
