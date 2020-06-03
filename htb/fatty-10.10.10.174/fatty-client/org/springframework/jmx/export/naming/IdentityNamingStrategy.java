/*    */ package org.springframework.jmx.export.naming;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import javax.management.MalformedObjectNameException;
/*    */ import javax.management.ObjectName;
/*    */ import org.springframework.jmx.support.ObjectNameManager;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ClassUtils;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IdentityNamingStrategy
/*    */   implements ObjectNamingStrategy
/*    */ {
/*    */   public static final String TYPE_KEY = "type";
/*    */   public static final String HASH_CODE_KEY = "hashCode";
/*    */   
/*    */   public ObjectName getObjectName(Object managedBean, @Nullable String beanKey) throws MalformedObjectNameException {
/* 58 */     String domain = ClassUtils.getPackageName(managedBean.getClass());
/* 59 */     Hashtable<String, String> keys = new Hashtable<>();
/* 60 */     keys.put("type", ClassUtils.getShortName(managedBean.getClass()));
/* 61 */     keys.put("hashCode", ObjectUtils.getIdentityHexString(managedBean));
/* 62 */     return ObjectNameManager.getInstance(domain, keys);
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jmx/export/naming/IdentityNamingStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */