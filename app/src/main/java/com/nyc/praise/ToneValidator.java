package com.nyc.praise;

/**
 * Created by Wayne Kellman on 7/19/18.
 */

interface ToneValidator {
    void locationFailed();

    void textIsEmpty();

    void toneValid();

    void toneInvalid();

    PraiseModel getPraiseModel(String key);
}
