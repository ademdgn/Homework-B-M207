package edu.estu;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException, CmdLineException {

        Options options = new Options();
        CmdLineParser parser = new CmdLineParser(options);
        parser.parseArgument(args);
        List<String> words = getWords(options.unique, options.fileNames);
        Map<String, Integer> wordCount = countWords(words);
        switch (options.task.toLowerCase()) {
            case "numoftokens":
                System.out.println("Number of Tokens: " +words.size());
                break;
            case "frequentterms":
                printFrequentTerms(wordCount, options.topN,options.reverse);
                break;
            case "termsstartwith":
                ArrayList<String> terms = termsStartWith(wordCount, options.start,options.reverse);
                try{
                    for(int i = 0;i< options.topN;i++){
                        System.out.println(terms.get(i));
                    }
                }catch (IndexOutOfBoundsException exception){
                    System.out.println("No more terms starts with '"+ options.start+"'");
                }break;
            case "termlengthstats":
                printStats(words, words.size());
                break;
        }

    }

    public static List<String> getWords(boolean unique, String... fileNames) throws IOException {
        List<Path> paths = new ArrayList<>();
        for (String fileName : fileNames)
            paths.add(Paths.get(fileName));
        List<String> words = new ArrayList<>();
        for (Path path : paths) {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                StringTokenizer tokenizer = new StringTokenizer(line, " ");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    StringBuilder builder = new StringBuilder();
                    for (char c : token.toCharArray())
                        if (Character.isLetterOrDigit(c))
                            builder.append(Character.toLowerCase(c));
                    String result = builder.toString();
                    if (!result.trim().isEmpty()) words.add(result);
                }
            }
        }
        return unique ? new ArrayList<>(new HashSet<>(words)) : words;
    }

    public static Map<String, Integer> countWords(List<String> words) {
        Map<String, Integer> res = new HashMap<>();
        for (String word : words) {
            if (res.containsKey(word))
                res.replace(word, res.get(word) + 1);
            else res.put(word, 1);
        }
        TreeMap<String, Integer> sorted = new TreeMap<>(res);
        return sorted;
    }

    public static void printFrequentTerms(Map<String, Integer> map, int topN , boolean reverse) {
        HashMap<Integer,ArrayList<String>> terms = new HashMap<>();

        for(String key : map.keySet()){
            if (!terms.containsKey(map.get(key))) {
                terms.put(map.get(key), new ArrayList<String>());
            }
            terms.get(map.get(key)).add(key);
        }
        for(ArrayList<String> list : terms.values()){
            Collections.sort(list);
        }
        int count = 0;
        if(reverse){
            for(Integer key : terms.keySet()){
                for(String element : terms.get(key)){
                    if(count==topN){
                        break;
                    }
                    System.out.println(element+"   "+key);
                    count+=1;
                }if(count==topN){
                    break;
                }
            }
        }else {
            TreeMap<Integer, ArrayList<String>> reversed = new TreeMap<>(Collections.reverseOrder());
            reversed.putAll(terms);
            for (Integer key : reversed.keySet()) {
                for (String element : reversed.get(key)) {
                    if (count == topN) {
                        break;
                    }
                    System.out.println(element + "   " + key);
                    count += 1;
                }
                if (count == topN) {
                    break;
                }
            }

        }
    }

    public static ArrayList<String> termsStartWith(Map<String,Integer> map,String term,boolean reverse){
        ArrayList<String> result = new ArrayList<>();
        for(String key: map.keySet()){
            if(key.startsWith(term)){
                result.add(key);
            }
        }
        Collections.sort(result);
        if(reverse){
            Collections.sort(result,Collections.reverseOrder());
        }

        return result;

    }

    public static void printStats(List<String> lst,Integer length){
        int max = 0;
        int min = 100;
        int total = 0;
        for(String key : lst){
            if(max<key.length()){
                max = key.length();
            }
            if(min>key.length()){
                min = key.length();
            }
            total+=key.length();
        }
        double avg = (double)total/(double) length;
        DecimalFormat formatter = new DecimalFormat("#0.0000");
        System.out.println("Max Token Length in Character: "+max+ ", Min Token Length: "
                +min+", Average Token Length:"+formatter.format(avg));

    }
}