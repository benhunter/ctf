/*     */ package org.springframework.web;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletException;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class HttpRequestMethodNotSupportedException
/*     */   extends ServletException
/*     */ {
/*     */   private final String method;
/*     */   @Nullable
/*     */   private final String[] supportedMethods;
/*     */   
/*     */   public HttpRequestMethodNotSupportedException(String method) {
/*  51 */     this(method, (String[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequestMethodNotSupportedException(String method, String msg) {
/*  60 */     this(method, null, msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequestMethodNotSupportedException(String method, @Nullable Collection<String> supportedMethods) {
/*  69 */     this(method, (supportedMethods != null) ? StringUtils.toStringArray(supportedMethods) : null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequestMethodNotSupportedException(String method, @Nullable String[] supportedMethods) {
/*  78 */     this(method, supportedMethods, "Request method '" + method + "' not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpRequestMethodNotSupportedException(String method, @Nullable String[] supportedMethods, String msg) {
/*  88 */     super(msg);
/*  89 */     this.method = method;
/*  90 */     this.supportedMethods = supportedMethods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethod() {
/*  98 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String[] getSupportedMethods() {
/* 106 */     return this.supportedMethods;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Set<HttpMethod> getSupportedHttpMethods() {
/* 116 */     if (this.supportedMethods == null) {
/* 117 */       return null;
/*     */     }
/* 119 */     List<HttpMethod> supportedMethods = new LinkedList<>();
/* 120 */     for (String value : this.supportedMethods) {
/* 121 */       HttpMethod resolved = HttpMethod.resolve(value);
/* 122 */       if (resolved != null) {
/* 123 */         supportedMethods.add(resolved);
/*     */       }
/*     */     } 
/* 126 */     return EnumSet.copyOf(supportedMethods);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/HttpRequestMethodNotSupportedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */