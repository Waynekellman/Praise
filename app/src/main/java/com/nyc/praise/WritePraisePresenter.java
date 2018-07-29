package com.nyc.praise;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Wayne Kellman on 7/19/18.
 */

class WritePraisePresenter {
    private ToneValidator toneValidator;
    private PraiseToneAnalyzer toneAnalyzer;

    WritePraisePresenter(ToneValidator toneValidator, PraiseToneAnalyzer toneAnalyzer) {
        this.toneValidator = toneValidator;
        this.toneAnalyzer = toneAnalyzer;
    }

    void analyzePraiseTone(String text, String currentLocation) {
        if (!currentLocation.equals(Constants.NOT_FOUND) && !text.isEmpty()) {
            if (!text.isEmpty()) {
                toneAnalyzer.serviceCall(text, isToneGood -> writeToPraiseDatabase(currentLocation, isToneGood));
            }
        } else if (currentLocation.equals(Constants.NOT_FOUND) && text.isEmpty()){
            toneValidator.textIsEmpty();
            toneValidator.locationFailed();
        } else if (currentLocation.equals(Constants.NOT_FOUND)){
            toneValidator.locationFailed();
        } else {
            toneValidator.textIsEmpty();
        }

    }

    private void writeToPraiseDatabase(String currentLocation, boolean isToneGood) {

            if (isToneGood) {
                DatabaseReference updatePost = getDatabaseReferenceForPraise(currentLocation);
                String key = updatePost.push().getKey();
                PraiseModel model = toneValidator.getPraiseModel(key);
                updatePost.child(key).setValue(model);
                toneValidator.toneValid();

            } else {
                toneValidator.toneInvalid();

            }

    }

    private DatabaseReference getDatabaseReferenceForPraise(String currentLocation) {
        return FirebaseDatabase.getInstance().getReference()
                .child(Constants.FEED)
                .child(currentLocation);
    }


    void analyzeCommentTone(PraiseModel model, String text, String userName) {
        if (!text.isEmpty()) {
            toneAnalyzer.serviceCall(text, isToneGood -> writeCommentToDatabase(model, text, userName, isToneGood));
        } else {
            toneValidator.textIsEmpty();
        }
    }

    void writeCommentToDatabase(PraiseModel model, String text, String userName, boolean isToneGood) {
        if (isToneGood) {
            if (userName != null && !userName.isEmpty()) {

                DatabaseReference commentsReference = getDatabaseReferenceForComments(model);
                CommentModel commentModel = createCommentModel(userName, text);
                commentsReference.push().setValue(commentModel);
            }

        } else {
            toneValidator.toneInvalid();
        }

        toneValidator.toneValid();
    }

    private DatabaseReference getDatabaseReferenceForComments(PraiseModel model) {
        return FirebaseDatabase.getInstance().getReference()
                .child(Constants.FEED)
                .child(model.getLocation())
                .child(model.getuId())
                .child(Constants.COMMENTS);
    }

    CommentModel createCommentModel(String userName, String text) {
        CommentModel commentModel = new CommentModel();
        commentModel.setName(userName);
        commentModel.setComment(text);
        return commentModel;
    }
}