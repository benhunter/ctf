/*     */ package org.springframework.http.codec.xml;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlSchema;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.codec.AbstractDecoder;
/*     */ import org.springframework.core.codec.CodecException;
/*     */ import org.springframework.core.codec.DecodingException;
/*     */ import org.springframework.core.codec.Hints;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import org.springframework.util.xml.StaxUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jaxb2XmlDecoder
/*     */   extends AbstractDecoder<Object>
/*     */ {
/*     */   private static final String JAXB_DEFAULT_ANNOTATION_VALUE = "##default";
/*  74 */   private final XmlEventDecoder xmlEventDecoder = new XmlEventDecoder();
/*     */   
/*  76 */   private final JaxbContextContainer jaxbContexts = new JaxbContextContainer();
/*     */   
/*  78 */   private Function<Unmarshaller, Unmarshaller> unmarshallerProcessor = Function.identity();
/*     */ 
/*     */   
/*     */   public Jaxb2XmlDecoder() {
/*  82 */     super(new MimeType[] { MimeTypeUtils.APPLICATION_XML, MimeTypeUtils.TEXT_XML });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Jaxb2XmlDecoder(MimeType... supportedMimeTypes) {
/*  91 */     super(supportedMimeTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUnmarshallerProcessor(Function<Unmarshaller, Unmarshaller> processor) {
/* 101 */     this.unmarshallerProcessor = this.unmarshallerProcessor.andThen(processor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Function<Unmarshaller, Unmarshaller> getUnmarshallerProcessor() {
/* 109 */     return this.unmarshallerProcessor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 115 */     Class<?> outputClass = elementType.toClass();
/* 116 */     return ((outputClass.isAnnotationPresent((Class)XmlRootElement.class) || outputClass
/* 117 */       .isAnnotationPresent((Class)XmlType.class)) && super.canDecode(elementType, mimeType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<Object> decode(Publisher<DataBuffer> inputStream, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 124 */     Flux<XMLEvent> xmlEventFlux = this.xmlEventDecoder.decode(inputStream, 
/* 125 */         ResolvableType.forClass(XMLEvent.class), mimeType, hints);
/*     */     
/* 127 */     Class<?> outputClass = elementType.toClass();
/* 128 */     QName typeName = toQName(outputClass);
/* 129 */     Flux<List<XMLEvent>> splitEvents = split(xmlEventFlux, typeName);
/*     */     
/* 131 */     return splitEvents.map(events -> {
/*     */           Object value = unmarshal(events, outputClass);
/*     */           LogFormatUtils.traceDebug(this.logger, ());
/*     */           return value;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<Object> decodeToMono(Publisher<DataBuffer> inputStream, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 145 */     return decode(inputStream, elementType, mimeType, hints).singleOrEmpty();
/*     */   }
/*     */   
/*     */   private Object unmarshal(List<XMLEvent> events, Class<?> outputClass) {
/*     */     try {
/* 150 */       Unmarshaller unmarshaller = initUnmarshaller(outputClass);
/* 151 */       XMLEventReader eventReader = StaxUtils.createXMLEventReader(events);
/* 152 */       if (outputClass.isAnnotationPresent((Class)XmlRootElement.class)) {
/* 153 */         return unmarshaller.unmarshal(eventReader);
/*     */       }
/*     */       
/* 156 */       JAXBElement<?> jaxbElement = unmarshaller.unmarshal(eventReader, outputClass);
/* 157 */       return jaxbElement.getValue();
/*     */     
/*     */     }
/* 160 */     catch (UnmarshalException ex) {
/* 161 */       throw new DecodingException("Could not unmarshal XML to " + outputClass, ex);
/*     */     }
/* 163 */     catch (JAXBException ex) {
/* 164 */       throw new CodecException("Invalid JAXB configuration", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Unmarshaller initUnmarshaller(Class<?> outputClass) throws JAXBException {
/* 169 */     Unmarshaller unmarshaller = this.jaxbContexts.createUnmarshaller(outputClass);
/* 170 */     return this.unmarshallerProcessor.apply(unmarshaller);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   QName toQName(Class<?> outputClass) {
/*     */     String localPart;
/*     */     String namespaceUri;
/* 181 */     if (outputClass.isAnnotationPresent((Class)XmlRootElement.class)) {
/* 182 */       XmlRootElement annotation = outputClass.<XmlRootElement>getAnnotation(XmlRootElement.class);
/* 183 */       localPart = annotation.name();
/* 184 */       namespaceUri = annotation.namespace();
/*     */     }
/* 186 */     else if (outputClass.isAnnotationPresent((Class)XmlType.class)) {
/* 187 */       XmlType annotation = outputClass.<XmlType>getAnnotation(XmlType.class);
/* 188 */       localPart = annotation.name();
/* 189 */       namespaceUri = annotation.namespace();
/*     */     } else {
/*     */       
/* 192 */       throw new IllegalArgumentException("Output class [" + outputClass.getName() + "] is neither annotated with @XmlRootElement nor @XmlType");
/*     */     } 
/*     */ 
/*     */     
/* 196 */     if ("##default".equals(localPart)) {
/* 197 */       localPart = ClassUtils.getShortNameAsProperty(outputClass);
/*     */     }
/* 199 */     if ("##default".equals(namespaceUri)) {
/* 200 */       Package outputClassPackage = outputClass.getPackage();
/* 201 */       if (outputClassPackage != null && outputClassPackage.isAnnotationPresent((Class)XmlSchema.class)) {
/* 202 */         XmlSchema annotation = outputClassPackage.<XmlSchema>getAnnotation(XmlSchema.class);
/* 203 */         namespaceUri = annotation.namespace();
/*     */       } else {
/*     */         
/* 206 */         namespaceUri = "";
/*     */       } 
/*     */     } 
/* 209 */     return new QName(namespaceUri, localPart);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Flux<List<XMLEvent>> split(Flux<XMLEvent> xmlEventFlux, QName desiredName) {
/* 236 */     return xmlEventFlux.flatMap(new SplitFunction(desiredName));
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SplitFunction
/*     */     implements Function<XMLEvent, Publisher<? extends List<XMLEvent>>>
/*     */   {
/*     */     private final QName desiredName;
/*     */     
/*     */     @Nullable
/*     */     private List<XMLEvent> events;
/* 247 */     private int elementDepth = 0;
/*     */     
/* 249 */     private int barrier = Integer.MAX_VALUE;
/*     */     
/*     */     public SplitFunction(QName desiredName) {
/* 252 */       this.desiredName = desiredName;
/*     */     }
/*     */ 
/*     */     
/*     */     public Publisher<? extends List<XMLEvent>> apply(XMLEvent event) {
/* 257 */       if (event.isStartElement()) {
/* 258 */         if (this.barrier == Integer.MAX_VALUE) {
/* 259 */           QName startElementName = event.asStartElement().getName();
/* 260 */           if (this.desiredName.equals(startElementName)) {
/* 261 */             this.events = new ArrayList<>();
/* 262 */             this.barrier = this.elementDepth;
/*     */           } 
/*     */         } 
/* 265 */         this.elementDepth++;
/*     */       } 
/* 267 */       if (this.elementDepth > this.barrier) {
/* 268 */         Assert.state((this.events != null), "No XMLEvent List");
/* 269 */         this.events.add(event);
/*     */       } 
/* 271 */       if (event.isEndElement()) {
/* 272 */         this.elementDepth--;
/* 273 */         if (this.elementDepth == this.barrier) {
/* 274 */           this.barrier = Integer.MAX_VALUE;
/* 275 */           Assert.state((this.events != null), "No XMLEvent List");
/* 276 */           return (Publisher<? extends List<XMLEvent>>)Mono.just(this.events);
/*     */         } 
/*     */       } 
/* 279 */       return (Publisher<? extends List<XMLEvent>>)Mono.empty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/xml/Jaxb2XmlDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */