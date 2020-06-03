/*     */ package org.slf4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.event.SubstituteLoggingEvent;
/*     */ import org.slf4j.helpers.NOPServiceProvider;
/*     */ import org.slf4j.helpers.SubstitureServiceProvider;
/*     */ import org.slf4j.helpers.SubstituteLogger;
/*     */ import org.slf4j.helpers.Util;
/*     */ import org.slf4j.spi.SLF4JServiceProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LoggerFactory
/*     */ {
/*     */   static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";
/*     */   static final String NO_PROVIDERS_URL = "http://www.slf4j.org/codes.html#noProviders";
/*     */   static final String IGNORED_BINDINGS_URL = "http://www.slf4j.org/codes.html#ignoredBindings";
/*     */   static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";
/*     */   static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";
/*     */   static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";
/*     */   static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";
/*     */   static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";
/*     */   static final String LOGGER_NAME_MISMATCH_URL = "http://www.slf4j.org/codes.html#loggerNameMismatch";
/*     */   static final String REPLAY_URL = "http://www.slf4j.org/codes.html#replay";
/*     */   static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also http://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final int UNINITIALIZED = 0;
/*     */   static final int ONGOING_INITIALIZATION = 1;
/*     */   static final int FAILED_INITIALIZATION = 2;
/*     */   static final int SUCCESSFUL_INITIALIZATION = 3;
/*     */   static final int NOP_FALLBACK_INITIALIZATION = 4;
/*  91 */   static volatile int INITIALIZATION_STATE = 0;
/*  92 */   static final SubstitureServiceProvider SUBST_PROVIDER = new SubstitureServiceProvider();
/*  93 */   static final NOPServiceProvider NOP_FALLBACK_FACTORY = new NOPServiceProvider();
/*     */   
/*     */   static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";
/*     */   
/*     */   static final String JAVA_VENDOR_PROPERTY = "java.vendor.url";
/*     */   
/*  99 */   static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safeGetBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
/*     */   
/*     */   static volatile SLF4JServiceProvider PROVIDER;
/*     */   
/*     */   private static List<SLF4JServiceProvider> findServiceProviders() {
/* 104 */     ServiceLoader<SLF4JServiceProvider> serviceLoader = ServiceLoader.load(SLF4JServiceProvider.class);
/* 105 */     List<SLF4JServiceProvider> providerList = new ArrayList<SLF4JServiceProvider>();
/* 106 */     for (SLF4JServiceProvider provider : serviceLoader) {
/* 107 */       providerList.add(provider);
/*     */     }
/* 109 */     return providerList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   private static final String[] API_COMPATIBILITY_LIST = new String[] { "1.8", "1.7" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void reset() {
/* 137 */     INITIALIZATION_STATE = 0;
/*     */   }
/*     */   
/*     */   private static final void performInitialization() {
/* 141 */     bind();
/* 142 */     if (INITIALIZATION_STATE == 3) {
/* 143 */       versionSanityCheck();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final void bind() {
/*     */     try {
/* 149 */       List<SLF4JServiceProvider> providersList = findServiceProviders();
/* 150 */       reportMultipleBindingAmbiguity(providersList);
/* 151 */       if (providersList != null && !providersList.isEmpty()) {
/* 152 */         PROVIDER = providersList.get(0);
/* 153 */         PROVIDER.initialize();
/* 154 */         INITIALIZATION_STATE = 3;
/* 155 */         reportActualBinding(providersList);
/* 156 */         fixSubstituteLoggers();
/* 157 */         replayEvents();
/*     */         
/* 159 */         SUBST_PROVIDER.getSubstituteLoggerFactory().clear();
/*     */       } else {
/* 161 */         INITIALIZATION_STATE = 4;
/* 162 */         Util.report("No SLF4J providers were found.");
/* 163 */         Util.report("Defaulting to no-operation (NOP) logger implementation");
/* 164 */         Util.report("See http://www.slf4j.org/codes.html#noProviders for further details.");
/*     */         
/* 166 */         Set<URL> staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
/* 167 */         reportIgnoredStaticLoggerBinders(staticLoggerBinderPathSet);
/*     */       } 
/* 169 */     } catch (Exception e) {
/* 170 */       failedBinding(e);
/* 171 */       throw new IllegalStateException("Unexpected initialization failure", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void reportIgnoredStaticLoggerBinders(Set<URL> staticLoggerBinderPathSet) {
/* 176 */     if (staticLoggerBinderPathSet.isEmpty()) {
/*     */       return;
/*     */     }
/* 179 */     Util.report("Class path contains SLF4J bindings targeting slf4j-api versions prior to 1.8.");
/* 180 */     for (URL path : staticLoggerBinderPathSet) {
/* 181 */       Util.report("Ignoring binding found at [" + path + "]");
/*     */     }
/* 183 */     Util.report("See http://www.slf4j.org/codes.html#ignoredBindings for an explanation.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Set<URL> findPossibleStaticLoggerBinderPathSet() {
/* 196 */     Set<URL> staticLoggerBinderPathSet = new LinkedHashSet<URL>(); try {
/*     */       Enumeration<URL> paths;
/* 198 */       ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
/*     */       
/* 200 */       if (loggerFactoryClassLoader == null) {
/* 201 */         paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
/*     */       } else {
/* 203 */         paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
/*     */       } 
/* 205 */       while (paths.hasMoreElements()) {
/* 206 */         URL path = paths.nextElement();
/* 207 */         staticLoggerBinderPathSet.add(path);
/*     */       } 
/* 209 */     } catch (IOException ioe) {
/* 210 */       Util.report("Error getting resources from path", ioe);
/*     */     } 
/* 212 */     return staticLoggerBinderPathSet;
/*     */   }
/*     */   
/*     */   private static void fixSubstituteLoggers() {
/* 216 */     synchronized (SUBST_PROVIDER) {
/* 217 */       SUBST_PROVIDER.getSubstituteLoggerFactory().postInitialization();
/* 218 */       for (SubstituteLogger substLogger : SUBST_PROVIDER.getSubstituteLoggerFactory().getLoggers()) {
/* 219 */         Logger logger = getLogger(substLogger.getName());
/* 220 */         substLogger.setDelegate(logger);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void failedBinding(Throwable t) {
/* 227 */     INITIALIZATION_STATE = 2;
/* 228 */     Util.report("Failed to instantiate SLF4J LoggerFactory", t);
/*     */   }
/*     */   
/*     */   private static void replayEvents() {
/* 232 */     LinkedBlockingQueue<SubstituteLoggingEvent> queue = SUBST_PROVIDER.getSubstituteLoggerFactory().getEventQueue();
/* 233 */     int queueSize = queue.size();
/* 234 */     int count = 0;
/* 235 */     int maxDrain = 128;
/* 236 */     List<SubstituteLoggingEvent> eventList = new ArrayList<SubstituteLoggingEvent>(128);
/*     */     while (true) {
/* 238 */       int numDrained = queue.drainTo(eventList, 128);
/* 239 */       if (numDrained == 0)
/*     */         break; 
/* 241 */       for (SubstituteLoggingEvent event : eventList) {
/* 242 */         replaySingleEvent(event);
/* 243 */         if (count++ == 0)
/* 244 */           emitReplayOrSubstituionWarning(event, queueSize); 
/*     */       } 
/* 246 */       eventList.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void emitReplayOrSubstituionWarning(SubstituteLoggingEvent event, int queueSize) {
/* 251 */     if (event.getLogger().isDelegateEventAware()) {
/* 252 */       emitReplayWarning(queueSize);
/* 253 */     } else if (!event.getLogger().isDelegateNOP()) {
/*     */ 
/*     */       
/* 256 */       emitSubstitutionWarning();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void replaySingleEvent(SubstituteLoggingEvent event) {
/* 261 */     if (event == null) {
/*     */       return;
/*     */     }
/* 264 */     SubstituteLogger substLogger = event.getLogger();
/* 265 */     String loggerName = substLogger.getName();
/* 266 */     if (substLogger.isDelegateNull()) {
/* 267 */       throw new IllegalStateException("Delegate logger cannot be null at this state.");
/*     */     }
/*     */     
/* 270 */     if (!substLogger.isDelegateNOP())
/*     */     {
/* 272 */       if (substLogger.isDelegateEventAware()) {
/* 273 */         substLogger.log((LoggingEvent)event);
/*     */       } else {
/* 275 */         Util.report(loggerName);
/*     */       }  } 
/*     */   }
/*     */   
/*     */   private static void emitSubstitutionWarning() {
/* 280 */     Util.report("The following set of substitute loggers may have been accessed");
/* 281 */     Util.report("during the initialization phase. Logging calls during this");
/* 282 */     Util.report("phase were not honored. However, subsequent logging calls to these");
/* 283 */     Util.report("loggers will work as normally expected.");
/* 284 */     Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
/*     */   }
/*     */   
/*     */   private static void emitReplayWarning(int eventCount) {
/* 288 */     Util.report("A number (" + eventCount + ") of logging calls during the initialization phase have been intercepted and are");
/* 289 */     Util.report("now being replayed. These are subject to the filtering rules of the underlying logging system.");
/* 290 */     Util.report("See also http://www.slf4j.org/codes.html#replay");
/*     */   }
/*     */   
/*     */   private static final void versionSanityCheck() {
/*     */     try {
/* 295 */       String requested = PROVIDER.getRequesteApiVersion();
/*     */       
/* 297 */       boolean match = false;
/* 298 */       for (String aAPI_COMPATIBILITY_LIST : API_COMPATIBILITY_LIST) {
/* 299 */         if (requested.startsWith(aAPI_COMPATIBILITY_LIST)) {
/* 300 */           match = true;
/*     */         }
/*     */       } 
/* 303 */       if (!match) {
/* 304 */         Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + Arrays.<String>asList(API_COMPATIBILITY_LIST).toString());
/*     */         
/* 306 */         Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
/*     */       } 
/* 308 */     } catch (NoSuchFieldError noSuchFieldError) {
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 313 */     catch (Throwable e) {
/*     */       
/* 315 */       Util.report("Unexpected problem occured during version sanity check", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isAmbiguousProviderList(List<SLF4JServiceProvider> providerList) {
/* 320 */     return (providerList.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reportMultipleBindingAmbiguity(List<SLF4JServiceProvider> providerList) {
/* 329 */     if (isAmbiguousProviderList(providerList)) {
/* 330 */       Util.report("Class path contains multiple SLF4J providers.");
/* 331 */       for (SLF4JServiceProvider provider : providerList) {
/* 332 */         Util.report("Found provider [" + provider + "]");
/*     */       }
/* 334 */       Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void reportActualBinding(List<SLF4JServiceProvider> providerList) {
/* 340 */     if (!providerList.isEmpty() && isAmbiguousProviderList(providerList)) {
/* 341 */       Util.report("Actual provider is of type [" + providerList.get(0) + "]");
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
/*     */   public static Logger getLogger(String name) {
/* 354 */     ILoggerFactory iLoggerFactory = getILoggerFactory();
/* 355 */     return iLoggerFactory.getLogger(name);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(Class<?> clazz) {
/* 380 */     Logger logger = getLogger(clazz.getName());
/* 381 */     if (DETECT_LOGGER_NAME_MISMATCH) {
/* 382 */       Class<?> autoComputedCallingClass = Util.getCallingClass();
/* 383 */       if (autoComputedCallingClass != null && nonMatchingClasses(clazz, autoComputedCallingClass)) {
/* 384 */         Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", new Object[] { logger.getName(), autoComputedCallingClass.getName() }));
/*     */         
/* 386 */         Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
/*     */       } 
/*     */     } 
/* 389 */     return logger;
/*     */   }
/*     */   
/*     */   private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
/* 393 */     return !autoComputedCallingClass.isAssignableFrom(clazz);
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
/*     */   public static ILoggerFactory getILoggerFactory() {
/* 405 */     return getProvider().getLoggerFactory();
/*     */   }
/*     */   
/*     */   static SLF4JServiceProvider getProvider() {
/* 409 */     if (INITIALIZATION_STATE == 0) {
/* 410 */       synchronized (LoggerFactory.class) {
/* 411 */         if (INITIALIZATION_STATE == 0) {
/* 412 */           INITIALIZATION_STATE = 1;
/* 413 */           performInitialization();
/*     */         } 
/*     */       } 
/*     */     }
/* 417 */     switch (INITIALIZATION_STATE) {
/*     */       case 3:
/* 419 */         return PROVIDER;
/*     */       case 4:
/* 421 */         return (SLF4JServiceProvider)NOP_FALLBACK_FACTORY;
/*     */       case 2:
/* 423 */         throw new IllegalStateException("org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also http://www.slf4j.org/codes.html#unsuccessfulInit");
/*     */ 
/*     */       
/*     */       case 1:
/* 427 */         return (SLF4JServiceProvider)SUBST_PROVIDER;
/*     */     } 
/* 429 */     throw new IllegalStateException("Unreachable code");
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/LoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */