/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Conjunto;
import entidades.Apartamento;
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
public class TorreJpaController implements Serializable {

    public TorreJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Torre torre) throws RollbackFailureException, Exception {
        if (torre.getApartamentoCollection() == null) {
            torre.setApartamentoCollection(new ArrayList<Apartamento>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Conjunto idConjunto = torre.getIdConjunto();
            if (idConjunto != null) {
                idConjunto = em.getReference(idConjunto.getClass(), idConjunto.getId());
                torre.setIdConjunto(idConjunto);
            }
            Collection<Apartamento> attachedApartamentoCollection = new ArrayList<Apartamento>();
            for (Apartamento apartamentoCollectionApartamentoToAttach : torre.getApartamentoCollection()) {
                apartamentoCollectionApartamentoToAttach = em.getReference(apartamentoCollectionApartamentoToAttach.getClass(), apartamentoCollectionApartamentoToAttach.getIdApt());
                attachedApartamentoCollection.add(apartamentoCollectionApartamentoToAttach);
            }
            torre.setApartamentoCollection(attachedApartamentoCollection);
            em.persist(torre);
            if (idConjunto != null) {
                idConjunto.getTorreCollection().add(torre);
                idConjunto = em.merge(idConjunto);
            }
            for (Apartamento apartamentoCollectionApartamento : torre.getApartamentoCollection()) {
                Torre oldIdTorreOfApartamentoCollectionApartamento = apartamentoCollectionApartamento.getIdTorre();
                apartamentoCollectionApartamento.setIdTorre(torre);
                apartamentoCollectionApartamento = em.merge(apartamentoCollectionApartamento);
                if (oldIdTorreOfApartamentoCollectionApartamento != null) {
                    oldIdTorreOfApartamentoCollectionApartamento.getApartamentoCollection().remove(apartamentoCollectionApartamento);
                    oldIdTorreOfApartamentoCollectionApartamento = em.merge(oldIdTorreOfApartamentoCollectionApartamento);
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

    public void edit(Torre torre) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Torre persistentTorre = em.find(Torre.class, torre.getIdTorre());
            Conjunto idConjuntoOld = persistentTorre.getIdConjunto();
            Conjunto idConjuntoNew = torre.getIdConjunto();
            Collection<Apartamento> apartamentoCollectionOld = persistentTorre.getApartamentoCollection();
            Collection<Apartamento> apartamentoCollectionNew = torre.getApartamentoCollection();
            List<String> illegalOrphanMessages = null;
            for (Apartamento apartamentoCollectionOldApartamento : apartamentoCollectionOld) {
                if (!apartamentoCollectionNew.contains(apartamentoCollectionOldApartamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Apartamento " + apartamentoCollectionOldApartamento + " since its idTorre field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idConjuntoNew != null) {
                idConjuntoNew = em.getReference(idConjuntoNew.getClass(), idConjuntoNew.getId());
                torre.setIdConjunto(idConjuntoNew);
            }
            Collection<Apartamento> attachedApartamentoCollectionNew = new ArrayList<Apartamento>();
            for (Apartamento apartamentoCollectionNewApartamentoToAttach : apartamentoCollectionNew) {
                apartamentoCollectionNewApartamentoToAttach = em.getReference(apartamentoCollectionNewApartamentoToAttach.getClass(), apartamentoCollectionNewApartamentoToAttach.getIdApt());
                attachedApartamentoCollectionNew.add(apartamentoCollectionNewApartamentoToAttach);
            }
            apartamentoCollectionNew = attachedApartamentoCollectionNew;
            torre.setApartamentoCollection(apartamentoCollectionNew);
            torre = em.merge(torre);
            if (idConjuntoOld != null && !idConjuntoOld.equals(idConjuntoNew)) {
                idConjuntoOld.getTorreCollection().remove(torre);
                idConjuntoOld = em.merge(idConjuntoOld);
            }
            if (idConjuntoNew != null && !idConjuntoNew.equals(idConjuntoOld)) {
                idConjuntoNew.getTorreCollection().add(torre);
                idConjuntoNew = em.merge(idConjuntoNew);
            }
            for (Apartamento apartamentoCollectionNewApartamento : apartamentoCollectionNew) {
                if (!apartamentoCollectionOld.contains(apartamentoCollectionNewApartamento)) {
                    Torre oldIdTorreOfApartamentoCollectionNewApartamento = apartamentoCollectionNewApartamento.getIdTorre();
                    apartamentoCollectionNewApartamento.setIdTorre(torre);
                    apartamentoCollectionNewApartamento = em.merge(apartamentoCollectionNewApartamento);
                    if (oldIdTorreOfApartamentoCollectionNewApartamento != null && !oldIdTorreOfApartamentoCollectionNewApartamento.equals(torre)) {
                        oldIdTorreOfApartamentoCollectionNewApartamento.getApartamentoCollection().remove(apartamentoCollectionNewApartamento);
                        oldIdTorreOfApartamentoCollectionNewApartamento = em.merge(oldIdTorreOfApartamentoCollectionNewApartamento);
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
                Short id = torre.getIdTorre();
                if (findTorre(id) == null) {
                    throw new NonexistentEntityException("The torre with id " + id + " no longer exists.");
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
            Torre torre;
            try {
                torre = em.getReference(Torre.class, id);
                torre.getIdTorre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The torre with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Apartamento> apartamentoCollectionOrphanCheck = torre.getApartamentoCollection();
            for (Apartamento apartamentoCollectionOrphanCheckApartamento : apartamentoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Torre (" + torre + ") cannot be destroyed since the Apartamento " + apartamentoCollectionOrphanCheckApartamento + " in its apartamentoCollection field has a non-nullable idTorre field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Conjunto idConjunto = torre.getIdConjunto();
            if (idConjunto != null) {
                idConjunto.getTorreCollection().remove(torre);
                idConjunto = em.merge(idConjunto);
            }
            em.remove(torre);
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

    public List<Torre> findTorreEntities() {
        return findTorreEntities(true, -1, -1);
    }

    public List<Torre> findTorreEntities(int maxResults, int firstResult) {
        return findTorreEntities(false, maxResults, firstResult);
    }

    private List<Torre> findTorreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Torre.class));
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

    public Torre findTorre(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Torre.class, id);
        } finally {
            em.close();
        }
    }

    public int getTorreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Torre> rt = cq.from(Torre.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
