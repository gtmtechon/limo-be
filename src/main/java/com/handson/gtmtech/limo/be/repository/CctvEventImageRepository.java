package com.handson.gtmtech.limo.be.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.handson.gtmtech.limo.be.entity.CctvEventImage;

import java.util.List;

@Repository
public interface CctvEventImageRepository extends JpaRepository<CctvEventImage, Long> {
    List<CctvEventImage> findAllByOrderByEventTimestampDesc(); // 최신순으로 정렬
}