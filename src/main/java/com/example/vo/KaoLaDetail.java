package com.example.vo;


import com.example.configuration.ApplicationConstants;

/**
 * Created by zhuzhihang on 2016/12/13.
 */
public class KaoLaDetail extends Crawler {
    //是否自营
    private boolean self;

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public void update() {
        if (self) {
            this.setShopType(ApplicationConstants.ShopType.SELF.toString());
        } else {
            this.setShopType(ApplicationConstants.ShopType.THIRD_PARTY.toString());
        }
    }
}
