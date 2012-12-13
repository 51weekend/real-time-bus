/*
 * Copyright (c) 2010 Shanda Corporation. All rights reserved.
 *
 * Created on 2011-09-10.
 */

package com.bustime.common.logger;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Locale;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;

/**
 * DailyRollingFileAppenderWithDate.
 * 
 * <p>Description: 
 * 
 * 此类代码拷贝自 log4j 的 DailyRollingFileAppender，修改的地方： activateOptions()和rollOver()
 * 
 * 在当天的日志文件名就直接增加日期后缀，而不是等到时间过了再改名；主要是为了日志文件的采集。
 * 
 * 原来的DailyRollingFileAppender日志文件采集会碰到问题：
 * 
 * 1) 增量采集日志内容时如果一直打开文件，会导致log4j日志文件切换失败;
 * 2) DailyRollingFileAppender是lazy切换日志文件名；如果某一天刚好没有日志输出，前一天的日志文件则一直不会做改名操作, 采集处理非常麻烦；
 * 
 * 
 * </p>
 * @author chengdong
 * 
 */
public class DailyRollingFileAppenderWithDate extends FileAppender {

    // The code assumes that the following constants are in a increasing
    // sequence.
    static final int TOP_OF_TROUBLE = -1;
    static final int TOP_OF_MINUTE = 0;
    static final int TOP_OF_HOUR = 1;
    static final int HALF_DAY = 2;
    static final int TOP_OF_DAY = 3;
    static final int TOP_OF_WEEK = 4;
    static final int TOP_OF_MONTH = 5;

    /**
       The date pattern. By default, the pattern is set to
       "'.'yyyy-MM-dd" meaning daily rollover.
     */
    private String datePattern = "yyyy-MM-dd";

    /**
       The log file will be renamed to the value of the
       scheduledFilename variable when the next interval is entered. For
       example, if the rollover period is one hour, the log file will be
       renamed to the value of "scheduledFilename" at the beginning of
       the next hour. 

       The precise time when a rollover occurs depends on logging
       activity. 
    */
    private String scheduledFilename;

    /**
       The next time we estimate a rollover should occur. */
    private long nextCheck = System.currentTimeMillis() - 1;

    Date now = new Date();

    SimpleDateFormat sdf;

    RollingCalendar rc = new RollingCalendar();

    String baseFileName;

    int checkPeriod = TOP_OF_TROUBLE;

    // The gmtTimeZone is used only in computeCheckPeriod() method.
    static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

    /**
       The default constructor does nothing. */
    public DailyRollingFileAppenderWithDate() {
    }

    /**
     * Instantiate a <code>DailyRollingFileAppender</code> and open the
     * file designated by <code>filename</code>. The opened filename will
     * become the ouput destination for this appender.
     * @param layout layout
     * @param filename filename
     * @param datePattern datePattern
     * @throws IOException exception
     */
    public DailyRollingFileAppenderWithDate(Layout layout, String filename, String datePattern) throws IOException {
        super(layout, filename, true);
        this.datePattern = datePattern;
        activateOptions();
    }

    /**
     *  The <b>DatePattern</b> takes a string in the same format as
     *  expected by {@link SimpleDateFormat}. This options determines the
     *  rollover schedule.
     *  @param pattern pattern
     */
    public void setDatePattern(String pattern) {
        datePattern = pattern;
    }

    /**
     * the value of the <b>DatePattern</b> option.
     * @return DatePattern
     */
    public String getDatePattern() {
        return datePattern;
    }

    /**
     * activateOptions.
     */
    public void activateOptions() {

        /* 原来的代码 */

        /*
         * super.activateOptions(); if(datePattern != null && fileName != null)
         * { now.setTime(System.currentTimeMillis()); sdf = new
         * SimpleDateFormat(datePattern); int type = computeCheckPeriod();
         * printPeriodicity(type); rc.setType(type); File file = new
         * File(fileName); scheduledFilename = fileName+sdf.format(new
         * Date(file.lastModified()));
         * 
         * } else { LogLog.error(
         * "Either File or DatePattern options are not set for appender ["
         * +name+"]."); }
         */

        /* 修改后的代码 */

        // super.activateOptions(); // 不可调用

        if (datePattern != null && fileName != null) {
            now.setTime(System.currentTimeMillis());
            sdf = new SimpleDateFormat(datePattern);
            int type = computeCheckPeriod();
            printPeriodicity(type);
            rc.setType(type);

            baseFileName = fileName; // baseFileName是新增属性，fileName在调用setFile会被修改掉
            int index = baseFileName.lastIndexOf("/");
            scheduledFilename = baseFileName.substring(0, index)+ "/" + sdf.format(now)+ baseFileName.substring(index); // 改成当天
            
            File target = new File(scheduledFilename);
            if (!target.exists()) {
                String parentName = target.getParent();
                File parentDir = new File(parentName);
                if (!parentDir.exists() && !parentDir.mkdirs()) {
                    errorHandler.error("create the dirs and file of log error." + target.getName());
                }
            }

            try {
                setFile(scheduledFilename, fileAppend, bufferedIO, bufferSize);
            } catch (java.io.IOException e) {
                errorHandler.error("setFile(" + scheduledFilename + ") call failed.");
            }

        } else {
            LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");
        }

    }

    /**
     * printPeriodicity.
     * @param type type
     */
    void printPeriodicity(int type) {
        switch (type) {
        case TOP_OF_MINUTE:
            LogLog.debug("Appender [" + name + "] to be rolled every minute.");
            break;
        case TOP_OF_HOUR:
            LogLog.debug("Appender [" + name + "] to be rolled on top of every hour.");
            break;
        case HALF_DAY:
            LogLog.debug("Appender [" + name + "] to be rolled at midday and midnight.");
            break;
        case TOP_OF_DAY:
            LogLog.debug("Appender [" + name + "] to be rolled at midnight.");
            break;
        case TOP_OF_WEEK:
            LogLog.debug("Appender [" + name + "] to be rolled at start of week.");
            break;
        case TOP_OF_MONTH:
            LogLog.debug("Appender [" + name + "] to be rolled at start of every month.");
            break;
        default:
            LogLog.warn("Unknown periodicity for appender [" + name + "].");
        }
    }

    // This method computes the roll over period by looping over the
    // periods, starting with the shortest, and stopping when the r0 is
    // different from from r1, where r0 is the epoch formatted according
    // the datePattern (supplied by the user) and r1 is the
    // epoch+nextMillis(i) formatted according to datePattern. All date
    // formatting is done in GMT and not local format because the test
    // logic is based on comparisons relative to 1970-01-01 00:00:00
    // GMT (the epoch).
    /**
     * computeCheckPeriod.
     * @return period
     */
    int computeCheckPeriod() {
        RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone, Locale.getDefault());
        // set sate to 1970-01-01 00:00:00 GMT
        Date epoch = new Date(0);
        if (datePattern != null) {
            for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
                simpleDateFormat.setTimeZone(gmtTimeZone); // do all date
                                                           // formatting in GMT
                String r0 = simpleDateFormat.format(epoch);
                rollingCalendar.setType(i);
                Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
                String r1 = simpleDateFormat.format(next);
                if (r0 != null && r1 != null && !r0.equals(r1)) {
                    return i;
                }
            }
        }
        return TOP_OF_TROUBLE; // Deliberately head for trouble...
    }

    /**
       Rollover the current file to a new file.
       @exception IOException io exception
    */
    void rollOver() throws IOException {

        /* Compute filename, but only if datePattern is specified */
        if (datePattern == null) {
            errorHandler.error("Missing DatePattern option in rollOver().");
            return;
        }

        /* 原来的代码 */

        /*
         * 
         * String datedFilename = fileName+sdf.format(now); // It is too early
         * to roll over because we are still within the // bounds of the current
         * interval. Rollover will occur once the // next interval is reached.
         * if (scheduledFilename.equals(datedFilename)) { return; }
         * 
         * // close current file, and rename it to datedFilename
         * this.closeFile();
         * 
         * File target = new File(scheduledFilename); if (target.exists()) {
         * target.delete(); }
         * 
         * File file = new File(fileName); boolean result =
         * file.renameTo(target); if(result) { LogLog.debug(fileName +" -> "+
         * scheduledFilename); } else {
         * LogLog.error("Failed to rename ["+fileName
         * +"] to ["+scheduledFilename+"]."); }
         * 
         * try { // This will also close the file. This is OK since multiple //
         * close operations are safe. this.setFile(fileName, true,
         * this.bufferedIO, this.bufferSize); } catch(IOException e) {
         * errorHandler.error("setFile("+fileName+", true) call failed."); }
         * scheduledFilename = datedFilename;
         */

        /* 修改后的代码 */
        int index = baseFileName.lastIndexOf("/");
        String datedFilename = baseFileName.substring(0, index) + "/" + sdf.format(now) + baseFileName.substring(index);
        
        if (scheduledFilename.equals(datedFilename)) {
            return;
        }

        this.closeFile();

        scheduledFilename = datedFilename;
        File target = new File(scheduledFilename);
        if (!target.exists()) {
            String parentName = target.getParent();
            File parentDir = new File(parentName);
            if (!parentDir.exists() && !parentDir.mkdirs()) {
                errorHandler.error("create the dirs and file of log error." + target.getName());
            }
            target.createNewFile();
        }

        try {
            this.setFile(scheduledFilename, true, this.bufferedIO, this.bufferSize);
        } catch (IOException e) {
            errorHandler.error("setFile(" + scheduledFilename + ", true) call failed.", e, ErrorCode.FILE_OPEN_FAILURE);
        }

    }

    /**
     * This method differentiates DailyRollingFileAppender from its
     * super class.
     *
     * <p>Before actually logging, this method will check whether it is
     * time to do a rollover. If it is, it will schedule the next
     * rollover time and then rollover.
     * @param event event
     * */
    protected void subAppend(LoggingEvent event) {
        long n = System.currentTimeMillis();
        if (n >= nextCheck) {
            now.setTime(n);
            nextCheck = rc.getNextCheckMillis(now);
            try {
                rollOver();
            } catch (IOException ioe) {
                if (ioe instanceof InterruptedIOException) {
                    Thread.currentThread().interrupt();
                }
                LogLog.error("rollOver() failed.", ioe);
            }
        }
        try {
            super.subAppend(event);
        } catch (Exception e) {
            errorHandler.error("super.subAppend error.", e, ErrorCode.FILE_OPEN_FAILURE);
        }
    }
}

/**
 *  RollingCalendar is a helper class to DailyRollingFileAppenderWithDate.
 *  Given a periodicity type and the current time, it computes the
 *  start of the next interval.  
 *  @author unknow
 * */
class RollingCalendar extends GregorianCalendar {
    private static final long serialVersionUID = -3560331770601814177L;

    int type = DailyRollingFileAppenderWithDate.TOP_OF_TROUBLE;

    /**
     * 构造函数.
     */
    RollingCalendar() {
        super();
    }

    /**
     * 构造函数.
     * @param tz tz
     * @param locale locale
     */
    RollingCalendar(TimeZone tz, Locale locale) {
        super(tz, locale);
    }

    void setType(int type) {
        this.type = type;
    }

    /**
     * todo.
     * @param now time
     * @return time
     */
    public long getNextCheckMillis(Date now) {
        return getNextCheckDate(now).getTime();
    }

    /**
     * todo.
     * @param now time
     * @return date
     */
    public Date getNextCheckDate(Date now) {
        this.setTime(now);

        switch (type) {
        case DailyRollingFileAppenderWithDate.TOP_OF_MINUTE:
            this.set(Calendar.SECOND, 0);
            this.set(Calendar.MILLISECOND, 0);
            this.add(Calendar.MINUTE, 1);
            break;
        case DailyRollingFileAppenderWithDate.TOP_OF_HOUR:
            this.set(Calendar.MINUTE, 0);
            this.set(Calendar.SECOND, 0);
            this.set(Calendar.MILLISECOND, 0);
            this.add(Calendar.HOUR_OF_DAY, 1);
            break;
        case DailyRollingFileAppenderWithDate.HALF_DAY:
            this.set(Calendar.MINUTE, 0);
            this.set(Calendar.SECOND, 0);
            this.set(Calendar.MILLISECOND, 0);
            int hour = get(Calendar.HOUR_OF_DAY);
            if (hour < 12) {
                this.set(Calendar.HOUR_OF_DAY, 12);
            } else {
                this.set(Calendar.HOUR_OF_DAY, 0);
                this.add(Calendar.DAY_OF_MONTH, 1);
            }
            break;
        case DailyRollingFileAppenderWithDate.TOP_OF_DAY:
            this.set(Calendar.HOUR_OF_DAY, 0);
            this.set(Calendar.MINUTE, 0);
            this.set(Calendar.SECOND, 0);
            this.set(Calendar.MILLISECOND, 0);
            this.add(Calendar.DATE, 1);
            break;
        case DailyRollingFileAppenderWithDate.TOP_OF_WEEK:
            this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
            this.set(Calendar.HOUR_OF_DAY, 0);
            this.set(Calendar.MINUTE, 0);
            this.set(Calendar.SECOND, 0);
            this.set(Calendar.MILLISECOND, 0);
            this.add(Calendar.WEEK_OF_YEAR, 1);
            break;
        case DailyRollingFileAppenderWithDate.TOP_OF_MONTH:
            this.set(Calendar.DATE, 1);
            this.set(Calendar.HOUR_OF_DAY, 0);
            this.set(Calendar.MINUTE, 0);
            this.set(Calendar.SECOND, 0);
            this.set(Calendar.MILLISECOND, 0);
            this.add(Calendar.MONTH, 1);
            break;
        default:
            throw new IllegalStateException("Unknown periodicity type.");
        }
        return getTime();
    }
}
