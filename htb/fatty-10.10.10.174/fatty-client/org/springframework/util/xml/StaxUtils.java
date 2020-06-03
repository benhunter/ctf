/*     */ package org.springframework.util.xml;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import javax.xml.stream.XMLEventFactory;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLEventWriter;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLResolver;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stax.StAXResult;
/*     */ import javax.xml.transform.stax.StAXSource;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StaxUtils
/*     */ {
/*     */   private static final XMLResolver NO_OP_XML_RESOLVER = (publicID, systemID, base, ns) -> StreamUtils.emptyInput();
/*     */   
/*     */   public static XMLInputFactory createDefensiveInputFactory() {
/*  66 */     return createDefensiveInputFactory(XMLInputFactory::newInstance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends XMLInputFactory> T createDefensiveInputFactory(Supplier<T> instanceSupplier) {
/*  76 */     XMLInputFactory xMLInputFactory = (XMLInputFactory)instanceSupplier.get();
/*  77 */     xMLInputFactory.setProperty("javax.xml.stream.supportDTD", Boolean.valueOf(false));
/*  78 */     xMLInputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.valueOf(false));
/*  79 */     xMLInputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
/*  80 */     return (T)xMLInputFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createStaxSource(XMLStreamReader streamReader) {
/*  89 */     return new StAXSource(streamReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createStaxSource(XMLEventReader eventReader) throws XMLStreamException {
/*  98 */     return new StAXSource(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createCustomStaxSource(XMLStreamReader streamReader) {
/* 107 */     return new StaxSource(streamReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Source createCustomStaxSource(XMLEventReader eventReader) {
/* 116 */     return new StaxSource(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStaxSource(Source source) {
/* 126 */     return (source instanceof StAXSource || source instanceof StaxSource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static XMLStreamReader getXMLStreamReader(Source source) {
/* 138 */     if (source instanceof StAXSource) {
/* 139 */       return ((StAXSource)source).getXMLStreamReader();
/*     */     }
/* 141 */     if (source instanceof StaxSource) {
/* 142 */       return ((StaxSource)source).getXMLStreamReader();
/*     */     }
/*     */     
/* 145 */     throw new IllegalArgumentException("Source '" + source + "' is neither StaxSource nor StAXSource");
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
/*     */   @Nullable
/*     */   public static XMLEventReader getXMLEventReader(Source source) {
/* 158 */     if (source instanceof StAXSource) {
/* 159 */       return ((StAXSource)source).getXMLEventReader();
/*     */     }
/* 161 */     if (source instanceof StaxSource) {
/* 162 */       return ((StaxSource)source).getXMLEventReader();
/*     */     }
/*     */     
/* 165 */     throw new IllegalArgumentException("Source '" + source + "' is neither StaxSource nor StAXSource");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createStaxResult(XMLStreamWriter streamWriter) {
/* 175 */     return new StAXResult(streamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createStaxResult(XMLEventWriter eventWriter) {
/* 184 */     return new StAXResult(eventWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createCustomStaxResult(XMLStreamWriter streamWriter) {
/* 193 */     return new StaxResult(streamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Result createCustomStaxResult(XMLEventWriter eventWriter) {
/* 202 */     return new StaxResult(eventWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isStaxResult(Result result) {
/* 212 */     return (result instanceof StAXResult || result instanceof StaxResult);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static XMLStreamWriter getXMLStreamWriter(Result result) {
/* 224 */     if (result instanceof StAXResult) {
/* 225 */       return ((StAXResult)result).getXMLStreamWriter();
/*     */     }
/* 227 */     if (result instanceof StaxResult) {
/* 228 */       return ((StaxResult)result).getXMLStreamWriter();
/*     */     }
/*     */     
/* 231 */     throw new IllegalArgumentException("Result '" + result + "' is neither StaxResult nor StAXResult");
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
/*     */   @Nullable
/*     */   public static XMLEventWriter getXMLEventWriter(Result result) {
/* 244 */     if (result instanceof StAXResult) {
/* 245 */       return ((StAXResult)result).getXMLEventWriter();
/*     */     }
/* 247 */     if (result instanceof StaxResult) {
/* 248 */       return ((StaxResult)result).getXMLEventWriter();
/*     */     }
/*     */     
/* 251 */     throw new IllegalArgumentException("Result '" + result + "' is neither StaxResult nor StAXResult");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLEventReader createXMLEventReader(List<XMLEvent> events) {
/* 262 */     return new ListBasedXMLEventReader(events);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentHandler createContentHandler(XMLStreamWriter streamWriter) {
/* 271 */     return new StaxStreamHandler(streamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContentHandler createContentHandler(XMLEventWriter eventWriter) {
/* 280 */     return new StaxEventHandler(eventWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLReader createXMLReader(XMLStreamReader streamReader) {
/* 289 */     return new StaxStreamXMLReader(streamReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLReader createXMLReader(XMLEventReader eventReader) {
/* 298 */     return new StaxEventXMLReader(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamReader createEventStreamReader(XMLEventReader eventReader) throws XMLStreamException {
/* 308 */     return new XMLEventStreamReader(eventReader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamWriter createEventStreamWriter(XMLEventWriter eventWriter) {
/* 317 */     return new XMLEventStreamWriter(eventWriter, XMLEventFactory.newFactory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLStreamWriter createEventStreamWriter(XMLEventWriter eventWriter, XMLEventFactory eventFactory) {
/* 326 */     return new XMLEventStreamWriter(eventWriter, eventFactory);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/util/xml/StaxUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */