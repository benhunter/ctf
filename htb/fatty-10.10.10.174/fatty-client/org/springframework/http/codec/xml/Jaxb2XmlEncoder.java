/*     */ package org.springframework.http.codec.xml;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.MarshalException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.AbstractSingleValueEncoder;
/*     */ import org.springframework.core.codec.CodecException;
/*     */ import org.springframework.core.codec.EncodingException;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jaxb2XmlEncoder
/*     */   extends AbstractSingleValueEncoder<Object>
/*     */ {
/*  60 */   private final JaxbContextContainer jaxbContexts = new JaxbContextContainer();
/*     */   
/*  62 */   private Function<Marshaller, Marshaller> marshallerProcessor = Function.identity();
/*     */ 
/*     */   
/*     */   public Jaxb2XmlEncoder() {
/*  66 */     super(new MimeType[] { MimeTypeUtils.APPLICATION_XML, MimeTypeUtils.TEXT_XML });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMarshallerProcessor(Function<Marshaller, Marshaller> processor) {
/*  76 */     this.marshallerProcessor = this.marshallerProcessor.andThen(processor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Function<Marshaller, Marshaller> getMarshallerProcessor() {
/*  84 */     return this.marshallerProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/*  90 */     if (super.canEncode(elementType, mimeType)) {
/*  91 */       Class<?> outputClass = elementType.toClass();
/*  92 */       return (outputClass.isAnnotationPresent((Class)XmlRootElement.class) || outputClass
/*  93 */         .isAnnotationPresent((Class)XmlType.class));
/*     */     } 
/*     */     
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Flux<DataBuffer> encode(Object value, DataBufferFactory bufferFactory, ResolvableType type, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 104 */     if (!Hints.isLoggingSuppressed(hints)) {
/* 105 */       LogFormatUtils.traceDebug(this.logger, traceOn -> {
/*     */             String formatted = LogFormatUtils.formatValue(value, !traceOn.booleanValue());
/*     */             
/*     */             return Hints.getLogPrefix(hints) + "Encoding [" + formatted + "]";
/*     */           });
/*     */     }
/* 111 */     return Mono.fromCallable(() -> {
/*     */           boolean release = true;
/*     */           
/*     */           DataBuffer buffer = bufferFactory.allocateBuffer(1024);
/*     */           try {
/*     */             OutputStream outputStream = buffer.asOutputStream();
/*     */             Class<?> clazz = ClassUtils.getUserClass(value);
/*     */             Marshaller marshaller = initMarshaller(clazz);
/*     */             marshaller.marshal(value, outputStream);
/*     */             release = false;
/*     */             return buffer;
/* 122 */           } catch (MarshalException ex) {
/*     */             
/*     */             throw new EncodingException("Could not marshal " + value.getClass() + " to XML", ex);
/*     */           }
/* 126 */           catch (JAXBException ex) {
/*     */             
/*     */             throw new CodecException("Invalid JAXB configuration", ex);
/*     */           } finally {
/*     */             if (release) {
/*     */               DataBufferUtils.release(buffer);
/*     */             }
/*     */           } 
/* 134 */         }).flux();
/*     */   }
/*     */   
/*     */   private Marshaller initMarshaller(Class<?> clazz) throws JAXBException {
/* 138 */     Marshaller marshaller = this.jaxbContexts.createMarshaller(clazz);
/* 139 */     marshaller.setProperty("jaxb.encoding", StandardCharsets.UTF_8.name());
/* 140 */     marshaller = this.marshallerProcessor.apply(marshaller);
/* 141 */     return marshaller;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/xml/Jaxb2XmlEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */