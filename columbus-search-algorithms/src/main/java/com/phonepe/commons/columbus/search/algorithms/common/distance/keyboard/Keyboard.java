package com.phonepe.commons.columbus.search.algorithms.common.distance.keyboard;

import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class Keyboard {

    private final Map<String, Connection> connections = new HashMap<>();

    public Keyboard(String... rows) {
        if (rows.length > 0) {
            connectAdjacent(rows[0]);
        }
        for (int i = 1; i < rows.length; i++) {
            connectAdjacent(rows[i]);
            connectDiagonal(rows[i - 1], rows[i]);
        }
    }

    public void connectAdjacent(final String row) {
        for (int i = 1; i < row.length(); i++) {
            connect(row.charAt(i - 1), row.charAt(i), Connection.ADJACENT);
        }
    }

    public void connectDiagonal(final String above, final String below) {
        var aboveLength = above.length();
        var length = Math.min(aboveLength, below.length());
        for (int i = 0; i < length; i++) {
            connectAll(below.charAt(i), above.substring(i, Math.min(aboveLength, i + 2)), Connection.DIAGONAL);
        }
    }

    public void connectAll(final char from, final String to, final Connection connection) {
        for (char x : to.toCharArray()) {
            connect(from, x, connection);
        }
    }

    public void connect(final char from, final char to, final Connection connection) {
        connections.put(join(from, to), connection);
        connections.put(join(to, from), connection);
    }

    public Connection getConnection(final char from, final char to) {
        if (from == to) {
            return Connection.SAME;
        }
        return connections.getOrDefault(join(from, to), Connection.DISTANT);
    }

    private static String join(final char left, final char right) {
        return left + "-" + right;
    }

    public enum Connection {
        SAME,
        ADJACENT,
        DIAGONAL,
        DISTANT,
    }
}
