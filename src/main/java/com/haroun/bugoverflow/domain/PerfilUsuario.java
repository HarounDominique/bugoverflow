package com.haroun.bugoverflow.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PerfilUsuario.
 */
@Entity
@Table(name = "perfil_usuario")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PerfilUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "nombre_visible", length = 50, nullable = false)
    private String nombreVisible;

    @Size(max = 500)
    @Column(name = "bio", length = 500)
    private String bio;

    @Size(max = 500)
    @Column(name = "github", length = 500)
    private String github;

    @Size(max = 500)
    @Column(name = "web_personal", length = 500)
    private String webPersonal;

    @Size(max = 500)
    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PerfilUsuario id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreVisible() {
        return this.nombreVisible;
    }

    public PerfilUsuario nombreVisible(String nombreVisible) {
        this.setNombreVisible(nombreVisible);
        return this;
    }

    public void setNombreVisible(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String getBio() {
        return this.bio;
    }

    public PerfilUsuario bio(String bio) {
        this.setBio(bio);
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getGithub() {
        return this.github;
    }

    public PerfilUsuario github(String github) {
        this.setGithub(github);
        return this;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getWebPersonal() {
        return this.webPersonal;
    }

    public PerfilUsuario webPersonal(String webPersonal) {
        this.setWebPersonal(webPersonal);
        return this;
    }

    public void setWebPersonal(String webPersonal) {
        this.webPersonal = webPersonal;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public PerfilUsuario avatarUrl(String avatarUrl) {
        this.setAvatarUrl(avatarUrl);
        return this;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PerfilUsuario user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PerfilUsuario)) {
            return false;
        }
        return getId() != null && getId().equals(((PerfilUsuario) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PerfilUsuario{" +
            "id=" + getId() +
            ", nombreVisible='" + getNombreVisible() + "'" +
            ", bio='" + getBio() + "'" +
            ", github='" + getGithub() + "'" +
            ", webPersonal='" + getWebPersonal() + "'" +
            ", avatarUrl='" + getAvatarUrl() + "'" +
            "}";
    }
}
