package com.mycompany.tinder2.model.internal;

/**
 *
 * @author delet
 */
public class Stat {
    private Integer userId1;
    private Integer userId2;
    private Integer commonFriendCount;
    private Double compatibility;

    public Integer getUserId1() {
        return userId1;
    }

    public Integer getUserId2() {
        return userId2;
    }

    public Integer getCommonFriendCount() {
        return commonFriendCount;
    }

    public Double getCompatibility() {
        return compatibility;
    }

    public void setCommonFriendCount(Integer commonFriendCount) {
        this.commonFriendCount = commonFriendCount;
    }

    public void setCompatibility(Double compatibility) {
        this.compatibility = compatibility;
    }

    public void setUserId1(Integer userId1) {
        this.userId1 = userId1;
    }

    public void setUserId2(Integer userId2) {
        this.userId2 = userId2;
    }
    
}
