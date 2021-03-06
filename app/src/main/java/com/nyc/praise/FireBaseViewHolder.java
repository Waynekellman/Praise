package com.nyc.praise;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Wayne Kellman on 5/15/18.
 */

public class FireBaseViewHolder extends RecyclerView.ViewHolder {

    private TextView likeCount, commentsCount, praiseMessage, userNameMainFeed, elapsedTime;
    ImageView likeImg, commentImg, iconImg;


    public FireBaseViewHolder(View itemView) {
        super(itemView);

        likeCount = itemView.findViewById(R.id.count_likes);
        commentsCount = itemView.findViewById(R.id.count_comments);
        likeImg = itemView.findViewById(R.id.like_icon);
        praiseMessage = itemView.findViewById(R.id.praise_message);
        commentImg = itemView.findViewById(R.id.comments_icon);
        userNameMainFeed = itemView.findViewById(R.id.user_name_main_feed);
        elapsedTime = itemView.findViewById(R.id.elapsed_time);
        iconImg = itemView.findViewById(R.id.praise_icon);
    }

    public void OnBind(PraiseModel model) {

        if (model != null && model.getLocation() != null) {

            if (model.getLikes() != null) {
                likeCount.setText(String.valueOf(model.getLikes().size()));
            } else {
                likeCount.setText("0");
            }
            if (model.getComments() != null) {
                commentsCount.setText(String.valueOf(model.getComments().size()));
            } else {
                commentsCount.setText("0");
            }
            if (model.getUserName() != null) {
                userNameMainFeed.setText(model.getUserName());
            }
            if (model.getDate() != null) {
                long timeStamp = SystemClock.elapsedRealtime() - model.getDate();
                elapsedTime.setText(getElapsedTimeMinutesSecondsString((int) timeStamp));
            }
            if (model.getIconResource() > 0) {
                iconImg.setColorFilter(ContextCompat.getColor(itemView.getContext(), model.getColorResource()), PorterDuff.Mode.SRC_ATOP);
                iconImg.setImageResource(model.getIconResource());
            }

            praiseMessage.setText(model.getMessage());


        }
    }

    public static String getElapsedTimeMinutesSecondsString(int milliseconds) {
        int elapsedTime = milliseconds;
        elapsedTime = elapsedTime / 1000;
        int secs = elapsedTime % 60;
        int mins = elapsedTime / 60;
        int hrs = mins / 60;
        int days = hrs / 24;
        int years = days / 365;
        if (mins < 1) {
            return secs + " seconds ago";
        } else if (hrs < 1) {
            return mins + " minutes ago";
        } else if (days < 1) {
            return hrs + " hours ago";
        } else if (years < 1) {
            return days + " days ago";
        } else {
            return years + " years ago";
        }
    }
}
