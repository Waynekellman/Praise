package com.nyc.praise;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

/**
 * Created by Wayne Kellman on 7/21/18.
 */
public class WritePraisePresenterTest {

    ToneValidator validator = Mockito.mock(ToneValidator.class);
    PraiseToneAnalyzer analyzer = Mockito.mock(PraiseToneAnalyzer.class);

    PraiseToneAnalyzer.ToneResponse response = Mockito.mock(PraiseToneAnalyzer.ToneResponse.class);

    WritePraisePresenter writePraisePresenter;

    @Before
    public void setUp() throws Exception {
        writePraisePresenter = new WritePraisePresenter(validator, analyzer);
    }

    @Test
    public void analyzePraiseToneCallsAnalyzer(){
        String text = "Text";
        writePraisePresenter.analyzePraiseTone(text, "location");
        Mockito.verify(analyzer).serviceCall(anyString(),any());
    }
    @Test
    public void analyzePraiseToneCallsVerifierWhenTextIsEmptyAndLocationIsNotFound(){
        String text = "";
        String location = Constants.NOT_FOUND;
        writePraisePresenter.analyzePraiseTone(text,location);
        Mockito.verify(validator).textIsEmpty();
        Mockito.verify(validator).locationFailed();
    }
    @Test
    public void analyzePraiseToneCallsVerifierWhenTextIsEmpTy(){
        String text = "";
        String location = "SomePlace";
        writePraisePresenter.analyzePraiseTone(text,location);
        Mockito.verify(validator).textIsEmpty();
        Mockito.verify(validator, never()).locationFailed();
    }
    @Test
    public void analyzePraiseToneCallsVerifierLocationNotFound(){
        String text = "some text";
        String location = Constants.NOT_FOUND;
        writePraisePresenter.analyzePraiseTone(text,location);
        Mockito.verify(validator, never()).textIsEmpty();
        Mockito.verify(validator).locationFailed();
    }

    @Test
    public void analyzeCommentToneCallAnalyzer(){
        writePraisePresenter.analyzeCommentTone(new PraiseModel(),"test","user");
        Mockito.verify(analyzer).serviceCall(eq("test"),any());
    }
    @Test
    public void analyzeCommentToneCallVerifierWhenTextEmpty(){
        writePraisePresenter.analyzeCommentTone(new PraiseModel(),"","user");
        Mockito.verify(validator).textIsEmpty();
    }

    @Test
    public void goodToneWritesToDatabase(){
        writePraisePresenter.writeCommentToDatabase(new PraiseModel(),"", "", true);
        Mockito.verify(validator).toneValid();
    }
    @Test
    public void badToneDoesNotWritesToDatabase(){
        writePraisePresenter.writeCommentToDatabase(new PraiseModel(),"", "", false);
        Mockito.verify(validator).toneInvalid();
    }
}