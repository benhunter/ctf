/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.beans.BeanWrapper;
/*     */ import org.springframework.beans.PropertyAccessorFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMultiCheckedElementTag
/*     */   extends AbstractCheckedElementTag
/*     */ {
/*     */   private static final String SPAN_TAG = "span";
/*     */   @Nullable
/*     */   private Object items;
/*     */   @Nullable
/*     */   private String itemValue;
/*     */   @Nullable
/*     */   private String itemLabel;
/*  73 */   private String element = "span";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String delimiter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItems(Object items) {
/*  89 */     Assert.notNull(items, "'items' must not be null");
/*  90 */     this.items = items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getItems() {
/*  99 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemValue(String itemValue) {
/* 108 */     Assert.hasText(itemValue, "'itemValue' must not be empty");
/* 109 */     this.itemValue = itemValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getItemValue() {
/* 118 */     return this.itemValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemLabel(String itemLabel) {
/* 127 */     Assert.hasText(itemLabel, "'itemLabel' must not be empty");
/* 128 */     this.itemLabel = itemLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getItemLabel() {
/* 137 */     return this.itemLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelimiter(String delimiter) {
/* 146 */     this.delimiter = delimiter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDelimiter() {
/* 155 */     return this.delimiter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElement(String element) {
/* 164 */     Assert.hasText(element, "'element' cannot be null or blank");
/* 165 */     this.element = element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getElement() {
/* 173 */     return this.element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveId() throws JspException {
/* 183 */     Object id = evaluate("id", getId());
/* 184 */     if (id != null) {
/* 185 */       String idString = id.toString();
/* 186 */       return StringUtils.hasText(idString) ? TagIdGenerator.nextId(idString, this.pageContext) : null;
/*     */     } 
/* 188 */     return autogenerateId();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int writeTagContent(TagWriter tagWriter) throws JspException {
/* 199 */     Object items = getItems();
/* 200 */     Object itemsObject = (items instanceof String) ? evaluate("items", items) : items;
/*     */     
/* 202 */     String itemValue = getItemValue();
/* 203 */     String itemLabel = getItemLabel();
/*     */     
/* 205 */     String valueProperty = (itemValue != null) ? ObjectUtils.getDisplayString(evaluate("itemValue", itemValue)) : null;
/*     */     
/* 207 */     String labelProperty = (itemLabel != null) ? ObjectUtils.getDisplayString(evaluate("itemLabel", itemLabel)) : null;
/*     */     
/* 209 */     Class<?> boundType = getBindStatus().getValueType();
/* 210 */     if (itemsObject == null && boundType != null && boundType.isEnum()) {
/* 211 */       itemsObject = boundType.getEnumConstants();
/*     */     }
/*     */     
/* 214 */     if (itemsObject == null) {
/* 215 */       throw new IllegalArgumentException("Attribute 'items' is required and must be a Collection, an Array or a Map");
/*     */     }
/*     */     
/* 218 */     if (itemsObject.getClass().isArray()) {
/* 219 */       Object[] itemsArray = (Object[])itemsObject;
/* 220 */       for (int i = 0; i < itemsArray.length; i++) {
/* 221 */         Object item = itemsArray[i];
/* 222 */         writeObjectEntry(tagWriter, valueProperty, labelProperty, item, i);
/*     */       }
/*     */     
/* 225 */     } else if (itemsObject instanceof Collection) {
/* 226 */       Collection<?> optionCollection = (Collection)itemsObject;
/* 227 */       int itemIndex = 0;
/* 228 */       for (Iterator<?> it = optionCollection.iterator(); it.hasNext(); itemIndex++) {
/* 229 */         Object item = it.next();
/* 230 */         writeObjectEntry(tagWriter, valueProperty, labelProperty, item, itemIndex);
/*     */       }
/*     */     
/* 233 */     } else if (itemsObject instanceof Map) {
/* 234 */       Map<?, ?> optionMap = (Map<?, ?>)itemsObject;
/* 235 */       int itemIndex = 0;
/* 236 */       for (Iterator<Map.Entry> it = optionMap.entrySet().iterator(); it.hasNext(); itemIndex++) {
/* 237 */         Map.Entry<?, ?> entry = it.next();
/* 238 */         writeMapEntry(tagWriter, valueProperty, labelProperty, entry, itemIndex);
/*     */       } 
/*     */     } else {
/*     */       
/* 242 */       throw new IllegalArgumentException("Attribute 'items' must be an array, a Collection or a Map");
/*     */     } 
/*     */     
/* 245 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObjectEntry(TagWriter tagWriter, @Nullable String valueProperty, @Nullable String labelProperty, Object item, int itemIndex) throws JspException {
/*     */     Object renderValue;
/* 251 */     BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
/*     */     
/* 253 */     if (valueProperty != null) {
/* 254 */       renderValue = wrapper.getPropertyValue(valueProperty);
/*     */     }
/* 256 */     else if (item instanceof Enum) {
/* 257 */       renderValue = ((Enum)item).name();
/*     */     } else {
/*     */       
/* 260 */       renderValue = item;
/*     */     } 
/* 262 */     Object renderLabel = (labelProperty != null) ? wrapper.getPropertyValue(labelProperty) : item;
/* 263 */     writeElementTag(tagWriter, item, renderValue, renderLabel, itemIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeMapEntry(TagWriter tagWriter, @Nullable String valueProperty, @Nullable String labelProperty, Map.Entry<?, ?> entry, int itemIndex) throws JspException {
/* 269 */     Object mapKey = entry.getKey();
/* 270 */     Object mapValue = entry.getValue();
/* 271 */     BeanWrapper mapKeyWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapKey);
/* 272 */     BeanWrapper mapValueWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapValue);
/*     */     
/* 274 */     Object renderValue = (valueProperty != null) ? mapKeyWrapper.getPropertyValue(valueProperty) : mapKey.toString();
/*     */     
/* 276 */     Object renderLabel = (labelProperty != null) ? mapValueWrapper.getPropertyValue(labelProperty) : mapValue.toString();
/* 277 */     writeElementTag(tagWriter, mapKey, renderValue, renderLabel, itemIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeElementTag(TagWriter tagWriter, Object item, @Nullable Object value, @Nullable Object label, int itemIndex) throws JspException {
/* 283 */     tagWriter.startTag(getElement());
/* 284 */     if (itemIndex > 0) {
/* 285 */       Object resolvedDelimiter = evaluate("delimiter", getDelimiter());
/* 286 */       if (resolvedDelimiter != null) {
/* 287 */         tagWriter.appendValue(resolvedDelimiter.toString());
/*     */       }
/*     */     } 
/* 290 */     tagWriter.startTag("input");
/* 291 */     String id = resolveId();
/* 292 */     Assert.state((id != null), "Attribute 'id' is required");
/* 293 */     writeOptionalAttribute(tagWriter, "id", id);
/* 294 */     writeOptionalAttribute(tagWriter, "name", getName());
/* 295 */     writeOptionalAttributes(tagWriter);
/* 296 */     tagWriter.writeAttribute("type", getInputType());
/* 297 */     renderFromValue(item, value, tagWriter);
/* 298 */     tagWriter.endTag();
/* 299 */     tagWriter.startTag("label");
/* 300 */     tagWriter.writeAttribute("for", id);
/* 301 */     tagWriter.appendValue(convertToDisplayString(label));
/* 302 */     tagWriter.endTag();
/* 303 */     tagWriter.endTag();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/AbstractMultiCheckedElementTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */