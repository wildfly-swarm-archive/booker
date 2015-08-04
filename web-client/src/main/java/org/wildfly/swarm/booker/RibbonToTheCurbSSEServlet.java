package org.wildfly.swarm.booker;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wildfly.swarm.netflix.ribbon.RibbonTopology;
import org.wildfly.swarm.netflix.ribbon.RibbonTopologyListener;

/**
 * @author Bob McWhirter
 */
@WebServlet( urlPatterns = { "/system/ribbon/stream"}, asyncSupported = true)
public class RibbonToTheCurbSSEServlet extends HttpServlet {

    private RibbonTopology topology;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        try {
            InitialContext context = new InitialContext();
            this.topology = (RibbonTopology) context.lookup("jboss/ribbon/cluster");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new ServletException();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");

        AsyncContext asyncContext = req.startAsync();
        PrintWriter writer = resp.getWriter();


        RibbonTopologyListener topologyListener = new RibbonTopologyListener() {
            @Override
            public void onChange(RibbonTopology topology) {
                String json = topologyToJson();
                writer.write( "data: " + json );
                writer.flush();
            }
        };

        asyncContext.setTimeout(0);
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent asyncEvent) throws IOException {
                RibbonToTheCurbSSEServlet.this.topology.removeListener( topologyListener );
            }

            @Override
            public void onTimeout(AsyncEvent asyncEvent) throws IOException {
                RibbonToTheCurbSSEServlet.this.topology.removeListener( topologyListener );
            }

            @Override
            public void onError(AsyncEvent asyncEvent) throws IOException {
                RibbonToTheCurbSSEServlet.this.topology.removeListener( topologyListener );
            }

            @Override
            public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
            }
        } );


        this.topology.addListener( topologyListener );
        String json = topologyToJson();
        writer.write( "data: " + json );
        writer.flush();

    }

    protected String topologyToJson() {
        StringBuilder json = new StringBuilder();

        json.append( "{" );

        Map<String, List<String>> map = this.topology.asMap();

        Set<String> keys = map.keySet();
        Iterator<String> keyIter = keys.iterator();

        while ( keyIter.hasNext() ) {
            String key = keyIter.next();
            json.append( "  " ).append( '"' ).append( key ).append( '"' ).append(": [");
            List<String> list = map.get(key);
            Iterator<String> listIter = list.iterator();
            while ( listIter.hasNext() ) {
                String server = listIter.next();
                json.append("    " ).append( '"' ).append( server ).append('"');
                if ( listIter.hasNext() ) {
                    json.append( ", " );
                }
                json.append( "" );
            }

            json.append( "  ]" );
            if ( keyIter.hasNext() ) {
                json.append( "," );
            }
            json.append( "" );
        }

        json.append( "}\n\n" );

        return json.toString();
    }
}
