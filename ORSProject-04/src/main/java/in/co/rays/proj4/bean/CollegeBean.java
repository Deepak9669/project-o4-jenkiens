package in.co.rays.proj4.bean;

// Log4j 1.2.17 import
import org.apache.log4j.Logger;

/**
 * CollegeBean represents the College entity.
 * <p>
 * It extends {@link BaseBean} and contains college-related details like
 * name, address, state, city and phone number.
 * It also implements dropdown key-value mapping.
 *
 * @author Deepak
 * @version 1.0
 */
public class CollegeBean extends BaseBean {

    // Log4j 1.2.17 Logger (NO logic change)
    private static final Logger log = Logger.getLogger(CollegeBean.class);

    private String name;
    private String address;
    private String state;
    private String city;
    private String phoneNo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String getKey() {
        return String.valueOf(id);
    }

    @Override
    public String getValue() {
        return name;
    }
}
