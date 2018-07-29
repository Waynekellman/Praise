package com.nyc.praise;

/**
 * Created by Wayne Kellman on 7/17/18.
 */

class LoginPresenter {
    private LoginView view;
    LoginValidator validator;

    public LoginPresenter(LoginView loginView, LoginValidator validator) {
        view = loginView;
        this.validator = validator;

    }

    public void checkLogin(int colorButtonID, int iconButtonID, String userNameString) {
        boolean isColorSelected = validator.isColorSelected(colorButtonID);
        boolean isIconSelected = validator.isIconSelected(iconButtonID);
        boolean isNameValid = validator.isNameValid(userNameString);

        if (isColorSelected && isIconSelected && isNameValid){
            view.navigateToMainScreen();
        }
        if (!isNameValid){
            view.failName();
        }
        if (!isIconSelected){
            view.failIcon();
        }
        if (!isColorSelected){
            view.failColor();
        }
    }
}
