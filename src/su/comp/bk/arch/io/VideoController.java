/*
 * Created: 23.04.2012
 *
 * Copyright (C) 2012 Victor Antonovich (v.antonovich@gmail.com)
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package su.comp.bk.arch.io;

import su.comp.bk.arch.memory.RandomAccessMemory;
import android.graphics.Bitmap;
import android.graphics.Color;


/**
 * BK-0010 video output controller.
 */
public class VideoController implements Device {

    public final static int CONTROL_REGISTER_ADDRESS = 0177664;

    private final static int SCROLL_BASE_VALUE = 0330;
    private final static int SCROLL_EXTMEM_VALUE = 0230;

    private final static int EXTMEM_CONTROL_BIT = 01000;

    private final static int SCREEN_WIDTH_BW = 512;
    private final static int SCREEN_HEIGHT_FULL = 256;
    private final static int SCREEN_HEIGHT_EXTMEM = 64;
    private final static int SCREEN_SCANLINE_LENGTH = 040;
    private final static int SCREEN_BPP_BW = 1;
    private final static int SCREEN_BPW_BW = Short.SIZE / SCREEN_BPP_BW;

    private final static int[] PIXEL_TAB_BW = new int[8 * 256];

    private final static int[] addresses = { CONTROL_REGISTER_ADDRESS };

    private int scrollRegister;

    private final RandomAccessMemory videoMemory;

    private final Bitmap videoBuffer;

    static {
        int pixelTabIdx = 0;
        for (int videoBufferByte = 0; videoBufferByte < 256; videoBufferByte++) {
            for (int videoBufferBytePixel = 0; videoBufferBytePixel < 8; videoBufferBytePixel++) {
                PIXEL_TAB_BW[pixelTabIdx++] = (videoBufferByte & (1 << videoBufferBytePixel)) != 0
                        ? Color.WHITE : Color.BLACK;
            }
        }
    }

    public VideoController(RandomAccessMemory videoMemory) {
        this.videoMemory = videoMemory;
        this.videoBuffer = Bitmap.createBitmap(SCREEN_WIDTH_BW, SCREEN_HEIGHT_FULL,
                Bitmap.Config.ARGB_8888);
        writeScrollRegister(SCROLL_EXTMEM_VALUE);
    }

    public Bitmap getVideoBuffer() {
        return videoBuffer;
    }

    public Bitmap renderVideoBuffer() {
        short[] videoData = videoMemory.getData();
        videoBuffer.eraseColor(Color.BLACK);
        int videoDataOffset = isFullFrameMode() ? 0 : videoData.length
                - SCREEN_HEIGHT_EXTMEM * SCREEN_SCANLINE_LENGTH;
        int scrollShift = (readScrollRegister() - SCROLL_BASE_VALUE) & 0377;
        int videoBufferX;
        int videoBufferY;
        for (int videoDataIdx = videoDataOffset; videoDataIdx < videoData.length;
                videoDataIdx++) {
            int videoDataWord = videoData[videoDataIdx];
            if (videoDataWord != 0) {
                videoBufferX = (videoDataIdx % SCREEN_SCANLINE_LENGTH) * SCREEN_BPW_BW;
                videoBufferY = (videoDataIdx / SCREEN_SCANLINE_LENGTH - scrollShift)
                        & (SCREEN_HEIGHT_FULL - 1);
                videoBuffer.setPixels(PIXEL_TAB_BW, (videoDataWord & 0377) << 3,
                        SCREEN_WIDTH_BW, videoBufferX, videoBufferY, 8, 1);
                videoBufferX += 8;
                videoBuffer.setPixels(PIXEL_TAB_BW, ((videoDataWord >> 8) & 0377) << 3,
                        SCREEN_WIDTH_BW, videoBufferX, videoBufferY, 8, 1);
            }
        }
        return videoBuffer;
    }

    @Override
    public int[] getAddresses() {
        return addresses;
    }

    @Override
    public void init() {
        // Do nothing
    }

    private boolean isFullFrameMode() {
        return (readScrollRegister() & EXTMEM_CONTROL_BIT) != 0;
    }

    private void writeScrollRegister(int value) {
        if ((value & EXTMEM_CONTROL_BIT) != 0) {
            this.scrollRegister = EXTMEM_CONTROL_BIT | (value & 0377);
        } else {
            this.scrollRegister = SCROLL_EXTMEM_VALUE;
        }
    }

    private int readScrollRegister() {
        return this.scrollRegister;
    }

    @Override
    public int read(int address) {
        return readScrollRegister();
    }

    @Override
    public void write(boolean isByteMode, int address, int value) {
        int registerValue;
        if (isByteMode) {
            if ((address & 1) != 0) {
                registerValue = (value << 8) | (readScrollRegister() & 0377);
            } else {
                registerValue = (readScrollRegister() & 0177400) | (value & 0377);
            }
        } else {
            registerValue = value;
        }
        writeScrollRegister(registerValue);
    }

}
