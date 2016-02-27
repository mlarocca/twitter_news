package org.mlarocca.twitter.news.clients;

import java.util.List;
import java.util.Optional;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterClient {
  Twitter twitter = new TwitterFactory().getInstance();
  Optional<AccessToken> maybeAccessToken;
  
  public List<Status> getTweetsFrom(String screenName, int count) throws TwitterException {
    Paging paging = new Paging(1, count);
    return twitter.getUserTimeline(parseScreenName(screenName), paging);
  }
  
  protected static String parseScreenName(String screenName) {
    if (screenName.startsWith("@")) {
      screenName = screenName.replaceAll("^@+", "");
    }
    return screenName;
  }
}
