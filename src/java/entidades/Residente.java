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
@Table(name = "residente")
@NamedQueries({
    @NamedQuery(name = "Residente.findAll", query = "SELECT r FROM Residente r")
    , @NamedQuery(name = "Residente.findByIdentificacion", query = "SELECT r FROM Residente r WHERE r.identificacion = :identificacion")
    , @NamedQuery(name = "Residente.findByTipoId", query = "SELECT r FROM Residente r WHERE r.tipoId = :tipoId")
    , @NamedQuery(name = "Residente.findByNombres", query = "SELECT r FROM Residente r WHERE r.nombres = :nombres")
    , @NamedQuery(name = "Residente.findByApellidos", query = "SELECT r FROM Residente r WHERE r.apellidos = :apellidos")
    , @NamedQuery(name = "Residente.findByCorreo", query = "SELECT r FROM Residente r WHERE r.correo = :correo")
    , @NamedQuery(name = "Residente.findByTelefono", query = "SELECT r FROM Residente r WHERE r.telefono = :telefono")
    , @NamedQuery(name = "Residente.findByActivo", query = "SELECT r FROM Residente r WHERE r.activo = :activo")})
@XmlRootElement
public class Residente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "identificacion")
    private String identificacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tipo_id")
    private String tipoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nombres")
    private String nombres;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activo")
    private boolean activo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "identificacion")
    private Collection<Evento> eventoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "identificacion")
    private Collection<Factura> facturaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "identificacion")
    private Collection<Comentario> comentarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "identificacion")
    private Collection<RcdApt> rcdAptCollection;
    @OneToMany(mappedBy = "titular")
    private Collection<Residente> residenteCollection;
    @JoinColumn(name = "titular", referencedColumnName = "identificacion")
    @ManyToOne
    private Residente titular;

    public Residente() {
    }

    public Residente(String identificacion) {
        this.identificacion = identificacion;
    }

    public Residente(String identificacion, String tipoId, String nombres, String apellidos, String correo, String telefono, boolean activo) {
        this.identificacion = identificacion;
        this.tipoId = tipoId;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.activo = activo;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoId() {
        return tipoId;
    }

    public void setTipoId(String tipoId) {
        this.tipoId = tipoId;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @XmlTransient
    public Collection<Evento> getEventoCollection() {
        return eventoCollection;
    }

    public void setEventoCollection(Collection<Evento> eventoCollection) {
        this.eventoCollection = eventoCollection;
    }

    @XmlTransient
    public Collection<Factura> getFacturaCollection() {
        return facturaCollection;
    }

    public void setFacturaCollection(Collection<Factura> facturaCollection) {
        this.facturaCollection = facturaCollection;
    }

    @XmlTransient
    public Collection<Comentario> getComentarioCollection() {
        return comentarioCollection;
    }

    public void setComentarioCollection(Collection<Comentario> comentarioCollection) {
        this.comentarioCollection = comentarioCollection;
    }

    @XmlTransient
    public Collection<RcdApt> getRcdAptCollection() {
        return rcdAptCollection;
    }

    public void setRcdAptCollection(Collection<RcdApt> rcdAptCollection) {
        this.rcdAptCollection = rcdAptCollection;
    }

    @XmlTransient
    public Collection<Residente> getResidenteCollection() {
        return residenteCollection;
    }

    public void setResidenteCollection(Collection<Residente> residenteCollection) {
        this.residenteCollection = residenteCollection;
    }

    public Residente getTitular() {
        return titular;
    }

    public void setTitular(Residente titular) {
        this.titular = titular;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identificacion != null ? identificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Residente)) {
            return false;
        }
        Residente other = (Residente) object;
        if ((this.identificacion == null && other.identificacion != null) || (this.identificacion != null && !this.identificacion.equals(other.identificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Residente[ identificacion=" + identificacion + " ]";
    }
    
}
