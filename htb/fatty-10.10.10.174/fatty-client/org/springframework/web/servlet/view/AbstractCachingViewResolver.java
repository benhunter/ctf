/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.ViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCachingViewResolver
/*     */   extends WebApplicationObjectSupport
/*     */   implements ViewResolver
/*     */ {
/*     */   public static final int DEFAULT_CACHE_LIMIT = 1024;
/*     */   
/*  50 */   private static final View UNRESOLVED_VIEW = new View()
/*     */     {
/*     */       @Nullable
/*     */       public String getContentType() {
/*  54 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void render(@Nullable Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {}
/*     */     };
/*     */ 
/*     */   
/*  63 */   private volatile int cacheLimit = 1024;
/*     */ 
/*     */   
/*     */   private boolean cacheUnresolved = true;
/*     */ 
/*     */   
/*  69 */   private final Map<Object, View> viewAccessCache = new ConcurrentHashMap<>(1024);
/*     */ 
/*     */   
/*  72 */   private final Map<Object, View> viewCreationCache = new LinkedHashMap<Object, View>(1024, 0.75F, true)
/*     */     {
/*     */       
/*     */       protected boolean removeEldestEntry(Map.Entry<Object, View> eldest)
/*     */       {
/*  77 */         if (size() > AbstractCachingViewResolver.this.getCacheLimit()) {
/*  78 */           AbstractCachingViewResolver.this.viewAccessCache.remove(eldest.getKey());
/*  79 */           return true;
/*     */         } 
/*     */         
/*  82 */         return false;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCacheLimit(int cacheLimit) {
/*  93 */     this.cacheLimit = cacheLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCacheLimit() {
/* 100 */     return this.cacheLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCache(boolean cache) {
/* 111 */     this.cacheLimit = cache ? 1024 : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCache() {
/* 118 */     return (this.cacheLimit > 0);
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
/*     */   public void setCacheUnresolved(boolean cacheUnresolved) {
/* 134 */     this.cacheUnresolved = cacheUnresolved;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCacheUnresolved() {
/* 141 */     return this.cacheUnresolved;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public View resolveViewName(String viewName, Locale locale) throws Exception {
/* 148 */     if (!isCache()) {
/* 149 */       return createView(viewName, locale);
/*     */     }
/*     */     
/* 152 */     Object cacheKey = getCacheKey(viewName, locale);
/* 153 */     View view = this.viewAccessCache.get(cacheKey);
/* 154 */     if (view == null) {
/* 155 */       synchronized (this.viewCreationCache) {
/* 156 */         view = this.viewCreationCache.get(cacheKey);
/* 157 */         if (view == null)
/*     */         {
/* 159 */           view = createView(viewName, locale);
/* 160 */           if (view == null && this.cacheUnresolved) {
/* 161 */             view = UNRESOLVED_VIEW;
/*     */           }
/* 163 */           if (view != null) {
/* 164 */             this.viewAccessCache.put(cacheKey, view);
/* 165 */             this.viewCreationCache.put(cacheKey, view);
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       } 
/* 171 */     } else if (this.logger.isTraceEnabled()) {
/* 172 */       this.logger.trace(formatKey(cacheKey) + "served from cache");
/*     */     } 
/*     */     
/* 175 */     return (view != UNRESOLVED_VIEW) ? view : null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String formatKey(Object cacheKey) {
/* 180 */     return "View with key [" + cacheKey + "] ";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getCacheKey(String viewName, Locale locale) {
/* 191 */     return viewName + '_' + locale;
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
/*     */   public void removeFromCache(String viewName, Locale locale) {
/* 204 */     if (!isCache()) {
/* 205 */       this.logger.warn("Caching is OFF (removal not necessary)");
/*     */     } else {
/*     */       
/* 208 */       Object cachedView, cacheKey = getCacheKey(viewName, locale);
/*     */       
/* 210 */       synchronized (this.viewCreationCache) {
/* 211 */         this.viewAccessCache.remove(cacheKey);
/* 212 */         cachedView = this.viewCreationCache.remove(cacheKey);
/*     */       } 
/* 214 */       if (this.logger.isDebugEnabled())
/*     */       {
/* 216 */         this.logger.debug(formatKey(cacheKey) + ((cachedView != null) ? "cleared from cache" : "not found in the cache"));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearCache() {
/* 227 */     this.logger.debug("Clearing all views from the cache");
/* 228 */     synchronized (this.viewCreationCache) {
/* 229 */       this.viewAccessCache.clear();
/* 230 */       this.viewCreationCache.clear();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected View createView(String viewName, Locale locale) throws Exception {
/* 250 */     return loadView(viewName, locale);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected abstract View loadView(String paramString, Locale paramLocale) throws Exception;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/AbstractCachingViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */