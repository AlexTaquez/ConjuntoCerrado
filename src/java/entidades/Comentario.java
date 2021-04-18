/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "comentario")
@NamedQueries({
    @NamedQuery(name = "Comentario.findAll", query = "SELECT c FROM Comentario c")
    , @NamedQuery(name = "Comentario.findByIdComentario", query = "SELECT c FROM Comentario c WHERE c.idComentario = :idComentario")
    , @NamedQuery(name = "Comentario.findByMensaje", query = "SELECT c FROM Comentario c WHERE c.mensaje = :mensaje")
    , @NamedQuery(name = "Comentario.findByTipo", query = "SELECT c FROM Comentario c WHERE c.tipo = :tipo")})
@XmlRootElement
public class Comentario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = true)
    @Column(name = "id_comentario")
    private Integer idComentario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "mensaje")
    private String mensaje;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tipo")
    private String tipo;
    @JoinColumn(name = "id_evento", referencedColumnName = "id_evento")
    @ManyToOne(optional = false)
    private Evento idEvento;
    @JoinColumn(name = "identificacion", referencedColumnName = "identificacion")
    @ManyToOne(optional = false)
    private Residente identificacion;

    public Comentario() {
    }

    public Comentario(Integer idComentario) {
        this.idComentario = idComentario;
    }

    public Comentario(Integer idComentario, String mensaje, String tipo) {
        this.idComentario = idComentario;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }

    public Integer getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(Integer idComentario) {
        this.idComentario = idComentario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Evento getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Evento idEvento) {
        this.idEvento = idEvento;
    }

    public Residente getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(Residente identificacion) {
        this.identificacion = identificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idComentario != null ? idComentario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comentario)) {
            return false;
        }
        Comentario other = (Comentario) object;
        if ((this.idComentario == null && other.idComentario != null) || (this.idComentario != null && !this.idComentario.equals(other.idComentario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Comentario[ idComentario=" + idComentario + " ]";
    }
    
}
