package ch.fhnw.cuie.timecontrol;

import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class TimeSkin extends SkinBase<TimeControl> {

    private static final String ANGLE_DOWN = "\uf107";
    private static final String ANGLE_UP   = "\uf106";

    private Label placeHolder;
    private TextField editableTimeLeft;

    private Popup popup;
    private Pane dropDownChooser;
    private Button chooserButton;

    private StackPane drawingPane;




    public static BooleanProperty invalid = new SimpleBooleanProperty();


    public static BooleanProperty toInvestigate = new SimpleBooleanProperty();


    TimeSkin(TimeControl control) {
        super(control);
        initializeSelf();
        initializeParts();
        layoutParts();
        setupEventListeners();

        setupValueChangeListeners();
        setupBindings();
    }

    private void setupEventListeners(){
        chooserButton.setOnAction(event -> {
            if (popup.isShowing()) {
                popup.hide();
            } else {
                popup.show(editableTimeLeft.getScene().getWindow());
            }
        });

        popup.setOnShown(event -> {
            chooserButton.setText(ANGLE_UP);
            Point2D location = editableTimeLeft.localToScreen(editableTimeLeft.getWidth() - dropDownChooser.getPrefWidth() - 3,
                    editableTimeLeft.getHeight() -3);

            popup.setX(location.getX());
            popup.setY(location.getY());
        });

        popup.setOnHidden(event -> chooserButton.setText(ANGLE_DOWN));

        invalid.addListener((observable, oldValue, newValue) -> {
            System.out.println(isInvalid());
            Shaker shaker = new Shaker(editableTimeLeft);
            if(isInvalid() == true){
                shaker.shake();
            }
        });

        editableTimeLeft.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.UP)){
                getSkinnable().increaseHour();
            }

            if(event.getCode().equals(KeyCode.DOWN)){
                getSkinnable().decreaseHour();
            }

            if(event.getCode().equals(KeyCode.RIGHT)){
                getSkinnable().increaseFivteenMinutes();
            }

            if(event.getCode().equals(KeyCode.LEFT)){
                getSkinnable().decreaseFivteenMinutes();
            }

            if(event.getCode().equals(KeyCode.ENTER)) {
                if(isToInvestigate() && (editableTimeLeft.getText().equals("now"))){

                    editableTimeLeft.setText(String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))));

                    setInvalid(false);
                    setToInvestigate(false);
                }
                 if(isToInvestigate()){
                    Matcher m = Pattern.compile(".*:.*").matcher(editableTimeLeft.getText());
                    if(m.matches()){
                        String [] timeArray = editableTimeLeft.getText().split(":");

                        for(int i=0;i<timeArray.length;i++){
                            if(timeArray[i].length() == 1){
                                String newtimeComponent = "0"+timeArray[i];
                                timeArray[i] = newtimeComponent;
                            }
                        }
                        String newTime = timeArray[0]+":"+timeArray[1];
                        editableTimeLeft.setText(newTime);
                    }

                     Matcher u = Pattern.compile("\\d{1,2}").matcher(editableTimeLeft.getText());
                      if(u.matches()){
                         String newHour = editableTimeLeft.getText();
                         if(newHour.length() == 1){
                             editableTimeLeft.setText("0"+newHour+":00");
                         }
                         else if(newHour.length() == 2){
                             editableTimeLeft.setText(newHour+":00");
                         }
                     }

                     Matcher z = Pattern.compile("\\d{4}").matcher(editableTimeLeft.getText());
                      if(z.matches()){
                         char [] oldTime = editableTimeLeft.getText().toCharArray();
                          System.out.println("i am in ");
                         String newTime = "";
                         for(int i=0;i<oldTime.length;i++){
                             String temp = newTime;
                             if (i==1){
                                 newTime = temp+oldTime[i]+":";
                             }
                             else{
                                 newTime = temp+String.valueOf(oldTime[i]);
                             }
                         }
                         editableTimeLeft.setText(newTime);
                     }
                }
            }
            if(event.getCode().equals(KeyCode.ESCAPE)) {
            editableTimeLeft.setText(String.valueOf(getSkinnable().getTime()));
            }
            });

    }
    private void initializeSelf() {
        String fonts = getClass().getResource("fonts.css").toExternalForm();
        getSkinnable().getStylesheets().add(fonts);

        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getSkinnable().getStylesheets().add(stylesheet);
    }

    private void initializeParts() {

        editableTimeLeft = new TextField();
        editableTimeLeft.getStyleClass().add("editableField");

        placeHolder = new Label();
        placeHolder.getStyleClass().add("placeholder");


        chooserButton = new Button(ANGLE_DOWN);
        chooserButton.getStyleClass().add("chooserButton");

        dropDownChooser = new DropDownChooser(getSkinnable());

        popup = new Popup();
        popup.getContent().addAll(dropDownChooser);

        drawingPane = new StackPane();
    }

    private void layoutParts() {

        drawingPane.setAlignment(chooserButton, Pos.CENTER_RIGHT);
        drawingPane.getChildren().addAll(editableTimeLeft, chooserButton, placeHolder);
        getChildren().add(drawingPane);
    }

    private void setupValueChangeListeners() {


    }

    private void setupBindings() {

        editableTimeLeft.textProperty().bindBidirectional(getSkinnable().userFacingProperty());

        editableTimeLeft.promptTextProperty().bind(getSkinnable().labelProperty());
        editableTimeLeft.visibleProperty().bind(getSkinnable().readOnlyProperty().not());
        chooserButton.visibleProperty().bind(getSkinnable().readOnlyProperty().not());
        dropDownChooser.visibleProperty().bind(getSkinnable().readOnlyProperty().not());

        placeHolder.textProperty().bind(getSkinnable().timeProperty().asString());
        System.out.println("time is "+placeHolder.getText());
        placeHolder.visibleProperty().bind(getSkinnable().readOnlyProperty());
    }

    public static boolean isInvalid() {
        return invalid.get();
    }

    public static BooleanProperty invalidProperty() {
        return invalid;
    }

    public static void setInvalid(boolean invalid) {
        TimeSkin.invalid.set(invalid);
    }

    public static boolean isToInvestigate() {
        return toInvestigate.get();
}

    public static BooleanProperty toInvestigateProperty() {
        return toInvestigate;
    }

    public static void setToInvestigate(boolean toInvestigate) {
        TimeSkin.toInvestigate.set(toInvestigate);
    }

}

class Shaker {
    private TranslateTransition tt;

    public Shaker(Node node) {
        tt = new TranslateTransition(Duration.millis(50), node);
        tt.setFromX(0f);
        tt.setByX(10f);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
    }

    public void shake() {
        tt.playFromStart();
    }
}
