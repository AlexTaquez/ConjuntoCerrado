/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Apartamento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Torre;
import entidades.Factura;
import java.util.ArrayList;
import java.util.Collection;
import entidades.RcdApt;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Usuario
 */
public class ApartamentoJpaController implements Serializable {

    public ApartamentoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Apartamento apartamento) throws RollbackFailureException, Exception {
        if (apartamento.getFacturaCollection() == null) {
            apartamento.setFacturaCollection(new ArrayList<Factura>());
        }
        if (apartamento.getRcdAptCollection() == null) {
            apartamento.setRcdAptCollection(new ArrayList<RcdApt>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Torre idTorre = apartamento.getIdTorre();
            if (idTorre != null) {
                idTorre = em.getReference(idTorre.getClass(), idTorre.getIdTorre());
                apartamento.setIdTorre(idTorre);
            }
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : apartamento.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdFactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            apartamento.setFacturaCollection(attachedFacturaCollection);
            Collection<RcdApt> attachedRcdAptCollection = new ArrayList<RcdApt>();
            for (RcdApt rcdAptCollectionRcdAptToAttach : apartamento.getRcdAptCollection()) {
                rcdAptCollectionRcdAptToAttach = em.getReference(rcdAptCollectionRcdAptToAttach.getClass(), rcdAptCollectionRcdAptToAttach.getIdRcdApt());
                attachedRcdAptCollection.add(rcdAptCollectionRcdAptToAttach);
            }
            apartamento.setRcdAptCollection(attachedRcdAptCollection);
            em.persist(apartamento);
            if (idTorre != null) {
                idTorre.getApartamentoCollection().add(apartamento);
                idTorre = em.merge(idTorre);
            }
            for (Factura facturaCollectionFactura : apartamento.getFacturaCollection()) {
                Apartamento oldIdAptOfFacturaCollectionFactura = facturaCollectionFactura.getIdApt();
                facturaCollectionFactura.setIdApt(apartamento);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldIdAptOfFacturaCollectionFactura != null) {
                    oldIdAptOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldIdAptOfFacturaCollectionFactura = em.merge(oldIdAptOfFacturaCollectionFactura);
                }
            }
            for (RcdApt rcdAptCollectionRcdApt : apartamento.getRcdAptCollection()) {
                Apartamento oldIdAptOfRcdAptCollectionRcdApt = rcdAptCollectionRcdApt.getIdApt();
                rcdAptCollectionRcdApt.setIdApt(apartamento);
                rcdAptCollectionRcdApt = em.merge(rcdAptCollectionRcdApt);
                if (oldIdAptOfRcdAptCollectionRcdApt != null) {
                    oldIdAptOfRcdAptCollectionRcdApt.getRcdAptCollection().remove(rcdAptCollectionRcdApt);
                    oldIdAptOfRcdAptCollectionRcdApt = em.merge(oldIdAptOfRcdAptCollectionRcdApt);
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

    public void edit(Apartamento apartamento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Apartamento persistentApartamento = em.find(Apartamento.class, apartamento.getIdApt());
            Torre idTorreOld = persistentApartamento.getIdTorre();
            Torre idTorreNew = apartamento.getIdTorre();
            Collection<Factura> facturaCollectionOld = persistentApartamento.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = apartamento.getFacturaCollection();
            Collection<RcdApt> rcdAptCollectionOld = persistentApartamento.getRcdAptCollection();
            Collection<RcdApt> rcdAptCollectionNew = apartamento.getRcdAptCollection();
            List<String> illegalOrphanMessages = null;
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollectionOldFactura + " since its idApt field is not nullable.");
                }
            }
            for (RcdApt rcdAptCollectionOldRcdApt : rcdAptCollectionOld) {
                if (!rcdAptCollectionNew.contains(rcdAptCollectionOldRcdApt)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RcdApt " + rcdAptCollectionOldRcdApt + " since its idApt field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTorreNew != null) {
                idTorreNew = em.getReference(idTorreNew.getClass(), idTorreNew.getIdTorre());
                apartamento.setIdTorre(idTorreNew);
            }
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdFactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            apartamento.setFacturaCollection(facturaCollectionNew);
            Collection<RcdApt> attachedRcdAptCollectionNew = new ArrayList<RcdApt>();
            for (RcdApt rcdAptCollectionNewRcdAptToAttach : rcdAptCollectionNew) {
                rcdAptCollectionNewRcdAptToAttach = em.getReference(rcdAptCollectionNewRcdAptToAttach.getClass(), rcdAptCollectionNewRcdAptToAttach.getIdRcdApt());
                attachedRcdAptCollectionNew.add(rcdAptCollectionNewRcdAptToAttach);
            }
            rcdAptCollectionNew = attachedRcdAptCollectionNew;
            apartamento.setRcdAptCollection(rcdAptCollectionNew);
            apartamento = em.merge(apartamento);
            if (idTorreOld != null && !idTorreOld.equals(idTorreNew)) {
                idTorreOld.getApartamentoCollection().remove(apartamento);
                idTorreOld = em.merge(idTorreOld);
            }
            if (idTorreNew != null && !idTorreNew.equals(idTorreOld)) {
                idTorreNew.getApartamentoCollection().add(apartamento);
                idTorreNew = em.merge(idTorreNew);
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    Apartamento oldIdAptOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getIdApt();
                    facturaCollectionNewFactura.setIdApt(apartamento);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldIdAptOfFacturaCollectionNewFactura != null && !oldIdAptOfFacturaCollectionNewFactura.equals(apartamento)) {
                        oldIdAptOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldIdAptOfFacturaCollectionNewFactura = em.merge(oldIdAptOfFacturaCollectionNewFactura);
                    }
                }
            }
            for (RcdApt rcdAptCollectionNewRcdApt : rcdAptCollectionNew) {
                if (!rcdAptCollectionOld.contains(rcdAptCollectionNewRcdApt)) {
                    Apartamento oldIdAptOfRcdAptCollectionNewRcdApt = rcdAptCollectionNewRcdApt.getIdApt();
                    rcdAptCollectionNewRcdApt.setIdApt(apartamento);
                    rcdAptCollectionNewRcdApt = em.merge(rcdAptCollectionNewRcdApt);
                    if (oldIdAptOfRcdAptCollectionNewRcdApt != null && !oldIdAptOfRcdAptCollectionNewRcdApt.equals(apartamento)) {
                        oldIdAptOfRcdAptCollectionNewRcdApt.getRcdAptCollection().remove(rcdAptCollectionNewRcdApt);
                        oldIdAptOfRcdAptCollectionNewRcdApt = em.merge(oldIdAptOfRcdAptCollectionNewRcdApt);
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
                Short id = apartamento.getIdApt();
                if (findApartamento(id) == null) {
                    throw new NonexistentEntityException("The apartamento with id " + id + " no longer exists.");
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
            Apartamento apartamento;
            try {
                apartamento = em.getReference(Apartamento.class, id);
                apartamento.getIdApt();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The apartamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Factura> facturaCollectionOrphanCheck = apartamento.getFacturaCollection();
            for (Factura facturaCollectionOrphanCheckFactura : facturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Apartamento (" + apartamento + ") cannot be destroyed since the Factura " + facturaCollectionOrphanCheckFactura + " in its facturaCollection field has a non-nullable idApt field.");
            }
            Collection<RcdApt> rcdAptCollectionOrphanCheck = apartamento.getRcdAptCollection();
            for (RcdApt rcdAptCollectionOrphanCheckRcdApt : rcdAptCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Apartamento (" + apartamento + ") cannot be destroyed since the RcdApt " + rcdAptCollectionOrphanCheckRcdApt + " in its rcdAptCollection field has a non-nullable idApt field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Torre idTorre = apartamento.getIdTorre();
            if (idTorre != null) {
                idTorre.getApartamentoCollection().remove(apartamento);
                idTorre = em.merge(idTorre);
            }
            em.remove(apartamento);
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

    public List<Apartamento> findApartamentoEntities() {
        return findApartamentoEntities(true, -1, -1);
    }

    public List<Apartamento> findApartamentoEntities(int maxResults, int firstResult) {
        return findApartamentoEntities(false, maxResults, firstResult);
    }

    private List<Apartamento> findApartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Apartamento.class));
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

    public Apartamento findApartamento(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Apartamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getApartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Apartamento> rt = cq.from(Apartamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
