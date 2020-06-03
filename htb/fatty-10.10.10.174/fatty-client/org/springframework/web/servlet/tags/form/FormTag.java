/*     */ package org.springframework.web.servlet.tags.form;
/*     */ 
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.jsp.JspException;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.servlet.support.RequestDataValueProcessor;
/*     */ import org.springframework.web.util.HtmlUtils;
/*     */ import org.springframework.web.util.UriUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormTag
/*     */   extends AbstractHtmlElementTag
/*     */ {
/*     */   private static final String DEFAULT_METHOD = "post";
/*     */   public static final String DEFAULT_COMMAND_NAME = "command";
/*     */   private static final String MODEL_ATTRIBUTE = "modelAttribute";
/* 261 */   public static final String MODEL_ATTRIBUTE_VARIABLE_NAME = Conventions.getQualifiedAttributeName(AbstractFormTag.class, "modelAttribute");
/*     */ 
/*     */   
/*     */   private static final String DEFAULT_METHOD_PARAM = "_method";
/*     */ 
/*     */   
/*     */   private static final String FORM_TAG = "form";
/*     */   
/*     */   private static final String INPUT_TAG = "input";
/*     */   
/*     */   private static final String ACTION_ATTRIBUTE = "action";
/*     */   
/*     */   private static final String METHOD_ATTRIBUTE = "method";
/*     */   
/*     */   private static final String TARGET_ATTRIBUTE = "target";
/*     */   
/*     */   private static final String ENCTYPE_ATTRIBUTE = "enctype";
/*     */   
/*     */   private static final String ACCEPT_CHARSET_ATTRIBUTE = "accept-charset";
/*     */   
/*     */   private static final String ONSUBMIT_ATTRIBUTE = "onsubmit";
/*     */   
/*     */   private static final String ONRESET_ATTRIBUTE = "onreset";
/*     */   
/*     */   private static final String AUTOCOMPLETE_ATTRIBUTE = "autocomplete";
/*     */   
/*     */   private static final String NAME_ATTRIBUTE = "name";
/*     */   
/*     */   private static final String VALUE_ATTRIBUTE = "value";
/*     */   
/*     */   private static final String TYPE_ATTRIBUTE = "type";
/*     */   
/*     */   @Nullable
/*     */   private TagWriter tagWriter;
/*     */   
/* 296 */   private String modelAttribute = "command";
/*     */   
/*     */   @Nullable
/*     */   private String name;
/*     */   
/*     */   @Nullable
/*     */   private String action;
/*     */   
/*     */   @Nullable
/*     */   private String servletRelativeAction;
/*     */   
/* 307 */   private String method = "post";
/*     */   
/*     */   @Nullable
/*     */   private String target;
/*     */   
/*     */   @Nullable
/*     */   private String enctype;
/*     */   
/*     */   @Nullable
/*     */   private String acceptCharset;
/*     */   
/*     */   @Nullable
/*     */   private String onsubmit;
/*     */   
/*     */   @Nullable
/*     */   private String onreset;
/*     */   
/*     */   @Nullable
/*     */   private String autocomplete;
/*     */   
/* 327 */   private String methodParam = "_method";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String previousNestedPath;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setModelAttribute(String modelAttribute) {
/* 339 */     this.modelAttribute = modelAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getModelAttribute() {
/* 346 */     return this.modelAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 356 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getName() throws JspException {
/* 365 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAction(@Nullable String action) {
/* 373 */     this.action = (action != null) ? action : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getAction() {
/* 381 */     return this.action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServletRelativeAction(@Nullable String servletRelativeAction) {
/* 391 */     this.servletRelativeAction = servletRelativeAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getServletRelativeAction() {
/* 400 */     return this.servletRelativeAction;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethod(String method) {
/* 408 */     this.method = method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getMethod() {
/* 415 */     return this.method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTarget(String target) {
/* 423 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getTarget() {
/* 431 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnctype(String enctype) {
/* 439 */     this.enctype = enctype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getEnctype() {
/* 447 */     return this.enctype;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAcceptCharset(String acceptCharset) {
/* 455 */     this.acceptCharset = acceptCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getAcceptCharset() {
/* 463 */     return this.acceptCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnsubmit(String onsubmit) {
/* 471 */     this.onsubmit = onsubmit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnsubmit() {
/* 479 */     return this.onsubmit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnreset(String onreset) {
/* 487 */     this.onreset = onreset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getOnreset() {
/* 495 */     return this.onreset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutocomplete(String autocomplete) {
/* 503 */     this.autocomplete = autocomplete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String getAutocomplete() {
/* 511 */     return this.autocomplete;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMethodParam(String methodParam) {
/* 518 */     this.methodParam = methodParam;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getMethodParam() {
/* 526 */     return this.methodParam;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isMethodBrowserSupported(String method) {
/* 533 */     return ("get".equalsIgnoreCase(method) || "post".equalsIgnoreCase(method));
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
/*     */   protected int writeTagContent(TagWriter tagWriter) throws JspException {
/* 545 */     this.tagWriter = tagWriter;
/*     */     
/* 547 */     tagWriter.startTag("form");
/* 548 */     writeDefaultAttributes(tagWriter);
/* 549 */     tagWriter.writeAttribute("action", resolveAction());
/* 550 */     writeOptionalAttribute(tagWriter, "method", getHttpMethod());
/* 551 */     writeOptionalAttribute(tagWriter, "target", getTarget());
/* 552 */     writeOptionalAttribute(tagWriter, "enctype", getEnctype());
/* 553 */     writeOptionalAttribute(tagWriter, "accept-charset", getAcceptCharset());
/* 554 */     writeOptionalAttribute(tagWriter, "onsubmit", getOnsubmit());
/* 555 */     writeOptionalAttribute(tagWriter, "onreset", getOnreset());
/* 556 */     writeOptionalAttribute(tagWriter, "autocomplete", getAutocomplete());
/*     */     
/* 558 */     tagWriter.forceBlock();
/*     */     
/* 560 */     if (!isMethodBrowserSupported(getMethod())) {
/* 561 */       assertHttpMethod(getMethod());
/* 562 */       String inputName = getMethodParam();
/* 563 */       String inputType = "hidden";
/* 564 */       tagWriter.startTag("input");
/* 565 */       writeOptionalAttribute(tagWriter, "type", inputType);
/* 566 */       writeOptionalAttribute(tagWriter, "name", inputName);
/* 567 */       writeOptionalAttribute(tagWriter, "value", processFieldValue(inputName, getMethod(), inputType));
/* 568 */       tagWriter.endTag();
/*     */     } 
/*     */ 
/*     */     
/* 572 */     String modelAttribute = resolveModelAttribute();
/* 573 */     this.pageContext.setAttribute(MODEL_ATTRIBUTE_VARIABLE_NAME, modelAttribute, 2);
/*     */ 
/*     */ 
/*     */     
/* 577 */     this
/* 578 */       .previousNestedPath = (String)this.pageContext.getAttribute("nestedPath", 2);
/* 579 */     this.pageContext.setAttribute("nestedPath", modelAttribute + ".", 2);
/*     */ 
/*     */     
/* 582 */     return 1;
/*     */   }
/*     */   
/*     */   private String getHttpMethod() {
/* 586 */     return isMethodBrowserSupported(getMethod()) ? getMethod() : "post";
/*     */   }
/*     */   
/*     */   private void assertHttpMethod(String method) {
/* 590 */     for (HttpMethod httpMethod : HttpMethod.values()) {
/* 591 */       if (httpMethod.name().equalsIgnoreCase(method)) {
/*     */         return;
/*     */       }
/*     */     } 
/* 595 */     throw new IllegalArgumentException("Invalid HTTP method: " + method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String autogenerateId() throws JspException {
/* 603 */     return resolveModelAttribute();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveModelAttribute() throws JspException {
/* 611 */     Object resolvedModelAttribute = evaluate("modelAttribute", getModelAttribute());
/* 612 */     if (resolvedModelAttribute == null) {
/* 613 */       throw new IllegalArgumentException("modelAttribute must not be null");
/*     */     }
/* 615 */     return (String)resolvedModelAttribute;
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
/*     */   protected String resolveAction() throws JspException {
/* 629 */     String action = getAction();
/* 630 */     String servletRelativeAction = getServletRelativeAction();
/* 631 */     if (StringUtils.hasText(action)) {
/* 632 */       action = getDisplayString(evaluate("action", action));
/* 633 */       return processAction(action);
/*     */     } 
/* 635 */     if (StringUtils.hasText(servletRelativeAction)) {
/* 636 */       String pathToServlet = getRequestContext().getPathToServlet();
/* 637 */       if (servletRelativeAction.startsWith("/") && 
/* 638 */         !servletRelativeAction.startsWith(getRequestContext().getContextPath())) {
/* 639 */         servletRelativeAction = pathToServlet + servletRelativeAction;
/*     */       }
/* 641 */       servletRelativeAction = getDisplayString(evaluate("action", servletRelativeAction));
/* 642 */       return processAction(servletRelativeAction);
/*     */     } 
/*     */     
/* 645 */     String requestUri = getRequestContext().getRequestUri();
/* 646 */     String encoding = this.pageContext.getResponse().getCharacterEncoding();
/*     */     try {
/* 648 */       requestUri = UriUtils.encodePath(requestUri, encoding);
/*     */     }
/* 650 */     catch (UnsupportedCharsetException unsupportedCharsetException) {}
/*     */ 
/*     */     
/* 653 */     ServletResponse response = this.pageContext.getResponse();
/* 654 */     if (response instanceof HttpServletResponse) {
/* 655 */       requestUri = ((HttpServletResponse)response).encodeURL(requestUri);
/* 656 */       String queryString = getRequestContext().getQueryString();
/* 657 */       if (StringUtils.hasText(queryString)) {
/* 658 */         requestUri = requestUri + "?" + HtmlUtils.htmlEscape(queryString);
/*     */       }
/*     */     } 
/* 661 */     if (StringUtils.hasText(requestUri)) {
/* 662 */       return processAction(requestUri);
/*     */     }
/*     */     
/* 665 */     throw new IllegalArgumentException("Attribute 'action' is required. Attempted to resolve against current request URI but request URI was null.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String processAction(String action) {
/* 676 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 677 */     ServletRequest request = this.pageContext.getRequest();
/* 678 */     if (processor != null && request instanceof HttpServletRequest) {
/* 679 */       action = processor.processAction((HttpServletRequest)request, action, getHttpMethod());
/*     */     }
/* 681 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int doEndTag() throws JspException {
/* 690 */     RequestDataValueProcessor processor = getRequestContext().getRequestDataValueProcessor();
/* 691 */     ServletRequest request = this.pageContext.getRequest();
/* 692 */     if (processor != null && request instanceof HttpServletRequest) {
/* 693 */       writeHiddenFields(processor.getExtraHiddenFields((HttpServletRequest)request));
/*     */     }
/* 695 */     Assert.state((this.tagWriter != null), "No TagWriter set");
/* 696 */     this.tagWriter.endTag();
/* 697 */     return 6;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeHiddenFields(@Nullable Map<String, String> hiddenFields) throws JspException {
/* 704 */     if (!CollectionUtils.isEmpty(hiddenFields)) {
/* 705 */       Assert.state((this.tagWriter != null), "No TagWriter set");
/* 706 */       this.tagWriter.appendValue("<div>\n");
/* 707 */       for (String name : hiddenFields.keySet()) {
/* 708 */         this.tagWriter.appendValue("<input type=\"hidden\" ");
/* 709 */         this.tagWriter.appendValue("name=\"" + name + "\" value=\"" + (String)hiddenFields.get(name) + "\" ");
/* 710 */         this.tagWriter.appendValue("/>\n");
/*     */       } 
/* 712 */       this.tagWriter.appendValue("</div>");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doFinally() {
/* 721 */     super.doFinally();
/*     */     
/* 723 */     this.pageContext.removeAttribute(MODEL_ATTRIBUTE_VARIABLE_NAME, 2);
/* 724 */     if (this.previousNestedPath != null) {
/*     */       
/* 726 */       this.pageContext.setAttribute("nestedPath", this.previousNestedPath, 2);
/*     */     }
/*     */     else {
/*     */       
/* 730 */       this.pageContext.removeAttribute("nestedPath", 2);
/*     */     } 
/* 732 */     this.tagWriter = null;
/* 733 */     this.previousNestedPath = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String resolveCssClass() throws JspException {
/* 742 */     return ObjectUtils.getDisplayString(evaluate("cssClass", getCssClass()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPath(String path) {
/* 751 */     throw new UnsupportedOperationException("The 'path' attribute is not supported for forms");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCssErrorClass(String cssErrorClass) {
/* 760 */     throw new UnsupportedOperationException("The 'cssErrorClass' attribute is not supported for forms");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/tags/form/FormTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */