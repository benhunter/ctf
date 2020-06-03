/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.beans.PropertyEditor;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.web.servlet.support.BindStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class OptionWriter
/*     */ {
/*     */   private final Object optionSource;
/*     */   private final BindStatus bindStatus;
/*     */   @Nullable
/*     */   private final String valueProperty;
/*     */   @Nullable
/*     */   private final String labelProperty;
/*     */   private final boolean htmlEscape;
/*     */   
/*     */   public OptionWriter(Object optionSource, BindStatus bindStatus, @Nullable String valueProperty, @Nullable String labelProperty, boolean htmlEscape) {
/* 117 */     Assert.notNull(optionSource, "'optionSource' must not be null");
/* 118 */     Assert.notNull(bindStatus, "'bindStatus' must not be null");
/* 119 */     this.optionSource = optionSource;
/* 120 */     this.bindStatus = bindStatus;
/* 121 */     this.valueProperty = valueProperty;
/* 122 */     this.labelProperty = labelProperty;
/* 123 */     this.htmlEscape = htmlEscape;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeOptions(TagWriter tagWriter) throws JspException {
/* 132 */     if (this.optionSource.getClass().isArray()) {
/* 133 */       renderFromArray(tagWriter);
/*     */     }
/* 135 */     else if (this.optionSource instanceof Collection) {
/* 136 */       renderFromCollection(tagWriter);
/*     */     }
/* 138 */     else if (this.optionSource instanceof Map) {
/* 139 */       renderFromMap(tagWriter);
/*     */     }
/* 141 */     else if (this.optionSource instanceof Class && ((Class)this.optionSource).isEnum()) {
/* 142 */       renderFromEnum(tagWriter);
/*     */     } else {
/*     */       
/* 145 */       throw new JspException("Type [" + this.optionSource
/* 146 */           .getClass().getName() + "] is not valid for option items");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderFromArray(TagWriter tagWriter) throws JspException {
/* 155 */     doRenderFromCollection(CollectionUtils.arrayToList(this.optionSource), tagWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderFromMap(TagWriter tagWriter) throws JspException {
/* 164 */     Map<?, ?> optionMap = (Map<?, ?>)this.optionSource;
/* 165 */     for (Map.Entry<?, ?> entry : optionMap.entrySet()) {
/* 166 */       Object mapKey = entry.getKey();
/* 167 */       Object mapValue = entry.getValue();
/*     */       
/* 169 */       Object renderValue = (this.valueProperty != null) ? PropertyAccessorFactory.forBeanPropertyAccess(mapKey).getPropertyValue(this.valueProperty) : mapKey;
/*     */ 
/*     */       
/* 172 */       Object renderLabel = (this.labelProperty != null) ? PropertyAccessorFactory.forBeanPropertyAccess(mapValue).getPropertyValue(this.labelProperty) : mapValue;
/*     */       
/* 174 */       renderOption(tagWriter, mapKey, renderValue, renderLabel);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderFromCollection(TagWriter tagWriter) throws JspException {
/* 183 */     doRenderFromCollection((Collection)this.optionSource, tagWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderFromEnum(TagWriter tagWriter) throws JspException {
/* 191 */     doRenderFromCollection(CollectionUtils.arrayToList(((Class)this.optionSource).getEnumConstants()), tagWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doRenderFromCollection(Collection<?> optionCollection, TagWriter tagWriter) throws JspException {
/* 201 */     for (Object item : optionCollection) {
/* 202 */       Object value; BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
/*     */       
/* 204 */       if (this.valueProperty != null) {
/* 205 */         value = wrapper.getPropertyValue(this.valueProperty);
/*     */       }
/* 207 */       else if (item instanceof Enum) {
/* 208 */         value = ((Enum)item).name();
/*     */       } else {
/*     */         
/* 211 */         value = item;
/*     */       } 
/* 213 */       Object label = (this.labelProperty != null) ? wrapper.getPropertyValue(this.labelProperty) : item;
/* 214 */       renderOption(tagWriter, item, value, label);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void renderOption(TagWriter tagWriter, Object item, @Nullable Object value, @Nullable Object label) throws JspException {
/* 225 */     tagWriter.startTag("option");
/* 226 */     writeCommonAttributes(tagWriter);
/*     */     
/* 228 */     String valueDisplayString = getDisplayString(value);
/* 229 */     String labelDisplayString = getDisplayString(label);
/*     */     
/* 231 */     valueDisplayString = processOptionValue(valueDisplayString);
/*     */ 
/*     */     
/* 234 */     tagWriter.writeAttribute("value", valueDisplayString);
/*     */     
/* 236 */     if (isOptionSelected(value) || (value != item && isOptionSelected(item))) {
/* 237 */       tagWriter.writeAttribute("selected", "selected");
/*     */     }
/* 239 */     if (isOptionDisabled()) {
/* 240 */       tagWriter.writeAttribute("disabled", "disabled");
/*     */     }
/* 242 */     tagWriter.appendValue(labelDisplayString);
/* 243 */     tagWriter.endTag();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getDisplayString(@Nullable Object value) {
/* 251 */     PropertyEditor editor = (value != null) ? this.bindStatus.findEditor(value.getClass()) : null;
/* 252 */     return ValueFormatter.getDisplayString(value, editor, this.htmlEscape);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String processOptionValue(String resolvedValue) {
/* 260 */     return resolvedValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isOptionSelected(@Nullable Object resolvedValue) {
/* 268 */     return SelectedValueComparator.isSelected(this.bindStatus, resolvedValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isOptionDisabled() throws JspException {
/* 275 */     return false;
/*     */   }
/*     */   
/*     */   protected void writeCommonAttributes(TagWriter tagWriter) throws JspException {}
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/OptionWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */