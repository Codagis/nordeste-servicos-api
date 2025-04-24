package com.ordem.servico.api.repository;

import com.ordem.servico.api.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    ImageEntity findByFileName(String fileName);
}