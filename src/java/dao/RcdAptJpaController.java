/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Residente;
import entidades.Apartamento;
import entidades.RcdApt;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Usuario
 */
public class RcdAptJpaController implements Serializable {

    public RcdAptJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RcdApt rcdApt) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Residente identificacion = rcdApt.getIdentificacion();
            if (identificacion != null) {
                identificacion = em.getReference(identificacion.getClass(), identificacion.getIdentificacion());
                rcdApt.setIdentificacion(identificacion);
            }
            Apartamento idApt = rcdApt.getIdApt();
            if (idApt != null) {
                idApt = em.getReference(idApt.getClass(), idApt.getIdApt());
                rcdApt.setIdApt(idApt);
            }
            em.persist(rcdApt);
            if (identificacion != null) {
                identificacion.getRcdAptCollection().add(rcdApt);
                identificacion = em.merge(identificacion);
            }
            if (idApt != null) {
                idApt.getRcdAptCollection().add(rcdApt);
                idApt = em.merge(idApt);
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

    public void edit(RcdApt rcdApt) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RcdApt persistentRcdApt = em.find(RcdApt.class, rcdApt.getIdRcdApt());
            Residente identificacionOld = persistentRcdApt.getIdentificacion();
            Residente identificacionNew = rcdApt.getIdentificacion();
            Apartamento idAptOld = persistentRcdApt.getIdApt();
            Apartamento idAptNew = rcdApt.getIdApt();
            if (identificacionNew != null) {
                identificacionNew = em.getReference(identificacionNew.getClass(), identificacionNew.getIdentificacion());
                rcdApt.setIdentificacion(identificacionNew);
            }
            if (idAptNew != null) {
                idAptNew = em.getReference(idAptNew.getClass(), idAptNew.getIdApt());
                rcdApt.setIdApt(idAptNew);
            }
            rcdApt = em.merge(rcdApt);
            if (identificacionOld != null && !identificacionOld.equals(identificacionNew)) {
                identificacionOld.getRcdAptCollection().remove(rcdApt);
                identificacionOld = em.merge(identificacionOld);
            }
            if (identificacionNew != null && !identificacionNew.equals(identificacionOld)) {
                identificacionNew.getRcdAptCollection().add(rcdApt);
                identificacionNew = em.merge(identificacionNew);
            }
            if (idAptOld != null && !idAptOld.equals(idAptNew)) {
                idAptOld.getRcdAptCollection().remove(rcdApt);
                idAptOld = em.merge(idAptOld);
            }
            if (idAptNew != null && !idAptNew.equals(idAptOld)) {
                idAptNew.getRcdAptCollection().add(rcdApt);
                idAptNew = em.merge(idAptNew);
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
                Integer id = rcdApt.getIdRcdApt();
                if (findRcdApt(id) == null) {
                    throw new NonexistentEntityException("The rcdApt with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            RcdApt rcdApt;
            try {
                rcdApt = em.getReference(RcdApt.class, id);
                rcdApt.getIdRcdApt();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rcdApt with id " + id + " no longer exists.", enfe);
            }
            Residente identificacion = rcdApt.getIdentificacion();
            if (identificacion != null) {
                identificacion.getRcdAptCollection().remove(rcdApt);
                identificacion = em.merge(identificacion);
            }
            Apartamento idApt = rcdApt.getIdApt();
            if (idApt != null) {
                idApt.getRcdAptCollection().remove(rcdApt);
                idApt = em.merge(idApt);
            }
            em.remove(rcdApt);
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

    public List<RcdApt> findRcdAptEntities() {
        return findRcdAptEntities(true, -1, -1);
    }

    public List<RcdApt> findRcdAptEntities(int maxResults, int firstResult) {
        return findRcdAptEntities(false, maxResults, firstResult);
    }

    private List<RcdApt> findRcdAptEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RcdApt.class));
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

    public RcdApt findRcdApt(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RcdApt.class, id);
        } finally {
            em.close();
        }
    }

    public int getRcdAptCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RcdApt> rt = cq.from(RcdApt.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
