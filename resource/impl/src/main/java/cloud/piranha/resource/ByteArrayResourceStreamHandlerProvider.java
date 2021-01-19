package cloud.piranha.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.spi.URLStreamHandlerProvider;
import java.util.function.Function;

public class ByteArrayResourceStreamHandlerProvider extends URLStreamHandlerProvider {
    
    private static InheritableThreadLocal<Function<String, InputStream>> localGetResourceAsStreamFunction = new InheritableThreadLocal<>();
    
    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (!"bytes".equals(protocol)) {
            return null;
        }
        
        return new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                return new URLConnection(u) {
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return localGetResourceAsStreamFunction.get().apply(u.toString());
                    }
                    
                    @Override
                    public void connect() throws IOException {
                        // Do nothing
                    }
                };
            }
        };
    }
    
    public static void setGetResourceAsStreamFunction(Function<String, InputStream> getResourceAsStreamFunction) {
        localGetResourceAsStreamFunction.set(getResourceAsStreamFunction);
    }

}
