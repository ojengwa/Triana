/*
 * The University of Wales, Cardiff Triana Project Software License (Based
 * on the Apache Software License Version 1.1)
 *
 * Copyright (c) 2007 University of Wales, Cardiff. All rights reserved.
 *
 * Redistribution and use of the software in source and binary forms, with
 * or without modification, are permitted provided that the following
 * conditions are met:
 *
 * 1.  Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 * 2.  Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 * 3. The end-user documentation included with the redistribution, if any,
 *    must include the following acknowledgment: "This product includes
 *    software developed by the University of Wales, Cardiff for the Triana
 *    Project (http://www.trianacode.org)." Alternately, this
 *    acknowledgment may appear in the software itself, if and wherever
 *    such third-party acknowledgments normally appear.
 *
 * 4. The names "Triana" and "University of Wales, Cardiff" must not be
 *    used to endorse or promote products derived from this software
 *    without prior written permission. For written permission, please
 *    contact triana@trianacode.org.
 *
 * 5. Products derived from this software may not be called "Triana," nor
 *    may Triana appear in their name, without prior written permission of
 *    the University of Wales, Cardiff.
 *
 * 6. This software may not be sold, used or incorporated into any product
 *    for sale to third parties.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN
 * NO EVENT SHALL UNIVERSITY OF WALES, CARDIFF OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 *
 * ------------------------------------------------------------------------
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Triana Project. For more information on the
 * Triana Project, please see. http://www.trianacode.org.
 *
 * This license is based on the BSD license as adopted by the Apache
 * Foundation and is governed by the laws of England and Wales.
 *
 */

package triana.types.clipins;

import org.trianacode.taskgraph.clipin.AttachInfo;
import org.trianacode.taskgraph.clipin.ClipIn;

import java.util.ArrayList;

/**
 * A clip-in which records the amount of time spent in each task it passes
 * through
 *
 * @author      Ian Wang
 * @created     11th August 2003
 * @version     $Revision: 4048 $
 * @date        $Date: 2007-10-08 16:38:22 +0100 (Mon, 08 Oct 2007) $ modified by $Author: spxmss $
 * @todo
 */

public class TimerClipIn implements ClipIn {

    public static final String TIMER_CLIPIN_TAG = "Timer";

    /**
     * the overall start time of this clip-in
     */
    private long start = -1;

    /**
     * the overall end time of this clip-in
     */
    private long end = -1;

    /**
     * the start time of this clip-in at its current task (or -1 if not
     * initialized at a task)
     */
    private long curstart = -1;

    /**
     * a list of all the timing info from each task
     */
    private ArrayList timeinfo = new ArrayList();


    public TimerClipIn() {
        start = System.currentTimeMillis();
    }

    public TimerClipIn(TimerClipIn clipin) {
        start = clipin.getStartTime();
        curstart = clipin.getCurrentStartTime();

        TimerInfo[] tinfo = clipin.getTimerInfo();

        for (int count = 0; count < tinfo.length; count++)
            timeinfo.add(tinfo[count]);
    }

    /**
     * @return the overall start time of this clipin
     */
    public long getStartTime() {
        return start;
    }

    /**
     * @return the overall end time of this clipin (or -1 if not finished)
     */
    public long getEndTime() {
        return end;
    }

    /**
     * @return the overall duration of the clip-in (or -1 if not finished)
     */
    public long getDuration() {
        if (end == -1)
            return -1;
        else
            return end - start;
    }


    /**
     * @return the start time of this clip-in at its current task (or -1 if
     * not initialized at a task)
     */
    public long getCurrentStartTime() {
        return curstart;
    }

    /**
     * @return an array of the timer infos generated by this clipin
     */
    public TimerInfo[] getTimerInfo() {
        return (TimerInfo[]) timeinfo.toArray(new TimerInfo[timeinfo.size()]);
    }

    /**
     * @return an array of the timer infos generated by this clip-in sorted
     * by start time
     */
    public TimerInfo[] getSortedTimerInfo() {
        TimerInfo[] info = getTimerInfo();
        ArrayList sorted = new ArrayList();
        int pos = 0;

        for (int count = 0; count < info.length; count++) {
            while ((pos < sorted.size()) && (info[count].getStartTime() > ((TimerInfo) sorted.get(pos)).getStartTime()))
                    pos++;

            sorted.add(pos, info[count]);
        }

        return (TimerInfo[]) sorted.toArray(new TimerInfo[sorted.size()]);
    }


    /**
     * This method is called before the clip-in enters a task's
     * clip-in bucket. This occurs when either the data it is attached
     * to is input by the task, or when the unit directly adds the
     * clip-in to its bucket.
     *
     * @param info info about the task the clip-in is being attached to
     */
    public void initializeAttach(AttachInfo info) {
        if (info.getClipInBucket().isClipInName(TIMER_CLIPIN_TAG)) {
            TimerClipIn clipin = (TimerClipIn) info.getClipInBucket().getClipIn(TIMER_CLIPIN_TAG);
            TimerInfo[] tinfo = clipin.getTimerInfo();

            for (int count = 0; count < tinfo.length; count++)
                timeinfo.add(tinfo[count]);

            curstart = clipin.getCurrentStartTime();
        } else
            curstart = System.currentTimeMillis();
    }

    /**
     * This method is called when the clip-in is removed from a
     * task's clip-in bucket. This occurs when either the data it is
     * attached to is output by the task, or when the unit directly
     * remove the clip-in from its bucket.
     *
     * @param info info about the task the clip-in is being removed from
     */
    public void finalizeAttach(AttachInfo info) {
        timeinfo.add(new TimerInfo(info.getTaskInterface(), curstart, System.currentTimeMillis()));
        end = System.currentTimeMillis();
        curstart = -1;
    }

    /**
     * Clones the ClipIn to an identical one. This is a copy by value,
     * not by reference. This method must be implemented for each class
     * in a way that depends on the contents of the ClipIn.
     *
     * @return a copy by value of the current ClipIn
     */
    public Object clone() {
        return new TimerClipIn(this);
    }

}
