/*     */ package org.springframework.web.bind;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.validation.DataBinder;
/*     */ import org.springframework.web.multipart.MultipartFile;
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
/*     */ public class WebDataBinder
/*     */   extends DataBinder
/*     */ {
/*     */   public static final String DEFAULT_FIELD_MARKER_PREFIX = "_";
/*     */   public static final String DEFAULT_FIELD_DEFAULT_PREFIX = "!";
/*     */   @Nullable
/*  78 */   private String fieldMarkerPrefix = "_";
/*     */   
/*     */   @Nullable
/*  81 */   private String fieldDefaultPrefix = "!";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean bindEmptyMultipartFiles = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebDataBinder(@Nullable Object target) {
/*  94 */     super(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebDataBinder(@Nullable Object target, String objectName) {
/* 104 */     super(target, objectName);
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
/*     */   public void setFieldMarkerPrefix(@Nullable String fieldMarkerPrefix) {
/* 130 */     this.fieldMarkerPrefix = fieldMarkerPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getFieldMarkerPrefix() {
/* 138 */     return this.fieldMarkerPrefix;
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
/*     */   public void setFieldDefaultPrefix(@Nullable String fieldDefaultPrefix) {
/* 156 */     this.fieldDefaultPrefix = fieldDefaultPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getFieldDefaultPrefix() {
/* 164 */     return this.fieldDefaultPrefix;
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
/*     */   public void setBindEmptyMultipartFiles(boolean bindEmptyMultipartFiles) {
/* 176 */     this.bindEmptyMultipartFiles = bindEmptyMultipartFiles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBindEmptyMultipartFiles() {
/* 183 */     return this.bindEmptyMultipartFiles;
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
/*     */   protected void doBind(MutablePropertyValues mpvs) {
/* 195 */     checkFieldDefaults(mpvs);
/* 196 */     checkFieldMarkers(mpvs);
/* 197 */     super.doBind(mpvs);
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
/*     */   protected void checkFieldDefaults(MutablePropertyValues mpvs) {
/* 209 */     String fieldDefaultPrefix = getFieldDefaultPrefix();
/* 210 */     if (fieldDefaultPrefix != null) {
/* 211 */       PropertyValue[] pvArray = mpvs.getPropertyValues();
/* 212 */       for (PropertyValue pv : pvArray) {
/* 213 */         if (pv.getName().startsWith(fieldDefaultPrefix)) {
/* 214 */           String field = pv.getName().substring(fieldDefaultPrefix.length());
/* 215 */           if (getPropertyAccessor().isWritableProperty(field) && !mpvs.contains(field)) {
/* 216 */             mpvs.add(field, pv.getValue());
/*     */           }
/* 218 */           mpvs.removePropertyValue(pv);
/*     */         } 
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFieldMarkers(MutablePropertyValues mpvs) {
/* 236 */     String fieldMarkerPrefix = getFieldMarkerPrefix();
/* 237 */     if (fieldMarkerPrefix != null) {
/* 238 */       PropertyValue[] pvArray = mpvs.getPropertyValues();
/* 239 */       for (PropertyValue pv : pvArray) {
/* 240 */         if (pv.getName().startsWith(fieldMarkerPrefix)) {
/* 241 */           String field = pv.getName().substring(fieldMarkerPrefix.length());
/* 242 */           if (getPropertyAccessor().isWritableProperty(field) && !mpvs.contains(field)) {
/* 243 */             Class<?> fieldType = getPropertyAccessor().getPropertyType(field);
/* 244 */             mpvs.add(field, getEmptyValue(field, fieldType));
/*     */           } 
/* 246 */           mpvs.removePropertyValue(pv);
/*     */         } 
/*     */       } 
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
/*     */   
/*     */   @Nullable
/*     */   protected Object getEmptyValue(String field, @Nullable Class<?> fieldType) {
/* 262 */     return (fieldType != null) ? getEmptyValue(fieldType) : null;
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
/*     */   @Nullable
/*     */   public Object getEmptyValue(Class<?> fieldType) {
/*     */     try {
/* 282 */       if (boolean.class == fieldType || Boolean.class == fieldType)
/*     */       {
/* 284 */         return Boolean.FALSE;
/*     */       }
/* 286 */       if (fieldType.isArray())
/*     */       {
/* 288 */         return Array.newInstance(fieldType.getComponentType(), 0);
/*     */       }
/* 290 */       if (Collection.class.isAssignableFrom(fieldType)) {
/* 291 */         return CollectionFactory.createCollection(fieldType, 0);
/*     */       }
/* 293 */       if (Map.class.isAssignableFrom(fieldType)) {
/* 294 */         return CollectionFactory.createMap(fieldType, 0);
/*     */       }
/*     */     }
/* 297 */     catch (IllegalArgumentException ex) {
/* 298 */       if (logger.isDebugEnabled()) {
/* 299 */         logger.debug("Failed to create default value - falling back to null: " + ex.getMessage());
/*     */       }
/*     */     } 
/*     */     
/* 303 */     return null;
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
/*     */   protected void bindMultipart(Map<String, List<MultipartFile>> multipartFiles, MutablePropertyValues mpvs) {
/* 318 */     multipartFiles.forEach((key, values) -> {
/*     */           if (values.size() == 1) {
/*     */             MultipartFile value = values.get(0);
/*     */             if (isBindEmptyMultipartFiles() || !value.isEmpty())
/*     */               mpvs.add(key, value); 
/*     */           } else {
/*     */             mpvs.add(key, values);
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/WebDataBinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */