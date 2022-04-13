package jp.co.plus.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jp.co.plus.core.mfa.MFAAuthenticationConfigurer;
import jp.co.plus.core.mfa.MFAWebAuthenticationDetails;
import jp.co.plus.core.service.UserDetailsServiceImpl;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    UserDetailsServiceImpl userDetailsService;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		MFAAuthenticationConfigurer configurer = new MFAAuthenticationConfigurer(userDetailsService)
				.passwordEncoder(passwordEncoder());
		auth.apply(configurer);
	}


    @Bean //パスワードのエンコード
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override //securityの設定をする
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()//ルール、アクセスポリシーの設定
            .antMatchers("/login").permitAll()//loginは認証なしでaccessできる
            .anyRequest().authenticated()//↑以外のすべてのURLリクエストをloginしないと見れない
            .and()
            .formLogin()//ログインの設定
            .authenticationDetailsSource(MFAWebAuthenticationDetails::new)	// セキュリティチェック設定
//            .authenticationDetailsSource(req -> new MFAWebAuthenticationDetails(req))	// セキュリティチェック設定
            .loginPage("/login")
            .defaultSuccessUrl("/auth", true)//ログインが成功したら/helloにいく
            .and()
            .logout()//ログアウトの設定
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));//logoutのURLを/logoutにする
    }
}
