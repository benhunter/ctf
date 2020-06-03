/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import org.springframework.http.converter.HttpMessageConversionException;
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
/*     */ public abstract class AbstractJaxb2HttpMessageConverter<T>
/*     */   extends AbstractXmlHttpMessageConverter<T>
/*     */ {
/*  40 */   private final ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<>(64);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Marshaller createMarshaller(Class<?> clazz) {
/*     */     try {
/*  51 */       JAXBContext jaxbContext = getJaxbContext(clazz);
/*  52 */       Marshaller marshaller = jaxbContext.createMarshaller();
/*  53 */       customizeMarshaller(marshaller);
/*  54 */       return marshaller;
/*     */     }
/*  56 */     catch (JAXBException ex) {
/*  57 */       throw new HttpMessageConversionException("Could not create Marshaller for class [" + clazz + "]: " + ex
/*  58 */           .getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customizeMarshaller(Marshaller marshaller) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Unmarshaller createUnmarshaller(Class<?> clazz) {
/*     */     try {
/*  80 */       JAXBContext jaxbContext = getJaxbContext(clazz);
/*  81 */       Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
/*  82 */       customizeUnmarshaller(unmarshaller);
/*  83 */       return unmarshaller;
/*     */     }
/*  85 */     catch (JAXBException ex) {
/*  86 */       throw new HttpMessageConversionException("Could not create Unmarshaller for class [" + clazz + "]: " + ex
/*  87 */           .getMessage(), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customizeUnmarshaller(Unmarshaller unmarshaller) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JAXBContext getJaxbContext(Class<?> clazz) {
/* 108 */     Assert.notNull(clazz, "Class must not be null");
/* 109 */     JAXBContext jaxbContext = this.jaxbContexts.get(clazz);
/* 110 */     if (jaxbContext == null) {
/*     */       try {
/* 112 */         jaxbContext = JAXBContext.newInstance(new Class[] { clazz });
/* 113 */         this.jaxbContexts.putIfAbsent(clazz, jaxbContext);
/*     */       }
/* 115 */       catch (JAXBException ex) {
/* 116 */         throw new HttpMessageConversionException("Could not instantiate JAXBContext for class [" + clazz + "]: " + ex
/* 117 */             .getMessage(), ex);
/*     */       } 
/*     */     }
/* 120 */     return jaxbContext;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/xml/AbstractJaxb2HttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */