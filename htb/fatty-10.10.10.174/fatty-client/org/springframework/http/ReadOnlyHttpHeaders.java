/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
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
/*     */ 
/*     */ class ReadOnlyHttpHeaders
/*     */   extends HttpHeaders
/*     */ {
/*     */   private static final long serialVersionUID = -8578554704772377436L;
/*     */   @Nullable
/*     */   private MediaType cachedContentType;
/*     */   @Nullable
/*     */   private List<MediaType> cachedAccept;
/*     */   
/*     */   ReadOnlyHttpHeaders(HttpHeaders headers) {
/*  48 */     super(headers.headers);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MediaType getContentType() {
/*  54 */     if (this.cachedContentType != null) {
/*  55 */       return this.cachedContentType;
/*     */     }
/*     */     
/*  58 */     MediaType contentType = super.getContentType();
/*  59 */     this.cachedContentType = contentType;
/*  60 */     return contentType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MediaType> getAccept() {
/*  66 */     if (this.cachedAccept != null) {
/*  67 */       return this.cachedAccept;
/*     */     }
/*     */     
/*  70 */     List<MediaType> accept = super.getAccept();
/*  71 */     this.cachedAccept = accept;
/*  72 */     return accept;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> get(Object key) {
/*  78 */     List<String> values = (List<String>)this.headers.get(key);
/*  79 */     return (values != null) ? Collections.<String>unmodifiableList(values) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(String headerName, @Nullable String headerValue) {
/*  84 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(String key, List<? extends String> values) {
/*  89 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAll(MultiValueMap<String, String> values) {
/*  94 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(String headerName, @Nullable String headerValue) {
/*  99 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAll(Map<String, String> values) {
/* 104 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> toSingleValueMap() {
/* 109 */     return Collections.unmodifiableMap(this.headers.toSingleValueMap());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> keySet() {
/* 114 */     return Collections.unmodifiableSet(this.headers.keySet());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> put(String key, List<String> value) {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> remove(Object key) {
/* 124 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends String, ? extends List<String>> map) {
/* 129 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 134 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<List<String>> values() {
/* 139 */     return Collections.unmodifiableCollection(this.headers.values());
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<String, List<String>>> entrySet() {
/* 144 */     return Collections.unmodifiableSet((Set<? extends Map.Entry<String, List<String>>>)this.headers.entrySet().stream()
/* 145 */         .map(java.util.AbstractMap.SimpleImmutableEntry::new)
/* 146 */         .collect(Collectors.toSet()));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/ReadOnlyHttpHeaders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */