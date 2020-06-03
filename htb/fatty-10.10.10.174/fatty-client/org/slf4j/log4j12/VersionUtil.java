/*    */ package org.slf4j.log4j12;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.slf4j.helpers.Util;
/*    */ 
/*    */ public class VersionUtil
/*    */ {
/*    */   static final int MINIMAL_VERSION = 5;
/*    */   
/*    */   public static int getJavaMajorVersion() {
/* 11 */     String javaVersionString = Util.safeGetSystemProperty("java.version");
/* 12 */     return getJavaMajorVersion(javaVersionString);
/*    */   }
/*    */   
/*    */   public static int getJavaMajorVersion(String versionString) {
/* 16 */     if (versionString == null)
/* 17 */       return 5; 
/* 18 */     if (versionString.startsWith("1.")) {
/* 19 */       return versionString.charAt(2) - 48;
/*    */     }
/*    */     
/*    */     try {
/* 23 */       Method versionMethod = Runtime.class.getMethod("version", new Class[0]);
/* 24 */       Object versionObj = versionMethod.invoke(null, new Object[0]);
/* 25 */       Method majorMethod = versionObj.getClass().getMethod("major", new Class[0]);
/* 26 */       Integer resultInteger = (Integer)majorMethod.invoke(versionObj, new Object[0]);
/* 27 */       return resultInteger.intValue();
/* 28 */     } catch (Exception e) {
/* 29 */       return 5;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/log4j12/VersionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */