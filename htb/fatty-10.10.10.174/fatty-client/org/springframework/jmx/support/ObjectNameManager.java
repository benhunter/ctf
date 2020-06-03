/*    */ package org.springframework.jmx.support;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import javax.management.MalformedObjectNameException;
/*    */ import javax.management.ObjectName;
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
/*    */ 
/*    */ public final class ObjectNameManager
/*    */ {
/*    */   public static ObjectName getInstance(Object objectName) throws MalformedObjectNameException {
/* 47 */     if (objectName instanceof ObjectName) {
/* 48 */       return (ObjectName)objectName;
/*    */     }
/* 50 */     if (!(objectName instanceof String)) {
/* 51 */       throw new MalformedObjectNameException("Invalid ObjectName value type [" + objectName
/* 52 */           .getClass().getName() + "]: only ObjectName and String supported.");
/*    */     }
/* 54 */     return getInstance((String)objectName);
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
/*    */   public static ObjectName getInstance(String objectName) throws MalformedObjectNameException {
/* 66 */     return ObjectName.getInstance(objectName);
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ObjectName getInstance(String domainName, String key, String value) throws MalformedObjectNameException {
/* 83 */     return ObjectName.getInstance(domainName, key, value);
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
/*    */ 
/*    */ 
/*    */   
/*    */   public static ObjectName getInstance(String domainName, Hashtable<String, String> properties) throws MalformedObjectNameException {
/* 99 */     return ObjectName.getInstance(domainName, properties);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/support/ObjectNameManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */