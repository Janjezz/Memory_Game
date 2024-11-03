package aleksa.janjic.memorygame;

import java.io.Serializable;

public class Element implements Serializable{
    private String mIme;
    private String mRezultatB;
    private String mEmail;
    private String mRezultatW;
    private String mId;
    private String mRezultat;

    public Element(String mIme, String mEmail, String mRezultat, String mId){
        this.mIme = mIme;
        this.mEmail = mEmail;
        this.mRezultat = mRezultat;
        this.mId = mId;
    }
    public Element(String mIme, String mEmail, String mRezultatB, String mRezultatW, boolean b) {
        this.mIme = mIme;
        this.mEmail = mEmail;
        this.mRezultatB = mRezultatB;
        this.mRezultatW = mRezultatW;
    }
    public String getmIme() {
        return mIme;
    }

    public String getmRezultatB() {
        return mRezultatB;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmRezultatW() {
        return mRezultatW;
    }

    public String getmId() {
        return mId;
    }

    public String getmRezultat() {
        return mRezultat;
    }

    public void setmRezultat(String mRezultat) {
        this.mRezultat = mRezultat;
    }
}

