package ch.fhnw.cuie.timecontrol;

import javafx.application.Platform;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


public class TimeControlTest {
    @BeforeClass
    public static void initJavaFX() {
        PlatformImpl.startup(() -> {
            //nothing to do
        });
        Platform.setImplicitExit(false);
    }

    private TimeControl control;

    @Before
    public void setup() {
        control = new TimeControl();
    }

    @Test
    public void testSomething() {
        //given

        //when

        //then
    }

}