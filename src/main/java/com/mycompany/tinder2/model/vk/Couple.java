package com.mycompany.tinder2.model.vk;

/**
 *
 * @author delet
 */
public class Couple {
    private UserVK user1;
    private UserVK user2;
    private int pending1;
    private int pending2;

    public UserVK getUser1() {
        return user1;
    }

    public UserVK getUser2() {
        return user2;
    }

    public void setUser1(UserVK user1) {
        this.user1 = user1;
    }

    public void setUser2(UserVK user2) {
        this.user2 = user2;
    }

    public int getPending1() {
        return pending1;
    }

    public int getPending2() {
        return pending2;
    }

    public void setPending1(int pending1) {
        this.pending1 = pending1;
    }

    public void setPending2(int pending2) {
        this.pending2 = pending2;
    }
    
}
