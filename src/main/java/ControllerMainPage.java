import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.stage.Stage;

public class ControllerMainPage implements Initializable {
    Stage window=Main.window;
    public static ArrayList<ImageView> drawn=new ArrayList<>();
    public static ArrayList<Line> lines=new ArrayList<>();
    public static String voltagename;
    public static String currentname;
    public static String powername;

    @FXML public TabPane tabPane;
    @FXML public Pane pane;
    @FXML public Tab outputTab,inputTab;
    @FXML public TextArea codeArea,outputArea;
    @FXML public TextField dvtf,ditf,dttf,timetf;
    @FXML public Label percentage;
    @FXML public ProgressBar bar;

    public void newProject() {
        hidePercentage();
        Stage stage = Main.window;
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            //System.out.println(selectedDirectory.getPath());
            String newFilePath = selectedDirectory.getPath();

            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("New Project");
            window.setMinWidth(250);
            VBox layout1 = new VBox();
            layout1.setPadding(new Insets(10, 50, 10, 50));


            javafx.scene.control.Label label = new javafx.scene.control.Label("Enter your project's name");
            javafx.scene.control.Button create = new javafx.scene.control.Button("Create");
            javafx.scene.control.Button cancel = new javafx.scene.control.Button("Cancel");
            javafx.scene.control.TextField textField = new TextField();
            textField.setPromptText("project's name");
            create.setOnAction(event1 -> {
                if (!textField.getText().isEmpty()) {
                    String newPath = newFilePath + "\\" + textField.getText() + ".txt";
                    File newFile = new File(newPath);
                    try {
                        newFile.createNewFile();
                        Main.path = newPath;
                        //System.out.println(path);
                        updateTextArea();
                        window.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            cancel.setOnAction(event1 -> window.close());
            layout1.setAlignment(Pos.CENTER);
            HBox buttons = new HBox(create, cancel);
            buttons.setAlignment(Pos.CENTER);
            layout1.setSpacing(9);
            buttons.setSpacing(14);
            layout1.getChildren().addAll(label, textField, buttons);

            Scene scene = new Scene(layout1);
            window.setScene(scene);
            window.show();
        }
    }

    public void openProject(){
        hidePercentage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            Main.path=file.getPath();
            //System.out.println(path);
            updateTextArea();
        }
    }

    public void saveProject(){
        try {
            saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadProject(){
        updateTextArea();
    }

    public void exitAZA() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("New Project");
        window.setMinWidth(250);
        VBox layout1= new VBox();
        layout1.setMinSize(300, 100);
        layout1.setPadding(new Insets(10,50,10,50));


        javafx.scene.control.Label label=new javafx.scene.control.Label("Are you sure you want to exit AZA?");
        javafx.scene.control.Button yes=new javafx.scene.control.Button("Yes");
        javafx.scene.control.Button cancel=new Button("No");



        yes.setOnAction(event1 -> {
            // TODO: 20/07/05 Ask to save before exit
            System.exit(0);
        });
        cancel.setOnAction(event1 -> window.close());
        layout1.setAlignment(Pos.CENTER);
        HBox buttons= new HBox(yes,cancel);
        buttons.setAlignment(Pos.CENTER);
        layout1.setSpacing(9);
        buttons.setSpacing(14);
        layout1.getChildren().addAll(label,buttons);

        Scene scene=new Scene(layout1);
        window.setScene(scene);
        window.show();
    }

    public void listshow1() throws Exception {
        final ObservableList<String> names =
                FXCollections.observableArrayList();
        final ObservableList<String> data =
                FXCollections.observableArrayList();
        final ListView listView = new ListView(data);
        listView.setPrefSize(200, 250);



        for (int i=0;i<Brain.everything.size();i++) {
            if (Brain.everything.get(i) instanceof Node){
                data.add("Node "+((Node) Brain.everything.get(i)).name+":V");
            }
            if(Brain.everything.get(i) instanceof ISource){
                data.add("ISource "+((ISource) Brain.everything.get(i)).name+":V");
            }
            if(Brain.everything.get(i) instanceof VSource){
                data.add("VSource "+((VSource) Brain.everything.get(i)).name+":V");
            }
            if(Brain.everything.get(i) instanceof Resistor){
                data.add("Resistor "+((Resistor) Brain.everything.get(i)).name+":V");
            }
            if(Brain.everything.get(i) instanceof Capacitor){
                data.add("Capacitor "+((Capacitor) Brain.everything.get(i)).name+":V");
            }
            if(Brain.everything.get(i) instanceof Inductor){
                data.add("Inductor "+((Inductor) Brain.everything.get(i)).name+":V");
            }
        }
        listView.setItems(data);
        listView.setPrefSize(300,250);

        Stage stage=new Stage();
        StackPane root = new StackPane();
        root.getChildren().add(listView);
        stage.setScene(new Scene(root, 200, 250));
        stage.show();
        initActions1(listView,stage);
    }

    public void initActions1(ListView<String> list,Stage stage1) throws IOException,Exception{
        list.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                //Check wich list index is selected then set txtContent value for that index

                if(arg0.getClickCount()>1){
                    voltagename=list.getSelectionModel().getSelectedItem();
                    list.setVisible(false);
                    stage1.close();
                    Parent root1= null;
                    try {
                        root1 = FXMLLoader.load(getClass().getResource("sample.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Stage stage = new Stage();

        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Diagrams");
        Scene scene=new Scene(root1, 1500, 800);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();

                }

            }

        });
    }

    public void listshow2() throws Exception {
        final ObservableList<String> names =
                FXCollections.observableArrayList();
        final ObservableList<String> data =
                FXCollections.observableArrayList();
        final ListView listView = new ListView(data);
        listView.setPrefSize(200, 250);



        for (int i=0;i<Brain.everything.size();i++) {
            if (Brain.everything.get(i) instanceof Element) {
                if (Brain.everything.get(i) instanceof Node) {
                    data.add("Node " + ((Node) Brain.everything.get(i)).name + ":I");
                }
                if (Brain.everything.get(i) instanceof ISource) {
                    data.add("ISource " + ((ISource) Brain.everything.get(i)).name + ":I");
                }
                if (Brain.everything.get(i) instanceof VSource) {
                    data.add("VSource " + ((VSource) Brain.everything.get(i)).name + ":I");
                }
                if (Brain.everything.get(i) instanceof Resistor) {
                    data.add("Resistor " + ((Resistor) Brain.everything.get(i)).name + ":I");
                }
                if (Brain.everything.get(i) instanceof Capacitor) {
                    data.add("Capacitor " + ((Capacitor) Brain.everything.get(i)).name + ":I");
                }
                if (Brain.everything.get(i) instanceof Inductor) {
                    data.add("Inductor " + ((Inductor) Brain.everything.get(i)).name + ":I");
                }
            }
        }
        listView.setItems(data);
        listView.setPrefSize(300,250);

        Stage stage=new Stage();
        StackPane root = new StackPane();
        root.getChildren().add(listView);
        stage.setScene(new Scene(root, 200, 250));
        stage.show();
        initActions2(listView,stage);
    }

    public void initActions2(ListView<String> list,Stage stage1) throws IOException,Exception{
        list.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                //Check wich list index is selected then set txtContent value for that index

                if(arg0.getClickCount()>1){
                    currentname=list.getSelectionModel().getSelectedItem();
                    list.setVisible(false);
                    stage1.close();

                }

            }

        });
    }

    public void listshow3() throws Exception {
        final ObservableList<String> names =
                FXCollections.observableArrayList();
        final ObservableList<String> data =
                FXCollections.observableArrayList();
        final ListView listView = new ListView(data);
        listView.setPrefSize(200, 250);



        for (int i=0;i<Brain.everything.size();i++) {
            if (Brain.everything.get(i) instanceof Element) {
                if (Brain.everything.get(i) instanceof Node) {
                    data.add("Node " + ((Node) Brain.everything.get(i)).name + ":P");
                }
                if (Brain.everything.get(i) instanceof ISource) {
                    data.add("ISource " + ((ISource) Brain.everything.get(i)).name + ":P");
                }
                if (Brain.everything.get(i) instanceof VSource) {
                    data.add("VSource " + ((VSource) Brain.everything.get(i)).name + ":P");
                }
                if (Brain.everything.get(i) instanceof Resistor) {
                    data.add("Resistor " + ((Resistor) Brain.everything.get(i)).name + ":P");
                }
                if (Brain.everything.get(i) instanceof Capacitor) {
                    data.add("Capacitor " + ((Capacitor) Brain.everything.get(i)).name + ":P");
                }
                if (Brain.everything.get(i) instanceof Inductor) {
                    data.add("Inductor " + ((Inductor) Brain.everything.get(i)).name + ":P");
                }
            }
        }
        listView.setItems(data);
        listView.setPrefSize(300,250);

        Stage stage=new Stage();
        StackPane root = new StackPane();
        root.getChildren().add(listView);
        stage.setScene(new Scene(root, 200, 250));
        stage.show();
        initActions3(listView,stage);
    }

    public void initActions3(ListView<String> list,Stage stage1) throws IOException,Exception{
        list.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent arg0) {
                //Check wich list index is selected then set txtContent value for that index

                if(arg0.getClickCount()>1){
                    powername=list.getSelectionModel().getSelectedItem();
                    list.setVisible(false);
                    stage1.close();

                }

            }

        });
    }

    public void draw() throws IOException,Exception {
        run();
        listshow1();
        listshow2();
        listshow3();
//        Parent root1= FXMLLoader.load(getClass().getResource("sample.fxml"));
//        Stage stage = new Stage();
//
//        //stage.initStyle(StageStyle.UNDECORATED);
//        stage.setTitle("Diagrams");
//        Scene scene=new Scene(root1, 1500, 800);
//        stage.initModality(Modality.APPLICATION_MODAL);
//        stage.setScene(scene);
//        stage.show();
    }

    public void run(){
        tabPane.getSelectionModel().select(inputTab);
        saveProject();
        percentage.setVisible(true);
        percentage.setText("0.0"+"%");
        bar.setProgress(0);
        bar.setVisible(true);
        Brain.simulateFile(percentage,bar);
        percentage.setText("100"+"%");
        bar.setProgress(1);
        updateOutputTextArea();
        /////////////////////////
        HashMap <Element,Boolean> checkList=new HashMap<>();
        for (Element element:Element.elements){
            checkList.put(element,false);
        }

        for (ImageView img:drawn){
            img.setVisible(false);
        }
        for (Line line:lines){
            line.setVisible(false);
            // TODO: 20/07/12 shit?
        }
        drawn.clear();
        lines.clear();
        int[] xy=getXY();
        Nodes gnd=null;
        int nc=0;
        double [] gndLoc=new double[2];
        double horSteps=(pane.getWidth()-200)/(xy[2]-xy[0]),verSteps=(pane.getHeight()+20)/(xy[3]+1);

        for (Nodes node1:Nodes.nodes){
            ArrayList<Element> hold;
            if (!(node1 instanceof Ground)) {
                for (Nodes node2:Nodes.nodes){
                    if (!(node2 instanceof Ground)&&!node1.name.equals(node2.name)){
                        hold=Nodes.getParallelElements(node1,node2);

                        int n1=Integer.parseInt(node1.name);
                        int n2=Integer.parseInt(node2.name);

                        double[] xy1=new double[2],xy2=new double[2];
                        xy1[0]=100+horSteps*((n1-1)%6+1-xy[0]);
                        xy2[0]=100+horSteps*((n2-1)%6+1-xy[0]);

                        xy1[1]=50+verSteps*((xy[3]-((n1-1)/6+1)));
                        xy2[1]=50+verSteps*((xy[3]-((n2-1)/6+1)));
                        int parallel=hold.size(),round=0;

                        for (Element element:hold) {
                            if (!checkList.get(element)){
                                double[] centre=getCentre(Integer.parseInt(element.node[0].name),Integer.parseInt(element.node[1].name),xy);
                                double shift=-30*parallel+60*round+30;
                                int isHor=0,isVer=0;
                                if ((int)(centre[2])%2==0)
                                    isHor=1;
                                else
                                    isVer=1;

                                Line line= new Line(xy1[0]+isHor*shift,xy1[1]+isVer*shift,xy2[0]+isHor*shift,xy2[1]+isVer*shift);
                                lines.add(line);
                                pane.getChildren().add(line);

                                Line line1= new Line(xy1[0]+isHor*shift,xy1[1]+isVer*shift,xy1[0],xy1[1]);
                                lines.add(line1);
                                pane.getChildren().add(line1);

                                Line line2= new Line(xy2[0],xy2[1],xy2[0]+isHor*shift,xy2[1]+isVer*shift);
                                lines.add(line2);
                                pane.getChildren().add(line2);

                                Image image1=new Image(element.imageAddress, 60, 60, false, false);
                                ImageView image=new ImageView(image1);
                                pane.getChildren().add(image);
                                drawn.add(image);



                                image.relocate(centre[0]-30+isHor*shift,centre[1]-30+isVer*shift);
                                image.setRotate(90*centre[2]);
                                //System.out.println(pane.getWidth());
                                //System.out.println(pane.getHeight());
                                System.out.println(element.name);
                                System.out.println(centre[0]);
                                System.out.println(centre[1]);
                                checkList.replace(element,true);
                                ++round;
                            }
                        }
                    }
                }
            }
            else {
                gnd=node1;
                for (Nodes node2:Nodes.nodes){
                    if (!(node2 instanceof Ground)&&!node1.name.equals(node2.name)){
                        hold=Nodes.getParallelElements(node1,node2);
                        int parallel=hold.size(),round=0;
                        if (parallel!=0)
                            ++nc;

                        double[] xy1=new double[2],xy2=new double[2];
                        int n2=Integer.parseInt(node2.name);
                        xy2[0]=100+horSteps*((n2-1)%6+1-xy[0]);
                        xy1[0]=xy2[0];


                        xy1[1]=50+verSteps*((xy[3]));
                        xy2[1]=50+verSteps*((xy[3]-((n2-1)/6+1)));



                       // line.relocate(xy1[0],xy1[1]);


                        for (Element element:hold) {
                            if (!checkList.get(element)){

                                gndLoc[0]=xy1[0];
                                gndLoc[1]=xy1[1];
                                double shift=-30*parallel+60*round+30;

                                Line line= new Line(xy1[0]+shift,xy1[1],xy2[0]+shift,xy2[1]);
                                lines.add(line);
                                pane.getChildren().add(line);

                                Line line1= new Line(xy1[0]+shift,xy1[1],xy1[0],xy1[1]);
                                lines.add(line1);
                                pane.getChildren().add(line1);

                                Line line2= new Line(xy2[0],xy2[1],xy2[0]+shift,xy2[1]);
                                lines.add(line2);
                                pane.getChildren().add(line2);


                                Image image1=new Image(element.imageAddress, 60, 60, false, false);
                                ImageView image=new ImageView(image1);
                                pane.getChildren().add(image);
                                drawn.add(image);

                                image.relocate((xy1[0]+xy2[0])/2-30+shift,(xy1[1]+xy2[1])/2-30);
                                // TODO: 20/07/12 age gharar shod abaad avaz koni loc ro deghat kon
                                if (element.node[0] instanceof Ground)
                                    image.setRotate(90*2);
                                //System.out.println(pane.getWidth());
                                //System.out.println(pane.getHeight());
                                //System.out.println(element.name);

                                checkList.replace(element,true);
                                ++round;
                            }
                        }
                    }
                }
            }
        }

        double hold=0,c=0;

        if (nc!=1){

            for (Element element:gnd.elements){
                System.out.println(element.name);
                Nodes node=element.otherNode(gnd);

                double[] xy2=new double[2];
                int n=Integer.parseInt(node.name);
                xy2[0]=100+horSteps*((n-1)%6+1-xy[0]);
                xy2[1]=50+verSteps*((xy[3]-((n-1)/6+1)));
                Line line= new Line(gndLoc[0],gndLoc[1],xy2[0],gndLoc[1]);
                lines.add(line);
                pane.getChildren().add(line);
                ++c;
                hold+=xy2[0];
            }
            hold/=c;
        }

        FileInputStream imageAddress=null;
        try {
            imageAddress= new FileInputStream(System.getProperty("user.dir")+"\\elements\\"+"gnd"+".jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Image image1=new Image(imageAddress, 60, 60, false, false);
        ImageView image=new ImageView(image1);
        pane.getChildren().add(image);
        drawn.add(image);


        if (nc != 1) {
            gndLoc[0] = hold;
            // TODO: 20/07/12 bug za

        }
        image.relocate(gndLoc[0]-30,gndLoc[1]+1);
    }

    public int[] getXY(){
        int [] holdMax=new int[2];
        int [] holdMin=new int[2];
        int [] hold=new int[4];
        holdMax[0]=1;holdMin[0]=6;
        for (Nodes node:Nodes.nodes){
            if (!(node instanceof Ground)){
                int n=Integer.parseInt(node.name);
                if ((n-1)%6+1>holdMax[0])
                    holdMax[0]=(n-1)%6+1;
                if ((n-1)%6+1<holdMin[0])
                    holdMin[0]=(n-1)%6+1;
                if ((n-1)/6+1>holdMax[1])
                    holdMax[1]=(n-1)/6+1;
            }
        }
        hold[0]=holdMin[0];hold[1]=holdMin[1];
        hold[2]=holdMax[0];hold[3]=holdMax[1];
        return hold;
    }

    public double[] getCentre(int n1,int n2,int[] xy){
        double[] centre=new double[4];
        double horSteps=(pane.getWidth()-200)/(xy[2]-xy[0]),verSteps=(pane.getHeight()+20)/(xy[3]+1);
        double[] xy1=new double[2],xy2=new double[2];
        xy1[0]=100+horSteps*((n1-1)%6+1-xy[0]);
        xy2[0]=100+horSteps*((n2-1)%6+1-xy[0]);

        xy1[1]=50+verSteps*((xy[3]-((n1-1)/6+1)));
        xy2[1]=50+verSteps*((xy[3]-((n2-1)/6+1)));
/*
        System.out.println(xy1[0]);
        System.out.println(xy1[1]);
        System.out.println(xy2[0]);
        System.out.println(xy2[1]);*/

        centre[0]=(xy1[0]+xy2[0])/2;
        centre[1]=(xy1[1]+xy2[1])/2;


        if (xy1[0]==xy2[0]){
            if (xy1[1]<xy2[1])
                centre[2]=0;
            else
                centre[2]=2;
        }
        else{
            if (xy1[0]<xy2[0])
                centre[2]=1;
            else
                centre[2]=3;
        }

        //System.out.println(centre[0]);
        //System.out.println(centre[1]);
        return centre;
    }

    public void addElement(ActionEvent actionEvent){
        addElementDialogue();
    }

    public void hidePercentage(){
        bar.setVisible(false);
        percentage.setVisible(false);
    }





    public void outputListener(){
        run();
        tabPane.getSelectionModel().select(outputTab);
    }

    public void updateOutputTextArea()  {

        StringBuilder text= new StringBuilder();
        File file = new File(Main.outputPath);
        try {
            Scanner scanner=new Scanner(file);
            while (scanner.hasNextLine()){
                String hold=scanner.nextLine();
                text.append(hold).append("\n");

            }
            outputArea.setText(text.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void updateTextArea()  {
        dttf.setText("");
        timetf.setText("");
        ditf.setText("");
        dvtf.setText("");
        StringBuilder text= new StringBuilder();
        File file = new File(Main.path);
        try {
            Scanner scanner=new Scanner(file);
            while (scanner.hasNextLine()){
                String hold=scanner.nextLine();
                text.append(hold).append("\n");
                Pattern pattern=Pattern.compile("^d([IiVvTt])\\s+((\\d+\\.?\\d*)([pnumkMGx]?))$");
                Matcher matcher=pattern.matcher(hold);
                if (matcher.find()){
                    String sth=matcher.group(1).toLowerCase();
                    switch (sth) {
                        case "t":
                            dttf.setText(matcher.group(2));
                            break;
                        case "i":
                            ditf.setText(matcher.group(2));
                            break;
                        case "v":
                            dvtf.setText(matcher.group(2));
                            break;
                        default:
                            //oh shit
                            break;
                    }

                }

                pattern=Pattern.compile("\\.tran\\s+((\\d+\\.?\\d*)([pnumkMGx]?))$");
                matcher=pattern.matcher(hold);
                if (matcher.find()){
                    timetf.setText(matcher.group(1));
                }

            }
            codeArea.setText(text.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() throws IOException {
        String text=codeArea.getText();
        File dataFile=new File(Main.path);
        FileWriter fw=new FileWriter(dataFile);
        BufferedWriter writer = new BufferedWriter(fw);
        String[] lines=text.split("\n");
        for (String line:lines){
            writer.write(line);
            writer.newLine();
        }
        writer.close();
        fw.close();
    }

   /* public void updateProgress(int p){
        percentage.setText(String.valueOf(p/10)+"."+String.valueOf(p%10)+"%");
    }
*/
    public void addElementDialogue(){
        Element element=null;

        Stage window= new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Add Element");
        window.setMinWidth(250);
        VBox layout1= new VBox();
        layout1.setPadding(new Insets(10,50,10,50));


        javafx.scene.control.Label label=new javafx.scene.control.Label("Select the type of element");
        ComboBox comboBox=new ComboBox();
        comboBox.getItems().addAll("Resistor","Capacitor","Inductor","Diode 1","Diode 2",
                "VSource DC","VSource AC", "ISource DC","ISource AC",
                "ESource", "HSource", "GSource", "FSource");
        Button next=new Button("Next");
        Button cancel=new Button("Cancel");

        layout1.setAlignment(Pos.CENTER);
        HBox buttons= new HBox(next,cancel);
        buttons.setAlignment(Pos.CENTER);
        layout1.setSpacing(9);
        buttons.setSpacing(14);
        layout1.getChildren().addAll(label,comboBox,buttons);

        Scene scene1=new Scene(layout1);

        next.setOnAction(event1 -> {
            if (comboBox.getValue()!=null){
                Scene scene2=new Scene(getLayout((String) comboBox.getValue(),window));
                window.setScene(scene2);
            }
        });
        cancel.setOnAction(event1 -> window.close());

        window.setScene(scene1);
        window.show();


        addElement(element);
    }

    public VBox getLayout(String type, Stage window){
        window.setTitle("New "+type);
        String line="";
        VBox layout= new VBox();
        layout.setPadding(new Insets(10,50,10,50));

        javafx.scene.control.Label label=new javafx.scene.control.Label("Enter the parameters");
        Button add=new Button("Add");
        Button cancel=new Button("Cancel");
        GridPane grid=new GridPane();
        layout.setAlignment(Pos.CENTER);
        HBox buttons= new HBox(add,cancel);
        buttons.setAlignment(Pos.CENTER);
        layout.setSpacing(9);
        buttons.setSpacing(14);
        layout.getChildren().addAll(label,grid);
        add.setOnAction(event -> {
            InputManager manager=InputManager.getInstance();
            manager.input=line;
            if (manager.checkInputFormat()) // TODO: 20/07/06 DIALOGUE!!!
                window.close();
        });
        cancel.setOnAction(event1 -> window.close());

        javafx.scene.control.Label n=new javafx.scene.control.Label("Node 1");
        TextField name=new TextField();

        javafx.scene.control.Label n1=new javafx.scene.control.Label("Node 1");
        TextField node1=new TextField();

        javafx.scene.control.Label n2=new javafx.scene.control.Label("Node 2");
        TextField node2=new TextField();

        javafx.scene.control.Label val=new Label();
        TextField value=new TextField();

        grid.setHgap(10);
        grid.setVgap(7);

        grid.add(n,0,0);
        grid.add(name,1,0);
        grid.add(n1,0,1);
        grid.add(node1,1,1);
        grid.add(n2,0,2);
        grid.add(node2,1,2);






        if (type.equals("Resistor")){
            val.setText("Resistance");
            grid.add(val,0,3);
            grid.add(value,1,3);
        }
        if (type.equals("Capacitor")){
            val.setText("Capacity");
            grid.add(val,0,3);
            grid.add(value,1,3);
        }
        if (type.equals("Inductor")){
            val.setText("Inductance");
            grid.add(val,0,3);
            grid.add(value,1,3);
        }
        if (type.equals("Diode 1")){

        }
        if (type.equals("Diode 2")){

        }

        if (type.equals("VSource DC")){
            val.setText("Offset Value");
            grid.add(val,0,3);
            grid.add(value,1,3);

        }
        if (type.equals("VSource AC")){
            val.setText("Offset Value");
            grid.add(val,0,3);
            grid.add(value,1,3);

        }
        if (type.equals("ISource DC")){
            val.setText("Offset Value");
            grid.add(val,0,3);
            grid.add(value,1,3);
        }
        if (type.equals("ISource AC")){
            val.setText("Offset Value");
            grid.add(val,0,3);
            grid.add(value,1,3);

        }

        if (type.equals("ESource")){

        }
        if (type.equals("HSource")){

        }

        if (type.equals("GSource")){

        }
        if (type.equals("FSource")){

        }
        layout.getChildren().addAll(buttons);


        return layout;
    }

    public void addElement(Element element){

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateTextArea();
        percentage.setVisible(false);
        bar.setVisible(false);

        /*try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }*/
        // Main.w0.hide();

    }

}
