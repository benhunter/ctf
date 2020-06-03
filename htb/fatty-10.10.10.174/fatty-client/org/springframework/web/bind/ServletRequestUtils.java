/*     */ package org.springframework.web.bind;
/*     */ 
/*     */ import javax.servlet.ServletRequest;
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
/*     */ public abstract class ServletRequestUtils
/*     */ {
/*  36 */   private static final IntParser INT_PARSER = new IntParser();
/*     */   
/*  38 */   private static final LongParser LONG_PARSER = new LongParser();
/*     */   
/*  40 */   private static final FloatParser FLOAT_PARSER = new FloatParser();
/*     */   
/*  42 */   private static final DoubleParser DOUBLE_PARSER = new DoubleParser();
/*     */   
/*  44 */   private static final BooleanParser BOOLEAN_PARSER = new BooleanParser();
/*     */   
/*  46 */   private static final StringParser STRING_PARSER = new StringParser();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Integer getIntParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/*  62 */     if (request.getParameter(name) == null) {
/*  63 */       return null;
/*     */     }
/*  65 */     return Integer.valueOf(getRequiredIntParameter(request, name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getIntParameter(ServletRequest request, String name, int defaultVal) {
/*  76 */     if (request.getParameter(name) == null) {
/*  77 */       return defaultVal;
/*     */     }
/*     */     try {
/*  80 */       return getRequiredIntParameter(request, name);
/*     */     }
/*  82 */     catch (ServletRequestBindingException ex) {
/*  83 */       return defaultVal;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] getIntParameters(ServletRequest request, String name) {
/*     */     try {
/*  94 */       return getRequiredIntParameters(request, name);
/*     */     }
/*  96 */     catch (ServletRequestBindingException ex) {
/*  97 */       return new int[0];
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
/*     */   public static int getRequiredIntParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 111 */     return INT_PARSER.parseInt(name, request.getParameter(name));
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
/*     */   public static int[] getRequiredIntParameters(ServletRequest request, String name) throws ServletRequestBindingException {
/* 124 */     return INT_PARSER.parseInts(name, request.getParameterValues(name));
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
/*     */   @Nullable
/*     */   public static Long getLongParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 141 */     if (request.getParameter(name) == null) {
/* 142 */       return null;
/*     */     }
/* 144 */     return Long.valueOf(getRequiredLongParameter(request, name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getLongParameter(ServletRequest request, String name, long defaultVal) {
/* 155 */     if (request.getParameter(name) == null) {
/* 156 */       return defaultVal;
/*     */     }
/*     */     try {
/* 159 */       return getRequiredLongParameter(request, name);
/*     */     }
/* 161 */     catch (ServletRequestBindingException ex) {
/* 162 */       return defaultVal;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long[] getLongParameters(ServletRequest request, String name) {
/*     */     try {
/* 173 */       return getRequiredLongParameters(request, name);
/*     */     }
/* 175 */     catch (ServletRequestBindingException ex) {
/* 176 */       return new long[0];
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
/*     */   public static long getRequiredLongParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 190 */     return LONG_PARSER.parseLong(name, request.getParameter(name));
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
/*     */   public static long[] getRequiredLongParameters(ServletRequest request, String name) throws ServletRequestBindingException {
/* 203 */     return LONG_PARSER.parseLongs(name, request.getParameterValues(name));
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
/*     */   @Nullable
/*     */   public static Float getFloatParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 220 */     if (request.getParameter(name) == null) {
/* 221 */       return null;
/*     */     }
/* 223 */     return Float.valueOf(getRequiredFloatParameter(request, name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float getFloatParameter(ServletRequest request, String name, float defaultVal) {
/* 234 */     if (request.getParameter(name) == null) {
/* 235 */       return defaultVal;
/*     */     }
/*     */     try {
/* 238 */       return getRequiredFloatParameter(request, name);
/*     */     }
/* 240 */     catch (ServletRequestBindingException ex) {
/* 241 */       return defaultVal;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static float[] getFloatParameters(ServletRequest request, String name) {
/*     */     try {
/* 252 */       return getRequiredFloatParameters(request, name);
/*     */     }
/* 254 */     catch (ServletRequestBindingException ex) {
/* 255 */       return new float[0];
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
/*     */   public static float getRequiredFloatParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 269 */     return FLOAT_PARSER.parseFloat(name, request.getParameter(name));
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
/*     */   public static float[] getRequiredFloatParameters(ServletRequest request, String name) throws ServletRequestBindingException {
/* 282 */     return FLOAT_PARSER.parseFloats(name, request.getParameterValues(name));
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
/*     */   @Nullable
/*     */   public static Double getDoubleParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 299 */     if (request.getParameter(name) == null) {
/* 300 */       return null;
/*     */     }
/* 302 */     return Double.valueOf(getRequiredDoubleParameter(request, name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double getDoubleParameter(ServletRequest request, String name, double defaultVal) {
/* 313 */     if (request.getParameter(name) == null) {
/* 314 */       return defaultVal;
/*     */     }
/*     */     try {
/* 317 */       return getRequiredDoubleParameter(request, name);
/*     */     }
/* 319 */     catch (ServletRequestBindingException ex) {
/* 320 */       return defaultVal;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double[] getDoubleParameters(ServletRequest request, String name) {
/*     */     try {
/* 331 */       return getRequiredDoubleParameters(request, name);
/*     */     }
/* 333 */     catch (ServletRequestBindingException ex) {
/* 334 */       return new double[0];
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
/*     */   public static double getRequiredDoubleParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 348 */     return DOUBLE_PARSER.parseDouble(name, request.getParameter(name));
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
/*     */   public static double[] getRequiredDoubleParameters(ServletRequest request, String name) throws ServletRequestBindingException {
/* 361 */     return DOUBLE_PARSER.parseDoubles(name, request.getParameterValues(name));
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
/*     */   @Nullable
/*     */   public static Boolean getBooleanParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 380 */     if (request.getParameter(name) == null) {
/* 381 */       return null;
/*     */     }
/* 383 */     return Boolean.valueOf(getRequiredBooleanParameter(request, name));
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
/*     */   public static boolean getBooleanParameter(ServletRequest request, String name, boolean defaultVal) {
/* 396 */     if (request.getParameter(name) == null) {
/* 397 */       return defaultVal;
/*     */     }
/*     */     try {
/* 400 */       return getRequiredBooleanParameter(request, name);
/*     */     }
/* 402 */     catch (ServletRequestBindingException ex) {
/* 403 */       return defaultVal;
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
/*     */   public static boolean[] getBooleanParameters(ServletRequest request, String name) {
/*     */     try {
/* 416 */       return getRequiredBooleanParameters(request, name);
/*     */     }
/* 418 */     catch (ServletRequestBindingException ex) {
/* 419 */       return new boolean[0];
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
/*     */   public static boolean getRequiredBooleanParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 436 */     return BOOLEAN_PARSER.parseBoolean(name, request.getParameter(name));
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
/*     */   public static boolean[] getRequiredBooleanParameters(ServletRequest request, String name) throws ServletRequestBindingException {
/* 452 */     return BOOLEAN_PARSER.parseBooleans(name, request.getParameterValues(name));
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
/*     */   @Nullable
/*     */   public static String getStringParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 468 */     if (request.getParameter(name) == null) {
/* 469 */       return null;
/*     */     }
/* 471 */     return getRequiredStringParameter(request, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getStringParameter(ServletRequest request, String name, String defaultVal) {
/* 482 */     String val = request.getParameter(name);
/* 483 */     return (val != null) ? val : defaultVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getStringParameters(ServletRequest request, String name) {
/*     */     try {
/* 493 */       return getRequiredStringParameters(request, name);
/*     */     }
/* 495 */     catch (ServletRequestBindingException ex) {
/* 496 */       return new String[0];
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
/*     */   public static String getRequiredStringParameter(ServletRequest request, String name) throws ServletRequestBindingException {
/* 510 */     return STRING_PARSER.validateRequiredString(name, request.getParameter(name));
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
/*     */   public static String[] getRequiredStringParameters(ServletRequest request, String name) throws ServletRequestBindingException {
/* 523 */     return STRING_PARSER.validateRequiredStrings(name, request.getParameterValues(name));
/*     */   }
/*     */   
/*     */   private static abstract class ParameterParser<T> {
/*     */     private ParameterParser() {}
/*     */     
/*     */     protected final T parse(String name, String parameter) throws ServletRequestBindingException {
/* 530 */       validateRequiredParameter(name, parameter);
/*     */       try {
/* 532 */         return doParse(parameter);
/*     */       }
/* 534 */       catch (NumberFormatException ex) {
/* 535 */         throw new ServletRequestBindingException("Required " + 
/* 536 */             getType() + " parameter '" + name + "' with value of '" + parameter + "' is not a valid number", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected final void validateRequiredParameter(String name, @Nullable Object parameter) throws ServletRequestBindingException {
/* 544 */       if (parameter == null)
/* 545 */         throw new MissingServletRequestParameterException(name, getType()); 
/*     */     }
/*     */     
/*     */     protected abstract String getType();
/*     */     
/*     */     protected abstract T doParse(String param1String) throws NumberFormatException;
/*     */   }
/*     */   
/*     */   private static class IntParser
/*     */     extends ParameterParser<Integer>
/*     */   {
/*     */     private IntParser() {}
/*     */     
/*     */     protected String getType() {
/* 559 */       return "int";
/*     */     }
/*     */ 
/*     */     
/*     */     protected Integer doParse(String s) throws NumberFormatException {
/* 564 */       return Integer.valueOf(s);
/*     */     }
/*     */     
/*     */     public int parseInt(String name, String parameter) throws ServletRequestBindingException {
/* 568 */       return parse(name, parameter).intValue();
/*     */     }
/*     */     
/*     */     public int[] parseInts(String name, String[] values) throws ServletRequestBindingException {
/* 572 */       validateRequiredParameter(name, values);
/* 573 */       int[] parameters = new int[values.length];
/* 574 */       for (int i = 0; i < values.length; i++) {
/* 575 */         parameters[i] = parseInt(name, values[i]);
/*     */       }
/* 577 */       return parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongParser
/*     */     extends ParameterParser<Long> {
/*     */     private LongParser() {}
/*     */     
/*     */     protected String getType() {
/* 586 */       return "long";
/*     */     }
/*     */ 
/*     */     
/*     */     protected Long doParse(String parameter) throws NumberFormatException {
/* 591 */       return Long.valueOf(parameter);
/*     */     }
/*     */     
/*     */     public long parseLong(String name, String parameter) throws ServletRequestBindingException {
/* 595 */       return parse(name, parameter).longValue();
/*     */     }
/*     */     
/*     */     public long[] parseLongs(String name, String[] values) throws ServletRequestBindingException {
/* 599 */       validateRequiredParameter(name, values);
/* 600 */       long[] parameters = new long[values.length];
/* 601 */       for (int i = 0; i < values.length; i++) {
/* 602 */         parameters[i] = parseLong(name, values[i]);
/*     */       }
/* 604 */       return parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FloatParser
/*     */     extends ParameterParser<Float> {
/*     */     private FloatParser() {}
/*     */     
/*     */     protected String getType() {
/* 613 */       return "float";
/*     */     }
/*     */ 
/*     */     
/*     */     protected Float doParse(String parameter) throws NumberFormatException {
/* 618 */       return Float.valueOf(parameter);
/*     */     }
/*     */     
/*     */     public float parseFloat(String name, String parameter) throws ServletRequestBindingException {
/* 622 */       return parse(name, parameter).floatValue();
/*     */     }
/*     */     
/*     */     public float[] parseFloats(String name, String[] values) throws ServletRequestBindingException {
/* 626 */       validateRequiredParameter(name, values);
/* 627 */       float[] parameters = new float[values.length];
/* 628 */       for (int i = 0; i < values.length; i++) {
/* 629 */         parameters[i] = parseFloat(name, values[i]);
/*     */       }
/* 631 */       return parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DoubleParser
/*     */     extends ParameterParser<Double> {
/*     */     private DoubleParser() {}
/*     */     
/*     */     protected String getType() {
/* 640 */       return "double";
/*     */     }
/*     */ 
/*     */     
/*     */     protected Double doParse(String parameter) throws NumberFormatException {
/* 645 */       return Double.valueOf(parameter);
/*     */     }
/*     */     
/*     */     public double parseDouble(String name, String parameter) throws ServletRequestBindingException {
/* 649 */       return parse(name, parameter).doubleValue();
/*     */     }
/*     */     
/*     */     public double[] parseDoubles(String name, String[] values) throws ServletRequestBindingException {
/* 653 */       validateRequiredParameter(name, values);
/* 654 */       double[] parameters = new double[values.length];
/* 655 */       for (int i = 0; i < values.length; i++) {
/* 656 */         parameters[i] = parseDouble(name, values[i]);
/*     */       }
/* 658 */       return parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class BooleanParser
/*     */     extends ParameterParser<Boolean> {
/*     */     private BooleanParser() {}
/*     */     
/*     */     protected String getType() {
/* 667 */       return "boolean";
/*     */     }
/*     */ 
/*     */     
/*     */     protected Boolean doParse(String parameter) throws NumberFormatException {
/* 672 */       return Boolean.valueOf((parameter.equalsIgnoreCase("true") || parameter.equalsIgnoreCase("on") || parameter
/* 673 */           .equalsIgnoreCase("yes") || parameter.equals("1")));
/*     */     }
/*     */     
/*     */     public boolean parseBoolean(String name, String parameter) throws ServletRequestBindingException {
/* 677 */       return parse(name, parameter).booleanValue();
/*     */     }
/*     */     
/*     */     public boolean[] parseBooleans(String name, String[] values) throws ServletRequestBindingException {
/* 681 */       validateRequiredParameter(name, values);
/* 682 */       boolean[] parameters = new boolean[values.length];
/* 683 */       for (int i = 0; i < values.length; i++) {
/* 684 */         parameters[i] = parseBoolean(name, values[i]);
/*     */       }
/* 686 */       return parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class StringParser
/*     */     extends ParameterParser<String> {
/*     */     private StringParser() {}
/*     */     
/*     */     protected String getType() {
/* 695 */       return "string";
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doParse(String parameter) throws NumberFormatException {
/* 700 */       return parameter;
/*     */     }
/*     */     
/*     */     public String validateRequiredString(String name, String value) throws ServletRequestBindingException {
/* 704 */       validateRequiredParameter(name, value);
/* 705 */       return value;
/*     */     }
/*     */     
/*     */     public String[] validateRequiredStrings(String name, String[] values) throws ServletRequestBindingException {
/* 709 */       validateRequiredParameter(name, values);
/* 710 */       for (String value : values) {
/* 711 */         validateRequiredParameter(name, value);
/*     */       }
/* 713 */       return values;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/kali/ctf/htb/fatty-10.10.10.174/ftp/fatty-client.jar!/org/springframework/web/bind/ServletRequestUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */