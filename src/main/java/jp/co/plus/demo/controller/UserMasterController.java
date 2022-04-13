package jp.co.plus.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.plus.core.controller.SecurityController;
import jp.co.plus.core.dto.SessionBean;
import jp.co.plus.core.mfa.MFAAuthenticatorUtil;
import jp.co.plus.demo.constants.Constants;
import jp.co.plus.demo.dto.EditUserDTO;
import jp.co.plus.demo.dto.UserListBean;
import jp.co.plus.demo.dto.UserListDTO;
import jp.co.plus.demo.form.EditUserForm;
import jp.co.plus.demo.form.UserListForm;
import jp.co.plus.demo.service.UserMasterService;
import jp.co.plus.demo.validator.EditUserFormValidator;

@Controller
@RequestMapping("usermaster")
public class UserMasterController {

	@Autowired
	private SessionBean sessionBean;
	@Autowired
	private SecurityController securityController;
	@Autowired
	private UserMasterService userMasterService;
	@Autowired
	protected EditUserFormValidator doValidate;

	@InitBinder("editUserForm")
	public void editUserFormValidatorBinder(WebDataBinder binder) {
		binder.addValidators(doValidate);
	}

	// 編集ページを表示
	@RequestMapping(params = "useredit", method = RequestMethod.POST)
	public String edit(@ModelAttribute EditUserForm editUserForm, Model model) {
		System.out.println("editUser");

		// Formにユーザー名をセット
		editUserForm.setUserName(userMasterService.getUserName());

		// editUserDTOにユーザー名をセットする
		EditUserDTO editUserDTO = new EditUserDTO();
		editUserDTO.setLog_id(userMasterService.getUserName());

		// 更新回数を取得してフォームにセット
		editUserForm.setUpdateCount(userMasterService.getUpdateCount(editUserDTO));

		// ユーザー情報更新画面を表示
		return "useredit";
	}

	// 更新処理
	@RequestMapping(params = "editUser", method = RequestMethod.POST)
	public String update(@Validated @ModelAttribute EditUserForm editUserForm, BindingResult result, Model model,
			Errors errors) {

		// エラーがあった場合は編集画面にメッセージをセットして遷移
		if (result != null && result.getErrorCount() > 0) {
			System.out.println("err");

			return "useredit";
		} else {
			// Formの中身をDTOに入れ替える
			EditUserDTO editUserDTO = new EditUserDTO();
			editUserDTO.setLog_id(editUserForm.getUserName());
			editUserDTO.setPassword(editUserForm.getPassword());
			editUserDTO.setRePassword(editUserForm.getRe_password());
			editUserDTO.setUpdate_count(editUserForm.getUpdateCount());

			// ユーザー情報を更新する処理実行(更新時エラーはエクセプションで取得する)
			try {
				userMasterService.userupdate(editUserDTO);
				// メニューを表示
				return "menu";
			} catch (Exception e) {
				System.out.println("Exception");
				errors.rejectValue("updateCount", "VLD004");
				return "useredit";
			}
		}
	}

	// mfa登録・解除画面表示
	@RequestMapping(params = "mfa", method = RequestMethod.POST)
	public String auth(Authentication loginUser, Model model) throws Exception {

		String username = loginUser.getName();

		// MFA認証キー作成（すでにMFA登録済みの場合はDBより取得
		String secret = securityController.getSecret(loginUser);
		if (secret != null) {
			sessionBean.setSecret(secret);

			model.addAttribute("dataUrl", securityController.createQrcodeData(username, secret));

			return "mfaRegistered";
		} else {
			secret = MFAAuthenticatorUtil.generateSecretKey();
			// 登録されていない場合
			sessionBean.setSecret(secret);

			model.addAttribute("dataUrl", securityController.createQrcodeData(username, secret));

			return "mfa";
		}
	}

	// 一覧ページを表示
	@RequestMapping(params = "userlist", method = RequestMethod.POST)
	public String list(Model model, UserListForm userListForm) {

		// DTOにセットする
		UserListDTO userListDTO = new UserListDTO();
		userListDTO.setUserListLimit(Constants.USER_LIST_LIMIT);

		// 一覧表示の結果を取得
		List<UserListDTO> searchResult = userMasterService.userlist(userListDTO);

		// 結果のdtoの中身をbeanに入れ替える
		List<UserListBean> userList = new ArrayList<UserListBean>();

		int i = 0;
		for (i = 0; i < searchResult.size(); i++) {

			UserListBean userListBean = new UserListBean();

			userListBean.setUserName(searchResult.get(i).getLog_id());
			userListBean.setPassword(searchResult.get(i).getPassword());
			userListBean.setCreateDate(searchResult.get(i).getCreate_date());
			userListBean.setUpdateDate(searchResult.get(i).getUpdate_date());
			userList.add(userListBean);
		}

		// ページに結果をセット
		userListForm.setUserList(userList);

		// ページングカウントをセット
		userListForm.setPagingCount(0);

		// 検索結果が１０件未満の時はページングを両方とも無効にする
		if (i < 10) {
			userListForm.setNextPageCheck(0);
		} else {
			userListForm.setNextPageCheck(1);
		}
		userListForm.setBackPageCheck(0);

		// 一覧を表示
		return "userlist";
	}

	// ページング進
	@RequestMapping(params = "pagingNext", method = RequestMethod.POST)
	public String pagingNext(Model model, UserListForm userListForm) {

		// 現在のページングを取得
		int pagingCount = userListForm.getPagingCount() + 1;

		// ページングカウントをセット
		userListForm.setPagingCount(pagingCount);

		// DTOにセットする
		UserListDTO userListDTO = new UserListDTO();
		userListDTO.setPagingCount(pagingCount * 10);
		userListDTO.setUserListLimit(Constants.USER_LIST_LIMIT + 1);

		// 一覧表示の結果を取得
		List<UserListDTO> searchResult = userMasterService.paging(userListDTO);

		// 結果のDTOの中身をbeanに入れ替える
		List<UserListBean> userList = new ArrayList<UserListBean>();
		String next = null;

		int i = 0;
		for (i = 0; i < searchResult.size(); i++) {
			if (i < Constants.USER_LIST_LIMIT) {
				UserListBean userListBean = new UserListBean();
				userListBean.setUserName(searchResult.get(i).getLog_id());
				userListBean.setPassword(searchResult.get(i).getPassword());
				userListBean.setCreateDate(searchResult.get(i).getCreate_date());
				userListBean.setUpdateDate(searchResult.get(i).getUpdate_date());
				userList.add(userListBean);
			}else {
				//次の一つを取得
				next =  searchResult.get(i).getLog_id();
			}
		}

		// ページングの処理
		try {
			// 表示数+1の結果があるかどうか
			searchResult.get(Constants.USER_LIST_LIMIT);
		} catch (IndexOutOfBoundsException e) {
			userListForm.setNextPageCheck(0);
		}
		userListForm.setBackPageCheck(1);

		// ページに結果をセット
		userListForm.setUserList(userList);

		// 一覧を表示
		return "userlist";
	}

	// ページング戻る
	@RequestMapping(params = "pagingBack", method = RequestMethod.POST)
	public String pagingBack(Model model, UserListForm userListForm) {

		// 現在のページングを取得
		int pagingCount = userListForm.getPagingCount() - 1;
		// ページングカウントをセット
		userListForm.setPagingCount(pagingCount);

		// DTOにセット
		UserListDTO userListDTO = new UserListDTO();
		userListDTO.setPagingCount(pagingCount * 10);
		userListDTO.setUserListLimit(Constants.USER_LIST_LIMIT + 1);

		// 一覧表示の結果を取得
		List<UserListDTO> searchResult = userMasterService.paging(userListDTO);

		// 結果のDTOの中身をbeanに入れ替える
		List<UserListBean> userList = new ArrayList<UserListBean>();

		for (int i = 0; i < searchResult.size() - 1; i++) {
			UserListBean userListBean = new UserListBean();
			userListBean.setUserName(searchResult.get(i).getLog_id());
			userListBean.setPassword(searchResult.get(i).getPassword());
			userListBean.setCreateDate(searchResult.get(i).getCreate_date());
			userListBean.setUpdateDate(searchResult.get(i).getUpdate_date());
			userList.add(userListBean);
		}

		if (pagingCount == 0) {
			userListForm.setBackPageCheck(0);
			userListForm.setNextPageCheck(1);
		} else {
			userListForm.setBackPageCheck(1);
			userListForm.setNextPageCheck(1);
		}

		// ページに結果をセット
		userListForm.setUserList(userList);

		// 一覧を表示
		return "userlist";
	}

	// メニューに戻る
	@RequestMapping(params = "back", method = RequestMethod.POST)
	public String back() {
		// メニューを表示
		return "menu";
	}
}