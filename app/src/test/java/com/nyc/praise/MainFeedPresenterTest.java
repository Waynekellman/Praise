package com.nyc.praise;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static com.nyc.praise.GPSHelper.*;
import static com.nyc.praise.MainFeedPresenter.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Created by Wayne Kellman on 7/21/18.
 */
public class MainFeedPresenterTest {


    @Mock GPSHelper gpsHelper;
    GetLocation location = Mockito.mock(GetLocation.class);
    LocationInterface locationInterface = Mockito.mock(LocationInterface.class);
    @Mock IMainFeed iMainFeed;


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void setGPS() throws Exception {
        MainFeedPresenter presenter = new MainFeedPresenter(gpsHelper, iMainFeed);
        presenter.setGPS(location);
        Mockito.verify(gpsHelper).setLocationInterface(any());
        Mockito.verify(gpsHelper,Mockito.atLeastOnce()).startGoogleApiClient();
    }

}