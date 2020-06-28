package model;

import java.io.*;

public class ActionData implements Serializable {
    private long eventTime;
    private String timeString;
    private String token;
    private String userId;
    private Boolean isShow = false;
    private Boolean isClick = false;
    private String showTime;
    private String clickTime;

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public Boolean getClick() {
        return isClick;
    }

    public void setClick(Boolean click) {
        isClick = click;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getClickTime() {
        return clickTime;
    }

    public void setClickTime(String clickTime) {
        this.clickTime = clickTime;
    }

    @Override
    public String toString() {
        return "ActionData{" +
                "timeString='" + timeString + '\'' +
                ", userId='" + userId + '\'' +
                ", isShow=" + isShow +
                ", isClick=" + isClick +
                ", showTime='" + showTime + '\'' +
                ", clickTime='" + clickTime + '\'' +
                '}';
    }

    /**
     * 对象转数组
     * @param obj
     * @return
     */
    public byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 数组转对象
     * @param bytes
     * @return
     */
    public ActionData toObject (byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return (ActionData)obj;
    }
}
