package com.nyc.praise;

import android.util.Log;

import com.ibm.watson.developer_cloud.http.ServiceCallback;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.DocumentAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import java.util.List;

/**
 * Created by Wayne Kellman on 6/10/18.
 */

public class PraiseToneAnalyzer {
    private static final String VERSION = "2017-09-21";
    private static final String USERNAME = "a35e3812-0762-447e-a1e4-2d50d3baa881";
    private static final String PASSWORD = "LQFBhBFrgsu7";
    private static final String URL = "https://gateway.watsonplatform.net/tone-analyzer/api";
    private static final String TAG = "PraiseToneAnalyzer";
    private ToneAnalyzer toneAnalyzer;
    private static final String ANGER = "Anger";
    private static final String FEAR = "Fear";
    private static final String SADNESS = "Sadness";
    public ToneResponse toneResponse;

    public PraiseToneAnalyzer() {
        toneAnalyzer = new ToneAnalyzer(
                VERSION,
                USERNAME,
                PASSWORD);
        toneAnalyzer.setEndPoint(URL);

    }

    public void serviceCall(String text, ToneResponse response) {
        toneResponse = response;
        ToneOptions toneOptions = new ToneOptions.Builder().text(text).build();
        toneAnalyzer.tone(toneOptions).enqueue(new ServiceCallback<ToneAnalysis>() {
            @Override
            public void onResponse(ToneAnalysis response) {
                DocumentAnalysis analysis = response.getDocumentTone();
                boolean isToneGood = checkTone(analysis);
                toneResponse.onToneResponse(isToneGood);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private boolean checkTone(DocumentAnalysis analysis) {
        if (toneAnalyzer == null) {
            return false;
        }
        List<ToneScore> toneScores = analysis.getTones();
        for (ToneScore t : toneScores) {
            if ((t.getToneName().equals(ANGER)
                    || t.getToneName().equals(SADNESS)
                    || t.getToneName().equals(FEAR))
                    && t.getScore() > 0.6) {
                return false;
            }
        }
        return true;
    }

    interface ToneResponse {
        void onToneResponse(boolean isToneGood);
    }
}
