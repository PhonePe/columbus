package com.phonepe.platform.columbus.search.algorithms.correction.symspell;

import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.models.search.exceptions.ColumbusSearchErrorCode;
import com.phonepe.platform.columbus.search.algorithms.common.distance.Distance;
import com.phonepe.platform.columbus.search.algorithms.correction.Suggestion;
import com.phonepe.platform.columbus.search.algorithms.correction.Verbosity;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class UnigramCorrector {
    private final InMemoryDataHolder inMemoryDataHolder;
    private final SymSpellConfig symSpellConfig;
    private final Distance<String> stringDistance;

    public List<Suggestion> correctWord(String phrase, double maxEditDistance) {
        if (maxEditDistance > symSpellConfig.getMaxEditDistance()) {
            throw ColumbusException.error(ColumbusSearchErrorCode.LOOKUP_ERROR, "max Edit distance should be less than  global Max");
        } else if (maxEditDistance < 0) {
            maxEditDistance = symSpellConfig.getMaxEditDistance();
        }
        if (symSpellConfig.isLowerCaseTerms()) {
            phrase = phrase.toLowerCase();
        }
        return lookup(phrase, Verbosity.TOP, maxEditDistance);
    }

    @SuppressWarnings("java:S135")
    private List<Suggestion> lookup(final String phrase, final Verbosity verbosity, final double maxEditDistance) {
        List<Suggestion> suggestionItems = new ArrayList<>(symSpellConfig.getTopK());
        Set<String> consideredDeletes = new HashSet<>();
        Set<String> consideredSuggestions = new HashSet<>();
        int phraseLen = phrase.length();
        if ((phraseLen - maxEditDistance) > symSpellConfig.getMaxLength()) {
            return SymSpellUtils.earlyExit(
                suggestionItems, phrase, maxEditDistance, symSpellConfig.isIgnoreUnknown());
        }
        boolean exists = checkExistsAndEarlyExit(verbosity, suggestionItems, phrase);
        if (exists) {
            return SymSpellUtils.earlyExit(suggestionItems, phrase, maxEditDistance, symSpellConfig.isIgnoreUnknown());
        }
        consideredSuggestions.add(phrase);
        double bestEditDistanceFound = maxEditDistance;
        List<String> candidates = new ArrayList<>();
        final int phrasePrefixLen = calculatePhasePrefixLength(phraseLen, candidates, phrase);
        candidates.add(phrase);
        while (!candidates.isEmpty()) {
            String candidate = candidates.remove(0);
            int candidateLen = candidate.length();
            int lenDiff = phraseLen - candidateLen;
            if (lenDiff > bestEditDistanceFound) {
                break;
            }
            if (inMemoryDataHolder.getDeletes(candidate) != null) {
                CandidateContext candidateContext = CandidateContext.builder()
                    .candidate(candidate)
                    .phrase(phrase)
                    .bestEditDistanceFound(bestEditDistanceFound)
                    .maxEditDistance(maxEditDistance)
                    .verbosity(verbosity)
                    .phrasePrefixLen(phrasePrefixLen)
                    .build();
                bestEditDistanceFound = processDeletes(candidateContext, consideredSuggestions, suggestionItems);
            }
            if (lenDiff < maxEditDistance && candidateLen <= symSpellConfig.getPrefixLength()) {
                if (verbosity != Verbosity.ALL && lenDiff >= bestEditDistanceFound) {
                    continue;
                }
                List<String> newCandidates = getDeletes(candidate, consideredDeletes);
                candidates.addAll(newCandidates);
            }
        }
        return suggestionItems;
    }

    private List<String> getDeletes(String candidate, Set<String> consideredDeletes) {
        List<String> candidates = new ArrayList<>();
        int candidateLen = candidate.length();
        for (int i = 0; i < candidateLen; i++) {
            String delete = candidate.substring(0, i) + candidate.substring(i + 1, candidateLen);
            if (consideredDeletes.add(delete)) {
                candidates.add(delete);
            }
        }
        return candidates;
    }

    @SuppressWarnings("java:S135")
    private double processDeletes(CandidateContext candidateContext, Set<String> consideredSuggestions, List<Suggestion> suggestionItems) {
        String candidate = candidateContext.getCandidate();
        int candidateLen = candidate.length();
        String phrase = candidateContext.getPhrase();
        double maxEditDistance = candidateContext.getMaxEditDistance();
        double bestEditDistanceFound = candidateContext.getBestEditDistanceFound();
        Verbosity verbosity = candidateContext.getVerbosity();
        List<String> deletes = inMemoryDataHolder.getDeletes(candidate);
        int phraseLen = phrase.length();
        int phrasePrefixLen = candidateContext.getPhrasePrefixLen();
        for (String suggestion : deletes) {
            if (!checkValidOnPrfefixAndEquivalance(suggestion, phrase, candidate, bestEditDistanceFound, phrasePrefixLen)) {
                continue;
            }
            double distance;
            int minDistance;
            if (checkisEmptyOrSingular(candidateLen, suggestion.length())) {
                distance = distanceForEmptyOrSingularSuggestion(phrase, suggestion, candidateLen, phraseLen);
                if (!checkNoCommonSuggestionValid(distance, bestEditDistanceFound, consideredSuggestions, suggestion)) {
                    continue;
                }
            } else {
                minDistance = getMinDistanceOnPrefixbasis(maxEditDistance, candidate, phrase, suggestion);
                SuggestionContext suggestionContext = SuggestionContext.builder()
                    .candidate(candidate)
                    .maxEditDistance(maxEditDistance)
                    .phrase(phrase)
                    .minDistance(minDistance)
                    .suggestion(suggestion)
                    .verbosity(verbosity)
                    .build();
                if (checkInvalidSuggestion(suggestionContext, consideredSuggestions)) {
                    continue;
                }
                distance = stringDistance.distance(phrase, suggestion, bestEditDistanceFound);
                if (distance < 0) {
                    continue;
                }
            }
            bestEditDistanceFound = processSuggestion(distance, bestEditDistanceFound, verbosity, suggestionItems, suggestion);
        }
        return bestEditDistanceFound;
    }

    private double processSuggestion(double distance, double bestEditDistanceFound, Verbosity verbosity, List<Suggestion> suggestionItems, String suggestion) throws ColumbusException {
        if (SymSpellUtils.isLessOrEqualDouble(distance, bestEditDistanceFound, 0.01)) {
            Double suggestionCount = inMemoryDataHolder.getItemFrequency(suggestion);
            Suggestion si = new Suggestion(suggestion, distance, suggestionCount);
            if (!suggestionItems.isEmpty()) {
                if (verbosity == Verbosity.CLOSEST && distance < bestEditDistanceFound) {
                    suggestionItems.clear();
                } else if (verbosity == Verbosity.TOP) {
                    if (SymSpellUtils.isLessDouble(distance, bestEditDistanceFound, 0.01)
                        || suggestionCount > suggestionItems.get(0).getCount()) {
                        bestEditDistanceFound = distance;
                        suggestionItems.set(0, si);
                    }
                    return bestEditDistanceFound;
                }
            }

            if (verbosity != Verbosity.ALL) {
                bestEditDistanceFound = distance;
            }
            suggestionItems.add(si);
        }
        return bestEditDistanceFound;

    }

    private boolean checkInvalidSuggestion(SuggestionContext suggestionContext, Set<String> consideredSuggestions) {
        return isDistanceCalculationRequired(suggestionContext.getPhrase(), suggestionContext.getMaxEditDistance(),
            suggestionContext.getMinDistance(), suggestionContext.getSuggestion(),
            suggestionContext.getCandidate()) || !checkValidSuggestion(suggestionContext.getVerbosity(),
            suggestionContext.getCandidate(), suggestionContext.getCandidate().length(), suggestionContext.getSuggestion(), consideredSuggestions);
    }


    private boolean checkValidSuggestion(Verbosity verbosity, String candidate, int candidateLen, String suggestion, Set<String> consideredSuggestions) {
        return !(verbosity != Verbosity.ALL
            && !deleteInSuggestionPrefix(candidate, candidateLen,
            suggestion, suggestion.length()) || !consideredSuggestions
            .add(suggestion));
    }

    /**
     * Check whether all delete chars are present in the suggestion prefix in correct order, otherwise
     * this is just a hash collision
     */
    private boolean deleteInSuggestionPrefix(String delete, int deleteLen, String suggestion, int suggestionLen) {
        if (deleteLen == 0) {
            return true;
        }
        if (symSpellConfig.getPrefixLength() < suggestionLen) {
            suggestionLen = symSpellConfig.getPrefixLength();
        }

        int j = 0;
        for (int i = 0; i < deleteLen; i++) {
            char delChar = delete.charAt(i);
            while (j < suggestionLen && delChar != suggestion.charAt(j)) {
                j++;
            }
            if (j == suggestionLen) {
                return false;
            }
        }
        return true;
    }

    private boolean isDistanceCalculationRequired(String phrase, double maxEditDistance, int min,
                                                  String suggestion, String candidate) {
        return phrase.length() - maxEditDistance == candidate.length()
            && (min > 1
            && !(phrase.substring(phrase.length() + 1 - min)
            .equals(suggestion.substring(suggestion.length() + 1 - min))))
            || (min > 0
            && phrase.charAt(phrase.length() - min) != suggestion
            .charAt(suggestion.length() - min)
            && phrase.charAt(phrase.length() - min - 1) != suggestion
            .charAt(suggestion.length() - min)
            && phrase.charAt(phrase.length() - min) != suggestion
            .charAt(suggestion.length() - min - 1));
    }

    private int getMinDistanceOnPrefixbasis(double maxEditDistance, String candidate, String phrase, String suggestion) {
        if ((symSpellConfig.getPrefixLength() - maxEditDistance) == candidate.length()) {
            return
                Math.min(phrase.length(), suggestion.length()) - symSpellConfig.getPrefixLength();
        } else {
            return 0;
        }
    }

    private double distanceForEmptyOrSingularSuggestion(String phrase, String suggestion, int candidateLen, int phraseLen) {
        if (candidateLen == 0) {
            return Math.max(phraseLen, suggestion.length());
        }
        return phrase.indexOf(suggestion.charAt(0)) < 0 ? phraseLen : phraseLen - 1;
    }


    private boolean checkNoCommonSuggestionValid(double distance, double maxEditDistance2, Set<String> consideredSuggestions, String suggestion) {
        return !(distance > maxEditDistance2 || !consideredSuggestions.add(suggestion));
    }

    private boolean checkisEmptyOrSingular(int candidateLen, int suggestionLength) {
        return candidateLen == 0 || suggestionLength == 1;
    }


    private boolean checkValidOnPrfefixAndEquivalance(String suggestion, String phrase, String candidate, double bestEditDistanceFound, int phrasePrefixLen) {
        return !(filterOnEquivalance(suggestion, phrase, candidate, bestEditDistanceFound)
            ||
            filterOnPrefixLen(suggestion.length(), symSpellConfig.getPrefixLength(),
                phrasePrefixLen, candidate.length(), bestEditDistanceFound));
    }

    private boolean filterOnEquivalance(String delete, String phrase, String candidate,
                                        double bestEditDistanceFound) {

        return (delete.equals(phrase)
            || (Math.abs(delete.length() - phrase.length()) > bestEditDistanceFound)
            || (delete.length() < candidate.length()) || (delete.length() == candidate.length()
            && !delete.equals(candidate)));
    }

    private boolean filterOnPrefixLen(int suggestionLen, int prefixLen, int phrasePrefixLen,
                                      int candidateLen,
                                      double bestEditDistanceFound) {
        int suggestionPrefixLen = Math.min(suggestionLen, prefixLen);
        return (suggestionPrefixLen > phrasePrefixLen
            && (suggestionPrefixLen - candidateLen) > bestEditDistanceFound);
    }

    private int calculatePhasePrefixLength(int phraseLen, List<String> candidates, String phrase) {
        int phrasePrefixLen;
        if (phraseLen > symSpellConfig.getPrefixLength()) {
            phrasePrefixLen = symSpellConfig.getPrefixLength();
            candidates.add(phrase.substring(0, phrasePrefixLen));
        } else {
            phrasePrefixLen = phraseLen;
        }
        return phrasePrefixLen;
    }

    private boolean checkExistsAndEarlyExit(Verbosity verbosity, List<Suggestion> suggestionItems, String phrase) throws ColumbusException {
        Double frequency = inMemoryDataHolder.getItemFrequency(phrase);
        if (frequency != null) {
            suggestionItems.add(new Suggestion(phrase, 0, frequency));
        }
        return verbosity != Verbosity.ALL && frequency != null;
    }
}
