/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TomcatHeadersAdapter
/*     */   implements MultiValueMap<String, String>
/*     */ {
/*     */   private final MimeHeaders headers;
/*     */   
/*     */   TomcatHeadersAdapter(MimeHeaders headers) {
/*  50 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFirst(String key) {
/*  56 */     return this.headers.getHeader(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(String key, @Nullable String value) {
/*  61 */     this.headers.addValue(key).setString(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(String key, List<? extends String> values) {
/*  66 */     values.forEach(value -> add(key, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(MultiValueMap<String, String> values) {
/*  71 */     values.forEach(this::addAll);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(String key, @Nullable String value) {
/*  76 */     this.headers.setValue(key).setString(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAll(Map<String, String> values) {
/*  81 */     values.forEach(this::set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> toSingleValueMap() {
/*  86 */     Map<String, String> singleValueMap = new LinkedHashMap<>(this.headers.size());
/*  87 */     keySet().forEach(key -> (String)singleValueMap.put(key, getFirst(key)));
/*  88 */     return singleValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  93 */     Enumeration<String> names = this.headers.names();
/*  94 */     int size = 0;
/*  95 */     while (names.hasMoreElements()) {
/*  96 */       size++;
/*  97 */       names.nextElement();
/*     */     } 
/*  99 */     return size;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 104 */     return (this.headers.size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 109 */     if (key instanceof String) {
/* 110 */       return (this.headers.findHeader((String)key, 0) != -1);
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 117 */     if (value instanceof String) {
/* 118 */       MessageBytes needle = MessageBytes.newInstance();
/* 119 */       needle.setString((String)value);
/* 120 */       for (int i = 0; i < this.headers.size(); i++) {
/* 121 */         if (this.headers.getValue(i).equals(needle)) {
/* 122 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> get(Object key) {
/* 132 */     if (containsKey(key)) {
/* 133 */       return Collections.list(this.headers.values((String)key));
/*     */     }
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> put(String key, List<String> value) {
/* 141 */     List<String> previousValues = get(key);
/* 142 */     this.headers.removeHeader(key);
/* 143 */     value.forEach(v -> this.headers.addValue(key).setString(v));
/* 144 */     return previousValues;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> remove(Object key) {
/* 150 */     if (key instanceof String) {
/* 151 */       List<String> previousValues = get(key);
/* 152 */       this.headers.removeHeader((String)key);
/* 153 */       return previousValues;
/*     */     } 
/* 155 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends List<String>> map) {
/* 160 */     map.forEach(this::put);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 165 */     this.headers.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/* 170 */     Set<String> result = new HashSet<>(8);
/* 171 */     Enumeration<String> names = this.headers.names();
/* 172 */     while (names.hasMoreElements()) {
/* 173 */       result.add(names.nextElement());
/*     */     }
/* 175 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<List<String>> values() {
/* 180 */     return (Collection<List<String>>)keySet().stream().map(this::get).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, List<String>>> entrySet() {
/* 185 */     return new AbstractSet<Map.Entry<String, List<String>>>()
/*     */       {
/*     */         public Iterator<Map.Entry<String, List<String>>> iterator() {
/* 188 */           return new TomcatHeadersAdapter.EntryIterator();
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 193 */           return TomcatHeadersAdapter.this.headers.size();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 201 */     return HttpHeaders.formatHeaders(this);
/*     */   }
/*     */   
/*     */   private class EntryIterator
/*     */     implements Iterator<Map.Entry<String, List<String>>>
/*     */   {
/* 207 */     private Enumeration<String> names = TomcatHeadersAdapter.this.headers.names();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 211 */       return this.names.hasMoreElements();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<String, List<String>> next() {
/* 216 */       return new TomcatHeadersAdapter.HeaderEntry(this.names.nextElement());
/*     */     }
/*     */     
/*     */     private EntryIterator() {}
/*     */   }
/*     */   
/*     */   private final class HeaderEntry implements Map.Entry<String, List<String>> {
/*     */     private final String key;
/*     */     
/*     */     HeaderEntry(String key) {
/* 226 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getKey() {
/* 231 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public List<String> getValue() {
/* 237 */       return TomcatHeadersAdapter.this.get(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public List<String> setValue(List<String> value) {
/* 243 */       List<String> previous = getValue();
/* 244 */       TomcatHeadersAdapter.this.headers.removeHeader(this.key);
/* 245 */       TomcatHeadersAdapter.this.addAll(this.key, value);
/* 246 */       return previous;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/TomcatHeadersAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */