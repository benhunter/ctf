/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.tagext.BodyTag;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ErrorsTag
/*     */   extends AbstractHtmlElementBodyTag
/*     */   implements BodyTag
/*     */ {
/*     */   public static final String MESSAGES_ATTRIBUTE = "messages";
/*     */   public static final String SPAN_TAG = "span";
/* 206 */   private String element = "span";
/*     */   
/* 208 */   private String delimiter = "<br/>";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object oldMessages;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean errorMessagesWereExposed;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setElement(String element) {
/* 224 */     Assert.hasText(element, "'element' cannot be null or blank");
/* 225 */     this.element = element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getElement() {
/* 232 */     return this.element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelimiter(String delimiter) {
/* 240 */     this.delimiter = delimiter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDelimiter() {
/* 247 */     return this.delimiter;
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
/*     */   protected String autogenerateId() throws JspException {
/* 261 */     String path = getPropertyPath();
/* 262 */     if ("".equals(path) || "*".equals(path)) {
/* 263 */       path = (String)this.pageContext.getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, 2);
/*     */     }
/*     */     
/* 266 */     return StringUtils.deleteAny(path, "[]") + ".errors";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getName() throws JspException {
/* 277 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldRender() throws JspException {
/*     */     try {
/* 288 */       return getBindStatus().isError();
/*     */     }
/* 290 */     catch (IllegalStateException ex) {
/*     */       
/* 292 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderDefaultContent(TagWriter tagWriter) throws JspException {
/* 298 */     tagWriter.startTag(getElement());
/* 299 */     writeDefaultAttributes(tagWriter);
/* 300 */     String delimiter = ObjectUtils.getDisplayString(evaluate("delimiter", getDelimiter()));
/* 301 */     String[] errorMessages = getBindStatus().getErrorMessages();
/* 302 */     for (int i = 0; i < errorMessages.length; i++) {
/* 303 */       String errorMessage = errorMessages[i];
/* 304 */       if (i > 0) {
/* 305 */         tagWriter.appendValue(delimiter);
/*     */       }
/* 307 */       tagWriter.appendValue(getDisplayString(errorMessage));
/*     */     } 
/* 309 */     tagWriter.endTag();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void exposeAttributes() throws JspException {
/* 320 */     List<String> errorMessages = new ArrayList<>();
/* 321 */     errorMessages.addAll(Arrays.asList(getBindStatus().getErrorMessages()));
/* 322 */     this.oldMessages = this.pageContext.getAttribute("messages", 1);
/* 323 */     this.pageContext.setAttribute("messages", errorMessages, 1);
/* 324 */     this.errorMessagesWereExposed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeAttributes() {
/* 334 */     if (this.errorMessagesWereExposed)
/* 335 */       if (this.oldMessages != null) {
/* 336 */         this.pageContext.setAttribute("messages", this.oldMessages, 1);
/* 337 */         this.oldMessages = null;
/*     */       } else {
/*     */         
/* 340 */         this.pageContext.removeAttribute("messages", 1);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/ErrorsTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */