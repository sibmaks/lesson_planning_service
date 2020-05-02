package xyz.dma.soft.api.entity;

public enum ApiResultCode {
    OK("Ok"),
    USER_NOT_FOUND("UserNotFound"),
    ROLE_NOT_FOUND("RoleNotFound"),
    COURSE_NOT_FOUND("CourseNotFound"),
    NOT_FOUND("NotFound"),
    UNAUTHORIZED("Unauthorized"),
    PAGE_NOT_FOUND("PageNotFound"),
    NOT_ALLOWED("NotAllowed"),
    CONSTRAINT_EXCEPTION("ConstraintException"),
    ALREADY_EXISTS("AlreadyExists"),
    CHILD_NOT_EXISTS("ChildNOtExists"),
    PASSWORD_MISMATCH("PasswordMismatch"),
    ROLE_ALREADY_GRANTED("RoleAlreadyGranted"),
    NOT_EXISTS("NotExists"),
    UNEXPECTED_ERROR("UnexpectedError");

    final String code;

    ApiResultCode(String code) {
        this.code = code;
    }

    public static ApiResultCode get(String code) {
        for(ApiResultCode apiResultCode : values()) {
            if(apiResultCode.code.equals(code)) {
                return apiResultCode;
            }
        }
        return null;
    }
}
