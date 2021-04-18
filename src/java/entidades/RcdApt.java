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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "rcd_apt")
@NamedQueries({
    @NamedQuery(name = "RcdApt.findAll", query = "SELECT r FROM RcdApt r")
    , @NamedQuery(name = "RcdApt.findByIdRcdApt", query = "SELECT r FROM RcdApt r WHERE r.idRcdApt = :idRcdApt")})
@XmlRootElement
public class RcdApt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = true)
    @Column(name = "id_rcd_apt")
    private Integer idRcdApt;
    @JoinColumn(name = "identificacion", referencedColumnName = "identificacion")
    @ManyToOne(optional = false)
    private Residente identificacion;
    @JoinColumn(name = "id_apt", referencedColumnName = "id_apt")
    @ManyToOne(optional = false)
    private Apartamento idApt;

    public RcdApt() {
    }

    public RcdApt(Integer idRcdApt) {
        this.idRcdApt = idRcdApt;
    }

    public Integer getIdRcdApt() {
        return idRcdApt;
    }

    public void setIdRcdApt(Integer idRcdApt) {
        this.idRcdApt = idRcdApt;
    }

    public Residente getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(Residente identificacion) {
        this.identificacion = identificacion;
    }

    public Apartamento getIdApt() {
        return idApt;
    }

    public void setIdApt(Apartamento idApt) {
        this.idApt = idApt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRcdApt != null ? idRcdApt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RcdApt)) {
            return false;
        }
        RcdApt other = (RcdApt) object;
        if ((this.idRcdApt == null && other.idRcdApt != null) || (this.idRcdApt != null && !this.idRcdApt.equals(other.idRcdApt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.RcdApt[ idRcdApt=" + idRcdApt + " ]";
    }
    
}
