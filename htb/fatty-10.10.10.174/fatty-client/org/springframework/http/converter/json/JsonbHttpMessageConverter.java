/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Type;
/*     */ import javax.json.bind.Jsonb;
/*     */ import javax.json.bind.JsonbBuilder;
/*     */ import javax.json.bind.JsonbConfig;
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
/*     */ public class JsonbHttpMessageConverter
/*     */   extends AbstractJsonHttpMessageConverter
/*     */ {
/*     */   private Jsonb jsonb;
/*     */   
/*     */   public JsonbHttpMessageConverter() {
/*  54 */     this(JsonbBuilder.create());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonbHttpMessageConverter(JsonbConfig config) {
/*  62 */     this.jsonb = JsonbBuilder.create(config);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonbHttpMessageConverter(Jsonb jsonb) {
/*  70 */     Assert.notNull(jsonb, "A Jsonb instance is required");
/*  71 */     this.jsonb = jsonb;
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
/*     */   public void setJsonb(Jsonb jsonb) {
/*  85 */     Assert.notNull(jsonb, "A Jsonb instance is required");
/*  86 */     this.jsonb = jsonb;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jsonb getJsonb() {
/*  93 */     return this.jsonb;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object readInternal(Type resolvedType, Reader reader) throws Exception {
/*  99 */     return getJsonb().fromJson(reader, resolvedType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeInternal(Object o, @Nullable Type type, Writer writer) throws Exception {
/* 104 */     if (type instanceof java.lang.reflect.ParameterizedType) {
/* 105 */       getJsonb().toJson(o, type, writer);
/*     */     } else {
/*     */       
/* 108 */       getJsonb().toJson(o, writer);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/json/JsonbHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */