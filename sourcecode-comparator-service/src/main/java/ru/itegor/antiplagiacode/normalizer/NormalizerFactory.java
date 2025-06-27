package ru.itegor.antiplagiacode.normalizer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.itegor.antiplagiacode.exception.NormalizerException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NormalizerFactory {
    private final List<Normalizer> normalizers;

    public Normalizer getNormalizer(String fileExtension) {
        for (Normalizer normalizer : normalizers) {
            if (normalizer.getFileExtention().contains(fileExtension)) {
                return normalizer;
            }
        }
        throw new NormalizerException("The extension `%s` is not supported".formatted(fileExtension));
    }
}
