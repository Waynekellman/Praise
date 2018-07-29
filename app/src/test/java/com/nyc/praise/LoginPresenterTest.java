package com.nyc.praise;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

/**
 * Created by Wayne Kellman on 7/21/18.
 */
public class LoginPresenterTest {

    LoginView view = mock(LoginView.class);
    LoginValidator validator = new LoginValidator();
    LoginPresenter presenter;

    @Before
    public void setUp() throws Exception {
        presenter = new LoginPresenter(view, validator);
    }

    @Test
    public void navigateToMainScreenOnCorrectInputs(){
        presenter.checkLogin(1,1,"WayneK");
        Mockito.verify(view).navigateToMainScreen();
        Mockito.verify(view, never()).failColor();
        Mockito.verify(view, never()).failName();
        Mockito.verify(view, never()).failIcon();

    }

    @Test
    public void viewFailsOnWrongColor(){
        presenter.checkLogin(-1,1,"WayneK");
        Mockito.verify(view).failColor();
        Mockito.verify(view, never()).failName();
        Mockito.verify(view, never()).failIcon();
        Mockito.verify(view, never()).navigateToMainScreen();
    }
    @Test
    public void viewFailsOnWrongName(){
        presenter.checkLogin(1,1,"");
        Mockito.verify(view,never()).failColor();
        Mockito.verify(view).failName();
        Mockito.verify(view, never()).failIcon();
        Mockito.verify(view, never()).navigateToMainScreen();
    }
    @Test
    public void viewFailsOnWrongIcon(){
        presenter.checkLogin(1,-1,"WayneK");
        Mockito.verify(view,never()).failColor();
        Mockito.verify(view, never()).failName();
        Mockito.verify(view).failIcon();
        Mockito.verify(view, never()).navigateToMainScreen();
    }
    @Test
    public void viewFailsOnWrongIconAndName(){
        presenter.checkLogin(1,-1,"");
        Mockito.verify(view,never()).failColor();
        Mockito.verify(view).failName();
        Mockito.verify(view).failIcon();
        Mockito.verify(view, never()).navigateToMainScreen();
    }

    @Test
    public void viewFailsOnWrongColorAndName(){

        presenter.checkLogin(-1,1,"");
        Mockito.verify(view).failColor();
        Mockito.verify(view).failName();
        Mockito.verify(view, never()).failIcon();
        Mockito.verify(view, never()).navigateToMainScreen();

    }
    @Test
    public void viewFailsOnWrongColorAndNameAndIcon(){

        presenter.checkLogin(-1,-1,"");
        Mockito.verify(view).failColor();
        Mockito.verify(view).failName();
        Mockito.verify(view).failIcon();
        Mockito.verify(view, never()).navigateToMainScreen();

    }
}