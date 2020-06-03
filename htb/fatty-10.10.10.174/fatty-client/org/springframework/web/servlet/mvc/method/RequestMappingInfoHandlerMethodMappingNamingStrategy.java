/*    */ package org.springframework.web.servlet.mvc.method;
/*    */ 
/*    */ import org.springframework.web.method.HandlerMethod;
/*    */ import org.springframework.web.servlet.handler.HandlerMethodMappingNamingStrategy;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestMappingInfoHandlerMethodMappingNamingStrategy
/*    */   implements HandlerMethodMappingNamingStrategy<RequestMappingInfo>
/*    */ {
/*    */   public static final String SEPARATOR = "#";
/*    */   
/*    */   public String getName(HandlerMethod handlerMethod, RequestMappingInfo mapping) {
/* 44 */     if (mapping.getName() != null) {
/* 45 */       return mapping.getName();
/*    */     }
/* 47 */     StringBuilder sb = new StringBuilder();
/* 48 */     String simpleTypeName = handlerMethod.getBeanType().getSimpleName();
/* 49 */     for (int i = 0; i < simpleTypeName.length(); i++) {
/* 50 */       if (Character.isUpperCase(simpleTypeName.charAt(i))) {
/* 51 */         sb.append(simpleTypeName.charAt(i));
/*    */       }
/*    */     } 
/* 54 */     sb.append("#").append(handlerMethod.getMethod().getName());
/* 55 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/RequestMappingInfoHandlerMethodMappingNamingStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */