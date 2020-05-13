package ch.fhnw.cuie.timecontrol;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;

import java.time.LocalTime;

public class DropDownChooser extends VBox {
    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "dropDownChooser.css";

    private final TimeControl timeControl;

    // todo: add all your controls here

    private Button plus;
    private Button minus;

    private Label hourLabel;
    private Label minutesLabel;

    private Slider hourSlider;
    private Slider minuteSlider;


    public DropDownChooser(TimeControl timeControl) {
        this.timeControl = timeControl;
        initializeSelf();
        initializeParts();
        layoutParts();
        setupEventHandlers();
        setupBindings();
        addValueChangeListeners();
    }

    private void initializeSelf() {
        getStyleClass().add("dropDownChooser");

        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        getStylesheets().add(stylesheet);
    }

    private void initializeParts() {

        hourLabel = new Label("Hour");
        minutesLabel = new Label("Minute");


        plus = new Button("+1 Hour");
        minus = new Button("-1 Hour");

        hourSlider = new Slider(0, 23, 0);
        minuteSlider = new Slider(0, 59, 0);
    }

    private void layoutParts() {
        VBox hourBox = new VBox(hourLabel, hourSlider);
        VBox minBox = new VBox(minutesLabel, minuteSlider);

        VBox mainDropBox = new VBox(10,plus, minus,hourBox,minBox);

        getChildren().addAll(mainDropBox);
    }

    private void setupEventHandlers(){
        plus.setOnAction(event -> timeControl.increaseHour());
        minus.setOnAction(event -> timeControl.decreaseHour());
    }

    private void setupBindings() {


    }

    private void addValueChangeListeners() {
        ChangeListener<Number> sliderListener = (observable, oldValue, newValue) ->
                timeControl.setTime(LocalTime.of((int) hourSlider.getValue(), (int) minuteSlider.getValue()));


        hourSlider.valueProperty().addListener(sliderListener);
        minuteSlider.valueProperty().addListener(sliderListener);

        timeControl.timeProperty().addListener((observable, oldValue, newValue) -> updateSliders());

        updateSliders();

    }

    private void updateSliders() {
        LocalTime startTime = timeControl.getTime();

        hourSlider.setValue(startTime.getHour());
        minuteSlider.setValue(startTime.getMinute());

    }
}
