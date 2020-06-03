/*     */ package org.springframework.web.servlet.tags;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import javax.servlet.jsp.JspTagException;
/*     */ import org.springframework.context.MessageSource;
/*     */ import org.springframework.context.MessageSourceResolvable;
/*     */ import org.springframework.context.NoSuchMessageException;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.util.JavaScriptUtils;
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
/*     */ public class MessageTag
/*     */   extends HtmlEscapingAwareTag
/*     */   implements ArgumentAware
/*     */ {
/*     */   public static final String DEFAULT_ARGUMENT_SEPARATOR = ",";
/*     */   @Nullable
/*     */   private MessageSourceResolvable message;
/*     */   @Nullable
/*     */   private String code;
/*     */   @Nullable
/*     */   private Object arguments;
/* 164 */   private String argumentSeparator = ",";
/*     */   
/* 166 */   private List<Object> nestedArguments = Collections.emptyList();
/*     */   
/*     */   @Nullable
/*     */   private String text;
/*     */   
/*     */   @Nullable
/*     */   private String var;
/*     */   
/* 174 */   private String scope = "page";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean javaScriptEscape = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(MessageSourceResolvable message) {
/* 185 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCode(String code) {
/* 192 */     this.code = code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArguments(Object arguments) {
/* 201 */     this.arguments = arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArgumentSeparator(String argumentSeparator) {
/* 210 */     this.argumentSeparator = argumentSeparator;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addArgument(@Nullable Object argument) throws JspTagException {
/* 215 */     this.nestedArguments.add(argument);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setText(String text) {
/* 222 */     this.text = text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVar(String var) {
/* 232 */     this.var = var;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setScope(String scope) {
/* 243 */     this.scope = scope;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJavaScriptEscape(boolean javaScriptEscape) throws JspException {
/* 251 */     this.javaScriptEscape = javaScriptEscape;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int doStartTagInternal() throws JspException, IOException {
/* 257 */     this.nestedArguments = new LinkedList();
/* 258 */     return 1;
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
/*     */   public int doEndTag() throws JspException {
/*     */     try {
/* 273 */       String msg = resolveMessage();
/*     */ 
/*     */       
/* 276 */       msg = htmlEscape(msg);
/* 277 */       msg = this.javaScriptEscape ? JavaScriptUtils.javaScriptEscape(msg) : msg;
/*     */ 
/*     */       
/* 280 */       if (this.var != null) {
/* 281 */         this.pageContext.setAttribute(this.var, msg, TagUtils.getScope(this.scope));
/*     */       } else {
/*     */         
/* 284 */         writeMessage(msg);
/*     */       } 
/*     */       
/* 287 */       return 6;
/*     */     }
/* 289 */     catch (IOException ex) {
/* 290 */       throw new JspTagException(ex.getMessage(), ex);
/*     */     }
/* 292 */     catch (NoSuchMessageException ex) {
/* 293 */       throw new JspTagException(getNoSuchMessageExceptionDescription(ex));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void release() {
/* 299 */     super.release();
/* 300 */     this.arguments = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveMessage() throws JspException, NoSuchMessageException {
/* 309 */     MessageSource messageSource = getMessageSource();
/*     */ 
/*     */     
/* 312 */     if (this.message != null)
/*     */     {
/* 314 */       return messageSource.getMessage(this.message, getRequestContext().getLocale());
/*     */     }
/*     */     
/* 317 */     if (this.code != null || this.text != null) {
/*     */       
/* 319 */       Object[] argumentsArray = resolveArguments(this.arguments);
/* 320 */       if (!this.nestedArguments.isEmpty()) {
/* 321 */         argumentsArray = appendArguments(argumentsArray, this.nestedArguments.toArray());
/*     */       }
/*     */       
/* 324 */       if (this.text != null) {
/*     */         
/* 326 */         String msg = messageSource.getMessage(this.code, argumentsArray, this.text, 
/* 327 */             getRequestContext().getLocale());
/* 328 */         return (msg != null) ? msg : "";
/*     */       } 
/*     */ 
/*     */       
/* 332 */       return messageSource.getMessage(this.code, argumentsArray, 
/* 333 */           getRequestContext().getLocale());
/*     */     } 
/*     */ 
/*     */     
/* 337 */     throw new JspTagException("No resolvable message");
/*     */   }
/*     */   
/*     */   private Object[] appendArguments(@Nullable Object[] sourceArguments, Object[] additionalArguments) {
/* 341 */     if (ObjectUtils.isEmpty(sourceArguments)) {
/* 342 */       return additionalArguments;
/*     */     }
/* 344 */     Object[] arguments = new Object[sourceArguments.length + additionalArguments.length];
/* 345 */     System.arraycopy(sourceArguments, 0, arguments, 0, sourceArguments.length);
/* 346 */     System.arraycopy(additionalArguments, 0, arguments, sourceArguments.length, additionalArguments.length);
/* 347 */     return arguments;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object[] resolveArguments(@Nullable Object arguments) throws JspException {
/* 359 */     if (arguments instanceof String) {
/*     */       
/* 361 */       String[] stringArray = StringUtils.delimitedListToStringArray((String)arguments, this.argumentSeparator);
/* 362 */       if (stringArray.length == 1) {
/* 363 */         Object argument = stringArray[0];
/* 364 */         if (argument != null && argument.getClass().isArray()) {
/* 365 */           return ObjectUtils.toObjectArray(argument);
/*     */         }
/*     */         
/* 368 */         return new Object[] { argument };
/*     */       } 
/*     */ 
/*     */       
/* 372 */       return (Object[])stringArray;
/*     */     } 
/*     */     
/* 375 */     if (arguments instanceof Object[]) {
/* 376 */       return (Object[])arguments;
/*     */     }
/* 378 */     if (arguments instanceof Collection) {
/* 379 */       return ((Collection)arguments).toArray();
/*     */     }
/* 381 */     if (arguments != null)
/*     */     {
/* 383 */       return new Object[] { arguments };
/*     */     }
/*     */     
/* 386 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeMessage(String msg) throws IOException {
/* 397 */     this.pageContext.getOut().write(String.valueOf(msg));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MessageSource getMessageSource() {
/* 404 */     return getRequestContext().getMessageSource();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getNoSuchMessageExceptionDescription(NoSuchMessageException ex) {
/* 411 */     return ex.getMessage();
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/MessageTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */