/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import io.undertow.util.HeaderMap;
/*     */ import io.undertow.util.HeaderValues;
/*     */ import io.undertow.util.HttpString;
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
/*     */ class UndertowHeadersAdapter
/*     */   implements MultiValueMap<String, String>
/*     */ {
/*     */   private final HeaderMap headers;
/*     */   
/*     */   UndertowHeadersAdapter(HeaderMap headers) {
/*  47 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFirst(String key) {
/*  53 */     return this.headers.getFirst(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(String key, @Nullable String value) {
/*  58 */     this.headers.add(HttpString.tryFromString(key), value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(String key, List<? extends String> values) {
/*  64 */     this.headers.addAll(HttpString.tryFromString(key), values);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(MultiValueMap<String, String> values) {
/*  69 */     values.forEach((key, list) -> this.headers.addAll(HttpString.tryFromString(key), list));
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(String key, @Nullable String value) {
/*  74 */     this.headers.put(HttpString.tryFromString(key), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAll(Map<String, String> values) {
/*  79 */     values.forEach((key, list) -> this.headers.put(HttpString.tryFromString(key), list));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> toSingleValueMap() {
/*  84 */     Map<String, String> singleValueMap = new LinkedHashMap<>(this.headers.size());
/*  85 */     this.headers.forEach(values -> (String)singleValueMap.put(values.getHeaderName().toString(), values.getFirst()));
/*     */     
/*  87 */     return singleValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  92 */     return this.headers.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  97 */     return (this.headers.size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 102 */     return (key instanceof String && this.headers.contains((String)key));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 107 */     return (value instanceof String && this.headers
/* 108 */       .getHeaderNames().stream()
/* 109 */       .map(this.headers::get)
/* 110 */       .anyMatch(values -> values.contains(value)));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> get(Object key) {
/* 116 */     if (key instanceof String) {
/* 117 */       return (List<String>)this.headers.get((String)key);
/*     */     }
/* 119 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> put(String key, List<String> value) {
/* 125 */     HeaderValues previousValues = this.headers.get(key);
/* 126 */     this.headers.putAll(HttpString.tryFromString(key), value);
/* 127 */     return (List<String>)previousValues;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> remove(Object key) {
/* 133 */     if (key instanceof String) {
/* 134 */       this.headers.remove((String)key);
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends List<String>> map) {
/* 141 */     map.forEach((key, values) -> this.headers.putAll(HttpString.tryFromString(key), values));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 147 */     this.headers.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/* 152 */     return (Set<String>)this.headers.getHeaderNames().stream()
/* 153 */       .map(HttpString::toString)
/* 154 */       .collect(Collectors.toSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<List<String>> values() {
/* 159 */     return (Collection<List<String>>)this.headers.getHeaderNames().stream()
/* 160 */       .map(this.headers::get)
/* 161 */       .collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, List<String>>> entrySet() {
/* 166 */     return new AbstractSet<Map.Entry<String, List<String>>>()
/*     */       {
/*     */         public Iterator<Map.Entry<String, List<String>>> iterator() {
/* 169 */           return new UndertowHeadersAdapter.EntryIterator();
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 174 */           return UndertowHeadersAdapter.this.headers.size();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 182 */     return HttpHeaders.formatHeaders(this);
/*     */   }
/*     */   
/*     */   private class EntryIterator
/*     */     implements Iterator<Map.Entry<String, List<String>>>
/*     */   {
/* 188 */     private Iterator<HttpString> names = UndertowHeadersAdapter.this.headers.getHeaderNames().iterator();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 192 */       return this.names.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<String, List<String>> next() {
/* 197 */       return new UndertowHeadersAdapter.HeaderEntry(this.names.next());
/*     */     }
/*     */     
/*     */     private EntryIterator() {}
/*     */   }
/*     */   
/*     */   private class HeaderEntry implements Map.Entry<String, List<String>> {
/*     */     private final HttpString key;
/*     */     
/*     */     HeaderEntry(HttpString key) {
/* 207 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getKey() {
/* 212 */       return this.key.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> getValue() {
/* 217 */       return (List<String>)UndertowHeadersAdapter.this.headers.get(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> setValue(List<String> value) {
/* 222 */       HeaderValues headerValues = UndertowHeadersAdapter.this.headers.get(this.key);
/* 223 */       UndertowHeadersAdapter.this.headers.putAll(this.key, value);
/* 224 */       return (List<String>)headerValues;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/UndertowHeadersAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */