package com.instrumentos.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categoria_instrumento", 
       indexes = @Index(name = "idx_categoria_denominacion", columnList = "denominacion"))
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "La denominación es requerida")
    @Size(min = 2, max = 100, message = "La denominación debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100, unique = true)
    private String denominacion;
    
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Instrumento> instrumentos;
    
    public Categoria() {}
    
    public Categoria(String denominacion) {
        this.denominacion = denominacion;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDenominacion() {
        return denominacion;
    }
    
    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion != null ? denominacion.trim() : null;
    }
    
    public List<Instrumento> getInstrumentos() {
        return instrumentos;
    }
    
    public void setInstrumentos(List<Instrumento> instrumentos) {
        this.instrumentos = instrumentos;
    }
    
    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", denominacion='" + denominacion + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;
        Categoria categoria = (Categoria) o;
        return id != null && id.equals(categoria.id);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
