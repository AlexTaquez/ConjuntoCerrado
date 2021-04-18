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
@Table(name = "conjunto")
@NamedQueries({
    @NamedQuery(name = "Conjunto.findAll", query = "SELECT c FROM Conjunto c")
    , @NamedQuery(name = "Conjunto.findById", query = "SELECT c FROM Conjunto c WHERE c.id = :id")
    , @NamedQuery(name = "Conjunto.findByNombre", query = "SELECT c FROM Conjunto c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Conjunto.findByCostoAdm", query = "SELECT c FROM Conjunto c WHERE c.costoAdm = :costoAdm")
    , @NamedQuery(name = "Conjunto.findByDireccion", query = "SELECT c FROM Conjunto c WHERE c.direccion = :direccion")
    , @NamedQuery(name = "Conjunto.findByAdmin", query = "SELECT c FROM Conjunto c WHERE c.admin = :admin")})
@XmlRootElement
public class Conjunto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = true)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombre")
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "costo_adm")
    private BigDecimal costoAdm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "admin")
    private String admin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idConjunto")
    private Collection<Torre> torreCollection;

    public Conjunto() {
    }

    public Conjunto(Short id) {
        this.id = id;
    }

    public Conjunto(Short id, String nombre, BigDecimal costoAdm, String direccion, String admin) {
        this.id = id;
        this.nombre = nombre;
        this.costoAdm = costoAdm;
        this.direccion = direccion;
        this.admin = admin;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getCostoAdm() {
        return costoAdm;
    }

    public void setCostoAdm(BigDecimal costoAdm) {
        this.costoAdm = costoAdm;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @XmlTransient
    public Collection<Torre> getTorreCollection() {
        return torreCollection;
    }

    public void setTorreCollection(Collection<Torre> torreCollection) {
        this.torreCollection = torreCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conjunto)) {
            return false;
        }
        Conjunto other = (Conjunto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " Conjunto " + nombre + " " + direccion;
    }
    
}
