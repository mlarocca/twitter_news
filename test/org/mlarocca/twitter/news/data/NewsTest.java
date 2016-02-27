package org.mlarocca.twitter.news.data;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class NewsTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @Test
  public void testEquals() {
    assertEquals(new News("test", "1"), new News("test", "1"));
    assertNotEquals(new News("test", "2"), new News("test", "1"));
    assertNotEquals(new News("test", "2"), new News("Test", "2"));
  }

}
