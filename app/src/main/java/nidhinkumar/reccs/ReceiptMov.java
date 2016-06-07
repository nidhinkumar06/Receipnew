package nidhinkumar.reccs;

/**
 * Created by M.S. Venugopal on 03-06-2016.
 */

public class ReceiptMov {
    private String merchantname, merdate, meramount, merpaid, mercategory, mecomment;
    byte[] mimage;

    public ReceiptMov() {

    }

    public ReceiptMov(byte[] mimage, String merchantname,  String mecomment, String mercategory, String merpaid, String meramount, String merdate) {
        this.merchantname = merchantname;
        this.mimage = mimage;
        this.mecomment = mecomment;
        this.mercategory = mercategory;
        this.merpaid = merpaid;
        this.meramount = meramount;
        this.merdate = merdate;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public byte[] getMimage() {
        return mimage;
    }

    public void setMimage(byte[] mimage) {
        this.mimage = mimage;
    }

    public String getMecomment() {
        return mecomment;
    }

    public void setMecomment(String mecomment) {
        this.mecomment = mecomment;
    }

    public String getMerpaid() {
        return merpaid;
    }

    public void setMerpaid(String merpaid) {
        this.merpaid = merpaid;
    }

    public String getMerdate() {
        return merdate;
    }

    public void setMerdate(String merdate) {
        this.merdate = merdate;
    }

    public String getMeramount() {
        return meramount;
    }

    public void setMeramount(String meramount) {
        this.meramount = meramount;
    }

    public String getMercategory() {
        return mercategory;
    }

    public void setMercategory(String mercategory) {
        this.mercategory = mercategory;
    }
}
