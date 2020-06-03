/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.servlet.SmartView;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.ViewResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContentNegotiatingViewResolver
/*     */   extends WebApplicationObjectSupport
/*     */   implements ViewResolver, Ordered, InitializingBean
/*     */ {
/*     */   @Nullable
/*     */   private ContentNegotiationManager contentNegotiationManager;
/*  94 */   private final ContentNegotiationManagerFactoryBean cnmFactoryBean = new ContentNegotiationManagerFactoryBean();
/*     */   
/*     */   private boolean useNotAcceptableStatusCode = false;
/*     */   
/*     */   @Nullable
/*     */   private List<View> defaultViews;
/*     */   
/*     */   @Nullable
/*     */   private List<ViewResolver> viewResolvers;
/*     */   
/* 104 */   private int order = Integer.MIN_VALUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentNegotiationManager(@Nullable ContentNegotiationManager contentNegotiationManager) {
/* 114 */     this.contentNegotiationManager = contentNegotiationManager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ContentNegotiationManager getContentNegotiationManager() {
/* 123 */     return this.contentNegotiationManager;
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
/*     */   public void setUseNotAcceptableStatusCode(boolean useNotAcceptableStatusCode) {
/* 136 */     this.useNotAcceptableStatusCode = useNotAcceptableStatusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUseNotAcceptableStatusCode() {
/* 143 */     return this.useNotAcceptableStatusCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultViews(List<View> defaultViews) {
/* 151 */     this.defaultViews = defaultViews;
/*     */   }
/*     */   
/*     */   public List<View> getDefaultViews() {
/* 155 */     return (this.defaultViews != null) ? Collections.<View>unmodifiableList(this.defaultViews) : 
/* 156 */       Collections.<View>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setViewResolvers(List<ViewResolver> viewResolvers) {
/* 164 */     this.viewResolvers = viewResolvers;
/*     */   }
/*     */   
/*     */   public List<ViewResolver> getViewResolvers() {
/* 168 */     return (this.viewResolvers != null) ? Collections.<ViewResolver>unmodifiableList(this.viewResolvers) : 
/* 169 */       Collections.<ViewResolver>emptyList();
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 173 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getOrder() {
/* 178 */     return this.order;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initServletContext(ServletContext servletContext) {
/* 185 */     Collection<ViewResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)obtainApplicationContext(), ViewResolver.class).values();
/* 186 */     if (this.viewResolvers == null) {
/* 187 */       this.viewResolvers = new ArrayList<>(matchingBeans.size());
/* 188 */       for (ViewResolver viewResolver : matchingBeans) {
/* 189 */         if (this != viewResolver) {
/* 190 */           this.viewResolvers.add(viewResolver);
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 195 */       for (int i = 0; i < this.viewResolvers.size(); i++) {
/* 196 */         ViewResolver vr = this.viewResolvers.get(i);
/* 197 */         if (!matchingBeans.contains(vr)) {
/*     */ 
/*     */           
/* 200 */           String name = vr.getClass().getName() + i;
/* 201 */           obtainApplicationContext().getAutowireCapableBeanFactory().initializeBean(vr, name);
/*     */         } 
/*     */       } 
/*     */     } 
/* 205 */     AnnotationAwareOrderComparator.sort(this.viewResolvers);
/* 206 */     this.cnmFactoryBean.setServletContext(servletContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterPropertiesSet() {
/* 211 */     if (this.contentNegotiationManager == null) {
/* 212 */       this.contentNegotiationManager = this.cnmFactoryBean.build();
/*     */     }
/* 214 */     if (this.viewResolvers == null || this.viewResolvers.isEmpty()) {
/* 215 */       this.logger.warn("No ViewResolvers configured");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public View resolveViewName(String viewName, Locale locale) throws Exception {
/* 223 */     RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
/* 224 */     Assert.state(attrs instanceof ServletRequestAttributes, "No current ServletRequestAttributes");
/* 225 */     List<MediaType> requestedMediaTypes = getMediaTypes(((ServletRequestAttributes)attrs).getRequest());
/* 226 */     if (requestedMediaTypes != null) {
/* 227 */       List<View> candidateViews = getCandidateViews(viewName, locale, requestedMediaTypes);
/* 228 */       View bestView = getBestView(candidateViews, requestedMediaTypes, attrs);
/* 229 */       if (bestView != null) {
/* 230 */         return bestView;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 235 */     String mediaTypeInfo = (this.logger.isDebugEnabled() && requestedMediaTypes != null) ? (" given " + requestedMediaTypes.toString()) : "";
/*     */     
/* 237 */     if (this.useNotAcceptableStatusCode) {
/* 238 */       if (this.logger.isDebugEnabled()) {
/* 239 */         this.logger.debug("Using 406 NOT_ACCEPTABLE" + mediaTypeInfo);
/*     */       }
/* 241 */       return NOT_ACCEPTABLE_VIEW;
/*     */     } 
/*     */     
/* 244 */     this.logger.debug("View remains unresolved" + mediaTypeInfo);
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected List<MediaType> getMediaTypes(HttpServletRequest request) {
/* 256 */     Assert.state((this.contentNegotiationManager != null), "No ContentNegotiationManager set");
/*     */     try {
/* 258 */       ServletWebRequest webRequest = new ServletWebRequest(request);
/* 259 */       List<MediaType> acceptableMediaTypes = this.contentNegotiationManager.resolveMediaTypes((NativeWebRequest)webRequest);
/* 260 */       List<MediaType> producibleMediaTypes = getProducibleMediaTypes(request);
/* 261 */       Set<MediaType> compatibleMediaTypes = new LinkedHashSet<>();
/* 262 */       for (MediaType acceptable : acceptableMediaTypes) {
/* 263 */         for (MediaType producible : producibleMediaTypes) {
/* 264 */           if (acceptable.isCompatibleWith(producible)) {
/* 265 */             compatibleMediaTypes.add(getMostSpecificMediaType(acceptable, producible));
/*     */           }
/*     */         } 
/*     */       } 
/* 269 */       List<MediaType> selectedMediaTypes = new ArrayList<>(compatibleMediaTypes);
/* 270 */       MediaType.sortBySpecificityAndQuality(selectedMediaTypes);
/* 271 */       return selectedMediaTypes;
/*     */     }
/* 273 */     catch (HttpMediaTypeNotAcceptableException ex) {
/* 274 */       if (this.logger.isDebugEnabled()) {
/* 275 */         this.logger.debug(ex.getMessage());
/*     */       }
/* 277 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<MediaType> getProducibleMediaTypes(HttpServletRequest request) {
/* 284 */     Set<MediaType> mediaTypes = (Set<MediaType>)request.getAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
/* 285 */     if (!CollectionUtils.isEmpty(mediaTypes)) {
/* 286 */       return new ArrayList<>(mediaTypes);
/*     */     }
/*     */     
/* 289 */     return Collections.singletonList(MediaType.ALL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private MediaType getMostSpecificMediaType(MediaType acceptType, MediaType produceType) {
/* 298 */     produceType = produceType.copyQualityValue(acceptType);
/* 299 */     return (MediaType.SPECIFICITY_COMPARATOR.compare(acceptType, produceType) < 0) ? acceptType : produceType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<View> getCandidateViews(String viewName, Locale locale, List<MediaType> requestedMediaTypes) throws Exception {
/* 305 */     List<View> candidateViews = new ArrayList<>();
/* 306 */     if (this.viewResolvers != null) {
/* 307 */       Assert.state((this.contentNegotiationManager != null), "No ContentNegotiationManager set");
/* 308 */       for (ViewResolver viewResolver : this.viewResolvers) {
/* 309 */         View view = viewResolver.resolveViewName(viewName, locale);
/* 310 */         if (view != null) {
/* 311 */           candidateViews.add(view);
/*     */         }
/* 313 */         for (MediaType requestedMediaType : requestedMediaTypes) {
/* 314 */           List<String> extensions = this.contentNegotiationManager.resolveFileExtensions(requestedMediaType);
/* 315 */           for (String extension : extensions) {
/* 316 */             String viewNameWithExtension = viewName + '.' + extension;
/* 317 */             view = viewResolver.resolveViewName(viewNameWithExtension, locale);
/* 318 */             if (view != null) {
/* 319 */               candidateViews.add(view);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 325 */     if (!CollectionUtils.isEmpty(this.defaultViews)) {
/* 326 */       candidateViews.addAll(this.defaultViews);
/*     */     }
/* 328 */     return candidateViews;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private View getBestView(List<View> candidateViews, List<MediaType> requestedMediaTypes, RequestAttributes attrs) {
/* 333 */     for (View candidateView : candidateViews) {
/* 334 */       if (candidateView instanceof SmartView) {
/* 335 */         SmartView smartView = (SmartView)candidateView;
/* 336 */         if (smartView.isRedirectView()) {
/* 337 */           return candidateView;
/*     */         }
/*     */       } 
/*     */     } 
/* 341 */     for (MediaType mediaType : requestedMediaTypes) {
/* 342 */       for (View candidateView : candidateViews) {
/* 343 */         if (StringUtils.hasText(candidateView.getContentType())) {
/* 344 */           MediaType candidateContentType = MediaType.parseMediaType(candidateView.getContentType());
/* 345 */           if (mediaType.isCompatibleWith(candidateContentType)) {
/* 346 */             if (this.logger.isDebugEnabled()) {
/* 347 */               this.logger.debug("Selected '" + mediaType + "' given " + requestedMediaTypes);
/*     */             }
/* 349 */             attrs.setAttribute(View.SELECTED_CONTENT_TYPE, mediaType, 0);
/* 350 */             return candidateView;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 355 */     return null;
/*     */   }
/*     */ 
/*     */   
/* 359 */   private static final View NOT_ACCEPTABLE_VIEW = new View()
/*     */     {
/*     */       @Nullable
/*     */       public String getContentType()
/*     */       {
/* 364 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public void render(@Nullable Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
/* 369 */         response.setStatus(406);
/*     */       }
/*     */     };
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/servlet/view/ContentNegotiatingViewResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */