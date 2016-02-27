package org.mlarocca.twitter.news.clients;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class TwitterClientTest {

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
  }

  @Test
  public void testScreenNameParser() {
    assertEquals("mlarocca", TwitterClient.parseScreenName("@mlarocca"));
    assertEquals("mlarocca", TwitterClient.parseScreenName("mlarocca"));
  }

}
