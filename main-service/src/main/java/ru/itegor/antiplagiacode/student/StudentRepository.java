package ru.itegor.antiplagiacode.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentClassEntity, Long> {
    Optional<StudentClassEntity> findByStudent_IdAndClazz_Id(Long studentId, Long classId);

    Optional<StudentClassEntity> findByStudent_Id(Long studentId);

    Collection<StudentClassEntity> findByClazz_Id(Long classId);

    @Modifying
    @Query("UPDATE StudentClassEntity sc SET sc.clazz.id = :clazzId WHERE sc.student.id IN :studentIds")
    int updateAllByStudentIdIn(
            @Param("studentIds") Collection<Long> studentIds,
            @Param("clazzId") Long clazzId
    );

    boolean existsByStudent_IdAndClazz_Id(Long studentId, Long classId);
}