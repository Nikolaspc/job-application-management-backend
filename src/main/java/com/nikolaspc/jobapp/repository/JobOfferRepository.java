package com.nikolaspc.jobapp.repository;

import com.nikolaspc.jobapp.domain.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {

    /**
     * Busca todas las ofertas de trabajo activas.
     * Al declarar este método, Spring Data JPA implementa automáticamente
     * la consulta: SELECT * FROM job_offers WHERE active = true
     */
    List<JobOffer> findByActiveTrue();
}