package ru.itegor.antiplagiacode.comparison_result.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itegor.antiplagiacode.comparison_result.ComparisonResultEntity;
import ru.itegor.antiplagiacode.comparison_result.ComparisonResultMapper;
import ru.itegor.antiplagiacode.comparison_result.ComparisonResultRepository;
import ru.itegor.antiplagiacode.comparison_result.dto.ComparisonResultResponseDto;
import ru.itegor.antiplagiacode.comparison_result.dto.CreateComparisonResultRequestDto;
import ru.itegor.antiplagiacode.comparison_result.dto.UpdateComparisonResultRequestDto;
import ru.itegor.antiplagiacode.comparison_result.service.ComparisonResultService;
import ru.itegor.antiplagiacode.exception.exceptions.BadRequestException;
import ru.itegor.antiplagiacode.exception.exceptions.NotFoundException;
import ru.itegor.antiplagiacode.file.FileEntity;
import ru.itegor.antiplagiacode.file.FileRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ComparisonResultServiceImpl implements ComparisonResultService {
    private final ComparisonResultMapper comparisonResultMapper;
    private final ComparisonResultRepository comparisonResultRepository;
    private final FileRepository fileRepository;
    private final EntityManager entityManager;

    @Override
    public Page<ComparisonResultResponseDto> getAll(Pageable pageable) {
        Page<ComparisonResultEntity> comparisonResults = comparisonResultRepository.findAll(pageable);
        return comparisonResults.map(comparisonResultMapper::toComparisonResultResponseDto);
    }

    @Override
    public ComparisonResultResponseDto getOne(Long id) {
        return comparisonResultMapper.toComparisonResultResponseDto(findById(id));
    }

    @Override
    public List<ComparisonResultResponseDto> getAllByFileId(Long id) {
        Collection<ComparisonResultEntity> comparisonResults = comparisonResultRepository.findAllByOriginalFile_Id(id);
        return comparisonResults.stream()
                .map(comparisonResultMapper::toComparisonResultResponseDto)
                .toList();
    }

    @Override
    public List<ComparisonResultResponseDto> createMany(List<CreateComparisonResultRequestDto> dto) {
        checkThatAllFilesExistsById(
                dto.stream().map(CreateComparisonResultRequestDto::getOriginalFileId).collect(Collectors.toSet()),
                dto.stream().map(CreateComparisonResultRequestDto::getComparedFileId).collect(Collectors.toSet())
        );
        List<ComparisonResultEntity> comparisonResults = dto.stream().map(comparisonResultMapper::toEntity).toList();
        comparisonResults = addMirrors(comparisonResults);
        List<ComparisonResultEntity> resultComparisonResultEntity = comparisonResultRepository.saveAll(comparisonResults);
        if (resultComparisonResultEntity.size() != dto.size() * 2) {
            throw new InternalError("Error while creating comparison results");
        }
        return resultComparisonResultEntity.stream().map(comparisonResultMapper::toComparisonResultResponseDto).toList();
    }

    @Override
    public List<ComparisonResultResponseDto> patchMany(List<UpdateComparisonResultRequestDto> dtos) {
        List<ComparisonResultEntity> comparisonResults = comparisonResultRepository.findAllById(
                dtos.stream().map(UpdateComparisonResultRequestDto::getId).toList());
        if (comparisonResults.isEmpty()) {
            throw new NotFoundException("Comparison result with some ids not found");
        }

        checkThatAllFilesExistsById(
                dtos.stream().map(UpdateComparisonResultRequestDto::getOriginalFileId).collect(Collectors.toSet()),
                dtos.stream().map(UpdateComparisonResultRequestDto::getComparedFileId).collect(Collectors.toSet())
        );
        for (ComparisonResultEntity comparisonResult : comparisonResults) {
            UpdateComparisonResultRequestDto dto = findUpdateDtoById(dtos, comparisonResult.getId());
            comparisonResultMapper.updateWithNull(dto, comparisonResult);
        }

        comparisonResults = updateMirrors(comparisonResults);
        List<ComparisonResultEntity> resultComparisonResult = comparisonResultRepository.saveAll(comparisonResults);
        if (resultComparisonResult.size() != dtos.size() * 2) {
            throw new InternalError("Error while patching comparison results");
        }
        return resultComparisonResult.stream().map(comparisonResultMapper::toComparisonResultResponseDto).toList();
    }

    @Override
    public void deleteManyByOriginalFileId(Long id) {
        List<ComparisonResultEntity> comparisonResults = comparisonResultRepository.findAllByOriginalFile_Id(id);
        List<ComparisonResultEntity> mirrors = findMirrors(comparisonResults);
        comparisonResults.addAll(mirrors);
        comparisonResultRepository.deleteAll(comparisonResults);
    }

    @Override
    public void deleteMany(List<Long> ids) {
        comparisonResultRepository.deleteAllById(ids);
    }

    private ComparisonResultEntity findById(Long id) {
        return comparisonResultRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Comparison result with id `%s` not found".formatted(id)));
    }

    private void checkThatAllFilesExistsById(Set<Long> originalFilesIds, Set<Long> comparedFilesIds) {
        List<FileEntity> originalFiles = fileRepository.findAllById(originalFilesIds);
        List<FileEntity> comparedFiles = fileRepository.findAllById(comparedFilesIds);
        if (originalFiles.size() != originalFilesIds.size()
                || comparedFiles.size() != comparedFilesIds.size()
                || originalFiles.isEmpty()
                || comparedFiles.isEmpty()) {
            throw new NotFoundException("Some of the files were not found");
        }
    }

    private UpdateComparisonResultRequestDto findUpdateDtoById(List<UpdateComparisonResultRequestDto> dtos, Long id) {
        int count = 0;
        UpdateComparisonResultRequestDto updateDto = null;
        for (UpdateComparisonResultRequestDto dto : dtos) {
            if (dto.getId().equals(id)) {
                count++;
                updateDto = dto;
            }
        }
        if (count == 0) {
            throw new NotFoundException("Comparison result with id `%s` not found".formatted(id));
        } else if (count > 1) {
            throw new BadRequestException("Comparison result with id `%s` found more than once".formatted(id));
        } else {
            return updateDto;
        }
    }

    private List<ComparisonResultEntity> addMirrors(List<ComparisonResultEntity> comparisonResults) {
        List<ComparisonResultEntity> mirrorResults = new ArrayList<>(comparisonResults);
        for (ComparisonResultEntity result : comparisonResults) {
            ComparisonResultEntity mirrorResult = new ComparisonResultEntity();
            mirrorResult.setId(result.getId());
            mirrorResult.setOriginalFile(result.getComparedFile());
            mirrorResult.setComparedFile(result.getOriginalFile());
            mirrorResult.setResult(result.getResult());
            mirrorResults.add(mirrorResult);
        }
        return mirrorResults;
    }

    private List<ComparisonResultEntity> updateMirrors(List<ComparisonResultEntity> comparisonResults) {
        List<ComparisonResultEntity> mirrorResults = new ArrayList<>(comparisonResults);
        List<ComparisonResultEntity> mirrors = findMirrors(comparisonResults);
        if (mirrorResults.size() != mirrors.size()) {
            throw new NotFoundException("Some comparison results where not found");
        }
        for (ComparisonResultEntity result : comparisonResults) {
            ComparisonResultEntity mirrorResult = findByOriginalFileIdAndComparedFileIdInMirrors(
                    result.getComparedFile().getId(),
                    result.getOriginalFile().getId(),
                    mirrors);
            mirrorResult.setResult(result.getResult());
            mirrorResults.add(mirrorResult);
        }
        return mirrorResults;
    }

    private ComparisonResultEntity findByOriginalFileIdAndComparedFileIdInMirrors(Long originalFileId,
                                                                                  Long comparedFileId,
                                                                                  List<ComparisonResultEntity> mirrors) {
        int count = 0;
        ComparisonResultEntity mirrorResult = null;
        for (ComparisonResultEntity mirror : mirrors) {
            if (mirror.getOriginalFile().getId().equals(originalFileId)
                    && mirror.getComparedFile().getId().equals(comparedFileId)) {
                count++;
                mirrorResult = mirror;
            }
        }
        if (count != 1) {
            throw new BadRequestException("Comparison result with originalFileId `%s` and comparedFileId `%s` not found or found more than once");
        }
        return mirrorResult;
    }

    private List<ComparisonResultEntity> findMirrors(List<ComparisonResultEntity> comparisonResults) {
        String values = comparisonResults.stream()
                .map(r -> String.format("(%d, %d)", r.getComparedFile().getId(), r.getOriginalFile().getId()))
                .collect(Collectors.joining(","));

        Query query = entityManager.createNativeQuery(
                "SELECT cr.* FROM comparison_results cr " +
                        "WHERE (cr.original_file_id, cr.compared_file_id) " +
                        "IN (" + values + ")",
                ComparisonResultEntity.class
        );
        return query.getResultList();
    }
}
