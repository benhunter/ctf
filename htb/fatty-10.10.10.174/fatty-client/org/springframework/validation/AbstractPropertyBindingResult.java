/*     */ package org.springframework.validation;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.ConfigurablePropertyAccessor;
/*     */ import org.springframework.beans.PropertyAccessorUtils;
/*     */ import org.springframework.beans.PropertyEditorRegistry;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.support.ConvertingPropertyEditorAdapter;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPropertyBindingResult
/*     */   extends AbstractBindingResult
/*     */ {
/*     */   @Nullable
/*     */   private transient ConversionService conversionService;
/*     */   
/*     */   protected AbstractPropertyBindingResult(String objectName) {
/*  56 */     super(objectName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initConversion(ConversionService conversionService) {
/*  61 */     Assert.notNull(conversionService, "ConversionService must not be null");
/*  62 */     this.conversionService = conversionService;
/*  63 */     if (getTarget() != null) {
/*  64 */       getPropertyAccessor().setConversionService(conversionService);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertyEditorRegistry getPropertyEditorRegistry() {
/*  74 */     return (getTarget() != null) ? (PropertyEditorRegistry)getPropertyAccessor() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String canonicalFieldName(String field) {
/*  83 */     return PropertyAccessorUtils.canonicalPropertyName(field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?> getFieldType(@Nullable String field) {
/*  93 */     return (getTarget() != null) ? getPropertyAccessor().getPropertyType(fixedField(field)) : super
/*  94 */       .getFieldType(field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getActualFieldValue(String field) {
/* 104 */     return getPropertyAccessor().getPropertyValue(field);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object formatFieldValue(String field, @Nullable Object value) {
/* 113 */     String fixedField = fixedField(field);
/*     */     
/* 115 */     PropertyEditor customEditor = getCustomEditor(fixedField);
/* 116 */     if (customEditor != null) {
/* 117 */       customEditor.setValue(value);
/* 118 */       String textValue = customEditor.getAsText();
/*     */ 
/*     */       
/* 121 */       if (textValue != null) {
/* 122 */         return textValue;
/*     */       }
/*     */     } 
/* 125 */     if (this.conversionService != null) {
/*     */       
/* 127 */       TypeDescriptor fieldDesc = getPropertyAccessor().getPropertyTypeDescriptor(fixedField);
/* 128 */       TypeDescriptor strDesc = TypeDescriptor.valueOf(String.class);
/* 129 */       if (fieldDesc != null && this.conversionService.canConvert(fieldDesc, strDesc)) {
/* 130 */         return this.conversionService.convert(value, fieldDesc, strDesc);
/*     */       }
/*     */     } 
/* 133 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected PropertyEditor getCustomEditor(String fixedField) {
/* 143 */     Class<?> targetType = getPropertyAccessor().getPropertyType(fixedField);
/* 144 */     PropertyEditor editor = getPropertyAccessor().findCustomEditor(targetType, fixedField);
/* 145 */     if (editor == null) {
/* 146 */       editor = BeanUtils.findEditorByConvention(targetType);
/*     */     }
/* 148 */     return editor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public PropertyEditor findEditor(@Nullable String field, @Nullable Class<?> valueType) {
/*     */     ConvertingPropertyEditorAdapter convertingPropertyEditorAdapter;
/* 158 */     Class<?> valueTypeForLookup = valueType;
/* 159 */     if (valueTypeForLookup == null) {
/* 160 */       valueTypeForLookup = getFieldType(field);
/*     */     }
/* 162 */     PropertyEditor editor = super.findEditor(field, valueTypeForLookup);
/* 163 */     if (editor == null && this.conversionService != null) {
/* 164 */       TypeDescriptor td = null;
/* 165 */       if (field != null && getTarget() != null) {
/* 166 */         TypeDescriptor ptd = getPropertyAccessor().getPropertyTypeDescriptor(fixedField(field));
/* 167 */         if (ptd != null && (valueType == null || valueType.isAssignableFrom(ptd.getType()))) {
/* 168 */           td = ptd;
/*     */         }
/*     */       } 
/* 171 */       if (td == null) {
/* 172 */         td = TypeDescriptor.valueOf(valueTypeForLookup);
/*     */       }
/* 174 */       if (this.conversionService.canConvert(TypeDescriptor.valueOf(String.class), td)) {
/* 175 */         convertingPropertyEditorAdapter = new ConvertingPropertyEditorAdapter(this.conversionService, td);
/*     */       }
/*     */     } 
/* 178 */     return (PropertyEditor)convertingPropertyEditorAdapter;
/*     */   }
/*     */   
/*     */   public abstract ConfigurablePropertyAccessor getPropertyAccessor();
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/validation/AbstractPropertyBindingResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */