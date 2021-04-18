/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "torre")
@NamedQueries({
    @NamedQuery(name = "Torre.findAll", query = "SELECT t FROM Torre t")
    , @NamedQuery(name = "Torre.findByIdTorre", query = "SELECT t FROM Torre t WHERE t.idTorre = :idTorre")
    , @NamedQuery(name = "Torre.findByNombre", query = "SELECT t FROM Torre t WHERE t.nombre = :nombre")
    , @NamedQuery(name = "Torre.findByNPisos", query = "SELECT t FROM Torre t WHERE t.nPisos = :nPisos")
    , @NamedQuery(name = "Torre.findByNApt", query = "SELECT t FROM Torre t WHERE t.nApt = :nApt")})
@XmlRootElement
public class Torre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = true)
    @Column(name = "id_torre")
    private Short idTorre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "n_pisos")
    private short nPisos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "n_apt")
    private short nApt;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTorre")
    private Collection<Apartamento> apartamentoCollection;
    @JoinColumn(name = "id_conjunto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Conjunto idConjunto;

    public Torre() {
    }

    public Torre(Short idTorre) {
        this.idTorre = idTorre;
    }

    public Torre(Short idTorre, String nombre, short nPisos, short nApt) {
        this.idTorre = idTorre;
        this.nombre = nombre;
        this.nPisos = nPisos;
        this.nApt = nApt;
    }

    public Short getIdTorre() {
        return idTorre;
    }

    public void setIdTorre(Short idTorre) {
        this.idTorre = idTorre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getNPisos() {
        return nPisos;
    }

    public void setNPisos(short nPisos) {
        this.nPisos = nPisos;
    }

    public short getNApt() {
        return nApt;
    }

    public void setNApt(short nApt) {
        this.nApt = nApt;
    }

    @XmlTransient
    public Collection<Apartamento> getApartamentoCollection() {
        return apartamentoCollection;
    }

    public void setApartamentoCollection(Collection<Apartamento> apartamentoCollection) {
        this.apartamentoCollection = apartamentoCollection;
    }

    public Conjunto getIdConjunto() {
        return idConjunto;
    }

    public void setIdConjunto(Conjunto idConjunto) {
        this.idConjunto = idConjunto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTorre != null ? idTorre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Torre)) {
            return false;
        }
        Torre other = (Torre) object;
        if ((this.idTorre == null && other.idTorre != null) || (this.idTorre != null && !this.idTorre.equals(other.idTorre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " " + nombre;        //Nombre de la torre
    }
    
}
