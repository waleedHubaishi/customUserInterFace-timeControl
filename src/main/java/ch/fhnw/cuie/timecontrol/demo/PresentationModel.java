package ch.fhnw.cuie.timecontrol.demo;


import javafx.beans.property.*;

import java.time.LocalTime;

public class PresentationModel {
    private final ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>(LocalTime.now());
    private final StringProperty label     = new SimpleStringProperty("Time (HH:mm)");
    private final BooleanProperty mandatory = new SimpleBooleanProperty(true);
    private final BooleanProperty           readOnly  = new SimpleBooleanProperty(false);


    public LocalTime getStartTime() {
        return startTime.get();
    }

    public ObjectProperty<LocalTime> startTimeProperty() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime.set(startTime);
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
}
