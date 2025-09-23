package com.phonepe.commons.columbus.search.algorithms.common.distance.string;

import com.phonepe.commons.columbus.search.algorithms.common.distance.Distance;
import lombok.Builder;
import lombok.NonNull;

import java.util.Objects;

@Builder
@SuppressWarnings("java:S1170") // Sonar does not identify Lombok's builder default
public class WeightedDamerauLevenshteinDistance implements Distance<String> {

    @Builder.Default
    private final double deletionWeight = 0.8f;
    @Builder.Default
    private final double insertionWeight = 1.01f;
    @Builder.Default
    private final double replaceWeight = 0.9f;
    @Builder.Default
    private final double transpositionWeight = 0.7f;
    @NonNull
    @Builder.Default
    private final Distance<Character> charDistance = (from, to) -> Objects.equals(from, to) ? 0 : 1;

    @Override
    public double distance(@NonNull String from, @NonNull String to) {
        if (from.equals(to)) {
            return 0;
        }

        if (from.isEmpty()) {
            return to.length() * insertionWeight;
        } else if (to.isEmpty()) {
            return from.length() * deletionWeight;
        }

        double[][] distance = new double[from.length() + 1][to.length() + 1];

        for (int i = 1; i <= from.length(); i++) {
            distance[i][0] = distance[i - 1][0] + deletionWeight;
        }
        for (int i = 1; i <= to.length(); i++) {
            distance[0][i] = distance[0][i - 1] + insertionWeight;
        }

        for (int i = 1; i <= from.length(); i++) {
            for (int j = 1; j <= to.length(); j++) {
                // Insert / delete
                var bestDistance = Math.min(distance[i - 1][j] + deletionWeight, distance[i][j - 1] + insertionWeight);
                // Replace
                var replaceCost = replaceWeight * charDistance.distance(from.charAt(i - 1), to.charAt(j - 1));
                bestDistance = Math.min(bestDistance, distance[i - 1][j - 1] + replaceCost);
                // Transpose
                if (i > 1 && j > 1 && from.charAt(i - 1) == to.charAt(j - 2) && from.charAt(i - 2) == to.charAt(
                        j - 1)) {
                    bestDistance = Math.min(bestDistance, distance[i - 2][j - 2] + transpositionWeight);
                }
                distance[i][j] = bestDistance;
            }
        }
        return distance[from.length()][to.length()];
    }

    @Override
    public double distance(@NonNull final String from, @NonNull final String to, final double max) {
        final var distance = distance(from, to);
        return distance > max ? -1 : distance;
    }

    @SuppressWarnings("unused")
    public static class WeightedDamerauLevenshteinDistanceBuilder {

        public WeightedDamerauLevenshteinDistanceBuilder unitWeights() {
            return insertionWeight(1)
                    .deletionWeight(1)
                    .replaceWeight(1)
                    .transpositionWeight(1);
        }
    }
}
