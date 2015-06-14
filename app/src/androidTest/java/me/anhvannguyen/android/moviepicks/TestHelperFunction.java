package me.anhvannguyen.android.moviepicks;

import android.test.AndroidTestCase;


public class TestHelperFunction extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testThatDemoAssertions() throws Throwable {
        int a = 5;
        int b = 3;
        int c = 5;
        int d = 10;

        assertEquals("(a, c) should be equal", a, c);
        assertTrue("(d > a) should be true", d > a);
        assertFalse("(a == b) should be false", a == b);
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
