/*     */ package org.springframework.web.servlet.resource;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.event.ContextRefreshedEvent;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceUrlProvider
/*     */   implements ApplicationListener<ContextRefreshedEvent>
/*     */ {
/*  53 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  55 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*  57 */   private PathMatcher pathMatcher = (PathMatcher)new AntPathMatcher();
/*     */   
/*  59 */   private final Map<String, ResourceHttpRequestHandler> handlerMap = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean autodetect = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/*  70 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlPathHelper getUrlPathHelper() {
/*  78 */     return this.urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathMatcher(PathMatcher pathMatcher) {
/*  86 */     this.pathMatcher = pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PathMatcher getPathMatcher() {
/*  93 */     return this.pathMatcher;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHandlerMap(@Nullable Map<String, ResourceHttpRequestHandler> handlerMap) {
/* 103 */     if (handlerMap != null) {
/* 104 */       this.handlerMap.clear();
/* 105 */       this.handlerMap.putAll(handlerMap);
/* 106 */       this.autodetect = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ResourceHttpRequestHandler> getHandlerMap() {
/* 115 */     return this.handlerMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutodetect() {
/* 123 */     return this.autodetect;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onApplicationEvent(ContextRefreshedEvent event) {
/* 128 */     if (isAutodetect()) {
/* 129 */       this.handlerMap.clear();
/* 130 */       detectResourceHandlers(event.getApplicationContext());
/* 131 */       if (!this.handlerMap.isEmpty()) {
/* 132 */         this.autodetect = false;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void detectResourceHandlers(ApplicationContext appContext) {
/* 139 */     Map<String, SimpleUrlHandlerMapping> beans = appContext.getBeansOfType(SimpleUrlHandlerMapping.class);
/* 140 */     List<SimpleUrlHandlerMapping> mappings = new ArrayList<>(beans.values());
/* 141 */     AnnotationAwareOrderComparator.sort(mappings);
/*     */     
/* 143 */     for (SimpleUrlHandlerMapping mapping : mappings) {
/* 144 */       for (String pattern : mapping.getHandlerMap().keySet()) {
/* 145 */         Object handler = mapping.getHandlerMap().get(pattern);
/* 146 */         if (handler instanceof ResourceHttpRequestHandler) {
/* 147 */           ResourceHttpRequestHandler resourceHandler = (ResourceHttpRequestHandler)handler;
/* 148 */           this.handlerMap.put(pattern, resourceHandler);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 153 */     if (this.handlerMap.isEmpty()) {
/* 154 */       this.logger.trace("No resource handling mappings found");
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
/*     */   @Nullable
/*     */   public final String getForRequestUrl(HttpServletRequest request, String requestUrl) {
/* 168 */     int prefixIndex = getLookupPathIndex(request);
/* 169 */     int suffixIndex = getEndPathIndex(requestUrl);
/* 170 */     if (prefixIndex >= suffixIndex) {
/* 171 */       return null;
/*     */     }
/* 173 */     String prefix = requestUrl.substring(0, prefixIndex);
/* 174 */     String suffix = requestUrl.substring(suffixIndex);
/* 175 */     String lookupPath = requestUrl.substring(prefixIndex, suffixIndex);
/* 176 */     String resolvedLookupPath = getForLookupPath(lookupPath);
/* 177 */     return (resolvedLookupPath != null) ? (prefix + resolvedLookupPath + suffix) : null;
/*     */   }
/*     */   
/*     */   private int getLookupPathIndex(HttpServletRequest request) {
/* 181 */     UrlPathHelper pathHelper = getUrlPathHelper();
/* 182 */     String requestUri = pathHelper.getRequestUri(request);
/* 183 */     String lookupPath = pathHelper.getLookupPathForRequest(request);
/* 184 */     return requestUri.indexOf(lookupPath);
/*     */   }
/*     */   
/*     */   private int getEndPathIndex(String lookupPath) {
/* 188 */     int suffixIndex = lookupPath.length();
/* 189 */     int queryIndex = lookupPath.indexOf('?');
/* 190 */     if (queryIndex > 0) {
/* 191 */       suffixIndex = queryIndex;
/*     */     }
/* 193 */     int hashIndex = lookupPath.indexOf('#');
/* 194 */     if (hashIndex > 0) {
/* 195 */       suffixIndex = Math.min(suffixIndex, hashIndex);
/*     */     }
/* 197 */     return suffixIndex;
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
/*     */   public final String getForLookupPath(String lookupPath) {
/*     */     String previous;
/*     */     do {
/* 218 */       previous = lookupPath;
/* 219 */       lookupPath = StringUtils.replace(lookupPath, "//", "/");
/* 220 */     } while (!lookupPath.equals(previous));
/*     */     
/* 222 */     List<String> matchingPatterns = new ArrayList<>();
/* 223 */     for (String pattern : this.handlerMap.keySet()) {
/* 224 */       if (getPathMatcher().match(pattern, lookupPath)) {
/* 225 */         matchingPatterns.add(pattern);
/*     */       }
/*     */     } 
/*     */     
/* 229 */     if (!matchingPatterns.isEmpty()) {
/* 230 */       Comparator<String> patternComparator = getPathMatcher().getPatternComparator(lookupPath);
/* 231 */       matchingPatterns.sort(patternComparator);
/* 232 */       for (String pattern : matchingPatterns) {
/* 233 */         String pathWithinMapping = getPathMatcher().extractPathWithinPattern(pattern, lookupPath);
/* 234 */         String pathMapping = lookupPath.substring(0, lookupPath.indexOf(pathWithinMapping));
/* 235 */         ResourceHttpRequestHandler handler = this.handlerMap.get(pattern);
/* 236 */         ResourceResolverChain chain = new DefaultResourceResolverChain(handler.getResourceResolvers());
/* 237 */         String resolved = chain.resolveUrlPath(pathWithinMapping, handler.getLocations());
/* 238 */         if (resolved == null) {
/*     */           continue;
/*     */         }
/* 241 */         return pathMapping + resolved;
/*     */       } 
/*     */     } 
/*     */     
/* 245 */     if (this.logger.isTraceEnabled()) {
/* 246 */       this.logger.trace("No match for \"" + lookupPath + "\"");
/*     */     }
/*     */     
/* 249 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/resource/ResourceUrlProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */