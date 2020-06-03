/*     */ package org.springframework.web.servlet.view.xml;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.oxm.Marshaller;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.servlet.view.AbstractView;
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
/*     */ public class MarshallingView
/*     */   extends AbstractView
/*     */ {
/*     */   public static final String DEFAULT_CONTENT_TYPE = "application/xml";
/*     */   @Nullable
/*     */   private Marshaller marshaller;
/*     */   @Nullable
/*     */   private String modelKey;
/*     */   
/*     */   public MarshallingView() {
/*  66 */     setContentType("application/xml");
/*  67 */     setExposePathVariables(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MarshallingView(Marshaller marshaller) {
/*  74 */     this();
/*  75 */     Assert.notNull(marshaller, "Marshaller must not be null");
/*  76 */     this.marshaller = marshaller;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMarshaller(Marshaller marshaller) {
/*  84 */     this.marshaller = marshaller;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModelKey(String modelKey) {
/*  93 */     this.modelKey = modelKey;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initApplicationContext() {
/*  98 */     Assert.notNull(this.marshaller, "Property 'marshaller' is required");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
/* 106 */     Object toBeMarshalled = locateToBeMarshalled(model);
/* 107 */     if (toBeMarshalled == null) {
/* 108 */       throw new IllegalStateException("Unable to locate object to be marshalled in model: " + model);
/*     */     }
/*     */     
/* 111 */     Assert.state((this.marshaller != null), "No Marshaller set");
/* 112 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/* 113 */     this.marshaller.marshal(toBeMarshalled, new StreamResult(baos));
/*     */     
/* 115 */     setResponseContentType(request, response);
/* 116 */     response.setContentLength(baos.size());
/* 117 */     baos.writeTo((OutputStream)response.getOutputStream());
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
/*     */   @Nullable
/*     */   protected Object locateToBeMarshalled(Map<String, Object> model) throws IllegalStateException {
/* 133 */     if (this.modelKey != null) {
/* 134 */       Object value = model.get(this.modelKey);
/* 135 */       if (value == null) {
/* 136 */         throw new IllegalStateException("Model contains no object with key [" + this.modelKey + "]");
/*     */       }
/* 138 */       if (!isEligibleForMarshalling(this.modelKey, value)) {
/* 139 */         throw new IllegalStateException("Model object [" + value + "] retrieved via key [" + this.modelKey + "] is not supported by the Marshaller");
/*     */       }
/*     */       
/* 142 */       return value;
/*     */     } 
/* 144 */     for (Map.Entry<String, Object> entry : model.entrySet()) {
/* 145 */       Object value = entry.getValue();
/* 146 */       if (value != null && (model.size() == 1 || !(value instanceof org.springframework.validation.BindingResult)) && 
/* 147 */         isEligibleForMarshalling(entry.getKey(), value)) {
/* 148 */         return value;
/*     */       }
/*     */     } 
/* 151 */     return null;
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
/*     */   protected boolean isEligibleForMarshalling(String modelKey, Object value) {
/* 165 */     Assert.state((this.marshaller != null), "No Marshaller set");
/* 166 */     Class<?> classToCheck = value.getClass();
/* 167 */     if (value instanceof JAXBElement) {
/* 168 */       classToCheck = ((JAXBElement)value).getDeclaredType();
/*     */     }
/* 170 */     return this.marshaller.supports(classToCheck);
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/xml/MarshallingView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */