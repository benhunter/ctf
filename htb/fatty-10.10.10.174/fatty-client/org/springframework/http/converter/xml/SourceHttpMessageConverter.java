/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringReader;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLResolver;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stax.StAXSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.AbstractHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.StreamUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SourceHttpMessageConverter<T extends Source>
/*     */   extends AbstractHttpMessageConverter<T>
/*     */ {
/*     */   private static final EntityResolver NO_OP_ENTITY_RESOLVER = (publicId, systemId) -> new InputSource(new StringReader(""));
/*     */   private static final XMLResolver NO_OP_XML_RESOLVER = (publicID, systemID, base, ns) -> StreamUtils.emptyInput();
/*  75 */   private static final Set<Class<?>> SUPPORTED_CLASSES = new HashSet<>(8);
/*     */   
/*     */   static {
/*  78 */     SUPPORTED_CLASSES.add(DOMSource.class);
/*  79 */     SUPPORTED_CLASSES.add(SAXSource.class);
/*  80 */     SUPPORTED_CLASSES.add(StAXSource.class);
/*  81 */     SUPPORTED_CLASSES.add(StreamSource.class);
/*  82 */     SUPPORTED_CLASSES.add(Source.class);
/*     */   }
/*     */ 
/*     */   
/*  86 */   private final TransformerFactory transformerFactory = TransformerFactory.newInstance();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean supportDtd = false;
/*     */ 
/*     */   
/*     */   private boolean processExternalEntities = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public SourceHttpMessageConverter() {
/*  98 */     super(new MediaType[] { MediaType.APPLICATION_XML, MediaType.TEXT_XML, new MediaType("application", "*+xml") });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSupportDtd(boolean supportDtd) {
/* 107 */     this.supportDtd = supportDtd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSupportDtd() {
/* 114 */     return this.supportDtd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessExternalEntities(boolean processExternalEntities) {
/* 124 */     this.processExternalEntities = processExternalEntities;
/* 125 */     if (processExternalEntities) {
/* 126 */       this.supportDtd = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProcessExternalEntities() {
/* 134 */     return this.processExternalEntities;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supports(Class<?> clazz) {
/* 140 */     return SUPPORTED_CLASSES.contains(clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected T readInternal(Class<? extends T> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 148 */     InputStream body = inputMessage.getBody();
/* 149 */     if (DOMSource.class == clazz) {
/* 150 */       return (T)readDOMSource(body, inputMessage);
/*     */     }
/* 152 */     if (SAXSource.class == clazz) {
/* 153 */       return (T)readSAXSource(body, inputMessage);
/*     */     }
/* 155 */     if (StAXSource.class == clazz) {
/* 156 */       return (T)readStAXSource(body, inputMessage);
/*     */     }
/* 158 */     if (StreamSource.class == clazz || Source.class == clazz) {
/* 159 */       return (T)readStreamSource(body);
/*     */     }
/*     */     
/* 162 */     throw new HttpMessageNotReadableException("Could not read class [" + clazz + "]. Only DOMSource, SAXSource, StAXSource, and StreamSource are supported.", inputMessage);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private DOMSource readDOMSource(InputStream body, HttpInputMessage inputMessage) throws IOException {
/*     */     try {
/* 169 */       DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 170 */       documentBuilderFactory.setNamespaceAware(true);
/* 171 */       documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", 
/* 172 */           !isSupportDtd());
/* 173 */       documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", 
/* 174 */           isProcessExternalEntities());
/* 175 */       DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
/* 176 */       if (!isProcessExternalEntities()) {
/* 177 */         documentBuilder.setEntityResolver(NO_OP_ENTITY_RESOLVER);
/*     */       }
/* 179 */       Document document = documentBuilder.parse(body);
/* 180 */       return new DOMSource(document);
/*     */     }
/* 182 */     catch (NullPointerException ex) {
/* 183 */       if (!isSupportDtd()) {
/* 184 */         throw new HttpMessageNotReadableException("NPE while unmarshalling: This can happen due to the presence of DTD declarations which are disabled.", ex, inputMessage);
/*     */       }
/*     */       
/* 187 */       throw ex;
/*     */     }
/* 189 */     catch (ParserConfigurationException ex) {
/* 190 */       throw new HttpMessageNotReadableException("Could not set feature: " + ex
/* 191 */           .getMessage(), ex, inputMessage);
/*     */     }
/* 193 */     catch (SAXException ex) {
/* 194 */       throw new HttpMessageNotReadableException("Could not parse document: " + ex
/* 195 */           .getMessage(), ex, inputMessage);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private SAXSource readSAXSource(InputStream body, HttpInputMessage inputMessage) throws IOException {
/*     */     try {
/* 202 */       XMLReader xmlReader = XMLReaderFactory.createXMLReader();
/* 203 */       xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
/* 204 */       xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", isProcessExternalEntities());
/* 205 */       if (!isProcessExternalEntities()) {
/* 206 */         xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
/*     */       }
/* 208 */       byte[] bytes = StreamUtils.copyToByteArray(body);
/* 209 */       return new SAXSource(xmlReader, new InputSource(new ByteArrayInputStream(bytes)));
/*     */     }
/* 211 */     catch (SAXException ex) {
/* 212 */       throw new HttpMessageNotReadableException("Could not parse document: " + ex
/* 213 */           .getMessage(), ex, inputMessage);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Source readStAXSource(InputStream body, HttpInputMessage inputMessage) {
/*     */     try {
/* 219 */       XMLInputFactory inputFactory = XMLInputFactory.newInstance();
/* 220 */       inputFactory.setProperty("javax.xml.stream.supportDTD", Boolean.valueOf(isSupportDtd()));
/* 221 */       inputFactory.setProperty("javax.xml.stream.isSupportingExternalEntities", Boolean.valueOf(isProcessExternalEntities()));
/* 222 */       if (!isProcessExternalEntities()) {
/* 223 */         inputFactory.setXMLResolver(NO_OP_XML_RESOLVER);
/*     */       }
/* 225 */       XMLStreamReader streamReader = inputFactory.createXMLStreamReader(body);
/* 226 */       return new StAXSource(streamReader);
/*     */     }
/* 228 */     catch (XMLStreamException ex) {
/* 229 */       throw new HttpMessageNotReadableException("Could not parse document: " + ex
/* 230 */           .getMessage(), ex, inputMessage);
/*     */     } 
/*     */   }
/*     */   
/*     */   private StreamSource readStreamSource(InputStream body) throws IOException {
/* 235 */     byte[] bytes = StreamUtils.copyToByteArray(body);
/* 236 */     return new StreamSource(new ByteArrayInputStream(bytes));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Long getContentLength(T t, @Nullable MediaType contentType) {
/* 242 */     if (t instanceof DOMSource) {
/*     */       try {
/* 244 */         CountingOutputStream os = new CountingOutputStream();
/* 245 */         transform((Source)t, new StreamResult(os));
/* 246 */         return Long.valueOf(os.count);
/*     */       }
/* 248 */       catch (TransformerException transformerException) {}
/*     */     }
/*     */ 
/*     */     
/* 252 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeInternal(T t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/*     */     try {
/* 259 */       Result result = new StreamResult(outputMessage.getBody());
/* 260 */       transform((Source)t, result);
/*     */     }
/* 262 */     catch (TransformerException ex) {
/* 263 */       throw new HttpMessageNotWritableException("Could not transform [" + t + "] to output message", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void transform(Source source, Result result) throws TransformerException {
/* 268 */     this.transformerFactory.newTransformer().transform(source, result);
/*     */   }
/*     */   
/*     */   private static class CountingOutputStream
/*     */     extends OutputStream
/*     */   {
/* 274 */     long count = 0L;
/*     */ 
/*     */     
/*     */     public void write(int b) throws IOException {
/* 278 */       this.count++;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) throws IOException {
/* 283 */       this.count += b.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException {
/* 288 */       this.count += len;
/*     */     }
/*     */     
/*     */     private CountingOutputStream() {}
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/xml/SourceHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */