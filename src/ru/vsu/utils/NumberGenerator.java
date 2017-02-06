package ru.vsu.utils;

import java.util.Random;

/**
 * Created by Evgeniy Evzerov on 06.02.17.
 * VIstar
 */
public class NumberGenerator {

    public static int generatePrime() {

        int num;

        Random rand = new Random(); // generatePrime a random number
        num = rand.nextInt(10000) + 1;

        while (!isPrime(num)) {
            num = rand.nextInt(10000) + 1;
        }

        return num;  // print the number
    }

    public static int generateNonPrime() {

        int num;

        Random rand = new Random(); // generatePrime a random number
        num = rand.nextInt(10000) + 1;

        return num;
    }

    private static boolean isPrime(int inputNum) {

        if (inputNum <= 3 || inputNum % 2 == 0) {
            return inputNum == 2 || inputNum == 3;
        }

        int divisor = 3;

        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0)) {
            divisor += 2;
        }

        return inputNum % divisor != 0;
    }
}