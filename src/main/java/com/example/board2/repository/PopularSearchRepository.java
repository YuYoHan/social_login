package com.example.board2.repository;

import com.example.board2.entity.PopularSearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularSearchRepository extends JpaRepository<PopularSearchEntity, String> {
}
