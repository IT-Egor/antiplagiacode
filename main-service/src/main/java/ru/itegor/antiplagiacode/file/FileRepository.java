package ru.itegor.antiplagiacode.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByStudent_IdAndTask_Id(Long studentId, Long taskId);
}