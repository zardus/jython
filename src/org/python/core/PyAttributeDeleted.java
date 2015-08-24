// Copyright (c) Corporation for National Research Initiatives
package org.python.core;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.AccessControlException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jnr.posix.util.Platform;

import org.python.Version;
import org.python.core.adapter.ClassicPyObjectAdapter;
import org.python.core.adapter.ExtensiblePyObjectAdapter;
import org.python.core.packagecache.PackageManager;
import org.python.core.packagecache.SysPackageManager;
import org.python.expose.ExposedGet;
import org.python.expose.ExposedType;
import org.python.modules.Setup;
import org.python.util.Generic;


/**
 * Value of a class or instance variable when the corresponding attribute is deleted. Used only in
 * PySystemState for now.
 */
@Untraversable
class PyAttributeDeleted extends PyObject {

    final static PyAttributeDeleted INSTANCE = new PyAttributeDeleted();

    private PyAttributeDeleted() {}

    @Override
    public String toString() {
        return "";
    }

    @Override
    public Object __tojava__(Class c) {
        if (c == PyObject.class) {
            return this;
        }
        // we can't quite "delete" non-PyObject attributes; settle for
        // null or nothing
        if (c.isPrimitive()) {
            return Py.NoConversion;
        }
        return null;
    }
}
