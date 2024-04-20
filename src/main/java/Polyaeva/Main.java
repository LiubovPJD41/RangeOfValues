package Polyaeva;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }

        long startTs = System.currentTimeMillis(); // start time
        ExecutorService threadPool = Executors.newFixedThreadPool(texts.length);
        List<Future<Integer>> futures = new ArrayList<>(texts.length);

        for (String text : texts) {
            Callable<Integer> callable = new Text(text);
            final Future<Integer> task = threadPool.submit(callable);
            futures.add(task);
        }
        Integer result = 0;
        for (Future<Integer> future :
                futures) {
            if (future.get() > result) {
                result = future.get();
            }
        }
        long endTs = System.currentTimeMillis(); // end time

        System.out.println("Time: " + (endTs - startTs) + "ms");
        System.out.println("Max value is " + result);
        threadPool.shutdown();
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}