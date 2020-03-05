package no.hials.page.replacement;

public class Main {
    public static void main(String[] args) {
        String ref = "7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1";
        OptimalReplacement alogrithm = new OptimalReplacement();
        alogrithm.setup(3);
        alogrithm.process(ref);
    }
}
