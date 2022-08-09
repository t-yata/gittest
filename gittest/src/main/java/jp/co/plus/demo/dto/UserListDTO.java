package jp.co.plus.demo.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserListDTO{

	//ユーザー名
	private String log_id;

	//パスワード
	private String password;

	//シークレットキー
	private String secret_key;
	
	//作成日
	private Date create_date;
	
	//更新日
	private Date update_date;
	
	//ページングカウント
	private int pagingCount;
	
	//ページ表示件数
	private int userListLimit;

}
