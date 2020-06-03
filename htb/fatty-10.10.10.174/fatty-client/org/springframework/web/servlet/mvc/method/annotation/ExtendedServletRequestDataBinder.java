/*    */ package org.springframework.web.servlet.mvc.method.annotation;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.servlet.ServletRequest;
/*    */ import org.springframework.beans.MutablePropertyValues;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.web.bind.ServletRequestDataBinder;
/*    */ import org.springframework.web.servlet.HandlerMapping;
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
/*    */ public class ExtendedServletRequestDataBinder
/*    */   extends ServletRequestDataBinder
/*    */ {
/*    */   public ExtendedServletRequestDataBinder(@Nullable Object target) {
/* 43 */     super(target);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ExtendedServletRequestDataBinder(@Nullable Object target, String objectName) {
/* 54 */     super(target, objectName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void addBindValues(MutablePropertyValues mpvs, ServletRequest request) {
/* 64 */     String attr = HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;
/* 65 */     Map<String, String> uriVars = (Map<String, String>)request.getAttribute(attr);
/* 66 */     if (uriVars != null)
/* 67 */       uriVars.forEach((name, value) -> {
/*    */             if (mpvs.contains(name)) {
/*    */               if (logger.isWarnEnabled())
/*    */                 logger.warn("Skipping URI variable '" + name + "' because request contains bind value with same name."); 
/*    */             } else {
/*    */               mpvs.addPropertyValue(name, value);
/*    */             } 
/*    */           }); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/mvc/method/annotation/ExtendedServletRequestDataBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */