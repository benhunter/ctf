/*     */ package org.springframework.http.converter.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.UnmarshalException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.GenericHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConversionException;
/*     */ import org.springframework.http.converter.HttpMessageNotReadableException;
/*     */ import org.springframework.http.converter.HttpMessageNotWritableException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.xml.StaxUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jaxb2CollectionHttpMessageConverter<T extends Collection>
/*     */   extends AbstractJaxb2HttpMessageConverter<T>
/*     */   implements GenericHttpMessageConverter<T>
/*     */ {
/*  67 */   private final XMLInputFactory inputFactory = createXmlInputFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Class<?> clazz, @Nullable MediaType mediaType) {
/*  76 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canRead(Type type, @Nullable Class<?> contextClass, @Nullable MediaType mediaType) {
/*  87 */     if (!(type instanceof ParameterizedType)) {
/*  88 */       return false;
/*     */     }
/*  90 */     ParameterizedType parameterizedType = (ParameterizedType)type;
/*  91 */     if (!(parameterizedType.getRawType() instanceof Class)) {
/*  92 */       return false;
/*     */     }
/*  94 */     Class<?> rawType = (Class)parameterizedType.getRawType();
/*  95 */     if (!Collection.class.isAssignableFrom(rawType)) {
/*  96 */       return false;
/*     */     }
/*  98 */     if ((parameterizedType.getActualTypeArguments()).length != 1) {
/*  99 */       return false;
/*     */     }
/* 101 */     Type typeArgument = parameterizedType.getActualTypeArguments()[0];
/* 102 */     if (!(typeArgument instanceof Class)) {
/* 103 */       return false;
/*     */     }
/* 105 */     Class<?> typeArgumentClass = (Class)typeArgument;
/* 106 */     return ((typeArgumentClass.isAnnotationPresent((Class)XmlRootElement.class) || typeArgumentClass
/* 107 */       .isAnnotationPresent((Class)XmlType.class)) && canRead(mediaType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canWrite(@Nullable Type type, @Nullable Class<?> clazz, @Nullable MediaType mediaType) {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean supports(Class<?> clazz) {
/* 131 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected T readFromSource(Class<? extends T> clazz, HttpHeaders headers, Source source) throws Exception {
/* 137 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T read(Type type, @Nullable Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
/* 145 */     ParameterizedType parameterizedType = (ParameterizedType)type;
/* 146 */     T result = createCollection((Class)parameterizedType.getRawType());
/* 147 */     Class<?> elementClass = (Class)parameterizedType.getActualTypeArguments()[0];
/*     */     
/*     */     try {
/* 150 */       Unmarshaller unmarshaller = createUnmarshaller(elementClass);
/* 151 */       XMLStreamReader streamReader = this.inputFactory.createXMLStreamReader(inputMessage.getBody());
/* 152 */       int event = moveToFirstChildOfRootElement(streamReader);
/*     */       
/* 154 */       while (event != 8) {
/* 155 */         if (elementClass.isAnnotationPresent((Class)XmlRootElement.class)) {
/* 156 */           result.add(unmarshaller.unmarshal(streamReader));
/*     */         }
/* 158 */         else if (elementClass.isAnnotationPresent((Class)XmlType.class)) {
/* 159 */           result.add(unmarshaller.unmarshal(streamReader, elementClass).getValue());
/*     */         }
/*     */         else {
/*     */           
/* 163 */           throw new HttpMessageNotReadableException("Cannot unmarshal to [" + elementClass + "]", inputMessage);
/*     */         } 
/*     */         
/* 166 */         event = moveToNextElement(streamReader);
/*     */       } 
/* 168 */       return result;
/*     */     }
/* 170 */     catch (XMLStreamException ex) {
/* 171 */       throw new HttpMessageNotReadableException("Failed to read XML stream: " + ex
/* 172 */           .getMessage(), ex, inputMessage);
/*     */     }
/* 174 */     catch (UnmarshalException ex) {
/* 175 */       throw new HttpMessageNotReadableException("Could not unmarshal to [" + elementClass + "]: " + ex
/* 176 */           .getMessage(), ex, inputMessage);
/*     */     }
/* 178 */     catch (JAXBException ex) {
/* 179 */       throw new HttpMessageConversionException("Invalid JAXB setup: " + ex.getMessage(), ex);
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
/*     */   protected T createCollection(Class<?> collectionClass) {
/* 191 */     if (!collectionClass.isInterface()) {
/*     */       try {
/* 193 */         return (T)ReflectionUtils.accessibleConstructor(collectionClass, new Class[0]).newInstance(new Object[0]);
/*     */       }
/* 195 */       catch (Throwable ex) {
/* 196 */         throw new IllegalArgumentException("Could not instantiate collection class: " + collectionClass
/* 197 */             .getName(), ex);
/*     */       } 
/*     */     }
/* 200 */     if (List.class == collectionClass) {
/* 201 */       return (T)new ArrayList();
/*     */     }
/* 203 */     if (SortedSet.class == collectionClass) {
/* 204 */       return (T)new TreeSet();
/*     */     }
/*     */     
/* 207 */     return (T)new LinkedHashSet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int moveToFirstChildOfRootElement(XMLStreamReader streamReader) throws XMLStreamException {
/* 213 */     int event = streamReader.next();
/* 214 */     while (event != 1) {
/* 215 */       event = streamReader.next();
/*     */     }
/*     */ 
/*     */     
/* 219 */     event = streamReader.next();
/* 220 */     while (event != 1 && event != 8) {
/* 221 */       event = streamReader.next();
/*     */     }
/* 223 */     return event;
/*     */   }
/*     */   
/*     */   private int moveToNextElement(XMLStreamReader streamReader) throws XMLStreamException {
/* 227 */     int event = streamReader.getEventType();
/* 228 */     while (event != 1 && event != 8) {
/* 229 */       event = streamReader.next();
/*     */     }
/* 231 */     return event;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(T t, @Nullable Type type, @Nullable MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
/* 238 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeToResult(T t, HttpHeaders headers, Result result) throws Exception {
/* 243 */     throw new UnsupportedOperationException();
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
/*     */   protected XMLInputFactory createXmlInputFactory() {
/* 255 */     return StaxUtils.createDefensiveInputFactory();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/xml/Jaxb2CollectionHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */