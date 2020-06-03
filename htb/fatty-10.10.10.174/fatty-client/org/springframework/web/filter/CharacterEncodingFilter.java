/*     */ package org.springframework.web.filter;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.FilterChain;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
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
/*     */ public class CharacterEncodingFilter
/*     */   extends OncePerRequestFilter
/*     */ {
/*     */   @Nullable
/*     */   private String encoding;
/*     */   private boolean forceRequestEncoding = false;
/*     */   private boolean forceResponseEncoding = false;
/*     */   
/*     */   public CharacterEncodingFilter() {}
/*     */   
/*     */   public CharacterEncodingFilter(String encoding) {
/*  71 */     this(encoding, false);
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
/*     */   public CharacterEncodingFilter(String encoding, boolean forceEncoding) {
/*  84 */     this(encoding, forceEncoding, forceEncoding);
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
/*     */   public CharacterEncodingFilter(String encoding, boolean forceRequestEncoding, boolean forceResponseEncoding) {
/* 100 */     Assert.hasLength(encoding, "Encoding must not be empty");
/* 101 */     this.encoding = encoding;
/* 102 */     this.forceRequestEncoding = forceRequestEncoding;
/* 103 */     this.forceResponseEncoding = forceResponseEncoding;
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
/*     */   public void setEncoding(@Nullable String encoding) {
/* 115 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEncoding() {
/* 124 */     return this.encoding;
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
/*     */   public void setForceEncoding(boolean forceEncoding) {
/* 140 */     this.forceRequestEncoding = forceEncoding;
/* 141 */     this.forceResponseEncoding = forceEncoding;
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
/*     */   public void setForceRequestEncoding(boolean forceRequestEncoding) {
/* 154 */     this.forceRequestEncoding = forceRequestEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isForceRequestEncoding() {
/* 162 */     return this.forceRequestEncoding;
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
/*     */   public void setForceResponseEncoding(boolean forceResponseEncoding) {
/* 174 */     this.forceResponseEncoding = forceResponseEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isForceResponseEncoding() {
/* 182 */     return this.forceResponseEncoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
/* 191 */     String encoding = getEncoding();
/* 192 */     if (encoding != null) {
/* 193 */       if (isForceRequestEncoding() || request.getCharacterEncoding() == null) {
/* 194 */         request.setCharacterEncoding(encoding);
/*     */       }
/* 196 */       if (isForceResponseEncoding()) {
/* 197 */         response.setCharacterEncoding(encoding);
/*     */       }
/*     */     } 
/* 200 */     filterChain.doFilter((ServletRequest)request, (ServletResponse)response);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/filter/CharacterEncodingFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */