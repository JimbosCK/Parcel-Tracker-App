package com.example.parcel_tracker.repository;

import com.example.parcel_tracker.model.Parcel;
import com.example.parcel_tracker.model.ParcelHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelHistoryRepository extends JpaRepository<ParcelHistory, Long> {
    List<ParcelHistory> findByParcelOrderByDateTimeAsc(Parcel parcel);
}