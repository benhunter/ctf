/*     */ package org.springframework.web.servlet;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ 
/*     */ public final class FlashMap
/*     */   extends HashMap<String, Object>
/*     */   implements Comparable<FlashMap>
/*     */ {
/*     */   @Nullable
/*     */   private String targetRequestPath;
/*  55 */   private final MultiValueMap<String, String> targetRequestParams = (MultiValueMap<String, String>)new LinkedMultiValueMap(4);
/*     */   
/*  57 */   private long expirationTime = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTargetRequestPath(@Nullable String path) {
/*  66 */     this.targetRequestPath = path;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getTargetRequestPath() {
/*  74 */     return this.targetRequestPath;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FlashMap addTargetRequestParams(@Nullable MultiValueMap<String, String> params) {
/*  82 */     if (params != null) {
/*  83 */       params.forEach((key, values) -> {
/*     */             for (String value : values) {
/*     */               addTargetRequestParam(key, value);
/*     */             }
/*     */           });
/*     */     }
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FlashMap addTargetRequestParam(String name, String value) {
/*  98 */     if (StringUtils.hasText(name) && StringUtils.hasText(value)) {
/*  99 */       this.targetRequestParams.add(name, value);
/*     */     }
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiValueMap<String, String> getTargetRequestParams() {
/* 108 */     return this.targetRequestParams;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExpirationPeriod(int timeToLive) {
/* 116 */     this.expirationTime = System.currentTimeMillis() + (timeToLive * 1000);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setExpirationTime(long expirationTime) {
/* 125 */     this.expirationTime = expirationTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getExpirationTime() {
/* 134 */     return this.expirationTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExpired() {
/* 142 */     return (this.expirationTime != -1L && System.currentTimeMillis() > this.expirationTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(FlashMap other) {
/* 153 */     int thisUrlPath = (this.targetRequestPath != null) ? 1 : 0;
/* 154 */     int otherUrlPath = (other.targetRequestPath != null) ? 1 : 0;
/* 155 */     if (thisUrlPath != otherUrlPath) {
/* 156 */       return otherUrlPath - thisUrlPath;
/*     */     }
/*     */     
/* 159 */     return other.targetRequestParams.size() - this.targetRequestParams.size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object other) {
/* 165 */     if (this == other) {
/* 166 */       return true;
/*     */     }
/* 168 */     if (!(other instanceof FlashMap)) {
/* 169 */       return false;
/*     */     }
/* 171 */     FlashMap otherFlashMap = (FlashMap)other;
/* 172 */     return (super.equals(otherFlashMap) && 
/* 173 */       ObjectUtils.nullSafeEquals(this.targetRequestPath, otherFlashMap.targetRequestPath) && this.targetRequestParams
/* 174 */       .equals(otherFlashMap.targetRequestParams));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 179 */     int result = super.hashCode();
/* 180 */     result = 31 * result + ObjectUtils.nullSafeHashCode(this.targetRequestPath);
/* 181 */     result = 31 * result + this.targetRequestParams.hashCode();
/* 182 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 187 */     return "FlashMap [attributes=" + super.toString() + ", targetRequestPath=" + this.targetRequestPath + ", targetRequestParams=" + this.targetRequestParams + "]";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/FlashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */