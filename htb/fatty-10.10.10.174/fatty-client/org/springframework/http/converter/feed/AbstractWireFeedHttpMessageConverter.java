/*     */ package org.springframework.http.converter.feed;
/*     */ 
/*     */ import com.rometools.rome.feed.WireFeed;
/*     */ import com.rometools.rome.io.FeedException;
/*     */ import com.rometools.rome.io.WireFeedInput;
/*     */ import com.rometools.rome.io.WireFeedOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
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
/*     */ public abstract class AbstractWireFeedHttpMessageConverter<T extends WireFeed>
/*     */   extends AbstractHttpMessageConverter<T>
/*     */ {
/*  59 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */ 
/*     */   
/*     */   protected AbstractWireFeedHttpMessageConverter(MediaType supportedMediaType) {
/*  63 */     super(supportedMediaType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/*  72 */     WireFeedInput feedInput = new WireFeedInput();
/*  73 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/*     */     
/*  75 */     Charset charset = (contentType != null && contentType.getCharset() != null) ? contentType.getCharset() : DEFAULT_CHARSET;
/*     */     try {
/*  77 */       Reader reader = new InputStreamReader(inputMessage.getBody(), charset);
/*  78 */       return (T)feedInput.build(reader);
/*     */     }
/*  80 */     catch (FeedException ex) {
/*  81 */       throw new HttpMessageNotReadableException("Could not read WireFeed: " + ex.getMessage(), ex, inputMessage);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(T wireFeed, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/*  90 */     Charset charset = StringUtils.hasLength(wireFeed.getEncoding()) ? Charset.forName(wireFeed.getEncoding()) : DEFAULT_CHARSET;
/*  91 */     MediaType contentType = outputMessage.getHeaders().getContentType();
/*  92 */     if (contentType != null) {
/*  93 */       contentType = new MediaType(contentType.getType(), contentType.getSubtype(), charset);
/*  94 */       outputMessage.getHeaders().setContentType(contentType);
/*     */     } 
/*     */     
/*  97 */     WireFeedOutput feedOutput = new WireFeedOutput();
/*     */     try {
/*  99 */       Writer writer = new OutputStreamWriter(outputMessage.getBody(), charset);
/* 100 */       feedOutput.output((WireFeed)wireFeed, writer);
/*     */     }
/* 102 */     catch (FeedException ex) {
/* 103 */       throw new HttpMessageNotWritableException("Could not write WireFeed: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/feed/AbstractWireFeedHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */