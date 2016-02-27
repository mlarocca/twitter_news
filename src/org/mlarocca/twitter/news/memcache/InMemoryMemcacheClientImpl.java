package org.mlarocca.twitter.news.memcache;

import java.util.Comparator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class InMemoryMemcacheClientImpl<T> implements MemcacheClient<T> {

  private static final Comparator<Entry> EntryComparator = new Comparator<InMemoryMemcacheClientImpl.Entry>() {    
    @Override
    public int compare(Entry o1, Entry o2) {
      return o1.compareTo(o2);
    }
  };
  
  private static final int MAX_ELEMENTS = (int) 1e5;
  
  private ConcurrentHashMap<String, T> cache;
  private Queue<Entry> keyQueue;
  private int maxElements;

  public InMemoryMemcacheClientImpl(){
    this(MAX_ELEMENTS);
  }
  
  public InMemoryMemcacheClientImpl(int maxElements){
    cache = new ConcurrentHashMap<>(maxElements);
    keyQueue = new PriorityBlockingQueue<>(maxElements, EntryComparator);
    this.maxElements = maxElements;
  }

  @Override
  public Optional<T> get(String key) {
    if (cache.containsKey(key)) {
      return Optional.of(cache.get(key));
    } else {
      return Optional.empty();
    }
  }

  /**
   * Set a value in the queue, possibly overriding the old value with the same key.
   * In this case, we don't modify the priority of the element of the queue: 
   * this would require linear time, as stdlib queue removal of a random element is O(n)
   * and moreover it does not update the queue, so rebuilding the queue would be necessary.
   * 
   * @param key
   * @param value
   * @return true iff another value for the same key was stored and overwritten;
   *         false otherwise.
   */
  @Override
  public boolean set(String key, T value) {
    if (cache.containsKey(key)) {
      cache.put(key, value);
      return true;
    } else {
      removeOldest();
      cache.put(key, value);
      keyQueue.add(new Entry(key));
      return false;
    }
  }

  @Override
  public boolean cas(String key, T value, long id) {
    throw new UnsupportedOperationException();
  }
  
  private synchronized void removeOldest() {
    if (!keyQueue.isEmpty() && keyQueue.size() == maxElements) {
      Entry oldest = keyQueue.remove();
      cache.remove(oldest.key);
    }
  }
  
  private static class Entry implements Comparable<Entry>{
    private static long time;
    
    private long id;
    private String key;
    
    public Entry(String key) {
      this.key = key;
      this.id = generateId();
    }
    
    private static long generateId() {
      return ++time;
    }
    
    @Override
    public int compareTo(Entry other) {
      if (this.id == other.id) {
        return 0;
      } else if (this.id < other.id) {
        return -1;
      } else {
        return 1;
      }
    }
  }
}
