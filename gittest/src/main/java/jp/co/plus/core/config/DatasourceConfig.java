package jp.co.plus.core.config;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;

/**
 * アプリケーション側JNDI設定に用いるクラス
 * DB接続の設定(tomcatの設定ファイルであるContextファイルに設定を追加する)をServerConfigで行い、
 * それを実行した結果をアプリケーション側で使えるようにするのがこのConfigの処理
 */
@Configuration
public class DatasourceConfig {

	//JNDIでDB接続する場合は下記の処理
	@Bean
	public DataSource jndiDataSource() throws IllegalArgumentException, NamingException {
		   JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
			bean.setJndiName("java:comp/env/jdbc/core_db");		//DBの接続情報を記述
			bean.setProxyInterface(DataSource.class);
			bean.setLookupOnStartup(false);
			bean.afterPropertiesSet();
			return (DataSource)bean.getObject();
	}
}