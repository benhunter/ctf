/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
/*     */ import org.springframework.web.accept.ContentNegotiationStrategy;
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
/*     */ public class ContentNegotiationConfigurer
/*     */ {
/*  95 */   private final ContentNegotiationManagerFactoryBean factory = new ContentNegotiationManagerFactoryBean();
/*     */   
/*  97 */   private final Map<String, MediaType> mediaTypes = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer(@Nullable ServletContext servletContext) {
/* 104 */     if (servletContext != null) {
/* 105 */       this.factory.setServletContext(servletContext);
/*     */     }
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
/*     */   public void strategies(@Nullable List<ContentNegotiationStrategy> strategies) {
/* 119 */     this.factory.setStrategies(strategies);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer favorPathExtension(boolean favorPathExtension) {
/* 130 */     this.factory.setFavorPathExtension(favorPathExtension);
/* 131 */     return this;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer mediaType(String extension, MediaType mediaType) {
/* 150 */     this.mediaTypes.put(extension, mediaType);
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer mediaTypes(@Nullable Map<String, MediaType> mediaTypes) {
/* 160 */     if (mediaTypes != null) {
/* 161 */       this.mediaTypes.putAll(mediaTypes);
/*     */     }
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer replaceMediaTypes(Map<String, MediaType> mediaTypes) {
/* 172 */     this.mediaTypes.clear();
/* 173 */     mediaTypes(mediaTypes);
/* 174 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer ignoreUnknownPathExtensions(boolean ignore) {
/* 184 */     this.factory.setIgnoreUnknownPathExtensions(ignore);
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ContentNegotiationConfigurer useJaf(boolean useJaf) {
/* 197 */     return useRegisteredExtensionsOnly(!useJaf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer useRegisteredExtensionsOnly(boolean useRegisteredExtensionsOnly) {
/* 208 */     this.factory.setUseRegisteredExtensionsOnly(useRegisteredExtensionsOnly);
/* 209 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer favorParameter(boolean favorParameter) {
/* 220 */     this.factory.setFavorParameter(favorParameter);
/* 221 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer parameterName(String parameterName) {
/* 229 */     this.factory.setParameterName(parameterName);
/* 230 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer ignoreAcceptHeader(boolean ignoreAcceptHeader) {
/* 238 */     this.factory.setIgnoreAcceptHeader(ignoreAcceptHeader);
/* 239 */     return this;
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
/*     */   public ContentNegotiationConfigurer defaultContentType(MediaType... defaultContentTypes) {
/* 251 */     this.factory.setDefaultContentTypes(Arrays.asList(defaultContentTypes));
/* 252 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationConfigurer defaultContentTypeStrategy(ContentNegotiationStrategy defaultStrategy) {
/* 263 */     this.factory.setDefaultContentTypeStrategy(defaultStrategy);
/* 264 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ContentNegotiationManager buildContentNegotiationManager() {
/* 274 */     this.factory.addMediaTypes(this.mediaTypes);
/* 275 */     return this.factory.build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/ContentNegotiationConfigurer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */