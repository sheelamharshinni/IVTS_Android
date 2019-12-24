package com.tecdatum.Tracking.School.newhelpers;



public class Monthdayreportitems {
    String tv_C_psname;


    String tv_vehcle, tv_date, tv_ST, tv_ET, tv_WDT, tv_ID, tv_SD, tv_inD, tv_MS, tv_AS, tv_D;


    public Monthdayreportitems(String tv_vehcle, String tv_date, String tv_st, String tv_ET, String tv_WDT, String tv_ID, String tv_SD, String tv_inD, String tv_MS, String tv_AS, String tv_D) {
        this.tv_vehcle = tv_vehcle;

    }


    public Monthdayreportitems(String tv_date, String tv_WDT, String tv_MS, String tv_D) {
        this.tv_date = tv_date;
        this.tv_WDT = tv_WDT;
        this.tv_MS = tv_MS;
        this.tv_D = tv_D;
    }


    public String getTv_C_psname() {
        return tv_C_psname;
    }

    public void setTv_C_psname(String tv_C_psname) {
        this.tv_C_psname = tv_C_psname;
    }

    public String getTv_date() {
        return tv_date;
    }

    public void setTv_date(String tv_date) {
        this.tv_date = tv_date;
    }

    public String getTv_ST() {
        return tv_ST;
    }

    public void setTv_ST(String tv_ST) {
        this.tv_ST = tv_ST;
    }

    public String getTv_ET() {
        return tv_ET;
    }

    public void setTv_ET(String tv_ET) {
        this.tv_ET = tv_ET;
    }

    public String getTv_WDT() {
        return tv_WDT;
    }

    public void setTv_WDT(String tv_WDT) {
        this.tv_WDT = tv_WDT;
    }

    public String getTv_ID() {
        return tv_ID;
    }

    public void setTv_ID(String tv_ID) {
        this.tv_ID = tv_ID;
    }

    public String getTv_SD() {
        return tv_SD;
    }

    public void setTv_SD(String tv_SD) {
        this.tv_SD = tv_SD;
    }

    public String getTv_inD() {
        return tv_inD;
    }

    public void setTv_inD(String tv_inD) {
        this.tv_inD = tv_inD;
    }

    public String getTv_MS() {
        return tv_MS;
    }

    public void setTv_MS(String tv_MS) {
        this.tv_MS = tv_MS;
    }

    public String getTv_AS() {
        return tv_AS;
    }

    public void setTv_AS(String tv_AS) {
        this.tv_AS = tv_AS;
    }

    public String getTv_D() {
        return tv_D;
    }

    public void setTv_D(String tv_D) {
        this.tv_D = tv_D;
    }

    public String getTv_vehcle() {
        return tv_vehcle;
    }

    public void setTv_vehcle(String tv_vehcle) {
        this.tv_vehcle = tv_vehcle;
    }

    public String toString() {
        return tv_date;
    }


}
