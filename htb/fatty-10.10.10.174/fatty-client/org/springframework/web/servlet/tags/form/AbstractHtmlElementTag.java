/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.DynamicAttributes;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.CollectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHtmlElementTag
/*     */   extends AbstractDataBoundFormElementTag
/*     */   implements DynamicAttributes
/*     */ {
/*     */   public static final String CLASS_ATTRIBUTE = "class";
/*     */   public static final String STYLE_ATTRIBUTE = "style";
/*     */   public static final String LANG_ATTRIBUTE = "lang";
/*     */   public static final String TITLE_ATTRIBUTE = "title";
/*     */   public static final String DIR_ATTRIBUTE = "dir";
/*     */   public static final String TABINDEX_ATTRIBUTE = "tabindex";
/*     */   public static final String ONCLICK_ATTRIBUTE = "onclick";
/*     */   public static final String ONDBLCLICK_ATTRIBUTE = "ondblclick";
/*     */   public static final String ONMOUSEDOWN_ATTRIBUTE = "onmousedown";
/*     */   public static final String ONMOUSEUP_ATTRIBUTE = "onmouseup";
/*     */   public static final String ONMOUSEOVER_ATTRIBUTE = "onmouseover";
/*     */   public static final String ONMOUSEMOVE_ATTRIBUTE = "onmousemove";
/*     */   public static final String ONMOUSEOUT_ATTRIBUTE = "onmouseout";
/*     */   public static final String ONKEYPRESS_ATTRIBUTE = "onkeypress";
/*     */   public static final String ONKEYUP_ATTRIBUTE = "onkeyup";
/*     */   public static final String ONKEYDOWN_ATTRIBUTE = "onkeydown";
/*     */   @Nullable
/*     */   private String cssClass;
/*     */   @Nullable
/*     */   private String cssErrorClass;
/*     */   @Nullable
/*     */   private String cssStyle;
/*     */   @Nullable
/*     */   private String lang;
/*     */   @Nullable
/*     */   private String title;
/*     */   @Nullable
/*     */   private String dir;
/*     */   @Nullable
/*     */   private String tabindex;
/*     */   @Nullable
/*     */   private String onclick;
/*     */   @Nullable
/*     */   private String ondblclick;
/*     */   @Nullable
/*     */   private String onmousedown;
/*     */   @Nullable
/*     */   private String onmouseup;
/*     */   @Nullable
/*     */   private String onmouseover;
/*     */   @Nullable
/*     */   private String onmousemove;
/*     */   @Nullable
/*     */   private String onmouseout;
/*     */   @Nullable
/*     */   private String onkeypress;
/*     */   @Nullable
/*     */   private String onkeyup;
/*     */   @Nullable
/*     */   private String onkeydown;
/*     */   @Nullable
/*     */   private Map<String, Object> dynamicAttributes;
/*     */   
/*     */   public void setCssClass(String cssClass) {
/* 140 */     this.cssClass = cssClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getCssClass() {
/* 149 */     return this.cssClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCssErrorClass(String cssErrorClass) {
/* 157 */     this.cssErrorClass = cssErrorClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getCssErrorClass() {
/* 166 */     return this.cssErrorClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCssStyle(String cssStyle) {
/* 174 */     this.cssStyle = cssStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getCssStyle() {
/* 183 */     return this.cssStyle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLang(String lang) {
/* 191 */     this.lang = lang;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getLang() {
/* 200 */     return this.lang;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTitle(String title) {
/* 208 */     this.title = title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getTitle() {
/* 217 */     return this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDir(String dir) {
/* 225 */     this.dir = dir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getDir() {
/* 234 */     return this.dir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTabindex(String tabindex) {
/* 242 */     this.tabindex = tabindex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getTabindex() {
/* 251 */     return this.tabindex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnclick(String onclick) {
/* 259 */     this.onclick = onclick;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnclick() {
/* 268 */     return this.onclick;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOndblclick(String ondblclick) {
/* 276 */     this.ondblclick = ondblclick;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOndblclick() {
/* 285 */     return this.ondblclick;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnmousedown(String onmousedown) {
/* 293 */     this.onmousedown = onmousedown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnmousedown() {
/* 302 */     return this.onmousedown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnmouseup(String onmouseup) {
/* 310 */     this.onmouseup = onmouseup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnmouseup() {
/* 319 */     return this.onmouseup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnmouseover(String onmouseover) {
/* 327 */     this.onmouseover = onmouseover;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnmouseover() {
/* 336 */     return this.onmouseover;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnmousemove(String onmousemove) {
/* 344 */     this.onmousemove = onmousemove;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnmousemove() {
/* 353 */     return this.onmousemove;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnmouseout(String onmouseout) {
/* 361 */     this.onmouseout = onmouseout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnmouseout() {
/* 369 */     return this.onmouseout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnkeypress(String onkeypress) {
/* 377 */     this.onkeypress = onkeypress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnkeypress() {
/* 386 */     return this.onkeypress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnkeyup(String onkeyup) {
/* 394 */     this.onkeyup = onkeyup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnkeyup() {
/* 403 */     return this.onkeyup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnkeydown(String onkeydown) {
/* 411 */     this.onkeydown = onkeydown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnkeydown() {
/* 420 */     return this.onkeydown;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Map<String, Object> getDynamicAttributes() {
/* 428 */     return this.dynamicAttributes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
/* 436 */     if (this.dynamicAttributes == null) {
/* 437 */       this.dynamicAttributes = new HashMap<>();
/*     */     }
/* 439 */     if (!isValidDynamicAttribute(localName, value)) {
/* 440 */       throw new IllegalArgumentException("Attribute " + localName + "=\"" + value + "\" is not allowed");
/*     */     }
/*     */     
/* 443 */     this.dynamicAttributes.put(localName, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isValidDynamicAttribute(String localName, Object value) {
/* 450 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeDefaultAttributes(TagWriter tagWriter) throws JspException {
/* 459 */     super.writeDefaultAttributes(tagWriter);
/* 460 */     writeOptionalAttributes(tagWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeOptionalAttributes(TagWriter tagWriter) throws JspException {
/* 469 */     tagWriter.writeOptionalAttributeValue("class", resolveCssClass());
/* 470 */     tagWriter.writeOptionalAttributeValue("style", 
/* 471 */         ObjectUtils.getDisplayString(evaluate("cssStyle", getCssStyle())));
/* 472 */     writeOptionalAttribute(tagWriter, "lang", getLang());
/* 473 */     writeOptionalAttribute(tagWriter, "title", getTitle());
/* 474 */     writeOptionalAttribute(tagWriter, "dir", getDir());
/* 475 */     writeOptionalAttribute(tagWriter, "tabindex", getTabindex());
/* 476 */     writeOptionalAttribute(tagWriter, "onclick", getOnclick());
/* 477 */     writeOptionalAttribute(tagWriter, "ondblclick", getOndblclick());
/* 478 */     writeOptionalAttribute(tagWriter, "onmousedown", getOnmousedown());
/* 479 */     writeOptionalAttribute(tagWriter, "onmouseup", getOnmouseup());
/* 480 */     writeOptionalAttribute(tagWriter, "onmouseover", getOnmouseover());
/* 481 */     writeOptionalAttribute(tagWriter, "onmousemove", getOnmousemove());
/* 482 */     writeOptionalAttribute(tagWriter, "onmouseout", getOnmouseout());
/* 483 */     writeOptionalAttribute(tagWriter, "onkeypress", getOnkeypress());
/* 484 */     writeOptionalAttribute(tagWriter, "onkeyup", getOnkeyup());
/* 485 */     writeOptionalAttribute(tagWriter, "onkeydown", getOnkeydown());
/*     */     
/* 487 */     if (!CollectionUtils.isEmpty(this.dynamicAttributes)) {
/* 488 */       for (String attr : this.dynamicAttributes.keySet()) {
/* 489 */         tagWriter.writeOptionalAttributeValue(attr, getDisplayString(this.dynamicAttributes.get(attr)));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveCssClass() throws JspException {
/* 499 */     if (getBindStatus().isError() && StringUtils.hasText(getCssErrorClass())) {
/* 500 */       return ObjectUtils.getDisplayString(evaluate("cssErrorClass", getCssErrorClass()));
/*     */     }
/*     */     
/* 503 */     return ObjectUtils.getDisplayString(evaluate("cssClass", getCssClass()));
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/AbstractHtmlElementTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */