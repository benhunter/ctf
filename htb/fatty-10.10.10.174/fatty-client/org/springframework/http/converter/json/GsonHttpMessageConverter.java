/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Type;
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
/*     */ public class GsonHttpMessageConverter
/*     */   extends AbstractJsonHttpMessageConverter
/*     */ {
/*     */   private Gson gson;
/*     */   
/*     */   public GsonHttpMessageConverter() {
/*  56 */     this.gson = new Gson();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GsonHttpMessageConverter(Gson gson) {
/*  65 */     Assert.notNull(gson, "A Gson instance is required");
/*  66 */     this.gson = gson;
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
/*     */   public void setGson(Gson gson) {
/*  78 */     Assert.notNull(gson, "A Gson instance is required");
/*  79 */     this.gson = gson;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Gson getGson() {
/*  86 */     return this.gson;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object readInternal(Type resolvedType, Reader reader) throws Exception {
/*  92 */     return getGson().fromJson(reader, resolvedType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(Object o, @Nullable Type type, Writer writer) throws Exception {
/* 102 */     if (type instanceof java.lang.reflect.ParameterizedType) {
/* 103 */       getGson().toJson(o, type, writer);
/*     */     } else {
/*     */       
/* 106 */       getGson().toJson(o, writer);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/json/GsonHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */