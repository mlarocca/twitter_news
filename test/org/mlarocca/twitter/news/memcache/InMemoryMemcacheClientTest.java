package org.mlarocca.twitter.news.memcache;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Optional;

public class InMemoryMemcacheClientTest {

  private static InMemoryMemcacheClientImpl<String> client;
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    client = new InMemoryMemcacheClientImpl<>();
  }

  @Test(expected = UnsupportedOperationException.class)  
  public void testCas() {
    client.cas("", "", 0);
  }
  @Test
  public void testUnsuccessfulGet() {
    assertEquals(Optional.empty(), client.get("k2"));
  }
  
  @Test
  public void testSetAndGet() {
    client.set("k1", "v1");
    assertEquals(Optional.of("v1"), client.get("k1"));
  }
  
  @Test
  public void testUpdateAndGet() {
    client.set("k1", "v1");
    assertEquals(Optional.of("v1"), client.get("k1"));
    client.set("k1", "v1.1");
    assertEquals(Optional.of("v1.1"), client.get("k1"));
  }
  
  @Test
  public void testSetOnFull() {
    InMemoryMemcacheClientImpl<Double> client = new InMemoryMemcacheClientImpl<>(2);

    client.set("k1", 1.);
    client.set("k2", 2.);
    client.set("k3", 3.);
    assertEquals(Optional.empty(), client.get("k1"));
    assertEquals(Optional.of(2.), client.get("k2"));
    assertEquals(Optional.of(3.), client.get("k3"));
  }
  
  @Test
  public void testConcurrentSet() {
    //TODO
  }
}
