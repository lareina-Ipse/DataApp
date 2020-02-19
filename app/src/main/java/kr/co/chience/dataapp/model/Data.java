package kr.co.chience.dataapp.model;

public class Data {

    private String cdc, mic, voc, co2, temp, att, humInt, humDec;

    public Data(String cdc, String mic, String voc, String co2,
                String temp, String att, String humInt, String humDec) {
        this.cdc = cdc;
        this.mic = mic;
        this.voc = voc;
        this.co2 = co2;
        this.temp = temp;
        this.att = att;
        this.humInt = humInt;
        this.humDec = humDec;
    }

    public String getCdc() {
        return cdc;
    }

    public void setCdc(String cdc) {
        this.cdc = cdc;
    }

    public String getMic() {
        return mic;
    }

    public void setMic(String mic) {
        this.mic = mic;
    }

    public String getVoc() {
        return voc;
    }

    public void setVoc(String voc) {
        this.voc = voc;
    }

    public String getCo2() {
        return co2;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getAtt() {
        return att;
    }

    public void setAtt(String att) {
        this.att = att;
    }

    public String getHumInt() {
        return humInt;
    }

    public void setHumInt(String humInt) {
        this.humInt = humInt;
    }

    public String getHumDec() {
        return humDec;
    }

    public void setHumDec(String humDec) {
        this.humDec = humDec;
    }
}
