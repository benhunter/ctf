/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
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
/*     */ public class ContentNegotiationManager
/*     */   implements ContentNegotiationStrategy, MediaTypeFileExtensionResolver
/*     */ {
/*  48 */   private final List<ContentNegotiationStrategy> strategies = new ArrayList<>();
/*     */   
/*  50 */   private final Set<MediaTypeFileExtensionResolver> resolvers = new LinkedHashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationManager(ContentNegotiationStrategy... strategies) {
/*  60 */     this(Arrays.asList(strategies));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationManager(Collection<ContentNegotiationStrategy> strategies) {
/*  70 */     Assert.notEmpty(strategies, "At least one ContentNegotiationStrategy is expected");
/*  71 */     this.strategies.addAll(strategies);
/*  72 */     for (ContentNegotiationStrategy strategy : this.strategies) {
/*  73 */       if (strategy instanceof MediaTypeFileExtensionResolver) {
/*  74 */         this.resolvers.add((MediaTypeFileExtensionResolver)strategy);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationManager() {
/*  83 */     this(new ContentNegotiationStrategy[] { new HeaderContentNegotiationStrategy() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ContentNegotiationStrategy> getStrategies() {
/*  92 */     return this.strategies;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T extends ContentNegotiationStrategy> T getStrategy(Class<T> strategyType) {
/* 104 */     for (ContentNegotiationStrategy strategy : getStrategies()) {
/* 105 */       if (strategyType.isInstance(strategy)) {
/* 106 */         return (T)strategy;
/*     */       }
/*     */     } 
/* 109 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFileExtensionResolvers(MediaTypeFileExtensionResolver... resolvers) {
/* 118 */     Collections.addAll(this.resolvers, resolvers);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MediaType> resolveMediaTypes(NativeWebRequest request) throws HttpMediaTypeNotAcceptableException {
/* 123 */     for (ContentNegotiationStrategy strategy : this.strategies) {
/* 124 */       List<MediaType> mediaTypes = strategy.resolveMediaTypes(request);
/* 125 */       if (mediaTypes.equals(MEDIA_TYPE_ALL_LIST)) {
/*     */         continue;
/*     */       }
/* 128 */       return mediaTypes;
/*     */     } 
/* 130 */     return MEDIA_TYPE_ALL_LIST;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> resolveFileExtensions(MediaType mediaType) {
/* 135 */     Set<String> result = new LinkedHashSet<>();
/* 136 */     for (MediaTypeFileExtensionResolver resolver : this.resolvers) {
/* 137 */       result.addAll(resolver.resolveFileExtensions(mediaType));
/*     */     }
/* 139 */     return new ArrayList<>(result);
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
/*     */   
/*     */   public List<String> getAllFileExtensions() {
/* 155 */     Set<String> result = new LinkedHashSet<>();
/* 156 */     for (MediaTypeFileExtensionResolver resolver : this.resolvers) {
/* 157 */       result.addAll(resolver.getAllFileExtensions());
/*     */     }
/* 159 */     return new ArrayList<>(result);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/accept/ContentNegotiationManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */