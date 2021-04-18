/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Residente;
import entidades.Evento;
import java.util.ArrayList;
import java.util.Collection;
import entidades.Factura;
import entidades.Comentario;
import entidades.RcdApt;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Usuario
 */
public class ResidenteJpaController implements Serializable {

    public ResidenteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Residente residente) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (residente.getEventoCollection() == null) {
            residente.setEventoCollection(new ArrayList<Evento>());
        }
        if (residente.getFacturaCollection() == null) {
            residente.setFacturaCollection(new ArrayList<Factura>());
        }
        if (residente.getComentarioCollection() == null) {
            residente.setComentarioCollection(new ArrayList<Comentario>());
        }
        if (residente.getRcdAptCollection() == null) {
            residente.setRcdAptCollection(new ArrayList<RcdApt>());
        }
        if (residente.getResidenteCollection() == null) {
            residente.setResidenteCollection(new ArrayList<Residente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Residente titular = residente.getTitular();
            if (titular != null) {
                titular = em.getReference(titular.getClass(), titular.getIdentificacion());
                residente.setTitular(titular);
            }
            Collection<Evento> attachedEventoCollection = new ArrayList<Evento>();
            for (Evento eventoCollectionEventoToAttach : residente.getEventoCollection()) {
                eventoCollectionEventoToAttach = em.getReference(eventoCollectionEventoToAttach.getClass(), eventoCollectionEventoToAttach.getIdEvento());
                attachedEventoCollection.add(eventoCollectionEventoToAttach);
            }
            residente.setEventoCollection(attachedEventoCollection);
            Collection<Factura> attachedFacturaCollection = new ArrayList<Factura>();
            for (Factura facturaCollectionFacturaToAttach : residente.getFacturaCollection()) {
                facturaCollectionFacturaToAttach = em.getReference(facturaCollectionFacturaToAttach.getClass(), facturaCollectionFacturaToAttach.getIdFactura());
                attachedFacturaCollection.add(facturaCollectionFacturaToAttach);
            }
            residente.setFacturaCollection(attachedFacturaCollection);
            Collection<Comentario> attachedComentarioCollection = new ArrayList<Comentario>();
            for (Comentario comentarioCollectionComentarioToAttach : residente.getComentarioCollection()) {
                comentarioCollectionComentarioToAttach = em.getReference(comentarioCollectionComentarioToAttach.getClass(), comentarioCollectionComentarioToAttach.getIdComentario());
                attachedComentarioCollection.add(comentarioCollectionComentarioToAttach);
            }
            residente.setComentarioCollection(attachedComentarioCollection);
            Collection<RcdApt> attachedRcdAptCollection = new ArrayList<RcdApt>();
            for (RcdApt rcdAptCollectionRcdAptToAttach : residente.getRcdAptCollection()) {
                rcdAptCollectionRcdAptToAttach = em.getReference(rcdAptCollectionRcdAptToAttach.getClass(), rcdAptCollectionRcdAptToAttach.getIdRcdApt());
                attachedRcdAptCollection.add(rcdAptCollectionRcdAptToAttach);
            }
            residente.setRcdAptCollection(attachedRcdAptCollection);
            Collection<Residente> attachedResidenteCollection = new ArrayList<Residente>();
            for (Residente residenteCollectionResidenteToAttach : residente.getResidenteCollection()) {
                residenteCollectionResidenteToAttach = em.getReference(residenteCollectionResidenteToAttach.getClass(), residenteCollectionResidenteToAttach.getIdentificacion());
                attachedResidenteCollection.add(residenteCollectionResidenteToAttach);
            }
            residente.setResidenteCollection(attachedResidenteCollection);
            em.persist(residente);
            if (titular != null) {
                titular.getResidenteCollection().add(residente);
                titular = em.merge(titular);
            }
            for (Evento eventoCollectionEvento : residente.getEventoCollection()) {
                Residente oldIdentificacionOfEventoCollectionEvento = eventoCollectionEvento.getIdentificacion();
                eventoCollectionEvento.setIdentificacion(residente);
                eventoCollectionEvento = em.merge(eventoCollectionEvento);
                if (oldIdentificacionOfEventoCollectionEvento != null) {
                    oldIdentificacionOfEventoCollectionEvento.getEventoCollection().remove(eventoCollectionEvento);
                    oldIdentificacionOfEventoCollectionEvento = em.merge(oldIdentificacionOfEventoCollectionEvento);
                }
            }
            for (Factura facturaCollectionFactura : residente.getFacturaCollection()) {
                Residente oldIdentificacionOfFacturaCollectionFactura = facturaCollectionFactura.getIdentificacion();
                facturaCollectionFactura.setIdentificacion(residente);
                facturaCollectionFactura = em.merge(facturaCollectionFactura);
                if (oldIdentificacionOfFacturaCollectionFactura != null) {
                    oldIdentificacionOfFacturaCollectionFactura.getFacturaCollection().remove(facturaCollectionFactura);
                    oldIdentificacionOfFacturaCollectionFactura = em.merge(oldIdentificacionOfFacturaCollectionFactura);
                }
            }
            for (Comentario comentarioCollectionComentario : residente.getComentarioCollection()) {
                Residente oldIdentificacionOfComentarioCollectionComentario = comentarioCollectionComentario.getIdentificacion();
                comentarioCollectionComentario.setIdentificacion(residente);
                comentarioCollectionComentario = em.merge(comentarioCollectionComentario);
                if (oldIdentificacionOfComentarioCollectionComentario != null) {
                    oldIdentificacionOfComentarioCollectionComentario.getComentarioCollection().remove(comentarioCollectionComentario);
                    oldIdentificacionOfComentarioCollectionComentario = em.merge(oldIdentificacionOfComentarioCollectionComentario);
                }
            }
            for (RcdApt rcdAptCollectionRcdApt : residente.getRcdAptCollection()) {
                Residente oldIdentificacionOfRcdAptCollectionRcdApt = rcdAptCollectionRcdApt.getIdentificacion();
                rcdAptCollectionRcdApt.setIdentificacion(residente);
                rcdAptCollectionRcdApt = em.merge(rcdAptCollectionRcdApt);
                if (oldIdentificacionOfRcdAptCollectionRcdApt != null) {
                    oldIdentificacionOfRcdAptCollectionRcdApt.getRcdAptCollection().remove(rcdAptCollectionRcdApt);
                    oldIdentificacionOfRcdAptCollectionRcdApt = em.merge(oldIdentificacionOfRcdAptCollectionRcdApt);
                }
            }
            for (Residente residenteCollectionResidente : residente.getResidenteCollection()) {
                Residente oldTitularOfResidenteCollectionResidente = residenteCollectionResidente.getTitular();
                residenteCollectionResidente.setTitular(residente);
                residenteCollectionResidente = em.merge(residenteCollectionResidente);
                if (oldTitularOfResidenteCollectionResidente != null) {
                    oldTitularOfResidenteCollectionResidente.getResidenteCollection().remove(residenteCollectionResidente);
                    oldTitularOfResidenteCollectionResidente = em.merge(oldTitularOfResidenteCollectionResidente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findResidente(residente.getIdentificacion()) != null) {
                throw new PreexistingEntityException("Residente " + residente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Residente residente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Residente persistentResidente = em.find(Residente.class, residente.getIdentificacion());
            Residente titularOld = persistentResidente.getTitular();
            Residente titularNew = residente.getTitular();
            Collection<Evento> eventoCollectionOld = persistentResidente.getEventoCollection();
            Collection<Evento> eventoCollectionNew = residente.getEventoCollection();
            Collection<Factura> facturaCollectionOld = persistentResidente.getFacturaCollection();
            Collection<Factura> facturaCollectionNew = residente.getFacturaCollection();
            Collection<Comentario> comentarioCollectionOld = persistentResidente.getComentarioCollection();
            Collection<Comentario> comentarioCollectionNew = residente.getComentarioCollection();
            Collection<RcdApt> rcdAptCollectionOld = persistentResidente.getRcdAptCollection();
            Collection<RcdApt> rcdAptCollectionNew = residente.getRcdAptCollection();
            Collection<Residente> residenteCollectionOld = persistentResidente.getResidenteCollection();
            Collection<Residente> residenteCollectionNew = residente.getResidenteCollection();
            List<String> illegalOrphanMessages = null;
            for (Evento eventoCollectionOldEvento : eventoCollectionOld) {
                if (!eventoCollectionNew.contains(eventoCollectionOldEvento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Evento " + eventoCollectionOldEvento + " since its identificacion field is not nullable.");
                }
            }
            for (Factura facturaCollectionOldFactura : facturaCollectionOld) {
                if (!facturaCollectionNew.contains(facturaCollectionOldFactura)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Factura " + facturaCollectionOldFactura + " since its identificacion field is not nullable.");
                }
            }
            for (Comentario comentarioCollectionOldComentario : comentarioCollectionOld) {
                if (!comentarioCollectionNew.contains(comentarioCollectionOldComentario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentario " + comentarioCollectionOldComentario + " since its identificacion field is not nullable.");
                }
            }
            for (RcdApt rcdAptCollectionOldRcdApt : rcdAptCollectionOld) {
                if (!rcdAptCollectionNew.contains(rcdAptCollectionOldRcdApt)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RcdApt " + rcdAptCollectionOldRcdApt + " since its identificacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (titularNew != null) {
                titularNew = em.getReference(titularNew.getClass(), titularNew.getIdentificacion());
                residente.setTitular(titularNew);
            }
            Collection<Evento> attachedEventoCollectionNew = new ArrayList<Evento>();
            for (Evento eventoCollectionNewEventoToAttach : eventoCollectionNew) {
                eventoCollectionNewEventoToAttach = em.getReference(eventoCollectionNewEventoToAttach.getClass(), eventoCollectionNewEventoToAttach.getIdEvento());
                attachedEventoCollectionNew.add(eventoCollectionNewEventoToAttach);
            }
            eventoCollectionNew = attachedEventoCollectionNew;
            residente.setEventoCollection(eventoCollectionNew);
            Collection<Factura> attachedFacturaCollectionNew = new ArrayList<Factura>();
            for (Factura facturaCollectionNewFacturaToAttach : facturaCollectionNew) {
                facturaCollectionNewFacturaToAttach = em.getReference(facturaCollectionNewFacturaToAttach.getClass(), facturaCollectionNewFacturaToAttach.getIdFactura());
                attachedFacturaCollectionNew.add(facturaCollectionNewFacturaToAttach);
            }
            facturaCollectionNew = attachedFacturaCollectionNew;
            residente.setFacturaCollection(facturaCollectionNew);
            Collection<Comentario> attachedComentarioCollectionNew = new ArrayList<Comentario>();
            for (Comentario comentarioCollectionNewComentarioToAttach : comentarioCollectionNew) {
                comentarioCollectionNewComentarioToAttach = em.getReference(comentarioCollectionNewComentarioToAttach.getClass(), comentarioCollectionNewComentarioToAttach.getIdComentario());
                attachedComentarioCollectionNew.add(comentarioCollectionNewComentarioToAttach);
            }
            comentarioCollectionNew = attachedComentarioCollectionNew;
            residente.setComentarioCollection(comentarioCollectionNew);
            Collection<RcdApt> attachedRcdAptCollectionNew = new ArrayList<RcdApt>();
            for (RcdApt rcdAptCollectionNewRcdAptToAttach : rcdAptCollectionNew) {
                rcdAptCollectionNewRcdAptToAttach = em.getReference(rcdAptCollectionNewRcdAptToAttach.getClass(), rcdAptCollectionNewRcdAptToAttach.getIdRcdApt());
                attachedRcdAptCollectionNew.add(rcdAptCollectionNewRcdAptToAttach);
            }
            rcdAptCollectionNew = attachedRcdAptCollectionNew;
            residente.setRcdAptCollection(rcdAptCollectionNew);
            Collection<Residente> attachedResidenteCollectionNew = new ArrayList<Residente>();
            for (Residente residenteCollectionNewResidenteToAttach : residenteCollectionNew) {
                residenteCollectionNewResidenteToAttach = em.getReference(residenteCollectionNewResidenteToAttach.getClass(), residenteCollectionNewResidenteToAttach.getIdentificacion());
                attachedResidenteCollectionNew.add(residenteCollectionNewResidenteToAttach);
            }
            residenteCollectionNew = attachedResidenteCollectionNew;
            residente.setResidenteCollection(residenteCollectionNew);
            residente = em.merge(residente);
            if (titularOld != null && !titularOld.equals(titularNew)) {
                titularOld.getResidenteCollection().remove(residente);
                titularOld = em.merge(titularOld);
            }
            if (titularNew != null && !titularNew.equals(titularOld)) {
                titularNew.getResidenteCollection().add(residente);
                titularNew = em.merge(titularNew);
            }
            for (Evento eventoCollectionNewEvento : eventoCollectionNew) {
                if (!eventoCollectionOld.contains(eventoCollectionNewEvento)) {
                    Residente oldIdentificacionOfEventoCollectionNewEvento = eventoCollectionNewEvento.getIdentificacion();
                    eventoCollectionNewEvento.setIdentificacion(residente);
                    eventoCollectionNewEvento = em.merge(eventoCollectionNewEvento);
                    if (oldIdentificacionOfEventoCollectionNewEvento != null && !oldIdentificacionOfEventoCollectionNewEvento.equals(residente)) {
                        oldIdentificacionOfEventoCollectionNewEvento.getEventoCollection().remove(eventoCollectionNewEvento);
                        oldIdentificacionOfEventoCollectionNewEvento = em.merge(oldIdentificacionOfEventoCollectionNewEvento);
                    }
                }
            }
            for (Factura facturaCollectionNewFactura : facturaCollectionNew) {
                if (!facturaCollectionOld.contains(facturaCollectionNewFactura)) {
                    Residente oldIdentificacionOfFacturaCollectionNewFactura = facturaCollectionNewFactura.getIdentificacion();
                    facturaCollectionNewFactura.setIdentificacion(residente);
                    facturaCollectionNewFactura = em.merge(facturaCollectionNewFactura);
                    if (oldIdentificacionOfFacturaCollectionNewFactura != null && !oldIdentificacionOfFacturaCollectionNewFactura.equals(residente)) {
                        oldIdentificacionOfFacturaCollectionNewFactura.getFacturaCollection().remove(facturaCollectionNewFactura);
                        oldIdentificacionOfFacturaCollectionNewFactura = em.merge(oldIdentificacionOfFacturaCollectionNewFactura);
                    }
                }
            }
            for (Comentario comentarioCollectionNewComentario : comentarioCollectionNew) {
                if (!comentarioCollectionOld.contains(comentarioCollectionNewComentario)) {
                    Residente oldIdentificacionOfComentarioCollectionNewComentario = comentarioCollectionNewComentario.getIdentificacion();
                    comentarioCollectionNewComentario.setIdentificacion(residente);
                    comentarioCollectionNewComentario = em.merge(comentarioCollectionNewComentario);
                    if (oldIdentificacionOfComentarioCollectionNewComentario != null && !oldIdentificacionOfComentarioCollectionNewComentario.equals(residente)) {
                        oldIdentificacionOfComentarioCollectionNewComentario.getComentarioCollection().remove(comentarioCollectionNewComentario);
                        oldIdentificacionOfComentarioCollectionNewComentario = em.merge(oldIdentificacionOfComentarioCollectionNewComentario);
                    }
                }
            }
            for (RcdApt rcdAptCollectionNewRcdApt : rcdAptCollectionNew) {
                if (!rcdAptCollectionOld.contains(rcdAptCollectionNewRcdApt)) {
                    Residente oldIdentificacionOfRcdAptCollectionNewRcdApt = rcdAptCollectionNewRcdApt.getIdentificacion();
                    rcdAptCollectionNewRcdApt.setIdentificacion(residente);
                    rcdAptCollectionNewRcdApt = em.merge(rcdAptCollectionNewRcdApt);
                    if (oldIdentificacionOfRcdAptCollectionNewRcdApt != null && !oldIdentificacionOfRcdAptCollectionNewRcdApt.equals(residente)) {
                        oldIdentificacionOfRcdAptCollectionNewRcdApt.getRcdAptCollection().remove(rcdAptCollectionNewRcdApt);
                        oldIdentificacionOfRcdAptCollectionNewRcdApt = em.merge(oldIdentificacionOfRcdAptCollectionNewRcdApt);
                    }
                }
            }
            for (Residente residenteCollectionOldResidente : residenteCollectionOld) {
                if (!residenteCollectionNew.contains(residenteCollectionOldResidente)) {
                    residenteCollectionOldResidente.setTitular(null);
                    residenteCollectionOldResidente = em.merge(residenteCollectionOldResidente);
                }
            }
            for (Residente residenteCollectionNewResidente : residenteCollectionNew) {
                if (!residenteCollectionOld.contains(residenteCollectionNewResidente)) {
                    Residente oldTitularOfResidenteCollectionNewResidente = residenteCollectionNewResidente.getTitular();
                    residenteCollectionNewResidente.setTitular(residente);
                    residenteCollectionNewResidente = em.merge(residenteCollectionNewResidente);
                    if (oldTitularOfResidenteCollectionNewResidente != null && !oldTitularOfResidenteCollectionNewResidente.equals(residente)) {
                        oldTitularOfResidenteCollectionNewResidente.getResidenteCollection().remove(residenteCollectionNewResidente);
                        oldTitularOfResidenteCollectionNewResidente = em.merge(oldTitularOfResidenteCollectionNewResidente);
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
                String id = residente.getIdentificacion();
                if (findResidente(id) == null) {
                    throw new NonexistentEntityException("The residente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Residente residente;
            try {
                residente = em.getReference(Residente.class, id);
                residente.getIdentificacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The residente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Evento> eventoCollectionOrphanCheck = residente.getEventoCollection();
            for (Evento eventoCollectionOrphanCheckEvento : eventoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Residente (" + residente + ") cannot be destroyed since the Evento " + eventoCollectionOrphanCheckEvento + " in its eventoCollection field has a non-nullable identificacion field.");
            }
            Collection<Factura> facturaCollectionOrphanCheck = residente.getFacturaCollection();
            for (Factura facturaCollectionOrphanCheckFactura : facturaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Residente (" + residente + ") cannot be destroyed since the Factura " + facturaCollectionOrphanCheckFactura + " in its facturaCollection field has a non-nullable identificacion field.");
            }
            Collection<Comentario> comentarioCollectionOrphanCheck = residente.getComentarioCollection();
            for (Comentario comentarioCollectionOrphanCheckComentario : comentarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Residente (" + residente + ") cannot be destroyed since the Comentario " + comentarioCollectionOrphanCheckComentario + " in its comentarioCollection field has a non-nullable identificacion field.");
            }
            Collection<RcdApt> rcdAptCollectionOrphanCheck = residente.getRcdAptCollection();
            for (RcdApt rcdAptCollectionOrphanCheckRcdApt : rcdAptCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Residente (" + residente + ") cannot be destroyed since the RcdApt " + rcdAptCollectionOrphanCheckRcdApt + " in its rcdAptCollection field has a non-nullable identificacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Residente titular = residente.getTitular();
            if (titular != null) {
                titular.getResidenteCollection().remove(residente);
                titular = em.merge(titular);
            }
            Collection<Residente> residenteCollection = residente.getResidenteCollection();
            for (Residente residenteCollectionResidente : residenteCollection) {
                residenteCollectionResidente.setTitular(null);
                residenteCollectionResidente = em.merge(residenteCollectionResidente);
            }
            em.remove(residente);
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

    public List<Residente> findResidenteEntities() {
        return findResidenteEntities(true, -1, -1);
    }

    public List<Residente> findResidenteEntities(int maxResults, int firstResult) {
        return findResidenteEntities(false, maxResults, firstResult);
    }

    private List<Residente> findResidenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Residente.class));
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

    public Residente findResidente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Residente.class, id);
        } finally {
            em.close();
        }
    }

    public int getResidenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Residente> rt = cq.from(Residente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
