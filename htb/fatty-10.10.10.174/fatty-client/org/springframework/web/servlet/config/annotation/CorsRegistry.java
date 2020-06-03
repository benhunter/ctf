/*    */ package org.springframework.web.servlet.config.annotation;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.web.cors.CorsConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CorsRegistry
/*    */ {
/* 37 */   private final List<CorsRegistration> registrations = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CorsRegistration addMapping(String pathPattern) {
/* 58 */     CorsRegistration registration = new CorsRegistration(pathPattern);
/* 59 */     this.registrations.add(registration);
/* 60 */     return registration;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Map<String, CorsConfiguration> getCorsConfigurations() {
/* 68 */     Map<String, CorsConfiguration> configs = new LinkedHashMap<>(this.registrations.size());
/* 69 */     for (CorsRegistration registration : this.registrations) {
/* 70 */       configs.put(registration.getPathPattern(), registration.getCorsConfiguration());
/*    */     }
/* 72 */     return configs;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/annotation/CorsRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */