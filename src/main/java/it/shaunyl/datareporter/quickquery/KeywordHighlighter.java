package it.shaunyl.datareporter.quickquery;

import org.springframework.stereotype.Service;

/**
 *
 * @author Filippo Testino
 */
@Service
public class KeywordHighlighter {

    private static final String[] SQL_KEYWORDS = { "null", "not", "max", "min", "avg", "group", "sum", "count", "asc", "desc", "dimension", "partition", "full", "left" ,"right", "outer", "inner" ,"using", "on" ,"join", "or", "only", "between", "and", "with", "as", "select", "from", "where", "order", "by" , "all", "distinct", "unique", "having", "union", "intersect", "minus", "over" };

    public static final String KEYWORDS_REGEX;
    
    static {
        StringBuilder buff = new StringBuilder("(");
        for (String keyword : SQL_KEYWORDS) {
            buff.append("\\b").append(keyword).append("\\b").append("|");
        }
        buff.deleteCharAt(buff.length() - 1).append(")");
        KEYWORDS_REGEX = buff.toString();
    }
}
