/*    */ package org.springframework.web.servlet.config;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.xml.BeanDefinitionParser;
/*    */ import org.springframework.beans.factory.xml.ParserContext;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.StringUtils;
/*    */ import org.springframework.util.xml.DomUtils;
/*    */ import org.springframework.web.cors.CorsConfiguration;
/*    */ import org.w3c.dom.Element;
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
/*    */ 
/*    */ 
/*    */ public class CorsBeanDefinitionParser
/*    */   implements BeanDefinitionParser
/*    */ {
/*    */   @Nullable
/*    */   public BeanDefinition parse(Element element, ParserContext parserContext) {
/* 49 */     Map<String, CorsConfiguration> corsConfigurations = new LinkedHashMap<>();
/* 50 */     List<Element> mappings = DomUtils.getChildElementsByTagName(element, "mapping");
/*    */     
/* 52 */     if (mappings.isEmpty()) {
/* 53 */       CorsConfiguration config = (new CorsConfiguration()).applyPermitDefaultValues();
/* 54 */       corsConfigurations.put("/**", config);
/*    */     } else {
/*    */       
/* 57 */       for (Element mapping : mappings) {
/* 58 */         CorsConfiguration config = new CorsConfiguration();
/* 59 */         if (mapping.hasAttribute("allowed-origins")) {
/* 60 */           String[] allowedOrigins = StringUtils.tokenizeToStringArray(mapping.getAttribute("allowed-origins"), ",");
/* 61 */           config.setAllowedOrigins(Arrays.asList(allowedOrigins));
/*    */         } 
/* 63 */         if (mapping.hasAttribute("allowed-methods")) {
/* 64 */           String[] allowedMethods = StringUtils.tokenizeToStringArray(mapping.getAttribute("allowed-methods"), ",");
/* 65 */           config.setAllowedMethods(Arrays.asList(allowedMethods));
/*    */         } 
/* 67 */         if (mapping.hasAttribute("allowed-headers")) {
/* 68 */           String[] allowedHeaders = StringUtils.tokenizeToStringArray(mapping.getAttribute("allowed-headers"), ",");
/* 69 */           config.setAllowedHeaders(Arrays.asList(allowedHeaders));
/*    */         } 
/* 71 */         if (mapping.hasAttribute("exposed-headers")) {
/* 72 */           String[] exposedHeaders = StringUtils.tokenizeToStringArray(mapping.getAttribute("exposed-headers"), ",");
/* 73 */           config.setExposedHeaders(Arrays.asList(exposedHeaders));
/*    */         } 
/* 75 */         if (mapping.hasAttribute("allow-credentials")) {
/* 76 */           config.setAllowCredentials(Boolean.valueOf(Boolean.parseBoolean(mapping.getAttribute("allow-credentials"))));
/*    */         }
/* 78 */         if (mapping.hasAttribute("max-age")) {
/* 79 */           config.setMaxAge(Long.valueOf(Long.parseLong(mapping.getAttribute("max-age"))));
/*    */         }
/* 81 */         corsConfigurations.put(mapping.getAttribute("path"), config.applyPermitDefaultValues());
/*    */       } 
/*    */     } 
/*    */     
/* 85 */     MvcNamespaceUtils.registerCorsConfigurations(corsConfigurations, parserContext, parserContext
/* 86 */         .extractSource(element));
/* 87 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/config/CorsBeanDefinitionParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */