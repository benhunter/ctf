/*    */ package org.springframework.http.converter.support;
/*    */ 
/*    */ import org.springframework.http.converter.FormHttpMessageConverter;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.http.converter.json.GsonHttpMessageConverter;
/*    */ import org.springframework.http.converter.json.JsonbHttpMessageConverter;
/*    */ import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
/*    */ import org.springframework.http.converter.smile.MappingJackson2SmileHttpMessageConverter;
/*    */ import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
/*    */ import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
/*    */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AllEncompassingFormHttpMessageConverter
/*    */   extends FormHttpMessageConverter
/*    */ {
/*    */   private static final boolean jaxb2Present;
/*    */   private static final boolean jackson2Present;
/*    */   private static final boolean jackson2XmlPresent;
/*    */   private static final boolean jackson2SmilePresent;
/*    */   private static final boolean gsonPresent;
/*    */   private static final boolean jsonbPresent;
/*    */   
/*    */   static {
/* 52 */     ClassLoader classLoader = AllEncompassingFormHttpMessageConverter.class.getClassLoader();
/* 53 */     jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", classLoader);
/*    */     
/* 55 */     jackson2Present = (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader));
/* 56 */     jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);
/* 57 */     jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
/* 58 */     gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
/* 59 */     jsonbPresent = ClassUtils.isPresent("javax.json.bind.Jsonb", classLoader);
/*    */   }
/*    */ 
/*    */   
/*    */   public AllEncompassingFormHttpMessageConverter() {
/*    */     try {
/* 65 */       addPartConverter((HttpMessageConverter)new SourceHttpMessageConverter());
/*    */     }
/* 67 */     catch (Error error) {}
/*    */ 
/*    */ 
/*    */     
/* 71 */     if (jaxb2Present && !jackson2XmlPresent) {
/* 72 */       addPartConverter((HttpMessageConverter)new Jaxb2RootElementHttpMessageConverter());
/*    */     }
/*    */     
/* 75 */     if (jackson2Present) {
/* 76 */       addPartConverter((HttpMessageConverter)new MappingJackson2HttpMessageConverter());
/*    */     }
/* 78 */     else if (gsonPresent) {
/* 79 */       addPartConverter((HttpMessageConverter)new GsonHttpMessageConverter());
/*    */     }
/* 81 */     else if (jsonbPresent) {
/* 82 */       addPartConverter((HttpMessageConverter)new JsonbHttpMessageConverter());
/*    */     } 
/*    */     
/* 85 */     if (jackson2XmlPresent) {
/* 86 */       addPartConverter((HttpMessageConverter)new MappingJackson2XmlHttpMessageConverter());
/*    */     }
/*    */     
/* 89 */     if (jackson2SmilePresent)
/* 90 */       addPartConverter((HttpMessageConverter)new MappingJackson2SmileHttpMessageConverter()); 
/*    */   }
/*    */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/http/converter/support/AllEncompassingFormHttpMessageConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */