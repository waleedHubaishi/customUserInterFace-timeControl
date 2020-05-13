package ch.fhnw.cuie.timecontrol;

import javafx.beans.property.*;
import javafx.css.PseudoClass;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeControl extends Control {

    private static final String CONVERTIBLE_REGEX = "now|(\\d{1,2}[:]{0,1}\\d{0,2})";
    private static final String TIME_FORMAT_REGEX = "\\d{2}:\\d{2}";

    private static final String FORMATTED_TIME_PATTERN = "HH:mm";

    private static final Pattern CONVERTIBLE_PATTERN = Pattern.compile(CONVERTIBLE_REGEX);
    private static final Pattern TIME_FORMAT_PATTERN = Pattern.compile(TIME_FORMAT_REGEX);

    private final ObjectProperty<LocalTime> time  = new SimpleObjectProperty<>();
    private final StringProperty label = new SimpleStringProperty();

    private static final PseudoClass MANDATORY_CLASS = PseudoClass.getPseudoClass("mandatory");

    private static final PseudoClass INVALID_CLASS = PseudoClass.getPseudoClass("invalid");


    private final BooleanProperty mandatory = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(MANDATORY_CLASS, get());
        }
    };

    private final BooleanProperty invalid = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(INVALID_CLASS, get());
        }
    };

    private final BooleanProperty readOnly = new SimpleBooleanProperty();

    private final StringProperty userFacing = new SimpleStringProperty();

    public TimeControl() {
        initializeSelf();
    }

    private void initializeSelf() {
        getStyleClass().add("TimeControl");
        userFacing.addListener((observable, oldValue, newValue) -> {
                try {
                    Matcher z = CONVERTIBLE_PATTERN.matcher(newValue);
                    if (z.matches()) {
                        Matcher q = TIME_FORMAT_PATTERN.matcher(newValue);
                        if(q.matches()){
                            Pattern p = Pattern.compile(".*(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9].*");
                            Matcher r = p.matcher(newValue);
                            if (r.matches()) {
                                setTime(LocalTime.parse(newValue));
                                setInvalid(false);
                                notifyInvalid(false);
                            }
                            else if (r.matches() == false){
                                setInvalid(true);
                                notifyInvalid(true);
                            }
                        }
                        else {
                            TimeSkin.setToInvestigate(true);
                        }
                    }
                    else if (z.matches() == false){
                        setInvalid(true);
                        notifyInvalid(true);
                    }


                } catch (Exception e) {

                }

        });

        timeProperty().addListener((observable, oldValue, newValue) -> {
            setUserFacing(newValue.toString());
        });

    }

    public void notifyInvalid(boolean isInvalid){
        TimeSkin.setInvalid(isInvalid);
        //System.out.println(TimeSkin.isInvalid());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TimeSkin(this);
    }

    public LocalTime getTime() {
        return time.get();
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public boolean isMandatory() {
        return mandatory.get();
    }

    public BooleanProperty mandatoryProperty() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory.set(mandatory);
    }

    public boolean isReadOnly() {
        return readOnly.get();
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly.set(readOnly);
    }


    public String getUserFacing() {
        return userFacing.get();
    }

    public StringProperty userFacingProperty() {
        return userFacing;
    }

    public void setUserFacing(String userFacing) {
        this.userFacing.set(userFacing);
    }

    public boolean isInvalid() {
        return invalid.get();
    }

    public BooleanProperty invalidProperty() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid.set(invalid);
    }

    public void increaseHour(){
        setTime(getTime().plusHours(1));
    }

    public void decreaseHour(){
        setTime(getTime().minusHours(1));
    }

    public void increaseFivteenMinutes(){
        setTime(getTime().plusMinutes(15));
    }

    public void decreaseFivteenMinutes(){
        setTime(getTime().minusMinutes(15));
    }


}
