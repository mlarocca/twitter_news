package org.mlarocca.twitter.news.memcache;

import java.util.Optional;

public interface MemcacheClient<T> {
  public Optional<T> get(String key);
  public boolean set(String key, T value);
  public boolean cas(String key, T value, long id);
}
