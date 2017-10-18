package fr.univartois.sonargo.toolkit;

import fr.univartois.sonargo.coverage.AntPathMatcher;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Arrays;
import java.util.List;

public class PathToolkit {
    private static final Logger LOGGER = Loggers.get(PathToolkit.class);

    public static boolean isExcludedPath(String path, String patterns) {
        List<String> patternList = Arrays.asList(StringUtils.split(patterns, ","));
        AntPathMatcher matcher = new AntPathMatcher.Builder().build();
        boolean match = patternList.stream().anyMatch(pattern -> matcher.isMatch(pattern.trim(), path));
        return match;
    }

}
