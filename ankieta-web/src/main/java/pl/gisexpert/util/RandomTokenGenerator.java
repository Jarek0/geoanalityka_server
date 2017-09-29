package pl.gisexpert.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class RandomTokenGenerator {

    private final SecureRandom random = new SecureRandom();

    public String nextToken() {
        return new BigInteger(130, random).toString(32);
    }

}
