package com.wright;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

import static java.lang.Math.log10;
import static java.lang.Math.max;

class Tester {
    private static class Pair<Key, Value> implements Serializable {
        private Key key;
        private Value value;

        Key getKey() {
            return key;
        }

        Value getValue() {
            return value;
        }

        Pair(Key key, Value value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 31 * hash + (key != null ? key.hashCode() : 0);
            hash = 31 * hash + (value != null ? value.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof Pair) {
                Pair pair = (Pair) o;
                if (!Objects.equals(key, pair.key)) return false;
                if (!Objects.equals(value, pair.value)) return false;
                return true;
            }
            return false;
        }
    }

    static void test(int testsCount, int testsSize) {
        ArrayList<Pair<Consumer<int[]>, String>> toBeTested = new ArrayList<Pair<Consumer<int[]>, String>>() {
            {
                add(new Pair<>(BubbleSort::sort, "bubble sort"));
                add(new Pair<>(SelectSort::sort, "select sort"));
                add(new Pair<>(InsertionSort::sort, "insert sort"));
                add(new Pair<>(MergeSort::sort, "merge sort"));
                add(new Pair<>(QuickSort::sort, "quick sort"));
            }
        };
        testThis(toBeTested, testsCount, testsSize);
    }

    private static void testThis(ArrayList<Pair<Consumer<int[]>, String>> toBeTested, int testsCount, int testsSize) {

        Random random = new Random();
        int maxStringLength = 0;
        for(Pair<Consumer<int[]>, String> function : toBeTested)
            maxStringLength = max(maxStringLength, function.getValue().length());
        for(Pair<Consumer<int[]>, String> function : toBeTested) {
            int correct = 0;
            long startTime = System.currentTimeMillis();
            for(int i = 0; i < testsCount; ++i) {
                int[] test = new int[testsSize];
                for (int j = 0; j < test.length; ++j)
                    test[j] = random.nextInt();

                int[] sorted = new int[test.length];
                System.arraycopy(test, 0, sorted, 0, test.length);
                Arrays.sort(sorted);

                int[] toBeSorted = new int[test.length];
                System.arraycopy(test, 0, toBeSorted, 0, test.length);
                function.getKey().accept(toBeSorted);

                if (Arrays.equals(sorted, toBeSorted))
                    ++correct;
            }
            System.out.println(function.getValue() + ":" + new String(new char[maxStringLength - function.getValue().length() + 1]).replace('\0', ' ') + String.format("%1$" + (((int) log10(testsCount)) + 1) + "s", correct) + "/" + testsCount + " " + (correct == testsCount ? "[ OK ]" : "[FAIL]") + " (" + (System.currentTimeMillis() - startTime) + "ms)");
        }
    }
}
