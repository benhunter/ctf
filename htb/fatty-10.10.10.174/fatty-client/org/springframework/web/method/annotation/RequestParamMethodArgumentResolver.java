/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.Part;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RequestPart;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.method.support.UriComponentsContributor;
/*     */ import org.springframework.web.multipart.MultipartException;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartRequest;
/*     */ import org.springframework.web.multipart.support.MissingServletRequestPartException;
/*     */ import org.springframework.web.multipart.support.MultipartResolutionDelegate;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestParamMethodArgumentResolver
/*     */   extends AbstractNamedValueMethodArgumentResolver
/*     */   implements UriComponentsContributor
/*     */ {
/*  79 */   private static final TypeDescriptor STRING_TYPE_DESCRIPTOR = TypeDescriptor.valueOf(String.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean useDefaultResolution;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RequestParamMethodArgumentResolver(boolean useDefaultResolution) {
/*  92 */     this.useDefaultResolution = useDefaultResolution;
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
/*     */ 
/*     */   
/*     */   public RequestParamMethodArgumentResolver(@Nullable ConfigurableBeanFactory beanFactory, boolean useDefaultResolution) {
/* 108 */     super(beanFactory);
/* 109 */     this.useDefaultResolution = useDefaultResolution;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportsParameter(MethodParameter parameter) {
/* 126 */     if (parameter.hasParameterAnnotation(RequestParam.class)) {
/* 127 */       if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
/* 128 */         RequestParam requestParam = (RequestParam)parameter.getParameterAnnotation(RequestParam.class);
/* 129 */         return (requestParam != null && StringUtils.hasText(requestParam.name()));
/*     */       } 
/*     */       
/* 132 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 136 */     if (parameter.hasParameterAnnotation(RequestPart.class)) {
/* 137 */       return false;
/*     */     }
/* 139 */     parameter = parameter.nestedIfOptional();
/* 140 */     if (MultipartResolutionDelegate.isMultipartArgument(parameter)) {
/* 141 */       return true;
/*     */     }
/* 143 */     if (this.useDefaultResolution) {
/* 144 */       return BeanUtils.isSimpleProperty(parameter.getNestedParameterType());
/*     */     }
/*     */     
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractNamedValueMethodArgumentResolver.NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
/* 154 */     RequestParam ann = (RequestParam)parameter.getParameterAnnotation(RequestParam.class);
/* 155 */     return (ann != null) ? new RequestParamNamedValueInfo(ann) : new RequestParamNamedValueInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
/* 161 */     HttpServletRequest servletRequest = (HttpServletRequest)request.getNativeRequest(HttpServletRequest.class);
/*     */     
/* 163 */     if (servletRequest != null) {
/* 164 */       Object mpArg = MultipartResolutionDelegate.resolveMultipartArgument(name, parameter, servletRequest);
/* 165 */       if (mpArg != MultipartResolutionDelegate.UNRESOLVABLE) {
/* 166 */         return mpArg;
/*     */       }
/*     */     } 
/*     */     
/* 170 */     Object arg = null;
/* 171 */     MultipartRequest multipartRequest = (MultipartRequest)request.getNativeRequest(MultipartRequest.class);
/* 172 */     if (multipartRequest != null) {
/* 173 */       List<MultipartFile> files = multipartRequest.getFiles(name);
/* 174 */       if (!files.isEmpty()) {
/* 175 */         arg = (files.size() == 1) ? files.get(0) : files;
/*     */       }
/*     */     } 
/* 178 */     if (arg == null) {
/* 179 */       String[] paramValues = request.getParameterValues(name);
/* 180 */       if (paramValues != null) {
/* 181 */         arg = (paramValues.length == 1) ? paramValues[0] : paramValues;
/*     */       }
/*     */     } 
/* 184 */     return arg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleMissingValue(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
/* 191 */     HttpServletRequest servletRequest = (HttpServletRequest)request.getNativeRequest(HttpServletRequest.class);
/* 192 */     if (MultipartResolutionDelegate.isMultipartArgument(parameter)) {
/* 193 */       if (servletRequest == null || !MultipartResolutionDelegate.isMultipartRequest(servletRequest)) {
/* 194 */         throw new MultipartException("Current request is not a multipart request");
/*     */       }
/*     */       
/* 197 */       throw new MissingServletRequestPartException(name);
/*     */     } 
/*     */ 
/*     */     
/* 201 */     throw new MissingServletRequestParameterException(name, parameter
/* 202 */         .getNestedParameterType().getSimpleName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void contributeMethodArgument(MethodParameter parameter, @Nullable Object value, UriComponentsBuilder builder, Map<String, Object> uriVariables, ConversionService conversionService) {
/* 210 */     Class<?> paramType = parameter.getNestedParameterType();
/* 211 */     if (Map.class.isAssignableFrom(paramType) || MultipartFile.class == paramType || Part.class == paramType) {
/*     */       return;
/*     */     }
/*     */     
/* 215 */     RequestParam requestParam = (RequestParam)parameter.getParameterAnnotation(RequestParam.class);
/*     */     
/* 217 */     String name = (requestParam != null && StringUtils.hasLength(requestParam.name())) ? requestParam.name() : parameter.getParameterName();
/* 218 */     Assert.state((name != null), "Unresolvable parameter name");
/*     */     
/* 220 */     if (value == null) {
/* 221 */       if (requestParam != null && (
/* 222 */         !requestParam.required() || !requestParam.defaultValue().equals("\n\t\t\n\t\t\n\n\t\t\t\t\n"))) {
/*     */         return;
/*     */       }
/* 225 */       builder.queryParam(name, new Object[0]);
/*     */     }
/* 227 */     else if (value instanceof java.util.Collection) {
/* 228 */       for (Object element : value) {
/* 229 */         element = formatUriValue(conversionService, TypeDescriptor.nested(parameter, 1), element);
/* 230 */         builder.queryParam(name, new Object[] { element });
/*     */       } 
/*     */     } else {
/*     */       
/* 234 */       builder.queryParam(name, new Object[] { formatUriValue(conversionService, new TypeDescriptor(parameter), value) });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected String formatUriValue(@Nullable ConversionService cs, @Nullable TypeDescriptor sourceType, @Nullable Object value) {
/* 242 */     if (value == null) {
/* 243 */       return null;
/*     */     }
/* 245 */     if (value instanceof String) {
/* 246 */       return (String)value;
/*     */     }
/* 248 */     if (cs != null) {
/* 249 */       return (String)cs.convert(value, sourceType, STRING_TYPE_DESCRIPTOR);
/*     */     }
/*     */     
/* 252 */     return value.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class RequestParamNamedValueInfo
/*     */     extends AbstractNamedValueMethodArgumentResolver.NamedValueInfo
/*     */   {
/*     */     public RequestParamNamedValueInfo() {
/* 260 */       super("", false, "\n\t\t\n\t\t\n\n\t\t\t\t\n");
/*     */     }
/*     */     
/*     */     public RequestParamNamedValueInfo(RequestParam annotation) {
/* 264 */       super(annotation.name(), annotation.required(), annotation.defaultValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/method/annotation/RequestParamMethodArgumentResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */