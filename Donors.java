package kazinazrul.csedu24.badhon_knih;

public class Donors {
    public String  date, profileimage, fullname;

    public Donors(){
    }


    public Donors(String date, String profileimage, String fullname) {
        this.date = date;
        this.profileimage = profileimage;
        this.fullname = fullname;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getDate() {
        return date;
    }



    public String getProfileimage() {
        return profileimage;
    }



    public String getFullname() {
        return fullname;
    }


}
