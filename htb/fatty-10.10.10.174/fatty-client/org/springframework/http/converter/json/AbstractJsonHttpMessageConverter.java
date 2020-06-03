/*     */ package org.springframework.http.converter.json;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
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
/*     */ public abstract class AbstractJsonHttpMessageConverter
/*     */   extends AbstractGenericHttpMessageConverter<Object>
/*     */ {
/*  56 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */   
/*     */   @Nullable
/*     */   private String jsonPrefix;
/*     */ 
/*     */   
/*     */   public AbstractJsonHttpMessageConverter() {
/*  63 */     super(new MediaType[] { MediaType.APPLICATION_JSON, new MediaType("application", "*+json") });
/*  64 */     setDefaultCharset(DEFAULT_CHARSET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJsonPrefix(String jsonPrefix) {
/*  73 */     this.jsonPrefix = jsonPrefix;
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
/*     */   public void setPrefixJson(boolean prefixJson) {
/*  86 */     this.jsonPrefix = prefixJson ? ")]}', " : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/*  94 */     return readResolved(GenericTypeResolver.resolveType(type, contextClass), inputMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 101 */     return readResolved(clazz, inputMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Object readResolved(Type resolvedType, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 107 */     Reader reader = getReader(inputMessage);
/*     */     try {
/* 109 */       return readInternal(resolvedType, reader);
/*     */     }
/* 111 */     catch (Exception ex) {
/* 112 */       throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex, inputMessage);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void writeInternal(Object o, @Nullable Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 120 */     Writer writer = getWriter(outputMessage);
/* 121 */     if (this.jsonPrefix != null) {
/* 122 */       writer.append(this.jsonPrefix);
/*     */     }
/*     */     try {
/* 125 */       writeInternal(o, type, writer);
/*     */     }
/* 127 */     catch (Exception ex) {
/* 128 */       throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
/*     */     } 
/* 130 */     writer.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Object readInternal(Type paramType, Reader paramReader) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void writeInternal(Object paramObject, @Nullable Type paramType, Writer paramWriter) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Reader getReader(HttpInputMessage inputMessage) throws IOException {
/* 154 */     return new InputStreamReader(inputMessage.getBody(), getCharset(inputMessage.getHeaders()));
/*     */   }
/*     */   
/*     */   private static Writer getWriter(HttpOutputMessage outputMessage) throws IOException {
/* 158 */     return new OutputStreamWriter(outputMessage.getBody(), getCharset(outputMessage.getHeaders()));
/*     */   }
/*     */   
/*     */   private static Charset getCharset(HttpHeaders headers) {
/* 162 */     Charset charset = (headers.getContentType() != null) ? headers.getContentType().getCharset() : null;
/* 163 */     return (charset != null) ? charset : DEFAULT_CHARSET;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/json/AbstractJsonHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */