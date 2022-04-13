package jp.co.plus.demo.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserListBean{

	//ユーザー名
	private String userName;

	//パスワード
	private String password;

	//シークレットキー
	private String secret;
	
	//作成日
	private Date createDate;
	
	//更新日
	private Date updateDate;
	
	//ページングカウント
	private int pagingCount;

}
