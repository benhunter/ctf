/*    */ package org.springframework.jndi;
/*    */ 
/*    */ import javax.naming.InitialContext;
/*    */ import javax.naming.NamingException;
/*    */ import org.springframework.core.SpringProperties;
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
/*    */ public class JndiLocatorDelegate
/*    */   extends JndiLocatorSupport
/*    */ {
/*    */   public static final String IGNORE_JNDI_PROPERTY_NAME = "spring.jndi.ignore";
/* 54 */   private static final boolean shouldIgnoreDefaultJndiEnvironment = SpringProperties.getFlag("spring.jndi.ignore");
/*    */ 
/*    */ 
/*    */   
/*    */   public Object lookup(String jndiName) throws NamingException {
/* 59 */     return super.lookup(jndiName);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T lookup(String jndiName, @Nullable Class<T> requiredType) throws NamingException {
/* 64 */     return super.lookup(jndiName, requiredType);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static JndiLocatorDelegate createDefaultResourceRefLocator() {
/* 74 */     JndiLocatorDelegate jndiLocator = new JndiLocatorDelegate();
/* 75 */     jndiLocator.setResourceRef(true);
/* 76 */     return jndiLocator;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isDefaultJndiEnvironmentAvailable() {
/* 86 */     if (shouldIgnoreDefaultJndiEnvironment) {
/* 87 */       return false;
/*    */     }
/*    */     try {
/* 90 */       (new InitialContext()).getEnvironment();
/* 91 */       return true;
/*    */     }
/* 93 */     catch (Throwable ex) {
/* 94 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/jndi/JndiLocatorDelegate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */