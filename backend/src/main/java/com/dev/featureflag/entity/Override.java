package com.dev.featureflag.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "override_flag")
@NoArgsConstructor
@AllArgsConstructor
public class Override {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long flagId;
    private String userId;
    private boolean enabled;
}
