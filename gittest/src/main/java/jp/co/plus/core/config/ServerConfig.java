package jp.co.plus.core.config;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * アプリケーション側JNDI設定に用いるクラス
 * TomcatのContextファイルに設定を追加するためのConfigファイル
 * 本来Tomcatを導入するとServerというプロジェクトができるが、Springだと内部にTomcatを持っているため
 * Contextファイルを直接編集できない。そのため、Configで上書きするかxmlを作成して
 * 指定されたファイルに配置することで、Tomcatの設定ファイルであるContextファイルを編集している
 */
@Configuration
public class ServerConfig {

	@Bean
	public ServletWebServerFactory tomcatFactory() {

		return new TomcatServletWebServerFactory() {
			@Override
			protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
				tomcat.enableNaming();
				return new TomcatWebServer(tomcat, getPort() >= 0);
			}
			@Override
			protected void postProcessContext(Context context) {
				ContextResource resource = new ContextResource();
				resource.setName("jdbc/core_db");
				//resource.setProperty("factory", "org.postgresql.xa.PGXADataSourceFactory");
				resource.setType("javax.sql.DataSource");
				resource.setAuth("container");
				resource.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
				resource.setProperty("url", "jdbc:mysql://localhost:3306/core");
				resource.setProperty("username", "root");
				resource.setProperty("password", "plus-bm#1");
				//				resource.setProperty("loginTimeout", "0");
				context.getNamingResources().addResource(resource);
				System.out.println("resource:" + resource);
			}
		};

	}

}