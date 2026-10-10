package com.descam.backend.repository;

import com.descam.backend.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisRepository 
  extends JpaRepository<Analysis, Long>
{
  
}