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
import ru.itegor.antiplagiacode.comparison_result.dto.ComparisonResultDto;
import ru.itegor.antiplagiacode.comparison_result.dto.ComparisonResultResponseDto;
import ru.itegor.antiplagiacode.comparison_result.dto.MergeComparisonResultRequestDto;
import ru.itegor.antiplagiacode.comparison_result.service.ComparisonResultService;
import ru.itegor.antiplagiacode.exception.exceptions.NotFoundException;
import ru.itegor.antiplagiacode.file.FileEntity;
import ru.itegor.antiplagiacode.file.FileRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public List<ComparisonResultResponseDto> mergeMany(MergeComparisonResultRequestDto dto) {
        List<ComparisonResultEntity> comparisonResults = comparisonResultRepository.findAllByOriginalFile_Id(dto.getOriginalFileId());
        mergerResults(dto, comparisonResults);
        addMirrors(comparisonResults);
        return comparisonResultRepository.saveAll(comparisonResults).stream()
                .map(comparisonResultMapper::toComparisonResultResponseDto)
                .toList();
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

    private void mergerResults(MergeComparisonResultRequestDto dto, List<ComparisonResultEntity> comparisonResults) {
        FileEntity originalFileEntity = findFileById(dto.getOriginalFileId());
        List<FileEntity> fileEntities = fileRepository.findAllByTask_Id(originalFileEntity.getTask().getId());
        Long originalFileId = dto.getOriginalFileId();

        for (ComparisonResultDto comparisonResultDto : dto.getComparisonResults()) {
            Long comparedFileId = comparisonResultDto.getComparedFileId();
            Optional<ComparisonResultEntity> comparisonResultOpt =
                    findByOriginalAndComparedFileId(comparisonResults, originalFileId, comparedFileId);

            if (comparisonResultOpt.isPresent()) {
                ComparisonResultEntity comparisonResult = comparisonResultOpt.get();
                comparisonResult.setResult(comparisonResultDto.getResult());
            } else {
                ComparisonResultEntity comparisonResult = new ComparisonResultEntity();
                comparisonResult.setResult(comparisonResultDto.getResult());
                comparisonResult.setOriginalFile(originalFileEntity);
                comparisonResult.setComparedFile(findFileById(fileEntities, comparedFileId));
                comparisonResults.add(comparisonResult);
            }
        }
    }

    private ComparisonResultEntity findById(Long id) {
        return comparisonResultRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Comparison result with id `%s` not found".formatted(id)));
    }

    private FileEntity findFileById(Long id) {
        return fileRepository.findById(id).orElseThrow(() ->
                new NotFoundException("File with id `%s` not found".formatted(id)));
    }

    private FileEntity findFileById(List<FileEntity> fileEntities, Long id) {
        for (FileEntity fileEntity : fileEntities) {
            if (fileEntity.getId().equals(id)) {
                return fileEntity;
            }
        }
        throw new NotFoundException("File with id `%s` not found".formatted(id));
    }

    private Optional<ComparisonResultEntity> findByOriginalAndComparedFileId(List<ComparisonResultEntity> comparisonResults,
                                                                   Long originalFileId,
                                                                   Long comparedFileId) {
        for (ComparisonResultEntity comparisonResultEntity : comparisonResults) {
            if (comparisonResultEntity.getOriginalFile().getId().equals(originalFileId) &&
                    comparisonResultEntity.getComparedFile().getId().equals(comparedFileId)) {
                return Optional.of(comparisonResultEntity);
            }
        }
        return Optional.empty();
    }

    private void addMirrors(List<ComparisonResultEntity> comparisonResults) {
        List<ComparisonResultEntity> mirrors = findMirrors(comparisonResults);
        for (ComparisonResultEntity comparisonResult : comparisonResults) {
            Long originalFileId = comparisonResult.getOriginalFile().getId();
            Long comparedFileId = comparisonResult.getComparedFile().getId();

            Optional<ComparisonResultEntity> mirrorOpt = findByOriginalAndComparedFileId(
                    mirrors,
                    comparedFileId,
                    originalFileId
            );
            ComparisonResultEntity mirror = mirrorOpt.orElse(new ComparisonResultEntity());
            mirror.setResult(comparisonResult.getResult());
            mirror.setOriginalFile(comparisonResult.getComparedFile());
            mirror.setComparedFile(comparisonResult.getOriginalFile());
            if (mirrorOpt.isEmpty()) {
                mirrors.add(mirror);
            }
        }
        comparisonResults.addAll(mirrors);
    }

    private List<ComparisonResultEntity> findMirrors(List<ComparisonResultEntity> comparisonResults) {
        if (comparisonResults.isEmpty()) {
            return Collections.emptyList();
        }

        String values = String.join(",",
                Collections.nCopies(comparisonResults.size(), "(?, ?)")
        );

        String sql = "SELECT * FROM comparison_results " +
                "WHERE (original_file_id, compared_file_id) IN (VALUES " + values + ")";

        Query query = entityManager.createNativeQuery(sql, ComparisonResultEntity.class);

        int index = 1;
        for (ComparisonResultEntity result : comparisonResults) {
            query.setParameter(index++, result.getComparedFile().getId());
            query.setParameter(index++, result.getOriginalFile().getId());
        }

        return query.getResultList();
    }
}
