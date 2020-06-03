/*    */ package org.springframework.http.codec.xml;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import javax.xml.bind.JAXBContext;
/*    */ import javax.xml.bind.JAXBException;
/*    */ import javax.xml.bind.Marshaller;
/*    */ import javax.xml.bind.Unmarshaller;
/*    */ import org.springframework.util.Assert;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class JaxbContextContainer
/*    */ {
/* 36 */   private final ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap<>(64);
/*    */ 
/*    */   
/*    */   public Marshaller createMarshaller(Class<?> clazz) throws JAXBException {
/* 40 */     JAXBContext jaxbContext = getJaxbContext(clazz);
/* 41 */     return jaxbContext.createMarshaller();
/*    */   }
/*    */   
/*    */   public Unmarshaller createUnmarshaller(Class<?> clazz) throws JAXBException {
/* 45 */     JAXBContext jaxbContext = getJaxbContext(clazz);
/* 46 */     return jaxbContext.createUnmarshaller();
/*    */   }
/*    */   
/*    */   private JAXBContext getJaxbContext(Class<?> clazz) throws JAXBException {
/* 50 */     Assert.notNull(clazz, "Class must not be null");
/* 51 */     JAXBContext jaxbContext = this.jaxbContexts.get(clazz);
/* 52 */     if (jaxbContext == null) {
/* 53 */       jaxbContext = JAXBContext.newInstance(new Class[] { clazz });
/* 54 */       this.jaxbContexts.putIfAbsent(clazz, jaxbContext);
/*    */     } 
/* 56 */     return jaxbContext;
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/codec/xml/JaxbContextContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */