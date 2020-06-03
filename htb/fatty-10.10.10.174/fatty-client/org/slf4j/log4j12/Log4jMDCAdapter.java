/*    */ package org.slf4j.log4j12;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import org.apache.log4j.MDC;
/*    */ import org.apache.log4j.MDCFriend;
/*    */ import org.slf4j.spi.MDCAdapter;
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
/*    */ public class Log4jMDCAdapter
/*    */   implements MDCAdapter
/*    */ {
/*    */   static {
/* 37 */     if (VersionUtil.getJavaMajorVersion() >= 9) {
/* 38 */       MDCFriend.fixForJava9();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 44 */     Map map = MDC.getContext();
/* 45 */     if (map != null) {
/* 46 */       map.clear();
/*    */     }
/*    */   }
/*    */   
/*    */   public String get(String key) {
/* 51 */     return (String)MDC.get(key);
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
/*    */   public void put(String key, String val) {
/* 67 */     MDC.put(key, val);
/*    */   }
/*    */   
/*    */   public void remove(String key) {
/* 71 */     MDC.remove(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public Map getCopyOfContextMap() {
/* 76 */     Map<?, ?> old = MDC.getContext();
/* 77 */     if (old != null) {
/* 78 */       return new HashMap<Object, Object>(old);
/*    */     }
/* 80 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setContextMap(Map contextMap) {
/* 86 */     Map old = MDC.getContext();
/* 87 */     if (old == null) {
/* 88 */       Iterator<Map.Entry> entrySetIterator = contextMap.entrySet().iterator();
/* 89 */       while (entrySetIterator.hasNext()) {
/* 90 */         Map.Entry mapEntry = entrySetIterator.next();
/* 91 */         MDC.put((String)mapEntry.getKey(), mapEntry.getValue());
/*    */       } 
/*    */     } else {
/* 94 */       old.clear();
/* 95 */       old.putAll(contextMap);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/log4j12/Log4jMDCAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */