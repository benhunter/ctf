/*    */ package org.springframework.beans.factory.support;
/*    */ 
/*    */ import org.springframework.beans.BeanMetadataAttributeAccessor;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AutowireCandidateQualifier
/*    */   extends BeanMetadataAttributeAccessor
/*    */ {
/*    */   public static final String VALUE_KEY = "value";
/*    */   private final String typeName;
/*    */   
/*    */   public AutowireCandidateQualifier(Class<?> type) {
/* 49 */     this(type.getName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AutowireCandidateQualifier(String typeName) {
/* 60 */     Assert.notNull(typeName, "Type name must not be null");
/* 61 */     this.typeName = typeName;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AutowireCandidateQualifier(Class<?> type, Object value) {
/* 72 */     this(type.getName(), value);
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
/*    */   public AutowireCandidateQualifier(String typeName, Object value) {
/* 85 */     Assert.notNull(typeName, "Type name must not be null");
/* 86 */     this.typeName = typeName;
/* 87 */     setAttribute("value", value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTypeName() {
/* 97 */     return this.typeName;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/beans/factory/support/AutowireCandidateQualifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */