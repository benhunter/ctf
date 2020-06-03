/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InputTag
/*     */   extends AbstractHtmlInputElementTag
/*     */ {
/*     */   public static final String SIZE_ATTRIBUTE = "size";
/*     */   public static final String MAXLENGTH_ATTRIBUTE = "maxlength";
/*     */   public static final String ALT_ATTRIBUTE = "alt";
/*     */   public static final String ONSELECT_ATTRIBUTE = "onselect";
/*     */   public static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";
/*     */   @Nullable
/*     */   private String size;
/*     */   @Nullable
/*     */   private String maxlength;
/*     */   @Nullable
/*     */   private String alt;
/*     */   @Nullable
/*     */   private String onselect;
/*     */   @Nullable
/*     */   private String autocomplete;
/*     */   
/*     */   public void setSize(String size) {
/* 271 */     this.size = size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getSize() {
/* 279 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxlength(String maxlength) {
/* 287 */     this.maxlength = maxlength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getMaxlength() {
/* 295 */     return this.maxlength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlt(String alt) {
/* 303 */     this.alt = alt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getAlt() {
/* 311 */     return this.alt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnselect(String onselect) {
/* 319 */     this.onselect = onselect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnselect() {
/* 327 */     return this.onselect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutocomplete(String autocomplete) {
/* 335 */     this.autocomplete = autocomplete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getAutocomplete() {
/* 343 */     return this.autocomplete;
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
/* 354 */     tagWriter.startTag("input");
/*     */     
/* 356 */     writeDefaultAttributes(tagWriter);
/* 357 */     Map<String, Object> attributes = getDynamicAttributes();
/* 358 */     if (attributes == null || !attributes.containsKey("type")) {
/* 359 */       tagWriter.writeAttribute("type", getType());
/*     */     }
/* 361 */     writeValue(tagWriter);
/*     */ 
/*     */     
/* 364 */     writeOptionalAttribute(tagWriter, "size", getSize());
/* 365 */     writeOptionalAttribute(tagWriter, "maxlength", getMaxlength());
/* 366 */     writeOptionalAttribute(tagWriter, "alt", getAlt());
/* 367 */     writeOptionalAttribute(tagWriter, "onselect", getOnselect());
/* 368 */     writeOptionalAttribute(tagWriter, "autocomplete", getAutocomplete());
/*     */     
/* 370 */     tagWriter.endTag();
/* 371 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeValue(TagWriter tagWriter) throws JspException {
/* 380 */     String value = getDisplayString(getBoundValue(), getPropertyEditor());
/* 381 */     String type = null;
/* 382 */     Map<String, Object> attributes = getDynamicAttributes();
/* 383 */     if (attributes != null) {
/* 384 */       type = (String)attributes.get("type");
/*     */     }
/* 386 */     if (type == null) {
/* 387 */       type = getType();
/*     */     }
/* 389 */     tagWriter.writeAttribute("value", processFieldValue(getName(), value, type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidDynamicAttribute(String localName, Object value) {
/* 398 */     return (!"type".equals(localName) || (!"checkbox".equals(value) && !"radio".equals(value)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getType() {
/* 407 */     return "text";
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/InputTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */