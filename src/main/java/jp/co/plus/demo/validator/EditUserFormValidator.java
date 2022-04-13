
package jp.co.plus.demo.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import jp.co.plus.demo.form.EditUserForm;

@Component
public class EditUserFormValidator extends BaseValidator<EditUserForm> {

	@Override
	protected void doValidate(EditUserForm form, Errors errors) {

		// パスワードのチェック
		if (!form.getPassword().isEmpty() && !form.getRe_password().isEmpty()) {
			// パスワードが一致してるかチェック
			if (!form.getPassword().equals(form.getRe_password())) {
				// パスワードが一致していません。
				errors.rejectValue("password", "VLD001");
			}
			// 桁数チェック
			if (form.getPassword().length() > 20) {
				// 桁数以内で入力してください。
				errors.rejectValue("password", "VLD002");
			}

			// パスワードがどちらか入力されていない場合
		} else {
			// パスワードを入力してください。
			errors.rejectValue("password", "VLD003");
		}

	}
}
