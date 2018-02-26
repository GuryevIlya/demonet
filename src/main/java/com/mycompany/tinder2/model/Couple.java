package com.mycompany.tinder2.model;

/**
 *
 * @author delet
 */
public class Couple {
    private User user1;
    private User user2;
    private int pending1;
    private int pending2;

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
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
