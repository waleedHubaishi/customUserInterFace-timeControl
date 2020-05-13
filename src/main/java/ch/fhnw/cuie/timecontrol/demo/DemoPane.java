package ch.fhnw.cuie.timecontrol.demo;

import ch.fhnw.cuie.timecontrol.TimeControl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class DemoPane extends BorderPane {
    PresentationModel pm = new PresentationModel();
    private TimeControl businessControl;

    private Label timeLabel;
    private Label settingLabel;
    private Label readOnlyLabel;
    private Label mandatoryLabel;
    private Label manualInputLabel;
    private Label hourLabel;
    private Label minutesLabel;

    private Slider hourSlider;
    private Slider minuteSlider;

    private CheckBox  readOnlyBox;
    private CheckBox  mandatoryBox;
    private TextField labelField;

    private final StringProperty label = new SimpleStringProperty("Time (HH:mm)");
    private final BooleanProperty mandatory = new SimpleBooleanProperty(true);
    private final BooleanProperty readOnly = new SimpleBooleanProperty(false);


    DemoPane(PresentationModel pm) {
        this.pm = pm;
        initializeControls();
        layoutControls();
        addValueChangeListeners();
        addBindings();
    }

    private void initializeControls() {

        String stylesheet = getClass().getResource("../style.css").toExternalForm();
        getStylesheets().add(stylesheet);

        setPadding(new Insets(10));

        businessControl = new TimeControl();

        settingLabel = new Label("Zeiteinstellungen");
        getStyleClass().add("settinglabel");
        settingLabel.getStyleClass().add("settinglabel");

        timeLabel = new Label();
        getStyleClass().add("DemoPane");
        timeLabel.getStyleClass().add("timelabel");

        readOnlyLabel = new Label("Schreibgeschützt");
        getStyleClass().add("checkboxlabel");
        readOnlyLabel.getStyleClass().add("checkboxlabel");

        mandatoryLabel = new Label("Pflicht");
        //getStyleClass().add("checkboxlabel");
        mandatoryLabel.getStyleClass().add("checkboxlabel");

        manualInputLabel = new Label("Manuelle Eingabe");
        getStyleClass().add("manualInputlabel");
        manualInputLabel.getStyleClass().add("manualInputlabel");

        hourSlider = new Slider(0, 23, 0);
        minuteSlider = new Slider(0, 59, 0);

        readOnlyBox = new CheckBox();
        readOnlyBox.setSelected(false);


        hourLabel = new Label("Hours");
        hourLabel.getStyleClass().add("hoursLabel");

        minutesLabel = new Label("Minutes");
        minutesLabel.getStyleClass().add("minutesLabel");


        mandatoryBox = new CheckBox();
        mandatoryBox.setSelected(true);

        labelField = new TextField("Time (HH:mm)");
    }

    private void layoutControls() {
        setCenter(businessControl);
        VBox hourBox = new VBox(hourLabel, hourSlider);
        VBox minBox = new VBox(minutesLabel, minuteSlider);


        VBox box = new VBox(10, settingLabel, timeLabel, hourBox,minBox,readOnlyLabel,
                            readOnlyBox, mandatoryLabel, mandatoryBox, manualInputLabel, labelField);


        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void addValueChangeListeners() {
        ChangeListener<Number> sliderListener = (observable, oldValue, newValue) ->
                pm.setStartTime(LocalTime.of((int) hourSlider.getValue(), (int) minuteSlider.getValue()));


        hourSlider.valueProperty().addListener(sliderListener);
        minuteSlider.valueProperty().addListener(sliderListener);

        pm.startTimeProperty().addListener((observable, oldValue, newValue) -> updateSliders());

        labelField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                String newTime = null;
                Matcher m = Pattern.compile("\\((.*?)\\)").matcher(labelField.getText());
                while (m.find()) {
                    newTime = (m.group(1));
                }
                Pattern p = Pattern.compile(".*(0[1-9]|[0-2][0-3]|[1-12]):[0-5][0-9].*");
                Matcher z = p.matcher(newTime);
                if (z.matches()) {
                    pm.setStartTime(LocalTime.parse(newTime));
                    //System.out.println("yes");
                } else {
                    //System.out.println("no");
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        });

        updateSliders();

    }

    private void updateSliders() {
        LocalTime startTime = pm.getStartTime();

        hourSlider.setValue(startTime.getHour());
        minuteSlider.setValue(startTime.getMinute());
        labelField.setText("Time("+timeLabel.getText()+")");

    }

    private void addBindings() {
        timeLabel.textProperty().bind(pm.startTimeProperty().asString());

        mandatoryBox.selectedProperty().bindBidirectional(pm.mandatoryProperty());
        readOnlyBox.selectedProperty().bindBidirectional(pm.readOnlyProperty());
        labelField.textProperty().bindBidirectional(pm.labelProperty());

        businessControl.timeProperty().bindBidirectional(pm.startTimeProperty());
        businessControl.labelProperty().bind(pm.labelProperty());

        businessControl.mandatoryProperty().bind(pm.mandatoryProperty());
        businessControl.readOnlyProperty().bind(pm.readOnlyProperty());
    }

}
