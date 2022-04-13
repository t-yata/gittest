package jp.co.plus.core.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.plus.core.dto.SessionBean;
import jp.co.plus.core.mfa.MFAAuthenticationProvider;
import jp.co.plus.core.mfa.MFAAuthenticatorUtil;
import jp.co.plus.core.service.LoginAccountDetails;
import jp.co.plus.core.service.UserService;

@Controller
public class SecurityController {

	@Autowired
	private SessionBean sessionBean;
	@Autowired
	private UserService service;


    @GetMapping("/login")
    public String login() {
        return "login";		//loginページを表示
    }

    @GetMapping("/auth") // 認証OK時呼び出し
    public String auth(Authentication loginUser,Model model) throws Exception {

		String username = loginUser.getName();

		// MFA認証キー作成（すでにMFA登録済みの場合はDBより取得
		String secret = this.getSecret(loginUser);
		if (secret == null) {
			secret = MFAAuthenticatorUtil.generateSecretKey();
		}
	    sessionBean.setSecret(secret);

        model.addAttribute("username",loginUser.getName());
	    model.addAttribute("dataUrl", this.createQrcodeData(username,secret));

        return "menu";
    }

    @PostMapping("/regist")
    public String regist(Authentication loginUser,Model model,@RequestParam String totpkey) throws Exception {

		String username =loginUser.getName();
		String secret = sessionBean.getSecret();

		boolean isError = false;
		try {
			if (!StringUtils.hasText(totpkey)) {
				totpkey = "0";
			}
			if (!MFAAuthenticationProvider.verifyCode(secret, new Integer(totpkey), 2)) {
				isError = true;
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			e.printStackTrace();
			isError = true;
		}

		// エラーの場合は再度表示
		if (isError) {
	        model.addAttribute("username",loginUser.getName());
		    model.addAttribute("dataUrl", this.createQrcodeData(username,secret));
	        model.addAttribute("msg","MFA Code verify failed");
	        return "auth";
		}

		service.updateSecret(username, secret);
        model.addAttribute("msg","登録");

        return "result";
    }

    @PostMapping("/cancel")
    public String cancel(Authentication loginUser,Model model) {

		String username =loginUser.getName();
		String secret = this.getSecret(loginUser);
		if (secret != null) {
			service.updateSecret(username, null);
		}
        model.addAttribute("msg","解除");

        return "result";
    }
    
    public String getSecret(Authentication loginUser) {
		String secret = null;
		if (loginUser.getPrincipal() instanceof LoginAccountDetails) {
			secret = ((LoginAccountDetails) loginUser.getPrincipal()).getAccount().getSecret();
		}
		return secret;

    }

    public String createQrcodeData(String username, String secret) throws Exception {

    	final String APP_NAME = "XXシステム";
		String url = String.format("otpauth://totp/%s/?secret=%s&issuer=%s", username, secret, URLEncoder.encode(APP_NAME,"UTF-8"));
		String encodeUrl = URLEncoder.encode(url,"UTF-8");
		URL imageUrl = new URL("https://chart.googleapis.com/chart?cht=qr&chs=180x180&chld=L|0&chl="+encodeUrl);

		BufferedImage buffer = ImageIO.read(imageUrl);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BufferedOutputStream os = new BufferedOutputStream(bos);
		buffer.flush();
		ImageIO.write( buffer, "png", os ); // png型
		String encodedStr64 = Base64.encodeBase64String(bos.toByteArray());
		return encodedStr64;

    }

 }

