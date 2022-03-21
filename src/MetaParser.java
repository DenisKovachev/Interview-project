import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MetaParser {

    MetaParser(){}

    public MetaInfo readLines(String path){

        File file = new File(path);
        Scanner sc = null;

        LinkedHashMap<String,ArrayList<String>> header = new LinkedHashMap<>();
        LinkedHashMap<String,ArrayList<String>> bodyDefinitions = new LinkedHashMap<>();
        LinkedHashMap<String,ArrayList<String>> bodyContent = new LinkedHashMap<>();
        ArrayList<ArrayList<String>> bodyEntries = new ArrayList<>();

        try {
            sc = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (sc != null) {
            boolean isHeader = true;
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] currentLine = line.split("\\t", -1);
                System.out.println("First line entry: " + currentLine[0]);
                if(line.matches("\\t*")){
                    isHeader = false;
                    continue;
                }
                if(Objects.equals(currentLine[0], "Relates to measurement")){
                    isHeader = false;
                }
                if(isHeader){
                    header.put(currentLine[0], this.copyOfStringArrayList(currentLine,1,currentLine.length-1));
                }else if(Objects.equals(currentLine[0], "Relates to measurement") ||
                        Objects.equals(currentLine[0], "Has Selection Panel") ||
                        Objects.equals(currentLine[0], "Field Names:")){
                    bodyDefinitions.put(currentLine[0], this.copyOfStringArrayList(currentLine,1,currentLine.length-1));
                } else {
                    if(!bodyContent.containsKey(currentLine[0]) && !Objects.equals(currentLine[0], "")) {
                        bodyContent.put(currentLine[0], copyOfStringArrayList(currentLine, 1, currentLine.length - 1));
                    }
                }

            }
            sc.close();
        }
        Set<String> bodyContentKeys = bodyContent.keySet();
        for (String key: bodyContentKeys
             ) {
            ArrayList<String> currentEntry = bodyContent.get(key);
            currentEntry.add(0,key);
            bodyEntries.add(currentEntry);
        }

        return new MetaInfo(header,bodyDefinitions,bodyEntries);
    }

    /**
     *
     * @param originalArray - original array from which we take the slice.
     * @param start - starting index for the return array.
     * @param end - ending index for the return array.
     * @return  a sub-array of the input array, starting from "start" and ending with "end" inclusive.
     */
    public String[] copyOfString(String[] originalArray, int start, int end){
        String[] result = new String[end - start + 1];
        int j = 0;
        for (int i = start; i<=end; i++){
            result[j] = originalArray[i];
            j++;
        }
        return result;
    }

    //TODO: TEST IF THIS WORKS THE WAY IT SHOULD ---- NOPE THERE IS A FUNCTION THAT DOES THAT FOR ARRAYLISTS
    public ArrayList<String> copyOfStringArrayList(String[] originalArray, int start, int end){
        ArrayList<String> result = new ArrayList<>();
        int j = 0;
        for (int i = start; i<=end; i++){
            result.add(j,originalArray[i]);
            j++;
        }
        return result;
    }

}
