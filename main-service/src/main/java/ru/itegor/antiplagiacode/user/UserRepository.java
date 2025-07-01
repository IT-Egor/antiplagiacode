package ru.itegor.antiplagiacode.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.id IN :ids AND u.role = 'STUDENT'")
    Collection<UserEntity> findAllStudentsById(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.role = 'STUDENT'")
    Optional<UserEntity> findStudentById(@Param("id") Long id);
}