package com.swiftsynq.medmanager;

import android.provider.Telephony;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import com.swiftsynq.medmanager.Model.History;
import com.swiftsynq.medmanager.Model.Medication;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by popoolaadebimpe on 18/04/2018.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SQLiteTest {
    MedManagerTbOperations medManagerTbOperations;
    @Before
    public void setUp(){
        medManagerTbOperations = new MedManagerTbOperations(InstrumentationRegistry.getTargetContext());
        medManagerTbOperations.openwritable();
    }

    @After
    public void finish() {
        medManagerTbOperations.close();
    }
    @Test
    public void testPreConditions() {
        assertNotNull(medManagerTbOperations);
    }

    @Test
    public void testShouldAddMedication() throws Exception {
       Medication medication= new Medication();
       medication.setDrugName("Malaria Drug");
       medication.setInterval("5");
       medication.setDecsription("Take with warm water");
       medication.setEnddate("23 May,2018");
       medication.setStartdate("23 June,2018");
       int row=  (int)medManagerTbOperations.insert(medication);

        assertEquals(row, medManagerTbOperations.Retrieve().size());

    }
    @Test
    public void testShouldAddHistory() throws Exception {
        History history= new History();
        history.setPillName("Malaria Drug");
        history.setMinuteTaken(10);
        history.setHourTaken(20);
        history.setDateString("23 May,2018");
        int row=  (int)medManagerTbOperations.insertHistory(history);

        assertEquals(row, medManagerTbOperations.getHistory().size());

    }
    @Test
    public void testRetrieveMedications() {
        List<Medication> medications = medManagerTbOperations.Retrieve();

        assertFalse(medications.isEmpty());
    }
    @Test
    public void testRetrieveHistories() {
        List<History> histories = medManagerTbOperations.getHistory();

        assertFalse(histories.isEmpty());
    }



}
