/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SelectTag
/*     */   extends AbstractHtmlInputElementTag
/*     */ {
/*     */   public static final String LIST_VALUE_PAGE_ATTRIBUTE = "org.springframework.web.servlet.tags.form.SelectTag.listValue";
/* 255 */   private static final Object EMPTY = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object items;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String itemValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String itemLabel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object multiple;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TagWriter tagWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItems(@Nullable Object items) {
/* 310 */     this.items = (items != null) ? items : EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getItems() {
/* 319 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemValue(String itemValue) {
/* 330 */     this.itemValue = itemValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getItemValue() {
/* 339 */     return this.itemValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setItemLabel(String itemLabel) {
/* 348 */     this.itemLabel = itemLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getItemLabel() {
/* 357 */     return this.itemLabel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSize(String size) {
/* 365 */     this.size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getSize() {
/* 373 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMultiple(Object multiple) {
/* 381 */     this.multiple = multiple;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object getMultiple() {
/* 390 */     return this.multiple;
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
/*     */   protected int writeTagContent(TagWriter tagWriter) throws JspException {
/* 403 */     tagWriter.startTag("select");
/* 404 */     writeDefaultAttributes(tagWriter);
/* 405 */     if (isMultiple()) {
/* 406 */       tagWriter.writeAttribute("multiple", "multiple");
/*     */     }
/* 408 */     tagWriter.writeOptionalAttributeValue("size", getDisplayString(evaluate("size", getSize())));
/*     */     
/* 410 */     Object items = getItems();
/* 411 */     if (items != null) {
/*     */       
/* 413 */       if (items != EMPTY) {
/* 414 */         Object itemsObject = evaluate("items", items);
/* 415 */         if (itemsObject != null) {
/* 416 */           final String selectName = getName();
/*     */           
/* 418 */           String valueProperty = (getItemValue() != null) ? ObjectUtils.getDisplayString(evaluate("itemValue", getItemValue())) : null;
/*     */           
/* 420 */           String labelProperty = (getItemLabel() != null) ? ObjectUtils.getDisplayString(evaluate("itemLabel", getItemLabel())) : null;
/*     */           
/* 422 */           OptionWriter optionWriter = new OptionWriter(itemsObject, getBindStatus(), valueProperty, labelProperty, isHtmlEscape())
/*     */             {
/*     */               protected String processOptionValue(String resolvedValue) {
/* 425 */                 return SelectTag.this.processFieldValue(selectName, resolvedValue, "option");
/*     */               }
/*     */             };
/* 428 */           optionWriter.writeOptions(tagWriter);
/*     */         } 
/*     */       } 
/* 431 */       tagWriter.endTag(true);
/* 432 */       writeHiddenTagIfNecessary(tagWriter);
/* 433 */       return 0;
/*     */     } 
/*     */ 
/*     */     
/* 437 */     tagWriter.forceBlock();
/* 438 */     this.tagWriter = tagWriter;
/* 439 */     this.pageContext.setAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue", getBindStatus());
/* 440 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeHiddenTagIfNecessary(TagWriter tagWriter) throws JspException {
/* 450 */     if (isMultiple()) {
/* 451 */       tagWriter.startTag("input");
/* 452 */       tagWriter.writeAttribute("type", "hidden");
/* 453 */       String name = "_" + getName();
/* 454 */       tagWriter.writeAttribute("name", name);
/* 455 */       tagWriter.writeAttribute("value", processFieldValue(name, "1", "hidden"));
/* 456 */       tagWriter.endTag();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMultiple() throws JspException {
/* 461 */     Object multiple = getMultiple();
/* 462 */     if (multiple != null) {
/* 463 */       String stringValue = multiple.toString();
/* 464 */       return ("multiple".equalsIgnoreCase(stringValue) || Boolean.parseBoolean(stringValue));
/*     */     } 
/* 466 */     return forceMultiple();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean forceMultiple() throws JspException {
/* 474 */     BindStatus bindStatus = getBindStatus();
/* 475 */     Class<?> valueType = bindStatus.getValueType();
/* 476 */     if (valueType != null && typeRequiresMultiple(valueType)) {
/* 477 */       return true;
/*     */     }
/* 479 */     if (bindStatus.getEditor() != null) {
/* 480 */       Object editorValue = bindStatus.getEditor().getValue();
/* 481 */       if (editorValue != null && typeRequiresMultiple(editorValue.getClass())) {
/* 482 */         return true;
/*     */       }
/*     */     } 
/* 485 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean typeRequiresMultiple(Class<?> type) {
/* 493 */     return (type.isArray() || Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doEndTag() throws JspException {
/* 502 */     if (this.tagWriter != null) {
/* 503 */       this.tagWriter.endTag();
/* 504 */       writeHiddenTagIfNecessary(this.tagWriter);
/*     */     } 
/* 506 */     return 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doFinally() {
/* 515 */     super.doFinally();
/* 516 */     this.tagWriter = null;
/* 517 */     this.pageContext.removeAttribute("org.springframework.web.servlet.tags.form.SelectTag.listValue");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/SelectTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */