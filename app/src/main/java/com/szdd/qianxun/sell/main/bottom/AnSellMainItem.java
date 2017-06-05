package com.szdd.qianxun.sell.main.bottom;

import java.util.List;

public class AnSellMainItem {

    /**
     * reward_money : 5
     * detail : 兔兔
     * status : 0
     * uesrname : 0
     * serviceType : 3
     * reward_unit : 次
     * id : 32
     * headIcon :
     * time : 1460711016586
     * reward_thing :
     * great : 0
     * nickName :
     * favoriteNumber : 0
     * name : 哦哦哦
     * images : [{"service":null,"id":110,"image":null,"link":"/qianxun/businessService/small/1460711021899.jpg"},{"service":null,"id":111,"image":null,"link":"/qianxun/businessService/small/1460711021788.jpg"},{"service":null,"id":109,"image":null,"link":"/qianxun/businessService/small/1460711021672.jpg"},{"service":null,"id":107,"image":null,"link":"/qianxun/businessService/small/1460711021358.jpg"},{"service":null,"id":108,"image":null,"link":"/qianxun/businessService/small/1460711021559.jpg"},{"service":null,"id":106,"image":null,"link":"/qianxun/businessService/small/1460711021457.jpg"},{"service":null,"id":104,"image":null,"link":"/qianxun/businessService/small/1460711021130.jpg"},{"service":null,"id":105,"image":null,"link":"/qianxun/businessService/small/1460711021243.jpg"},{"service":null,"id":103,"image":null,"link":"/qianxun/businessService/small/1460711020859.jpg"}]
     * user : {"id":5,"headIcon":"/qianxun/headIcon/small/1478340136605.jpg","gender":"男","username":17865169986,"verifyStatus":0,"nickName":"冷暖自知"}
     */

    private String reward_money;
    private String detail;
    private int status;
    private int uesrname;
    private int serviceType;
    private String reward_unit;
    private int id;
    private String headIcon;
    private String time;
    private String reward_thing;
    private int great;
    private String nickName;
    private int favoriteNumber;
    private String name;
    /**
     * id : 5
     * headIcon : /qianxun/headIcon/small/1478340136605.jpg
     * gender : 男
     * username : 17865169986
     * verifyStatus : 0
     * nickName : 冷暖自知
     */

    private UserBean user;
    /**
     * service : null
     * id : 110
     * image : null
     * link : /qianxun/businessService/small/1460711021899.jpg
     */

    private List<ImagesBean> images;

    public String getReward_money() {
        return reward_money;
    }

    public void setReward_money(String reward_money) {
        this.reward_money = reward_money;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUesrname() {
        return uesrname;
    }

    public void setUesrname(int uesrname) {
        this.uesrname = uesrname;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getReward_unit() {
        return reward_unit;
    }

    public void setReward_unit(String reward_unit) {
        this.reward_unit = reward_unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReward_thing() {
        return reward_thing;
    }

    public void setReward_thing(String reward_thing) {
        this.reward_thing = reward_thing;
    }

    public int getGreat() {
        return great;
    }

    public void setGreat(int great) {
        this.great = great;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getFavoriteNumber() {
        return favoriteNumber;
    }

    public void setFavoriteNumber(int favoriteNumber) {
        this.favoriteNumber = favoriteNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public static class UserBean {
        private int id;
        private String headIcon;
        private String gender;
        private long username;
        private int verifyStatus;
        private String nickName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHeadIcon() {
            return headIcon;
        }

        public void setHeadIcon(String headIcon) {
            this.headIcon = headIcon;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public long getUsername() {
            return username;
        }

        public void setUsername(long username) {
            this.username = username;
        }

        public int getVerifyStatus() {
            return verifyStatus;
        }

        public void setVerifyStatus(int verifyStatus) {
            this.verifyStatus = verifyStatus;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }

    public static class ImagesBean {
        private Object service;
        private int id;
        private Object image;
        private String link;

        public Object getService() {
            return service;
        }

        public void setService(Object service) {
            this.service = service;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getImage() {
            return image;
        }

        public void setImage(Object image) {
            this.image = image;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
