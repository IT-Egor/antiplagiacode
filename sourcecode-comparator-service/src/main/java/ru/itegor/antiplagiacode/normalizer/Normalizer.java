package ru.itegor.antiplagiacode.normalizer;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public abstract class Normalizer {
    protected final String commentSymbol;
    protected final String multilineOpenCommentSymbol;
    protected final String multilineCloseCommentSymbol;
    protected final List<String> fileExtention;

    public List<String> normalize(List<String> sourceCode) {
        sourceCode = removeMultilineComments(sourceCode);
        sourceCode = removeComments(sourceCode);
        sourceCode = removeWhitespace(sourceCode);
        sourceCode = removeEmptyLines(sourceCode);
        sourceCode = toLowerCase(sourceCode);
        return sourceCode;
    }

    protected List<String> removeComments(List<String> sourceCode) {
        List<String> response = new ArrayList<>(sourceCode.size());
        for (String line : sourceCode) {
            if (line.contains(commentSymbol) && !isCommentInQuotation(line)) {
                response.add(line.substring(0, line.indexOf(commentSymbol)));
            } else {
                response.add(line);
            }
        }
        return response;
    }

    protected boolean isCommentInQuotation(String line) {
        if (line.contains("\"")) {
            int firstQuoIndex = line.indexOf("\"");
            int lastQuoIndex = line.lastIndexOf("\"") + 1;
            line = line.substring(firstQuoIndex, lastQuoIndex);
        } else if (line.contains("\'")) {
            int firstQuoIndex = line.indexOf("\'");
            int lastQuoIndex = line.lastIndexOf("\'") + 1;
            line = line.substring(firstQuoIndex, lastQuoIndex);
        } else {
            return false;
        }
        return line.contains(commentSymbol) || line.contains(multilineOpenCommentSymbol) || line.contains(multilineCloseCommentSymbol);
    }

    protected List<String> removeMultilineComments(List<String> sourceCode) {
        List<String> response = new ArrayList<>(sourceCode.size());
        boolean isComment = false;
        for (String line : sourceCode) {
            if (!isComment && line.contains(multilineOpenCommentSymbol) && !isCommentInQuotation(line)) {
                line = line.substring(0, line.indexOf(multilineOpenCommentSymbol));
                isComment = true;
                if (!line.isBlank()) {
                    response.add(line);
                }
            }
            if (isComment && line.contains(multilineCloseCommentSymbol) && !isCommentInQuotation(line)) {
                line = line.substring(line.lastIndexOf(multilineCloseCommentSymbol) + multilineCloseCommentSymbol.length());
                isComment = false;
            }
            if (!isComment) {
                response.add(line);
            }
        }
        return response;
    }

    protected List<String> removeWhitespace(List<String> sourceCode) {
        List<String> response = new ArrayList<>(sourceCode.size());
        for (String line : sourceCode) {
            line = line.replaceAll("\\s+", " ");
            if (line.startsWith(" ")) {
                line = line.substring(1);
            }
            response.add(line);
        }
        return response;
    }

    protected List<String> removeEmptyLines(List<String> sourceCode) {
        return sourceCode.stream().filter(line -> !line.isBlank()).collect(Collectors.toList());
    }

    protected List<String> toLowerCase(List<String> sourceCode) {
        return sourceCode.stream().map(String::toLowerCase).collect(Collectors.toList());
    }
}
