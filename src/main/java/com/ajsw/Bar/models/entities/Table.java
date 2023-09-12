package com.ajsw.Bar.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class Table implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String reference;

    @ManyToOne
    @JoinColumn(name = "idBar")
    private Bar bar;
}
