package ru.itegor.antiplagiacode.normalizer.impl;

import org.springframework.stereotype.Component;
import ru.itegor.antiplagiacode.normalizer.Normalizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class PythonNormalizer extends Normalizer {
    public PythonNormalizer() {
        super("#", "'''", "'''", List.of("py"));
    }

    @Override
    protected List<String> removeWhitespace(List<String> sourceCode) {
        List<String> response = new LinkedList<>();
        for (String line : sourceCode) {
            int lineStartIndex = findLineStartIndex(line);
            line = line.substring(0, lineStartIndex)
                    + line.substring(lineStartIndex).replaceAll("\\s+", " ");
            response.add(line);
        }
        return response;
    }

    @Override
    protected boolean isCommentInQuotation(String line) {
        if (line.contains("\"")) {
            int firstQuoIndex = line.indexOf("\"");
            int lastQuoIndex = line.lastIndexOf("\"") + 1;
            line = line.substring(firstQuoIndex, lastQuoIndex);
            return line.contains(commentSymbol) || line.contains(multilineOpenCommentSymbol);
        } else if (line.contains("\'")) {
            int firstQuoIndex = line.indexOf("\'");
            int lastQuoIndex = line.lastIndexOf("\'") + 1;
            line = line.substring(firstQuoIndex, lastQuoIndex);
            return line.contains(commentSymbol);
        } else {
            return false;
        }
    }

    @Override
    protected List<String> removeMultilineComments(List<String> sourceCode) {
        List<String> response = new ArrayList<>(sourceCode.size());
        boolean isComment = false;
        for (String line : sourceCode) {
            if (line.contains(multilineOpenCommentSymbol) && !isCommentInQuotation(line)) {
                isComment = !isComment;
            }
            if (!isComment && (!line.contains(multilineOpenCommentSymbol) || isCommentInQuotation(line))) {
                response.add(line);
            }
        }
        return response;
    }

    private int findLineStartIndex(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (!Character.isWhitespace(line.charAt(i))) {
                return i;
            }
        }
        return 0;
    }
}
