import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
  protected LinkedList<Pair>[] table = null;
  private int size;

  /**
   * Constructor
   *
   * @param capacity capacity of hashtable map
   */
  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity) {
    table = (LinkedList<Pair>[]) new LinkedList[capacity];
    size = 0;
  }

  /**
   * Default constructor with capacity of 64
   */
  public HashtableMap() {
    this(64);
  }

  protected class Pair {
    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }
  }

  /**
   * Adds a key value pair to the hashtable
   *
   * @param key   the key to insert
   * @param value the value that key maps to
   * @throws NullPointerException     if the key is null
   * @throws IllegalArgumentException if the key already exists in the map
   */
  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    if (key == null)
      throw new NullPointerException();
    int index = Math.abs(key.hashCode()) % table.length;

    if (table[index] == null) {
      table[index] = new LinkedList<>();
    } else {
      for (Pair pair : table[index]) {
        if (pair.key.equals(key)) {
          throw new IllegalArgumentException("Key already exists");
        }
      }
    }

    table[index].add(new Pair(key, value));
    size++;

    if ((double) size / table.length >= 0.8)
      resize();
  }

  /**
   * Checks if a key is contained in the hashtable
   *
   * @param key the key to look for
   * @return true if the key exists, false otherwise
   */
  @Override
  public boolean containsKey(KeyType key) {
    if (key == null)
      return false;
    int index = Math.abs(key.hashCode()) % table.length;

    if (table[index] == null) {
      return false;
    }

    for (Pair pair : table[index]) {
      if (pair.key.equals(key))
        return true;
    }

    return false;
  }

  /**
   * Retrieves the value associated with a given key
   *
   * @param key the key to look up
   * @return the value paired with the key
   * @throws NoSuchElementException if the key isn't found
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    if (key == null)
      throw new NoSuchElementException();
    int index = Math.abs(key.hashCode()) % table.length;

    if (table[index] != null) {
      for (Pair pair : table[index]) {
        if (pair.key.equals(key))
          return pair.value;
      }
    }

    throw new NoSuchElementException();
  }

  /**
   * Removes a pair from the map
   *
   * @param key the key to remove
   * @return the value associated with the key
   * @throws NoSuchElementException if the key is not found
   */
  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    if (key == null)
      throw new NoSuchElementException();
    int index = Math.abs(key.hashCode()) % table.length;

    if (table[index] != null) {
      for (int i = 0; i < table[index].size(); i++) {
        Pair pair = table[index].get(i);
        if (pair.key.equals(key)) {
          table[index].remove(i);
          size--;
          return pair.value;
        }
      }
    }

    throw new NoSuchElementException();
  }

  /**
   * Removes all pairs from the hashtable
   */
  @Override
  public void clear() {
    for (int i = 0; i < table.length; i++) {
      table[i] = null;
    }
    size = 0;
  }

  /**
   * Returns the number of pairs in the hashtable
   *
   * @return the size of the hashtable
   */
  @Override
  public int getSize() {
    return size;
  }

  /**
   * Returns the capacity of the hashtable
   *
   * @return the hashtable capacity
   */
  @Override
  public int getCapacity() {
    return table.length;
  }

  /**
   * Resizes the hashtable by doubling its capacity and rehashing all pairs.
   */
  @SuppressWarnings("unchecked")
  private void resize() {
    LinkedList<Pair>[] oldTable = table;
    table = (LinkedList<Pair>[]) new LinkedList[oldTable.length * 2];
    size = 0;

    for (LinkedList<Pair> list : oldTable) {
      if (list != null) {
        for (Pair pair : list) {
          put(pair.key, pair.value);
        }
      }
    }
}
	public List<KeyType> getKeys() {
  List<KeyType> keys = new LinkedList<>();
  for (LinkedList<Pair> bucket : table) {
    if (bucket != null) {
      for (Pair pair : bucket) {
        keys.add(pair.key);
      }
    }
  }
  return keys;
  }

  /**
   * Tests the put() and get() methods for the Hashtable
   */
  @Test
  public void test1() {
    HashtableMap<Integer, String> table = new HashtableMap<>();
    table.put(3, "Ethan");
    assertEquals(table.get(3), "Ethan");
  }

  /**
   * Test that an IllegalArgumentException is thrown when inserting a duplicate key.
   */
  @Test
  public void test2() {
    HashtableMap<Integer, String> table = new HashtableMap<>();
    table.put(5, "Hello");
    assertThrows(IllegalArgumentException.class, () -> table.put(5, "Again"));
  }

  /**
   * Tests that the containsKey() returns the correct information
   */
  @Test
  public void test3() {
    HashtableMap<Integer, String> table = new HashtableMap<>();
    table.put(5, "five");
    assertTrue(table.containsKey(5));
    assertFalse(table.containsKey(10));
  }

  /**
   * Tests that the remove() method removes the value and returns it
   */
  @Test
  public void test4() {
    HashtableMap<Integer, String> table = new HashtableMap<>();
    table.put(12, "me");
    String removed = table.remove(12);
    assertEquals("me", removed);
    assertFalse(table.containsKey(12));
  }

  /**
   * Tests that the clear() method removes all pairs and resets the size to 0
   */
  @Test
  public void test5() {
    HashtableMap<Integer, String> map = new HashtableMap<>();
    map.put(1, "Ethan");
    map.put(2, "O'Brien");
    map.clear();
    assertEquals(0, map.getSize());
    assertFalse(map.containsKey(1));
    assertFalse(map.containsKey(2));
  }
}

