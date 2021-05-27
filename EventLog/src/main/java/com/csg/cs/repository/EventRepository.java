package com.csg.cs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.csg.cs.entity.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String> {

}
