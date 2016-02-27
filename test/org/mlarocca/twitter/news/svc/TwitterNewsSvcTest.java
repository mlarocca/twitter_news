package org.mlarocca.twitter.news.svc;

import static org.junit.Assert.*;

import org.glassfish.jersey.server.ParamException;
import org.junit.Test;

public class TwitterNewsSvcTest {

  @Test(expected = ParamException.class)
  public void testValidateAccountFail() {
    TwitterNewsSvc.validateAccount("");
  }

  @Test(expected = ParamException.class)
  public void testValidateCountFail() {
    TwitterNewsSvc.validateCount(-1);
  }
  
  @Test(expected = ParamException.class)
  public void testValidateCountFailOnZero() {
    TwitterNewsSvc.validateCount(0);
  }
  
  @Test
  public void testValidateFetchTweets() {
    //TODO - Mockito
  }
  
  @Test
  public void testValidateDoGetNews() {
    //TODO - Mockito
  }
}
