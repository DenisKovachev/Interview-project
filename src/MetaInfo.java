import java.util.*;

public class MetaInfo {

    private LinkedHashMap<String,ArrayList<String>> header;
    private LinkedHashMap<String,ArrayList<String>> bodyDefinitions;
    private ArrayList<ArrayList<String>> bodyEntries;
    private ArrayList<String> measurementFields;
    private ArrayList<String> selectionFields;

    public MetaInfo(LinkedHashMap<String, ArrayList<String>> header, LinkedHashMap<String, ArrayList<String>> bodyDefinitions, ArrayList<ArrayList<String>> bodyEntries) {
        this.header = header;
        this.bodyDefinitions = bodyDefinitions;
        this.bodyEntries = bodyEntries;
        this.measurementFields = new ArrayList<>();
        this.selectionFields = new ArrayList<>();
        formatHeaderLines();
        captureColumnOptions();
        validateColumns();
        System.out.println();
    }

    public void formatHeaderLines(){

        Set<String> keys = header.keySet();
        for (String key: keys
        ) {
            ArrayList<String> oldArr = this.header.get(key);
            ArrayList<String> newArr = new ArrayList<>();
            for (String arg: oldArr
                 ) {
                if(arg.equals("")){
                    break;
                }else{
                    newArr.add(arg);
                }
            }
            header.replace(key,newArr);
        }
    }

    public void validateColumns() {

        ArrayList<String> fieldNames = this.bodyDefinitions.get("Field Names:");
        ArrayList<String> validFields = new ArrayList<>();
        List<Integer> validIndexes = new ArrayList<Integer>() {};

        for (int i = 0; i < fieldNames.size(); i++) {
            for (ArrayList<String> entry : bodyEntries
            ) {
                //checks if the field name is blank, if so does not fetch any of the content (edge case at the end column)
                if(!Objects.equals(fieldNames.get(i), "")) {
                    if (!Objects.equals(entry.get(i), "") && !Objects.equals(entry.get(i), "\t")) {
                        validIndexes.add(i);
                        validFields.add(fieldNames.get(i));
                        break;
                    }
                }
            }

        }
        this.bodyDefinitions.replace("Field Names:", validFields);

        //TODO: can just remove the invalid i from every line
        for (int i = 0; i < bodyEntries.size(); i++) {
            ArrayList<String> newEntry = new ArrayList<>();
            for (Integer j: validIndexes
            ) {
                newEntry.add(bodyEntries.get(i).get(j));
            }
            bodyEntries.remove(i);
            bodyEntries.add(i,newEntry);
        }
    }

    private void captureColumnOptions(){
        ArrayList<String> relatesToMeasurements = this.bodyDefinitions.get("Relates to measurement");
        ArrayList<String> hasSelectionPanel = this.bodyDefinitions.get("Has Selection Panel");
        ArrayList<String> fieldNames = this.bodyDefinitions.get("Field Names:");

        for (int i = 0; i < relatesToMeasurements.size(); i++) {
            if(Objects.equals(relatesToMeasurements.get(i), "y") || Objects.equals(relatesToMeasurements.get(i), "Y")){
                this.measurementFields.add(fieldNames.get(i));
            }
            if(Objects.equals(hasSelectionPanel.get(i), "y") || Objects.equals(hasSelectionPanel.get(i), "Y")){
                this.selectionFields.add(fieldNames.get(i));
            }
        }

    }

    public LinkedHashMap<String, ArrayList<String>> getHeader() {
        return header;
    }

    public LinkedHashMap<String, ArrayList<String>> getBodyDefinitions() {
        return bodyDefinitions;
    }

    public ArrayList<ArrayList<String>> getBodyEntries() {
        return bodyEntries;
    }

    public ArrayList<String> getMeasurementFields() {
        return measurementFields;
    }

    public ArrayList<String> getSelectionFields() {
        return selectionFields;
    }
}
