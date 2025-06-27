package ru.itegor.antiplagiacode.normalizer.impl;

import org.springframework.stereotype.Component;
import ru.itegor.antiplagiacode.normalizer.Normalizer;

import java.util.List;

@Component
public class CppNormalizer extends Normalizer {
    public CppNormalizer() {
        super("//", "/*", "*/", List.of("cpp"));
    }
}
