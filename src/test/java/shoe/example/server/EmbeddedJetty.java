package shoe.example.server;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import shoe.example.log.SystemLoggerFactory;
import shoe.example.metrics.InformEntriesAndExists;

import java.io.IOException;
import java.util.logging.Level;

public class EmbeddedJetty implements Runnable {
    public static final String CONTEXT_PATH = "/DipExample";
    public static final String SRC_MAIN_WEBAPP = "src/main/webapp";
    public static final String WEB_XML = "/WEB-INF/web.xml";
    public static final String host = "127.0.0.1";
    public static volatile Server server;

    public static void main(String[] args) throws IOException {
        SystemLoggerFactory.setLevel("org", Level.WARNING);
        SystemLoggerFactory.setLevel(EmbeddedJetty.class, Level.INFO);

        EmbeddedJetty jetty = new EmbeddedJetty();
        jetty.start();
        System.setProperty(InformEntriesAndExists.APP_NAME_PROPERTY, "DipExample");
        System.setProperty(InformEntriesAndExists.PORT_NAME_PROPERTY, "" + jetty.port());
        SystemLoggerFactory.get(EmbeddedJetty.class).info(jetty.applicationUrl());
        System.in.read();
        jetty.stop();
    }

    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
        while (server == null || !server.isStarted()) {
            try {
                Thread.sleep(100);
            } catch (java.lang.InterruptedException e) {
                // do nothing, we were shutting down anyway
            }
        }
    }

    public void stop() {
        try {
            if (server != null && server.isStarted()) {
                server.stop();
            }
        } catch (Exception e) {
            System.out.println("Failed to stop server - did it start?");
        }
    }

    public String applicationUrl() {
        return String.format("http://%s:%s%s", host, port(), CONTEXT_PATH);
    }

    public int port() {
        return server.getConnectors()[0].getLocalPort();
    }

    @Override
    public void run() {
        server = new Server(0);
        server.setHandler(buildWebAppContext());

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ResourceCollection buildResourceCollection() {
        return new ResourceCollection(new String[]{SRC_MAIN_WEBAPP});
    }

    private WebAppContext buildWebAppContext() {
        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath(CONTEXT_PATH);
        context.setBaseResource(buildResourceCollection());
        context.setDescriptor(SRC_MAIN_WEBAPP + WEB_XML);
        return context;
    }
}
