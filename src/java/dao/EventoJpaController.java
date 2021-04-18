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
import entidades.Residente;
import entidades.Comentario;
import entidades.Evento;
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
public class EventoJpaController implements Serializable {

    public EventoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Evento evento) throws RollbackFailureException, Exception {
        if (evento.getComentarioCollection() == null) {
            evento.setComentarioCollection(new ArrayList<Comentario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Residente identificacion = evento.getIdentificacion();
            if (identificacion != null) {
                identificacion = em.getReference(identificacion.getClass(), identificacion.getIdentificacion());
                evento.setIdentificacion(identificacion);
            }
            Collection<Comentario> attachedComentarioCollection = new ArrayList<Comentario>();
            for (Comentario comentarioCollectionComentarioToAttach : evento.getComentarioCollection()) {
                comentarioCollectionComentarioToAttach = em.getReference(comentarioCollectionComentarioToAttach.getClass(), comentarioCollectionComentarioToAttach.getIdComentario());
                attachedComentarioCollection.add(comentarioCollectionComentarioToAttach);
            }
            evento.setComentarioCollection(attachedComentarioCollection);
            em.persist(evento);
            if (identificacion != null) {
                identificacion.getEventoCollection().add(evento);
                identificacion = em.merge(identificacion);
            }
            for (Comentario comentarioCollectionComentario : evento.getComentarioCollection()) {
                Evento oldIdEventoOfComentarioCollectionComentario = comentarioCollectionComentario.getIdEvento();
                comentarioCollectionComentario.setIdEvento(evento);
                comentarioCollectionComentario = em.merge(comentarioCollectionComentario);
                if (oldIdEventoOfComentarioCollectionComentario != null) {
                    oldIdEventoOfComentarioCollectionComentario.getComentarioCollection().remove(comentarioCollectionComentario);
                    oldIdEventoOfComentarioCollectionComentario = em.merge(oldIdEventoOfComentarioCollectionComentario);
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

    public void edit(Evento evento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Evento persistentEvento = em.find(Evento.class, evento.getIdEvento());
            Residente identificacionOld = persistentEvento.getIdentificacion();
            Residente identificacionNew = evento.getIdentificacion();
            Collection<Comentario> comentarioCollectionOld = persistentEvento.getComentarioCollection();
            Collection<Comentario> comentarioCollectionNew = evento.getComentarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Comentario comentarioCollectionOldComentario : comentarioCollectionOld) {
                if (!comentarioCollectionNew.contains(comentarioCollectionOldComentario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Comentario " + comentarioCollectionOldComentario + " since its idEvento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (identificacionNew != null) {
                identificacionNew = em.getReference(identificacionNew.getClass(), identificacionNew.getIdentificacion());
                evento.setIdentificacion(identificacionNew);
            }
            Collection<Comentario> attachedComentarioCollectionNew = new ArrayList<Comentario>();
            for (Comentario comentarioCollectionNewComentarioToAttach : comentarioCollectionNew) {
                comentarioCollectionNewComentarioToAttach = em.getReference(comentarioCollectionNewComentarioToAttach.getClass(), comentarioCollectionNewComentarioToAttach.getIdComentario());
                attachedComentarioCollectionNew.add(comentarioCollectionNewComentarioToAttach);
            }
            comentarioCollectionNew = attachedComentarioCollectionNew;
            evento.setComentarioCollection(comentarioCollectionNew);
            evento = em.merge(evento);
            if (identificacionOld != null && !identificacionOld.equals(identificacionNew)) {
                identificacionOld.getEventoCollection().remove(evento);
                identificacionOld = em.merge(identificacionOld);
            }
            if (identificacionNew != null && !identificacionNew.equals(identificacionOld)) {
                identificacionNew.getEventoCollection().add(evento);
                identificacionNew = em.merge(identificacionNew);
            }
            for (Comentario comentarioCollectionNewComentario : comentarioCollectionNew) {
                if (!comentarioCollectionOld.contains(comentarioCollectionNewComentario)) {
                    Evento oldIdEventoOfComentarioCollectionNewComentario = comentarioCollectionNewComentario.getIdEvento();
                    comentarioCollectionNewComentario.setIdEvento(evento);
                    comentarioCollectionNewComentario = em.merge(comentarioCollectionNewComentario);
                    if (oldIdEventoOfComentarioCollectionNewComentario != null && !oldIdEventoOfComentarioCollectionNewComentario.equals(evento)) {
                        oldIdEventoOfComentarioCollectionNewComentario.getComentarioCollection().remove(comentarioCollectionNewComentario);
                        oldIdEventoOfComentarioCollectionNewComentario = em.merge(oldIdEventoOfComentarioCollectionNewComentario);
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
                Integer id = evento.getIdEvento();
                if (findEvento(id) == null) {
                    throw new NonexistentEntityException("The evento with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Evento evento;
            try {
                evento = em.getReference(Evento.class, id);
                evento.getIdEvento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The evento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Comentario> comentarioCollectionOrphanCheck = evento.getComentarioCollection();
            for (Comentario comentarioCollectionOrphanCheckComentario : comentarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Evento (" + evento + ") cannot be destroyed since the Comentario " + comentarioCollectionOrphanCheckComentario + " in its comentarioCollection field has a non-nullable idEvento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Residente identificacion = evento.getIdentificacion();
            if (identificacion != null) {
                identificacion.getEventoCollection().remove(evento);
                identificacion = em.merge(identificacion);
            }
            em.remove(evento);
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

    public List<Evento> findEventoEntities() {
        return findEventoEntities(true, -1, -1);
    }

    public List<Evento> findEventoEntities(int maxResults, int firstResult) {
        return findEventoEntities(false, maxResults, firstResult);
    }

    private List<Evento> findEventoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Evento.class));
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

    public Evento findEvento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Evento.class, id);
        } finally {
            em.close();
        }
    }

    public int getEventoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Evento> rt = cq.from(Evento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
