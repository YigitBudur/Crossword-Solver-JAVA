/*
 * Copyright (c) 2002-2020, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * https://opensource.org/licenses/BSD-3-Clause
 */
package org.jline.terminal.impl.jna.solaris;

import java.io.FileDescriptor;
import java.io.IOException;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.impl.jna.JnaNativePty;

import static org.jline.terminal.impl.jna.solaris.CLibrary.TCSANOW;
import static org.jline.terminal.impl.jna.solaris.CLibrary.TIOCGWINSZ;
import static org.jline.terminal.impl.jna.solaris.CLibrary.TIOCSWINSZ;
import static org.jline.terminal.impl.jna.solaris.CLibrary.termios;
import static org.jline.terminal.impl.jna.solaris.CLibrary.winsize;

public class SolarisNativePty extends JnaNativePty {

    private static final CLibrary C_LIBRARY = Native.load(Platform.C_LIBRARY_NAME, CLibrary.class);

    public static SolarisNativePty current() throws IOException {
        int slave = 0;
        byte[] buf = new byte[64];
        C_LIBRARY.ttyname_r(slave, buf, buf.length);
        int len = 0;
        while (buf[len] != 0) {
            len++;
        }
        String name = new String(buf, 0, len);
        return new SolarisNativePty(-1, null, slave, FileDescriptor.in, 1, FileDescriptor.out, name);
    }

    public static SolarisNativePty open(Attributes attr, Size size) throws IOException {
        int[] master = new int[1];
        int[] slave = new int[1];
        byte[] buf = new byte[64];
        C_LIBRARY.openpty(master, slave, buf,
                attr != null ? new termios(attr) : null,
                size != null ? new winsize(size) : null);
        int len = 0;
        while (buf[len] != 0) {
            len++;
        }
        String name = new String(buf, 0, len);
        return new SolarisNativePty(master[0], newDescriptor(master[0]), slave[0], newDescriptor(slave[0]), name);
    }

    public SolarisNativePty(int master, FileDescriptor masterFD, int slave, FileDescriptor slaveFD, String name) {
        super(master, masterFD, slave, slaveFD, name);
    }

    public SolarisNativePty(int master, FileDescriptor masterFD, int slave, FileDescriptor slaveFD, int slaveOut, FileDescriptor slaveOutFD, String name) {
        super(master, masterFD, slave, slaveFD, slaveOut, slaveOutFD, name);
    }

    @Override
    public Attributes getAttr() throws IOException {
        termios termios = new termios();
        C_LIBRARY.tcgetattr(getSlave(), termios);
        return termios.toAttributes();
    }

    @Override
    protected void doSetAttr(Attributes attr) throws IOException {
        termios termios = new termios(attr);
        C_LIBRARY.tcsetattr(getSlave(), TCSANOW, termios);
    }

    @Override
    public Size getSize() throws IOException {
        winsize sz = new winsize();
        C_LIBRARY.ioctl(getSlave(), TIOCGWINSZ, sz);
        return sz.toSize();
    }

    @Override
    public void setSize(Size size) throws IOException {
        winsize sz = new winsize(size);
        C_LIBRARY.ioctl(getSlave(), TIOCSWINSZ, sz);
    }

    public static int isatty(int fd) {
        return C_LIBRARY.isatty(fd);
    }

}
