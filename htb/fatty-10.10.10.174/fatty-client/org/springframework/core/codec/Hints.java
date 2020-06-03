/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Hints
/*     */ {
/*  39 */   public static final String LOG_PREFIX_HINT = Log.class.getName() + ".PREFIX";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   public static final String SUPPRESS_LOGGING_HINT = Log.class.getName() + ".SUPPRESS_LOGGING";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, Object> from(String hintName, Object value) {
/*  56 */     return Collections.singletonMap(hintName, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, Object> none() {
/*  64 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T getRequiredHint(@Nullable Map<String, Object> hints, String hintName) {
/*  77 */     if (hints == null) {
/*  78 */       throw new IllegalArgumentException("No hints map for required hint '" + hintName + "'");
/*     */     }
/*  80 */     T hint = (T)hints.get(hintName);
/*  81 */     if (hint == null) {
/*  82 */       throw new IllegalArgumentException("Hints map must contain the hint '" + hintName + "'");
/*     */     }
/*  84 */     return hint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getLogPrefix(@Nullable Map<String, Object> hints) {
/*  93 */     return (hints != null) ? (String)hints.getOrDefault(LOG_PREFIX_HINT, "") : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLoggingSuppressed(@Nullable Map<String, Object> hints) {
/* 102 */     return (hints != null && ((Boolean)hints.getOrDefault(SUPPRESS_LOGGING_HINT, Boolean.valueOf(false))).booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, Object> merge(Map<String, Object> hints1, Map<String, Object> hints2) {
/* 113 */     if (hints1.isEmpty() && hints2.isEmpty()) {
/* 114 */       return Collections.emptyMap();
/*     */     }
/* 116 */     if (hints2.isEmpty()) {
/* 117 */       return hints1;
/*     */     }
/* 119 */     if (hints1.isEmpty()) {
/* 120 */       return hints2;
/*     */     }
/*     */     
/* 123 */     Map<String, Object> result = new HashMap<>(hints1.size() + hints2.size());
/* 124 */     result.putAll(hints1);
/* 125 */     result.putAll(hints2);
/* 126 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, Object> merge(Map<String, Object> hints, String hintName, Object hintValue) {
/* 140 */     if (hints.isEmpty()) {
/* 141 */       return Collections.singletonMap(hintName, hintValue);
/*     */     }
/*     */     
/* 144 */     Map<String, Object> result = new HashMap<>(hints.size() + 1);
/* 145 */     result.putAll(hints);
/* 146 */     result.put(hintName, hintValue);
/* 147 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/core/codec/Hints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */