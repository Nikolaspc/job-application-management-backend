package com.Nikolaspc.jobapp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Candidate candidate;

    @ManyToOne(optional = false)
    private JobOffer jobOffer;

    @Column(nullable = false)
    private String status;
}
