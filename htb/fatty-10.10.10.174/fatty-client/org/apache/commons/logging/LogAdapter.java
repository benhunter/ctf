/*     */ package org.apache.commons.logging;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.logging.log4j.Level;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.spi.ExtendedLogger;
/*     */ import org.apache.logging.log4j.spi.LoggerContext;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.slf4j.spi.LocationAwareLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class LogAdapter
/*     */ {
/*     */   private static final String LOG4J_SPI = "org.apache.logging.log4j.spi.ExtendedLogger";
/*     */   private static final String LOG4J_SLF4J_PROVIDER = "org.apache.logging.slf4j.SLF4JProvider";
/*     */   private static final String SLF4J_SPI = "org.slf4j.spi.LocationAwareLogger";
/*     */   private static final String SLF4J_API = "org.slf4j.Logger";
/*     */   private static final LogApi logApi;
/*     */   
/*     */   static {
/*  51 */     if (isPresent("org.apache.logging.log4j.spi.ExtendedLogger")) {
/*  52 */       if (isPresent("org.apache.logging.slf4j.SLF4JProvider") && isPresent("org.slf4j.spi.LocationAwareLogger"))
/*     */       {
/*     */ 
/*     */         
/*  56 */         logApi = LogApi.SLF4J_LAL;
/*     */       }
/*     */       else
/*     */       {
/*  60 */         logApi = LogApi.LOG4J;
/*     */       }
/*     */     
/*  63 */     } else if (isPresent("org.slf4j.spi.LocationAwareLogger")) {
/*     */       
/*  65 */       logApi = LogApi.SLF4J_LAL;
/*     */     }
/*  67 */     else if (isPresent("org.slf4j.Logger")) {
/*     */       
/*  69 */       logApi = LogApi.SLF4J;
/*     */     }
/*     */     else {
/*     */       
/*  73 */       logApi = LogApi.JUL;
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
/*     */   public static Log createLog(String name) {
/*  87 */     switch (logApi) {
/*     */       case LOG4J:
/*  89 */         return Log4jAdapter.createLog(name);
/*     */       case SLF4J_LAL:
/*  91 */         return Slf4jAdapter.createLocationAwareLog(name);
/*     */       case SLF4J:
/*  93 */         return Slf4jAdapter.createLog(name);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     return JavaUtilAdapter.createLog(name);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isPresent(String className) {
/*     */     try {
/* 107 */       Class.forName(className, false, LogAdapter.class.getClassLoader());
/* 108 */       return true;
/*     */     }
/* 110 */     catch (ClassNotFoundException ex) {
/* 111 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private enum LogApi {
/* 116 */     LOG4J, SLF4J_LAL, SLF4J, JUL;
/*     */   }
/*     */   
/*     */   private static class Log4jAdapter
/*     */   {
/*     */     public static Log createLog(String name) {
/* 122 */       return new LogAdapter.Log4jLog(name);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Slf4jAdapter
/*     */   {
/*     */     public static Log createLocationAwareLog(String name) {
/* 130 */       Logger logger = LoggerFactory.getLogger(name);
/* 131 */       return (logger instanceof LocationAwareLogger) ? new LogAdapter.Slf4jLocationAwareLog((LocationAwareLogger)logger) : new LogAdapter.Slf4jLog<>(logger);
/*     */     }
/*     */ 
/*     */     
/*     */     public static Log createLog(String name) {
/* 136 */       return new LogAdapter.Slf4jLog<>(LoggerFactory.getLogger(name));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JavaUtilAdapter
/*     */   {
/*     */     public static Log createLog(String name) {
/* 144 */       return new LogAdapter.JavaUtilLog(name);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Log4jLog
/*     */     implements Log, Serializable
/*     */   {
/* 152 */     private static final String FQCN = Log4jLog.class.getName();
/*     */ 
/*     */     
/* 155 */     private static final LoggerContext loggerContext = LogManager.getContext(Log4jLog.class.getClassLoader(), false);
/*     */     
/*     */     private final ExtendedLogger logger;
/*     */     
/*     */     public Log4jLog(String name) {
/* 160 */       this.logger = loggerContext.getLogger(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isFatalEnabled() {
/* 165 */       return this.logger.isEnabled(Level.FATAL);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isErrorEnabled() {
/* 170 */       return this.logger.isEnabled(Level.ERROR);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isWarnEnabled() {
/* 175 */       return this.logger.isEnabled(Level.WARN);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isInfoEnabled() {
/* 180 */       return this.logger.isEnabled(Level.INFO);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDebugEnabled() {
/* 185 */       return this.logger.isEnabled(Level.DEBUG);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isTraceEnabled() {
/* 190 */       return this.logger.isEnabled(Level.TRACE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void fatal(Object message) {
/* 195 */       log(Level.FATAL, message, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void fatal(Object message, Throwable exception) {
/* 200 */       log(Level.FATAL, message, exception);
/*     */     }
/*     */ 
/*     */     
/*     */     public void error(Object message) {
/* 205 */       log(Level.ERROR, message, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void error(Object message, Throwable exception) {
/* 210 */       log(Level.ERROR, message, exception);
/*     */     }
/*     */ 
/*     */     
/*     */     public void warn(Object message) {
/* 215 */       log(Level.WARN, message, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void warn(Object message, Throwable exception) {
/* 220 */       log(Level.WARN, message, exception);
/*     */     }
/*     */ 
/*     */     
/*     */     public void info(Object message) {
/* 225 */       log(Level.INFO, message, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void info(Object message, Throwable exception) {
/* 230 */       log(Level.INFO, message, exception);
/*     */     }
/*     */ 
/*     */     
/*     */     public void debug(Object message) {
/* 235 */       log(Level.DEBUG, message, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void debug(Object message, Throwable exception) {
/* 240 */       log(Level.DEBUG, message, exception);
/*     */     }
/*     */ 
/*     */     
/*     */     public void trace(Object message) {
/* 245 */       log(Level.TRACE, message, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void trace(Object message, Throwable exception) {
/* 250 */       log(Level.TRACE, message, exception);
/*     */     }
/*     */     
/*     */     private void log(Level level, Object message, Throwable exception) {
/* 254 */       if (message instanceof String) {
/*     */ 
/*     */         
/* 257 */         if (exception != null) {
/* 258 */           this.logger.logIfEnabled(FQCN, level, null, (String)message, exception);
/*     */         } else {
/*     */           
/* 261 */           this.logger.logIfEnabled(FQCN, level, null, (String)message);
/*     */         } 
/*     */       } else {
/*     */         
/* 265 */         this.logger.logIfEnabled(FQCN, level, null, message, exception);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Slf4jLog<T extends Logger>
/*     */     implements Log, Serializable
/*     */   {
/*     */     protected final String name;
/*     */     
/*     */     protected transient T logger;
/*     */     
/*     */     public Slf4jLog(T logger) {
/* 279 */       this.name = logger.getName();
/* 280 */       this.logger = logger;
/*     */     }
/*     */     
/*     */     public boolean isFatalEnabled() {
/* 284 */       return isErrorEnabled();
/*     */     }
/*     */     
/*     */     public boolean isErrorEnabled() {
/* 288 */       return this.logger.isErrorEnabled();
/*     */     }
/*     */     
/*     */     public boolean isWarnEnabled() {
/* 292 */       return this.logger.isWarnEnabled();
/*     */     }
/*     */     
/*     */     public boolean isInfoEnabled() {
/* 296 */       return this.logger.isInfoEnabled();
/*     */     }
/*     */     
/*     */     public boolean isDebugEnabled() {
/* 300 */       return this.logger.isDebugEnabled();
/*     */     }
/*     */     
/*     */     public boolean isTraceEnabled() {
/* 304 */       return this.logger.isTraceEnabled();
/*     */     }
/*     */     
/*     */     public void fatal(Object message) {
/* 308 */       error(message);
/*     */     }
/*     */     
/*     */     public void fatal(Object message, Throwable exception) {
/* 312 */       error(message, exception);
/*     */     }
/*     */     
/*     */     public void error(Object message) {
/* 316 */       if (message instanceof String || this.logger.isErrorEnabled()) {
/* 317 */         this.logger.error(String.valueOf(message));
/*     */       }
/*     */     }
/*     */     
/*     */     public void error(Object message, Throwable exception) {
/* 322 */       if (message instanceof String || this.logger.isErrorEnabled()) {
/* 323 */         this.logger.error(String.valueOf(message), exception);
/*     */       }
/*     */     }
/*     */     
/*     */     public void warn(Object message) {
/* 328 */       if (message instanceof String || this.logger.isWarnEnabled()) {
/* 329 */         this.logger.warn(String.valueOf(message));
/*     */       }
/*     */     }
/*     */     
/*     */     public void warn(Object message, Throwable exception) {
/* 334 */       if (message instanceof String || this.logger.isWarnEnabled()) {
/* 335 */         this.logger.warn(String.valueOf(message), exception);
/*     */       }
/*     */     }
/*     */     
/*     */     public void info(Object message) {
/* 340 */       if (message instanceof String || this.logger.isInfoEnabled()) {
/* 341 */         this.logger.info(String.valueOf(message));
/*     */       }
/*     */     }
/*     */     
/*     */     public void info(Object message, Throwable exception) {
/* 346 */       if (message instanceof String || this.logger.isInfoEnabled()) {
/* 347 */         this.logger.info(String.valueOf(message), exception);
/*     */       }
/*     */     }
/*     */     
/*     */     public void debug(Object message) {
/* 352 */       if (message instanceof String || this.logger.isDebugEnabled()) {
/* 353 */         this.logger.debug(String.valueOf(message));
/*     */       }
/*     */     }
/*     */     
/*     */     public void debug(Object message, Throwable exception) {
/* 358 */       if (message instanceof String || this.logger.isDebugEnabled()) {
/* 359 */         this.logger.debug(String.valueOf(message), exception);
/*     */       }
/*     */     }
/*     */     
/*     */     public void trace(Object message) {
/* 364 */       if (message instanceof String || this.logger.isTraceEnabled()) {
/* 365 */         this.logger.trace(String.valueOf(message));
/*     */       }
/*     */     }
/*     */     
/*     */     public void trace(Object message, Throwable exception) {
/* 370 */       if (message instanceof String || this.logger.isTraceEnabled()) {
/* 371 */         this.logger.trace(String.valueOf(message), exception);
/*     */       }
/*     */     }
/*     */     
/*     */     protected Object readResolve() {
/* 376 */       return LogAdapter.Slf4jAdapter.createLog(this.name);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Slf4jLocationAwareLog
/*     */     extends Slf4jLog<LocationAwareLogger>
/*     */     implements Serializable
/*     */   {
/* 384 */     private static final String FQCN = Slf4jLocationAwareLog.class.getName();
/*     */     
/*     */     public Slf4jLocationAwareLog(LocationAwareLogger logger) {
/* 387 */       super(logger);
/*     */     }
/*     */ 
/*     */     
/*     */     public void fatal(Object message) {
/* 392 */       error(message);
/*     */     }
/*     */ 
/*     */     
/*     */     public void fatal(Object message, Throwable exception) {
/* 397 */       error(message, exception);
/*     */     }
/*     */ 
/*     */     
/*     */     public void error(Object message) {
/* 402 */       if (message instanceof String || this.logger.isErrorEnabled()) {
/* 403 */         this.logger.log(null, FQCN, 40, String.valueOf(message), null, null);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void error(Object message, Throwable exception) {
/* 409 */       if (message instanceof String || this.logger.isErrorEnabled()) {
/* 410 */         this.logger.log(null, FQCN, 40, String.valueOf(message), null, exception);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void warn(Object message) {
/* 416 */       if (message instanceof String || this.logger.isWarnEnabled()) {
/* 417 */         this.logger.log(null, FQCN, 30, String.valueOf(message), null, null);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void warn(Object message, Throwable exception) {
/* 423 */       if (message instanceof String || this.logger.isWarnEnabled()) {
/* 424 */         this.logger.log(null, FQCN, 30, String.valueOf(message), null, exception);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void info(Object message) {
/* 430 */       if (message instanceof String || this.logger.isInfoEnabled()) {
/* 431 */         this.logger.log(null, FQCN, 20, String.valueOf(message), null, null);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void info(Object message, Throwable exception) {
/* 437 */       if (message instanceof String || this.logger.isInfoEnabled()) {
/* 438 */         this.logger.log(null, FQCN, 20, String.valueOf(message), null, exception);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void debug(Object message) {
/* 444 */       if (message instanceof String || this.logger.isDebugEnabled()) {
/* 445 */         this.logger.log(null, FQCN, 10, String.valueOf(message), null, null);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void debug(Object message, Throwable exception) {
/* 451 */       if (message instanceof String || this.logger.isDebugEnabled()) {
/* 452 */         this.logger.log(null, FQCN, 10, String.valueOf(message), null, exception);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void trace(Object message) {
/* 458 */       if (message instanceof String || this.logger.isTraceEnabled()) {
/* 459 */         this.logger.log(null, FQCN, 0, String.valueOf(message), null, null);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void trace(Object message, Throwable exception) {
/* 465 */       if (message instanceof String || this.logger.isTraceEnabled()) {
/* 466 */         this.logger.log(null, FQCN, 0, String.valueOf(message), null, exception);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object readResolve() {
/* 472 */       return LogAdapter.Slf4jAdapter.createLocationAwareLog(this.name);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class JavaUtilLog
/*     */     implements Log, Serializable
/*     */   {
/*     */     private String name;
/*     */     
/*     */     private transient Logger logger;
/*     */     
/*     */     public JavaUtilLog(String name) {
/* 485 */       this.name = name;
/* 486 */       this.logger = Logger.getLogger(name);
/*     */     }
/*     */     
/*     */     public boolean isFatalEnabled() {
/* 490 */       return isErrorEnabled();
/*     */     }
/*     */     
/*     */     public boolean isErrorEnabled() {
/* 494 */       return this.logger.isLoggable(Level.SEVERE);
/*     */     }
/*     */     
/*     */     public boolean isWarnEnabled() {
/* 498 */       return this.logger.isLoggable(Level.WARNING);
/*     */     }
/*     */     
/*     */     public boolean isInfoEnabled() {
/* 502 */       return this.logger.isLoggable(Level.INFO);
/*     */     }
/*     */     
/*     */     public boolean isDebugEnabled() {
/* 506 */       return this.logger.isLoggable(Level.FINE);
/*     */     }
/*     */     
/*     */     public boolean isTraceEnabled() {
/* 510 */       return this.logger.isLoggable(Level.FINEST);
/*     */     }
/*     */     
/*     */     public void fatal(Object message) {
/* 514 */       error(message);
/*     */     }
/*     */     
/*     */     public void fatal(Object message, Throwable exception) {
/* 518 */       error(message, exception);
/*     */     }
/*     */     
/*     */     public void error(Object message) {
/* 522 */       log(Level.SEVERE, message, null);
/*     */     }
/*     */     
/*     */     public void error(Object message, Throwable exception) {
/* 526 */       log(Level.SEVERE, message, exception);
/*     */     }
/*     */     
/*     */     public void warn(Object message) {
/* 530 */       log(Level.WARNING, message, null);
/*     */     }
/*     */     
/*     */     public void warn(Object message, Throwable exception) {
/* 534 */       log(Level.WARNING, message, exception);
/*     */     }
/*     */     
/*     */     public void info(Object message) {
/* 538 */       log(Level.INFO, message, null);
/*     */     }
/*     */     
/*     */     public void info(Object message, Throwable exception) {
/* 542 */       log(Level.INFO, message, exception);
/*     */     }
/*     */     
/*     */     public void debug(Object message) {
/* 546 */       log(Level.FINE, message, null);
/*     */     }
/*     */     
/*     */     public void debug(Object message, Throwable exception) {
/* 550 */       log(Level.FINE, message, exception);
/*     */     }
/*     */     
/*     */     public void trace(Object message) {
/* 554 */       log(Level.FINEST, message, null);
/*     */     }
/*     */     
/*     */     public void trace(Object message, Throwable exception) {
/* 558 */       log(Level.FINEST, message, exception);
/*     */     }
/*     */     
/*     */     private void log(Level level, Object message, Throwable exception) {
/* 562 */       if (this.logger.isLoggable(level)) {
/*     */         LogRecord rec;
/* 564 */         if (message instanceof LogRecord) {
/* 565 */           rec = (LogRecord)message;
/*     */         } else {
/*     */           
/* 568 */           rec = new LogAdapter.LocationResolvingLogRecord(level, String.valueOf(message));
/* 569 */           rec.setLoggerName(this.name);
/* 570 */           rec.setResourceBundleName(this.logger.getResourceBundleName());
/* 571 */           rec.setResourceBundle(this.logger.getResourceBundle());
/* 572 */           rec.setThrown(exception);
/*     */         } 
/* 574 */         this.logger.log(rec);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected Object readResolve() {
/* 579 */       return new JavaUtilLog(this.name);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class LocationResolvingLogRecord
/*     */     extends LogRecord
/*     */   {
/* 587 */     private static final String FQCN = LogAdapter.JavaUtilLog.class.getName();
/*     */     
/*     */     private volatile boolean resolved;
/*     */     
/*     */     public LocationResolvingLogRecord(Level level, String msg) {
/* 592 */       super(level, msg);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSourceClassName() {
/* 597 */       if (!this.resolved) {
/* 598 */         resolve();
/*     */       }
/* 600 */       return super.getSourceClassName();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSourceClassName(String sourceClassName) {
/* 605 */       super.setSourceClassName(sourceClassName);
/* 606 */       this.resolved = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSourceMethodName() {
/* 611 */       if (!this.resolved) {
/* 612 */         resolve();
/*     */       }
/* 614 */       return super.getSourceMethodName();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSourceMethodName(String sourceMethodName) {
/* 619 */       super.setSourceMethodName(sourceMethodName);
/* 620 */       this.resolved = true;
/*     */     }
/*     */     
/*     */     private void resolve() {
/* 624 */       StackTraceElement[] stack = (new Throwable()).getStackTrace();
/* 625 */       String sourceClassName = null;
/* 626 */       String sourceMethodName = null;
/* 627 */       boolean found = false;
/* 628 */       for (StackTraceElement element : stack) {
/* 629 */         String className = element.getClassName();
/* 630 */         if (FQCN.equals(className)) {
/* 631 */           found = true;
/*     */         }
/* 633 */         else if (found) {
/* 634 */           sourceClassName = className;
/* 635 */           sourceMethodName = element.getMethodName();
/*     */           break;
/*     */         } 
/*     */       } 
/* 639 */       setSourceClassName(sourceClassName);
/* 640 */       setSourceMethodName(sourceMethodName);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Object writeReplace() {
/* 645 */       LogRecord serialized = new LogRecord(getLevel(), getMessage());
/* 646 */       serialized.setLoggerName(getLoggerName());
/* 647 */       serialized.setResourceBundle(getResourceBundle());
/* 648 */       serialized.setResourceBundleName(getResourceBundleName());
/* 649 */       serialized.setSourceClassName(getSourceClassName());
/* 650 */       serialized.setSourceMethodName(getSourceMethodName());
/* 651 */       serialized.setSequenceNumber(getSequenceNumber());
/* 652 */       serialized.setParameters(getParameters());
/* 653 */       serialized.setThreadID(getThreadID());
/* 654 */       serialized.setMillis(getMillis());
/* 655 */       serialized.setThrown(getThrown());
/* 656 */       return serialized;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/apache/commons/logging/LogAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */