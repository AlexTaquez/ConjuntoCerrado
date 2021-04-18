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
import entidades.Factura;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Usuario
 */
public class FacturaJpaController implements Serializable {

    public FacturaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Factura factura) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Residente identificacion = factura.getIdentificacion();
            if (identificacion != null) {
                identificacion = em.getReference(identificacion.getClass(), identificacion.getIdentificacion());
                factura.setIdentificacion(identificacion);
            }
            Apartamento idApt = factura.getIdApt();
            if (idApt != null) {
                idApt = em.getReference(idApt.getClass(), idApt.getIdApt());
                factura.setIdApt(idApt);
            }
            em.persist(factura);
            if (identificacion != null) {
                identificacion.getFacturaCollection().add(factura);
                identificacion = em.merge(identificacion);
            }
            if (idApt != null) {
                idApt.getFacturaCollection().add(factura);
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

    public void edit(Factura factura) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Factura persistentFactura = em.find(Factura.class, factura.getIdFactura());
            Residente identificacionOld = persistentFactura.getIdentificacion();
            Residente identificacionNew = factura.getIdentificacion();
            Apartamento idAptOld = persistentFactura.getIdApt();
            Apartamento idAptNew = factura.getIdApt();
            if (identificacionNew != null) {
                identificacionNew = em.getReference(identificacionNew.getClass(), identificacionNew.getIdentificacion());
                factura.setIdentificacion(identificacionNew);
            }
            if (idAptNew != null) {
                idAptNew = em.getReference(idAptNew.getClass(), idAptNew.getIdApt());
                factura.setIdApt(idAptNew);
            }
            factura = em.merge(factura);
            if (identificacionOld != null && !identificacionOld.equals(identificacionNew)) {
                identificacionOld.getFacturaCollection().remove(factura);
                identificacionOld = em.merge(identificacionOld);
            }
            if (identificacionNew != null && !identificacionNew.equals(identificacionOld)) {
                identificacionNew.getFacturaCollection().add(factura);
                identificacionNew = em.merge(identificacionNew);
            }
            if (idAptOld != null && !idAptOld.equals(idAptNew)) {
                idAptOld.getFacturaCollection().remove(factura);
                idAptOld = em.merge(idAptOld);
            }
            if (idAptNew != null && !idAptNew.equals(idAptOld)) {
                idAptNew.getFacturaCollection().add(factura);
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
                Integer id = factura.getIdFactura();
                if (findFactura(id) == null) {
                    throw new NonexistentEntityException("The factura with id " + id + " no longer exists.");
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
            Factura factura;
            try {
                factura = em.getReference(Factura.class, id);
                factura.getIdFactura();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The factura with id " + id + " no longer exists.", enfe);
            }
            Residente identificacion = factura.getIdentificacion();
            if (identificacion != null) {
                identificacion.getFacturaCollection().remove(factura);
                identificacion = em.merge(identificacion);
            }
            Apartamento idApt = factura.getIdApt();
            if (idApt != null) {
                idApt.getFacturaCollection().remove(factura);
                idApt = em.merge(idApt);
            }
            em.remove(factura);
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

    public List<Factura> findFacturaEntities() {
        return findFacturaEntities(true, -1, -1);
    }

    public List<Factura> findFacturaEntities(int maxResults, int firstResult) {
        return findFacturaEntities(false, maxResults, firstResult);
    }

    private List<Factura> findFacturaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Factura.class));
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

    public Factura findFactura(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Factura.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Factura> rt = cq.from(Factura.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
