package org.mlarocca.twitter.news.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Utils {
  private static Logger logger = Logger.getLogger("rest");
  static {
    ConsoleHandler handler = new ConsoleHandler();
    handler.setLevel(Level.ALL);
    handler.setFormatter(new SimpleFormatter());
    logger.addHandler(handler);
  }
  
  public static Logger getConsoleLogger() {
    return logger;
  }
}
