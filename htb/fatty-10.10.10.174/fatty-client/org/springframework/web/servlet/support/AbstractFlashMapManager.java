/*     */ package org.springframework.web.servlet.support;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.FlashMap;
/*     */ import org.springframework.web.servlet.FlashMapManager;
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
/*     */ public abstract class AbstractFlashMapManager
/*     */   implements FlashMapManager
/*     */ {
/*  48 */   private static final Object DEFAULT_FLASH_MAPS_MUTEX = new Object();
/*     */ 
/*     */   
/*  51 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  53 */   private int flashMapTimeout = 180;
/*     */   
/*  55 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFlashMapTimeout(int flashMapTimeout) {
/*  64 */     this.flashMapTimeout = flashMapTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFlashMapTimeout() {
/*  71 */     return this.flashMapTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
/*  78 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  79 */     this.urlPathHelper = urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UrlPathHelper getUrlPathHelper() {
/*  86 */     return this.urlPathHelper;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final FlashMap retrieveAndUpdate(HttpServletRequest request, HttpServletResponse response) {
/*  93 */     List<FlashMap> allFlashMaps = retrieveFlashMaps(request);
/*  94 */     if (CollectionUtils.isEmpty(allFlashMaps)) {
/*  95 */       return null;
/*     */     }
/*     */     
/*  98 */     List<FlashMap> mapsToRemove = getExpiredFlashMaps(allFlashMaps);
/*  99 */     FlashMap match = getMatchingFlashMap(allFlashMaps, request);
/* 100 */     if (match != null) {
/* 101 */       mapsToRemove.add(match);
/*     */     }
/*     */     
/* 104 */     if (!mapsToRemove.isEmpty()) {
/* 105 */       Object mutex = getFlashMapsMutex(request);
/* 106 */       if (mutex != null) {
/* 107 */         synchronized (mutex) {
/* 108 */           allFlashMaps = retrieveFlashMaps(request);
/* 109 */           if (allFlashMaps != null) {
/* 110 */             allFlashMaps.removeAll(mapsToRemove);
/* 111 */             updateFlashMaps(allFlashMaps, request, response);
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 116 */         allFlashMaps.removeAll(mapsToRemove);
/* 117 */         updateFlashMaps(allFlashMaps, request, response);
/*     */       } 
/*     */     } 
/*     */     
/* 121 */     return match;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<FlashMap> getExpiredFlashMaps(List<FlashMap> allMaps) {
/* 128 */     List<FlashMap> result = new LinkedList<>();
/* 129 */     for (FlashMap map : allMaps) {
/* 130 */       if (map.isExpired()) {
/* 131 */         result.add(map);
/*     */       }
/*     */     } 
/* 134 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private FlashMap getMatchingFlashMap(List<FlashMap> allMaps, HttpServletRequest request) {
/* 143 */     List<FlashMap> result = new LinkedList<>();
/* 144 */     for (FlashMap flashMap : allMaps) {
/* 145 */       if (isFlashMapForRequest(flashMap, request)) {
/* 146 */         result.add(flashMap);
/*     */       }
/*     */     } 
/* 149 */     if (!result.isEmpty()) {
/* 150 */       Collections.sort(result);
/* 151 */       if (this.logger.isTraceEnabled()) {
/* 152 */         this.logger.trace("Found " + result.get(0));
/*     */       }
/* 154 */       return result.get(0);
/*     */     } 
/* 156 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isFlashMapForRequest(FlashMap flashMap, HttpServletRequest request) {
/* 164 */     String expectedPath = flashMap.getTargetRequestPath();
/* 165 */     if (expectedPath != null) {
/* 166 */       String requestUri = getUrlPathHelper().getOriginatingRequestUri(request);
/* 167 */       if (!requestUri.equals(expectedPath) && !requestUri.equals(expectedPath + "/")) {
/* 168 */         return false;
/*     */       }
/*     */     } 
/* 171 */     MultiValueMap<String, String> actualParams = getOriginatingRequestParams(request);
/* 172 */     MultiValueMap<String, String> expectedParams = flashMap.getTargetRequestParams();
/* 173 */     for (String expectedName : expectedParams.keySet()) {
/* 174 */       List<String> actualValues = (List<String>)actualParams.get(expectedName);
/* 175 */       if (actualValues == null) {
/* 176 */         return false;
/*     */       }
/* 178 */       for (String expectedValue : expectedParams.get(expectedName)) {
/* 179 */         if (!actualValues.contains(expectedValue)) {
/* 180 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 184 */     return true;
/*     */   }
/*     */   
/*     */   private MultiValueMap<String, String> getOriginatingRequestParams(HttpServletRequest request) {
/* 188 */     String query = getUrlPathHelper().getOriginatingQueryString(request);
/* 189 */     return ServletUriComponentsBuilder.fromPath("/").query(query).build().getQueryParams();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void saveOutputFlashMap(FlashMap flashMap, HttpServletRequest request, HttpServletResponse response) {
/* 194 */     if (CollectionUtils.isEmpty((Map)flashMap)) {
/*     */       return;
/*     */     }
/*     */     
/* 198 */     String path = decodeAndNormalizePath(flashMap.getTargetRequestPath(), request);
/* 199 */     flashMap.setTargetRequestPath(path);
/*     */     
/* 201 */     flashMap.startExpirationPeriod(getFlashMapTimeout());
/*     */     
/* 203 */     Object mutex = getFlashMapsMutex(request);
/* 204 */     if (mutex != null) {
/* 205 */       synchronized (mutex) {
/* 206 */         List<FlashMap> allFlashMaps = retrieveFlashMaps(request);
/* 207 */         allFlashMaps = (allFlashMaps != null) ? allFlashMaps : new CopyOnWriteArrayList<>();
/* 208 */         allFlashMaps.add(flashMap);
/* 209 */         updateFlashMaps(allFlashMaps, request, response);
/*     */       } 
/*     */     } else {
/*     */       
/* 213 */       List<FlashMap> allFlashMaps = retrieveFlashMaps(request);
/* 214 */       allFlashMaps = (allFlashMaps != null) ? allFlashMaps : new LinkedList<>();
/* 215 */       allFlashMaps.add(flashMap);
/* 216 */       updateFlashMaps(allFlashMaps, request, response);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private String decodeAndNormalizePath(@Nullable String path, HttpServletRequest request) {
/* 222 */     if (path != null && !path.isEmpty()) {
/* 223 */       path = getUrlPathHelper().decodeRequestString(request, path);
/* 224 */       if (path.charAt(0) != '/') {
/* 225 */         String requestUri = getUrlPathHelper().getRequestUri(request);
/* 226 */         path = requestUri.substring(0, requestUri.lastIndexOf('/') + 1) + path;
/* 227 */         path = StringUtils.cleanPath(path);
/*     */       } 
/*     */     } 
/* 230 */     return path;
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
/*     */   protected abstract List<FlashMap> retrieveFlashMaps(HttpServletRequest paramHttpServletRequest);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void updateFlashMaps(List<FlashMap> paramList, HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getFlashMapsMutex(HttpServletRequest request) {
/* 262 */     return DEFAULT_FLASH_MAPS_MUTEX;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/support/AbstractFlashMapManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */