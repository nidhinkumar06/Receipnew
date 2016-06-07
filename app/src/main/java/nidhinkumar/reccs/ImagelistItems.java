package nidhinkumar.reccs;

/**
 * Created by M.S. Venugopal on 21-05-2016.
 */
public class ImagelistItems {
    byte[] image;
    String merchantname;
    String paidon;
    String status;
    String amount;
    String category;
    String paymmode;

    public ImagelistItems(byte[] image, String merchantname, String paidon, String amount, String category, String paymmode, String comment) {
        this.image = image;
        this.merchantname = merchantname;
        this.paidon = paidon;
        this.amount = amount;
        this.category = category;
        this.paymmode = paymmode;
        this.comment = comment;
    }

    String comment;

    public ImagelistItems() {
    }

    public ImagelistItems(byte[] image, String merchantname, String paidon, String status, String amount, String category, String paymmode, String comment) {
        this.image = image;
        this.merchantname = merchantname;
        this.paidon = paidon;
        this.status = status;
        this.amount = amount;
        this.category = category;
        this.paymmode = paymmode;
        this.comment = comment;
    }

    public byte[] getImage(){
        return image;
    }
    public void setImage(byte[] image){
        this.image=image;
    }
    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public String getPaidon() {
        return paidon;
    }

    public void setPaidon(String paidon) {
        this.paidon = paidon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getCategory(){
        return category;
    }
    public void setCategory(String category){
        this.category=category;
    }
    public String getPaymmode(){
        return paymmode;
    }
    public void setPaymmode(String paymmode){
        this.paymmode=paymmode;
    }
    public String getComment(){
        return comment;
    }
    public void setComment(String comment){
        this.comment=comment;
    }
}
