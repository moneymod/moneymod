package me.sleepy.loader;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author yoursleep
 * @since 07 November 2021
 */
public class Load {
    public Load(URL url) throws IOException {
        DifferentClassLoader DIFFERENTCLASSLOADER = new DifferentClassLoader(this.getClass().getClassLoader());

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        // i took my user agent (whatever)
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36 OPR/80.0.4170.61");
        InputStream originalIn = connection.getInputStream();

        ZipInputStream in = new ZipInputStream(originalIn);
        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null)
        {
            String name = entry.getName();

            name = name.substring(0, name.length() - 6);
            name = name.replace('/', '.');

            ByteArrayOutputStream streamBuilder = new ByteArrayOutputStream();
            int bytesRead;
            byte[] tempBuffer = new byte[16384];
            while ((bytesRead = in.read(tempBuffer)) != -1)
                streamBuilder.write(tempBuffer, 0, bytesRead);

            if (name.endsWith(".class")) DIFFERENTCLASSLOADER.loadClass(name, streamBuilder.toByteArray());
            else DIFFERENTCLASSLOADER.loadResource(name, streamBuilder.toByteArray());
        }

        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.moneymod.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
    }

    private static class DifferentClassLoader extends ClassLoader {
        private final Map<String, Class> classes;
        private final Map<String, byte[]> resources;

        public DifferentClassLoader(ClassLoader classLoader) {
            // this classloader is going to be Launch.classLoader if nothing is broken
            // if we will not specify parent class loader then we will fuck up because minecraft classes will be loaded twice (lol wtf)
            super(classLoader);

            this.classes = new HashMap<>();
            this.resources = new HashMap<>();
        }

        public void loadClass(String name, byte[] data) {
            classes.put(name, defineClass(name, data, 0, data.length));
        }

        public void loadResource(String name, byte[] data) {
            resources.put(name, data);
        }

        // loading resources lol
        @Override @Nullable public InputStream getResourceAsStream(String name) {
            return resources.containsKey(name) ? new ByteArrayInputStream(resources.get(name)) : super.getResourceAsStream(name);
        }

        // don't delete this
        // it will fuck up all the loading shit
        // loadClass() does findClass(name) and if we will not return our shit then fuck up
        @Override public Class<?> findClass(String name) throws ClassNotFoundException {
            return name.toLowerCase().contains("moneymod") && classes.get(name) != null ? classes.get(name) : super.findClass(name);
        }
    }
}
