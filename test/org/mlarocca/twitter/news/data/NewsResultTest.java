package org.mlarocca.twitter.news.data;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class NewsResultTest {

  @Test
  public void testToJson() {
    String tmp = new NewsResult("test", 0).toString();
    assertEquals(tmp, "{\"createdAt\":0,\"news\":\"test\"}");
  }
  
  public void testFromJson() throws IOException{
    NewsResult tmp = NewsResult.fromString("{\"news\":[], \"createdAt: 100\"}");
    assertEquals(tmp.getNews(), "[]");
  }
  
  public void testJsonSimmetry() throws IOException{
    NewsResult tmp = new NewsResult("{\"news\":[], \"test: 100\"}");   
    assertEquals(tmp, NewsResult.fromString(tmp.toString()));
  }
  
  public void testIsExpired() throws IOException, InterruptedException{
    NewsResult tmp = NewsResult.fromString("{\"news\":[], \"createdAt: " + System.nanoTime() + "\"}");
    Thread.sleep(1100);
    assertTrue(tmp.isStale(1));
    assertFalse(tmp.isStale(3));
  }
}
