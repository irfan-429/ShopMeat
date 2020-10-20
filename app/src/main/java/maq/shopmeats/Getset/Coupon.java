package maq.shopmeats.Getset;

public class Coupon {

    String code,type, value, mincartvalue,userLimit, fromdate, todate;

    public Coupon(String code, String type, String value, String mincartvalue, String userLimit, String fromdate, String todate) {
        this.code = code;
        this.type = type;
        this.value = value;
        this.mincartvalue = mincartvalue;
        this.userLimit = userLimit;
        this.fromdate = fromdate;
        this.todate = todate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMincartvalue() {
        return mincartvalue;
    }

    public void setMincartvalue(String mincartvalue) {
        this.mincartvalue = mincartvalue;
    }

    public String getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(String userLimit) {
        this.userLimit = userLimit;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }
}
