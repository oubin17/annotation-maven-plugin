package org.example.request;

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


    /**
     *
     * <div style="display:none">start validator</div>
     * <ul>
     *     <li>Not Null</li>
     *     <li>Not Blank</li>
     *     <li>value >= 10000, message = ipRoleId not null</li>
     * </ul>
     * <div style="display:none">end validator</div>
     */
    @NotNull
    @NotBlank
    @Min(value = 10000, message = "ipRoleId not null")
    private String ipRoleId;

    /**
     *
     * <div style="display:none">start validator</div>
     * <ul>
     *     <li>value >= 2311, message = 1</li>
     * </ul>
     * <div style="display:none">end validator</div>
     */
    @Min(value = 2311, message = "1")
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
