package com.nyc.praise;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Wayne Kellman on 7/19/18.
 */

class WritePraisePresenter {
    private ToneValidator toneValidator;
    private PraiseToneAnalyzer toneAnalyzer;
    private String TAG = WritePraisePresenter.class.getSimpleName();

    public WritePraisePresenter(ToneValidator toneValidator) {
        this.toneValidator = toneValidator;
        toneAnalyzer = new PraiseToneAnalyzer();
    }

    public void analyzePraiseTone(String text, String currentLocation) {
        Log.d(TAG, "analyzePraiseTone: " + currentLocation);
        if (!text.isEmpty()) {
            toneAnalyzer.serviceCall(text, isToneGood -> writeToPraiseDatabase(currentLocation, isToneGood));
        }

    }

    private void writeToPraiseDatabase(String currentLocation, boolean isToneGood) {
        Log.d(TAG, "writeToPraiseDatabase: called");
        if (isToneGood) {
            DatabaseReference updatePost = getDatabaseReferenceForPraise(currentLocation);
            String key = updatePost.push().getKey();
            PraiseModel model = toneValidator.getPraiseModel(key);
            updatePost.child(key).setValue(model);
            Log.d(TAG, "writeToPraiseDatabase: praise posted");
            toneValidator.toneValid();

        } else {
            Log.d(TAG, "writeToPraiseDatabase: call invalid");
            toneValidator.toneInvalid();

        }
    }

    private DatabaseReference getDatabaseReferenceForPraise(String currentLocation) {
        return FirebaseDatabase.getInstance().getReference()
                .child(Constants.FEED)
                .child(currentLocation);
    }


    public void analyzeCommentTone(PraiseModel model, String text, String userName) {
        toneAnalyzer.serviceCall(text, isToneGood -> {
            if (isToneGood) {
                if (!userName.isEmpty()) {

                    DatabaseReference commentsReference = getDatabaseReferenceForComments(model);
                    CommentModel commentModel = createCommentModel(userName, text);
                    commentsReference.push().setValue(commentModel);
                }

            } else {
                toneValidator.toneInvalid();
            }

            toneValidator.toneValid();
        });
    }

    private DatabaseReference getDatabaseReferenceForComments(PraiseModel model) {
        return FirebaseDatabase.getInstance().getReference()
                .child(Constants.FEED)
                .child(model.getLocation())
                .child(model.getuId())
                .child(Constants.COMMENTS);
    }

    private CommentModel createCommentModel(String userName, String text) {
        CommentModel commentModel = new CommentModel();
        commentModel.setName(userName);
        commentModel.setComment(text);
        return commentModel;
    }
}