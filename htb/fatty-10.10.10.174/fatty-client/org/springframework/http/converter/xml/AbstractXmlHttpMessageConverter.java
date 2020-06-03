/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractXmlHttpMessageConverter<T>
/*     */   extends AbstractHttpMessageConverter<T>
/*     */ {
/*  51 */   private final TransformerFactory transformerFactory = TransformerFactory.newInstance();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractXmlHttpMessageConverter() {
/*  59 */     super(new MediaType[] { MediaType.APPLICATION_XML, MediaType.TEXT_XML, new MediaType("application", "*+xml") });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/*     */     try {
/*  68 */       return readFromSource(clazz, inputMessage.getHeaders(), new StreamSource(inputMessage.getBody()));
/*     */     }
/*  70 */     catch (IOException|org.springframework.http.converter.HttpMessageConversionException ex) {
/*  71 */       throw ex;
/*     */     }
/*  73 */     catch (Exception ex) {
/*  74 */       throw new HttpMessageNotReadableException("Could not unmarshal to [" + clazz + "]: " + ex.getMessage(), ex, inputMessage);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/*     */     try {
/*  84 */       writeToResult(t, outputMessage.getHeaders(), new StreamResult(outputMessage.getBody()));
/*     */     }
/*  86 */     catch (IOException|org.springframework.http.converter.HttpMessageConversionException ex) {
/*  87 */       throw ex;
/*     */     }
/*  89 */     catch (Exception ex) {
/*  90 */       throw new HttpMessageNotWritableException("Could not marshal [" + t + "]: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void transform(Source source, Result result) throws TransformerException {
/* 101 */     this.transformerFactory.newTransformer().transform(source, result);
/*     */   }
/*     */   
/*     */   protected abstract T readFromSource(Class<? extends T> paramClass, HttpHeaders paramHttpHeaders, Source paramSource) throws Exception;
/*     */   
/*     */   protected abstract void writeToResult(T paramT, HttpHeaders paramHttpHeaders, Result paramResult) throws Exception;
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/xml/AbstractXmlHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */