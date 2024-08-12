package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.*;




@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false, unique = true)
    private String name;

    @Column(length = 128, nullable = false)
    private String description;

    @Override
    public String toString() {
        return this.name;
    }

}
