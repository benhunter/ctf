/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import org.eclipse.jetty.http.HttpField;
/*     */ import org.eclipse.jetty.http.HttpFields;
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
/*     */ class JettyHeadersAdapter
/*     */   implements MultiValueMap<String, String>
/*     */ {
/*     */   private final HttpFields headers;
/*     */   
/*     */   JettyHeadersAdapter(HttpFields headers) {
/*  48 */     this.headers = headers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFirst(String key) {
/*  54 */     return this.headers.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(String key, @Nullable String value) {
/*  59 */     this.headers.add(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(String key, List<? extends String> values) {
/*  64 */     values.forEach(value -> add(key, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(MultiValueMap<String, String> values) {
/*  69 */     values.forEach(this::addAll);
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(String key, @Nullable String value) {
/*  74 */     this.headers.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAll(Map<String, String> values) {
/*  79 */     values.forEach(this::set);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> toSingleValueMap() {
/*  84 */     Map<String, String> singleValueMap = new LinkedHashMap<>(this.headers.size());
/*  85 */     Iterator<HttpField> iterator = this.headers.iterator();
/*  86 */     iterator.forEachRemaining(field -> {
/*     */           if (!singleValueMap.containsKey(field.getName())) {
/*     */             singleValueMap.put(field.getName(), field.getValue());
/*     */           }
/*     */         });
/*  91 */     return singleValueMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  96 */     return this.headers.getFieldNamesCollection().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 101 */     return (this.headers.size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 106 */     return (key instanceof String && this.headers.containsKey((String)key));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 111 */     return (value instanceof String && this.headers
/* 112 */       .stream().anyMatch(field -> field.contains((String)value)));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> get(Object key) {
/* 118 */     if (containsKey(key)) {
/* 119 */       return this.headers.getValuesList((String)key);
/*     */     }
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> put(String key, List<String> value) {
/* 127 */     List<String> oldValues = get(key);
/* 128 */     this.headers.put(key, value);
/* 129 */     return oldValues;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public List<String> remove(Object key) {
/* 135 */     if (key instanceof String) {
/* 136 */       List<String> oldValues = get(key);
/* 137 */       this.headers.remove((String)key);
/* 138 */       return oldValues;
/*     */     } 
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends List<String>> map) {
/* 145 */     map.forEach(this::put);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 150 */     this.headers.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/* 155 */     return this.headers.getFieldNamesCollection();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<List<String>> values() {
/* 160 */     return (Collection<List<String>>)this.headers.getFieldNamesCollection().stream()
/* 161 */       .map(this.headers::getValuesList).collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, List<String>>> entrySet() {
/* 166 */     return new AbstractSet<Map.Entry<String, List<String>>>()
/*     */       {
/*     */         public Iterator<Map.Entry<String, List<String>>> iterator() {
/* 169 */           return new JettyHeadersAdapter.EntryIterator();
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 174 */           return JettyHeadersAdapter.this.headers.size();
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
/* 188 */     private Enumeration<String> names = JettyHeadersAdapter.this.headers.getFieldNames();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 192 */       return this.names.hasMoreElements();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<String, List<String>> next() {
/* 197 */       return new JettyHeadersAdapter.HeaderEntry(this.names.nextElement());
/*     */     }
/*     */     
/*     */     private EntryIterator() {}
/*     */   }
/*     */   
/*     */   private class HeaderEntry implements Map.Entry<String, List<String>> {
/*     */     private final String key;
/*     */     
/*     */     HeaderEntry(String key) {
/* 207 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getKey() {
/* 212 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> getValue() {
/* 217 */       return JettyHeadersAdapter.this.headers.getValuesList(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> setValue(List<String> value) {
/* 222 */       List<String> previousValues = JettyHeadersAdapter.this.headers.getValuesList(this.key);
/* 223 */       JettyHeadersAdapter.this.headers.put(this.key, value);
/* 224 */       return previousValues;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/JettyHeadersAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */