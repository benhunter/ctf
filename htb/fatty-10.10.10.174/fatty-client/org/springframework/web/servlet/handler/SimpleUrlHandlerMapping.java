/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleUrlHandlerMapping
/*     */   extends AbstractUrlHandlerMapping
/*     */ {
/*  59 */   private final Map<String, Object> urlMap = new LinkedHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMappings(Properties mappings) {
/*  71 */     CollectionUtils.mergePropertiesIntoMap(mappings, this.urlMap);
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
/*     */   public void setUrlMap(Map<String, ?> urlMap) {
/*  83 */     this.urlMap.putAll(urlMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ?> getUrlMap() {
/*  94 */     return this.urlMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void initApplicationContext() throws BeansException {
/* 104 */     super.initApplicationContext();
/* 105 */     registerHandlers(this.urlMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void registerHandlers(Map<String, Object> urlMap) throws BeansException {
/* 115 */     if (urlMap.isEmpty()) {
/* 116 */       this.logger.trace("No patterns in " + formatMappingName());
/*     */     } else {
/*     */       
/* 119 */       urlMap.forEach((url, handler) -> {
/*     */             if (!url.startsWith("/")) {
/*     */               url = "/" + url;
/*     */             }
/*     */             
/*     */             if (handler instanceof String) {
/*     */               handler = ((String)handler).trim();
/*     */             }
/*     */             
/*     */             registerHandler(url, handler);
/*     */           });
/* 130 */       if (this.logger.isDebugEnabled()) {
/* 131 */         List<String> patterns = new ArrayList<>();
/* 132 */         if (getRootHandler() != null) {
/* 133 */           patterns.add("/");
/*     */         }
/* 135 */         if (getDefaultHandler() != null) {
/* 136 */           patterns.add("/**");
/*     */         }
/* 138 */         patterns.addAll(getHandlerMap().keySet());
/* 139 */         this.logger.debug("Patterns " + patterns + " in " + formatMappingName());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/handler/SimpleUrlHandlerMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */