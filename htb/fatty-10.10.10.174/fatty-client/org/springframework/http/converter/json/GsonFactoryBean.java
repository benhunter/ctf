/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ public class GsonFactoryBean
/*     */   implements FactoryBean<Gson>, InitializingBean
/*     */ {
/*     */   private boolean base64EncodeByteArrays = false;
/*     */   private boolean serializeNulls = false;
/*     */   private boolean prettyPrinting = false;
/*     */   private boolean disableHtmlEscaping = false;
/*     */   @Nullable
/*     */   private String dateFormatPattern;
/*     */   @Nullable
/*     */   private Gson gson;
/*     */   
/*     */   public void setBase64EncodeByteArrays(boolean base64EncodeByteArrays) {
/*  62 */     this.base64EncodeByteArrays = base64EncodeByteArrays;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSerializeNulls(boolean serializeNulls) {
/*  73 */     this.serializeNulls = serializeNulls;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrettyPrinting(boolean prettyPrinting) {
/*  84 */     this.prettyPrinting = prettyPrinting;
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
/*     */   public void setDisableHtmlEscaping(boolean disableHtmlEscaping) {
/*  96 */     this.disableHtmlEscaping = disableHtmlEscaping;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDateFormatPattern(String dateFormatPattern) {
/* 107 */     this.dateFormatPattern = dateFormatPattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 114 */     GsonBuilder builder = this.base64EncodeByteArrays ? GsonBuilderUtils.gsonBuilderWithBase64EncodedByteArrays() : new GsonBuilder();
/* 115 */     if (this.serializeNulls) {
/* 116 */       builder.serializeNulls();
/*     */     }
/* 118 */     if (this.prettyPrinting) {
/* 119 */       builder.setPrettyPrinting();
/*     */     }
/* 121 */     if (this.disableHtmlEscaping) {
/* 122 */       builder.disableHtmlEscaping();
/*     */     }
/* 124 */     if (this.dateFormatPattern != null) {
/* 125 */       builder.setDateFormat(this.dateFormatPattern);
/*     */     }
/* 127 */     this.gson = builder.create();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Gson getObject() {
/* 137 */     return this.gson;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getObjectType() {
/* 142 */     return Gson.class;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSingleton() {
/* 147 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/json/GsonFactoryBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */