/*    */ package org.springframework.web.bind.support;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.context.request.WebRequest;
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
/*    */ public class DefaultSessionAttributeStore
/*    */   implements SessionAttributeStore
/*    */ {
/* 36 */   private String attributeNamePrefix = "";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAttributeNamePrefix(@Nullable String attributeNamePrefix) {
/* 45 */     this.attributeNamePrefix = (attributeNamePrefix != null) ? attributeNamePrefix : "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void storeAttribute(WebRequest request, String attributeName, Object attributeValue) {
/* 51 */     Assert.notNull(request, "WebRequest must not be null");
/* 52 */     Assert.notNull(attributeName, "Attribute name must not be null");
/* 53 */     Assert.notNull(attributeValue, "Attribute value must not be null");
/* 54 */     String storeAttributeName = getAttributeNameInSession(request, attributeName);
/* 55 */     request.setAttribute(storeAttributeName, attributeValue, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object retrieveAttribute(WebRequest request, String attributeName) {
/* 61 */     Assert.notNull(request, "WebRequest must not be null");
/* 62 */     Assert.notNull(attributeName, "Attribute name must not be null");
/* 63 */     String storeAttributeName = getAttributeNameInSession(request, attributeName);
/* 64 */     return request.getAttribute(storeAttributeName, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void cleanupAttribute(WebRequest request, String attributeName) {
/* 69 */     Assert.notNull(request, "WebRequest must not be null");
/* 70 */     Assert.notNull(attributeName, "Attribute name must not be null");
/* 71 */     String storeAttributeName = getAttributeNameInSession(request, attributeName);
/* 72 */     request.removeAttribute(storeAttributeName, 1);
/*    */   }
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
/*    */   protected String getAttributeNameInSession(WebRequest request, String attributeName) {
/* 85 */     return this.attributeNamePrefix + attributeName;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/support/DefaultSessionAttributeStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */