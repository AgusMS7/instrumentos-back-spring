package com.instrumentos.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "categoria_instrumento")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "La denominación es requerida")
    @Column(nullable = false, length = 100)
    private String denominacion;
    
    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @JsonIgnore // Evita la referencia circular
    private List<Instrumento> instrumentos;
    
    // Constructores
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
        this.denominacion = denominacion;
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
}
