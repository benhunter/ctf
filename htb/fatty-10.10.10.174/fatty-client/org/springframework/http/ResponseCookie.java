/*     */ package org.springframework.http;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ResponseCookie
/*     */   extends HttpCookie
/*     */ {
/*     */   private final Duration maxAge;
/*     */   @Nullable
/*     */   private final String domain;
/*     */   @Nullable
/*     */   private final String path;
/*     */   private final boolean secure;
/*     */   private final boolean httpOnly;
/*     */   @Nullable
/*     */   private final String sameSite;
/*     */   
/*     */   private ResponseCookie(String name, String value, Duration maxAge, @Nullable String domain, @Nullable String path, boolean secure, boolean httpOnly, @Nullable String sameSite) {
/*  60 */     super(name, value);
/*  61 */     Assert.notNull(maxAge, "Max age must not be null");
/*  62 */     this.maxAge = maxAge;
/*  63 */     this.domain = domain;
/*  64 */     this.path = path;
/*  65 */     this.secure = secure;
/*  66 */     this.httpOnly = httpOnly;
/*  67 */     this.sameSite = sameSite;
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
/*     */   public Duration getMaxAge() {
/*  79 */     return this.maxAge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDomain() {
/*  87 */     return this.domain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPath() {
/*  95 */     return this.path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSecure() {
/* 102 */     return this.secure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHttpOnly() {
/* 110 */     return this.httpOnly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSameSite() {
/* 122 */     return this.sameSite;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 128 */     if (this == other) {
/* 129 */       return true;
/*     */     }
/* 131 */     if (!(other instanceof ResponseCookie)) {
/* 132 */       return false;
/*     */     }
/* 134 */     ResponseCookie otherCookie = (ResponseCookie)other;
/* 135 */     return (getName().equalsIgnoreCase(otherCookie.getName()) && 
/* 136 */       ObjectUtils.nullSafeEquals(this.path, otherCookie.getPath()) && 
/* 137 */       ObjectUtils.nullSafeEquals(this.domain, otherCookie.getDomain()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 142 */     int result = super.hashCode();
/* 143 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.domain);
/* 144 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.path);
/* 145 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 150 */     StringBuilder sb = new StringBuilder();
/* 151 */     sb.append(getName()).append('=').append(getValue());
/* 152 */     if (StringUtils.hasText(getPath())) {
/* 153 */       sb.append("; Path=").append(getPath());
/*     */     }
/* 155 */     if (StringUtils.hasText(this.domain)) {
/* 156 */       sb.append("; Domain=").append(this.domain);
/*     */     }
/* 158 */     if (!this.maxAge.isNegative()) {
/* 159 */       sb.append("; Max-Age=").append(this.maxAge.getSeconds());
/* 160 */       sb.append("; Expires=");
/* 161 */       long millis = (this.maxAge.getSeconds() > 0L) ? (System.currentTimeMillis() + this.maxAge.toMillis()) : 0L;
/* 162 */       sb.append(HttpHeaders.formatDate(millis));
/*     */     } 
/* 164 */     if (this.secure) {
/* 165 */       sb.append("; Secure");
/*     */     }
/* 167 */     if (this.httpOnly) {
/* 168 */       sb.append("; HttpOnly");
/*     */     }
/* 170 */     if (StringUtils.hasText(this.sameSite)) {
/* 171 */       sb.append("; SameSite=").append(this.sameSite);
/*     */     }
/* 173 */     return sb.toString();
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
/*     */   public static ResponseCookieBuilder from(final String name, final String value) {
/* 186 */     return new ResponseCookieBuilder()
/*     */       {
/* 188 */         private Duration maxAge = Duration.ofSeconds(-1L);
/*     */         
/*     */         @Nullable
/*     */         private String domain;
/*     */         
/*     */         @Nullable
/*     */         private String path;
/*     */         
/*     */         private boolean secure;
/*     */         
/*     */         private boolean httpOnly;
/*     */         
/*     */         @Nullable
/*     */         private String sameSite;
/*     */ 
/*     */         
/*     */         public ResponseCookie.ResponseCookieBuilder maxAge(Duration maxAge) {
/* 205 */           this.maxAge = maxAge;
/* 206 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public ResponseCookie.ResponseCookieBuilder maxAge(long maxAgeSeconds) {
/* 211 */           this.maxAge = (maxAgeSeconds >= 0L) ? Duration.ofSeconds(maxAgeSeconds) : Duration.ofSeconds(-1L);
/* 212 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public ResponseCookie.ResponseCookieBuilder domain(String domain) {
/* 217 */           this.domain = domain;
/* 218 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public ResponseCookie.ResponseCookieBuilder path(String path) {
/* 223 */           this.path = path;
/* 224 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public ResponseCookie.ResponseCookieBuilder secure(boolean secure) {
/* 229 */           this.secure = secure;
/* 230 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public ResponseCookie.ResponseCookieBuilder httpOnly(boolean httpOnly) {
/* 235 */           this.httpOnly = httpOnly;
/* 236 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public ResponseCookie.ResponseCookieBuilder sameSite(@Nullable String sameSite) {
/* 241 */           this.sameSite = sameSite;
/* 242 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public ResponseCookie build() {
/* 247 */           return new ResponseCookie(name, value, this.maxAge, this.domain, this.path, this.secure, this.httpOnly, this.sameSite);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static interface ResponseCookieBuilder {
/*     */     ResponseCookieBuilder maxAge(Duration param1Duration);
/*     */     
/*     */     ResponseCookieBuilder maxAge(long param1Long);
/*     */     
/*     */     ResponseCookieBuilder path(String param1String);
/*     */     
/*     */     ResponseCookieBuilder domain(String param1String);
/*     */     
/*     */     ResponseCookieBuilder secure(boolean param1Boolean);
/*     */     
/*     */     ResponseCookieBuilder httpOnly(boolean param1Boolean);
/*     */     
/*     */     ResponseCookieBuilder sameSite(@Nullable String param1String);
/*     */     
/*     */     ResponseCookie build();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/ResponseCookie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */