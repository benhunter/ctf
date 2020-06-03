/*    */ package org.apache.commons.logging;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
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
/*    */ @Deprecated
/*    */ public class LogFactoryService
/*    */   extends LogFactory
/*    */ {
/* 36 */   private final Map<String, Object> attributes = new ConcurrentHashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   public Log getInstance(Class<?> clazz) {
/* 41 */     return getInstance(clazz.getName());
/*    */   }
/*    */ 
/*    */   
/*    */   public Log getInstance(String name) {
/* 46 */     return LogAdapter.createLog(name);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setAttribute(String name, Object value) {
/* 53 */     if (value != null) {
/* 54 */       this.attributes.put(name, value);
/*    */     } else {
/*    */       
/* 57 */       this.attributes.remove(name);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void removeAttribute(String name) {
/* 62 */     this.attributes.remove(name);
/*    */   }
/*    */   
/*    */   public Object getAttribute(String name) {
/* 66 */     return this.attributes.get(name);
/*    */   }
/*    */   
/*    */   public String[] getAttributeNames() {
/* 70 */     return (String[])this.attributes.keySet().toArray((Object[])new String[0]);
/*    */   }
/*    */   
/*    */   public void release() {}
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/commons/logging/LogFactoryService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */