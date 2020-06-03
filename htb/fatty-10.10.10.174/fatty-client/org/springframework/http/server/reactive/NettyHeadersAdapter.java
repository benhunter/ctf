/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
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
/*     */ class NettyHeadersAdapter
/*     */   implements MultiValueMap<String, String>
/*     */ {
/*     */   private final HttpHeaders headers;
/*     */   
/*     */   NettyHeadersAdapter(HttpHeaders headers) {
/*  45 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getFirst(String key) {
/*  52 */     return this.headers.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(String key, @Nullable String value) {
/*  57 */     this.headers.add(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(String key, List<? extends String> values) {
/*  62 */     this.headers.add(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(MultiValueMap<String, String> values) {
/*  67 */     values.forEach(this.headers::add);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(String key, @Nullable String value) {
/*  72 */     this.headers.set(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAll(Map<String, String> values) {
/*  77 */     values.forEach(this.headers::set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> toSingleValueMap() {
/*  82 */     Map<String, String> singleValueMap = new LinkedHashMap<>(this.headers.size());
/*  83 */     this.headers.entries()
/*  84 */       .forEach(entry -> {
/*     */           if (!singleValueMap.containsKey(entry.getKey())) {
/*     */             singleValueMap.put(entry.getKey(), entry.getValue());
/*     */           }
/*     */         });
/*  89 */     return singleValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  94 */     return this.headers.names().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  99 */     return this.headers.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 104 */     return (key instanceof String && this.headers.contains((String)key));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 109 */     return (value instanceof String && this.headers
/* 110 */       .entries().stream()
/* 111 */       .anyMatch(entry -> value.equals(entry.getValue())));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> get(Object key) {
/* 117 */     if (containsKey(key)) {
/* 118 */       return this.headers.getAll((String)key);
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> put(String key, @Nullable List<String> value) {
/* 126 */     List<String> previousValues = this.headers.getAll(key);
/* 127 */     this.headers.set(key, value);
/* 128 */     return previousValues;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> remove(Object key) {
/* 134 */     if (key instanceof String) {
/* 135 */       List<String> previousValues = this.headers.getAll((String)key);
/* 136 */       this.headers.remove((String)key);
/* 137 */       return previousValues;
/*     */     } 
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends List<String>> map) {
/* 144 */     map.forEach(this.headers::add);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 149 */     this.headers.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/* 154 */     return this.headers.names();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<List<String>> values() {
/* 159 */     return (Collection<List<String>>)this.headers.names().stream()
/* 160 */       .map(this.headers::getAll).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, List<String>>> entrySet() {
/* 165 */     return new AbstractSet<Map.Entry<String, List<String>>>()
/*     */       {
/*     */         public Iterator<Map.Entry<String, List<String>>> iterator() {
/* 168 */           return new NettyHeadersAdapter.EntryIterator();
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 173 */           return NettyHeadersAdapter.this.headers.size();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 181 */     return HttpHeaders.formatHeaders(this);
/*     */   }
/*     */   
/*     */   private class EntryIterator
/*     */     implements Iterator<Map.Entry<String, List<String>>>
/*     */   {
/* 187 */     private Iterator<String> names = NettyHeadersAdapter.this.headers.names().iterator();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 191 */       return this.names.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<String, List<String>> next() {
/* 196 */       return new NettyHeadersAdapter.HeaderEntry(this.names.next());
/*     */     }
/*     */     
/*     */     private EntryIterator() {}
/*     */   }
/*     */   
/*     */   private class HeaderEntry implements Map.Entry<String, List<String>> {
/*     */     private final String key;
/*     */     
/*     */     HeaderEntry(String key) {
/* 206 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getKey() {
/* 211 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> getValue() {
/* 216 */       return NettyHeadersAdapter.this.headers.getAll(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> setValue(List<String> value) {
/* 221 */       List<String> previousValues = NettyHeadersAdapter.this.headers.getAll(this.key);
/* 222 */       NettyHeadersAdapter.this.headers.set(this.key, value);
/* 223 */       return previousValues;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/NettyHeadersAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */