package org.springframework.util;

import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;

public interface MultiValueMap<K, V> extends Map<K, List<V>> {
  @Nullable
  V getFirst(K paramK);
  
  void add(K paramK, @Nullable V paramV);
  
  void addAll(K paramK, List<? extends V> paramList);
  
  void addAll(MultiValueMap<K, V> paramMultiValueMap);
  
  void set(K paramK, @Nullable V paramV);
  
  void setAll(Map<K, V> paramMap);
  
  Map<K, V> toSingleValueMap();
}


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/MultiValueMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */