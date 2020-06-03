/*     */ package org.slf4j.log4j12;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.log4j.Category;
/*     */ import org.apache.log4j.Level;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.spi.LocationInfo;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ import org.apache.log4j.spi.ThrowableInformation;
/*     */ import org.slf4j.Marker;
/*     */ import org.slf4j.event.LoggingEvent;
/*     */ import org.slf4j.helpers.FormattingTuple;
/*     */ import org.slf4j.helpers.MarkerIgnoringBase;
/*     */ import org.slf4j.helpers.MessageFormatter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Log4jLoggerAdapter
/*     */   extends MarkerIgnoringBase
/*     */   implements LocationAwareLogger, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6182834493563598289L;
/*     */   final transient Logger logger;
/*  70 */   static final String FQCN = Log4jLoggerAdapter.class.getName();
/*     */ 
/*     */ 
/*     */   
/*     */   final boolean traceCapable;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Log4jLoggerAdapter(Logger logger) {
/*  80 */     this.logger = logger;
/*  81 */     this.name = logger.getName();
/*  82 */     this.traceCapable = isTraceCapable();
/*     */   }
/*     */   
/*     */   private boolean isTraceCapable() {
/*     */     try {
/*  87 */       this.logger.isTraceEnabled();
/*  88 */       return true;
/*  89 */     } catch (NoSuchMethodError e) {
/*  90 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTraceEnabled() {
/* 100 */     if (this.traceCapable) {
/* 101 */       return this.logger.isTraceEnabled();
/*     */     }
/* 103 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trace(String msg) {
/* 114 */     this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, msg, null);
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
/*     */   public void trace(String format, Object arg) {
/* 132 */     if (isTraceEnabled()) {
/* 133 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 134 */       this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
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
/*     */   public void trace(String format, Object arg1, Object arg2) {
/* 155 */     if (isTraceEnabled()) {
/* 156 */       FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
/* 157 */       this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
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
/*     */   public void trace(String format, Object... arguments) {
/* 176 */     if (isTraceEnabled()) {
/* 177 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 178 */       this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
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
/*     */   public void trace(String msg, Throwable t) {
/* 191 */     this.logger.log(FQCN, this.traceCapable ? (Priority)Level.TRACE : (Priority)Level.DEBUG, msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 200 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void debug(String msg) {
/* 210 */     this.logger.log(FQCN, (Priority)Level.DEBUG, msg, null);
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
/*     */   public void debug(String format, Object arg) {
/* 228 */     if (this.logger.isDebugEnabled()) {
/* 229 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 230 */       this.logger.log(FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
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
/*     */   public void debug(String format, Object arg1, Object arg2) {
/* 251 */     if (this.logger.isDebugEnabled()) {
/* 252 */       FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
/* 253 */       this.logger.log(FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
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
/*     */   public void debug(String format, Object... arguments) {
/* 271 */     if (this.logger.isDebugEnabled()) {
/* 272 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
/* 273 */       this.logger.log(FQCN, (Priority)Level.DEBUG, ft.getMessage(), ft.getThrowable());
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
/*     */   public void debug(String msg, Throwable t) {
/* 286 */     this.logger.log(FQCN, (Priority)Level.DEBUG, msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 295 */     return this.logger.isInfoEnabled();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void info(String msg) {
/* 305 */     this.logger.log(FQCN, (Priority)Level.INFO, msg, null);
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
/*     */   public void info(String format, Object arg) {
/* 322 */     if (this.logger.isInfoEnabled()) {
/* 323 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 324 */       this.logger.log(FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
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
/*     */   public void info(String format, Object arg1, Object arg2) {
/* 345 */     if (this.logger.isInfoEnabled()) {
/* 346 */       FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
/* 347 */       this.logger.log(FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
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
/*     */   public void info(String format, Object... argArray) {
/* 366 */     if (this.logger.isInfoEnabled()) {
/* 367 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 368 */       this.logger.log(FQCN, (Priority)Level.INFO, ft.getMessage(), ft.getThrowable());
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
/*     */   public void info(String msg, Throwable t) {
/* 382 */     this.logger.log(FQCN, (Priority)Level.INFO, msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 391 */     return this.logger.isEnabledFor((Priority)Level.WARN);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void warn(String msg) {
/* 401 */     this.logger.log(FQCN, (Priority)Level.WARN, msg, null);
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
/*     */   public void warn(String format, Object arg) {
/* 419 */     if (this.logger.isEnabledFor((Priority)Level.WARN)) {
/* 420 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 421 */       this.logger.log(FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
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
/*     */   public void warn(String format, Object arg1, Object arg2) {
/* 442 */     if (this.logger.isEnabledFor((Priority)Level.WARN)) {
/* 443 */       FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
/* 444 */       this.logger.log(FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
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
/*     */   public void warn(String format, Object... argArray) {
/* 463 */     if (this.logger.isEnabledFor((Priority)Level.WARN)) {
/* 464 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 465 */       this.logger.log(FQCN, (Priority)Level.WARN, ft.getMessage(), ft.getThrowable());
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
/*     */   public void warn(String msg, Throwable t) {
/* 479 */     this.logger.log(FQCN, (Priority)Level.WARN, msg, t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 488 */     return this.logger.isEnabledFor((Priority)Level.ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void error(String msg) {
/* 498 */     this.logger.log(FQCN, (Priority)Level.ERROR, msg, null);
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
/*     */   public void error(String format, Object arg) {
/* 516 */     if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
/* 517 */       FormattingTuple ft = MessageFormatter.format(format, arg);
/* 518 */       this.logger.log(FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
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
/*     */   public void error(String format, Object arg1, Object arg2) {
/* 539 */     if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
/* 540 */       FormattingTuple ft = MessageFormatter.format(format, arg1, arg2);
/* 541 */       this.logger.log(FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
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
/*     */   public void error(String format, Object... argArray) {
/* 560 */     if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
/* 561 */       FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
/* 562 */       this.logger.log(FQCN, (Priority)Level.ERROR, ft.getMessage(), ft.getThrowable());
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
/*     */   public void error(String msg, Throwable t) {
/* 576 */     this.logger.log(FQCN, (Priority)Level.ERROR, msg, t);
/*     */   }
/*     */   
/*     */   public void log(Marker marker, String callerFQCN, int level, String msg, Object[] argArray, Throwable t) {
/* 580 */     Level log4jLevel = toLog4jLevel(level);
/* 581 */     this.logger.log(callerFQCN, (Priority)log4jLevel, msg, t);
/*     */   }
/*     */   
/*     */   private Level toLog4jLevel(int level) {
/*     */     Level log4jLevel;
/* 586 */     switch (level) {
/*     */       case 0:
/* 588 */         log4jLevel = this.traceCapable ? Level.TRACE : Level.DEBUG;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 605 */         return log4jLevel;case 10: log4jLevel = Level.DEBUG; return log4jLevel;case 20: log4jLevel = Level.INFO; return log4jLevel;case 30: log4jLevel = Level.WARN; return log4jLevel;case 40: log4jLevel = Level.ERROR; return log4jLevel;
/*     */     } 
/*     */     throw new IllegalStateException("Level number " + level + " is not recognized.");
/*     */   } public void log(LoggingEvent event) {
/* 609 */     Level log4jLevel = toLog4jLevel(event.getLevel().toInt());
/* 610 */     if (!this.logger.isEnabledFor((Priority)log4jLevel)) {
/*     */       return;
/*     */     }
/* 613 */     LoggingEvent log4jevent = toLog4jEvent(event, log4jLevel);
/* 614 */     this.logger.callAppenders(log4jevent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LoggingEvent toLog4jEvent(LoggingEvent event, Level log4jLevel) {
/* 620 */     FormattingTuple ft = MessageFormatter.format(event.getMessage(), event.getArgumentArray(), event.getThrowable());
/*     */     
/* 622 */     LocationInfo locationInfo = new LocationInfo("NA/SubstituteLogger", "NA/SubstituteLogger", "NA/SubstituteLogger", "0");
/*     */     
/* 624 */     ThrowableInformation ti = null;
/* 625 */     Throwable t = ft.getThrowable();
/* 626 */     if (t != null) {
/* 627 */       ti = new ThrowableInformation(t);
/*     */     }
/* 629 */     LoggingEvent log4jEvent = new LoggingEvent(FQCN, (Category)this.logger, event.getTimeStamp(), log4jLevel, ft.getMessage(), event.getThreadName(), ti, null, locationInfo, null);
/*     */ 
/*     */     
/* 632 */     return log4jEvent;
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/slf4j/log4j12/Log4jLoggerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */