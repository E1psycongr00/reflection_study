package fields.deserialize.config;

public class UserInterfaceConfig {

    private String titleColor;
    private String footerText;
    private short titleFontSize;
    private short footerFontSIze;

    public String getTitleColor() {
        return titleColor;
    }

    public String getFooterText() {
        return footerText;
    }

    public short getTitleFontSize() {
        return titleFontSize;
    }

    public short getFooterFontSIze() {
        return footerFontSIze;
    }

    @Override
    public String toString() {
        return "UserInterfaceConfig{" +
            "titleColor='" + titleColor + '\'' +
            ", footerText='" + footerText + '\'' +
            ", titleFontSize=" + titleFontSize +
            ", footerFontSIze=" + footerFontSIze +
            '}';
    }
}
