/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
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
/*     */ public abstract class AbstractHtmlInputElementTag
/*     */   extends AbstractHtmlElementTag
/*     */ {
/*     */   public static final String ONFOCUS_ATTRIBUTE = "onfocus";
/*     */   public static final String ONBLUR_ATTRIBUTE = "onblur";
/*     */   public static final String ONCHANGE_ATTRIBUTE = "onchange";
/*     */   public static final String ACCESSKEY_ATTRIBUTE = "accesskey";
/*     */   public static final String DISABLED_ATTRIBUTE = "disabled";
/*     */   public static final String READONLY_ATTRIBUTE = "readonly";
/*     */   @Nullable
/*     */   private String onfocus;
/*     */   @Nullable
/*     */   private String onblur;
/*     */   @Nullable
/*     */   private String onchange;
/*     */   @Nullable
/*     */   private String accesskey;
/*     */   private boolean disabled;
/*     */   private boolean readonly;
/*     */   
/*     */   public void setOnfocus(String onfocus) {
/*  90 */     this.onfocus = onfocus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnfocus() {
/*  98 */     return this.onfocus;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnblur(String onblur) {
/* 106 */     this.onblur = onblur;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnblur() {
/* 114 */     return this.onblur;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnchange(String onchange) {
/* 122 */     this.onchange = onchange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnchange() {
/* 130 */     return this.onchange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAccesskey(String accesskey) {
/* 138 */     this.accesskey = accesskey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getAccesskey() {
/* 146 */     return this.accesskey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/* 153 */     this.disabled = disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isDisabled() {
/* 160 */     return this.disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReadonly(boolean readonly) {
/* 167 */     this.readonly = readonly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isReadonly() {
/* 174 */     return this.readonly;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeOptionalAttributes(TagWriter tagWriter) throws JspException {
/* 183 */     super.writeOptionalAttributes(tagWriter);
/*     */     
/* 185 */     writeOptionalAttribute(tagWriter, "onfocus", getOnfocus());
/* 186 */     writeOptionalAttribute(tagWriter, "onblur", getOnblur());
/* 187 */     writeOptionalAttribute(tagWriter, "onchange", getOnchange());
/* 188 */     writeOptionalAttribute(tagWriter, "accesskey", getAccesskey());
/* 189 */     if (isDisabled()) {
/* 190 */       tagWriter.writeAttribute("disabled", "disabled");
/*     */     }
/* 192 */     if (isReadonly())
/* 193 */       writeOptionalAttribute(tagWriter, "readonly", "readonly"); 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/AbstractHtmlInputElementTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */