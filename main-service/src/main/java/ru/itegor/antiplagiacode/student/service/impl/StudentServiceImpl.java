package ru.itegor.antiplagiacode.student.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.itegor.antiplagiacode.clazz.ClassEntity;
import ru.itegor.antiplagiacode.clazz.ClassRepository;
import ru.itegor.antiplagiacode.student.StudentClassEntity;
import ru.itegor.antiplagiacode.student.StudentMapper;
import ru.itegor.antiplagiacode.student.StudentRepository;
import ru.itegor.antiplagiacode.student.StudentResponseDto;
import ru.itegor.antiplagiacode.student.service.StudentService;
import ru.itegor.antiplagiacode.user.UserEntity;
import ru.itegor.antiplagiacode.user.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final ClassRepository classRepository;

    @Override
    public Page<StudentResponseDto> getAll(Pageable pageable) {
        Page<StudentClassEntity> studentClassEntities = studentRepository.findAll(pageable);
        return studentClassEntities.map(studentMapper::toStudentResponseDto);
    }

    @Override
    public StudentResponseDto getOneById(Long id) {
        StudentClassEntity studentClassEntityOptional = findById(id);
        return studentMapper.toStudentResponseDto(studentClassEntityOptional);
    }

    @Override
    public StudentResponseDto getOneByStudentIdAndClassId(Long studentId, Long classId) {
        StudentClassEntity studentClassEntityOptional = findByStudentIdAndClassId(studentId, classId);
        return studentMapper.toStudentResponseDto(studentClassEntityOptional);
    }

    @Override
    public List<Long> getStudentsByClassId(Long classId) {
        return studentRepository.findByClazz_Id(classId).stream().map(StudentClassEntity::getId).toList();
    }

    @Override
    public StudentResponseDto addStudentToClass(Long studentId, Long classId) {
        UserEntity student = findStudentById(studentId);
        checkUserRole(student);
        ClassEntity clazz = findClassById(classId);

        StudentClassEntity studentClass = new StudentClassEntity();
        studentClass.setStudent(student);
        studentClass.setClazz(clazz);

        return studentMapper.toStudentResponseDto(studentRepository.save(studentClass));
    }

    @Override
    public StudentResponseDto changeStudentClass(Long studentId, Long classId) {
        StudentClassEntity studentClass = findByStudentId(studentId);
        ClassEntity clazz = findClassById(classId);
        studentClass.setClazz(clazz);
        return studentMapper.toStudentResponseDto(studentRepository.save(studentClass));
    }

    @Override
    public int changeStudentsClasses(List<Long> studentIds, Long classId) {
        return studentRepository.updateAllByStudentIdIn(studentIds, classId);
    }

    @Override
    public int changeClassOfAllStudents(Long oldClassId, Long newClassId) {
        List<Long> studentIds = studentRepository.findByClazz_Id(oldClassId).stream().map(StudentClassEntity::getId).toList();
        return changeStudentsClasses(studentIds, newClassId);
    }

    @Override
    public StudentResponseDto delete(Long id) {
        StudentClassEntity studentClassEntity = studentRepository.findById(id).orElse(null);
        if (studentClassEntity != null) {
            studentRepository.delete(studentClassEntity);
        }
        return studentMapper.toStudentResponseDto(studentClassEntity);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        studentRepository.deleteAllById(ids);
    }

    private StudentClassEntity findByStudentIdAndClassId(Long studentId, Long classId) {
        return studentRepository.findByStudent_IdAndClazz_Id(studentId, classId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Entity with student id `%s` and class id `%s` not found".formatted(studentId, classId)));
    }

    private StudentClassEntity findById(Long id) {
        return studentRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    private StudentClassEntity findByStudentId(Long studentId) {
        return studentRepository.findByStudent_Id(studentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(studentId)));
    }

    private UserEntity findStudentById(Long studentId) {
        return userRepository.findById(studentId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(studentId)));
    }

    private ClassEntity findClassById(Long classId) {
        return classRepository.findById(classId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(classId)));
    }

    private void checkUserRole(UserEntity user) {
        if (user.getRole() != UserEntity.Role.STUDENT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id `%s` is not a student".formatted(user.getId()));
        }
    }
}
