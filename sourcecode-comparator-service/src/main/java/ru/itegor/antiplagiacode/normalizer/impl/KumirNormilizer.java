package ru.itegor.antiplagiacode.normalizer.impl;

import org.springframework.stereotype.Component;
import ru.itegor.antiplagiacode.normalizer.Normalizer;

import java.util.List;

@Component
public class KumirNormilizer extends Normalizer {
    public KumirNormilizer() {
        super("|", "|", "|", List.of("kum"));
    }

    @Override
    public List<String> normalize(List<String> sourceCode) {
        sourceCode = removeComments(sourceCode);
        sourceCode = removeWhitespace(sourceCode);
        sourceCode = removeEmptyLines(sourceCode);
        sourceCode = toLowerCase(sourceCode);
        return sourceCode;
    }
}
