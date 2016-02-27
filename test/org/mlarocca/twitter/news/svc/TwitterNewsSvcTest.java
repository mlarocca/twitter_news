package org.mlarocca.twitter.news.svc;

import static org.junit.Assert.*;

import java.util.Date;

import org.glassfish.jersey.server.ParamException;
import org.junit.Test;
import org.mlarocca.twitter.news.data.News;

import twitter4j.ExtendedMediaEntity;
import twitter4j.GeoLocation;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.Place;
import twitter4j.RateLimitStatus;
import twitter4j.Scopes;
import twitter4j.Status;
import twitter4j.SymbolEntity;
import twitter4j.URLEntity;
import twitter4j.User;
import twitter4j.UserMentionEntity;

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
  public void testNewsToTweet() {
    String baseUrl = "http://";
    Status tweet = getStatus("test", 1);
    News test = TwitterNewsSvc.tweetToNews(baseUrl, tweet);
    News expected = new News("test", baseUrl + 1);
    assertEquals(expected, test);
  }
  
  @Test
  public void testValidateFetchTweets() {
    //TODO - Mockito
  }
  
  @Test
  public void testValidateDoGetNews() {
    //TODO - Mockito
  }
  
  private Status getStatus(String text, long tweetId) {
    //TODO: Set up Mockito and use mock
    return new Status() {

      /**
       * 
       */
      private static final long serialVersionUID = 1L;

      @Override
      public int compareTo(Status o) {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public int getAccessLevel() {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public RateLimitStatus getRateLimitStatus() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public ExtendedMediaEntity[] getExtendedMediaEntities() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public HashtagEntity[] getHashtagEntities() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public MediaEntity[] getMediaEntities() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public SymbolEntity[] getSymbolEntities() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public URLEntity[] getURLEntities() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public UserMentionEntity[] getUserMentionEntities() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public long[] getContributors() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Date getCreatedAt() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public long getCurrentUserRetweetId() {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public int getFavoriteCount() {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public GeoLocation getGeoLocation() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public long getId() {
        return tweetId;
      }

      @Override
      public String getInReplyToScreenName() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public long getInReplyToStatusId() {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public long getInReplyToUserId() {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public String getLang() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Place getPlace() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Status getQuotedStatus() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public long getQuotedStatusId() {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public int getRetweetCount() {
        // TODO Auto-generated method stub
        return 0;
      }

      @Override
      public Status getRetweetedStatus() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public Scopes getScopes() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public String getSource() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public String getText() {
        return text;
      }

      @Override
      public User getUser() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public String[] getWithheldInCountries() {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public boolean isFavorited() {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public boolean isPossiblySensitive() {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public boolean isRetweet() {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public boolean isRetweeted() {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public boolean isRetweetedByMe() {
        // TODO Auto-generated method stub
        return false;
      }

      @Override
      public boolean isTruncated() {
        // TODO Auto-generated method stub
        return false;
      }
    };
  }
}
