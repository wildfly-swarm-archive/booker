package org.wildfly.swarm.booker.store;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Bob McWhirter
 */
public class DynamicClassLoader extends URLClassLoader {

    public DynamicClassLoader(ClassLoader parent) {
        super( new URL[]{}, parent );
    }

    public Class<?> makeClass(String name, byte[] bytes) {
        return defineClass(name, bytes, 0, bytes.length );
    }
}
