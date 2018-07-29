package com.nyc.praise;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Wayne Kellman on 7/21/18.
 */
public class LoginValidatorTest {

    LoginValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new LoginValidator();
    }

    @Test
    public void colorIsSelectedReturnTrue(){
        assertEquals(true,validator.isColorSelected(1));
    }
    @Test
    public void colorIsSelectedReturnFalse(){
        assertEquals(false,validator.isColorSelected(-1));
    }
    @Test
    public void iconIsSelectedReturnFalse(){
        assertEquals(false,validator.isIconSelected(-1));
    }
    @Test
    public void iconIsSelectedReturnTrue(){
        assertEquals(true,validator.isIconSelected(1));
    }
    @Test
    public void nameIsValidReturnTrue(){
        assertEquals(true,validator.isNameValid("sfsfefese"));
    }
    @Test
    public void nameIsValidReturnFalse(){
        assertEquals(false,validator.isNameValid(""));
    }
}