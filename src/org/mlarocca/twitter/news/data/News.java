package org.mlarocca.twitter.news.data;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mlarocca.twitter.news.utils.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class News {
 private String content ="";
 private String url ="";
 
 private static ObjectMapper objectMapper = new ObjectMapper();
 private static Logger logger = Utils.getConsoleLogger();
 
 public News() {
   
 }
 
 public News(String content, String url) {
   setContent(content);
   setUrl(url);
 }
 
 public String getContent() {
  return content;
 }
 
 private void setContent(String content) {
  this.content = content;
 }
 
 public String getUrl() {
  return url;
 }
 
 private void setUrl(String url) {
  this.url = url;
 }

 @Override
 public String toString() {
   try {
     return objectMapper.writeValueAsString(this);
   } catch (JsonProcessingException jpe) {
     logger.log(Level.WARNING, "News parsing error", jpe);
     return "";
   }
 }
 
 @Override
 public int hashCode() {
   return (content + url).hashCode();
 }
 
 @Override
 public boolean equals(Object other) {
   if (other == null ||
       other.getClass() != this.getClass() ||
       other.hashCode() != this.hashCode()) {
     return false;
   } else {
     return true;
   }
 }
}