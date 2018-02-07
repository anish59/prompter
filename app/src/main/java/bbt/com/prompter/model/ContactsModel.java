package bbt.com.prompter.model;

/**
 * Created by anish on 09-01-2018.
 */

public class ContactsModel {
    String Name;
    String Number;
    String imgUri;

    public ContactsModel(String name, String number, String imgUri) {
        Name = name;
        Number = number;
        this.imgUri = imgUri;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }



}
