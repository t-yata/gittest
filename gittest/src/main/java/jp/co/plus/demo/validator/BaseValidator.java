package jp.co.plus.demo.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class BaseValidator<T> implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(final Object target, final Errors errors) {
        try {
            boolean hasErrors = errors.hasErrors();

            if (!hasErrors) {
                // 各機能で実装しているバリデーションを実行する
                doValidate((T) target, errors);
            }
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * 入力チェックを実施します。
     *
     * @param form
     * @param errors
     */
    protected abstract void doValidate(final T form, final Errors errors);


}
