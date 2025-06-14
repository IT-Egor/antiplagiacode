package ru.itegor.antiplagiacode.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Collection<TaskEntity> findAllByClazz_Id(Long classId);

    @Query("SELECT t FROM TaskEntity t WHERE :date BETWEEN t.startDate AND t.endDate AND t.clazz.id = :classId")
    Collection<TaskEntity> findRelevantForClass(
            @Param("classId") Long classId,
            @Param("date") LocalDate date);

    @Query("SELECT t FROM TaskEntity t WHERE :date > t.endDate AND t.clazz.id = :classId")
    Collection<TaskEntity> findIrrelevantForClass(
            @Param("classId") Long classId,
            @Param("date") LocalDate date);

    int deleteAllByClazz_Id(Long classId);
}