package com.nyc.praise;

/**
 * Created by Wayne Kellman on 7/17/18.
 */

class LoginValidator implements ILoginValidator {

    public LoginValidator() {
    }

    @Override
    public boolean isColorSelected(int colorButtonID) {
        return colorButtonID != -1;
    }

    @Override
    public boolean isIconSelected(int iconButtonID) {
        return iconButtonID != -1;
    }

    @Override
    public boolean isNameValid(String userName) {
        return userName.length() > 5;
    }
}
