package ru.hse.autocode.utils.calcers;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.annotations.Nullable;

import java.util.Set;

/**
 * Class for calculating method connectivity
 */
public class ConnectivityCalculator {
    /**
     * Method for calculating methods connectivity
     */
    public static int calcConnectivity(String code, Set<String> members, @Nullable String methodName) {
        int res = 0;
        for (String member: members) {
            res += StringUtils.countMatches(code, member);
        }

        if (methodName != null) {
            res -= StringUtils.countMatches(code, methodName); // self-calls
        }

        return res;
    }
}
