package org.mlarocca.twitter.news.data;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mlarocca.twitter.news.utils.Utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class NewsResult {
  private static final long NANOSECONDS_IN_SECONDS = (long) 1e9;
  private static ObjectMapper objectMapper = new ObjectMapper();
  private static Logger logger = Utils.getConsoleLogger();
  
  private Long createdAt;
  private String news;
  
  private static String resultToString(NewsResult result) {
    try {
      return objectMapper.writeValueAsString(result);
    } catch (JsonProcessingException jpe) {
      logger.log(Level.SEVERE, "Can't serialize NewsResult", jpe);
      return null;
    }
  }
  
  public static NewsResult fromString(String json) throws IOException {
    try {
      JsonNode node = objectMapper.readValue(json, JsonNode.class);
  
      JsonNode newsNode = node.get("news");
      String news = newsNode.asText();

      JsonNode createdAtNode = node.get("createdAt");
      long createdAt = createdAtNode.asLong();
  
      return new NewsResult(news, createdAt);
    } catch (IOException ioe) {
      logger.log(Level.SEVERE, "Can't unmarshal NewsResult: Unparsable Json", ioe);
      throw ioe;
    }
  }
  
  public NewsResult(String news) {
    this.news = news;
    this.createdAt = System.nanoTime();
  }
  
  protected NewsResult(String news, long createdAt) {
    this.news = news;
    this.createdAt = createdAt;
  }
  
  public String getNews() {
    return news;
  }
  
  /**
   * Checks if the result is stale, according 
   * 
   * @param maxAge Maximum age for the result to be valid, in seconds.
   */
  public boolean isStale(long maxAge) {
    return System.nanoTime() - createdAt > maxAge * NANOSECONDS_IN_SECONDS;
  }

  @Override
  public String toString() {
    return resultToString(this);
  }
  
}
