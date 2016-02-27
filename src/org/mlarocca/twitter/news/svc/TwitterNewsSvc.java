package org.mlarocca.twitter.news.svc;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.glassfish.jersey.server.ParamException;
import org.mlarocca.twitter.news.clients.TwitterClient;
import org.mlarocca.twitter.news.data.News;
import org.mlarocca.twitter.news.data.NewsResult;
import org.mlarocca.twitter.news.memcache.MemcacheClient;
import org.mlarocca.twitter.news.utils.Utils;

import twitter4j.Status;
import twitter4j.TwitterException;

import javax.ws.rs.PathParam;

@Path("/")
public class TwitterNewsSvc {
  private static final int MAX_NEWS_LIFE_SECONDS = 60;
  private static final String MEMCACHE_KEY_PREFIX = "TWTTR_NEWS";
  public static final String DEFAULT_ACCOUNT = "Atooma_team";
  public static final int DEFAULT_COUNT = 10;

  private static TwitterClient twitterClient = new TwitterClient();
  
  private static Optional<MemcacheClient<String>> memcacheClient = Optional.empty();
  
  private static Logger logger = Utils.getConsoleLogger();

  public static void setMemcacheClient(MemcacheClient<String> memCacheClient) {
    memcacheClient = Optional.of(memCacheClient);
  }
  
  @GET
  @Produces("text/html")
  public String getHome(){
    return String.format("<h1>Home</h1><p>Usage: /twitter/news/<account name>/<tweet count></p>");
  }
  
  @GET
  @Path("/twitter/news")
  @Produces("application/json")
  public String getWithDefaultCount(){
    return doGetNews(DEFAULT_ACCOUNT, DEFAULT_COUNT);
  }
  
  @GET
  @Path("/twitter/news/{count}")
  @Produces("application/json")
  public String getWithCount(@PathParam("count") String count){
    int c = parseCount(count);
    return doGetNews(DEFAULT_ACCOUNT, c);
  }
  
  @GET
  @Path("/twitter/news/{account}/{count}")
  @Produces("application/json")
  public String getFromAccountWithCount(@PathParam("account") String account, @PathParam("count") String count){
    int c = parseCount(count);
    return doGetNews(account, c);
  }
  
  private Integer parseCount(String count) throws ParamException.PathParamException {
    try {
      return Integer.parseInt(count);
    } catch (NumberFormatException e) {
      throw new ParamException.PathParamException(e, "count", count);
    }
  }
  
  protected static News tweetToNews(String accountUrl, Status tweet) {
    return new News(tweet.getText(), accountUrl + tweet.getId());
  }
  
  /**
   * Retrieves the list of tweets for the account passed, if any.
   * First checks if a memcacheClient has been set, and if so,
   * tries to retrieve the result from memcache.
   * If the result is stored on memcache, it checks that it's not stale.
   * In this case, it returns the result stored on memcache.
   * If any of the above conditions fails, fetches the results through Twitter API.
   * 
   * @param account The screen name whose tweets we are interested in.
   * @param count How many tweets we need.
   * @return
   */
  protected static String doGetNews(String account, int count) {
    validateAccount(account);
    validateCount(count);
    
    return memcacheClient.flatMap(client -> {
      String key = buildMemcacheKey(account, count);
      Optional<String> val = client.get(key);
      return val.flatMap(resultJson -> {
        try {
          NewsResult result = NewsResult.fromString(resultJson);
          logger.info("Getting from cache " + key);
          if (result.isStale(MAX_NEWS_LIFE_SECONDS)) {
            logger.info("Cache content expired" + key);
            return Optional.empty();
          }
          return Optional.of(result.getNews());
        } catch (IOException e) {
          return Optional.empty();
        }
      });
    }).orElseGet(new Supplier<String>() {
      @Override
      public String get() {
        String news = fetchNewsList(account, count).toString();
        logger.info(String.format("Live query %s %d", account, count));

        memcacheClient.ifPresent(client -> {
          NewsResult result = new NewsResult(news);
          String key = buildMemcacheKey(account, count);
          logger.info("Storing in memcache " + key);
          client.set(key, result.toString());
        });
        return news;
      }
    });
  }
  
  protected static void validateAccount(String account) throws ParamException {
    if (account.length() == 0) {
      throw new ParamException.PathParamException(new Throwable(), "account", account);
    }
  }
  
  protected static void validateCount(Integer count) throws ParamException {
    if (count <= 0) {
      throw new ParamException.PathParamException(new Throwable(), "count", count.toString());
    }
  }
  
  protected static String buildMemcacheKey(String account, int count) {
    return String.format("%s_%s_%d", MEMCACHE_KEY_PREFIX, account, count);
  }
  
  protected static List<News> fetchNewsList(String account, int count) {    
    String accountBaseUrl = String.format("http://twitter.com/%s/status/", account);
    final Function<Status, News> tweetToNewsMap =
      tweet ->  tweetToNews(accountBaseUrl, tweet);
    
    try {
      List<Status> tweets = twitterClient.getTweetsFrom(account, count);
      return tweets.stream()
        .map(tweetToNewsMap)
        .collect(Collectors.toList());
    } catch (TwitterException te) {
      throw new NotFoundException();
    }

  }
} 