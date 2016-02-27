package org.mlarocca.twitter.news;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.mlarocca.twitter.news.memcache.InMemoryMemcacheClientImpl;
import org.mlarocca.twitter.news.svc.TwitterNewsSvc;

public class TwitterServer {

  public static void main(String[] args) throws Exception {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");

    Server jettyServer = new Server(8080);
    jettyServer.setHandler(context);

    ServletHolder jerseyServlet = context.addServlet(
         org.glassfish.jersey.servlet.ServletContainer.class, "/*");
    jerseyServlet.setInitOrder(0);

    TwitterNewsSvc.setMemcacheClient(new InMemoryMemcacheClientImpl<String>());
    
    // Tells the Jersey Servlet which REST service/class to load.
    jerseyServlet.setInitParameter(
       "jersey.config.server.provider.classnames",
       TwitterNewsSvc.class.getCanonicalName());
    
    try {
        jettyServer.start();
        jettyServer.join();
    } finally {
        jettyServer.destroy();
    }
  }
}