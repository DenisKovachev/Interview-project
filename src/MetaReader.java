import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;


public class MetaReader {

    private JFrame frame;
    private final MetaParser mp;
    private MetaInfo info;
    private final String metaDir = null;

    MetaReader(String initialFilepath){
        this.mp = new MetaParser();
        this.info = mp.readLines(initialFilepath);
    }

    public JTable createFieldContentTable(){

        String[] columns = info.getBodyDefinitions().get("Field Names:").toArray(new String[0]);
        ArrayList<String[]> entries = new ArrayList<>();

        for (ArrayList<String> entry: info.getBodyEntries()
             ) {
            entries.add(entry.toArray(new String[0]));
        }
        String[][] rows = entries.toArray(new String[0][]);

        return new JTable(rows,columns);
    }

    public JTable createHeaderTable(){

        String[] cols = {"Key", "Values"};
        ArrayList<String[]> entries = new ArrayList<>();
        Set<String> keys = info.getHeader().keySet();
        for (String key: keys
        ) {
            ArrayList<String> currentEntry = new ArrayList<>();
            currentEntry.add(key);
            currentEntry.add(Arrays.toString(info.getHeader().get(key).toArray(new String[0])));
            entries.add(currentEntry.toArray(new String[0]));
        }

        return new JTable(entries.toArray(new String[0][]),cols);
    }

    public JTable createDefinitionOptionsTable(){
        String[] cols = {"Key", "Columns the option applies to"};
        String[] measurements = {"Relates to measurement",Arrays.toString(info.getMeasurementFields().toArray(new String[0]))};
        String[] selectable = {"Has Selection Panel",Arrays.toString(info.getSelectionFields().toArray(new String[0]))};
        String[][] arr = {measurements,selectable};
        return new JTable(arr,cols);
    }

    public void init(){

        frame = new JFrame();

        //Initialise header table
        JTable headerContentTable = createHeaderTable();

        //Initialise bodyContent table
        JTable fieldContentTable = createFieldContentTable();
        fieldContentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTable fieldContentOptionsTable = createDefinitionOptionsTable();
        TableColumnModel columnModel = fieldContentOptionsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(250);
        fieldContentOptionsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        //Create scroll-panes
        JScrollPane headerScrollPane = new JScrollPane(headerContentTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane bodyEntriesScrollPane = new JScrollPane(fieldContentTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JScrollPane bodyOptionsScrollPane = new JScrollPane(fieldContentOptionsTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel headerPanel = new JPanel();
        headerPanel.setBorder(BorderFactory.createTitledBorder(" Header information "));
        headerPanel.add(headerScrollPane);

        JPanel buttonPanel = new JPanel();
        JButton load = new JButton(" Load file ");
        ActionListener actionListener = event -> {
            JFileChooser fileChooser = new JFileChooser(metaDir);
            if(fileChooser.showOpenDialog(load)== JFileChooser.APPROVE_OPTION){
                try {
                   setFile(fileChooser.getSelectedFile().getAbsolutePath());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

        };
        load.addActionListener(actionListener);
        buttonPanel.add(load);

        JPanel bodyPanel = new JPanel();
        bodyPanel.setBorder(BorderFactory.createTitledBorder(" Field entries "));
        bodyPanel.add(bodyEntriesScrollPane);

        JPanel bodyOptions = new JPanel();
        bodyOptions.setBorder(BorderFactory.createTitledBorder(" Field options information "));
        bodyOptions.add(bodyOptionsScrollPane);

        frame.setLayout(new FlowLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        frame.add(bodyOptions,gbc);
////        gbc.gridx = 1;
////        gbc.gridy = 0;
////        frame.add(bodyOptions,gbc);
//        gbc.gridx = 0;
//        gbc.gridy = 1;
//        gbc.gridwidth = 3;
//        gbc.gridheight = 0;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.anchor = GridBagConstraints.LINE_START;
//        frame.add(bodyPanel,gbc);
//        gbc.gridx = 2;
//        gbc.gridy = 1;
//        gbc.ipadx = 50;
//        frame.add(buttonPanel,gbc);
        frame.add(headerPanel);
        frame.add(bodyOptions);
        frame.add(bodyPanel);
        frame.add(buttonPanel);
        frame.setTitle("Metafile reader");
        frame.setSize(1200, 1000);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    public void setFile(String path){

        frame.setVisible(false);
        this.info = this.mp.readLines(path);
        init();
    }

}
