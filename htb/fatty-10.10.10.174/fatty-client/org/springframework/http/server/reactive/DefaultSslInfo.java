/*     */ package org.springframework.http.server.reactive;
/*     */ 
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ final class DefaultSslInfo
/*     */   implements SslInfo
/*     */ {
/*     */   @Nullable
/*     */   private final String sessionId;
/*     */   @Nullable
/*     */   private final X509Certificate[] peerCertificates;
/*     */   
/*     */   DefaultSslInfo(@Nullable String sessionId, X509Certificate[] peerCertificates) {
/*  44 */     Assert.notNull(peerCertificates, "No SSL certificates");
/*  45 */     this.sessionId = sessionId;
/*  46 */     this.peerCertificates = peerCertificates;
/*     */   }
/*     */   
/*     */   DefaultSslInfo(SSLSession session) {
/*  50 */     Assert.notNull(session, "SSLSession is required");
/*  51 */     this.sessionId = initSessionId(session);
/*  52 */     this.peerCertificates = initCertificates(session);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSessionId() {
/*  59 */     return this.sessionId;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public X509Certificate[] getPeerCertificates() {
/*  65 */     return this.peerCertificates;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static String initSessionId(SSLSession session) {
/*  71 */     byte[] bytes = session.getId();
/*  72 */     if (bytes == null) {
/*  73 */       return null;
/*     */     }
/*     */     
/*  76 */     StringBuilder sb = new StringBuilder();
/*  77 */     for (byte b : bytes) {
/*  78 */       String digit = Integer.toHexString(b);
/*  79 */       if (digit.length() < 2) {
/*  80 */         sb.append('0');
/*     */       }
/*  82 */       if (digit.length() > 2) {
/*  83 */         digit = digit.substring(digit.length() - 2);
/*     */       }
/*  85 */       sb.append(digit);
/*     */     } 
/*  87 */     return sb.toString();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static X509Certificate[] initCertificates(SSLSession session) {
/*     */     Certificate[] certificates;
/*     */     try {
/*  94 */       certificates = session.getPeerCertificates();
/*     */     }
/*  96 */     catch (Throwable ex) {
/*  97 */       return null;
/*     */     } 
/*     */     
/* 100 */     List<X509Certificate> result = new ArrayList<>(certificates.length);
/* 101 */     for (Certificate certificate : certificates) {
/* 102 */       if (certificate instanceof X509Certificate) {
/* 103 */         result.add((X509Certificate)certificate);
/*     */       }
/*     */     } 
/* 106 */     return !result.isEmpty() ? result.<X509Certificate>toArray(new X509Certificate[0]) : null;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/server/reactive/DefaultSslInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */