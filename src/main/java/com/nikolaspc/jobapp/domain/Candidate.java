package com.nikolaspc.jobapp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import java.time.LocalDate;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate implements Persistable<Long> {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Transient
    @Builder.Default
    private boolean isNewCandidate = true; // English: Renamed to avoid confusion with the method name

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNewCandidate;
    }

    // English: Use PrePersist to ensure state is handled before Hibernate executes the INSERT
    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNewCandidate = false;
    }
}