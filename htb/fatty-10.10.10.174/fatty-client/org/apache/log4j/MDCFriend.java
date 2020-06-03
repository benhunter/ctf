/*    */ package org.apache.log4j;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.log4j.helpers.ThreadLocalMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MDCFriend
/*    */ {
/*    */   public static void fixForJava9() {
/*    */     try {
/* 13 */       Field mdcField = MDC.class.getDeclaredField("mdc");
/*    */       
/* 15 */       MDC mdcSingleton = (MDC)mdcField.get(null);
/* 16 */       Field tlmField = MDC.class.getDeclaredField("tlm");
/*    */       
/* 18 */       Field java1Field = MDC.class.getDeclaredField("java1");
/* 19 */       Object mdcSingleton_tlm = tlmField.get(mdcSingleton);
/*    */       
/* 21 */       if (mdcSingleton_tlm == null) {
/* 22 */         tlmField.set(mdcSingleton, new ThreadLocalMap());
/* 23 */         java1Field.setBoolean(mdcSingleton, false);
/* 24 */         setRemoveMethod(mdcSingleton);
/*    */       } 
/* 26 */     } catch (SecurityException e) {
/* 27 */       e.printStackTrace();
/* 28 */     } catch (NoSuchFieldException e) {
/* 29 */       e.printStackTrace();
/* 30 */     } catch (IllegalArgumentException e) {
/* 31 */       e.printStackTrace();
/* 32 */     } catch (IllegalAccessException e) {
/* 33 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static void setRemoveMethod(MDC mdc) {
/*    */     try {
/* 40 */       Method removeMethod = ThreadLocal.class.getMethod("remove", new Class[0]);
/* 41 */       Field removeMethodField = MDC.class.getDeclaredField("removeMethod");
/* 42 */       removeMethodField.setAccessible(true);
/* 43 */       removeMethodField.set(mdc, removeMethod);
/* 44 */     } catch (NoSuchMethodException e) {
/* 45 */       e.printStackTrace();
/* 46 */     } catch (SecurityException e) {
/* 47 */       e.printStackTrace();
/* 48 */     } catch (NoSuchFieldException e) {
/* 49 */       e.printStackTrace();
/* 50 */     } catch (IllegalArgumentException e) {
/* 51 */       e.printStackTrace();
/* 52 */     } catch (IllegalAccessException e) {
/* 53 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/log4j/MDCFriend.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */