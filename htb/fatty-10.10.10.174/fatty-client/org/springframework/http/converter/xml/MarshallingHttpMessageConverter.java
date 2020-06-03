/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import org.springframework.beans.TypeMismatchException;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.oxm.Marshaller;
/*     */ import org.springframework.oxm.Unmarshaller;
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
/*     */ public class MarshallingHttpMessageConverter
/*     */   extends AbstractXmlHttpMessageConverter<Object>
/*     */ {
/*     */   @Nullable
/*     */   private Marshaller marshaller;
/*     */   @Nullable
/*     */   private Unmarshaller unmarshaller;
/*     */   
/*     */   public MarshallingHttpMessageConverter() {}
/*     */   
/*     */   public MarshallingHttpMessageConverter(Marshaller marshaller) {
/*  70 */     Assert.notNull(marshaller, "Marshaller must not be null");
/*  71 */     this.marshaller = marshaller;
/*  72 */     if (marshaller instanceof Unmarshaller) {
/*  73 */       this.unmarshaller = (Unmarshaller)marshaller;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarshallingHttpMessageConverter(Marshaller marshaller, Unmarshaller unmarshaller) {
/*  84 */     Assert.notNull(marshaller, "Marshaller must not be null");
/*  85 */     Assert.notNull(unmarshaller, "Unmarshaller must not be null");
/*  86 */     this.marshaller = marshaller;
/*  87 */     this.unmarshaller = unmarshaller;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMarshaller(Marshaller marshaller) {
/*  95 */     this.marshaller = marshaller;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnmarshaller(Unmarshaller unmarshaller) {
/* 102 */     this.unmarshaller = unmarshaller;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
/* 108 */     return (canRead(mediaType) && this.unmarshaller != null && this.unmarshaller.supports(clazz));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
/* 113 */     return (canWrite(mediaType) && this.marshaller != null && this.marshaller.supports(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source) throws Exception {
/* 124 */     Assert.notNull(this.unmarshaller, "Property 'unmarshaller' is required");
/* 125 */     Object result = this.unmarshaller.unmarshal(source);
/* 126 */     if (!clazz.isInstance(result)) {
/* 127 */       throw new TypeMismatchException(result, clazz);
/*     */     }
/* 129 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeToResult(Object o, HttpHeaders headers, Result result) throws Exception {
/* 134 */     Assert.notNull(this.marshaller, "Property 'marshaller' is required");
/* 135 */     this.marshaller.marshal(o, result);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/xml/MarshallingHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */