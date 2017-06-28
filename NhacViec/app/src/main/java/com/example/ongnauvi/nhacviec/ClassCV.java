package com.example.ongnauvi.nhacviec;

/**
 * Created by OngNauVi on 03/06/2017.
 */

public class ClassCV {
    private int Macv;
    private String Tencv;
    private String Mota;
    private String Ngay;
    private String Gio;
    private String Kieulaplai;
    private String Trangthai;

    public ClassCV() {
    }

    public ClassCV(int macv, String tencv, String mota, String ngay, String gio, String kieulaplai, String trangthai) {
        Macv = macv;
        Tencv = tencv;
        Mota = mota;
        Ngay = ngay;
        Gio = gio;
        Kieulaplai = kieulaplai;
        Trangthai = trangthai;
    }

    public ClassCV(String tencv, String mota, String ngay, String gio, String kieulaplai, String trangthai) {
        Tencv = tencv;
        Mota = mota;
        Ngay = ngay;
        Gio = gio;
        Kieulaplai = kieulaplai;
        Trangthai = trangthai;
    }

    public int getMacv() {
        return Macv;
    }

    public void setMacv(int macv) {
        Macv = macv;
    }

    public String getTencv() {
        return Tencv;
    }

    public void setTencv(String tencv) {
        Tencv = tencv;
    }

    public String getMota() {
        return Mota;
    }

    public void setMota(String mota) {
        Mota = mota;
    }

    public String getNgay() {
        return Ngay;
    }

    public void setNgay(String ngay) {
        Ngay = ngay;
    }

    public String getGio() {
        return Gio;
    }

    public void setGio(String gio) {
        Gio = gio;
    }

    public String getKieulaplai() {
        return Kieulaplai;
    }

    public void setKieulaplai(String kieulaplai) {
        Kieulaplai = kieulaplai;
    }

    public String getTrangthai() {
        return Trangthai;
    }

    public void setTrangthai(String trangthai) {
        Trangthai = trangthai;
    }
}
