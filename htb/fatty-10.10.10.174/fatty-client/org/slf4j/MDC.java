/*     */ package org.slf4j;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.util.Map;
/*     */ import org.slf4j.helpers.NOPMDCAdapter;
/*     */ import org.slf4j.helpers.Util;
/*     */ import org.slf4j.spi.MDCAdapter;
/*     */ import org.slf4j.spi.SLF4JServiceProvider;
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
/*     */ public class MDC
/*     */ {
/*     */   static final String NULL_MDCA_URL = "http://www.slf4j.org/codes.html#null_MDCA";
/*     */   static final String NO_STATIC_MDC_BINDER_URL = "http://www.slf4j.org/codes.html#no_static_mdc_binder";
/*     */   static MDCAdapter mdcAdapter;
/*     */   
/*     */   public static class MDCCloseable
/*     */     implements Closeable
/*     */   {
/*     */     private final String key;
/*     */     
/*     */     private MDCCloseable(String key) {
/*  77 */       this.key = key;
/*     */     }
/*     */     
/*     */     public void close() {
/*  81 */       MDC.remove(this.key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  89 */     SLF4JServiceProvider provider = LoggerFactory.getProvider();
/*  90 */     if (provider != null) {
/*  91 */       mdcAdapter = provider.getMDCAdapter();
/*     */     } else {
/*  93 */       Util.report("Failed to find provider.");
/*  94 */       Util.report("Defaulting to no-operation MDCAdapter implementation.");
/*  95 */       mdcAdapter = (MDCAdapter)new NOPMDCAdapter();
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void put(String key, String val) throws IllegalArgumentException {
/* 115 */     if (key == null) {
/* 116 */       throw new IllegalArgumentException("key parameter cannot be null");
/*     */     }
/* 118 */     if (mdcAdapter == null) {
/* 119 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 121 */     mdcAdapter.put(key, val);
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
/*     */   public static MDCCloseable putCloseable(String key, String val) throws IllegalArgumentException {
/* 153 */     put(key, val);
/* 154 */     return new MDCCloseable(key);
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
/*     */ 
/*     */   
/*     */   public static String get(String key) throws IllegalArgumentException {
/* 170 */     if (key == null) {
/* 171 */       throw new IllegalArgumentException("key parameter cannot be null");
/*     */     }
/*     */     
/* 174 */     if (mdcAdapter == null) {
/* 175 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 177 */     return mdcAdapter.get(key);
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
/*     */   public static void remove(String key) throws IllegalArgumentException {
/* 191 */     if (key == null) {
/* 192 */       throw new IllegalArgumentException("key parameter cannot be null");
/*     */     }
/*     */     
/* 195 */     if (mdcAdapter == null) {
/* 196 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 198 */     mdcAdapter.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clear() {
/* 205 */     if (mdcAdapter == null) {
/* 206 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 208 */     mdcAdapter.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> getCopyOfContextMap() {
/* 219 */     if (mdcAdapter == null) {
/* 220 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 222 */     return mdcAdapter.getCopyOfContextMap();
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
/*     */   public static void setContextMap(Map<String, String> contextMap) {
/* 235 */     if (mdcAdapter == null) {
/* 236 */       throw new IllegalStateException("MDCAdapter cannot be null. See also http://www.slf4j.org/codes.html#null_MDCA");
/*     */     }
/* 238 */     mdcAdapter.setContextMap(contextMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MDCAdapter getMDCAdapter() {
/* 248 */     return mdcAdapter;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/MDC.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */