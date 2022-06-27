package org.example;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *     QueryMemberRequest
 * </p>
 *
 * @author oubin.ob
 * @version : QueryMemberRequest.java v 0.1 2022/6/17 16:54 oubin.ob Exp $$
 */
public class QueryMemberRequest {


//    /**
//     * {@value STATIC_STRING}
//     */
//    private static final String STATIC_STRING = "static_string";



    @NotNull
    @NotBlank
    @Min(value = 10, message = "1")
    private String ipRoleId;


    @Min(value = 23, message = "11")
    private String type;

    /**
     * Getter method for property <tt>ipRoleId</tt>.
     *
     * @return property value of ipRoleId
     */
    public String getIpRoleId() {
        return ipRoleId;
    }

    /**
     * Setter method for property <tt>ipRoleId</tt>.
     *
     * @param ipRoleId value to be assigned to property ipRoleId
     */
    public void setIpRoleId(String ipRoleId) {
        this.ipRoleId = ipRoleId;
    }

    /**
     * Getter method for property <tt>type</tt>.
     *
     * @return property value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     *
     * @param type value to be assigned to property type
     */
    public void setType(String type) {
        this.type = type;
    }
}
