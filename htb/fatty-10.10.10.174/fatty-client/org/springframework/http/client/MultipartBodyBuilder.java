/*     */ package org.springframework.http.client;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.ResolvableTypeProvider;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.NonNull;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.LinkedMultiValueMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MultipartBodyBuilder
/*     */ {
/*  79 */   private final LinkedMultiValueMap<String, DefaultPartBuilder> parts = new LinkedMultiValueMap();
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
/*     */   public PartBuilder part(String name, Object part) {
/* 103 */     return part(name, part, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PartBuilder part(String name, Object part, @Nullable MediaType contentType) {
/*     */     Object partBody;
/* 114 */     Assert.hasLength(name, "'name' must not be empty");
/* 115 */     Assert.notNull(part, "'part' must not be null");
/*     */     
/* 117 */     if (part instanceof PublisherEntity) {
/* 118 */       PublisherPartBuilder<?, ?> publisherPartBuilder = new PublisherPartBuilder<>((PublisherEntity<?, ?>)part);
/* 119 */       if (contentType != null) {
/* 120 */         publisherPartBuilder.header("Content-Type", new String[] { contentType.toString() });
/*     */       }
/* 122 */       this.parts.add(name, publisherPartBuilder);
/* 123 */       return publisherPartBuilder;
/*     */     } 
/*     */ 
/*     */     
/* 127 */     HttpHeaders partHeaders = null;
/* 128 */     if (part instanceof HttpEntity) {
/* 129 */       partBody = ((HttpEntity)part).getBody();
/* 130 */       partHeaders = new HttpHeaders();
/* 131 */       partHeaders.putAll((Map)((HttpEntity)part).getHeaders());
/*     */     } else {
/*     */       
/* 134 */       partBody = part;
/*     */     } 
/*     */     
/* 137 */     if (partBody instanceof Publisher) {
/* 138 */       throw new IllegalArgumentException("Use asyncPart(String, Publisher, Class) or asyncPart(String, Publisher, ParameterizedTypeReference) or or MultipartBodyBuilder.PublisherEntity");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     DefaultPartBuilder builder = new DefaultPartBuilder(partHeaders, partBody);
/* 145 */     if (contentType != null) {
/* 146 */       builder.header("Content-Type", new String[] { contentType.toString() });
/*     */     }
/* 148 */     this.parts.add(name, builder);
/* 149 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T, P extends Publisher<T>> PartBuilder asyncPart(String name, P publisher, Class<T> elementClass) {
/* 160 */     Assert.hasLength(name, "'name' must not be empty");
/* 161 */     Assert.notNull(publisher, "'publisher' must not be null");
/* 162 */     Assert.notNull(elementClass, "'elementClass' must not be null");
/*     */     
/* 164 */     PublisherPartBuilder<T, P> builder = (PublisherPartBuilder)new PublisherPartBuilder<>(null, (Publisher<T>)publisher, elementClass);
/* 165 */     this.parts.add(name, builder);
/* 166 */     return builder;
/*     */   }
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
/*     */   public <T, P extends Publisher<T>> PartBuilder asyncPart(String name, P publisher, ParameterizedTypeReference<T> typeReference) {
/* 181 */     Assert.hasLength(name, "'name' must not be empty");
/* 182 */     Assert.notNull(publisher, "'publisher' must not be null");
/* 183 */     Assert.notNull(typeReference, "'typeReference' must not be null");
/*     */     
/* 185 */     PublisherPartBuilder<T, P> builder = (PublisherPartBuilder)new PublisherPartBuilder<>(null, (Publisher<T>)publisher, typeReference);
/* 186 */     this.parts.add(name, builder);
/* 187 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, HttpEntity<?>> build() {
/* 194 */     LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap(this.parts.size());
/* 195 */     for (Map.Entry<String, List<DefaultPartBuilder>> entry : (Iterable<Map.Entry<String, List<DefaultPartBuilder>>>)this.parts.entrySet()) {
/* 196 */       for (DefaultPartBuilder builder : entry.getValue()) {
/* 197 */         HttpEntity<?> entity = builder.build();
/* 198 */         linkedMultiValueMap.add(entry.getKey(), entity);
/*     */       } 
/*     */     } 
/* 201 */     return (MultiValueMap<String, HttpEntity<?>>)linkedMultiValueMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface PartBuilder
/*     */   {
/*     */     PartBuilder header(String param1String, String... param1VarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     PartBuilder headers(Consumer<HttpHeaders> param1Consumer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DefaultPartBuilder
/*     */     implements PartBuilder
/*     */   {
/*     */     @Nullable
/*     */     protected HttpHeaders headers;
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     protected final Object body;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DefaultPartBuilder(@Nullable HttpHeaders headers, @Nullable Object body) {
/* 237 */       this.headers = headers;
/* 238 */       this.body = body;
/*     */     }
/*     */ 
/*     */     
/*     */     public MultipartBodyBuilder.PartBuilder header(String headerName, String... headerValues) {
/* 243 */       initHeadersIfNecessary().addAll(headerName, Arrays.asList(headerValues));
/* 244 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public MultipartBodyBuilder.PartBuilder headers(Consumer<HttpHeaders> headersConsumer) {
/* 249 */       headersConsumer.accept(initHeadersIfNecessary());
/* 250 */       return this;
/*     */     }
/*     */     
/*     */     private HttpHeaders initHeadersIfNecessary() {
/* 254 */       if (this.headers == null) {
/* 255 */         this.headers = new HttpHeaders();
/*     */       }
/* 257 */       return this.headers;
/*     */     }
/*     */     
/*     */     public HttpEntity<?> build() {
/* 261 */       return new HttpEntity(this.body, (MultiValueMap)this.headers);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PublisherPartBuilder<S, P extends Publisher<S>>
/*     */     extends DefaultPartBuilder
/*     */   {
/*     */     private final ResolvableType resolvableType;
/*     */     
/*     */     public PublisherPartBuilder(@Nullable HttpHeaders headers, P body, Class<S> elementClass) {
/* 271 */       super(headers, body);
/* 272 */       this.resolvableType = ResolvableType.forClass(elementClass);
/*     */     }
/*     */     
/*     */     public PublisherPartBuilder(@Nullable HttpHeaders headers, P body, ParameterizedTypeReference<S> typeRef) {
/* 276 */       super(headers, body);
/* 277 */       this.resolvableType = ResolvableType.forType(typeRef);
/*     */     }
/*     */     
/*     */     public PublisherPartBuilder(MultipartBodyBuilder.PublisherEntity<S, P> other) {
/* 281 */       super(other.getHeaders(), other.getBody());
/* 282 */       this.resolvableType = other.getResolvableType();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HttpEntity<?> build() {
/* 288 */       Publisher publisher = (Publisher)this.body;
/* 289 */       Assert.state((publisher != null), "Publisher must not be null");
/* 290 */       return new MultipartBodyBuilder.PublisherEntity<>((MultiValueMap<String, String>)this.headers, publisher, this.resolvableType);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class PublisherEntity<T, P extends Publisher<T>>
/*     */     extends HttpEntity<P>
/*     */     implements ResolvableTypeProvider
/*     */   {
/*     */     private final ResolvableType resolvableType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     PublisherEntity(@Nullable MultiValueMap<String, String> headers, P publisher, ResolvableType resolvableType) {
/* 310 */       super(publisher, headers);
/* 311 */       Assert.notNull(publisher, "'publisher' must not be null");
/* 312 */       Assert.notNull(resolvableType, "'resolvableType' must not be null");
/* 313 */       this.resolvableType = resolvableType;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @NonNull
/*     */     public ResolvableType getResolvableType() {
/* 322 */       return this.resolvableType;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/client/MultipartBodyBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */