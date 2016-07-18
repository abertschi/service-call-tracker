package ch.abertschi.sct.domain;

/**
 * Created by abertschi on 18/07/16.
 */
public class SuperCustomer extends Customer
{
    private String nickname;

    public SuperCustomer(String name, String comment)
    {
        super(name, comment);
    }

    public String getNickname()
    {
        return nickname;
    }

    public SuperCustomer setNickname(String nickname)
    {
        this.nickname = nickname;
        return this;
    }
}
