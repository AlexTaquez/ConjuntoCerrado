/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Usuario
 */
@Entity
@Table(name = "factura")
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f")
    , @NamedQuery(name = "Factura.findByIdFactura", query = "SELECT f FROM Factura f WHERE f.idFactura = :idFactura")
    , @NamedQuery(name = "Factura.findByConcepto", query = "SELECT f FROM Factura f WHERE f.concepto = :concepto")
    , @NamedQuery(name = "Factura.findByValor", query = "SELECT f FROM Factura f WHERE f.valor = :valor")
    , @NamedQuery(name = "Factura.findByFechaExp", query = "SELECT f FROM Factura f WHERE f.fechaExp = :fechaExp")
    , @NamedQuery(name = "Factura.findByFechaLim", query = "SELECT f FROM Factura f WHERE f.fechaLim = :fechaLim")
    , @NamedQuery(name = "Factura.findByEstado", query = "SELECT f FROM Factura f WHERE f.estado = :estado")})
@XmlRootElement
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_factura")
    private Integer idFactura;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "concepto")
    private String concepto;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private BigDecimal valor;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_exp")
    @Temporal(TemporalType.DATE)
    private Date fechaExp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_lim")
    @Temporal(TemporalType.DATE)
    private Date fechaLim;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "identificacion", referencedColumnName = "identificacion")
    @ManyToOne(optional = false)
    private Residente identificacion;
    @JoinColumn(name = "id_apt", referencedColumnName = "id_apt")
    @ManyToOne(optional = false)
    private Apartamento idApt;

    public Factura() {
    }

    public Factura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Factura(Integer idFactura, String concepto, BigDecimal valor, Date fechaExp, Date fechaLim, String estado) {
        this.idFactura = idFactura;
        this.concepto = concepto;
        this.valor = valor;
        this.fechaExp = fechaExp;
        this.fechaLim = fechaLim;
        this.estado = estado;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getFechaExp() {
        return fechaExp;
    }

    public void setFechaExp(Date fechaExp) {
        this.fechaExp = fechaExp;
    }

    public Date getFechaLim() {
        return fechaLim;
    }

    public void setFechaLim(Date fechaLim) {
        this.fechaLim = fechaLim;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
        hash += (idFactura != null ? idFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        if ((this.idFactura == null && other.idFactura != null) || (this.idFactura != null && !this.idFactura.equals(other.idFactura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Factura[ idFactura=" + idFactura + " ]";
    }
    
}
