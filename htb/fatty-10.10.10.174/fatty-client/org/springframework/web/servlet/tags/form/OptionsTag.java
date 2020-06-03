/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.Tag;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.support.BindStatus;
/*     */ import org.springframework.web.util.TagUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionsTag
/*     */   extends AbstractHtmlElementTag
/*     */ {
/*     */   @Nullable
/*     */   private Object items;
/*     */   @Nullable
/*     */   private String itemValue;
/*     */   @Nullable
/*     */   private String itemLabel;
/*     */   private boolean disabled;
/*     */   
/*     */   public void setItems(Object items) {
/* 228 */     this.items = items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getItems() {
/* 238 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemValue(String itemValue) {
/* 248 */     Assert.hasText(itemValue, "'itemValue' must not be empty");
/* 249 */     this.itemValue = itemValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getItemValue() {
/* 258 */     return this.itemValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemLabel(String itemLabel) {
/* 266 */     Assert.hasText(itemLabel, "'itemLabel' must not be empty");
/* 267 */     this.itemLabel = itemLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getItemLabel() {
/* 276 */     return this.itemLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/* 283 */     this.disabled = disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isDisabled() {
/* 290 */     return this.disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int writeTagContent(TagWriter tagWriter) throws JspException {
/* 296 */     SelectTag selectTag = getSelectTag();
/* 297 */     Object items = getItems();
/* 298 */     Object itemsObject = null;
/* 299 */     if (items != null) {
/* 300 */       itemsObject = (items instanceof String) ? evaluate("items", items) : items;
/*     */     } else {
/*     */       
/* 303 */       Class<?> selectTagBoundType = selectTag.getBindStatus().getValueType();
/* 304 */       if (selectTagBoundType != null && selectTagBoundType.isEnum()) {
/* 305 */         itemsObject = selectTagBoundType.getEnumConstants();
/*     */       }
/*     */     } 
/* 308 */     if (itemsObject != null) {
/* 309 */       String selectName = selectTag.getName();
/* 310 */       String itemValue = getItemValue();
/* 311 */       String itemLabel = getItemLabel();
/*     */       
/* 313 */       String valueProperty = (itemValue != null) ? ObjectUtils.getDisplayString(evaluate("itemValue", itemValue)) : null;
/*     */       
/* 315 */       String labelProperty = (itemLabel != null) ? ObjectUtils.getDisplayString(evaluate("itemLabel", itemLabel)) : null;
/* 316 */       OptionsWriter optionWriter = new OptionsWriter(selectName, itemsObject, valueProperty, labelProperty);
/* 317 */       optionWriter.writeOptions(tagWriter);
/*     */     } 
/* 319 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveId() throws JspException {
/* 328 */     Object id = evaluate("id", getId());
/* 329 */     if (id != null) {
/* 330 */       String idString = id.toString();
/* 331 */       return StringUtils.hasText(idString) ? TagIdGenerator.nextId(idString, this.pageContext) : null;
/*     */     } 
/* 333 */     return null;
/*     */   }
/*     */   
/*     */   private SelectTag getSelectTag() {
/* 337 */     TagUtils.assertHasAncestorOfType((Tag)this, SelectTag.class, "options", "select");
/* 338 */     return (SelectTag)findAncestorWithClass((Tag)this, SelectTag.class);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BindStatus getBindStatus() {
/* 343 */     return (BindStatus)this.pageContext.getAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class OptionsWriter
/*     */     extends OptionWriter
/*     */   {
/*     */     @Nullable
/*     */     private final String selectName;
/*     */ 
/*     */ 
/*     */     
/*     */     public OptionsWriter(String selectName, @Nullable Object optionSource, @Nullable String valueProperty, String labelProperty) {
/* 358 */       super(optionSource, OptionsTag.this.getBindStatus(), valueProperty, labelProperty, OptionsTag.this.isHtmlEscape());
/* 359 */       this.selectName = selectName;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isOptionDisabled() throws JspException {
/* 364 */       return OptionsTag.this.isDisabled();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void writeCommonAttributes(TagWriter tagWriter) throws JspException {
/* 369 */       OptionsTag.this.writeOptionalAttribute(tagWriter, "id", OptionsTag.this.resolveId());
/* 370 */       OptionsTag.this.writeOptionalAttributes(tagWriter);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String processOptionValue(String value) {
/* 375 */       return OptionsTag.this.processFieldValue(this.selectName, value, "option");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/OptionsTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */