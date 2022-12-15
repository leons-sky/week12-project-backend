package us.group14.backend.constants;

public enum ApiCookie {
    AUTH_COOKIE("auth_token");

    private final String cookieName;

    ApiCookie(String cookieName) {
        this.cookieName = cookieName;
    }

    public String get() {
        return this.cookieName;
    }

    @Override
    public String toString() {
        return this.cookieName;
    }
}
