/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.MarshalException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageConversionException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jaxb2RootElementHttpMessageConverter
/*     */   extends AbstractJaxb2HttpMessageConverter<Object>
/*     */ {
/*     */   private boolean supportDtd = false;
/*     */   private boolean processExternalEntities = false;
/*     */   
/*     */   public void setSupportDtd(boolean supportDtd) {
/*  75 */     this.supportDtd = supportDtd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSupportDtd() {
/*  82 */     return this.supportDtd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProcessExternalEntities(boolean processExternalEntities) {
/*  92 */     this.processExternalEntities = processExternalEntities;
/*  93 */     if (processExternalEntities) {
/*  94 */       this.supportDtd = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isProcessExternalEntities() {
/* 102 */     return this.processExternalEntities;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
/* 108 */     return ((clazz.isAnnotationPresent((Class)XmlRootElement.class) || clazz.isAnnotationPresent((Class)XmlType.class)) && 
/* 109 */       canRead(mediaType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
/* 114 */     return (AnnotationUtils.findAnnotation(clazz, XmlRootElement.class) != null && canWrite(mediaType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object readFromSource(Class<?> clazz, HttpHeaders headers, Source source) throws Exception {
/*     */     try {
/* 126 */       source = processSource(source);
/* 127 */       Unmarshaller unmarshaller = createUnmarshaller(clazz);
/* 128 */       if (clazz.isAnnotationPresent((Class)XmlRootElement.class)) {
/* 129 */         return unmarshaller.unmarshal(source);
/*     */       }
/*     */       
/* 132 */       JAXBElement<?> jaxbElement = unmarshaller.unmarshal(source, clazz);
/* 133 */       return jaxbElement.getValue();
/*     */     
/*     */     }
/* 136 */     catch (NullPointerException ex) {
/* 137 */       if (!isSupportDtd()) {
/* 138 */         throw new IllegalStateException("NPE while unmarshalling. This can happen due to the presence of DTD declarations which are disabled.", ex);
/*     */       }
/*     */       
/* 141 */       throw ex;
/*     */     }
/* 143 */     catch (UnmarshalException ex) {
/* 144 */       throw ex;
/*     */     }
/* 146 */     catch (JAXBException ex) {
/* 147 */       throw new HttpMessageConversionException("Invalid JAXB setup: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Source processSource(Source source) {
/* 153 */     if (source instanceof StreamSource) {
/* 154 */       StreamSource streamSource = (StreamSource)source;
/* 155 */       InputSource inputSource = new InputSource(streamSource.getInputStream());
/*     */       try {
/* 157 */         XMLReader xmlReader = XMLReaderFactory.createXMLReader();
/* 158 */         xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !isSupportDtd());
/* 159 */         String featureName = "http://xml.org/sax/features/external-general-entities";
/* 160 */         xmlReader.setFeature(featureName, isProcessExternalEntities());
/* 161 */         if (!isProcessExternalEntities()) {
/* 162 */           xmlReader.setEntityResolver(NO_OP_ENTITY_RESOLVER);
/*     */         }
/* 164 */         return new SAXSource(xmlReader, inputSource);
/*     */       }
/* 166 */       catch (SAXException ex) {
/* 167 */         this.logger.warn("Processing of external entities could not be disabled", ex);
/* 168 */         return source;
/*     */       } 
/*     */     } 
/*     */     
/* 172 */     return source;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeToResult(Object o, HttpHeaders headers, Result result) throws Exception {
/*     */     try {
/* 179 */       Class<?> clazz = ClassUtils.getUserClass(o);
/* 180 */       Marshaller marshaller = createMarshaller(clazz);
/* 181 */       setCharset(headers.getContentType(), marshaller);
/* 182 */       marshaller.marshal(o, result);
/*     */     }
/* 184 */     catch (MarshalException ex) {
/* 185 */       throw ex;
/*     */     }
/* 187 */     catch (JAXBException ex) {
/* 188 */       throw new HttpMessageConversionException("Invalid JAXB setup: " + ex.getMessage(), ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setCharset(@Nullable MediaType contentType, Marshaller marshaller) throws PropertyException {
/* 193 */     if (contentType != null && contentType.getCharset() != null)
/* 194 */       marshaller.setProperty("jaxb.encoding", contentType.getCharset().name()); 
/*     */   }
/*     */   
/*     */   private static final EntityResolver NO_OP_ENTITY_RESOLVER = (publicId, systemId) -> new InputSource(new StringReader(""));
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/xml/Jaxb2RootElementHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */