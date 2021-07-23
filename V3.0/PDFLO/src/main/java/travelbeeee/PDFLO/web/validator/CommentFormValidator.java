package travelbeeee.PDFLO.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import travelbeeee.PDFLO.web.form.CommentForm;

@Component
public class CommentFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CommentForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CommentForm form = (CommentForm) target;
        if(form.getComment() == null || form.getComment().isEmpty()){
            errors.rejectValue("comment", "NotEmpty", "필수 입력 값입니다.");
        }
        if(form.getScore() == null){
            errors.rejectValue("score", "NotEmpty", "필수 입력 값입니다.");
        }
        if(form.getScore() != null && !(1.0 <= form.getScore() && form.getScore() <= 5.0)){
            errors.rejectValue("score", "Range");
        }
    }
}
