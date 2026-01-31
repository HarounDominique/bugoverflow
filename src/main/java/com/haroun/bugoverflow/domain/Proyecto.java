package com.haroun.bugoverflow.domain;

import com.haroun.bugoverflow.domain.enumeration.tipocategoria;
import com.haroun.bugoverflow.domain.enumeration.tipoestado;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Proyecto.
 */
@Entity
@Table(name = "proyecto")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Proyecto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @NotNull
    @Size(max = 500)
    @Column(name = "descripcion", length = 500, nullable = false)
    private String descripcion;

    @Size(max = 500)
    @Column(name = "urlrepo", length = 500)
    private String urlrepo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private tipocategoria categoria;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private tipoestado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    private User autor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_proyecto__colaboradores",
        joinColumns = @JoinColumn(name = "proyecto_id"),
        inverseJoinColumns = @JoinColumn(name = "colaboradores_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> colaboradores = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proyecto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Proyecto titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Proyecto descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlrepo() {
        return this.urlrepo;
    }

    public Proyecto urlrepo(String urlrepo) {
        this.setUrlrepo(urlrepo);
        return this;
    }

    public void setUrlrepo(String urlrepo) {
        this.urlrepo = urlrepo;
    }

    public tipocategoria getCategoria() {
        return this.categoria;
    }

    public Proyecto categoria(tipocategoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(tipocategoria categoria) {
        this.categoria = categoria;
    }

    public tipoestado getEstado() {
        return this.estado;
    }

    public Proyecto estado(tipoestado estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(tipoestado estado) {
        this.estado = estado;
    }

    public User getAutor() {
        return this.autor;
    }

    public void setAutor(User user) {
        this.autor = user;
    }

    public Proyecto autor(User user) {
        this.setAutor(user);
        return this;
    }

    public Set<User> getColaboradores() {
        return this.colaboradores;
    }

    public void setColaboradores(Set<User> users) {
        this.colaboradores = users;
    }

    public Proyecto colaboradores(Set<User> users) {
        this.setColaboradores(users);
        return this;
    }

    public Proyecto addColaboradores(User user) {
        this.colaboradores.add(user);
        return this;
    }

    public Proyecto removeColaboradores(User user) {
        this.colaboradores.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proyecto)) {
            return false;
        }
        return getId() != null && getId().equals(((Proyecto) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proyecto{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", urlrepo='" + getUrlrepo() + "'" +
            ", categoria='" + getCategoria() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
