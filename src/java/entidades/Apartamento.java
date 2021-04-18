/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "apartamento")
@NamedQueries({
    @NamedQuery(name = "Apartamento.findAll", query = "SELECT a FROM Apartamento a")
    , @NamedQuery(name = "Apartamento.findByIdApt", query = "SELECT a FROM Apartamento a WHERE a.idApt = :idApt")
    , @NamedQuery(name = "Apartamento.findByPiso", query = "SELECT a FROM Apartamento a WHERE a.piso = :piso")
    , @NamedQuery(name = "Apartamento.findByNumero", query = "SELECT a FROM Apartamento a WHERE a.numero = :numero")
    , @NamedQuery(name = "Apartamento.findByPropiedad", query = "SELECT a FROM Apartamento a WHERE a.propiedad = :propiedad")
    , @NamedQuery(name = "Apartamento.findByCostoArr", query = "SELECT a FROM Apartamento a WHERE a.costoArr = :costoArr")})
@XmlRootElement
public class Apartamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = true)
    @Column(name = "id_apt")
    private Short idApt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "piso")
    private short piso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero")
    private short numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "propiedad")
    private String propiedad;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo_arr")
    private BigDecimal costoArr;
    @JoinColumn(name = "id_torre", referencedColumnName = "id_torre")
    @ManyToOne(optional = false)
    private Torre idTorre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idApt")
    private Collection<Factura> facturaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idApt")
    private Collection<RcdApt> rcdAptCollection;

    public Apartamento() {
    }

    public Apartamento(Short idApt) {
        this.idApt = idApt;
    }

    public Apartamento(Short idApt, short piso, short numero, String propiedad, BigDecimal costoArr) {
        this.idApt = idApt;
        this.piso = piso;
        this.numero = numero;
        this.propiedad = propiedad;
        this.costoArr = costoArr;
    }

    public Short getIdApt() {
        return idApt;
    }

    public void setIdApt(Short idApt) {
        this.idApt = idApt;
    }

    public short getPiso() {
        return piso;
    }

    public void setPiso(short piso) {
        this.piso = piso;
    }

    public short getNumero() {
        return numero;
    }

    public void setNumero(short numero) {
        this.numero = numero;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public void setPropiedad(String propiedad) {
        this.propiedad = propiedad;
    }

    public BigDecimal getCostoArr() {
        return costoArr;
    }

    public void setCostoArr(BigDecimal costoArr) {
        this.costoArr = costoArr;
    }

    public Torre getIdTorre() {
        return idTorre;
    }

    public void setIdTorre(Torre idTorre) {
        this.idTorre = idTorre;
    }

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    @XmlTransient
    public Collection<RcdApt> getRcdAptCollection() {
        return rcdAptCollection;
    }

    public void setRcdAptCollection(Collection<RcdApt> rcdAptCollection) {
        this.rcdAptCollection = rcdAptCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idApt != null ? idApt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Apartamento)) {
            return false;
        }
        Apartamento other = (Apartamento) object;
        if ((this.idApt == null && other.idApt != null) || (this.idApt != null && !this.idApt.equals(other.idApt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Apartamento: " + idTorre + " piso "+ piso +" número " + piso + "0" + numero;
    }
    
}
