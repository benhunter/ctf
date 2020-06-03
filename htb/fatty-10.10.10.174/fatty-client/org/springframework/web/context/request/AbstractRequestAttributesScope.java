/*    */ package org.springframework.web.context.request;
/*    */ 
/*    */ import org.springframework.beans.factory.ObjectFactory;
/*    */ import org.springframework.beans.factory.config.Scope;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public abstract class AbstractRequestAttributesScope
/*    */   implements Scope
/*    */ {
/*    */   public Object get(String name, ObjectFactory<?> objectFactory) {
/* 42 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 43 */     Object scopedObject = attributes.getAttribute(name, getScope());
/* 44 */     if (scopedObject == null) {
/* 45 */       scopedObject = objectFactory.getObject();
/* 46 */       attributes.setAttribute(name, scopedObject, getScope());
/*    */ 
/*    */       
/* 49 */       Object retrievedObject = attributes.getAttribute(name, getScope());
/* 50 */       if (retrievedObject != null)
/*    */       {
/*    */         
/* 53 */         scopedObject = retrievedObject;
/*    */       }
/*    */     } 
/* 56 */     return scopedObject;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object remove(String name) {
/* 62 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 63 */     Object scopedObject = attributes.getAttribute(name, getScope());
/* 64 */     if (scopedObject != null) {
/* 65 */       attributes.removeAttribute(name, getScope());
/* 66 */       return scopedObject;
/*    */     } 
/*    */     
/* 69 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void registerDestructionCallback(String name, Runnable callback) {
/* 75 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 76 */     attributes.registerDestructionCallback(name, callback, getScope());
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object resolveContextualObject(String key) {
/* 82 */     RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
/* 83 */     return attributes.resolveReference(key);
/*    */   }
/*    */   
/*    */   protected abstract int getScope();
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/context/request/AbstractRequestAttributesScope.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */