package jp.co.plus.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.plus.core.security.LoginAccountHolder;
import jp.co.plus.demo.dto.EditUserDTO;
import jp.co.plus.demo.dto.UserListDTO;
import jp.co.plus.demo.mapper.UserMasterMapper;

@Service
public class UserMasterServiceImpl implements UserMasterService {

	@Autowired
	private UserMasterMapper userMasterMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

	List<?> errorMessageList;

	// ユーザー名取得の処理
	@Override
	public String getUserName() {

		// ユーザー名を取得
		String username = LoginAccountHolder.get().getUsername();
		// ユーザー名を返す
		return username;
	}

	// 更新回数取得の処理
	@Override
	public int getUpdateCount(EditUserDTO editUserDTO) {
		// 更新回数を取得する
		int UpdateCount = userMasterMapper.updateCount(editUserDTO);

		return UpdateCount;
	}

	// ユーザー情報更新の処理
	@Override
	@Transactional
	public String userupdate(EditUserDTO editUserDTO) throws Exception {

		// パスワードを暗号化する処理
		String password = passwordEncoder.encode(editUserDTO.getPassword());
		editUserDTO.setPassword(password);

		// ユーザー情報更新のSQL実行
		int resultCheck = userMasterMapper.updatePassword(editUserDTO);

		//更新処理が失敗した時はExceptionを返してロールバックする
		if (resultCheck == 0) {
			throw new Exception();
		}
		return null;
	}

	// 一覧表示の処理
	@Override
	public List<UserListDTO> userlist(UserListDTO UserListDTO) {

		// 一覧表示の結果を取得するSQLを実行
		List<UserListDTO> searchResult = userMasterMapper.userlist(UserListDTO);

		// 結果を返す
		return searchResult;
	}

	// ページングの処理
	@Override
	public List<UserListDTO> paging(UserListDTO UserListDTO) {

		// 一覧表示の結果を取得するSQLを実行
		List<UserListDTO> searchResult = userMasterMapper.paging(UserListDTO);

		// 結果を返す
		return searchResult;
	}

}
