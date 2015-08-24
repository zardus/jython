// Copyright (c) Corporation for National Research Initiatives
package org.python.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.google.common.base.CharMatcher;
import jline.console.UserInterruptException;
import jnr.constants.Constant;
import jnr.constants.platform.Errno;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;

import jnr.posix.util.Platform;
import org.python.antlr.base.mod;
import org.python.core.adapter.ClassicPyObjectAdapter;
import org.python.core.adapter.ExtensiblePyObjectAdapter;
import org.python.core.Traverseproc;
import org.python.core.Visitproc;
import org.python.core.PyObject;
import org.python.modules.posix.PosixModule;
import org.python.util.Generic;

/**
 * A function object wrapper for a java method which comply with the
 * PyArgsKeywordsCall standard.
 */
@Untraversable
public class JavaFunc extends PyObject {

    Method method;

    public JavaFunc(Method method) {
        this.method = method;
    }

    @Override
    public PyObject __call__(PyObject[] args, String[] kws) {
        Object[] margs = new Object[]{args, kws};
        try {
            return Py.java2py(method.invoke(null, margs));
        } catch (Throwable t) {
            throw Py.JavaError(t);
        }
    }

    @Override
    public PyObject _doget(PyObject container) {
        return _doget(container, null);
    }

    @Override
    public PyObject _doget(PyObject container, PyObject wherefound) {
        if (container == null) {
            return this;
        }
        return new PyMethod(this, container, wherefound);
    }

    public boolean _doset(PyObject container) {
        throw Py.TypeError("java function not settable: " + method.getName());
    }
}
