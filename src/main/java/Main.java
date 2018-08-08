import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class Main {
    private static String[] fields = {"Busha Joseph Makamba",
            "Chamisa Nelson",
            "Chikanga  Everisto Washington",
            "Dzapasi Melbah ",
            "Gava Peter Mapfumo ",
            "Hlabangana Kwanele",
            "Kasiyamhuru Blessing",
            "Khupe Thokozani",
            "Madhuku Lovemore",
            "Mangoma Elton Steers",
            "Manyika Noah Ngoni",
            "Mapfumo Chiguvare Tonderayi Johannes Timothy",
            "MARIYACHA Violet",
            "Mhambi-Hove Divine",
            "Mnangagwa Emmerson Dambudzo",
            "Moyo Donald Nkosana ",
            "Mteki Bryn Taurai",
            "Mugadza Willard Tawonezvi",
            "Mujuru Joice Teurai Ropa",
            "Munyanduri Tenda Peter",
            "MutinhirI Ambrose",
            "Shumba Kuzozvirava Doniel",
            "Wilson Peter Harry",
            "Total Votes Rejected ",
            "Ballot Paper Unaccounted for",
            "Total Votes Cast",
            "Total Valid Votes Cast"};
    private static String[] fieldsParties = {
            "Busha Joseph Makamba FreeZim Congress",
            "Chamisa Nelson MDC Alliance",
            "Chikanga Everisto Washington Rebuild Zimbabwe",
            "Dzapasi Melbah 1980 Freedom Movement Zimbabwe",
            "Gava Peter Mapfumo UDF",
            "Hlabangana Kwanele RPZ",
            "Kasiyamhuru Blessing ZPP",
            "Khupe Thokozani MDC-T",
            "Madhuku Lovemore NCA",
            "Mangoma Elton Steers Coalition of Democrats",
            "Manyika Noah Ngoni BZA",
            "Mapfumo Chiguvare Tonderayi Johannes Timothy PPPZ",
            "MARIYACHA Violet UDM",
            "Mhambi-Hove Divine NAPDR",
            "Mnangagwa Emmerson Dambudzo ZANU PF",
            "Moyo Donald Nkosana APA",
            "Mteki Bryn Taurai Independent",
            "Mugadza Willard Tawonezvi BCP",
            "Mujuru Joice Teurai Ropa PRC",
            "Munyanduri Tenda Peter NPF",
            "MutinhirI Ambrose NPF",
            "Shumba Kuzozvirava Doniel UDA",
            "Wilson Peter Harry DOP",
            "Total Votes Rejected ",
            "Ballot Paper Unaccounted for",
            "Total Votes Cast",
            "Total Valid Votes Cast"};

    private static String[][] v11 = {
            {"ZECOMESS HALL", "1", "Hwange"},
            {"matabo sec", "32", "mberen"},
            {"jonas", "8", "manyame"},
            {"murewa culture house", "30", "mure"}
    };
    private static void checkWithV11(String filename){
        try(Stream<String> stream = Files.lines(Paths.get(filename), ISO_8859_1)){
            stream.map(a -> a.split(","))
                .filter(a-> a.length == 33 && !a[0].equals("DISTRICT") && !a[0].isEmpty() && !a[1].isEmpty() && !a[2].isEmpty() && !a[3].isEmpty())
                .map(b -> {
                    List<String> a = Arrays.asList(b);
                   return a;
                }).forEach(a -> {
                    List<String> pollStations = new ArrayList<>();
                    for(String[] x : v11){
                        pollStations.add(x[0].toLowerCase());
                        if(a.get(4).toLowerCase().contains(x[0].toLowerCase()) && x[1].trim().equals(a.get(3).trim()) && a.get(2).toLowerCase().contains(x[2].toLowerCase()))
                            System.out.println(a + " " + x[1] + " " +a.get(3));
                    }
                    //System.out.println(a.get(4)+ " \t" + pollStations);
                    //if(a.get(4).toLowerCase().contains())
                    //System.out.println(a)
                });
        }catch (MalformedInputException e){
            System.out.print(filename + "has malformed charset");
        }catch (IOException e){
            System.out.println("error reading file");
        }
    }
    private static List<String> findUntallyingTotals(String filename){
        List<String> csv = new ArrayList<>();
        try(Stream<String> stream = Files.lines(Paths.get(filename), ISO_8859_1)){
            csv =
                stream.map(a -> a.split(","))
                    .filter(a-> a.length == 33 && !a[0].equals("DISTRICT") && !a[0].isEmpty() && !a[1].isEmpty() && !a[2].isEmpty() && !a[3].isEmpty())
                    .map(b -> {
                        List<String> a = Arrays.asList(b);
                        /*
                            29 "Total Votes Rejected ",
                            30 "Ballot Paper Unaccounted for",
                            31 "Total Votes Cast",
                            32 "Total Valid Votes Cast"
                        */
                        int total = 0;
                        int totalVotesCast = 0;
                        try{
                            totalVotesCast = Integer.parseInt(a.get(31));
                        }catch (NumberFormatException e){

                        }
                        List<Integer> com = new ArrayList();
                        for(int i = 6; i < 31; ++i){
                            int n;
                            if(a.get(i).trim().isEmpty())
                                n = 0;
                            else
                                n = Integer.parseInt(a.get(i));
                            com.add(n);
                            total += n;
                        }
                        return new Pair<>(a, total - totalVotesCast);
                    })
                    .filter(a -> a.getValue() != 0 )
                    .map(a -> {
                        String station = String.join("," ,a.getKey());
                        String difference = a.getValue().toString();
                        return difference + "," + station;
                    })
                    .collect(Collectors.toList());

        }catch (MalformedInputException e){
            System.out.print(filename + "has malformed charset");
        }catch (IOException e){
            System.out.println("error reading file");
        }
        return csv;
    }
    private static List<String> wildMargins(String filename, int threshold){
        List<String> csvFile = new ArrayList();
        try(Stream<String> stream = Files.lines(Paths.get(filename), ISO_8859_1)){
            csvFile = stream.map(a -> a.split(","))
                    .filter(a-> a.length == 33 && !a[0].equals("DISTRICT")  && !a[0].isEmpty() && !a[1].isEmpty() && !a[2].isEmpty() && !a[3].isEmpty())
                    .map(a -> {
                        List<String> res = Arrays.asList(a);//.subList(6,a.length);
                        List<Integer> com = new ArrayList<>();
                        Integer mn = Integer.parseInt(res.get(20));
                        Integer ch = Integer.parseInt(res.get(7));

                        com.add(0,ch);
                        com.add(1,mn);
                        com.add(2,mn - ch);
                        com.add(3,100 * (mn - ch)/(mn + ch));
                        return new Pair<> (res.subList(0,6),com);
                    }).filter(a -> Math.abs(a.getValue().get(3)) > threshold)
                    .map(a -> {
                        String csv = String.join(",", a.getValue().stream().map(x -> x.toString()).collect(Collectors.toList())) + "," + String.join(",",a.getKey());
                        return csv;
                    }).collect(Collectors.toList());

        }catch (MalformedInputException e){
            System.out.print(filename + "has malformed charset");
        }catch (IOException e){
            System.out.println("error reading file");
        }
        return csvFile;
    }
    private static List<String> pipeNationalByPollingStation(String filename){
        List<String> csvFile = new ArrayList();
        try(Stream<String> stream = Files.lines(Paths.get(filename), ISO_8859_1)){
            csvFile = stream.map(a -> a.split(","))
                    .filter(a-> a.length == 33 && !a[0].equals("DISTRICT")  && !a[0].isEmpty() && !a[1].isEmpty() && !a[2].isEmpty() && !a[3].isEmpty())
                    .map(a -> String.join(",",a))
                    .collect(Collectors.toList());

        }catch (MalformedInputException e){
            System.out.print(filename + "has malformed charset");
        }catch (IOException e){
            System.out.println("error reading file");
        }
        return csvFile;
    }
    private static void countVotes(String filename){
        try(Stream<String> stream = Files.lines(Paths.get(filename), ISO_8859_1)){
            //List<String[]> pollingStationResults =
                    stream.map(a -> a.split(","))
                        .filter(a-> a.length == 33 && !a[0].equals("DISTRICT")  && !a[0].isEmpty() && !a[1].isEmpty() && !a[2].isEmpty() && !a[3].isEmpty())
                        .map(a -> {
                            List<String> res = Arrays.asList(a).subList(6,a.length);
                            List<Pair<String,Long>> lsRes = new ArrayList();
                            int i = 0;
                            for(String num : res){
                                try {
                                    String n = num;
                                    if(num.isEmpty())
                                        n = "0";
                                    lsRes.add(new Pair<>(fields[i++], Long.parseLong(n)));
                                }catch (NumberFormatException e){
                                    System.out.println(res);
                                }
                            }
                            return lsRes;
                        })/*.forEach(System.out::println)*/;//todo really count the votes
                    //.collect(Collectors.toList());
            //pollingStationResults.

        }catch (MalformedInputException e){
            System.out.print(filename + "has malformed charset");
        }catch (IOException e){
            System.out.println("error reading file");
        }
    }
    private static List<String> duplicateResults(String filename){
        List<String> csv = new ArrayList<>();
         try(Stream<String> stream = Files.lines(Paths.get(filename), ISO_8859_1)){
            List<Pair<String,String>> pollingStationResultsPairs = stream.map(a -> a.split(","))
                    .filter(a-> a.length == 33 && !a[0].isEmpty() && !a[1].isEmpty() && !a[2].isEmpty() && !a[3].isEmpty())
                    .map(a ->{
                        List<String> res = Arrays.asList(a);
                        String identity = String.join(", ",res.subList(0,6));
                        String value = String.join(", ",res.subList(6,a.length));
                        return new Pair<>(identity, value);
                    }).collect(Collectors.toList());

            //pollingStationResultsPairs.forEach(System.out::println);
            //duplicate results
            Map<String, Long> pollingStationResultsResultFrequencies = pollingStationResultsPairs.stream().map(a -> a.getValue())
                    .collect(groupingBy(Function.identity(), counting()));

            List<String> duplicateResultEntries = pollingStationResultsResultFrequencies
                    .entrySet()
                    .stream()
                    .filter(a -> a.getValue() > 1)
                    .map(a -> a.getKey())
                    .collect(Collectors.toList());

            csv = pollingStationResultsPairs
                    .stream()
                    .filter(a -> duplicateResultEntries.contains(a.getValue()))
                    .sorted(Comparator.comparing(Pair::getValue))
                    .map(a -> a.getKey() + ", " + a.getValue())
                    .collect(Collectors.toList());
            //csv.forEach(System.out::println);

        }catch (MalformedInputException e){
            System.out.print(filename + "has malformed charset");
        }catch (IOException e){
            System.out.println("error reading file");
        }
        return csv;
    }
    public static void main(String[] args){

        File directory = new File("src/main/resources/");
        File[] fList = directory.listFiles();
        int extraVotes = 0;
        List<String> weirdDifferencesCsv = new ArrayList<>();
        List<String> extraVotesCsv = new ArrayList<>();
        List<String> duplicateRowsCsv = new ArrayList<>();
        List<String> searchableResultsCsv = new ArrayList<>();
        List<String> searchableResultsJsonCsv = new ArrayList<>();

        weirdDifferencesCsv.add("Chamisa, Mnagagwa, Mnagagwa - Chamisa, % Mnagagwa - Chamisa, DISTRICT,CONSTITUENCY,LOCAL AUTHORITY,WARD NO.,POLLING STATION,STATION CODE");
        extraVotesCsv.add("Extra votes, DISTRICT,CONSTITUENCY,LOCAL AUTHORITY,WARD NO.,POLLING STATION,STATION CODE,"+String.join(",",fields) );
        duplicateRowsCsv.add("DISTRICT,CONSTITUENCY,LOCAL AUTHORITY,WARD NO.,POLLING STATION,STATION CODE,"+String.join(",",fields));
        searchableResultsCsv.add("DISTRICT,CONSTITUENCY,LOCAL AUTHORITY,WARD NO,POLLING STATION,STATION CODE,"+String.join(",",fields));
        searchableResultsJsonCsv.add("DISTRICT,CONSTITUENCY,LOCAL_AUTHORITY,WARD_NO,POLLING_STATION,STATION_CODE,"+String.join(",",fieldsParties).replaceAll(" ","_"));
        for (File file : fList){
            duplicateRowsCsv.addAll(duplicateResults(file.getPath()));
            //countVotes(file.getPath());
            extraVotesCsv.addAll(findUntallyingTotals(file.getPath()));
            searchableResultsCsv.addAll(pipeNationalByPollingStation(file.getPath()));
            weirdDifferencesCsv.addAll(wildMargins(file.getPath(), 80));
            searchableResultsJsonCsv.addAll(pipeNationalByPollingStation(file.getPath()));
            //checkWithV11(file.getPath());
        }
        System.out.println("Extra votes from ZEC result rows = " + extraVotes);
        try {
            Files.write(Paths.get("results/weird_differences.csv"), weirdDifferencesCsv);
            Files.write(Paths.get("results/extra-votes.csv"), extraVotesCsv);
            Files.write(Paths.get("results/duplicated-results.csv"), duplicateRowsCsv);

            int l = searchableResultsCsv.size();
            int l1 = l/3;

            Files.write(Paths.get("results/searchable-results.csv"), searchableResultsJsonCsv);

            Files.write(Paths.get("results/searchable-results-1.csv"), searchableResultsCsv.subList(0,l/3));
            Files.write(Paths.get("results/searchable-results-2.csv"), searchableResultsCsv.subList(l/3, 2*l1));
            Files.write(Paths.get("results/searchable-results-3.csv"), searchableResultsCsv.subList(2*l1, l));
        }catch (IOException e){
            System.out.println("path error");
        }
    }
}
