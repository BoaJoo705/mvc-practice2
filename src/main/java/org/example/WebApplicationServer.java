package org.example;

import org.apache.catalina.startup.Tomcat;
import org.slf4j.LoggerFactory;

import java.io.File;
import org.slf4j.Logger;

public class WebApplicationServer {

    private static final Logger log = LoggerFactory.getLogger(WebApplicationServer.class);

    public static void main(String[] args) throws Exception {
        String webappDirLocation = "webapps/";
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080); //8080포트 설정

        tomcat.addWebapp("/",new File(webappDirLocation).getAbsolutePath()); //webapp/경로를 바라보도록
        log.info("configuring app with basedir: {}", new File("./" + webappDirLocation).getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}
