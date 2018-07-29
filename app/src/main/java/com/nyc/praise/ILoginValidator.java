package com.nyc.praise;

/**
 * Created by Wayne Kellman on 7/17/18.
 */

interface ILoginValidator {
    boolean isColorSelected(int colorButtonID);
    boolean isIconSelected(int iconButtonID);
    boolean isNameValid(String userName);
}
