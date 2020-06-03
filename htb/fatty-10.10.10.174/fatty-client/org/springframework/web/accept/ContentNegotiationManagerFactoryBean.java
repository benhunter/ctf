/*     */ package org.springframework.web.accept;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.context.ServletContextAware;
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
/*     */ public class ContentNegotiationManagerFactoryBean
/*     */   implements FactoryBean<ContentNegotiationManager>, ServletContextAware, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private List<ContentNegotiationStrategy> strategies;
/*     */   private boolean favorPathExtension = true;
/*     */   private boolean favorParameter = false;
/*     */   private boolean ignoreAcceptHeader = false;
/* 104 */   private Map<String, MediaType> mediaTypes = new HashMap<>();
/*     */   
/*     */   private boolean ignoreUnknownPathExtensions = true;
/*     */   
/*     */   @Nullable
/*     */   private Boolean useRegisteredExtensionsOnly;
/*     */   
/* 111 */   private String parameterName = "format";
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ContentNegotiationStrategy defaultNegotiationStrategy;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ContentNegotiationManager contentNegotiationManager;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ServletContext servletContext;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStrategies(@Nullable List<ContentNegotiationStrategy> strategies) {
/* 132 */     this.strategies = (strategies != null) ? new ArrayList<>(strategies) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFavorPathExtension(boolean favorPathExtension) {
/* 143 */     this.favorPathExtension = favorPathExtension;
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
/*     */   public void setMediaTypes(Properties mediaTypes) {
/* 161 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 162 */       mediaTypes.forEach((key, value) -> {
/*     */             String extension = ((String)key).toLowerCase(Locale.ENGLISH);
/*     */             MediaType mediaType = MediaType.valueOf((String)value);
/*     */             this.mediaTypes.put(extension, mediaType);
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMediaType(String fileExtension, MediaType mediaType) {
/* 176 */     this.mediaTypes.put(fileExtension, mediaType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addMediaTypes(@Nullable Map<String, MediaType> mediaTypes) {
/* 185 */     if (mediaTypes != null) {
/* 186 */       this.mediaTypes.putAll(mediaTypes);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreUnknownPathExtensions(boolean ignore) {
/* 197 */     this.ignoreUnknownPathExtensions = ignore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setUseJaf(boolean useJaf) {
/* 208 */     setUseRegisteredExtensionsOnly(!useJaf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseRegisteredExtensionsOnly(boolean useRegisteredExtensionsOnly) {
/* 219 */     this.useRegisteredExtensionsOnly = Boolean.valueOf(useRegisteredExtensionsOnly);
/*     */   }
/*     */   
/*     */   private boolean useRegisteredExtensionsOnly() {
/* 223 */     return (this.useRegisteredExtensionsOnly != null && this.useRegisteredExtensionsOnly.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFavorParameter(boolean favorParameter) {
/* 234 */     this.favorParameter = favorParameter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParameterName(String parameterName) {
/* 242 */     Assert.notNull(parameterName, "parameterName is required");
/* 243 */     this.parameterName = parameterName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIgnoreAcceptHeader(boolean ignoreAcceptHeader) {
/* 251 */     this.ignoreAcceptHeader = ignoreAcceptHeader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultContentType(MediaType contentType) {
/* 260 */     this.defaultNegotiationStrategy = new FixedContentNegotiationStrategy(contentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultContentTypes(List<MediaType> contentTypes) {
/* 270 */     this.defaultNegotiationStrategy = new FixedContentNegotiationStrategy(contentTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultContentTypeStrategy(ContentNegotiationStrategy strategy) {
/* 281 */     this.defaultNegotiationStrategy = strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletContext(ServletContext servletContext) {
/* 289 */     this.servletContext = servletContext;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 295 */     build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ContentNegotiationManager build() {
/* 303 */     List<ContentNegotiationStrategy> strategies = new ArrayList<>();
/*     */     
/* 305 */     if (this.strategies != null) {
/* 306 */       strategies.addAll(this.strategies);
/*     */     } else {
/*     */       
/* 309 */       if (this.favorPathExtension) {
/*     */         PathExtensionContentNegotiationStrategy strategy;
/* 311 */         if (this.servletContext != null && !useRegisteredExtensionsOnly()) {
/* 312 */           strategy = new ServletPathExtensionContentNegotiationStrategy(this.servletContext, this.mediaTypes);
/*     */         } else {
/*     */           
/* 315 */           strategy = new PathExtensionContentNegotiationStrategy(this.mediaTypes);
/*     */         } 
/* 317 */         strategy.setIgnoreUnknownExtensions(this.ignoreUnknownPathExtensions);
/* 318 */         if (this.useRegisteredExtensionsOnly != null) {
/* 319 */           strategy.setUseRegisteredExtensionsOnly(this.useRegisteredExtensionsOnly.booleanValue());
/*     */         }
/* 321 */         strategies.add(strategy);
/*     */       } 
/*     */       
/* 324 */       if (this.favorParameter) {
/* 325 */         ParameterContentNegotiationStrategy strategy = new ParameterContentNegotiationStrategy(this.mediaTypes);
/* 326 */         strategy.setParameterName(this.parameterName);
/* 327 */         if (this.useRegisteredExtensionsOnly != null) {
/* 328 */           strategy.setUseRegisteredExtensionsOnly(this.useRegisteredExtensionsOnly.booleanValue());
/*     */         } else {
/*     */           
/* 331 */           strategy.setUseRegisteredExtensionsOnly(true);
/*     */         } 
/* 333 */         strategies.add(strategy);
/*     */       } 
/*     */       
/* 336 */       if (!this.ignoreAcceptHeader) {
/* 337 */         strategies.add(new HeaderContentNegotiationStrategy());
/*     */       }
/*     */       
/* 340 */       if (this.defaultNegotiationStrategy != null) {
/* 341 */         strategies.add(this.defaultNegotiationStrategy);
/*     */       }
/*     */     } 
/*     */     
/* 345 */     this.contentNegotiationManager = new ContentNegotiationManager(strategies);
/* 346 */     return this.contentNegotiationManager;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ContentNegotiationManager getObject() {
/* 353 */     return this.contentNegotiationManager;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 358 */     return ContentNegotiationManager.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 363 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/accept/ContentNegotiationManagerFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */