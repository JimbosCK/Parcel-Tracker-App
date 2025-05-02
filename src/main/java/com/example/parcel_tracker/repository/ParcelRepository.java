package com.example.parcel_tracker.repository;

import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelStatusEnum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    Optional<Parcel> findByTrackingCode(String trackingCode);
    List<Parcel> findByStatus(ParcelStatusEnum status);
    Optional<Parcel> findById(long id);
}