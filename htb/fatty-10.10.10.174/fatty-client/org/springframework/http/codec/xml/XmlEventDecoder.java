/*     */ package org.springframework.http.codec.xml;
/*     */ 
/*     */ import com.fasterxml.aalto.AsyncByteBufferFeeder;
/*     */ import com.fasterxml.aalto.AsyncXMLInputFactory;
/*     */ import com.fasterxml.aalto.AsyncXMLStreamReader;
/*     */ import com.fasterxml.aalto.evt.EventAllocatorImpl;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import javax.xml.stream.util.XMLEventAllocator;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.AbstractDecoder;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import org.springframework.util.xml.StaxUtils;
/*     */ import reactor.core.Exceptions;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.SignalType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlEventDecoder
/*     */   extends AbstractDecoder<XMLEvent>
/*     */ {
/*  83 */   private static final XMLInputFactory inputFactory = StaxUtils.createDefensiveInputFactory();
/*     */   
/*  85 */   private static final boolean aaltoPresent = ClassUtils.isPresent("com.fasterxml.aalto.AsyncXMLStreamReader", XmlEventDecoder.class
/*  86 */       .getClassLoader());
/*     */   
/*  88 */   boolean useAalto = aaltoPresent;
/*     */ 
/*     */   
/*     */   public XmlEventDecoder() {
/*  92 */     super(new MimeType[] { MimeTypeUtils.APPLICATION_XML, MimeTypeUtils.TEXT_XML });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<XMLEvent> decode(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 101 */     if (this.useAalto) {
/* 102 */       AaltoDataBufferToXmlEvent mapper = new AaltoDataBufferToXmlEvent();
/* 103 */       return Flux.from(input)
/* 104 */         .flatMapIterable(mapper)
/* 105 */         .doFinally(signalType -> mapper.endOfInput());
/*     */     } 
/*     */     
/* 108 */     return DataBufferUtils.join(input)
/* 109 */       .flatMapIterable(buffer -> {
/*     */           try {
/*     */             InputStream is = buffer.asInputStream();
/*     */             
/*     */             Iterator eventReader = inputFactory.createXMLEventReader(is);
/*     */             List<XMLEvent> result = new ArrayList<>();
/*     */             eventReader.forEachRemaining(());
/*     */             return result;
/* 117 */           } catch (XMLStreamException ex) {
/*     */             throw Exceptions.propagate(ex);
/*     */           } finally {
/*     */             DataBufferUtils.release(buffer);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class AaltoDataBufferToXmlEvent
/*     */     implements Function<DataBuffer, List<? extends XMLEvent>>
/*     */   {
/* 134 */     private static final AsyncXMLInputFactory inputFactory = (AsyncXMLInputFactory)StaxUtils.createDefensiveInputFactory(com.fasterxml.aalto.stax.InputFactoryImpl::new);
/*     */     
/* 136 */     private final AsyncXMLStreamReader<AsyncByteBufferFeeder> streamReader = inputFactory
/* 137 */       .createAsyncForByteBuffer();
/*     */     
/* 139 */     private final XMLEventAllocator eventAllocator = (XMLEventAllocator)EventAllocatorImpl.getDefaultInstance();
/*     */ 
/*     */ 
/*     */     
/*     */     public List<? extends XMLEvent> apply(DataBuffer dataBuffer) {
/*     */       try {
/* 145 */         ((AsyncByteBufferFeeder)this.streamReader.getInputFeeder()).feedInput(dataBuffer.asByteBuffer());
/* 146 */         List<XMLEvent> events = new ArrayList<>();
/*     */         
/* 148 */         while (this.streamReader.next() != 257) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 153 */           XMLEvent event = this.eventAllocator.allocate((XMLStreamReader)this.streamReader);
/* 154 */           events.add(event);
/* 155 */           if (event.isEndDocument()) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         
/* 160 */         return events;
/*     */       }
/* 162 */       catch (XMLStreamException ex) {
/* 163 */         throw Exceptions.propagate(ex);
/*     */       } finally {
/*     */         
/* 166 */         DataBufferUtils.release(dataBuffer);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void endOfInput() {
/* 171 */       ((AsyncByteBufferFeeder)this.streamReader.getInputFeeder()).endOfInput();
/*     */     }
/*     */     
/*     */     private AaltoDataBufferToXmlEvent() {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/xml/XmlEventDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */