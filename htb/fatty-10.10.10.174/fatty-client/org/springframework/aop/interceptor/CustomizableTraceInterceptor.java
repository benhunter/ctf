/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.Constants;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.StopWatch;
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
/*     */ public class CustomizableTraceInterceptor
/*     */   extends AbstractTraceInterceptor
/*     */ {
/*     */   public static final String PLACEHOLDER_METHOD_NAME = "$[methodName]";
/*     */   public static final String PLACEHOLDER_TARGET_CLASS_NAME = "$[targetClassName]";
/*     */   public static final String PLACEHOLDER_TARGET_CLASS_SHORT_NAME = "$[targetClassShortName]";
/*     */   public static final String PLACEHOLDER_RETURN_VALUE = "$[returnValue]";
/*     */   public static final String PLACEHOLDER_ARGUMENT_TYPES = "$[argumentTypes]";
/*     */   public static final String PLACEHOLDER_ARGUMENTS = "$[arguments]";
/*     */   public static final String PLACEHOLDER_EXCEPTION = "$[exception]";
/*     */   public static final String PLACEHOLDER_INVOCATION_TIME = "$[invocationTime]";
/*     */   private static final String DEFAULT_ENTER_MESSAGE = "Entering method '$[methodName]' of class [$[targetClassName]]";
/*     */   private static final String DEFAULT_EXIT_MESSAGE = "Exiting method '$[methodName]' of class [$[targetClassName]]";
/*     */   private static final String DEFAULT_EXCEPTION_MESSAGE = "Exception thrown in method '$[methodName]' of class [$[targetClassName]]";
/* 150 */   private static final Pattern PATTERN = Pattern.compile("\\$\\[\\p{Alpha}+\\]");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   private static final Set<Object> ALLOWED_PLACEHOLDERS = (new Constants(CustomizableTraceInterceptor.class))
/* 156 */     .getValues("PLACEHOLDER_");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 162 */   private String enterMessage = "Entering method '$[methodName]' of class [$[targetClassName]]";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   private String exitMessage = "Exiting method '$[methodName]' of class [$[targetClassName]]";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 172 */   private String exceptionMessage = "Exception thrown in method '$[methodName]' of class [$[targetClassName]]";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnterMessage(String enterMessage) throws IllegalArgumentException {
/* 186 */     Assert.hasText(enterMessage, "enterMessage must not be empty");
/* 187 */     checkForInvalidPlaceholders(enterMessage);
/* 188 */     Assert.doesNotContain(enterMessage, "$[returnValue]", "enterMessage cannot contain placeholder $[returnValue]");
/*     */     
/* 190 */     Assert.doesNotContain(enterMessage, "$[exception]", "enterMessage cannot contain placeholder $[exception]");
/*     */     
/* 192 */     Assert.doesNotContain(enterMessage, "$[invocationTime]", "enterMessage cannot contain placeholder $[invocationTime]");
/*     */     
/* 194 */     this.enterMessage = enterMessage;
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
/*     */   public void setExitMessage(String exitMessage) {
/* 210 */     Assert.hasText(exitMessage, "exitMessage must not be empty");
/* 211 */     checkForInvalidPlaceholders(exitMessage);
/* 212 */     Assert.doesNotContain(exitMessage, "$[exception]", "exitMessage cannot contain placeholder$[exception]");
/*     */     
/* 214 */     this.exitMessage = exitMessage;
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
/*     */   public void setExceptionMessage(String exceptionMessage) {
/* 229 */     Assert.hasText(exceptionMessage, "exceptionMessage must not be empty");
/* 230 */     checkForInvalidPlaceholders(exceptionMessage);
/* 231 */     Assert.doesNotContain(exceptionMessage, "$[returnValue]", "exceptionMessage cannot contain placeholder $[returnValue]");
/*     */     
/* 233 */     this.exceptionMessage = exceptionMessage;
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
/*     */   protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
/* 248 */     String name = ClassUtils.getQualifiedMethodName(invocation.getMethod());
/* 249 */     StopWatch stopWatch = new StopWatch(name);
/* 250 */     Object returnValue = null;
/* 251 */     boolean exitThroughException = false;
/*     */     try {
/* 253 */       stopWatch.start(name);
/* 254 */       writeToLog(logger, 
/* 255 */           replacePlaceholders(this.enterMessage, invocation, (Object)null, (Throwable)null, -1L));
/* 256 */       returnValue = invocation.proceed();
/* 257 */       return returnValue;
/*     */     }
/* 259 */     catch (Throwable ex) {
/* 260 */       if (stopWatch.isRunning()) {
/* 261 */         stopWatch.stop();
/*     */       }
/* 263 */       exitThroughException = true;
/* 264 */       writeToLog(logger, replacePlaceholders(this.exceptionMessage, invocation, (Object)null, ex, stopWatch
/* 265 */             .getTotalTimeMillis()), ex);
/* 266 */       throw ex;
/*     */     } finally {
/*     */       
/* 269 */       if (!exitThroughException) {
/* 270 */         if (stopWatch.isRunning()) {
/* 271 */           stopWatch.stop();
/*     */         }
/* 273 */         writeToLog(logger, replacePlaceholders(this.exitMessage, invocation, returnValue, (Throwable)null, stopWatch
/* 274 */               .getTotalTimeMillis()));
/*     */       } 
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String replacePlaceholders(String message, MethodInvocation methodInvocation, @Nullable Object returnValue, @Nullable Throwable throwable, long invocationTime) {
/* 298 */     Matcher matcher = PATTERN.matcher(message);
/*     */     
/* 300 */     StringBuffer output = new StringBuffer();
/* 301 */     while (matcher.find()) {
/* 302 */       String match = matcher.group();
/* 303 */       if ("$[methodName]".equals(match)) {
/* 304 */         matcher.appendReplacement(output, Matcher.quoteReplacement(methodInvocation.getMethod().getName())); continue;
/*     */       } 
/* 306 */       if ("$[targetClassName]".equals(match)) {
/* 307 */         String className = getClassForLogging(methodInvocation.getThis()).getName();
/* 308 */         matcher.appendReplacement(output, Matcher.quoteReplacement(className)); continue;
/*     */       } 
/* 310 */       if ("$[targetClassShortName]".equals(match)) {
/* 311 */         String shortName = ClassUtils.getShortName(getClassForLogging(methodInvocation.getThis()));
/* 312 */         matcher.appendReplacement(output, Matcher.quoteReplacement(shortName)); continue;
/*     */       } 
/* 314 */       if ("$[arguments]".equals(match)) {
/* 315 */         matcher.appendReplacement(output, 
/* 316 */             Matcher.quoteReplacement(StringUtils.arrayToCommaDelimitedString(methodInvocation.getArguments()))); continue;
/*     */       } 
/* 318 */       if ("$[argumentTypes]".equals(match)) {
/* 319 */         appendArgumentTypes(methodInvocation, matcher, output); continue;
/*     */       } 
/* 321 */       if ("$[returnValue]".equals(match)) {
/* 322 */         appendReturnValue(methodInvocation, matcher, output, returnValue); continue;
/*     */       } 
/* 324 */       if (throwable != null && "$[exception]".equals(match)) {
/* 325 */         matcher.appendReplacement(output, Matcher.quoteReplacement(throwable.toString())); continue;
/*     */       } 
/* 327 */       if ("$[invocationTime]".equals(match)) {
/* 328 */         matcher.appendReplacement(output, Long.toString(invocationTime));
/*     */         
/*     */         continue;
/*     */       } 
/* 332 */       throw new IllegalArgumentException("Unknown placeholder [" + match + "]");
/*     */     } 
/*     */     
/* 335 */     matcher.appendTail(output);
/*     */     
/* 337 */     return output.toString();
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
/*     */   private void appendReturnValue(MethodInvocation methodInvocation, Matcher matcher, StringBuffer output, @Nullable Object returnValue) {
/* 352 */     if (methodInvocation.getMethod().getReturnType() == void.class) {
/* 353 */       matcher.appendReplacement(output, "void");
/*     */     }
/* 355 */     else if (returnValue == null) {
/* 356 */       matcher.appendReplacement(output, "null");
/*     */     } else {
/*     */       
/* 359 */       matcher.appendReplacement(output, Matcher.quoteReplacement(returnValue.toString()));
/*     */     } 
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
/*     */   private void appendArgumentTypes(MethodInvocation methodInvocation, Matcher matcher, StringBuffer output) {
/* 374 */     Class<?>[] argumentTypes = methodInvocation.getMethod().getParameterTypes();
/* 375 */     String[] argumentTypeShortNames = new String[argumentTypes.length];
/* 376 */     for (int i = 0; i < argumentTypeShortNames.length; i++) {
/* 377 */       argumentTypeShortNames[i] = ClassUtils.getShortName(argumentTypes[i]);
/*     */     }
/* 379 */     matcher.appendReplacement(output, 
/* 380 */         Matcher.quoteReplacement(StringUtils.arrayToCommaDelimitedString((Object[])argumentTypeShortNames)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkForInvalidPlaceholders(String message) throws IllegalArgumentException {
/* 389 */     Matcher matcher = PATTERN.matcher(message);
/* 390 */     while (matcher.find()) {
/* 391 */       String match = matcher.group();
/* 392 */       if (!ALLOWED_PLACEHOLDERS.contains(match))
/* 393 */         throw new IllegalArgumentException("Placeholder [" + match + "] is not valid"); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/aop/interceptor/CustomizableTraceInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */